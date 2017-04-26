package es.unizar.smartcampuz.model.reservation;

import org.junit.Test;

import javax.validation.constraints.AssertTrue;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class TimeReservationTest {

    private boolean[] array = new boolean[]{true,false,true,false,
        false,true,false,false,
        true,false,false,true,
        true,false,true,true,
        false,true,true,false,
        true,true,false,true};

    private int integer = 0b101101101101100100100101;
    private int tooLargerInteger = 0b1000000000000000000000000; // 1<<23
    private static final SimpleDateFormat dateFormater = new SimpleDateFormat("dd-mm-yyyy");

    @Test
    public void getTimeSlots() throws Exception {
        TimeReservation time = new TimeReservation(array, dateFormater.parse("15-11-1995") );
        assertArrayEquals(array, time.getTimeSlots());
    }
}
