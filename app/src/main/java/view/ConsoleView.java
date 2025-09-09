package view;

import controller.ControllerManager;
import controller.dto.ContractDto;
import controller.dto.ItemDto;
import controller.dto.MemberDto;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * The class that handles everything related to UI.
 */
public class ConsoleView {
  private final ControllerManager controller;
  private final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

  public ConsoleView(ControllerManager controller) {
    this.controller = controller;
  }

  /**
   * Start the UI loop.
   */
  public void start() {
    while (true) {
      printMainMenu();
      String selection = scanner.nextLine().trim();
      try {
        switch (selection) {
          case "1": 
            handleMembers();
            break;
          case "2":
            handleItems();
            break;
          case "3":
            handleContracts();
            break;
          case "4":
            handleTime();
            break;
          case "0":
            System.out.println("Bye!");
            return;
          default:
            System.out.println("Invalid option: " + selection);
        }
      } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
      }
    }
  }

  private void printMainMenu() {
    System.out.println("=== === Stuff Lending === ===");
    System.out.println("1) Members");
    System.out.println("2) Items");
    System.out.println("3) Contracts");
    System.out.println("4) Time");
    System.out.println("0) Exit");
    System.out.print("Select: ");
  }

  private void handleMembers() {
    System.out.println("Members: \n  -- 1) Add \n  -- 2) List simple \n  -- 3) List verbose"
        + "\n  -- 4) Update \n  -- 5) Delete \n  -- 0) Back");
    String selection = scanner.nextLine().trim();
    switch (selection) {
      case "1":
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        MemberDto m = new MemberDto(controller.createMember(name, email, phone));
        System.out.println("Created member id = " + m.getMemberId());
        break;
      case "2":
        for (MemberDto memb : controller.listMembers()) {
          System.out.println(memb.getMemberId() + " | " + memb.getName() + ", "
              + memb.getEmail() + ", Phone: " + memb.getPhone() + ", credits: "
              + memb.getCredits());
        }
        break;
      case "3":
        for (MemberDto memb : controller.listMembers()) {
          System.out.println("=== ===");
          System.out.println("ID: " + memb.getMemberId());
          System.out.println("Name: " + memb.getName());
          System.out.println("Email: " + memb.getEmail());
          System.out.println("Phone: " + memb.getPhone());
          System.out.println("Credits: " + memb.getCredits());
          System.out.println("Owned items:");
          for (ItemDto item : controller.getOwnedItemsForMember(memb.getMemberId())) {
            System.out.println("  " + item.getName() + " (" + item.getId() + ")");
            for (ContractDto c : controller.getContractsForItem(item.getId())) {
              System.out.println("  Contract: " + c.id + " borrower: " + c.borrowerName
                  + " " + c.startDay + " --> " + c.endDay + ", Status: " + c.status);
            }
          }
        }
        break;
      case "4":
        String id = prompt("Member id: ");
        controller.updateMember(
            id,
            prompt("New name:"),
            prompt("New email:"),
            prompt("New phone:")
        );
        System.out.println("Member updated.");
        break;
      case "5":
        String deleteId = prompt("Id of member to delete: ");
        controller.deleteMember(deleteId);
        System.out.println("Member deleted.");
        break;
      case "0": 
        return;
      default: 
        System.out.println("Unknown choice.");
    }
  }

  private void handleItems() {
    System.out.println("Items:\n  -- 1) Add\n  -- 2) List\n  -- 3) View\n  -- 4) Update\n  -- 5) Delete\n  -- 0) Back");
    String selection = scanner.nextLine().trim();
    switch (selection) {
      case "1":
        System.out.print("Owner id: ");
        String ownerId = scanner.nextLine();
        System.out.print("Category (Tool, Vehicle, Game, Toy, Sport, Other): ");
        String cat = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        System.out.print("Cost per day (int): ");
        int cost = Integer.parseInt(scanner.nextLine());
        ItemDto it = new ItemDto(controller.createItem(ownerId,
            Enum.valueOf(model.ItemCatagory.class, cat), name, desc, cost));
        System.out.println("Item created: " + it.getId());
        break;
      case "2":
        for (ItemDto it2 : controller.listItems()) {
          System.out.println(it2.getId() + ": " + it2.getName()
              + " owner: " + it2.getOwner().getName()
              + ", Cost per day: " + it2.getCostPerDay());
        }
        break;
      case "3":
        String iid = prompt("Item ID: ");
        ItemDto item = controller.getItemDtoById(iid);
        if (item == null) {
          System.out.println("Not found");
          break;
        }
        System.out.println("Name: " + item.getName());
        System.out.println("Owner: " + item.getOwner().getName());
        System.out.println("Cost per day to borrow: " + item.getCostPerDay());
        System.out.println("Description: " + item.getDescription());
        System.out.println("\nContracts:");
        for (ContractDto c : controller.getContractsForItem(item.getId())) {
          System.out.println("  " + c.id + " " + c.startDay
              + " --> " + c.endDay + " borrower: " + c.borrowerName
              + ", Status: " + c.status);
        }
        break;
      case "4":
        String itemToUpdateId = prompt("Item ID: ");
        controller.updateItemInfo(
            itemToUpdateId,
            prompt("New name: "),
            prompt("New description"),
            Integer.parseInt(prompt("Set new price:"))
        );
        System.out.println("Updated");
        break;
      case "5":
        String deleteItemId = prompt("Item ID: ");
        controller.deleteItem(deleteItemId);
        System.out.println("Deleted");
        break;
      case "0": return;
      default: System.out.println("Unknown selection");
    }
  }

  private void handleContracts() {
    System.out.println("Contracts: \n  -- 1) Create \n  -- 2) List \n  -- 0) Back");
    String selection = scanner.nextLine();
    switch (selection) {
      case "1":
        String borrowerId = prompt("Borrower ID: ");
        String itemId = prompt("Item ID: ");
        int startDay = Integer.parseInt(prompt("Start day (int): "));
        int endDay = Integer.parseInt(prompt("End day (int): "));
        ContractDto c = controller.createContractDtoAndGeDto(borrowerId, itemId, startDay, endDay);
        System.out.println("Created contract:\n  ID: " + c.id
            + ", Item: " + c.itemName
            + ", Borrower: " + c.borrowerName
            + ", Period: " + c.startDay + " --> " + c.endDay
            + ", Status: " + c.status);
        break;
      case "2":
        System.out.println("Existing contracts:");
        for (ContractDto contract : controller.listContracts()) {
          System.out.println("Created contract:\n  ID: " + contract.id
              + ", Item: " + contract.itemName
              + ", Borrower: " + contract.borrowerName
              + ", Period: " + contract.startDay + " --> " + contract.endDay
              + ", Status: " + contract.status);
          break;
        }
        break;
      case "0":
        return;
      default:
        System.out.println("Unknown selection");
    }
  }

  private void handleTime() {
    System.out.println("Time: current day = " + controller.getCurrentDay());
    System.out.print("Advance days (int): ");
    String s = scanner.nextLine();
    if (!s.trim().isEmpty()) {
      int days = Integer.parseInt(s);
      controller.advanceDay(days);
      System.out.println("Now day = " + controller.getCurrentDay());
    }
  }

  private String prompt(String message) {
    System.out.print(message);
    return scanner.nextLine();
  }
}
