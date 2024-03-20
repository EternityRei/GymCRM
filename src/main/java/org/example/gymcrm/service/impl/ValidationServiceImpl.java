package org.example.gymcrm.service.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.example.gymcrm.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidationServiceImpl implements ValidationService {

    private final Validator validator;
    private static final Logger logger = LoggerFactory.getLogger(ValidationServiceImpl.class);


    @Autowired
    public ValidationServiceImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public <T> void validateEntity(T entity, Class<?>... groups) {
        logger.debug("Starting validation for entity of type: {}", entity.getClass().getName());

        Set<ConstraintViolation<T>> violations = validator.validate(entity, groups);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> violation : violations) {
                sb.append(violation.getPropertyPath()).append(" ").append(violation.getMessage()).append("\n");
                logger.warn("Validation violation for entity of type: {}, property: {}, message: {}",
                        entity.getClass().getName(), violation.getPropertyPath(), violation.getMessage());
            }
            logger.error("Validation failed for entity of type: {}. Errors: {}", entity.getClass().getName(), sb);
            throw new IllegalArgumentException("Validation failed: " + sb.toString().trim());
        } else {
            logger.debug("No validation violations found for entity of type: {}", entity.getClass().getName());
        }
    }

}
