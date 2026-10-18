# Exercise: Many-to-Many Dog Adoption — Shared Custody (Java SE / Pre-Spring Boot) — Part 2

## Objective

Evolve the adoption system from the previous **one-to-one** `adoptDog` into a **many-to-many** relationship.

-   A `Person` can adopt **multiple dogs** (max 3 active).
-   A `Dog` can have **multiple owners** (max 2 active — shared custody).
-   `DogAdoption` becomes the explicit **join entity** (association class): one record per person-dog pair, carrying `fee`, `date`, and `status`.

All business logic must live in `Service.java`.  
All user interaction must live in `Controller.java` + `Utils.java`.

No JPA annotations in this exercise — pure Java SE. The mapping table at the end only shows how this will translate later.

## Context

-   This is **Part 2** of the adoption story. You should have completed (or at least studied) the first "Adopt a Dog" exercise.
-   Architecture remains simple DDD style (model, repository, service, controller/utils).
-   `DogAdoption` is still read-only from the UI perspective (created only as a side-effect of `adoptDog` or modified only by `returnDog`).
-   `buyCar` / `CarTransaction` and all CRUD remain unchanged.

## Business Rules

Shared custody with limits:

-   A person may have **at most 3 active dogs** at any time.
-   A dog may have **at most 2 active owners** at any time.
-   No duplicate active pair: the same person cannot have two ACTIVE adoptions for the same dog.
-   Only adults (`age >= 18`) can adopt.
-   Adoption fee must be `>= 0`.
-   `returnDog(person, dog)` ends **only that person's link** to the dog (the dog may still have other owners).
-   Every adoption (past and present) is recorded as a `DogAdoption`.

## Requirements

### 1\. Model — Refactor to Lists + Status

#### `Person`

-   Replace the single reference:
    
    // before (Part 1)  
    private Dog dog;
    
    with a collection:
    
    private List<Dog> dogs = new ArrayList<>();
    
-   Add getter `getDogs()` and a method or setter to manage the list (Service will control mutations).
-   Update `toString()` to avoid recursion (print only dog names or ids).

#### `Dog`

-   Replace the single back-reference:
    
    // before  
    private Person owner;
    
    with:
    
    private List<Person> owners = new ArrayList<>();
    
-   Same recursion caution in `toString()`.

#### `DogAdoption` — Add Status

Keep the existing shape and add lifecycle state:

-   `String id`
-   `Person adopter`
-   `Dog dog`
-   `Date date`
-   `int fee`
-   `String contract`
-   `String status` (or an `enum AdoptionStatus { ACTIVE, RETURNED }`)
-   On creation: `status = "ACTIVE"`
-   On return: set `status = "RETURNED"` and append `" - RETURNED"` (or similar) to `contract`.

Provide empty constructor + full constructor, getters/setters, `toString()`.

### 2\. Repository

You can reuse most of the existing `DogAdoption` methods. You may add lightweight helpers if useful:

-   `getDogAdoptionsByPersonId(String personId)` → `ArrayList<DogAdoption>`
-   `getDogAdoptionsByDogId(String dogId)` → `ArrayList<DogAdoption>`

Or implement the logic in Service using the existing `getAllDogAdoptions()`.

`clear()` / `isEmpty()` (if not already present) are useful for tests and reload scenarios.

### 3\. Service Layer — Many-to-Many Use Cases

#### Core use case (refactored)

public static ResultOperation adoptDog(Person adopter, Dog dog, int fee, Repository repo)

Validation chain (each failure returns `ResultOperation` with `false` + message, same style as Part 1):

1.  `adopter` exists in repo
2.  `dog` exists in repo
3.  `adopter.getAge() >= 18`
4.  `fee >= 0`
5.  No duplicate ACTIVE pair: the person is not already actively linked to this dog via an adoption record (or check both lists).
6.  `adopter.getDogs().size() < 3` → "Person already has the maximum number of dogs."
7.  `dog.getOwners().size() < 2` → "Dog already has the maximum number of owners."

On success:

-   Create `DogAdoption(adopter, dog, new Date(), fee, "Dog adopted for " + fee, "ACTIVE")`
-   Add to both sides of the relation:
-   -   `adopter.getDogs().add(dog)`
    -   `dog.getOwners().add(adopter)`
-   `repo.addDogAdoption(adoption)`
-   Print and return success with the adoption.

#### Read-only helpers

-   `getDogsByOwner(Person person, Repository repo)` → `ResultOperation` (list of currently owned dogs)
-   `getOwnersByDog(Dog dog, Repository repo)` → `ResultOperation` (list of current owners)
-   Keep `getDogAdoptionById` and `getAllDogAdoptions`

#### Close one side of the relationship

public static ResultOperation returnDog(Person person, Dog dog, Repository repo)

-   Find the ACTIVE `DogAdoption` for this exact pair.
-   If none exists → failure.
-   Remove the link from both collections:
-   -   `person.getDogs().remove(dog)`
    -   `dog.getOwners().remove(person)`
-   Update the `DogAdoption`: set status `RETURNED`, append `" - RETURNED"` to contract.
-   Print the updated adoption.
-   Return success. The record stays in history.

#### Cascade on delete (update existing methods)

-   `deletePerson`: before removing the person, for each dog in `person.getDogs()`:
-   -   remove the person from `dog.getOwners()`
    -   mark every related `DogAdoption` for this person as `RETURNED`
