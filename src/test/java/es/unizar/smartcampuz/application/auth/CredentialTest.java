package es.unizar.smartcampuz.application.auth;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CredentialTest {

    @Test
    public void checkPassword() throws Exception {
        Credential credential = new Credential("777@unizar.es", "pass", "role");
        assertFalse(credential.checkPassword("wrong"));
        assertTrue(credential.checkPassword("pass"));
    }

}
