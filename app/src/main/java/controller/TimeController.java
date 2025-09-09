package controller;

import controller.dto.TimeDto;
import model.TimeManager;

/**
 * Handle external contact to TimeManager.
 */
public class TimeController {
  private final TimeManager timeManager;
  private final ContractManager contractManager;

  private TimeController(TimeManager tm, ContractManager cm) {
    this.timeManager = tm;
    this.contractManager = cm;
  }

  public TimeDto getCurrentTime() {
    return new TimeDto(timeManager.getCurrentDay());
  }

  /**
   * Advace days by a given amount.
   *
   * @param days    --> to advance by.
   * @return        --> currect time.
   */
  public TimeDto advanceDays(int days) {
    timeManager.advanceDay(days);
    contractManager.cleanUpExpiredContracts(timeManager.getCurrentDay());
    return getCurrentTime();
  }
}
