package org.example.gymcrm.service.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.example.gymcrm.annotation.Authenticate;
import org.example.gymcrm.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidationServiceImpl implements ValidationService {

    private final Validator validator;

    @Autowired
    public ValidationServiceImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public <T> void validateEntity(T entity, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity, groups);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            throw new IllegalArgumentException("Validation failed: " + sb);
        }
    }
}
