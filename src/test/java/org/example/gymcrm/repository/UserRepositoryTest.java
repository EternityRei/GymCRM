//package org.example.gymcrm.repository;
//
//import org.example.gymcrm.dao.TraineeDao;
//import org.example.gymcrm.dao.TrainerDao;
//import org.example.gymcrm.model.Trainee;
//import org.example.gymcrm.model.Trainer;
//import org.example.gymcrm.model.User;
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
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//public class UserRepositoryTest {
//    @Mock
//    private TrainerDao trainerDao;
//
//    @Mock
//    private TraineeDao traineeDao;
//
//    @InjectMocks
//    private UserRepository userDao;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testSaveTrainer() {
//        Trainer trainer = new Trainer();
//        when(trainerDao.save(trainer)).thenReturn(trainer);
//
//        User result = userDao.save(trainer);
//
//        verify(trainerDao).save(trainer);
//        assertEquals(trainer, result);
//    }
//
//    @Test
//    void testSaveTrainee() {
//        Trainee trainee = new Trainee();
//        when(traineeDao.save(trainee)).thenReturn(trainee);
//
//        User result = userDao.save(trainee);
//
//        verify(traineeDao).save(trainee);
//        assertEquals(trainee, result);
//    }
//
//    @Test
//    void testFindUsers() {
//        List<Trainer> trainers = Arrays.asList(new Trainer());
//        List<Trainee> trainees = Arrays.asList(new Trainee());
//        when(trainerDao.findAll()).thenReturn(trainers);
//        when(traineeDao.findAll()).thenReturn(trainees);
//
//        List<User> result = userDao.findUsers();
//
//        assertEquals(2, result.size()); // Assuming one trainer and one trainee
//    }
//
//    @Test
//    void testDeleteByIdTrainer() {
//        Trainer trainer = new Trainer();
//        trainer.setId("1");
//
//        userDao.deleteById(trainer);
//
//        verify(trainerDao).deleteById("1");
//    }
//
//    @Test
//    void testDeleteByIdTrainee() {
//        Trainee trainee = new Trainee();
//        trainee.setId("2");
//
//        userDao.deleteById(trainee);
//
//        verify(traineeDao).deleteById("2");
//    }
//}
