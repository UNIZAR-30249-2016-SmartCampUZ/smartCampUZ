package es.unizar.smartcampuz.application.auth;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CredentialRepositoryTest {

    @Autowired
    private CredentialRepository repository;
    private Credential credential;

    @Before
    public void setUp() {
        credential = new Credential();
        credential.setEmail("mail@mail.com");
        credential.setPassword("pass");
        credential.setRole("name");
    }

    @Test
    public void save() {
        credential = repository.save(credential);
        assertThat(repository.findByEmail(credential.getEmail()), is(credential));
    }

    @Test
    public void delete() {
        credential = repository.save(credential);
        repository.delete(credential);
        assertNull(repository.findOne(credential.getId()));
    }

    @Test
    public void update() {
        credential = repository.save(credential);
        assertThat(repository.findOne(credential.getId()), is(credential));
        credential.setPassword("password");
        credential = repository.save(credential);
        assertThat(repository.findOne(credential.getId()), is(credential));
    }

    @Test
    public void findOne() {
        credential = repository.save(credential);
        assertThat(repository.findOne(credential.getId()), is(credential));
    }

    @Test
    public void findByEmail() {
        credential = repository.save(credential);
        assertThat(repository.findByEmail(credential.getEmail()), is(credential));
    }

}
