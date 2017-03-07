package es.unizar.smartcampuz.model.user;


import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.hibernate.exception.GenericJDBCException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired UserRepository repository;
    User user;

    @Before
    public void setUp() {
        user = new User();
        user.setName("name");
        user.setEmail("mail@mail.com");
        user.setPassword("pass");
    }

    @Test
    public void save() {
        user = repository.save(user);
        assertThat(repository.findByEmail(user.getEmail()), is(user));
    }

    @Test
    public void delete() {
        user = repository.save(user);
        repository.delete(user);
        assertNull(repository.findOne(user.getId()));
    }

    @Test
    public void update() {
        user = repository.save(user);
        assertThat(repository.findOne(user.getId()), is(user));
        user.setPassword("password");
        user = repository.save(user);
        assertThat(repository.findOne(user.getId()), is(user));
    }

    @Test
    public void findOne() {
        user = repository.save(user);
        assertThat(repository.findOne(user.getId()), is(user));
    }

    @Test
    public void findByEmail() {
        user = repository.save(user);
        assertThat(repository.findByEmail(user.getEmail()), is(user));
    }

    @Test
    public void findByName() {
        user = repository.save(user);
        assertThat(repository.findByName(user.getName()), is(user));
    }

}
