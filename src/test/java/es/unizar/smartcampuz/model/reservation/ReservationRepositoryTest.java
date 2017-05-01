package es.unizar.smartcampuz.model.reservation;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;

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
        reservation.setDate(new Date(0));
        boolean[] array = new boolean[24];
        array[8]=true;
        reservation.setTimeReservation(new TimeReservation(array));
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
        boolean[] array = new boolean[24];
        array[9]=true;
        reservation.setTimeReservation(new TimeReservation(array));
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

    @Test
    public void findByState() {
        reservation = repository.save(reservation);
        Set<Reservation> set = repository.findAllByState(ReservationState.PENDING);

        assertThat(set.iterator().next(), is(reservation));
    }

    @Test
    public void findByRoomIDAndState() {
        reservation = repository.save(reservation);
        Set<Reservation> set = repository.findAllByRoomIDAndState(reservation.getRoomID(), ReservationState.PENDING);

        assertThat(set.iterator().next(), is(reservation));
    }

    @Test
    public void findByRoomIDAndDateAndState() {
        reservation = repository.save(reservation);
        Set<Reservation> set = repository.findAllByRoomIDAndDateAndState(reservation.getRoomID(),
            reservation.getDate(), ReservationState.PENDING);

        assertThat(set.iterator().next(), is(reservation));
    }

}