-   `deleteDog`: symmetrically, for each owner, remove the dog from their list and mark adoptions `RETURNED`.

Do **not** delete the `DogAdoption` records — only mark them returned (audit trail).

### 4\. Utils — UI Helpers

Extend menus:

-   New or updated main menu option: **Adopt a dog (shared custody)**
-   New option: **Return a dog (end one adoption)**
-   Read-only submenu or entries:
-   -   List dogs of a person
    -   List owners of a dog
    -   View all adoptions (already exists)

Add input helpers if missing (`askString`, `askInt`).

### 5\. Controller — Menu-Driven UI

Update `Controller.java`:

-   "Adopt a dog (shared custody)": ask for person ID and dog ID, ask fee, resolve, call `Service.adoptDog(...)`.
-   "Return a dog": ask for person ID and dog ID, call `Service.returnDog(...)`.
-   Allow viewing a person's current dogs and a dog's current owners (by ID).
-   User **must select by ID** (no hardcoded demo data).
-   Existing CRUD and buy-car flows must continue to work.

**Example main menu sketch:**

\===== MAIN MENU =====  
1\. Person operations  
2\. Dog operations  
3\. Car operations  
4\. Buy a car (person to person)  
5\. View car transactions (read only)  
6\. Adopt a dog (shared custody)  
7\. Return a dog (end one adoption)  
8\. View dog adoptions (read only)  
9\. Save repository to JSON file  
10\. Load repository from JSON file  
11\. Quit  

### 6\. DataSeeder (recommended)

Seed data that exercises the many-to-many rules:

-   At least one dog with **two active owners**.
-   At least one person with **two active dogs**.
-   Enough people and dogs so that students can test the "max reached" rules from the console.

Make sure seeded adopters are adults.

### 7\. Non-Functional / Style Requirements

-   All list mutations for the relationship happen **only inside Service** methods.
-   Never allow direct creation/update/deletion of `DogAdoption` from the UI except through the defined use cases.
-   Keep the same message and `ResultOperation` style as `buyCar` / Part 1.
-   `DogAdoption` records are append-only for history; only status/contract may change on return.
-   Avoid infinite recursion in `toString()` implementations.
-   Defensive copies or careful encapsulation of the lists is encouraged but not required.

## Acceptance Criteria

-   `Person` has `List<Dog> dogs`; `Dog` has `List<Person> owners`.  
    `DogAdoption` has a `status` field (`ACTIVE` / `RETURNED`).  
    `adoptDog` enforces all 7 rules and maintains both sides of the relationship.  
    `returnDog` removes only the selected pair and marks the adoption `RETURNED`.  
    `getDogsByOwner` and `getOwnersByDog` return correct active lists.  
    `deletePerson` and `deleteDog` clean the opposite lists and mark related adoptions `RETURNED`.  
    No duplicate active (person, dog) pairs are allowed.  
    Max limits (3 dogs / 2 owners) are enforced.  
    Controller allows adopting and returning by selecting IDs.  
    DataSeeder creates at least one multi-owner dog and one multi-dog person.  
    Existing `buyCar`, CRUD, and 1:1-style flows (if kept) do not regress.  
    No JPA annotations appear in the Java SE source.

## Optional Extensions

-   Add an `AdoptionStatus` enum instead of String.
-   Implement `getActiveAdoptionsForPerson` / `getAllAdoptionsForPerson` (history vs current).
-   Allow a person to "re-adopt" a previously returned dog (new ACTIVE record).
-   Add a read-only "Adoption history for person/dog" submenu.
-   Track a `returnDate` on the adoption when returned.

## Preparation for Spring Boot

| Java SE (this project)              | Spring Boot / JPA equivalent                                  |
| ----------------------------------- | ------------------------------------------------------------- |
| List<Dog> dogs in Person            | @ManyToMany + @JoinTable or @OneToMany on join entity side    |
| List<Person> owners in Dog          | @ManyToMany(mappedBy=...) or inverse side of the relationship |
| DogAdoption as explicit join entity | Separate @Entity with @ManyToOne to Person and to Dog         |
| Manual list add/remove + status     | @OneToMany + @ManyToOne on DogAdoption; cascade management    |
| Service.adoptDog / returnDog        | @Service method, @Transactional                               |
| Repository.addDogAdoption           | DogAdoptionRepository (Spring Data JPA)                       |
| ResultOperation                     | ResponseEntity / custom DTO                                   |
| Menu option "Adopt a dog (shared)"  | @PostMapping in a @RestController                             |

## Tips

-   Start with the model refactor (`Person`, `Dog`, `DogAdoption` with status).
-   Implement `adoptDog` next — the duplicate check and size limits are the new interesting parts.
-   Then implement `returnDog` (find the right adoption record, mutate only that pair).
-   Update cascade logic in `deletePerson` / `deleteDog` early so you don't leak links.
-   Use the existing `buyCar` / Part 1 `adoptDog` as templates for validation order and messaging.
-   Seed multi-ownership data so the console can demonstrate shared custody.
-   Keep `DogAdoption` as the source of truth for "is this pair currently active?" if you want extra safety instead of only trusting the lists.

  

**Exercise file location:** `docs/pra/exercise-many-to-many-dog-adoption.md`  
**Related:** builds on `@/home/albert/MyProjects/Sandbox/PersonCarDog/docs/pra/exercise-adopt-dog.md`