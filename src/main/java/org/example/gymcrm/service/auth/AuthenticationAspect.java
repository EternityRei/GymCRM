package org.example.gymcrm.service.auth;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class AuthenticationAspect {

    @Autowired
    private AuthenticationService authenticationService;

    @Pointcut("@annotation(org.example.gymcrm.annotation.Authenticate)")
    public void authenticate() {
    }

    @Before("authenticate()")
    public void beforeAdvice(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String username = (String) args[0];
        String password = (String) args[1];

        if (!authenticationService.authenticate(username, password)) {
            throw new SecurityException("Invalid username or password");
        }
    }
}
