package org.example.gymcrm.service.auth;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.service.TraineeService;
import org.example.gymcrm.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


@Aspect
@Component
public class AuthenticationAspect {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationAspect.class);

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationCredentials authenticationCredentials;

    @Pointcut("@annotation(org.example.gymcrm.annotation.Authenticate) && !within(AuthenticationAspect)")
    public void authenticate() {
    }

    @Around("authenticate()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("Authentication advice started for method: {}", joinPoint.getSignature().getName());

        Object[] args = joinPoint.getArgs();
        Map<String, String> credentials = authenticationCredentials.getCredentials(args);
        String username = credentials.get("username");
        String password = credentials.get("password");

        boolean authenticated = authenticationService.authenticate(username, password);

        if (!authenticated) {
            throw new SecurityException("Authentication failed for user: " + username);
        }

        return joinPoint.proceed();
    }
}
