package org.example.gymcrm.service.auth;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationAspectTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private AuthenticationCredentials authenticationCredentials;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private Signature signature; // Mocked Signature

    @InjectMocks
    private AuthenticationAspect authenticationAspect;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(joinPoint.getSignature()).thenReturn(signature); // Return the mocked Signature from the joinPoint
        when(signature.getName()).thenReturn("testMethod"); // Return a dummy method name when getName() is called
    }

    @Test
    void whenAuthenticationSucceeds_ProceedWithMethodExecution() throws Throwable {
        // Arrange
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "validUser");
        credentials.put("password", "validPassword");
        when(authenticationCredentials.getCredentials(any())).thenReturn(credentials);
        when(authenticationService.authenticate("validUser", "validPassword")).thenReturn(true);
        when(joinPoint.proceed()).thenReturn(null); // Assuming void method, adjust as needed

        // Act
        Object result = authenticationAspect.aroundAdvice(joinPoint);

        // Assert
        verify(joinPoint, times(1)).proceed();
        assertNull(result); // Adjust based on the actual method's return type
    }

    @Test
    void whenAuthenticationFails_ThrowSecurityException() {
        // Arrange
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "invalidUser");
        credentials.put("password", "invalidPassword");
        when(authenticationCredentials.getCredentials(any())).thenReturn(credentials);
        when(authenticationService.authenticate("invalidUser", "invalidPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(SecurityException.class, () -> authenticationAspect.aroundAdvice(joinPoint),
                "Expected a SecurityException to be thrown when authentication fails.");
    }
}
