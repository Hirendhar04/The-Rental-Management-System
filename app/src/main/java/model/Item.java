package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an item that can be lent by a member.
 */
public class Item {
  private final String id;
  private final Member owner;
  private ItemCatagory category;
  private String name;
  private String description;
  private final int creationDay;
  private int costPerDay;
  private final List<Contract> contracts = new ArrayList<>();

  /**
   * Creates a new item object.

   * @param id           Unique identifier for the item
   * @param category     Category of the item
   * @param name         Name of the item
   * @param desc  Short description of the item
   * @param cpd   Cost to lend the item per day
   */
  public Item(String id, Member owner, int cd, ItemCatagory category, String name, String desc, int cpd) {
    this.id = id;
    this.owner = owner;
    this.creationDay = cd;
    this.category = category;
    this.name = name;
    this.description = desc;
    this.costPerDay = cpd;
  }

  /**
   * Shallow copy constructor, copies primitive fields. 
   *
   * <p>The owner is copied via Member(Member) to avoid referencing 
   * the exact same Member instance passed in. Owned contracts are not copied.
   *
   * @param other   --> object to copy from.
   */
  public Item(Item other) {
    this.id = other.id;
    this.owner = other.owner;
    this.creationDay = other.creationDay;
    this.category = other.category;
    this.name = other.name;
    this.description = other.description;
    this.costPerDay = other.costPerDay;
  }

  public String getId() {
    return id;
  }

  // For external use, copy for safety
  public Member getOwner() {
    return new Member(owner);
  }

  // Live referance, internall use only
  public Member getOwnerInternal() {
    return owner;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public ItemCatagory getCategory() {
    return category;
  }

  public int getCostPerDay() {
    return costPerDay;
  }

  public int getCreationDay() { 
    return creationDay;
  }

  public List<Contract> getContracts() {
    return Collections.unmodifiableList(contracts);
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setCategory(ItemCatagory category) {
    this.category = category;
  }

  public void setCostPerDay(int cost) {
    this.costPerDay = cost;
  }

  /**
   * Add item to a contract.

   * @param contract that item is a part of.
   */
  public void addContract(Contract contract) {
    contracts.add(contract);
  }

  /**
   * Checks if the item is available during the specified time period.

   * @param startDay  --> Starting day of the period.
   * @param endDay    --> Number of days to check.
   * @return          --> True if available, false otherwise.
   */
  public boolean isAvailable(int startDay, int endDay) {
    for (Contract contract : contracts) {
      if (contract.overlaps(startDay, endDay)) { 
        return false;
      }
    }
    return true;
  }
}
