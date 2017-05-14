package es.unizar.smartcampuz.application.controller;


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

import java.util.Arrays;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DashboardControllerTest {

    private static final String AVAILABLE_HOURS_TEST_SQL = "{\"availableHours\":[false,false,false,false,false,false,false," +
        "false,true,true,false,true,true,true,true,true,true,true,true,true,true,false,false,false]}";

    // This string counts the reservation made in test suit, since the execution order of the tests is not known
    private static final String AVAILABLE_HOURS = "{\"availableHours\":[false,false,false,false,false,false,false," +
        "false,true,true,false,true,true,true,true,true,true,true,true,true,true,false,false,false]}";

    private static final String REPORT_STORED_MESSAGE = "\"Report guardado correctamente.\"";
    private static final String DESCRIPTION_IS_BLANK_MESSAGE = "\"Debe introducir una descripción.\"";
    private static final String LOCATION_IS_BLANK_MESSAGE = "\"Debe introducir una localización.\"";
    private static final String RESERVATION_SUCCESSFUL_MESSAGE = "\"Reserva solicitada correctamente.\"";
    private static final String WRONG_RESERVATION_LIST_LENGTH = "\"La lista de horas no es válida\"";
    private static final String WRONG_RESERVATION_FIELDS = "\"Debes introducir localización, email y descripción\"";
    private static final String CONFLICTING_RESERVATION_MESSAGE = "\"Reserva en conflicto con otra. No puede aprobarse.\"";
    private static final String WRONG_AVAILABLE_HOURS_FIELDS = "\"Debes introducir localización, día y mes\"";

    private static final boolean[] REQUESTED_HOURS = {false, false, false, false, false, false, false, false, true, true,
        false, false, false, false, false, false, false, false, false, true, false, false, false, false};
    private static final boolean[] SHORT_REQUESTED_HOURS = {false, false, false, false, false, false, false, false, true, true,
        false, false, false, false, false, false, false, false, false, true, false, false, false};
    private static final boolean[] LONG_REQUESTED_HOURS = {false, false, false, false, false, false, false, false, true, true,
        false, false, false, false, false, false, false, false, false, true, false, false, false, false, false};
    private static final boolean[] CONFLICT_REQUESTED_HOURS = {false, false, false, false, false, false, false, false, false, false,
        true, false, false, false, false, false, false, false, false, false, false, false, false, false};

    @Autowired
    private MockMvc mvc;

    private static final Logger LOG = LoggerFactory
        .getLogger(CredentialController.class);


    /*
    * Checks if the process of reporting a new report works correctly.
    */
    @Test
    public void report() throws Exception{
        String body = "{\"description\":\"Una descripcion\", \"location\":\"Una localizacion\"}";
        ResultActions result = sendReportRequest(body);
        result.andExpect(status().isOk());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(REPORT_STORED_MESSAGE));
    }

    /*
    * Checks if there's an error message if the client tries to send one or both of the
    * two fields blank.
    */
    @Test
    public void reportWithBlankFields() throws Exception{
        String body = "{\"description\":\"\", \"location\":\"Una localizacion\"}";
        ResultActions result = sendReportRequest(body);
        result.andExpect(status().isBadRequest());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(DESCRIPTION_IS_BLANK_MESSAGE));

        body = "{\"description\":\"Una descripcion\", \"location\":\"\"}";
        result = sendReportRequest(body);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(LOCATION_IS_BLANK_MESSAGE));

        body = "{\"description\":\"\", \"location\":\"\"}";
        result = sendReportRequest(body);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(DESCRIPTION_IS_BLANK_MESSAGE));
    }


    @Test
    public void makeReservation() throws Exception{
        String body = "{\"description\":\"Una descripcion\", \"location\":\"HD-403\", \"email\":\"unemail@correo.com\", " +
            "\"day\":"+12+", \"month\":" + 12 + ", \"requestedHours\":"+Arrays.toString(REQUESTED_HOURS)+"}";
        ResultActions result = sendReservationRequest(body);
        result.andExpect(status().isOk());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(RESERVATION_SUCCESSFUL_MESSAGE));
    }

    @Test
    public void makeReservationWithWrongRequestedHours() throws Exception{
        String body = "{\"description\":\"Una descripcion\", \"location\":\"HD-403\", \"email\":\"unemail@correo.com\", " +
            "\"day\":"+12+", \"month\":" + 12 + ", \"requestedHours\":"+Arrays.toString(SHORT_REQUESTED_HOURS)+"}";
        ResultActions result = sendReservationRequest(body);
        result.andExpect(status().isBadRequest());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_RESERVATION_LIST_LENGTH));

        body = "{\"description\":\"Una descripcion\", \"location\":\"HD-403\", \"email\":\"unemail@correo.com\", " +
            "\"day\":"+12+", \"month\":" + 12 + ", \"requestedHours\":"+Arrays.toString(LONG_REQUESTED_HOURS)+"}";
        result = sendReservationRequest(body);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_RESERVATION_LIST_LENGTH));
    }

    @Test
    public void makeReservationWithBlankFields() throws Exception{
        String body = "{\"description\":\"Una descripcion\", \"location\":\"\", \"email\":\"unemail@correo.com\", " +
            "\"day\":"+12+", \"month\":" + 12 + ", \"requestedHours\":"+Arrays.toString(REQUESTED_HOURS)+"}";
        ResultActions result = sendReservationRequest(body);
        result.andExpect(status().isBadRequest());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_RESERVATION_FIELDS));

        body = "{\"description\":\"Una descripcion\", \"location\":\"HD-403\", \"email\":\"\", " +
            "\"day\":"+12+", \"month\":" + 12 + ", \"requestedHours\":"+Arrays.toString(REQUESTED_HOURS)+"}";
        result = sendReservationRequest(body);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_RESERVATION_FIELDS));

        body = "{\"description\":\"\", \"location\":\"HD-403\", \"email\":\"unemail@correo.com\", " +
            "\"day\":"+12+", \"month\":" + 12 + ", \"requestedHours\":"+Arrays.toString(REQUESTED_HOURS)+"}";
        result = sendReservationRequest(body);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_RESERVATION_FIELDS));

        body = "{\"description\":\"\", \"location\":\"\", \"email\":\"unemail@correo.com\", " +
            "\"day\":"+12+", \"month\":" + 12 + ", \"requestedHours\":"+Arrays.toString(REQUESTED_HOURS)+"}";
        result = sendReservationRequest(body);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_RESERVATION_FIELDS));

        body = "{\"description\":\"\", \"location\":\"HD-403\", \"email\":\"\", " +
            "\"day\":"+12+", \"month\":" + 12 + ", \"requestedHours\":"+Arrays.toString(REQUESTED_HOURS)+"}";
        result = sendReservationRequest(body);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_RESERVATION_FIELDS));

        body = "{\"description\":\"Una descripcion\", \"location\":\"\", \"email\":\"\", " +
            "\"day\":"+12+", \"month\":" + 12 + ", \"requestedHours\":"+Arrays.toString(REQUESTED_HOURS)+"}";
        result = sendReservationRequest(body);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_RESERVATION_FIELDS));

        body = "{\"description\":\"\", \"location\":\"\", \"email\":\"\", " +
            "\"day\":"+12+", \"month\":" + 12 + ", \"requestedHours\":"+Arrays.toString(REQUESTED_HOURS)+"}";
        result = sendReservationRequest(body);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_RESERVATION_FIELDS));
    }

    @Test
    public void makeConflictReservation() throws Exception{
        String body = "{\"description\":\"Una descripcion\", \"location\":\"HD-407\", \"email\":\"unemail@correo.com\", " +
            "\"day\":"+15+", \"month\":" + 12 + ", \"requestedHours\":"+Arrays.toString(CONFLICT_REQUESTED_HOURS)+"}";
        ResultActions result = sendReservationRequest(body);
        result.andExpect(status().isBadRequest());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(CONFLICTING_RESERVATION_MESSAGE));
    }

    @Test
    public void listAvailableHours() throws Exception{
        String location = "HD-407";
        int day = 15;
        int month = 12;
        ResultActions result = sendAvailableHoursRequest(location, day, month);
        result.andExpect(status().isOk());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(AVAILABLE_HOURS_TEST_SQL) ||
            mockResponse.getContentAsString().equals(AVAILABLE_HOURS));
    }

    @Test
    public void listAvailableHoursWithWrongFields() throws Exception{
        String location = "HD-407";
        int day = 15;
        int month = 12;
        ResultActions result = sendAvailableHoursRequest("", day, month);
        result.andExpect(status().isBadRequest());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_AVAILABLE_HOURS_FIELDS));

        result = sendAvailableHoursRequest(location, -1, month);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_AVAILABLE_HOURS_FIELDS));

        result = sendAvailableHoursRequest(location, day, -1);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_AVAILABLE_HOURS_FIELDS));

        result = sendAvailableHoursRequest("", day, -1);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_AVAILABLE_HOURS_FIELDS));

        result = sendAvailableHoursRequest("", -1, month);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_AVAILABLE_HOURS_FIELDS));

        result = sendAvailableHoursRequest(location, -1, -1);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_AVAILABLE_HOURS_FIELDS));

        result = sendAvailableHoursRequest("", -1, -1);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(WRONG_AVAILABLE_HOURS_FIELDS));
    }

    /*
     * Sends the request to the report endpoint with the given body
     */
    private ResultActions sendReportRequest(String body) throws Exception{
        return this.mvc.perform(post("/report")
            .content(body));
    }

    /*
     * Sends the request to the reservation endpoint with the given params
     */
    private ResultActions sendReservationRequest(String body) throws Exception{

        return this.mvc.perform(post("/reservation")
            .content(body));
    }

    /*
     * Sends the request to the report endpoint with the given body
     */
    private ResultActions sendAvailableHoursRequest(String header1, int header2, int header3) throws Exception{
        return this.mvc.perform(get("/availableHours")
            .header("location", header1)
            .header("day", header2)
            .header("month", header3));
    }

}
