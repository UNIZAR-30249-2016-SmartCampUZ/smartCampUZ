package es.unizar.smartcampuz.application.controller;

import es.unizar.smartcampuz.infrastructure.auth.Credential;
import es.unizar.smartcampuz.infrastructure.auth.CredentialRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Base64;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AdminDashboardControllerTest {

    private Credential manager;
    private String token = "";
    private static final String FEEDBACK_COMPLETE_LIST = "{\"feedbacks\":[]}";
    private static final String FEEDBACK_LOCATION_LIST = "{\"feedbacks\":[]}";

    @Autowired
    private MockMvc mvc;

    @Autowired
    CredentialRepository credentialRepository;

    /**
     *Logs in with the admin user and gets the token returned by the server
     */
    @Before
    public void setUp() throws Exception{
        manager = credentialRepository.findByEmail("admin@unizar.es");
        String header = "Basic " + Base64.getEncoder().encodeToString((manager.getEmail() + ":" + manager.getPassword()).getBytes());
        token = this.mvc.perform(post("/signIn")
            .header("Authorization", header)).andReturn().getResponse().getHeader("Token");
        System.out.println(token);
    }

    /*
    * Checks if the process of listing all the existing feedbacks works correctly.
    */
    @Test
    public void listAllFeedback() throws Exception{
        String body = "{\"location\":\"\"}";
        String header = "Bearer "+token;
        ResultActions result = sendRequest(body, header);
        result.andExpect(status().isOk());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(FEEDBACK_COMPLETE_LIST));
    }

    /*
    * Checks if the process of listing all the existing feedbacks
    * in a concrete location works correctly.
    */
    @Test
    public void listLocationFeedback() throws Exception{
        String body = "{\"location\":\"Some location\"}";
        String header = "Bearer "+token;
        ResultActions result = sendRequest(body, header);
        result.andExpect(status().isOk());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(FEEDBACK_LOCATION_LIST));
    }

    /*
     * Sends the request to the listFeedback endpoint with the given body and authorization header
     */
    private ResultActions sendRequest(String body, String header) throws Exception{
        return this.mvc.perform(post("/listFeedback")
            .content(body)
            .header("Authorization", header));
    }
}

