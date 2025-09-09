package controller;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import model.Contract;
import model.Item;
import model.Member;
import model.TimeManager;

/**
 * Controller that coordinates contract creation by delegating to the models.
 */
public class ContractManager {
  private final Map<String, Contract> contractsById = new HashMap<>();
  private final MemberManager memberManager;
  private final ItemManager itemManager;
  private final TimeManager timeManager;
  private final Random rnd = new Random();

  /**
   * Constructor for the contract manager.
   * 
   * <p>handles the interaction requierd for contracts.
   *
   * @param mm    --> Referance to the memberManager
   * @param im    --> Referance to the itemManager
   * @param tm    --> Referance to the timeManager
   */
  ContractManager(MemberManager mm, ItemManager im, TimeManager tm) {
    this.memberManager = mm;
    this.itemManager = im;
    this.timeManager = tm;
  }

  // Package-private.
  MemberManager getMemberManager() {
    return memberManager;
  }

  public ItemManager getItemManager() {
    return itemManager;
  }

  public TimeManager getTimeManager() {
    return timeManager;
  }

  /**
   * Directs the creation of a contract between two users and the items.

   * @param borrowerId    --> The id of the user borrowing the item.
   * @param itemId        --> The id of the item being borrowed.
   * @param startDay      --> Start day of contract.
   * @param endDay        --> End day of contract.
   * @return              --> Contract.
   */
  public Contract createContract(String borrowerId, String itemId, int startDay, int endDay) {
    Member borrower = memberManager.getMemberById(borrowerId);
    Item item = itemManager.getItemById(itemId);

    if (borrower == null) {
      throw new IllegalArgumentException("Borrower not found.");
    } else if (item == null) {
      throw new IllegalArgumentException("Item not found");
    } else if (!item.isAvailable(startDay, endDay)) {
      throw new IllegalArgumentException("Item not available in that period");
    }

    int days = endDay - startDay;
    int cost = days * item.getCostPerDay();
    if (borrower.getCredits() < cost) {
      throw new IllegalArgumentException("Borrower has insufficient credits");
    }

    borrower.deduceCredits(cost);
    Member lender = item.getOwnerInternal();
    lender.addCredits(cost);

    String contractId;
    do {
      contractId = generateId();
    } while (contractsById.containsKey(contractId));
    Contract contract = new Contract(contractId, borrower, lender, item, startDay, endDay);
    contractsById.put(contractId, contract);
    item.addContract(contract);
    return contract;
  }

  /**
   * Generate a random ID for the contract.

   * @return    --> id.
   */
  private String generateId() {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder sb = new StringBuilder(6);
    for (int i = 0; i < 6; i++) {
      sb.append(chars.charAt(rnd.nextInt(chars.length())));
    }
    return sb.toString();
  }

  public Contract getContractById(String id) {
    return contractsById.get(id);
  }

  public Collection<Contract> listContracts() {
    return Collections.unmodifiableCollection(contractsById.values());
  }

  public void cleanUpExpiredContracts(int currentDay) {
    contractsById.values().removeIf(c -> currentDay >= c.getEndDay() + 1);
  }
}