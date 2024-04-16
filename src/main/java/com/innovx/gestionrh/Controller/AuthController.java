    package com.innovx.gestionrh.Controller;
    import com.innovx.gestionrh.Entity.User;
    import com.innovx.gestionrh.Repository.UserRepository;
    import com.innovx.gestionrh.Service.EmailService;
    import com.innovx.gestionrh.Service.PasswordService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.web.bind.annotation.*;

    import java.time.LocalDateTime;

    @RestController
    @RequestMapping("/api/v1/auth")
    public class AuthController {

        @Autowired
        private PasswordService passwordService;

        @Autowired
        private UserRepository userRepository; // Assuming you have a UserRepository for database operations

        @Autowired
        private BCryptPasswordEncoder passwordEncoder; // Assuming you're using BCrypt for password hashing


        @Autowired
        private EmailService emailService; // Inject EmailService

        // Login endpoint
        @PostMapping("/login")
        public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
            // Find User by email
            User user = userRepository.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            // Check if password matches
            if (passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
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

                // Set the new password for the user
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setLastPasswordChange(LocalDateTime.now());

                // Save the user to the database
                userRepository.save(user);

                // Send the new password via email
                emailService.sendSimpleMessage(user.getEmail(), "Welcome to Our Platform", "Your password is: " + newPassword);



                return ResponseEntity.ok("User created successfully. Password sent to the user's email.");
            } catch (Exception e) {
                // Log the error for debugging purposes
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user: " + e.getMessage());
            }
        }
    }
