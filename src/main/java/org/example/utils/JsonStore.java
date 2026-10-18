package org.example.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.repository.Repository;

import java.io.File;
import java.io.IOException;

/**
 * Saves and loads the whole Repository as JSON.
 *
 * The object graph contains cycles (Person.dogs <-> Dog.owners, and
 * DogAdoption / CarTransaction pointing back to entities), so the entity
 * classes use @JsonIdentityInfo to turn repeated references into $id links.
 */
public class JsonStore {

    public static final String DEFAULT_FILE = "repository.json";

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Repository keeps its collections in private fields without getters,
        // so Jackson must read and write fields directly.
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    public static boolean save(Repository repo, String path) {
        File file = new File(path);
        try {
            createMapper().writeValue(file, repo);
            System.out.println("Repository saved to: " + file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            System.out.println("Could not save repository: " + e.getMessage());
            return false;
        }
    }

    public static Repository load(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("File not found: " + file.getAbsolutePath());
            return null;
        }
        try {
            Repository repo = createMapper().readValue(file, Repository.class);
            System.out.println("Repository loaded from: " + file.getAbsolutePath());
            return repo;
        } catch (IOException e) {
            System.out.println("Could not load repository: " + e.getMessage());
            return null;
        }
    }
}
