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

import java.util.List;
import java.util.UUID;

public class GymCrmApplication {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        Trainee joeDoe = new Trainee();
        joeDoe.setId(String.valueOf(UUID.randomUUID()));
        joeDoe.setFirstName("Joe");
        joeDoe.setLastName("Doe");

        Trainee joeDoe1 = new Trainee();
        joeDoe1.setId(String.valueOf(UUID.randomUUID()));
        joeDoe1.setFirstName("Joe");
        joeDoe1.setLastName("Doe");

        Trainer joeDoe2trainer = new Trainer();
        joeDoe2trainer.setId(String.valueOf(UUID.randomUUID()));
        joeDoe2trainer.setFirstName("Joe");
        joeDoe2trainer.setLastName("Doe");

        TraineeServiceImpl traineeService = context.getBean("traineeServiceImpl", TraineeServiceImpl.class);
        TrainerServiceImpl trainerService = context.getBean("trainerServiceImpl", TrainerServiceImpl.class);

        Trainee createdTrainee = traineeService.createTrainee(joeDoe);
        Trainee createdTrainee2 = traineeService.createTrainee(joeDoe1);

        List<Trainee> allTrainees = traineeService.getAllTrainees();
        System.out.println("All Trainees: " + allTrainees);

        Trainer createdTrainer = trainerService.createTrainer(joeDoe2trainer);

        System.out.println("Created Trainee1: " + createdTrainee);
        System.out.println("Created Trainee2: " + createdTrainee2);

        System.out.println("Created Trainer: " + createdTrainer);

        traineeService.deleteTrainee(joeDoe1.getId());

        Trainee joeDoeError = new Trainee();
        joeDoeError.setId(String.valueOf(UUID.randomUUID()));
        joeDoeError.setFirstName("Joe");
        joeDoeError.setLastName("Doe");

        System.out.println("Created Trainee3: " + createdTrainee2);


        System.out.println("All Trainees: " + allTrainees);

        traineeService.deleteTrainee(joeDoe.getId());

        System.out.println("All Trainees after deletion: " + allTrainees);

        Trainee joeDoe3 = new Trainee();
        joeDoe3.setId(String.valueOf(UUID.randomUUID()));
        joeDoe3.setFirstName("Joe");
        joeDoe3.setLastName("Doe");

        Trainee joeDoe3Created = traineeService.createTrainee(joeDoe3);

        System.out.println("Created Trainee4: " + joeDoe3Created);

    }
}
