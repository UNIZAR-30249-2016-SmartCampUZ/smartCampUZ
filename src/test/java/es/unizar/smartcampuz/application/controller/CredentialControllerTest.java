package es.unizar.smartcampuz.application.controller;

import es.unizar.smartcampuz.application.auth.Credential;
import es.unizar.smartcampuz.application.auth.CredentialRepository;
import es.unizar.smartcampuz.application.service.JwtService;
import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Base64;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CredentialControllerTest {

    private static final String SIGN_IN_INCORRECT_FIELDS_MESSAGE = "\"Usuario o contraseña incorrectos\"";
    private static final String NOT_EXISTING_USER_MESSAGE = "\"El usuario no existe\"";
    private static final String WRONG_PASS_MESSAGE = "\"Contraseña incorrecta\"";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtService jwtService;

    private Credential manager;
    private Credential teacher;
    private Credential worker;

    @Autowired
    CredentialRepository credentialRepository;

    @Before
    public void setUp() throws Exception{
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        manager = credentialRepository.findByEmail("manager@unizar.es");
        teacher = credentialRepository.findByEmail("teacher@unizar.es");
        worker = credentialRepository.findByEmail("worker@unizar.es");

    }

    /*
    * Checks if the process of signing in a user works correctly.
    */
    @Test
    public void signIn() throws Exception {
        String header = "Basic " + Base64.getEncoder().encodeToString((manager.getEmail() + ":" + manager.getPassword()).getBytes());
        ResultActions result = sendLoginRequest(header);
        result.andExpect(status().isOk());
        // Retrieves the response from the request that was sent
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        JSONObject jResponse= JSONObject.fromObject(mockResponse.getContentAsString());
        String token = mockResponse.getHeader("Token");
        assertTrue(jResponse.getString("userName").equals(manager.getEmail())); //TODO delete this
        assertTrue(jResponse.getString("email").equals(manager.getEmail()));
        assertTrue(jResponse.getString("type").equals(manager.getRole()));
        // Checks if the token is not blank and retrieves it
        assertFalse(token.equals(""));
        // Verifies the token and checks if it's the correct one for the user
        String user = jwtService.verify(token.replaceAll("Bearer", "")).getEmail();
        assertTrue(user.equals(manager.getEmail()));
    }

    /*
    * Checks if there's an error message if the client sends a sign in request with the required
    * fields blank.
    */
    @Test
    public void signInWithBlankFields() throws Exception {
        String header = "Basic " + Base64.getEncoder().encodeToString((":").getBytes());
        ResultActions result = sendLoginRequest(header);
        result.andExpect(status().isBadRequest());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(SIGN_IN_INCORRECT_FIELDS_MESSAGE));
    }

    /*
     * Checks if there's an error message if the user is trying to sign in with
     * a non existing username.
     */
    @Test
    public void signInWithNotExistingUsername() throws Exception {
        String header = "Basic " + Base64.getEncoder().encodeToString(("doesNotExists:" + manager.getPassword()).getBytes());
        ResultActions result = sendLoginRequest(header);
        result.andExpect(status().isBadRequest());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(NOT_EXISTING_USER_MESSAGE));
    }

    /*
    * Checks if there's an error message if the password doesn't match with the username
    * during the sign in process
    */
    @Test
    public void signInWithWrongPassword() throws Exception {
        String header = "Basic " + Base64.getEncoder().encodeToString((manager.getEmail() + ":wrongPass").getBytes());
        ResultActions result = sendLoginRequest(header);
        result.andExpect(status().isBadRequest());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_PASS_MESSAGE));
    }

    @Test
    public void create() throws Exception {
        this.mvc.perform(post("/user")
            .requestAttr("email","test")
            .requestAttr("name","testCreate")
            .requestAttr("password","pass"))
            .andExpect(status().isCreated());
    }

    @Test
    @Ignore
    public void deleteUser() throws Exception {
        //TODO Implement
    }

    @Test
    @Ignore
    public void getByEmail() throws Exception {
        //TODO Implement
    }

    @Test
    @Ignore
    public void updateUser() throws Exception {
        //TODO Implement

    }

    /*
     * Sends the request to the signIn endpoint with the given header
     */
    private ResultActions sendLoginRequest(String header) throws Exception{
       return this.mvc.perform(post("/signIn")
            .header("Authorization", header));
    }

}
