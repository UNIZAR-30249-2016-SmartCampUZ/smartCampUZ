package es.unizar.smartcampuz.application.controller;

import es.unizar.smartcampuz.infrastructure.auth.Credential;
import es.unizar.smartcampuz.infrastructure.auth.CredentialRepository;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Base64;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WorkerDashboardControllerTest {

    private String token = "";

    private static final String WORKER_REPORT_LIST =
        "{\"reports\":[{\"state\":\"INBOX\",\"worker\":\"Worker7\",\"description\":\"Report1\",\"title\":\"Report1\",\"location\":\"HD-403\",\"id\":10}," +
            "{\"state\":\"INBOX\",\"worker\":\"Worker7\",\"description\":\"Report2\",\"title\":\"Report2\",\"location\":\"HD-403\",\"id\":11}," +
            "{\"state\":\"INBOX\",\"worker\":\"Worker7\",\"description\":\"Report50\",\"title\":\"Report50\",\"location\":\"HD-405\",\"id\":16}]}";

    private static final String WORKER_REPORT_LOCATION_LIST =
        "{\"reports\":[{\"state\":\"INBOX\",\"worker\":\"Worker7\",\"description\":\"Report1\",\"title\":\"Report1\",\"location\":\"HD-403\",\"id\":10}," +
            "{\"state\":\"INBOX\",\"worker\":\"Worker7\",\"description\":\"Report2\",\"title\":\"Report2\",\"location\":\"HD-403\",\"id\":11}]}";

    private static final String LOCATION  = "HD-403";
    private static final String INVALID_LOCATION_ERROR_MESSAGE = "\"Debe introducir una localización válida.\"";

    @Autowired
    private MockMvc mvc;

    @Autowired
    CredentialRepository credentialRepository;

    private static final Logger LOG = LoggerFactory
        .getLogger(CredentialController.class);

    /**
     *Logs in with the maintenance user and gets the token returned by the server
     */
    @Before
    public void setUp() throws Exception{
        Credential worker = credentialRepository.findByEmail("maintenance@unizar.es");
        String header = "Basic " + Base64.getEncoder().encodeToString((worker.getEmail() + ":" + worker.getPassword()).getBytes());
        token = this.mvc.perform(post("/signIn")
            .header("Authorization", header)).andReturn().getResponse().getHeader("Token");
        System.out.println(token);
    }

    @Test
    public void listAllWorkerReports() throws Exception{
        String header1 = "Bearer "+token;
        String header2 = "";
        ResultActions result = sendWorkerReportsListRequest(header1, header2);
        result.andExpect(status().isOk());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WORKER_REPORT_LIST));
    }

    @Test
    public void listLocationWorkerReports() throws Exception{
        String header1 = "Bearer "+token;
        ResultActions result = sendWorkerReportsListRequest(header1, LOCATION);
        result.andExpect(status().isOk());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WORKER_REPORT_LOCATION_LIST));
    }

    /*
     * Sends the request to the listReports endpoint with the given body and authorization header
     */
    private ResultActions sendWorkerReportsListRequest(String header1, String header2) throws Exception{
        return this.mvc.perform(get("/listWorkerReports")
            .header("Authorization", header1)
            .header("location", header2));
    }
}
