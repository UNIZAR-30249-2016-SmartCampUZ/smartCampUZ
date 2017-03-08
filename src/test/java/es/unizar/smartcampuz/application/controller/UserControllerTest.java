package es.unizar.smartcampuz.application.controller;

import es.unizar.smartcampuz.application.service.JwtService;
import es.unizar.smartcampuz.model.user.*;
import org.junit.*;
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
import net.sf.json.JSONObject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String USERNAME = "test";
    private static final String EMAIL = "test@testemail.com";
    private static final String PASS = "testPass";
    private static final String TYPE = "";
    private static final String SIGN_IN_INCORRECT_FIELDS_MESSAGE = "\"Usuario o contraseña incorrectos\"";
    private static final String NOT_EXISTING_USER_MESSAGE = "\"El usuario no existe\"";
    private static final String WRONG_PASS_MESSAGE = "\"Contraseña incorrecta\"";

    private int userId;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtService jwtService;

    @Before
    public void setUp() throws Exception{
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        String response = this.mvc.perform(post("/user")
            .requestAttr("email",EMAIL)
            .requestAttr("name",USERNAME)
            .requestAttr("password",PASS))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        JSONObject jResponse= JSONObject.fromObject(response);
        userId = jResponse.getInt("id");
    }

    /*
    * Checks if the process of signing in a user works correctly.
    */
    @Test
    public void signIn() throws Exception {
        String header = "Basic " + Base64.getEncoder().encodeToString((USERNAME + ":" + PASS).getBytes());
        ResultActions result = sendLoginRequest(header);
        result.andExpect(status().isOk());
        // Retrieves the response from the request that was sent
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        JSONObject jResponse= JSONObject.fromObject(mockResponse.getContentAsString());
        String token = mockResponse.getHeader("Token");
        assertTrue(jResponse.getString("userName").equals(USERNAME));
        assertTrue(jResponse.getString("email").equals(EMAIL));
        assertTrue(jResponse.getString("type").equals(TYPE));
        // Checks if the token is not blank and retrieves it
        assertFalse(token.equals(""));
        // Verifies the token and checks if it's the correct one for the user
        String user = jwtService.verify(token.replaceAll("Bearer", "")).getName();
        assertTrue(user.equals(USERNAME));
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
        String header = "Basic " + Base64.getEncoder().encodeToString(("doesNotExists:" + PASS).getBytes());
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
        String header = "Basic " + Base64.getEncoder().encodeToString((USERNAME + ":wrongPass").getBytes());
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

    @After
    public void tearDown() throws Exception{
        this.mvc.perform(delete("/user")
            .requestAttr("id",userId))
            .andExpect(status().isAccepted());
    }

}
