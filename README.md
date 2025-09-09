## Overview
Hirendhar Ramkumar (hr22rs)
Kevin Johansson (kj223ag)


The Rental Management System is a Java-based application designed to facilitate the management of item rentals between members. It includes features for creating members, managing items, establishing contracts for item rentals, and tracking time progression in the system.

The application follows an MVC-like structure with well-organized components for controllers, models, and views.

## Features

- **Member Management**
  - Add, update, remove members
  - Search by email, phone, or ID
  - Track credits and join date

- **Item Management**
  - Add, update, and remove items
  - Assign items to specific members
  - View item details
  - List items by member or globally

- **Contract Management**
  - Create rental contracts between members
  - Check availability of items
  - Track contract start/end dates and costs
  - Support multiple contract statuses: `SCHEDULED`, `ACTIVE`, `COMPLETED`

- **Time Control**
  - View current system day
  - Advance the system date
  - Automatically update contract statuses based on time

- **Persistence**
  - Load predefined data using `HardcodedPersistenceManager`
  - (Future) Implement custom persistence backends

---

## Project Structure

The application is organized into several main components:

### 1. **Controllers**
- **App**  
  Entry point of the application. Starts the system and launches the console interface.  

- **ControllerManager**  
  Central coordinator of the system. Manages and provides access to all other managers (`MemberManager`, `ItemManager`, `ContractManager`, `TimeManager`, and `PersistenceManager`).  

- **ContractManager**  
  Handles creation and management of rental contracts. Ensures contracts are valid, calculates total cost, and checks item availability.  

- **MemberManager**  
  Manages members and their information. Handles adding, finding, updating, and removing members, as well as tracking their credits.  

- **ItemManager**  
  Manages items available for rental. Handles adding items to owners, updating item details, removing items, and listing available items.  

- **TimeController**  
  Provides operations for viewing and advancing the current system time, affecting contract statuses accordingly.  

- **HardcodedPersistenceManager**  
  Loads predefined data for testing purposes, making it quick to set up a working example. Implements `PersistenceManager` interface.  

---

### 2. **Models**
- **Member**  
  Represents a user in the system. Stores personal details (name, email, phone), credit balance, and items owned.  

- **Item**  
  Represents an item available for rental. Stores details such as name, description, category, creation date, cost per day, and associated contracts.  

- **Contract**  
  Represents a rental agreement between two members for a specific item. Includes start and end dates, total cost, and contract status (`SCHEDULED`, `ACTIVE`, `COMPLETED`).  

- **TimeManager**  
  Tracks the current day in the system and advances time when requested.  

- **Enums**  
  - `ItemCategory` – Classification of items (e.g., Tool, Vehicle, Game, Toy, Sport, Other)  
  - `ContractStatus` – Lifecycle state of a contract  

---

### 3. **DTOs (Data Transfer Objects)**
- **MemberDto**  
  Immutable snapshot of `Member` for safe transfer between layers.  

- **ItemDto**  
  Immutable snapshot of `Item` including owner information.  

- **ContractDto**  
  Immutable snapshot of `Contract` containing borrower, lender, and item details.  

- **TimeDto**  
  Snapshot of current system day.  

---

### 4. **Views**
- **ConsoleView**  
  Menu-driven text-based user interface for interacting with the system. Allows navigation between different services:  
  - Member Management  
  - Item Management  
  - Contract Management  
  - Time Management  

  Uses the `Scanner` class to capture user input, validate it, and display feedback. Loops until the user exits the program.
