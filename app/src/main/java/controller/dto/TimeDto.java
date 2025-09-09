package controller.dto;

/**
 * Class that only returns "view" level data.
 */
public class TimeDto {
  public final int currentDay;

  public TimeDto(int currentDay) {
    this.currentDay = currentDay;
  }
}
