package controller;

import model.ItemCatagory;
import model.Member;

class HardCodedPersistenceManager implements PersistenceManager {
  private final ControllerManager controller;

  public HardCodedPersistenceManager(ControllerManager controller) {
    this.controller = controller;
  }

  @Override
  public void loadSampleData() {
    Member m1 = controller.createMember("Alice", "alice@example.com", "0701111111");
    Member m3 = controller.createMember("Moronica Dracula Dextrose", "MDD@example.com", "0703333333");

    controller.createItem(m1.getMemberId(), ItemCatagory.Vehicle, "I1", "A cool bike", 50);
    controller.createItem(m1.getMemberId(), ItemCatagory.Tool, "I2", "A cheap, heavy hammer", 10);
    controller.createItem(m3.getMemberId(), ItemCatagory.Game, "Halo", "Halo 2", 20);

    Member m2 = controller.createMember("Bob", "bob@example.com", "0702222222");
    m2.setCredits(100);

    m3.setCredits(90);

    String i2Id = controller.getOwnedItemsForMember(m1.getMemberId())
        .stream()
        .filter(i -> i.getName().equals("I2"))
        .findFirst()
        .orElseThrow()
        .getId();

    m1.setCredits(480);
    controller.createContract(m3.getMemberId(), i2Id, 5, 7);
  }
}
