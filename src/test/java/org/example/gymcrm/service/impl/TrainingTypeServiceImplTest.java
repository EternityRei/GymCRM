package org.example.gymcrm.service.impl;

import org.example.gymcrm.model.TrainingType;
import org.example.gymcrm.repository.TrainingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceImplTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Test
    void whenFindById_thenSuccess() {
        Long id = 1L;
        Optional<TrainingType> expectedTrainingType = Optional.of(new TrainingType(id, "Strength"));
        when(trainingTypeRepository.findById(id)).thenReturn(expectedTrainingType);

        Optional<TrainingType> result = trainingTypeService.findById(String.valueOf(id));

        assertTrue(result.isPresent());
        assertEquals("Strength", result.get().getName());
    }

    @Test
    void whenFindByIdAndTrainingTypeNotFound_thenEmptyOptional() {
        Long id = 99L; // Assuming no TrainingType with this ID exists
        when(trainingTypeRepository.findById(id)).thenReturn(Optional.empty());

        Optional<TrainingType> result = trainingTypeService.findById(String.valueOf(id));

        assertFalse(result.isPresent());
    }

    @Test
    void whenFindAll_thenSuccess() {
        List<TrainingType> expectedTrainingTypes = List.of(
                new TrainingType(1L, "Strength"),
                new TrainingType(2L, "Cardio")
        );
        when(trainingTypeRepository.findAll()).thenReturn(expectedTrainingTypes);

        List<TrainingType> result = trainingTypeService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Strength", result.get(0).getName());
        assertEquals("Cardio", result.get(1).getName());
    }

    @Test
    void whenFindAllAndNoTrainingTypesExist_thenEmptyList() {
        when(trainingTypeRepository.findAll()).thenReturn(List.of());

        List<TrainingType> result = trainingTypeService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void whenFindByIdWithInvalidIdFormat_thenHandleErrorGracefully() {
        String invalidId = "invalid";

        assertThrows(NumberFormatException.class, () -> trainingTypeService.findById(invalidId));
        verify(trainingTypeRepository, never()).findById(anyLong());
    }

    @Test
    void whenRepositoryThrowsException_thenServiceHandlesItGracefully() {
        when(trainingTypeRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> trainingTypeService.findAll());
        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void whenFindByIdWithNullId_thenHandleGracefully() {
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.findById(null));
        verify(trainingTypeRepository, never()).findById(anyLong());
    }

}
