package es.unizar.smartcampuz.application.auth;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CredentialTest {

    @Test
    public void checkPassword() throws Exception {
        Credential u = new Credential("777@unizar.es", "pass", "role");
        assertFalse(u.checkPassword("wrong"));
        assertTrue(u.checkPassword("pass"));
    }

}
