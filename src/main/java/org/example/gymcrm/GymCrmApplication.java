package org.example.gymcrm;


import org.example.gymcrm.config.AppConfig;
import org.example.gymcrm.model.*;
import org.example.gymcrm.service.*;
import org.example.gymcrm.service.impl.TraineeServiceImpl;
import org.example.gymcrm.service.impl.TrainerServiceImpl;
import org.example.gymcrm.service.impl.TrainingServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GymCrmApplication {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve the service beans
        TraineeService traineeService = context.getBean(TraineeService.class);
        UserService userService = context.getBean(UserService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingTypeService trainingTypeService = context.getBean(TrainingTypeService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);

        //TrainingType trainingType = trainingTypeService.findById("1").orElseThrow();
//        trainingTypeService.createTrainingType("Fitness");
//        trainingTypeService.createTrainingType("Strength Training");
//        trainingTypeService.createTrainingType("Cardiovascular Training");
//        trainingTypeService.createTrainingType("HIIT (High-Intensity Interval Training)");
//        trainingTypeService.createTrainingType("Circuit Training");
//        trainingTypeService.createTrainingType("Flexibility Training");
//        trainingTypeService.createTrainingType("Functional Training");
//        trainingTypeService.createTrainingType("CrossFit");

        List<TrainingType> trainingTypes = trainingTypeService.findAll();


        // Create the first user and associate with a trainer
        User user1 = new User();
        user1.setFirstName("Alice");
        user1.setLastName("Adams");
        user1.setUsername(userService.createUsername(user1.getFirstName(), user1.getLastName()));
        user1.setPassword(userService.generateRandomPassword());
        user1.setActive(true);

        Trainer trainer1 = new Trainer();
        trainer1.setUser(user1);
        trainer1.setSpecialization(trainingTypes.get(1));

        //trainerService.createTrainer(trainer1.getUser().getFirstName(), trainer1.getUser().getLastName(), trainer1.getSpecialization());

        // Create the second user and associate with a trainer
        User user2 = new User();
        user2.setFirstName("Bob");
        user2.setLastName("Baker");
        user2.setUsername(userService.createUsername(user2.getFirstName(), user2.getLastName()));
        user2.setPassword(userService.generateRandomPassword());
        user2.setActive(true);

        Trainer trainer2 = new Trainer();
        trainer2.setUser(user2);
        trainer2.setSpecialization(trainingTypes.get(2));

        //trainerService.createTrainer(trainer2.getUser().getFirstName(), trainer2.getUser().getLastName(), trainer2.getSpecialization());

        //System.out.println("Two trainers created: " + trainer1.getUser().getUsername() + " and " + trainer2.getUser().getUsername());

        User user3 = new User();
        user3.setFirstName("Joe");
        user3.setLastName("Doe");
        user3.setUsername(userService.createUsername(user3.getFirstName(), user3.getLastName()));
        user3.setPassword(userService.generateRandomPassword());
        user3.setActive(true);

        Trainee trainee1 = new Trainee();
        trainee1.setUser(user3);

        //traineeService.createTrainee(trainee1);

        User user4 = new User();
        user4.setFirstName("Jane");
        user4.setLastName("Smith");
        user4.setUsername(userService.createUsername(user4.getFirstName(), user4.getLastName()));
        user4.setPassword(userService.generateRandomPassword());
        user4.setActive(true);

        Trainee trainee2 = new Trainee();
        trainee2.setUser(user4);

        //traineeService.createTrainee(trainee2);

        //System.out.println("Two trainee created: " + trainee1.getUser().getUsername() + " and " + trainee1.getUser().getUsername());
        System.out.println("Existing training types:" + trainingTypes);

        Trainee existedTrainee = traineeService.getTraineeByUsername("Joe.Doe");
        existedTrainee.setAddress("St. Delovaya, 12");
        Trainee existedTrainee1 = traineeService.updateTrainee(existedTrainee);
        traineeService.updateTraineePassword(String.valueOf(existedTrainee1.getId()), "newPass");
        //traineeService.updateTraineeProfileStatus(String.valueOf(existedTrainee1.getId()));

        Trainer existedTrainer = trainerService.getTrainerByUsername("Bob.Baker").orElseThrow();
        trainerService.updateTrainerProfileStatus(String.valueOf(existedTrainer.getId()));

        //traineeService.deleteTraineeByUsername("Jane.Smith");

        //List<Training> trainingsForExistedTrainee1 = traineeService.getTraineeTrainings(existedTrainee.getUser().getUsername(), Date.valueOf("2003-02-12"), Date.valueOf("2003-02-13"), "Bob.Baker", "Fitness");
        //System.out.println("Found " + trainingsForExistedTrainee1.size() + " trainings for trainee " + existedTrainee1.toString());

        //List<Training> trainingsForExistedTrainer = trainerService.getTrainerTrainings(existedTrainer.getUser().getUsername(), Date.valueOf("2004-02-13"), Date.valueOf("2004-02-15"), "Alice");
        //System.out.println("Found " + trainingsForExistedTrainer.size() + " trainings for trainer " + existedTrainer);

        Training training = new Training();
        training.setTrainee(existedTrainee1);
        training.setTrainer(existedTrainer);
        training.setTrainingType(trainingTypes.get(0)); // or other index based on training type
        training.setTrainingDate(Date.valueOf("2003-12-20")); // or your desired date
        training.setTrainingName("Stretching");
        training.setTrainingDuration(123);
        //Training createdTraining = trainingService.createTraining(training);
        //System.out.println(createdTraining);

        //List<Trainer> notAssignedTrainers = trainerService.getTrainersNotAssignedToTraineeByUsername(existedTrainee.getUser().getUsername());
        //System.out.println(notAssignedTrainers);
    }
}
