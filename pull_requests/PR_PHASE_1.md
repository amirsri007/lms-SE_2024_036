# Pull Request: Week 2: Classes, Objects & Encapsulation

**Branch**: `week2-encapsulation`  
**Target**: `main`  
**Author**: SENG 12233 Student  
**Lab Phase**: Phase 1 (Week 2)  

---

## 1. Description of Changes
- Implemented `Book` class in `lms.model` with private fields (`title`, `author`, `isbn`, `isBorrowed`), validated setters rejecting null/blank strings via `IllegalArgumentException`, unvalidated `setBorrowed`, and formatted `toString()`.
- Implemented `Member` class in `lms.model` with private fields (`memberId`, `name`, `borrowedCount`), validated setters, non-negative validation for `setBorrowedCount(int)`, business method `incrementBorrowedCount()`, and formatted `toString()`.
- Implemented `Library` service in `lms.service` demonstrating object composition (`List<Book>` and `List<Member>`), along with `addBook`, `addMember`, `listBooks`, `listMembers`, and unmodifiable collection accessors.
- Implemented application entry point and verification driver in `lms.Main` showing successful entity registration, listing, and graceful handling of invariant violations.
- Generated and committed UML class diagram deliverable to `docs/uml-week2.png`.

---

## 2. Analytical Questions

### Q: Which fields lack setters and why?
1. **`Member.borrowedCount` lacks an open, unrestricted public setter:**
   - **Reason**: The number of books borrowed by a member represents sensitive business state governed by library invariants (it must only increment upon a valid loan and decrement upon a valid return). Exposing a public `setBorrowedCount(int)` to outside callers would allow clients to arbitrarily rewrite member loan statistics, bypassing system integrity. Instead, controlled mutations occur exclusively through domain methods like `incrementBorrowedCount()`.
2. **`Book.isbn` / Identity Fields:**
   - **Reason**: An ISBN is an intrinsic, immutable identifier assigned to a book upon publication. Once a `Book` instance is initialized, mutating its ISBN is semantically invalid. Limiting or removing external setter access preserves object identity and consistency in collections (e.g., hash maps and sets).

---

## 3. Lab Experiments & Observations

### Experiment 1: Public Field Exposure
- **Hypothesis**: Making fields `public` allows direct external access, completely bypassing business validation rules.
- **Code Change**:
  ```java
  // Changed in Book.java
  public String title;
  // External client execution:
  Book b = new Book("Valid Title", "Author", "ISBN-123");
  b.title = ""; // Invariant violated directly without setter interception!
  ```
- **Observed Behavior**: The code compiled and executed without error, leaving the `Book` in an illegal, blank-title state. No `IllegalArgumentException` was thrown.
- **Conclusion**: Encapsulation with `private` fields and validated mutators is mandatory to enforce domain invariants throughout the lifecycle of an object.

### Experiment 2: Bypassing Setters in Constructor
- **Hypothesis**: Assigning constructor parameters directly to fields (`this.title = title;`) instead of invoking validated setters allows invalid objects to be instantiated.
- **Code Change**:
  ```java
  public Book(String title, String author, String isbn) {
      this.title = title;   // Bypassing setTitle(title)
      this.author = author; // Bypassing setAuthor(author)
      this.isbn = isbn;     // Bypassing setIsbn(isbn)
  }
  ```
- **Observed Behavior**: When executing `new Book("", null, "   ")`, no exception was raised. The constructor successfully created an invalid `Book` instance with corrupt state.
- **Conclusion**: Constructors must delegate assignment to validated setters (or shared validation helpers) to guarantee that an object can never exist in an invalid state.

### Experiment 3: Missing `this.` Keyword in Mutators
- **Hypothesis**: Omitting `this.` when the setter parameter name matches the class field name causes variable shadowing, where the parameter assigns to itself.
- **Code Change**:
  ```java
  public void setTitle(String title) {
      if (title == null || title.isBlank()) throw new IllegalArgumentException();
      title = title; // Missing this.
  }
  ```
- **Observed Behavior**: The setter validated the parameter, but the instance field `this.title` remained `null`. When printing the object, `title` was displayed as `'null'`.
- **Conclusion**: The `this` reference explicitly distinguishes instance fields from local parameters that shadow them in scope.

---

## 4. Verification & Output Log
Executing `lms.Main` produces:
```
=== Phase 1: Classes, Objects & Encapsulation ===
--- Library Books ---
Book{title='Clean Code', author='Robert C. Martin', isbn='978-0132350884', borrowed=false}
Book{title='Design Patterns', author='Erich Gamma et al.', isbn='978-0201633610', borrowed=false}
Book{title='Effective Java', author='Joshua Bloch', isbn='978-0134685991', borrowed=false}
--- Library Members ---
Member{id='M001', name='Alice Johnson', borrowedCount=0}
Member{id='M002', name='Bob Smith', borrowedCount=0}

--- Testing Invariant Validation ---
Rejected invalid book: Title cannot be blank
```

---

## 5. Peer Review Comments (Simulated Classmate PRs)

### Peer Review 1: PR #12 (Classmate: Kasun Perera)
> *"Great work structuring the `Book` and `Member` classes! I noticed in your `Book` constructor that you assigned `this.title = title` directly instead of calling `setTitle(title)`. As demonstrated in Experiment 2, this means passing a blank title to `new Book("", ...)` bypasses your setter checks and creates an invalid object. Consider routing your constructor assignments through your validated setters."*

### Peer Review 2: PR #15 (Classmate: Sanduni Fernando)
> *"Excellent implementation of `Library` composition! In `Member.java`, your `incrementBorrowedCount()` directly accesses `this.borrowedCount++` rather than calling `setBorrowedCount(...)`. While this works, delegating to `setBorrowedCount()` ensures that any future validation rules added to the setter (such as maximum borrow caps) will automatically be respected. Overall, clean code and great documentation!"*
