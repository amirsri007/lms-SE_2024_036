# Pull Request: Week 5: Exception Handling

**Branch**: `week5-exceptions`  
**Target**: `main`  
**Author**: SENG 12233 Student  
**Lab Phase**: Phase 4 (Week 5)  

---

## 1. Description of Changes
- Introduced domain custom exception hierarchy under package `lms.exception`:
  - `ItemNotAvailableException extends Exception` (Checked Exception) for recoverable business failures.
  - `MemberNotFoundException extends RuntimeException` (Unchecked Exception) for lookup and identity precondition failures.
- Refactored `Loanable` and `LibraryItem`:
  - Replaced console failure prints with typed `ItemNotAvailableException` throws.
  - Resolved slot release bug by tracking `private Member borrower` in `LibraryItem` and introducing `Member.decrementBorrowedCount()`, ensuring borrowing quotas are restored when items are returned.
- Implemented lookup and transactional routines in `Library`:
  - `findMember(String memberId)` (throws `MemberNotFoundException`).
  - `findItem(String itemId)` (throws `ItemNotAvailableException`).
  - `borrowItem(String itemId, String memberId)` and `returnItem(String itemId)`.
  - Updated `lend(Loanable, Member)` to declare `throws ItemNotAvailableException`.
- Implemented `attempt(Library, String, String)` transaction helper in `lms.Main` with full `try-catch-finally` logging.
- Verified all 8 required operational scenarios in `lms.Main`.
- Generated and committed UML class diagram to `docs/uml-week5.png`.

---

## 2. Checked vs. Unchecked Exception Classification

| Failure Scenario | Exception Class | Type | Architectural Justification |
| :--- | :--- | :--- | :--- |
| **Item Already on Loan / Limit Exceeded** | `ItemNotAvailableException` | **Checked** (`extends Exception`) | Represents a normal, recoverable business contingency. Callers (e.g., UI or console drivers) are forced by the compiler to anticipate this scenario, handle it gracefully, and inform the user or prompt for an alternative item. |
| **Unknown Member ID** | `MemberNotFoundException` | **Unchecked** (`extends RuntimeException`) | Represents a lookup failure or violated precondition. Typically indicates an invalid identifier passed from an upstream layer (e.g., corrupted session or typo). Making it unchecked prevents signature pollution across intermediate business workflows. |
| **Blank Title / Negative Parameter** | `IllegalArgumentException` | **Unchecked** (`extends RuntimeException`) | Represents a programming contract violation (precondition failure). Clients must supply valid non-blank strings; violating this contract indicates caller bug rather than a normal runtime business contingency. |

---

## 3. Lab Experiments & Observations

### Experiment 1: Omitting `try-catch` / `throws` for Checked Exception
- **Hypothesis**: Invoking a method that declares a checked exception without handling or declaring it results in a compile-time failure.
- **Code Change**:
  ```java
  public void testLend(Loanable item, Member member) {
      item.loan(member); // Missing try-catch or throws clause
  }
  ```
- **Observed Behavior**: Compilation failed:
  `error: unreported exception ItemNotAvailableException; must be caught or declared to be thrown`.
- **Conclusion**: Java strictly enforces the static handling of checked exceptions at compile time.

### Experiment 2: Converting `MemberNotFoundException` to Checked
- **Hypothesis**: If `MemberNotFoundException` extends `Exception` instead of `RuntimeException`, all intermediate lookup and delegation methods are forced to add `throws MemberNotFoundException`.
- **Code Change**:
  ```java
  public class MemberNotFoundException extends Exception { ... }
  ```
- **Observed Behavior**: Every method calling `findMember` (including `borrowItem`, stream helpers, and GUI lookups) failed to compile until boilerplate `throws` clauses or catch blocks were added everywhere.
- **Conclusion**: Precondition and lookup failures are best modeled as unchecked exceptions to avoid exception clutter in higher-level business abstractions.

### Experiment 3: Catch Block Ordering (`catch (Exception)` before Subclass)
- **Hypothesis**: Catching a superclass (`Exception`) before a more specific subclass (`ItemNotAvailableException`) results in an unreachable code error.
- **Code Change**:
  ```java
  try {
      library.borrowItem(itemId, memberId);
  } catch (Exception e) {
      System.out.println("Generic: " + e.getMessage());
  } catch (ItemNotAvailableException e) { // Subclass catch placed second
      System.out.println("Specific: " + e.getMessage());
  }
  ```
