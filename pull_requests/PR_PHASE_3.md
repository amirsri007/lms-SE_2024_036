# Pull Request: Week 4: Abstract Classes & Interfaces

**Branch**: `week4-abstraction-interfaces`  
**Target**: `main`  
**Author**: SENG 12233 Student  
**Lab Phase**: Phase 3 (Week 4)  

---

## 1. Description of Changes
- Introduced contract interfaces `Loanable` (`loan`, `returnItem`) and `Reservable` (`reserve`, `cancelReservation`, `isReserved`).
- Converted `LibraryItem` into an `abstract class` implementing `Loanable`, centralizing stateful borrowing checks (`maxBorrowLimit` validation and loan status updates) while declaring `calculateLateFee(int daysLate)` as an abstract method.
- Updated `Book` to implement `Reservable`, adding `reservedBy` state and reservation management methods.
- Implemented `Magazine extends LibraryItem` (non-reservable) with `issueNumber` and late fee rate of \$5.00/day.
- Refactored `Member` to implement `Comparable<Member>`, overriding `compareTo` using `Integer.compare(other.borrowedCount, this.borrowedCount)` to rank active borrowers in descending order.
- Enhanced `Library` service with `lend(Loanable, Member)`, `reserveIfPossible(LibraryItem, Member)` using pattern matching, and `printMembersByActivity()` which safely copies the internal member list before sorting with `Collections.sort`.
- Generated and committed UML diagram to `docs/uml-week4.png`.

---

## 2. Design Justification: Why Reservable is an Interface

### Architectural Rationale:
1. **Interface Segregation Principle (ISP)**:
   - In a modern library system, reservation capabilities are not universal across all catalog items. Reference books, high-demand DVDs, or periodicals like magazines may be restricted from reservations due to short circulation windows or physical media constraints.
2. **Preventing Unsupported Operations & Pollution**:
   - If `reserve()` and `cancelReservation()` were placed on `LibraryItem`, non-reservable subclasses like `DVD` and `Magazine` would inherit methods they cannot legitimately fulfill, forcing them to throw `UnsupportedOperationException` or provide empty stub methods.
3. **Pluggable Contract**:
   - Modeling `Reservable` as an interface decouples capability from class hierarchy. Any entity—whether a `Book`, a future `StudyRoom`, or specialized equipment—can implement `Reservable` without forcing an unnatural inheritance relationship with `LibraryItem`.

---

## 3. Lab Experiments & Observations

### Experiment 1: Instantiating Abstract `LibraryItem`
- **Hypothesis**: Attempting to directly instantiate an abstract class causes a compile-time error.
- **Code Change**:
  ```java
  LibraryItem item = new LibraryItem("X001", "Generic Item");
  ```
- **Observed Behavior**: Compilation failed:
  `error: LibraryItem is abstract; cannot be instantiated`.
- **Conclusion**: Abstract classes prevent incomplete or semantically ambiguous base objects from being instantiated directly.

### Experiment 2: Omitting Abstract Method Implementation in `Magazine`
- **Hypothesis**: A concrete subclass that fails to implement an inherited abstract method will fail compilation.
- **Code Change**:
  ```java
  public class Magazine extends LibraryItem {
      // Omitted calculateLateFee(int daysLate)
  }
  ```
- **Observed Behavior**: Compilation failed:
  `error: Magazine is not abstract and does not override abstract method calculateLateFee(int) in LibraryItem`.
- **Conclusion**: Abstract methods define mandatory behavioral contracts that every concrete subclass must satisfy.

### Experiment 3: Implementing `Reservable` on `DVD`
- **Hypothesis**: Adding `implements Reservable` to `DVD` requires implementing all interface methods or declaring `DVD` as abstract.
- **Code Change**:
  ```java
  public class DVD extends LibraryItem implements Reservable {
      // without defining reserve, cancelReservation, isReserved
  }
  ```
- **Observed Behavior**: Compilation failed:
  `error: DVD is not abstract and does not override abstract method reserve(Member) in Reservable`.
- **Conclusion**: Java enforces interface contract fulfillment at compile time.

### Experiment 4: Inverting `compareTo` Sort Order
- **Hypothesis**: Changing the comparison arguments in `Integer.compare(this.borrowedCount, other.borrowedCount)` reverses the natural ordering from descending to ascending.
- **Code Change**:
  ```java
  @Override
  public int compareTo(Member other) {
      return Integer.compare(this.borrowedCount, other.borrowedCount); // Inverted
  }
  ```
- **Observed Behavior**: `printMembersByActivity()` ranked the member with 1 borrowed item first, and the member with 3 borrowed items second.
- **Conclusion**: To sort in descending order (highest activity first), `other.borrowedCount` must be compared against `this.borrowedCount`.

---

## 4. Verification & Output Log
Executing `lms.Main` produces:
```
=== Phase 3: Abstract Classes & Interfaces ===
[Static Binding] addItem(Book) called for: Clean Code
[Static Binding] addItem(Book) called for: Effective Java
[Static Binding] addItem(DVD) called for: Inception
[Static Binding] addItem(LibraryItem) called for: IEEE Software

--- Polymorphic Late Fee Calculation (3 days late) ---
[Book] 'Clean Code' -> Late Fee: $30.00
[Book] 'Effective Java' -> Late Fee: $30.00
[DVD] 'Inception' -> Late Fee: $75.00
[Magazine] 'IEEE Software' -> Late Fee: $15.00

--- Testing Loanable Contract (lend) ---
[Loan Confirmed] 'Clean Code' loaned to Kasun Perera.
[Loan Confirmed] 'Inception' loaned to Kasun Perera.
[Loan Confirmed] 'IEEE Software' loaned to Kasun Perera.
[Static Binding] addItem(Book) called for: Refactoring
[Refusal] Member 'Kasun Perera' has reached the limit of 3 items.
[Refusal] 'Clean Code' is already on loan.

--- Testing Reservable Contract (reserveIfPossible) ---
[Reservation Confirmed] Book 'Refactoring' reserved for Kasun Perera.
[Refusal] Item 'Inception' is not reservable (only Books support reservations).
[Refusal] Item 'IEEE Software' is not reservable (only Books support reservations).
[Loan Confirmed] 'Refactoring' loaned to Dr. Kamal Gunasekara.

--- Member Rankings by Borrowing Activity (Descending) ---
Rank 1: Kasun Perera (S101) - Borrowed Items: 3 (Limit: 3)
Rank 2: Dr. Kamal Gunasekara (E201) - Borrowed Items: 1 (Limit: 10)
```

---

## 5. Peer Review Comments (Simulated Classmate PRs)

### Peer Review 1: PR #28 (Classmate: Nuwan Wickramasinghe)
> *"Impressive use of interface segregation with `Reservable`! In your `printMembersByActivity()` implementation, I noticed you directly called `Collections.sort(this.members)`. This mutates the internal insertion order of your backing list. As recommended in the lab spec, copying the list first via `List<Member> sorted = new ArrayList<>(members);` before sorting protects your original collection from unintended side-effects."*

### Peer Review 2: PR #31 (Classmate: Tharushi Wickramasinghe)
> *"Great work on the `Magazine` class and `Comparable` implementation! In `LibraryItem.loan()`, remember to check both whether the item is already borrowed AND whether the member has exceeded their `maxBorrowLimit`. Your code handles both cases cleanly. Make sure to also check `member != null` at the start of `loan` to avoid `NullPointerException`s."*
