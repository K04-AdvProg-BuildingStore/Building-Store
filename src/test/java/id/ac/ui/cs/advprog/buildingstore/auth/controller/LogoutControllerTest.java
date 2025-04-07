package id.ac.ui.cs.advprog.buildingstore.auth.controller;

import id.ac.ui.cs.advprog.buildingstore.auth.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LogoutControllerTest {

    @Mock
    private LogoutService logoutService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private LogoutController logoutController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnSuccessMessageWhenLogoutIsCalled() throws IOException {
        ResponseEntity<?> responseEntity = logoutController.logout(request, response);

        verify(logoutService, times(1)).logout(request, response);
        assertEquals("Logged out successfully.", responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void shouldHandleIOExceptionThrownByLogoutService() throws IOException {
        doThrow(new IOException("Logout failure")).when(logoutService).logout(request, response);

        try {
            logoutController.logout(request, response);
        } catch (IOException e) {
            assertEquals("Logout failure", e.getMessage());
            verify(logoutService, times(1)).logout(request, response);
        }
    }
}
