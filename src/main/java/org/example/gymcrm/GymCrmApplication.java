package org.example.gymcrm;


import org.example.gymcrm.config.AppConfig;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.model.TrainingType;
import org.example.gymcrm.repository.TraineeDaoImpl;
import org.example.gymcrm.repository.UserDaoImpl;
import org.example.gymcrm.service.TraineeService;
import org.example.gymcrm.service.TrainerService;
import org.example.gymcrm.service.TrainingService;
import org.example.gymcrm.service.impl.TraineeServiceImpl;
import org.example.gymcrm.service.impl.TrainerServiceImpl;
import org.example.gymcrm.service.impl.TrainingServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public class GymCrmApplication {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        TraineeServiceImpl traineeService = context.getBean("traineeServiceImpl", TraineeServiceImpl.class);
        TrainerServiceImpl trainerService = context.getBean("trainerServiceImpl", TrainerServiceImpl.class);
        TrainingService trainingService = context.getBean("trainingServiceImpl", TrainingServiceImpl.class);

        Trainee trainee = traineeService.getTrainee("2bc140d7-5873-4a26-ac89-1a131781df18");
        Trainer trainer = trainerService.getTrainer("5f4f3af3-a423-4d5b-8b76-5f41f24ea85b");

        Training training = new Training();
        training.setTrainingId(String.valueOf(UUID.randomUUID()));
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        Training createdTraining = trainingService.createTraining(training);
        System.out.println("Created Training: " + createdTraining);

//        Trainee joeDoe = new Trainee();
//        joeDoe.setId(String.valueOf(UUID.randomUUID()));
//        joeDoe.setFirstName("Joe");
//        joeDoe.setLastName("Doe");
//
//        Trainee joeDoe1 = new Trainee();
//        joeDoe1.setId(String.valueOf(UUID.randomUUID()));
//        joeDoe1.setFirstName("Joe");
//        joeDoe1.setLastName("Doe");
//
//        Trainer joeDoe2trainer = new Trainer();
//        joeDoe2trainer.setId(String.valueOf(UUID.randomUUID()));
//        joeDoe2trainer.setFirstName("Joe");
//        joeDoe2trainer.setLastName("Doe");
//
//        TraineeServiceImpl traineeService = context.getBean("traineeServiceImpl", TraineeServiceImpl.class);
//        TrainerServiceImpl trainerService = context.getBean("trainerServiceImpl", TrainerServiceImpl.class);
//        TrainingService trainingService = context.getBean("trainingServiceImpl", TrainingServiceImpl.class);
//
//        Trainee createdTrainee = traineeService.createTrainee(joeDoe);
//        Trainee createdTrainee2 = traineeService.createTrainee(joeDoe1);
//
//        List<Trainee> allTrainees = traineeService.getAllTrainees();
//        System.out.println("All Trainees: " + allTrainees);
//
//        Trainer createdTrainer = trainerService.createTrainer(joeDoe2trainer);
//
//        System.out.println("Created Trainee1: " + createdTrainee);
//        System.out.println("Created Trainee2: " + createdTrainee2);
//
//        System.out.println("Created Trainer: " + createdTrainer);
//
//        traineeService.deleteTrainee(joeDoe1.getId());
//
//        Trainee joeDoeError = new Trainee();
//        joeDoeError.setId(String.valueOf(UUID.randomUUID()));
//        joeDoeError.setFirstName("Joe");
//        joeDoeError.setLastName("Doe");
//
//        System.out.println("Created Trainee3: " + createdTrainee2);
//
//
//        System.out.println("All Trainees: " + allTrainees);
//
//        traineeService.deleteTrainee(joeDoe.getId());
//
//        System.out.println("All Trainees after deletion: " + allTrainees);
//
//        Trainee joeDoe3 = new Trainee();
//        joeDoe3.setId(String.valueOf(UUID.randomUUID()));
//        joeDoe3.setFirstName("Joe");
//        joeDoe3.setLastName("Doe");
//
//        Trainee joeDoe3Created = traineeService.createTrainee(joeDoe3);
//
//        System.out.println("Created Trainee4: " + joeDoe3Created);
//
//        Training training = new Training();
//        training.setTrainingId(String.valueOf(UUID.randomUUID()));
//        training.setTrainer(createdTrainer);
//        training.setTrainee(joeDoe3Created);
//        training.setTrainingType(new TrainingType("1", "Cardio"));
//        training.setTrainingName("Cardio Basics");
//        training.setTrainingDate(Date.valueOf("2024-03-01"));
//        training.setTrainingDuration(60);
//
//        Training createdTraining = trainingService.createTraining(training);
//        System.out.println("Created Training: " + createdTraining);

    }
}
