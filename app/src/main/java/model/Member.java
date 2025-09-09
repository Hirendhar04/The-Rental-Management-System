package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represent a user in the system.
 */
public class Member {
  private final String memberId;
  private String name;
  private String email;
  private String phone;
  private int credits;
  private final List<Item> ownedItems;
  private final int creationDay;

  /**
   * Creates member object.

   * @param name   --> name of the user.
   * @param email  --> email of the user.
   * @param phone  --> phone number.
   */
  public Member(String memberId, String name, String email, String phone, int creationDay) {
    this.memberId = Objects.requireNonNull(memberId);
    this.name = Objects.requireNonNull(name);
    this.email = Objects.requireNonNull(email);
    this.phone = Objects.requireNonNull(phone);
    this.creationDay = creationDay;
    this.credits = 0;
    this.ownedItems = new ArrayList<>();
  }

  /**
   * Copy constructor (shallow), copies primitive fields, does not copy ownedItems.
   *
   * <p>This is intentional to avoid cycles when copying graph structures.
   *
   * @param other     --> Object copying from.
   */
  public Member(Member other) {
    this.memberId = Objects.requireNonNull(other.memberId);
    this.name = other.name;
    this.email = other.email;
    this.phone = other.phone;
    this.creationDay = other.creationDay;
    this.credits = other.credits;
    this.ownedItems = new ArrayList<>();
  }

  /* Getters */
  public String getMemberId() {
    return memberId;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public int getCredits() {
    return credits;
  }

  public int getCreationDate() {
    return creationDay;
  }

  public List<Item> getOwnedItems() {
    return Collections.unmodifiableList(ownedItems);
  }

  /* Setters */

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPhone(String phone) {
    this.phone = phone; 
  }

  public void setCredits(int credits) {
    this.credits = credits;
  }

  // Ownership helpers
  /**
   * Help add item to appropriate member.

   * @param item    --> to recive.
   */
  public void addItem(Item item) {
    if (ownedItems.contains(item)) {
      return;
    }
    ownedItems.add(item);
  }

  /**
   * Help remove the item from the appropriate member.

   * @param item    --> to lend.
   */
  public void removeItem(Item item) {
    if (!ownedItems.contains(item)) {
      return;
    }
    ownedItems.remove(item);
  }

  // Credit helpers
  /**
   * Add funds to the lender.

   * @param amount    --> of credits to gain.
   */
  public void addCredits(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Cannot add negative credits.");
    }
    this.credits += amount;
  }

  /**
   * Deduce funds from the borrower.

   * @param amount    --> of credits to remove.
   */
  public void deduceCredits(int amount) {
    if (amount < 0) {
      throw new IllegalStateException("Cannot deduct negative credits.");
    } else if (this.credits < amount) {
      throw new IllegalStateException("Insufficient credits.");
    }
    this.credits -= amount;
  }
}
