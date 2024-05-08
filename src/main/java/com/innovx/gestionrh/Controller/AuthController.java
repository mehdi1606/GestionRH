    package com.innovx.gestionrh.Controller;
    import com.innovx.gestionrh.Entity.User;
    import com.innovx.gestionrh.Repository.UserRepository;
    import com.innovx.gestionrh.Service.EmailService;
    import com.innovx.gestionrh.Service.PasswordService;
    import com.innovx.gestionrh.payload.request.LoginRequest;
    import com.innovx.gestionrh.payload.response.JwtResponse;
    import com.innovx.gestionrh.security.jwt.JwtUtils;
    import com.innovx.gestionrh.security.services.UserDetailsImpl;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.AuthenticationException;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.web.bind.annotation.*;

    import javax.validation.Valid;
    import java.time.LocalDateTime;
    import java.util.Collections;
    import java.util.List;
    import java.util.stream.Collectors;

    import static com.azure.core.implementation.http.rest.RestProxyUtils.LOGGER;

    @RestController
    @RequestMapping("/api/v1/auth")
    public class AuthController {
        @Autowired
        AuthenticationManager authenticationManager;
        @Autowired
        PasswordEncoder encoder;

        @Autowired
        JwtUtils jwtUtils;
        @Autowired
        private PasswordService passwordService;

        @Autowired
        private UserRepository userRepository; // Assuming you have a UserRepository for database operations
        // Login endpoint
        private static final Logger logger = LoggerFactory.getLogger(AuthController.class);






        @Autowired
        private EmailService emailService; // Inject EmailService
        @GetMapping("/users")
        public ResponseEntity<List<User>> getAllUsers() {
            try {
                // Retrieve all users from the database
                List<User> users = userRepository.findAll();
                return ResponseEntity.ok(users);
            } catch (Exception e) {
                // Log the error for debugging purposes
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }


        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
            try {
                logger.info("Attempting to authenticate user: {}", loginRequest.getEmail());
                logger.info("Attempting to authenticate password: {}", loginRequest.getPassword());
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
                );
                logger.info("Attempting to authenticate password: {}", authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.info("Authentication successful for user: {}", loginRequest.getEmail());

                String jwt = jwtUtils.generateJwtToken(authentication);
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                List<String> roles = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());

                String jwtToken = jwtUtils.generateJwtToken(authentication); // Generate JWT token
                JwtResponse jwtResponse = new JwtResponse(
                        jwtToken,
                        userDetails.getId(),
                        userDetails.getLastname(),
                        userDetails.getFirstname(),
                        userDetails.getEmail(),
                        userDetails.getUsername(),
                        Collections.singletonList(userDetails.getUserRole())

                );

                logger.info("JWT Token and user details fetched successfully for user: {}", loginRequest.getEmail());
                return ResponseEntity.ok(jwtResponse);

            } catch (AuthenticationException e) {
                logger.error("Authentication exception for user: {} with error: {}", loginRequest.getEmail(), e.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } catch (Exception e) {
                logger.error("Unexpected error during login for user: {} with error: {}", loginRequest.getEmail(), e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        // Logout endpoint
        @PostMapping("/logout")
        public String logout() {
            // Implement logout logic here (optional depending on your application's requirements)
            SecurityContextHolder.clearContext();
            return "Logout successful";
        }
        // Password modification endpoint
        @PutMapping("/password")
        public String modifyPassword(@RequestParam String email, @RequestParam String oldPassword, @RequestParam String newPassword) {
            // Find user by email (you can implement your logic to find user by email)
            User user = null; // userRepository.findByEmail(email);
            if (user != null) {
                // Call password service to modify password
                passwordService.modifyPassword(user, oldPassword, newPassword);
                return "Password modified successfully";
            } else {
                return "User not found";
            }
        }

        @PostMapping("/register")
        public ResponseEntity<String> registerUser(@RequestBody User user) {
            try {
                // Check if the user with the given email already exists
                if (userRepository.existsByEmail(user.getEmail())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with this email already exists");
                }

                // Generate a new password
                String newPassword = passwordService.generatePassword();
                LOGGER.info("password: " + newPassword);

                // Set the new password for the user
                user.setPassword(encoder.encode(newPassword));
                user.setLastPasswordChange(LocalDateTime.now());

                // Save the user to the database
                userRepository.save(user);
                LOGGER.info("Email: " + user.getEmail());

                // Customize the email template
                String emailSubject = "Welcome to Our Platform";
                String emailBody = "Hello " + user.getLastName()+ " " + user.getFirstName()+",\n\n"
                        + "Welcome to Our Platform. Your password is: " + newPassword + "\n\n"
                        + "Please log in using this password and change it immediately.\n\n"
                        + "Best regards,\n"
                        + "Your Platform Team";

                // Send the customized email
                emailService.sendEmail(user.getEmail(), emailSubject, emailBody);

                return ResponseEntity.ok("User created successfully. Password sent to the user's email.");
            } catch (Exception e) {
                // Log the error for debugging purposes
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user: " + e.getMessage());
            }
        }

    }
