package model;

import controller.dto.ItemDto;
import controller.dto.MemberDto;

// import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Represents the contract between two users.
 */
public class Contract {
  private final String contractId;
  private final MemberDto borrower;     // Snapshot of borrower
  private final MemberDto lender;       // Snapshot of lender
  private final ItemDto item;           // Snapshot of item
  private final int startDay;
  private final int endDay;
  private final int totalCost;

  /**
   * Constructor, represents the contract between two users.

   * @param borrower      --> The person borrowing an item.
   * @param lender       --> The person who the item belongs to.
   * @param item        --> The item being lended.
   * @param startDay    --> Start date of the contract.
   * @param endDay      --> End date of the contract.
   */
  public Contract(String contractId, Member borrower, Member lender, Item item, int startDay, int endDay) {
    if (startDay < 0 || endDay <= startDay) {
      throw new IllegalArgumentException("Invalid contract days");
    }
    this.contractId = contractId;
    this.borrower = new MemberDto(borrower);
    this.lender = new MemberDto(lender);
    this.item = new ItemDto(item);
    this.startDay = startDay;
    this.endDay = endDay;
    int days = endDay - startDay;
    this.totalCost = days * item.getCostPerDay();
  }

  public String getContractId() {
    return contractId;
  }

  public MemberDto getLender() {
    return lender;
  }

  public MemberDto getBorrower() {
    return borrower;
  }

  public ItemDto getItem() {
    return item;
  }

  public int getTotalCost() {
    return totalCost;
  }

  public int getStartDay() {
    return startDay;
  }

  public int getEndDay() {
    return endDay;
  }

  /**
   * "Track" the lifecycle status computed for a given day.
   *
   * @param currentDay    --> The current active day.
   * @return              --> Status of contract.
   */
  public ContractStatus statusAt(int currentDay) {
    if (currentDay < startDay) {
      return ContractStatus.SCHEDULED;
    } else if (currentDay < endDay) {
      return ContractStatus.ACTIVE;
    }  
    return ContractStatus.COMPLEATED;
  }

  public boolean overlaps(int start, int end) {
    // Overlap detection: if new interval intersects existing [startDay, endDay)
    return !(end <= this.startDay || start >= this.endDay);
  }
}
