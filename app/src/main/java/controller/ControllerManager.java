package controller;

import controller.dto.ContractDto;
import controller.dto.ItemDto;
import controller.dto.MemberDto;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import model.Contract;
import model.Item;
import model.ItemCatagory;
import model.Member;
import model.TimeManager;

/**
 * Main controller, manages interaction between the other controllers
 * and the Model and View layers.
 */
public class ControllerManager {
  private final MemberManager memberManager;
  private final ItemManager itemManager;
  private final ContractManager contractManager;
  private final TimeManager timeManager;

  /**
   * Start an instance with hardcoded data.
   */
  public static ControllerManager createWithHardCodedData() {
    TimeManager tm = new TimeManager();
    MemberManager mm = new MemberManager();
    ItemManager im = new ItemManager(mm, tm);
    ContractManager cm = new ContractManager(mm, im, tm);

    ControllerManager controller = new ControllerManager(mm, im, cm, tm);
    HardCodedPersistenceManager loader = new HardCodedPersistenceManager(controller);
    loader.loadSampleData();
    return controller;
  }

  /**
   * Constructor.
   *
   * @param mm    --> MemberManager
   * @param it    --> ItemManager
   * @param cm    --> ContractManager
   * @param tm    --> TimeManager
   */
  ControllerManager(MemberManager mm, ItemManager it, ContractManager cm, TimeManager tm) {
    this.memberManager = mm;
    this.itemManager = it;
    this.contractManager = cm;
    this.timeManager = tm;
  }

  /* ==== === ==== ==== === ==== */
  /* ==== Member operations ==== */
  /* ==== === ==== ==== === ==== */
  public Member createMember(String name, String email, String phone) {
    return memberManager.createMember(name, email, phone, timeManager.getCurrentDay());
  }

  public MemberDto getMemberDtoById(String id) {
    Member member = memberManager.getMemberById(id);
    return (member != null) ? new MemberDto(member) : null;
  }

  /**
   * rRead-only acces for View.
   *
   * @param memberId    --> That the items belong to.
   * @return            --> The items, assuming there are any.
   */
  public Collection<ItemDto> getOwnedItemsForMember(String memberId) {
    Member member = memberManager.getMemberById(memberId);
    if (member == null) { 
      return Collections.emptyList();
    }
    return member.getOwnedItems().stream()
        .map(ItemDto::new)
        .collect(Collectors.toList());
  }

  /**
   * Getter for DTO members.
   *
   * @return    --> Dto.
   */
  public Collection<MemberDto> listMembers() {
    return memberManager.listMembers().stream()
        .map(MemberDto::new)
        .collect(Collectors.toList());
  }

  public Member getMemberById(String id) {
    return memberManager.getMemberById(id);
  }

  public void updateMember(String id, String newName, String newEmail, String newPhone) {
    memberManager.updateMemberInfo(id, newName, newEmail, newPhone);
  }

  public void deleteMember(String id) {
    memberManager.deleteMember(id);
  }

  /* === === ==== ==== === === */
  /* ==== Item operations ==== */
  /* === === ==== ==== === === */
  public Item getItemById(String id) {
    return itemManager.getItemById(id);
  }

  public ItemDto getItemDtoById(String id) {
    Item item = itemManager.getItemById(id);
    return (item != null) ? new ItemDto(item) : null;
  }

  public Item createItem(String ownerId, ItemCatagory category, String name, String desc, int costPerDay) {
    return itemManager.createItem(ownerId, category, name, desc, costPerDay);
  }

  /**
   * Getter for DTO Items.
   *
   * @return    --> Dto.
   */
  public Collection<ItemDto> listItems() {
    return itemManager.listitems().stream()
        .map(ItemDto::new)
        .collect(Collectors.toList());
  }

  public void updateItemInfo(String id, String newName, String newDesc, Integer newCost) {
    itemManager.updateItemInfo(id, newName, newDesc, newCost);
  }

  public void deleteItem(String id) {
    itemManager.deleateItem(id);
  }

  /* ==== ==== ==== ==== ==== ==== */
  /* ==== Contract operations ==== */
  /* ==== ==== ==== ==== ==== ==== */
  public Contract createContract(String borrowerId, String itemId, int startDay, int endDay) {
    return contractManager.createContract(borrowerId, itemId, startDay, endDay);
  }

  public ContractDto createContractDtoAndGeDto(String borrowerId, String itemId, int startDay, int endDay) {
    Contract c = createContract(borrowerId, itemId, startDay, endDay);
    return new ContractDto(c, timeManager.getCurrentDay());
  }

  /**
   * Getter for DTO contracts.
   *
   * @return    --> Dto.
   */
  public Collection<ContractDto> listContracts() {
    return contractManager.listContracts().stream()
        .map(c -> new ContractDto(
          c.getContractId(),
          c.getItem().getName(),
          c.getBorrower().getName(),
          c.getStartDay(),
          c.getEndDay(),
          c.statusAt(timeManager.getCurrentDay())
        ))
        .collect(Collectors.toList());
  }

  /**
   * Return a read-only view of contracts for items.
   *
   * @param itemId    --> To check contracts of.
   * @return          --> The contracts.
   */
  public Collection<ContractDto> getContractsForItem(String itemId) {
    Item item = itemManager.getItemById(itemId);
    if (item == null) {
      return Collections.emptyList();
    }
    return item.getContracts().stream()
        .map(c -> new ContractDto(
          c.getContractId(),
          c.getItem().getName(),
          c.getBorrower().getName(),
          c.getStartDay(),
          c.getEndDay(),
          c.statusAt(timeManager.getCurrentDay())
        ))
        .collect(Collectors.toList());
  }

  /* ==== === === === === ==== */
  /* ==== Time operations ==== */
  /* ==== === === === === ==== */
  public int getCurrentDay() {
    return timeManager.getCurrentDay();
  }

  public void advanceDay(int days) {
    timeManager.advanceDay(days);
    contractManager.cleanUpExpiredContracts(timeManager.getCurrentDay());
  }
}
