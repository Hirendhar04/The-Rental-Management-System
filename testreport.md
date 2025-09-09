# Stuff Lending Test Report
Document the results of your final system test below. You find instructions on the course homepage.

Version: <#269388> (ecd6617cd91324c68f504356123ada0ea305d6d3)
Date: 2025-08-15
Enviroment: Windows 10, Java version 18.0.2.1
Performed by: Kevin Johansson.

## Overview:
Hirendhar Ramkumar (hr22rs)
Kevin Johansson (kj223ag) 

The Rental Management System is a Java-based application designed to facilitate the managment of item
rentals between members. It includes deatures for creating members, managing items, establishing contracts
for item rentals and tracking time progession in said system.

The applivation was designed to follow an MVC structure with well-organized components for controllers,
models, and views.

## Test Cases:

### 1. Member Managment
**Test Case:** 5.1 Member Date.
- **Result:** Ok.
- **Notes:** All member data matches requiermemnts.

**Test Case:** 1.1 Create Member.
- **Result:** Ok.
- **Notes:** Member created successfully with correct details.

**Test Case:** 1.2 Create Member - Duplicate Email/Phone.
- **Result:** Ok.
- **Notes:** Correctly rejected duplicates, allowing uniqe entries. No such failsafe exists for ID, while its unlikely, due to the range of possible different IDs, that two exact same ones will be created, it is theoretically possible.

**Test Case:** 1.3 Delete Member.
- **Result:** ok.
- **Notes:** Member removed from list after deletion. Do note, Mamber containing items, will also be deleted, despite console output stating otherwise. This can lead to residual items, linked to a missing member building up, unless you remove the items too.

### 2. Item Management
**Test Case:** 2.1 Create Item.
- **Result:** Ok.
- **Notes:** Item created, credits increased by 100.


**Test Case:** 2.2 Delete Item (not in contract).
- **Result:** Ok.
- **Notes:** Item deleted successfully.


**Test Case:** 2.3 Delete Item (in contract).
- **Result:** Failed.
- **Notes:** Item deleted, contract in contract listing updates correctly, however, member listing for contracts does not.

### 3. Contract Mangement
**Test Case:** 3.1 Create Contract.
- **Result:** Ok.
- **Notes:** Contract created with correct details.

**Test Case:** 3.2 Create Contract - Not Enough Funds.
- **Result:** Ok.
- **Notes:** Creation blocked when borrower had insufficient credits.

**Test Case:** 3.3 - 3.6 Create Contract - Conflicting Times.
- **Result:** Ok.
- **Notes:** All conflicting date scenarios rejected as expected.

**Test Case:** Special case, Contract Expiery Deduction.
- **Result:** Fail.
- **Notes:** Instead of deducing credits over time, all credits get deduced on day one. For instance, 4 day period, 10 credits a day. Borrower gets item for 4 days, but pays full 40 credits cost up-front.

### 4. Time Managment
**Test Case:** 4.1 Advance Time.
- **Result:** Ok.
- **Notes:** Contract expiery works as expected. 

### Extra notes, UI:
Most of the UI works as expected, however, when a contract expires, it remains in the members list, makred as "expired". For contract listings, the contract gets fully removed one day after experation date.