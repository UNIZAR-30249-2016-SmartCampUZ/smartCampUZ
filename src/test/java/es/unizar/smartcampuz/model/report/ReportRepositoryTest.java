package es.unizar.smartcampuz.model.report;

import es.unizar.smartcampuz.model.worker.Worker;
import es.unizar.smartcampuz.model.worker.WorkerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ReportRepositoryTest {

    @Autowired
    private ReportRepository rRepository;
    @Autowired
    private WorkerRepository wRepository;

    private Report report;
    private Worker worker;

    @Before
    public void setUp() {
        report = rRepository.findOne(10L);
        worker = wRepository.findOne(7L);
    }

    @Test
    public void save() {
        assertThat(rRepository.findOne(report.getId()), is(report));
    }

    @Test
    public void delete() {
        rRepository.delete(report);
        assertNull(rRepository.findOne(report.getId()));
    }

    @Test
    public void update() {
        assertThat(rRepository.findOne(report.getId()), is(report));
        report.setDescription("newDesc");
        report.setState(ReportState.APPROVED);
        report = rRepository.save(report);
        assertThat(rRepository.findOne(report.getId()), is(report));
    }

    @Test
    public void findOne() {
        assertThat(rRepository.findOne(report.getId()), is(report));
    }

    @Test
    public void findByWorker() {
        Set<Report> set = rRepository.findByWorker(worker);
        assertEquals(2,set.size());
        Iterator<Report> i = set.iterator();
        assertEquals(10, i.next().getId());
        assertEquals(11, i.next().getId()); //Fully test.sql dependant
    }

    @Test
    public void findByRoom() {
        Set<Report> set = rRepository.findByRoomID("HD-403");
        assertEquals(2,set.size());
        Iterator<Report> i = set.iterator();
        assertEquals(10, i.next().getId());
        assertEquals(11, i.next().getId()); //Fully test.sql dependant
    }

}
