package es.unizar.smartcampuz.model.worker;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WorkerRepositoryTest {

    @Autowired
    private WorkerRepository repository;
    private Worker worker;

    @Before
    public void setUp() {
        worker = new Worker();
        worker.setEmail("mail@mail.com");
        worker.setUserData(new UserData("TestName"));
    }

    @Test
    public void save() {
        worker = repository.save(worker);
        assertThat(repository.findByEmail(worker.getEmail()), is(worker));
    }

    @Test
    public void delete() {
        worker = repository.save(worker);
        repository.delete(worker);
        assertNull(repository.findOne(worker.getId()));
    }

    @Test
    public void update() {
        worker = repository.save(worker);
        assertThat(repository.findOne(worker.getId()), is(worker));
        worker.setUserData(new UserData("NewTestName"));
        worker = repository.save(worker);
        assertThat(repository.findOne(worker.getId()), is(worker));
    }

    @Test
    public void findOne() {
        worker = repository.save(worker);
        assertThat(repository.findOne(worker.getId()), is(worker));
    }

    @Test
    public void findByEmail() {
        worker = repository.save(worker);
        assertThat(repository.findByEmail(worker.getEmail()), is(worker));
    }

}
