package es.unizar.smartcampuz.model.reservation;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository repository;
    private Reservation reservation;

    @Before
    public void setUp() {
        reservation = new Reservation();
        reservation.setRoomID("HD-101");
        reservation.setUserID("testUser");
        reservation.setState(ReservationState.PENDING);
        reservation.setTimeReservation(new TimeReservation(new boolean[24],new Date(0)));
    }

    @Test
    public void save() {
        reservation = repository.save(reservation);
        assertThat(repository.findByUserID(reservation.getUserID()), is(reservation));
    }

    @Test
    public void delete() {
        reservation = repository.save(reservation);
        repository.delete(reservation);
        assertNull(repository.findOne(reservation.getId()));
    }

    @Test
    public void update() {
        reservation = repository.save(reservation);
        assertThat(repository.findOne(reservation.getId()), is(reservation));
        reservation.setTimeReservation(new TimeReservation(new boolean[24], new Date(100)));
        reservation = repository.save(reservation);
        assertThat(repository.findOne(reservation.getId()), is(reservation));
    }

    @Test
    public void findOne() {
        reservation = repository.save(reservation);
        assertThat(repository.findOne(reservation.getId()), is(reservation));
    }

    @Test
    public void findByUserID() {
        reservation = repository.save(reservation);
        assertThat(repository.findByUserID(reservation.getUserID()), is(reservation));
    }

}
