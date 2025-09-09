package controller;

import java.util.ArrayList;
import java.util.List;
import model.Item;

/**
 * Storage for items in our codebase.
 */
public class Storage {
  private List<Item> storedItems;

  /**
   * Constructor to initialize an empty storage.
   */
  public Storage() {
    this.storedItems = new ArrayList<>();
  }

  /**
   * Adds an item to the storage.
   *
   * @param item The item to be added.
   */
  public void addItem(Item item) {
    storedItems.add(item);
  }

  /**
   * Removes an item from the storage by its ID.
   *
   * @param id The unique identifier of the item to be removed.
   * @return true if the item was removed, false otherwise.
   */
  public boolean removeItemById(String id) {
    return storedItems.removeIf(item -> item.getId().equals(id));
  }

  /**
   * Retrieves an item from the storage by its ID.
   *
   * @param id The unique identifier of the item.
   * @return The item if found, otherwise null.
   */
  public Item getItemById(String id) {
    for (Item item : storedItems) {
      if (item.getId().equals(id)) {
        return item;
      }
    }
    return null;
  }

  /**
   * Lists all items currently stored in the storage.
   *
   * @return List of all items.
   */
  public List<Item> listItems() {
    return new ArrayList<>(storedItems);
  }

  /**
   * Checks if an item is available in the storage.
   *
   * @param id The unique identifier of the item.
   * @return true if the item is available, false otherwise.
   */
  public boolean isItemAvailable(String id) {
    return storedItems.stream().anyMatch(item -> item.getId().equals(id));
  }
}