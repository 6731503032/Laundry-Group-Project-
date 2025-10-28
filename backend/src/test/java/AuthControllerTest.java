import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for AuthController login functionality.
 * Tests various login scenarios including:
 * - Successful login with valid credentials
 * - Failed login with invalid credentials
 * - Missing studentId/email validation
 * - Missing password validation
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private service.UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();

    @BeforeEach
    public void setup() {
        if (userService == null) {
            MockitoAnnotations.openMocks(this);
        }
        userService = mock(service.UserService.class);
        
        AuthController authController = new AuthController(userService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    /**
     * Test successful login with valid studentId and password
     */
    @Test
    public void testLoginSuccess_WithStudentId() throws Exception {
        // Arrange
        model.User mockUser = new model.User();
        mockUser.setId(1L);
        mockUser.setStudentId("S12345");
        mockUser.setName("John Doe");
        mockUser.setEmail("john@example.com");
        mockUser.setPassword("password123");
        mockUser.setRole(model.Role.STUDENT);

        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.studentId = "S12345";
        loginRequest.password = "password123";

        when(userService.authenticate("S12345", null, "password123"))
                .thenReturn(Optional.of(mockUser));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.studentId").value("S12345"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.role").value("STUDENT"));

        verify(userService, times(1)).authenticate("S12345", null, "password123");
    }

    /**
     * Test successful login with valid email and password
     */
    @Test
    public void testLoginSuccess_WithEmail() throws Exception {
        // Arrange
        model.User mockUser = new model.User();
        mockUser.setId(2L);
        mockUser.setStudentId("S67890");
        mockUser.setName("Jane Smith");
        mockUser.setEmail("jane@example.com");
        mockUser.setPassword("hashedPassword");
        mockUser.setRole(model.Role.MANAGER);

        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.email = "jane@example.com";
        loginRequest.password = "password456";

        when(userService.authenticate(null, "jane@example.com", "password456"))
                .thenReturn(Optional.of(mockUser));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.role").value("MANAGER"));

        verify(userService, times(1)).authenticate(null, "jane@example.com", "password456");
    }

    /**
     * Test failed login with invalid credentials (no user found)
     */
    @Test
    public void testLoginFailure_InvalidCredentials() throws Exception {
        // Arrange
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.studentId = "S99999";
        loginRequest.password = "wrongPassword";

        when(userService.authenticate("S99999", null, "wrongPassword"))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));

        verify(userService, times(1)).authenticate("S99999", null, "wrongPassword");
    }

    /**
     * Test login validation: missing both studentId and email
     */
    @Test
    public void testLoginValidation_MissingStudentIdAndEmail() throws Exception {
        // Arrange
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.studentId = "";
        loginRequest.email = "";
        loginRequest.password = "password123";

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("studentId or email required"));

        verify(userService, never()).authenticate(anyString(), anyString(), anyString());
    }

    /**
     * Test login validation: missing password
     */
    @Test
    public void testLoginValidation_MissingPassword() throws Exception {
        // Arrange
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.studentId = "S12345";
        loginRequest.password = "";

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("password required"));

        verify(userService, never()).authenticate(anyString(), anyString(), anyString());
    }

    /**
     * Test login with null password in JSON (not provided)
     */
    @Test
    public void testLoginValidation_NullPassword() throws Exception {
        // Arrange
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.studentId = "S12345";
        loginRequest.password = null;

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("password required"));

        verify(userService, never()).authenticate(anyString(), anyString(), anyString());
    }
}
