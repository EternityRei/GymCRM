package org.example.gymcrm.service.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValidationServiceImplTest {
    @Mock
    private Validator validator;

    @InjectMocks
    private ValidationServiceImpl validationService;

    private static class TestEntity {
        private String property;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }
    }

    @Test
    void whenValidateEntityWithNoViolations_thenSucceed() {
        TestEntity entity = new TestEntity();
        entity.setProperty("validValue");

        when(validator.validate(entity)).thenReturn(Collections.emptySet());

        assertDoesNotThrow(() -> validationService.validateEntity(entity),
                "Validation should pass without throwing an exception.");
    }

    @Test
    void whenValidateEntityWithViolations_thenThrowIllegalArgumentException() {
        TestEntity entity = new TestEntity();
        entity.setProperty("invalidValue");

        Set<ConstraintViolation<TestEntity>> violations = new HashSet<>();
        ConstraintViolation<TestEntity> violation = mock(ConstraintViolation.class);

        // Mocking Path and its behavior
        jakarta.validation.Path pathMock = mock(jakarta.validation.Path.class);
        when(pathMock.toString()).thenReturn("property");

        when(violation.getPropertyPath()).thenReturn(pathMock);
        when(violation.getMessage()).thenReturn("must not be blank");
        violations.add(violation);

        when(validator.validate(entity)).thenReturn(violations);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validationService.validateEntity(entity),
                "Validation should fail and throw IllegalArgumentException.");

        assertTrue(exception.getMessage().contains("must not be blank"),
                "Exception message should contain the violation message.");
    }

}
