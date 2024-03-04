package org.example.gymcrm;


import org.example.gymcrm.config.AppConfig;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.service.TrainingService;
import org.example.gymcrm.service.impl.TraineeServiceImpl;
import org.example.gymcrm.service.impl.TrainerServiceImpl;
import org.example.gymcrm.service.impl.TrainingServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.UUID;

public class GymCrmApplication {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    }
}
