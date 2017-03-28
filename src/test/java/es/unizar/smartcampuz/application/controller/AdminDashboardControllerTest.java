package es.unizar.smartcampuz.application.controller;

import es.unizar.smartcampuz.infrastructure.auth.Credential;
import es.unizar.smartcampuz.infrastructure.auth.CredentialRepository;
import es.unizar.smartcampuz.model.report.ReportState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Base64;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AdminDashboardControllerTest {

    private Credential manager;
    private String token = "";

    private static final String FEEDBACK_LOCATION_LIST=
        "{\"feedbacks\":[{\"state\":\"INBOX\",\"worker\":\"Worker7\",\"description\":\"Report1\",\"title\":\"Report1\",\"location\":\"HD-403\",\"id\":10}," +
        "{\"state\":\"INBOX\",\"worker\":\"Worker7\",\"description\":\"Report2\",\"title\":\"Report2\",\"location\":\"HD-403\",\"id\":11}]}";

    private static final String FEEDBACK_TEST_SQL_LIST =
        "{\"feedbacks\":[{\"state\":\"INBOX\",\"worker\":\"Worker7\",\"description\":\"Report1\",\"title\":\"Report1\",\"location\":\"HD-403\",\"id\":10}," +
        "{\"state\":\"INBOX\",\"worker\":\"Worker7\",\"description\":\"Report2\",\"title\":\"Report2\",\"location\":\"HD-403\",\"id\":11}," +
        "{\"state\":\"INBOX\",\"worker\":\"\",\"description\":\"Report3\",\"title\":\"Report3\",\"location\":\"HD-404\",\"id\":12}," +
        "{\"state\":\"INBOX\",\"worker\":\"\",\"description\":\"Report4\",\"title\":\"Report4\",\"location\":\"HD-404\",\"id\":13}," +
        "{\"state\":\"APPROVED\",\"worker\":\"\",\"description\":\"Report5\",\"title\":\"Report5\",\"location\":\"HD-404\",\"id\":14}," +
        "{\"state\":\"INBOX\",\"worker\":\"Worker7\",\"description\":\"Report6\",\"title\":\"Report6\",\"location\":\"HD-404\",\"id\":15},]}";

    private static final String FEEDBACK_COMPLETE_LIST =
        "{\"feedbacks\":[{\"state\":\"INBOX\",\"worker\":\"Worker7\",\"description\":\"Report1\",\"title\":\"Report1\",\"location\":\"HD-403\",\"id\":10}," +
        "{\"state\":\"INBOX\",\"worker\":\"Worker7\",\"description\":\"Report2\",\"title\":\"Report2\",\"location\":\"HD-403\",\"id\":11}," +
        "{\"state\":\"INBOX\",\"worker\":\"\",\"description\":\"Report3\",\"title\":\"Report3\",\"location\":\"HD-404\",\"id\":12}," +
        "{\"state\":\"INBOX\",\"worker\":\"\",\"description\":\"Report4\",\"title\":\"Report4\",\"location\":\"HD-404\",\"id\":13}," +
        "{\"state\":\"APPROVED\",\"worker\":\"\",\"description\":\"Report5\",\"title\":\"Report5\",\"location\":\"HD-404\",\"id\":14}," +
        "{\"state\":\"INBOX\",\"worker\":\"Worker7\",\"description\":\"Report6\",\"title\":\"Report6\",\"location\":\"HD-404\",\"id\":15}," +
        "{\"state\":\"INBOX\",\"worker\":\"\",\"description\":\"Una descripcion\",\"title\":\"Una descripcion\",\"location\":\"Una localizacion\",\"id\":16}]}";

    private static final String WORKERS_LIST = "{\"workers\":[{\"id\":7,\"email\":\"maintenance@unizar.es\",\"name\":\"Worker7\"}]}";

    private static final String STATE_CHANGED_SUCCESS_MSG = "\"Estado modificado correctamente\"";
    private static final String STATE_CHANGED_ERROR_MSG = "\"El cambio de estado solicitado no es posible.\"";
    private static final String REPORT_NOT_FOUND_MSG = "\"La sugerencia no existe.\"";
    private static final String WRONG_STATE_MSG = "\"Debe introducir un estado válido\"";
    private static final String ASSIGN_WORKER_SUCCESS_MSG = "\"Trabajador asignado con éxito.\"";
    private static final String WORKER_OR_REPORT_NOT_FOUND = "\"La sugerencia y/o el trabajador no existen.\"";
    private static final String LOCATION  = "HD-403";
    private static final long REPORT_ID_1 = 10;
    private static final long REPORT_ID_2 = 11;
    private static final long REPORT_ID_3 = 12;
    private static final long REPORT_ID_4 = 13;
    private static final long REPORT_ID_5 = 14;
    private static final long REPORT_ID_6 = 15;
    private static final long WORKER_ID = 7;



    @Autowired
    private MockMvc mvc;

    @Autowired
    CredentialRepository credentialRepository;

    private static final Logger LOG = LoggerFactory
        .getLogger(CredentialController.class);

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
        String header1 = "Bearer "+token;
        String header2 = "";
        ResultActions result = sendFeedbackListRequest(header1, header2);
        result.andExpect(status().isOk());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(FEEDBACK_COMPLETE_LIST)
           || mockResponse.getContentAsString().equals(FEEDBACK_TEST_SQL_LIST));
    }

    /*
    * Checks if the process of listing all the existing feedbacks
    * in a concrete location works correctly.
    */
    @Test
    public void listLocationFeedback() throws Exception{
        String header1 = "Bearer "+token;
        ResultActions result = sendFeedbackListRequest(header1, LOCATION);
        result.andExpect(status().isOk());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(FEEDBACK_LOCATION_LIST));
    }

    /**
     * Checks if the process of chaging the current state of a report
     * to the next one works correctly. This report already have a worker assigned.
     */
    @Test
    public void changeReportState() throws Exception{
        String header = "Bearer "+token;
        ReportState[] states = ReportState.values();
        for(int i=3;i<states.length;i++){
            String body = "{\"state\":\""+ states[i]+"\", \"id\":"+REPORT_ID_6+"}";
            ResultActions result = sendChangeStateRequest(header, body);
            result.andExpect(status().isOk());
            MockHttpServletResponse mockResponse = result.andReturn().getResponse();
            assertTrue(mockResponse.getContentAsString().equals(STATE_CHANGED_SUCCESS_MSG));
        }
    }

    /**
     * Checks if there's an error message when trying to change a report's state
     * to one that is not reacheable based on the reports state policy.
     */
    @Test
    public void changeReportToRefused() throws Exception{
        String header = "Bearer "+token;
        String body = "{\"state\":\""+ ReportState.REFUSED+"\", \"id\":"+REPORT_ID_3+"}";
        ResultActions result = sendChangeStateRequest(header, body);
        result.andExpect(status().isOk());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(STATE_CHANGED_SUCCESS_MSG));
    }

    /**
     * Checks if there's an error message when trying to change a report's state
     * to one that is not reacheable based on the reports state policy.
     */
    @Test
    public void changeReportToNotified() throws Exception{
        String header = "Bearer "+token;
        String body = "{\"state\":\""+ ReportState.NOTIFIED+"\", \"id\":"+REPORT_ID_4+"}";
        ResultActions result = sendChangeStateRequest(header, body);
        result.andExpect(status().isOk());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(STATE_CHANGED_SUCCESS_MSG));
    }

    /**
     * Checks if there's an error message when trying to change a report's state
     * to one that is not reacheable based on the reports state policy.
     */
    @Test
    public void changeReportToWrongState() throws Exception{
        String header = "Bearer "+token;
        String body = "{\"state\":\""+ ReportState.DONE+"\", \"id\":"+REPORT_ID_2+"}";
        ResultActions result = sendChangeStateRequest(header, body);
        result.andExpect(status().isBadRequest());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(STATE_CHANGED_ERROR_MSG));
    }

    /**
     * Checks if there's an error message when trying to change a
     * non existing report's state.
     */
    @Test
    public void changeNonExistingReportState() throws Exception{
        String header = "Bearer "+token;
        String body = "{\"state\":\""+ ReportState.APPROVED+"\", \"id\":"+1500000+"}";
        ResultActions result = sendChangeStateRequest(header, body);
        result.andExpect(status().isNotFound());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(REPORT_NOT_FOUND_MSG));
    }

    /**
     * Checks if there's an error message when trying to change a
     * blank report's state.
     */
    @Test
    public void changeReportToBlankState() throws Exception{
        String header = "Bearer "+token;
        String body = "{\"state\": \"\", \"id\":"+REPORT_ID_1+"}";
        ResultActions result = sendChangeStateRequest(header, body);
        result.andExpect(status().isBadRequest());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_STATE_MSG));
    }

    /**
     * Checks if the process to assign a worker to a report works correctly.
     * The report's innitial state is "Approved" and, in the second
     * request, "Assigned".
     */
    @Test
    public void assignWorkerToReport() throws Exception{
        String header = "Bearer "+token;
        String body = "{\"worker\":"+WORKER_ID+", \"id\":"+REPORT_ID_5+"}";
        ResultActions result = sendAssignWorkerRequest(header, body);
        result.andExpect(status().isOk());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(ASSIGN_WORKER_SUCCESS_MSG));

        body = "{\"worker\":"+WORKER_ID+", \"id\":"+REPORT_ID_5+"}";
        result = sendAssignWorkerRequest(header, body);
        result.andExpect(status().isOk());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(ASSIGN_WORKER_SUCCESS_MSG));
    }

    /**
     * Checks if there's an error message when trying to assign a non
     * existing worker to a report.
     */
    @Test
    public void assignNonExistingWorkerToReport() throws Exception{
        String header = "Bearer "+token;
        String body = "{\"worker\":"+15000+", \"id\":"+REPORT_ID_5+"}";
        ResultActions result = sendAssignWorkerRequest(header, body);
        result.andExpect(status().isNotFound());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WORKER_OR_REPORT_NOT_FOUND));
    }

    /**
     * Checks if there's an error message when trying to assign a
     * worker to a non existing report.
     */
    @Test
    public void assignWorkerToNonExistingReport() throws Exception{
        String header = "Bearer "+token;
        String body = "{\"worker\":"+WORKER_ID+", \"id\":"+15000+"}";
        ResultActions result = sendAssignWorkerRequest(header, body);
        result.andExpect(status().isNotFound());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WORKER_OR_REPORT_NOT_FOUND));
    }

    /**
     * Checks if there's an error message when trying to assign a
     * worker to a report with a state different that "Approved" or "Assigned"
     */
    @Test
    public void assignWorkerToReportWithWrongState() throws Exception{
        String header = "Bearer "+token;
        String body = "{\"worker\":"+WORKER_ID+", \"id\":"+REPORT_ID_4+"}";
        ResultActions result = sendAssignWorkerRequest(header, body);
        result.andExpect(status().isBadRequest());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(STATE_CHANGED_ERROR_MSG));
    }

    /**
     * Checks if the process to list all workers in the systemas
     * works correctly.
     */
    @Test
    public void listAllWorkers() throws Exception{
        String header = "Bearer "+token;
        ResultActions result = sendListAllWorkersRequest(header);
        result.andExpect(status().isOk());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WORKERS_LIST));
    }

    /*
     * Sends the request to the listFeedback endpoint with the given body and authorization header
     */
    private ResultActions sendFeedbackListRequest(String header1, String header2) throws Exception{
        return this.mvc.perform(get("/listFeedback")
            .header("Authorization", header1)
            .header("location", header2));
    }

    private ResultActions sendChangeStateRequest(String header, String body) throws Exception{
        return this.mvc.perform(put("/state")
            .header("Authorization", header)
            .content(body));
    }

    private ResultActions sendAssignWorkerRequest(String header, String body) throws Exception{
        return this.mvc.perform(put("/assignWorker")
            .header("Authorization", header)
            .content(body));
    }

    private ResultActions sendListAllWorkersRequest(String header) throws Exception{
        return this.mvc.perform(get("/listWorkers")
            .header("Authorization", header));
    }
}

