package org.example.utils;

import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.DogAdoption;
import org.example.model.Person;
import org.example.repository.Repository;
import org.example.service.Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class JsonStoreTest {

    @TempDir
    Path tempDir;

    private String path(String name) {
        return tempDir.resolve(name).toString();
    }

    private Person personByName(Repository repo, String name) {
        return repo.getAllPeople().stream()
                .filter(p -> name.equals(p.getName())).findFirst().orElseThrow();
    }

    private Dog dogByName(Repository repo, String name) {
        return repo.getAllDogs().stream()
                .filter(d -> name.equals(d.getName())).findFirst().orElseThrow();
    }

    /**
     * Repository with every entity type and a real many-to-many graph:
     * shared custody (rex has 2 owners), a person with 3 dogs, and a car sale.
     */
    private Repository buildFullRepository() {
        Repository repo = new Repository();

        Person alice = new Person("Alice", 40);
        Person bob = new Person("Bob", 45);
        Person carol = new Person("Carol", 35);
        Person dave = new Person("Dave", 50);
        Person erin = new Person("Erin", 28);
        repo.addPerson(alice);
        repo.addPerson(bob);
        repo.addPerson(carol);
        repo.addPerson(dave);
        repo.addPerson(erin);

        Car carOne = new Car("Toyota", "Corolla", 2020);
        Car carTwo = new Car("Ford", "Focus", 2019);
        repo.addCar(carOne);
        repo.addCar(carTwo);
        alice.setCar(carTwo);
        dave.setCar(carOne);

        Dog rex = new Dog("Rex", "Labrador", 3);
        Dog luna = new Dog("Luna", "Beagle", 2);
        Dog bella = new Dog("Bella", "Poodle", 4);
        repo.addDog(rex);
        repo.addDog(luna);
        repo.addDog(bella);

        assertTrue(Service.adoptDog(alice, rex, 100, repo).isSuccess());
        assertTrue(Service.adoptDog(bob, rex, 50, repo).isSuccess());    // shared custody on rex
        assertTrue(Service.adoptDog(alice, luna, 80, repo).isSuccess());
        assertTrue(Service.adoptDog(alice, bella, 60, repo).isSuccess()); // alice now has 3 dogs

        assertTrue(Service.buyCar(erin, dave, 5000, repo).isSuccess());

        return repo;
    }

    @Test
    void saveThenLoadPreservesEntityCountsAndIds() {
        Repository original = buildFullRepository();
        String file = path("counts.json");

        assertTrue(JsonStore.save(original, file));
        Repository loaded = JsonStore.load(file);

        assertNotNull(loaded);
        assertEquals(5, loaded.getAllPeople().size());
        assertEquals(2, loaded.getAllCars().size());
        assertEquals(3, loaded.getAllDogs().size());
        assertEquals(1, loaded.getAllCarTransactions().size());
        assertEquals(4, loaded.getAllDogAdoptions().size());

        // ids survive the round-trip
        Person originalAlice = personByName(original, "Alice");
        Person loadedAlice = personByName(loaded, "Alice");
        assertEquals(originalAlice.getId(), loadedAlice.getId());
        assertEquals(original.getAllDogAdoptions().get(0).getId(),
                loaded.getDogAdoptionById(original.getAllDogAdoptions().get(0).getId()).getId());
    }

    @Test
    void saveThenLoadRebuildsSharedCustodyGraph() {
        Repository original = buildFullRepository();
        Dog rex = dogByName(original, "Rex");
        String file = path("shared.json");

        assertTrue(JsonStore.save(original, file));
        Repository loaded = JsonStore.load(file);
        assertNotNull(loaded);

        Dog loadedRex = loaded.getDogById(rex.getId());
        assertNotNull(loadedRex);
        assertEquals(2, loadedRex.getOwners().size());

        Person loadedAlice = personByName(loaded, "Alice");
        assertEquals(3, loadedAlice.getDogs().size());

        // @JsonIdentityInfo must re-link the SAME instances, not copies
        assertSame(loadedRex, dogByName(loaded, "Rex"));
        assertSame(loadedRex, loadedAlice.getDogs().stream()
                .filter(d -> d.getName().equals("Rex")).findFirst().orElseThrow());

        DogAdoption adoption = loaded.getAllDogAdoptions().get(0);
        assertSame(loaded.getPersonById(adoption.getAdopter().getId()), adoption.getAdopter());
        assertSame(loaded.getDogById(adoption.getDog().getId()), adoption.getDog());
    }

    @Test
    void saveThenLoadPreservesCarOwnershipAndTransaction() {
        Repository original = buildFullRepository();
        String file = path("cars.json");

        assertTrue(JsonStore.save(original, file));
        Repository loaded = JsonStore.load(file);
        assertNotNull(loaded);

        Person erin = personByName(loaded, "Erin");
        Person dave = personByName(loaded, "Dave");
        assertNotNull(erin.getCar());
        assertEquals("Toyota", erin.getCar().getMake());
        assertNull(dave.getCar());

        CarTransaction tx = loaded.getAllCarTransactions().get(0);
        assertEquals("Car sold for 5000", tx.getContract());
        assertSame(erin, tx.getBuyer());
        assertSame(dave, tx.getSeller());
    }

    @Test
    void saveThenLoadPreservesReturnedStatusAndContract() {
        Repository original = buildFullRepository();
        Dog rex = dogByName(original, "Rex");
        Person bob = personByName(original, "Bob");

        assertTrue(Service.returnDog(bob, rex, original).isSuccess());
        String file = path("returned.json");

        assertTrue(JsonStore.save(original, file));
        Repository loaded = JsonStore.load(file);
        assertNotNull(loaded);

        // history is kept: 4 records, 1 returned + 3 active
        assertEquals(4, loaded.getAllDogAdoptions().size());
        assertEquals(1, loaded.getAllDogAdoptions().stream()
                .filter(a -> "RETURNED".equals(a.getStatus())).count());

        DogAdoption returned = loaded.getAllDogAdoptions().stream()
                .filter(a -> "RETURNED".equals(a.getStatus())).findFirst().orElseThrow();
        assertEquals("Dog adopted for 50 - RETURNED", returned.getContract());

        // the link is gone from both sides, the other owner stays
        assertFalse(personByName(loaded, "Bob").getDogs().stream()
                .anyMatch(d -> d.getName().equals("Rex")));
        assertEquals(1, loaded.getDogById(rex.getId()).getOwners().size());
    }

    @Test
    void loadedRepositoryStillAcceptsNewOperations() {
        Repository original = buildFullRepository();
        String file = path("usable.json");

        assertTrue(JsonStore.save(original, file));
        Repository loaded = JsonStore.load(file);
        assertNotNull(loaded);

        Person carol = personByName(loaded, "Carol");
        Dog rex = dogByName(loaded, "Rex");

        // rex already has 2 owners -> must fail with the real business rule
        ResultOperation full = Service.adoptDog(carol, rex, 10, loaded);
        assertFalse(full.isSuccess());
        assertEquals("Dog already has the maximum number of owners.", full.getMessage());

        Dog luna = dogByName(loaded, "Luna");
        ResultOperation ok = Service.adoptDog(carol, luna, 10, loaded);
        assertTrue(ok.isSuccess());
        assertEquals(1, carol.getDogs().size());
        assertSame(luna, carol.getDogs().get(0));
        assertEquals(2, luna.getOwners().size()); // alice + carol, shared custody works on the loaded graph
    }

    @Test
    void saveDoesNotRecurseInfinitelyOnCyclicGraph() throws Exception {
        Repository original = buildFullRepository();
        String file = path("cyclic.json");

        // would throw StackOverflowError if the cycles were not broken by @JsonIdentityInfo
        assertDoesNotThrow(() -> assertTrue(JsonStore.save(original, file)));
        assertTrue(Files.size(Path.of(file)) > 0);
    }

    @Test
    void loadReturnsNullWhenFileDoesNotExist() {
        assertNull(JsonStore.load(path("nope.json")));
    }

    @Test
    void loadReturnsNullOnCorruptJson() throws Exception {
        Path corrupt = tempDir.resolve("corrupt.json");
        Files.writeString(corrupt, "{ this is not valid json");
        assertNull(JsonStore.load(corrupt.toString()));
    }

    @Test
    void saveReturnsFalseWhenTargetIsADirectory() {
        // a directory cannot be written as a file -> graceful failure, no exception
        assertFalse(JsonStore.save(buildFullRepository(), tempDir.toString()));
    }

    @Test
    void emptyRepositoryRoundTripsAsEmpty() {
        Repository original = new Repository();
        String file = path("empty.json");

        assertTrue(JsonStore.save(original, file));
        Repository loaded = JsonStore.load(file);

        assertNotNull(loaded);
        assertNotSame(original, loaded);
        assertTrue(loaded.isEmpty());
    }
}
