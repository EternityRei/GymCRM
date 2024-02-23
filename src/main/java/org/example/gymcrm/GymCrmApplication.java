package org.example.gymcrm;


import org.example.gymcrm.config.AppConfig;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.repository.TraineeDaoImpl;
import org.example.gymcrm.repository.UserDaoImpl;
import org.example.gymcrm.service.TraineeService;
import org.example.gymcrm.service.TrainerService;
import org.example.gymcrm.service.impl.TraineeServiceImpl;
import org.example.gymcrm.service.impl.TrainerServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.UUID;

public class GymCrmApplication {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        Trainee trainee = new Trainee();
        trainee.setId(String.valueOf(UUID.randomUUID()));
        trainee.setFirstName("Joe");
        trainee.setLastName("Doe");

        Trainee anotherTrainee = new Trainee();
        anotherTrainee.setId(String.valueOf(UUID.randomUUID()));
        anotherTrainee.setFirstName("Joe");
        anotherTrainee.setLastName("Doe");

        Trainer trainer = new Trainer();
        trainer.setId(String.valueOf(UUID.randomUUID()));
        trainer.setFirstName("Joe");
        trainer.setLastName("Doe");

        TraineeServiceImpl traineeService = context.getBean("traineeServiceImpl", TraineeServiceImpl.class);
        TrainerServiceImpl trainerService = context.getBean("trainerServiceImpl", TrainerServiceImpl.class);

        Trainee createdTrainee = traineeService.createTrainee(trainee);
        Trainee createdTrainee2 = traineeService.createTrainee(anotherTrainee);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        System.out.println("Created Trainee1: " + createdTrainee);
        System.out.println("Created Trainee2: " + createdTrainee2);

        System.out.println("Created Trainer: " + createdTrainer);
    }
}
