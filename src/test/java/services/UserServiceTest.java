package services;

import entities.User;
import exceptions.DBexceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import persistence.UserMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest { // Tester login med rigtige og forkerte oplysninger

    private UserService userService;

    // Falsk mapper så testen ikke skal bruge databasen
    private class FakeUserMapper extends UserMapper {
        private final String hash = BCrypt.hashpw("1234", BCrypt.gensalt());

        FakeUserMapper() {
            super(null);
        }

        @Override
        public String getPasswordHash(String email) {
            return email.equals("test@cupcake.com") ? hash : null;
        }

        @Override
        public User getByEmail(String email) {
            return new User(1, email, "customer", 500);
        }
    }

    @BeforeEach
    void setUp() {
        userService = new UserService(new FakeUserMapper());
    }

    @Test
    void loginWithCorrectCredentials() throws DBexceptions {
        User user = userService.login("test@cupcake.com", "1234");
        assertEquals("test@cupcake.com", user.getEmail());
    }

    @Test
    void loginIgnoresUppercaseAndSpaces() throws DBexceptions {
        User user = userService.login("  Test@Cupcake.com ", "1234");
        assertEquals("test@cupcake.com", user.getEmail());
    }

    @Test
    void loginWithWrongPassword() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.login("test@cupcake.com", "forkert"));
    }

    @Test
    void loginWithUnknownEmail() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.login("findes@ikke.dk", "1234"));
    }

    @Test
    void loginWithMissingCredentials() {
        assertThrows(IllegalArgumentException.class, () -> userService.login(null, "1234"));
        assertThrows(IllegalArgumentException.class, () -> userService.login("test@cupcake.com", null));
    }
}
