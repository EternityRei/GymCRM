//package org.example.gymcrm.repository;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.TypedQuery;
//import org.example.gymcrm.model.Trainee;
//import org.example.gymcrm.storage.TraineeStorage;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//
//public class TraineeRepositoryTest {
//    @Mock
//    private EntityManager entityManager;
//
//    @InjectMocks
//    private TraineeRepository traineeRepository; // This would be your custom implementation that uses EntityManager
//
//    @Mock
//    private TypedQuery<Trainee> query;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void findByUsername_ReturnsTrainee() {
//        String username = "user1";
//        Trainee expectedTrainee = new Trainee();
//        expectedTrainee.setUsername(username);
//
//        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(query);
//        when(query.setParameter("username", username)).thenReturn(query);
//        when(query.getSingleResult()).thenReturn(expectedTrainee);
//
//        Optional<Trainee> result = traineeRepository.findByUsername(username);
//
//        assertTrue(result.isPresent());
//        assertEquals(expectedTrainee, result.get());
//    }
//}
