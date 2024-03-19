package org.example.gymcrm.service;

public interface ValidationService {
    <T> void validateEntity(T entity, Class<?>... groups);
}
