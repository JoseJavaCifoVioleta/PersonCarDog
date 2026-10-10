# Exercise: Full CRUD Operations (Java SE / Pre-Spring Boot)

## Objective

Implement complete **Create, Read, Update, Delete (CRUD)** operations for `Person`, `Car`, and `Dog` models, plus **read-only** operations for `CarTransaction`.

All business logic must live in `Service.java`.  
All user interaction (menus, input, output) must live in `Controller.java` + `Utils.java`.

This exercise simulates what you will later build with Spring Boot using `@Service`, `@Controller`/`@RestController`, and repositories.

## Context

- This is a **plain Java SE** project (no Spring yet).
- Architecture follows a simple DDD style:
  - `model/` → domain objects (no business logic)
  - `repository/` → in-memory storage (fake DB)
  - `service/` → all business rules and operations
  - `controller/` + `utils/` → console UI
- `buyCar(...)` in `Service` is the reference implementation for a use case.
- `CarTransaction` is created only as a side effect of `buyCar`. You must **not** allow direct creation, update or deletion of `CarTransaction` through the new CRUD.

## Requirements

### 1. Service Layer — CRUD Methods

Add the following methods to `Service.java`. All methods must:
- Accept `Repository repo` as the last parameter.
- Perform validation.
- Print clear messages (similar to `buyCar`).
- Return a meaningful value (`boolean`, the created/updated object, or a list).

#### Person CRUD (full)
- `createPerson(String name, int age, Repository repo)` → `Person`
- `getPersonById(String id, Repository repo)` → `Person`
- `getAllPeople(Repository repo)` → `ArrayList<Person>`
- `updatePerson(String id, String newName, int newAge, Repository repo)` → `boolean`
- `deletePerson(String id, Repository repo)` → `boolean`

#### Car CRUD (full)
- `createCar(String make, String model, int year, Repository repo)` → `Car`
- `getCarById(String id, Repository repo)` → `Car`
- `getAllCars(Repository repo)` → `ArrayList<Car>`
- `updateCar(String id, String newMake, String newModel, int newYear, Repository repo)` → `boolean`
- `deleteCar(String id, Repository repo)` → `boolean`

#### Dog CRUD (full)
- `createDog(String name, String breed, int age, Repository repo)` → `Dog`
- `getDogById(String id, Repository repo)` → `Dog`
- `getAllDogs(Repository repo)` → `ArrayList<Dog>`
- `updateDog(String id, String newName, String newBreed, int newAge, Repository repo)` → `boolean`
- `deleteDog(String id, Repository repo)` → `boolean`

#### CarTransaction (read-only only)
- `getCarTransactionById(String id, Repository repo)` → `CarTransaction`
- `getAllCarTransactions(Repository repo)` → `ArrayList<CarTransaction>`

**Rules / Validations (examples — you may add more):**
- `create*`: name/make/model must not be blank; age/year must be reasonable.
- `update*`: target must exist; new values must be valid.
- `deletePerson`: if the person owns a car, you may either:
  - Block deletion, or
  - Clear the car reference first (document your choice).
- `deleteCar`: if a person currently owns this car, clear the reference from the person.
- `deleteDog`: no special ownership rules yet (unless you implemented adoption in another exercise).
- Never allow direct creation of `CarTransaction` outside `buyCar`.

### 2. Utils — UI Helpers

Extend `Utils.java` with:

**Submenus (print only):**
- `personMenu()`
- `carMenu()`
- `dogMenu()`
- `carTransactionMenu()` (read-only options)

**Input helpers:**
- `askString(Scanner scan, String prompt)`
- `askInt(Scanner scan, String prompt)`
- `askMenuOption(Scanner scan)` (already exists — keep it)

### 3. Controller — Full Menu-Driven UI

Update `Controller.java` so that:

- Main menu options 1, 2, 3 now open **submenus** instead of printing "not implemented".
- Each submenu offers:
  - Create
  - List all
  - Get by ID
  - Update
  - Delete
  - Back to main menu
- Option 4 remains `buyCar`.
- Add a new top-level option or submenu entry to **list all CarTransactions** (read-only).

**Example main menu after changes:**
```
===== MAIN MENU =====
1. Person operations
2. Dog operations
3. Car operations
4. Buy a car (person to person)
5. View car transactions (read only)
6. Quit
```

Inside "Person operations":
```
===== PERSON MENU =====
1. Create person
2. List all people
3. Find person by ID
4. Update person
5. Delete person
6. Back
```

Do the same pattern for Car and Dog.

For CarTransaction menu (read-only):
```
===== CAR TRANSACTIONS (READ ONLY) =====
1. List all transactions
2. Find transaction by ID
3. Back
```

### 4. Non-Functional / Style Requirements

- Keep the same coding style as existing `buyCar`.
- All messages should be clear for students debugging.
- Do **not** put business logic inside `Controller` or `Utils`.
- Do **not** add new model fields unless necessary for the exercise.
- `CarTransaction` must remain read-only from the new UI.

## Acceptance Criteria

- [ ] `Service` contains all required CRUD methods for Person, Car, Dog.
- [ ] `Service` contains only read methods for CarTransaction.
- [ ] All service methods validate input and print informative messages.
- [ ] Utils provides submenus and input helpers.
- [ ] Controller implements complete submenu navigation for Person, Car, Dog.
- [ ] Controller allows viewing (list + by id) of CarTransactions.
- [ ] `buyCar` continues to work unchanged.
- [ ] No direct creation of `CarTransaction` is possible from the new menus.

## Preparation for Spring Boot

After completing this exercise you will be able to map:

| Java SE (this project)       | Spring Boot equivalent                  |
|------------------------------|-----------------------------------------|
| `Service.createPerson(...)`  | `@Service` method + `PersonService`     |
| `Repository.addPerson(...)`  | `PersonRepository` (Spring Data JPA)    |
| `Controller` menu handling   | `@RestController` + `@GetMapping` etc.  |
| Manual validation + prints   | Bean Validation + `@Valid` + exceptions |

## Tips

- Start by implementing the Service methods (pure logic + repo calls).
- Then add the Utils menu helpers.
- Finally wire everything in Controller.
- Test each operation from the console before moving to the next entity.
- Use the existing `buyCar` implementation as a template for validation style.

---

**Exercise file location:** `docs/pra/exercise-full-crud-operations.md`
