# Pull Request: Week 3: Inheritance & Polymorphism

**Branch**: `week3-inheritance-polymorphism`  
**Target**: `main`  
**Author**: SENG 12233 Student  
**Lab Phase**: Phase 2 (Week 3)  

---

## 1. Description of Changes
- Refactored entity model into an inheritance hierarchy by extracting common state (`id`, `title`, `isBorrowed`) and behavior into `LibraryItem`.
- Subclassed `Book` (`extends LibraryItem`) to add `author` and specialized formatting.
- Subclassed `DVD` (`extends LibraryItem`) to add `durationMinutes` and polymorphically override `calculateLateFee(int daysLate)` to compute at \$25.00/day compared to the base \$10.00/day rate.
- Extended `Member` to support `maxBorrowLimit` and derived `Student` (quota 3) and `Staff` (quota 10) subclasses via `super(...)` initialization.
- Refactored `Library` to manage a polymorphic collection `List<LibraryItem> items`.
- Implemented `printAllLateFees(int daysLate)` demonstrating dynamic binding without conditional type dispatch.
- Implemented three overloaded `addItem` methods demonstrating compile-time static binding.
- Implemented `streamIfPossible(LibraryItem item)` using Java pattern-matching `instanceof DVD dvd` for safe downcasting.
- Generated and committed UML diagram to `docs/uml-week3.png`.

---

## 2. Architectural Analysis: Is-A vs. Has-A Relationships

| Relationship Type | Source Class | Target Class | OOP Concept | Architectural Justification |
| :--- | :--- | :--- | :--- | :--- |
| **Is-A (Generalization)** | `Book` | `LibraryItem` | Inheritance | A Book *is a* specific kind of LibraryItem, inheriting identity, title, and loan state. |
| **Is-A (Generalization)** | `DVD` | `LibraryItem` | Inheritance | A DVD *is a* LibraryItem with additional multimedia duration attributes and custom late-fee rates. |
| **Is-A (Generalization)** | `Student` | `Member` | Inheritance | A Student *is a* Member bound to a strict borrowing quota of 3 items. |
| **Is-A (Generalization)** | `Staff` | `Member` | Inheritance | A Staff member *is a* Member granted an extended borrowing quota of 10 items. |
| **Has-A (Composition)** | `Library` | `LibraryItem` | Composition | The Library owns and manages zero-to-many catalog items (`List<LibraryItem>`). |
| **Has-A (Composition)** | `Library` | `Member` | Composition | The Library owns and manages zero-to-many enrolled patrons (`List<Member>`). |

---

## 3. Lab Experiments & Observations

### Experiment 1: Dynamic Binding Class Resolution
- **Hypothesis**: When calling an overridden method through a superclass reference (`LibraryItem item = new DVD(...)`), the JVM resolves the method implementation dynamically at runtime based on the actual object instance.
- **Code Change**:
  ```java
  LibraryItem item = new DVD("D001", "Inception", 148);
  double fee = item.calculateLateFee(4);
  ```
- **Observed Behavior**: The output yielded `$100.00` ($25.0 \times 4$), executing `DVD.calculateLateFee` rather than `LibraryItem.calculateLateFee` ($10.0 \times 4 = $40.00$).
- **Conclusion**: Virtual method dispatch dynamically resolves method invocations based on the actual runtime object type, enabling extensible polymorphism.

### Experiment 2: Static Binding with Overloaded Methods
- **Hypothesis**: Overloaded method selection is decided at compile time based on the declared static reference type, not the runtime object type.
- **Code Change**:
  ```java
  LibraryItem generic = new Book("B002", "Effective Java", "Joshua Bloch");
  library.addItem(generic);
  ```
- **Observed Behavior**: The console printed:
  `[Static Binding] addItem(LibraryItem) called for: Effective Java`
  Even though `generic` held a `Book` instance at runtime, the compiler bound the call to `addItem(LibraryItem)` because the declared type of the reference is `LibraryItem`.
- **Conclusion**: Method overloading is statically bound at compile time based on reference types, unlike method overriding which is dynamically bound at runtime.

### Experiment 3: Compile-Time Type Symbol Errors
- **Hypothesis**: Subclass-specific methods cannot be invoked through a superclass reference without explicit downcasting.
- **Code Change**:
  ```java
  LibraryItem item = new DVD("D001", "Inception", 148);
  int mins = item.getDurationMinutes(); // Compiler error!
  ```
- **Observed Behavior**: Compilation failed:
  `error: cannot find symbol - method getDurationMinutes() in class LibraryItem`.
- **Conclusion**: The Java compiler validates symbol resolution against the static type of the reference. Pattern matching `instanceof DVD dvd` must be used to safely access subclass members.

### Experiment 4: Missing `super()` Constructor Calls
- **Hypothesis**: If a superclass does not provide a default no-argument constructor, a subclass constructor omitting `super(...)` fails compilation.
- **Code Change**:
  ```java
  public Book(String id, String title, String author) {
      // omitted super(id, title);
      this.author = author;
  }
  ```
- **Observed Behavior**: Compilation failed:
  `error: constructor LibraryItem in class LibraryItem cannot be applied to given types; required: String,String; found: no arguments`.
- **Conclusion**: Subclass constructors must explicitly chain to an accessible superclass constructor using `super(...)` when no default constructor exists.

---

## 4. Verification & Output Log
Executing `lms.Main` produces:
```
=== Phase 2: Inheritance & Polymorphism ===

--- Demonstrating Static Binding (Overloaded addItem) ---
[Static Binding] addItem(Book) called for: Clean Code
[Static Binding] addItem(DVD) called for: Inception
[Static Binding] addItem(LibraryItem) called for: Effective Java

--- Registering Members (Student & Staff) ---
--- Library Items ---
Book{id='B001', title='Clean Code', author='Robert C. Martin', borrowed=false}
DVD{id='D001', title='Inception', durationMinutes=148, borrowed=false}
Book{id='B002', title='Effective Java', author='Joshua Bloch', borrowed=false}
--- Library Members ---
Student{id='S101', name='Kasun Perera', borrowedCount=0, maxLimit=3}
Staff{id='E201', name='Dr. Kamal Gunasekara', borrowedCount=0, maxLimit=10}

--- Polymorphic Late Fee Calculation (4 days late) ---
[Book] 'Clean Code' -> Late Fee: $40.00
[DVD] 'Inception' -> Late Fee: $100.00
[Book] 'Effective Java' -> Late Fee: $40.00

--- Demonstrating Safe Pattern Matching (streamIfPossible) ---
Streaming DVD 'Inception' (Duration: 148 mins)... Enjoy the movie!
Item 'Clean Code' is not streamable: only DVDs can be streamed.
```

---

## 5. Peer Review Comments (Simulated Classmate PRs)

### Peer Review 1: PR #18 (Classmate: Dineth Jayasuriya)
> *"Solid refactoring of the item hierarchy! In your `printAllLateFees` method, you used `if (item instanceof DVD) ... else ...` to compute the fee. Notice that this bypasses dynamic binding! If you define `calculateLateFee` in `LibraryItem` and override it in `DVD`, you can simply call `item.calculateLateFee(daysLate)` on every item. The JVM will handle dispatching automatically without branching."*

### Peer Review 2: PR #21 (Classmate: Nipuni Senaratne)
> *"Nice work implementing the `Student` and `Staff` classes. I noticed you passed hardcoded numbers `3` and `10` into `super(memberId, name, 3)`. To avoid magic numbers and improve maintainability, consider defining `private static final int DEFAULT_LIMIT = 3;` in `Student` and `10` in `Staff`. It makes the borrow limit rules explicit and configurable in one place."*
