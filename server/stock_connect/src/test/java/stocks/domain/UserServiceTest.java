package stocks.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import stocks.data.UserRepository;
import stocks.models.AppUser;
import stocks.security.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceTest {

    @Autowired
    UserService service;

    @MockBean
    UserRepository repository;

    @Test
    void findAll() {
        List<AppUser> expected = make2Users();
        when(repository.findAll()).thenReturn(expected);
        List<AppUser> actual = service.findAll();
        assertEquals(actual.size(), expected.size());
    }

    @Test
    void findByID() {
        AppUser expected = makeUser();
        when(repository.findById(1)).thenReturn(expected);
        AppUser actual = service.findByID(1);
        assertEquals(actual, expected);
    }

    @Test
    void findByUsername() {
        AppUser expected = makeUser();
        when(repository.findByUsername("test")).thenReturn(expected);
        AppUser actual = service.findByUsername("test");
        assertEquals(actual, expected);
    }

    @Test
    void findByEmail() {
        AppUser expected = makeUser();
        when(repository.findByEmail("test@example.com")).thenReturn(expected);
        AppUser actual = service.findByEmail("test@example.com");
        assertEquals(actual, expected);
    }

    @Test
    void add() {
        AppUser expected = makeUser();
        AppUser expectedwithIdZero = new AppUser();
        expectedwithIdZero.setUserId(0);
        expectedwithIdZero.setUsername(expected.getUsername());
        expectedwithIdZero.setPassword(expected.getPassword());
        expectedwithIdZero.setEmail(expected.getEmail());

        when(repository.add(expectedwithIdZero)).thenReturn(expected);
        Result<AppUser> result = service.add(expectedwithIdZero);
        assertTrue(result.isSuccess());
    }

    @Test
    void update() {
        AppUser user = makeUser();
        user.setUserId(1);

        when(repository.update(user)).thenReturn(true);

        Result<AppUser> result = service.update(user);

        assertEquals(ResultType.SUCCESS, result.getType());
        verify(repository, times(1)).update(user);
    }

    @Test
    void deleteById() {
        int userId = 1;

        when(repository.deleteById(userId)).thenReturn(true);

        boolean result = service.deleteById(userId);

        assertTrue(result);
        verify(repository, times(1)).deleteById(userId);
    }


    AppUser makeUser() {
        AppUser user = new AppUser();
        user.setUserId(1);
        user.setUsername("test");
        user.setPassword("password");
        user.setEmail("test@example.com");
        return user;
    }

    List<AppUser> make2Users() {
        List<AppUser> users = new ArrayList<>();
        AppUser user = new AppUser();
        user.setUserId(1);
        user.setUsername("test");
        user.setPassword("password");
        user.setEmail("test@test.com");

        AppUser user2 = new AppUser();
        user.setUserId(2);
        user.setUsername("test2");
        user.setPassword("password");
        user.setEmail("test2@test.com");

        users.add(user);
        users.add(user2);
        return users;
    }
}