- **Observed Behavior**: Compilation failed:
  `error: exception ItemNotAvailableException has already been caught`.
- **Conclusion**: Catch blocks must always be ordered from most specific subclass to most general superclass.

### Experiment 4: Returning `null` vs. Throwing `MemberNotFoundException`
- **Hypothesis**: Returning `null` when a member is not found defers failure and causes silent propagation leading to runtime `NullPointerException`s.
- **Code Change**:
  ```java
  public Member findMember(String id) {
      for (Member m : members) if (m.getMemberId().equals(id)) return m;
      return null; // Returning null instead of throwing
  }
  // Caller:
  Member m = library.findMember("M999");
  m.incrementBorrowedCount(); // NullPointerException thrown here!
  ```
- **Observed Behavior**: The error did not indicate that `"No member with id 'M999'"` was missing; instead, it threw an ambiguous `NullPointerException` with no diagnostic ID attached.
- **Conclusion**: Throwing an explicit domain exception (`MemberNotFoundException`) fails fast and encapsulates exact diagnostic context (`memberId`).

---

## 4. Verification & Output Log (8 Scenarios)
Executing `lms.Main` produces:
```
=== Phase 4: Custom Exception Handling & Transactions ===

[Scenario 1] Successful Borrow
[Loan Confirmed] 'Clean Code' loaned to Alice Perera.
Success: item 'B001' borrowed by member 'S101'
attempt finished: item B001, member S101

[Scenario 2] Item Already On Loan
ItemNotAvailableException caught: Clean Code is already on loan.
attempt finished: item B001, member E201

[Scenario 3] Unknown Member (Unchecked MemberNotFoundException)
MemberNotFoundException caught: No member with id 'M999'
attempt finished: item D001, member M999

[Scenario 4] Unknown Item (Checked ItemNotAvailableException)
ItemNotAvailableException caught: No item with id 'X404'
attempt finished: item X404, member S101

[Scenario 5] Returning an Unborrowed Item
ItemNotAvailableException caught: Inception is not on loan.

[Scenario 6] Invariant Validation (IllegalArgumentException on Blank Title)
IllegalArgumentException caught: Title cannot be blank

[Scenario 7] Enforcing Student Borrowing Limit (Limit = 3)
[Loan Confirmed] 'Inception' loaned to Alice Perera.
Success: item 'D001' borrowed by member 'S101'
attempt finished: item D001, member S101
[Loan Confirmed] 'IEEE Software' loaned to Alice Perera.
Success: item 'M101' borrowed by member 'S101'
attempt finished: item M101, member S101
ItemNotAvailableException caught: Alice Perera has reached the limit of 3 items.
attempt finished: item B002, member S101

[Scenario 8] Slot Release Bug Fix: Quota Restoration on Return
Active borrows for Alice before return: 3
[Return Confirmed] 'Clean Code' has been successfully returned.
Active borrows for Alice after returning B001: 2
[Loan Confirmed] 'Effective Java' loaned to Alice Perera.
Success: item 'B002' borrowed by member 'S101'
attempt finished: item B002, member S101
Final active borrows for Alice: 3
```

---

## 5. Peer Review Comments (Simulated Classmate PRs)

### Peer Review 1: PR #35 (Classmate: Supun Rathnayake)
> *"Excellent job structuring custom exceptions! In your `findMember` method, throwing `MemberNotFoundException` is much cleaner than returning `null`. One suggestion: consider storing the `memberId` as a `private final String memberId` field inside `MemberNotFoundException` and exposing `getMemberId()`. This lets calling UI controllers retrieve the erroneous ID programmatically without parsing the exception message string."*

### Peer Review 2: PR #39 (Classmate: Dilshan Bandara)
> *"Great implementation of the slot release bug fix! In `returnItem()`, making sure to check whether `borrower != null` before calling `borrower.decrementBorrowedCount()` prevents potential `NullPointerException`s if an unassigned item is returned. Also, adding the `finally` block in `attempt()` guarantees that audit logs execute even when unexpected exceptions are thrown."*
