package es.unizar.smartcampuz.model.reservation;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class ReservationTest {
    @Test
    public void constructor() throws Exception {
        String room = "HD-101";
        String user = "testUser";
        ReservationState state = ReservationState.PENDING;
        boolean[] array = new boolean[24];
        Date date = new Date( System.currentTimeMillis());
        TimeReservation timeReservation = new TimeReservation(array, date);

        Reservation reservation = new Reservation(room, user, timeReservation);

        assertEquals(room, reservation.getRoomID());
        assertEquals(user, reservation.getUserID());
        assertEquals(state, reservation.getState());
        assertEquals(timeReservation, reservation.getTimeReservation());
    }

}
