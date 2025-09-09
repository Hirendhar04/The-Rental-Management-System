package model;

/**
 * TimeManager handles the simulation of time in the system.
 * It manages a day counter that starts from day 0 and can be manually advanced.
 * The current day is used by other components to check contract periods, availability, etc.
 */
public class TimeManager {
  private int currentDay = 0;

  // default contructor.
  public TimeManager() {}

  /**
   * Copy constructor.
   *
   * @param other   --> Instance to copy.
   */
  public TimeManager(TimeManager other) {
    this.currentDay = other.currentDay;
  }

  public int getCurrentDay() {
    return currentDay;
  }

  /**
   * Advance the program by some amount of days.
   *
   * @param days    --> The amount of days to advance by.
   */
  public void advanceDay(int days) {
    if (days <= 0) {
      throw new IllegalArgumentException("Days to advance must be > 0");
    }
    currentDay += days;
  }
}