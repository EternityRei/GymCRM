//package org.example.gymcrm.repository;
//
//import org.example.gymcrm.model.Trainer;
//import org.example.gymcrm.storage.TrainerStorage;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//public class TrainerRepositoryTest {
//    @Mock
//    private TrainerStorage trainerStorage;
//
//    @InjectMocks
//    private TrainerRepository trainerDao;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testSave() {
//        Trainer trainer = new Trainer(); // Assuming Trainer has a default constructor
//        when(trainerStorage.save(trainer)).thenReturn(trainer);
//
//        Trainer result = trainerDao.save(trainer);
//
//        verify(trainerStorage).save(trainer);
//        assertEquals(trainer, result);
//    }
//
//    @Test
//    void testFindById() {
//        Trainer trainer = new Trainer(); // Setup
//        when(trainerStorage.findById("1")).thenReturn(trainer);
//
//        Trainer result = trainerDao.findById("1");
//
//        verify(trainerStorage).findById("1");
//        assertEquals(trainer, result);
//    }
//
//    @Test
//    void testFindAll() {
//        List<Trainer> trainers = Arrays.asList(new Trainer(), new Trainer());
//        when(trainerStorage.findAll()).thenReturn(trainers);
//
//        List<Trainer> result = trainerDao.findAll();
//
//        verify(trainerStorage).findAll();
//        assertEquals(trainers, result);
//    }
//
//    @Test
//    void testDeleteById() {
//        doNothing().when(trainerStorage).deleteById("1");
//
//        trainerDao.deleteById("1");
//
//        verify(trainerStorage).deleteById("1");
//    }
//
//    @Test
//    void testFindByFirstNameAndLastNameStartingWith() {
//        List<Trainer> trainers = Arrays.asList(new Trainer(), new Trainer());
//        when(trainerStorage.findByFirstNameAndLastNameStartingWith("John", "D")).thenReturn(trainers);
//
//        List<Trainer> result = trainerDao.findByFirstNameAndLastNameStartingWith("John", "D");
//
//        verify(trainerStorage).findByFirstNameAndLastNameStartingWith("John", "D");
//        assertEquals(trainers, result);
//    }
//}
