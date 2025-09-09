package controller.dto;

import model.Item;
import model.ItemCatagory;

/**
 * Immutable snapshot (DTO) of items for read-only exposure.
 */
public class ItemDto {
  private final String id;
  private final String name;
  private final String description;
  private final ItemCatagory category;
  private final int creationDay;
  private final int costPerDay;
  private final MemberDto owner; // small owner snapshot (no ownedItems)

  /**
   * Functionally a copy constructor.
   *
   * @param item    --> to copy feilds from.
   */
  public ItemDto(Item item) {
    this.id = item.getId();
    this.name = item.getName();
    this.description = item.getDescription();
    this.category = item.getCategory();
    this.creationDay = item.getCreationDay();
    this.costPerDay = item.getCostPerDay();
    this.owner = new MemberDto(item.getOwnerInternal());
  }

  public String getId() {
    return id;
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

  public int getCreationdate() {
    return creationDay;
  }

  public int getCostPerDay() {
    return costPerDay;
  }

  public MemberDto getOwner() {
    return owner;
  }
}
