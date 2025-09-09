import static org.junit.jupiter.api.Assertions.*;

import controller.ControllerManager;
import controller.dto.*;
import model.Item;
import model.ItemCatagory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class LendingSystemTests {

  private ControllerManager controller;
  private MemberDto m1, m2, m3;
  private ItemDto i1, i2;

  @BeforeEach
  public void setup() {
    controller = ControllerManager.createWithHardCodedData();

    m1 = controller.listMembers().stream().filter(m -> m.getName().equals("Alice")).findFirst().orElseThrow();
    m2 = controller.listMembers().stream().filter(m -> m.getName().equals("Bob")).findFirst().orElseThrow();
    m3 = controller.listMembers().stream().filter(m -> m.getName().contains("Moronica")).findFirst().orElseThrow();

    i1 = controller.getOwnedItemsForMember(m1.getMemberId())
        .stream().filter(i -> i.getName().equals("I1")).findFirst().orElseThrow();
    i2 = controller.getOwnedItemsForMember(m1.getMemberId())
        .stream().filter(i -> i.getName().equals("I2")).findFirst().orElseThrow();
  }

  // 5.1 Member Data
  @Test
  public void testMemberData() {
    assertTrue(controller.listMembers().size() >= 3);
    assertEquals(500, m1.getCredits());
    assertEquals(2, controller.getOwnedItemsForMember(m1.getMemberId()).size());
    assertEquals(50, i1.getCostPerDay());
    assertEquals(10, i2.getCostPerDay());

    assertEquals(100, m2.getCredits());
    assertTrue(controller.getOwnedItemsForMember(m2.getMemberId()).isEmpty());

    List<ContractDto> contractsForI2 = controller.getContractsForItem(i2.getId()).stream().toList();
    assertEquals(1, contractsForI2.size());
    ContractDto c = contractsForI2.get(0);
    assertEquals("I2", c.itemName);
    assertEquals(m3.getName(), c.borrowerName);
    assertEquals(5, c.startDay);
    assertEquals(7, c.endDay);
  }

  // 1.1 Create Member
  @Test
  public void testCreateMember() {
    MemberDto newM = new MemberDto(controller.createMember("Allan Turing", "allan@enigma.com", "123456"));
    assertNotNull(newM.getMemberId());
    assertEquals("Allan Turing", newM.getName());
    assertEquals("allan@enigma.com", newM.getEmail());
  }

  // 1.2 Duplicate Email/Phone
  @Test
  public void testDuplicateEmailAndPhone() {
    controller.createMember("Allan", "allan@enigma.com", "123456");
    assertThrows(IllegalArgumentException.class,
        () -> controller.createMember("Turing", "allan@enigma.com", "123"));
    assertThrows(IllegalArgumentException.class,
        () -> controller.createMember("Turing", "turing@enigma.com", "123456"));
    MemberDto m = new MemberDto(controller.createMember("Turing", "turing@enigma.com", "123"));
    assertNotNull(m.getMemberId());
  }

  // 1.3 Delete Member
  @Test
  public void testDeleteMember() {
    MemberDto m = new MemberDto(controller.createMember("Allan", "allan@enigma.com", "123456"));
    String id = m.getMemberId();
    controller.deleteMember(id);
    assertTrue(controller.listMembers().stream().noneMatch(mem -> mem.getMemberId().equals(id)));
  }

  // 2.1 Create Item
  @Test
  public void testCreateItem() {
    int creditsBefore = m1.getCredits();
    Item item = controller.createItem(m1.getMemberId(), ItemCatagory.Game, "Chess Set", "A board game", 5);
    assertTrue(controller.getOwnedItemsForMember(m1.getMemberId()).stream().anyMatch(i -> i.getId().equals(item.getId())));
    assertEquals(creditsBefore + 100, controller.getMemberDtoById(m1.getMemberId()).getCredits());
  }

  // 2.2 Delete Item (not in contract)
  @Test
  public void testDeleteItemNotInContract() {
    Item freeItem = controller.createItem(m1.getMemberId(), ItemCatagory.Other, "Spare", "Unused", 1);
    controller.deleteItem(freeItem.getId());
    assertTrue(controller.getOwnedItemsForMember(m1.getMemberId()).stream().noneMatch(i -> i.getId().equals(freeItem.getId())));
  }

  // 3.1 Create Contract
  @Test
  public void testCreateContract() {
    ContractDto c = new ContractDto(controller.createContract(m2.getMemberId(), i2.getId(), 1, 4), controller.getCurrentDay());
    assertNotNull(c.id);
  }

  // 3.2 Not Enough Funds
  @Test
  public void testContractNotEnoughFunds() {
    assertThrows(IllegalArgumentException.class,
        () -> controller.createContract(m2.getMemberId(), i1.getId(), 1, 4));
  }

  // 3.3–3.6 Conflicting Times
  @Test
  public void testContractConflicts() {
    assertThrows(IllegalArgumentException.class,
        () -> controller.createContract(m2.getMemberId(), i2.getId(), 4, 7));
    assertThrows(IllegalArgumentException.class,
        () -> controller.createContract(m2.getMemberId(), i2.getId(), 6, 9));
    assertThrows(IllegalArgumentException.class,
        () -> controller.createContract(m2.getMemberId(), i2.getId(), 4, 9));
    assertThrows(IllegalArgumentException.class,
        () -> controller.createContract(m2.getMemberId(), i2.getId(), 6, 6));
  }

  // 4.1 Advance Time
  @Test
  public void testAdvanceTime() {
    controller.advanceDay(8);
    MemberDto m3Updated = controller.getMemberDtoById(m3.getMemberId());
    assertEquals(70, m3Updated.getCredits());
  }
}
