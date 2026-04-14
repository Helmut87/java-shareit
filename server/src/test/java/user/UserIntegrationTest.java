package user;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ShareItServer.class)
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager entityManager;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setName("Integration User");
        userDto.setEmail("integration@example.com");
    }

    @Test
    void createAndFindUser_ShouldWorkWithDatabase() {
        UserDto created = userService.create(userDto);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Integration User");
        assertThat(created.getEmail()).isEqualTo("integration@example.com");

        UserDto found = userService.findById(created.getId());
        assertThat(found.getEmail()).isEqualTo("integration@example.com");
        assertThat(found.getName()).isEqualTo("Integration User");
    }

    @Test
    void updateUser_ShouldUpdateInDatabase() {
        UserDto created = userService.create(userDto);

        UserDto updateDto = new UserDto();
        updateDto.setName("Updated Name");
        updateDto.setEmail("updated@example.com");

        UserDto updated = userService.update(created.getId(), updateDto);

        assertThat(updated.getName()).isEqualTo("Updated Name");
        assertThat(updated.getEmail()).isEqualTo("updated@example.com");

        User userFromDb = userRepository.findById(created.getId()).orElseThrow();
        assertThat(userFromDb.getName()).isEqualTo("Updated Name");
        assertThat(userFromDb.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    void deleteUser_ShouldRemoveFromDatabase() {
        UserDto created = userService.create(userDto);

        userService.deleteById(created.getId());

        assertThat(userRepository.findById(created.getId())).isEmpty();
    }

    @Test
    void findAllUsers_ShouldReturnAllFromDatabase() {
        userService.create(userDto);

        UserDto secondUser = new UserDto();
        secondUser.setName("Second User");
        secondUser.setEmail("second@example.com");
        userService.create(secondUser);

        List<UserDto> users = userService.findAll();

        assertThat(users).hasSize(2);
        assertThat(users).extracting(UserDto::getName)
                .containsExactlyInAnyOrder("Integration User", "Second User");
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        UserDto created = userService.create(userDto);

        UserDto found = userService.findById(created.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(created.getId());
        assertThat(found.getName()).isEqualTo("Integration User");
    }

    @Test
    void findByEmail_ShouldWorkCorrectly() {
        userService.create(userDto);

        boolean exists = userRepository.existsByEmail("integration@example.com");
        assertThat(exists).isTrue();

        boolean notExists = userRepository.existsByEmail("nonexistent@example.com");
        assertThat(notExists).isFalse();
    }

    @Test
    void createUser_WithDuplicateEmail_ShouldThrowException() {
        userService.create(userDto);

        UserDto duplicateUser = new UserDto();
        duplicateUser.setName("Another User");
        duplicateUser.setEmail("integration@example.com");

        org.junit.jupiter.api.Assertions.assertThrows(
                ru.practicum.shareit.exception.DuplicateEmailException.class,
                () -> userService.create(duplicateUser)
        );
    }

    @Test
    void updateUser_WithDuplicateEmail_ShouldThrowException() {

        userService.create(userDto);

        UserDto secondUser = new UserDto();
        secondUser.setName("Second User");
        secondUser.setEmail("second@example.com");
        UserDto createdSecond = userService.create(secondUser);

        UserDto updateDto = new UserDto();
        updateDto.setEmail("integration@example.com");

        org.junit.jupiter.api.Assertions.assertThrows(
                ru.practicum.shareit.exception.DuplicateEmailException.class,
                () -> userService.update(createdSecond.getId(), updateDto)
        );
    }

    @Test
    void deleteUser_WithInvalidId_ShouldThrowException() {
        org.junit.jupiter.api.Assertions.assertThrows(
                ru.practicum.shareit.exception.NotFoundException.class,
                () -> userService.deleteById(999L)
        );
    }

    @Test
    void updateUser_WithInvalidId_ShouldThrowException() {
        UserDto updateDto = new UserDto();
        updateDto.setName("Updated Name");

        org.junit.jupiter.api.Assertions.assertThrows(
                ru.practicum.shareit.exception.NotFoundException.class,
                () -> userService.update(999L, updateDto)
        );
    }
}
