package org.example.gymcrm.service.auth;

import jakarta.persistence.EntityNotFoundException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.User;
import org.example.gymcrm.service.TraineeService;
import org.example.gymcrm.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Aspect
@Component
public class AuthenticationAspect {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainerService trainerService;

    @Pointcut("@annotation(org.example.gymcrm.annotation.Authenticate) && !within(AuthenticationAspect)")
    public void authenticate() {
    }

    @Around("authenticate()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        boolean authenticated = false;

        for (Object arg : args) {
            if (arg instanceof Trainee trainee) {
                authenticated = authenticateTrainee(trainee);
            } else if (arg instanceof Trainer trainer) {
                authenticated = authenticateTrainer(trainer);
            } else if (arg instanceof String) {
                authenticated = authenticateByIdOrUsername(String.valueOf(arg));
            }
            if (authenticated) {
                break;
            }
        }

        if (!authenticated) {
            throw new SecurityException("Authentication failed");
        }

        return joinPoint.proceed();
    }

    private boolean authenticateTrainer(Trainer trainer) {
        return authenticationService.authenticate(trainer.getUser().getUsername(), trainer.getUser().getPassword());
    }

    private boolean authenticateTrainee(Trainee trainee) {
        return authenticationService.authenticate(trainee.getUser().getUsername(), trainee.getUser().getPassword());
    }

    private boolean authenticateByIdOrUsername(String identifier) {
       User user = null;
        if (identifier.matches("\\d+")) { // Simplistic check if identifier is numeric (ID).
            try {
                Trainee trainee = traineeService.getTraineeAuthentication(identifier).orElseThrow(() -> new EntityNotFoundException("Trainee not found"));
                user = trainee.getUser();
            } catch (EntityNotFoundException e) {
                Trainer trainer = trainerService.getTrainerAuthentication(identifier).orElseThrow(() -> new RuntimeException("No entity was found in entire Database"));
                user = trainer.getUser();
            }
            Optional<Trainee> trainee = traineeService.getTraineeAuthentication(identifier);

        } else { // Assume it's a username.
            try{
                Trainee trainee = traineeService.getTraineeByUsernameAuthentication(identifier).orElseThrow(() -> new EntityNotFoundException("Trainee not found"));
                user = trainee.getUser();
            } catch (EntityNotFoundException e) {
                Trainer trainer = trainerService.getTrainerByUsernameAuthenticate(identifier).orElseThrow(() -> new RuntimeException("No entity was found in entire Database"));
                user = trainer.getUser();
            }
        }

        if (user == null) {
            return false;
        }

        // Example authentication check. Adjust based on how your AuthenticationService is implemented.
        return authenticationService.authenticate(user.getUsername(), user.getPassword());
    }
}
