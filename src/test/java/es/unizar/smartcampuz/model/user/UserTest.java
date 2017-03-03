package es.unizar.smartcampuz.model.user;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void checkPassword() throws Exception {
        User u = new User("777@unizar.es", "Seven", "pass");
        assertFalse(u.checkPassword("wrong"));
        assertTrue(u.checkPassword("pass"));
    }

}
