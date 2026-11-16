# Library Management System (LMS)

**Index Number**: SE/2024/036  
**Course**: SENG 12233 — Object-Oriented Programming  
**Institution**: University of Kelaniya  
**Target Environment**: Java 21 (javac / console execution, structured for future GUI integration)  

## Project Overview
The Library Management System (LMS) is a console-based application iteratively developed across four sequential phases to demonstrate mastery of core Object-Oriented Programming (OOP) concepts:
1. **Phase 1 (Week 2)**: Classes, Objects & Encapsulation
2. **Phase 2 (Week 3)**: Inheritance & Polymorphism (Static vs. Dynamic Binding)
3. **Phase 3 (Week 4)**: Abstract Classes & Interfaces
4. **Phase 4 (Week 5)**: Custom Exception Handling & Transaction Integrity

## Project Architecture & Package Hierarchy
```
lms-SE_2024_036/
├── .idea/                 # IntelliJ IDEA configuration
├── docs/                  # UML diagrams per phase
├── pull_requests/         # Pull Request templates and documentation
├── src/
│   └── lms/
│       ├── Main.java      # Application entry point and test driver
│       ├── model/         # Domain entities and contracts
│       ├── service/       # Business logic services (Library)
│       └── exception/     # Domain custom exceptions
├── lms-SE_2024_036.iml    # IntelliJ module descriptor
└── README.md
```

## Compilation & Execution (Java 21)
```bash
# Compile from root directory
javac -d out $(find src -name "*.java")

# Run application
java -cp out lms.Main
```
