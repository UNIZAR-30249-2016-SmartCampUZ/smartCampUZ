package es.unizar.smartcampuz.model.report;

import es.unizar.smartcampuz.model.worker.Worker;
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
public class ReportRepositoryTest {

    @Autowired
    private ReportRepository repository;
    private Report report = new Report("204", new Worker(7), "desc", ReportState.APROBED);

    @Before
    public void setUp() {
        report = repository.save(report);
    }

    @Test
    public void save() {
        assertThat(repository.findOne(report.getId()), is(report));
    }

    @Test
    public void delete() {
        repository.delete(report);
        assertNull(repository.findOne(report.getId()));
    }

    @Test
    public void update() {
        assertThat(repository.findOne(report.getId()), is(report));
        report.setDescription("newDesc");
        report.setState(ReportState.APROBED);
        report = repository.save(report);
        assertThat(repository.findOne(report.getId()), is(report));
    }

    @Test
    public void findOne() {
        assertThat(repository.findOne(report.getId()), is(report));
    }

    @Test
    public void findByWorker() {

    }

}
