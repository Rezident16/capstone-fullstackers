package stocks.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import stocks.models.User;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // Ensure Spring Boot context is loaded for tests
public class UserJdbcTemplateRepositoryTest {

    @Autowired
    private UserJdbcTemplateRepository userRepository;

    @Autowired
    private KnownGoodState knownGoodState;  // Ensure the bean is injected correctly

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        // Reset the database to a known good state before each test
        knownGoodState.set();  // Ensure this is not null
    }

    @Test
    void shouldFindAllUsers() {
        var users = userRepository.findAll();
        assertNotNull(users);
        assertTrue(users.size() > 0, "The user list should not be empty.");
    }

    @Test
    void shouldFindUserById() {
        User user = userRepository.findById(1);
        assertNotNull(user, "User should be found.");
        assertEquals(1, user.getUserId(), "User ID should match.");
        assertEquals("John", user.getFirstName(), "First name should match.");
    }

    @Test
    void shouldFindUserByUsername() {
        User user = userRepository.findByUsername("johndoe");
        assertNotNull(user, "User should be found by username.");
        assertEquals("johndoe", user.getUsername(), "Username should match.");
        assertEquals("John", user.getFirstName(), "First name should match.");
    }

    @Test
    void shouldFindUserByEmail() {
        User user = userRepository.findByEmail("janesmith@example.com");
        assertNotNull(user, "User should be found by email.");
        assertEquals("janesmith@example.com", user.getEmail(), "Email should match.");
        assertEquals("Jane", user.getFirstName(), "First name should match.");
    }

    @Test
    void shouldAddUser() {
        User newUser = new User();
        newUser.setFirstName("Alice");
        newUser.setLastName("Johnson");
        newUser.setPassword("password123");
        newUser.setUsername("alicejohnson");
        newUser.setEmail("alice.johnson@example.com");
        newUser.setRoleId(2);

        boolean added = userRepository.add(newUser);
        assertTrue(added, "User should be added successfully.");
    }

    @Test
    void shouldUpdateUser() {
        User existingUser = userRepository.findByUsername("johndoe");
        existingUser.setEmail("updatedemail@example.com");

        boolean updated = userRepository.update(existingUser);
        assertTrue(updated, "User should be updated successfully.");

        User updatedUser = userRepository.findByUsername("johndoe");
        assertEquals("updatedemail@example.com", updatedUser.getEmail(), "Email should be updated.");
    }

    @Test
    void shouldDeleteUser() {
        User existingUser = userRepository.findByUsername("alicejohnson");
        boolean deleted = userRepository.deleteById(existingUser.getUserId());
        assertTrue(deleted);

        // Verify the user is deleted
        User deletedUser = userRepository.findByUsername("alicejohnson");
        assertNull(deletedUser);
    }
}
