package controller;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import model.Item;
import model.ItemCatagory;
import model.Member;
import model.TimeManager;

/**
 * Manages interactions using the Item class.
 * Used to coardinate interactions, does not handle or mutate members internals directly.
 */
public class ItemManager {
  private final Map<String, Item> itemsById = new HashMap<>();
  private final MemberManager memberManager;
  private final TimeManager timeManager;
  private final Random rnd = new Random();

  /**
   * Basic constructor.
   *
   * @param memberManager     --> Referance to an instance of memberManager.
   * @param timeManager       --> Referance to an instance of timeManager
   */
  ItemManager(MemberManager memberManager, TimeManager timeManager) {
    this.memberManager = memberManager;
    this.timeManager = timeManager;
  }

  /**
   * Keep rederance instead of copying.
   *
   * @param other                 --> to "Copy" from.
   * @param keepReferences        --> Usually yes.
   */
  public ItemManager(ItemManager other, boolean keepReferences) {
    if (keepReferences) {
      this.memberManager = other.memberManager;
      this.timeManager = other.timeManager;
      this.itemsById.putAll(other.itemsById);
    } else {
      this.memberManager = new MemberManager(other.memberManager);
      this.timeManager = new TimeManager(other.timeManager);
      other.listitems().forEach(item -> itemsById.put(item.getId(), new model.Item(item)));
    }
  }


  /**
   * Method to initialize creation of an item.
   *
   * @param ownerId       --> Id of the owner of the item.
   * @param category      --> Of the item being made.
   * @param name          --> of the item.
   * @param description   --> of the item.
   * @param costPerDay    --> of the item.
   * @return              --> the item.
   */
  public Item createItem(String ownerId, ItemCatagory category, String name, String description, int costPerDay) {
    Member owner = memberManager.getMemberById(ownerId);
    if (owner == null) {
      throw new IllegalArgumentException("Owner not found");
    }
    String itemId;
    do {
      itemId = generateId();
    } while (itemsById.containsKey(itemId));
    int creationday = timeManager.getCurrentDay();
    Item i = new Item(itemId, owner, creationday, category, name, description, costPerDay);
    itemsById.put(itemId, i);
    owner.addItem(i);
    owner.addCredits(100);
    return i;
  }

  /**
   * Generate a random id for the item.
   *
   * @return    --> The id.
   */
  public String generateId() {
    String chars = "0123456789";
    StringBuilder sb = new StringBuilder(8);
    for (int i = 0; i < 8; i++) {
      sb.append(chars.charAt(rnd.nextInt(chars.length())));
    }
    return sb.toString();
  }

  /**
   * Helper method to initialize item removal.
   *
   * @param itemId    --> Id of the item to be removed.
   */
  public void deleateItem(String itemId) {
    Item i = itemsById.remove(itemId);
    if (i == null) {
      throw new IllegalArgumentException("Item not found");
    }
    i.getOwnerInternal().removeItem(i);
  }

  /**
   * Added to act as a gateway between ConsoleView and Item.
   * (prevents the view mutating the model directly).
   *
   * @param itemId    --> Id of the item to edit.
   * @param newName   --> of the item.
   * @param newDesc   --> of the item.
   * @param newCost   --> per day to borrow the item.
   */
  public void updateItemInfo(String itemId, String newName, String newDesc, Integer newCost) {
    Item item = itemsById.get(itemId);
    if (item == null) {
      throw new IllegalArgumentException("Item not found");
    }
    if (newName != null && !newName.trim().isEmpty()) {
      item.setName(newName);
    }
    if (newDesc != null) {
      item.setDescription(newDesc);
    }
    if (newCost != null) {
      item.setCostPerDay(newCost);
    }
  }

  public Item getItemById(String id) {
    return itemsById.get(id);
  }

  public Collection<Item> listitems() {
    return Collections.unmodifiableCollection(itemsById.values());
  }
}
