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
        //System.out.println("Existing training types:" + trainingTypes);

        Trainee existedTrainee = traineeService.getTraineeByUsername("Joe.Doe", "coconut");
        //existedTrainee.setAddress("St. Delovaya, 12");
        //existedTrainee.setDateOfBirth(Date.valueOf("2001-01-23"));
        //Trainee existedTrainee1 = traineeService.updateTrainee(existedTrainee);
        //traineeService.updateTraineePassword(existedTrainee, "coconut");
        //traineeService.updateTraineeProfileStatus(existedTrainee);

        Trainer existedTrainer = trainerService.getTrainerByUsername("Bob.Baker", "cat").orElseThrow();
        //existedTrainer.setSpecialization(trainingTypes.get(7));
        //Trainer existedTrainer1 = trainerService.updateTrainer(existedTrainer);
        //System.out.println(existedTrainer);
        //trainerService.updateTrainerPassword(existedTrainer, "cat");
        //trainerService.updateTrainerProfileStatus(existedTrainer);

        //traineeService.deleteTraineeByUsername("Jane.Smith", "0l7Cl72l0n");

//        List<Training> trainingsForExistedTrainee1 = traineeService.getTraineeTrainings(existedTrainee, null, null, "Bob", null);
//        System.out.println("Found " + trainingsForExistedTrainee1.size() + " trainings for trainee " + existedTrainee.toString());

        // System.out.println(trainingService.getAllTrainings());

//        List<Training> trainingsForExistedTrainer = trainerService.getTrainerTrainings(existedTrainer, null, null,"Joe");
//        System.out.println("Found " + trainingsForExistedTrainer.size() + " trainings for trainer " + existedTrainer);

//        Training training = new Training();
//        training.setTrainee(existedTrainee);
//        training.setTrainer(existedTrainer);
//        training.setTrainingType(trainingTypes.get(4));
//        training.setTrainingDate(Date.valueOf("2003-12-23"));
//        training.setTrainingName("Test");
//        training.setTrainingDuration(123);
//        Training createdTraining = trainingService.createTraining(training);
//        System.out.println(createdTraining);

//        List<Trainer> notAssignedTrainers = trainerService.getTrainersNotAssignedToTraineeByUsername(existedTrainee.getUser().getUsername(), existedTrainee.getUser().getPassword());
//        System.out.println(notAssignedTrainers);

//        System.out.println("Trainee list number of trainee " + existedTrainee.getUser().getUsername() + " is " + existedTrainee.getTrainers().size());
//        List<Trainer> trainers = trainerService.getAllTrainers();
//        traineeService.addTrainersToTrainee(existedTrainee, trainers);
//        System.out.println("Trainee list number of trainee " + existedTrainee.getUser().getUsername() + " is " + existedTrainee.getTrainers().size());

        //traineeService.updateTraineePassword(existedTrainee, "newPassword");

        //Trainer trainer = trainerService.getTrainerByUsername("Alice.Adams", "KVICkyEDGi").orElseThrow();
        Trainer existedTrainer2 = trainerService.getTrainerByUsername("Kate.Kate", "abc").orElseThrow();
        Trainee existedTrainee6 = traineeService.getTraineeByUsername("Joe.Doe", "coconut");
        traineeService.addTrainersToTrainee(existedTrainee6, List.of(existedTrainer2));
//        Training training = new Training();
//        training.setTrainee(existedTrainee2);
//        training.setTrainer(trainer);
//        training.setTrainingType(trainingTypes.get(6));
//        training.setTrainingDate(Date.valueOf("2003-10-13"));
//        training.setTrainingName("Test3");
//        training.setTrainingDuration(123);
//        Training createdTraining = trainingService.createTraining(training);
//        System.out.println(createdTraining);
//
//        System.out.println("Training list: " + existedTrainee2.getTrainingList());

//        traineeService.deleteTraineeByUsername("Jane.Smith", "atgn0taX5q");
//        traineeService.deleteTraineeByUsername("Jane.Smith2", "$nEv8uxvos");

        traineeService.deleteTraineeByUsername("Joe.Doe", "coconut");



    }
}
