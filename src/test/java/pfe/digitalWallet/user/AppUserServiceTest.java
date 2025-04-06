package pfe.digitalWallet.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AppUserServiceTest {

    @Autowired
    private UserService userService;

    private AppUser randomAppUser;

    @BeforeEach
    void setUp() {
        // Set up a random AppUser
        randomAppUser = new AppUser();
        randomAppUser.setUsername("randomUser" + System.currentTimeMillis());
        randomAppUser.setEmail("random" + System.currentTimeMillis() + "@example.com");
        randomAppUser.setPassword("password123");
        randomAppUser.setCreatedAt(LocalDateTime.now());
        randomAppUser.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @Transactional
    void testSaveAppUser() {
        // Save the AppUser
        Optional<AppUser> savedUser = userService.save(randomAppUser);

        // Assert the user is saved and id is assigned
        assertTrue(savedUser.isPresent(), "User should be saved");
        assertNotNull(savedUser.get().getId(), "Saved user should have an id");

        // Assert other fields
        assertEquals(randomAppUser.getUsername(), savedUser.get().getUsername());
        assertEquals(randomAppUser.getEmail(), savedUser.get().getEmail());
        assertEquals(randomAppUser.getPassword(), savedUser.get().getPassword());
    }
}
