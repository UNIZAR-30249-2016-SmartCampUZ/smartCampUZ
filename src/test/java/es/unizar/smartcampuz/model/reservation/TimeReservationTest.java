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
        TimeReservation time = new TimeReservation(array);
        assertArrayEquals(array, time.getTimeSlots());
    }

    @Test
    public void isCompatibleWith() throws Exception {
        TimeReservation time = new TimeReservation(array);
        boolean[] array2 = new boolean[24];
        array2[3]=true; //It's no a collision with time[3]
        array2[4]=true; //It's no a collision with time[4]
        TimeReservation time2 = new TimeReservation(array2);

        boolean[] array3 = new boolean[24];
        array3[2]=true; //We force a collision with time[2]
        TimeReservation time3 = new TimeReservation(array3);

        assertTrue(time.isCompatibleWith(time2));
        assertFalse(time.isCompatibleWith(time3));
    }
}
