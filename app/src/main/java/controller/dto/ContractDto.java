package controller.dto;

import controller.TimeController;
import model.Contract;
import model.ContractStatus;

/**
 * Immutable snapshot (DTO) of contract for read-only exposure.
 */
public class ContractDto {
  public final String id;
  public final String itemName;
  public final String borrowerName;
  public final int startDay;
  public final int endDay;
  public final ContractStatus status;

  /**
   * constructor.
   *
   * @param id      --> ID
   * @param in      --> Item name
   * @param bn      --> Barrow name
   * @param sd      --> Start day
   * @param ed      --> End day
   * @param st      --> Status
   */
  public ContractDto(String id, String in, String bn, int sd, int ed, ContractStatus st) {
    this.id = id;
    this.itemName = in;
    this.borrowerName = bn;
    this.startDay = sd;
    this.endDay = ed;
    this.status = st;
  }

  /**
   * Alt Dto constructor, for view case.
   *
   * @param c               --> Contract.
   * @param currentDay      --> The currect day.
   */
  public ContractDto(Contract c, int currentDay) {
    this(
        c.getContractId(),
        c.getItem().getName(),
        c.getBorrower().getName(),
        c.getStartDay(),
        c.getEndDay(),
        c.statusAt(currentDay)
    );
  }
}
