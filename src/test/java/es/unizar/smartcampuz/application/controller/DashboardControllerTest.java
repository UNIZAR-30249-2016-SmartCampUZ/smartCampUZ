package es.unizar.smartcampuz.application.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DashboardControllerTest {

    private static final String FEEDBACK_STORED_MESSAGE = "\"Feedback guardado correctamente.\"";
    private static final String DESCRIPTION_IS_BLANK_MESSAGE = "\"Debe introducir una descripción.\"";
    private static final String LOCATION_IS_BLANK_MESSAGE = "\"Debe introducir una localización.\"";

    @Autowired
    private MockMvc mvc;


    /*
    * Checks if the process of reporting a new feedback works correctly.
    */
    @Test
    public void reportFeedback() throws Exception{
        String body = "{\"description\":\"Una descripcion\", \"location\":\"Una localizacion\"}";
        ResultActions result = sendRequest(body);
        result.andExpect(status().isOk());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(FEEDBACK_STORED_MESSAGE));
    }

    /*
    * Checks if there's an error message if the client tries to send one or both of the
    * two fields blank.
    */
    @Test
    public void reportFeedbackWithBlankFields() throws Exception{
        String body = "{\"description\":\"\", \"location\":\"Una localizacion\"}";
        ResultActions result = sendRequest(body);
        result.andExpect(status().isBadRequest());
        MockHttpServletResponse mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(DESCRIPTION_IS_BLANK_MESSAGE));

        body = "{\"description\":\"Una descripcion\", \"location\":\"\"}";
        result = sendRequest(body);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(LOCATION_IS_BLANK_MESSAGE));

        body = "{\"description\":\"\", \"location\":\"\"}";
        result = sendRequest(body);
        result.andExpect(status().isBadRequest());
        mockResponse = result.andReturn().getResponse();
        assertTrue(mockResponse.getContentAsString().equals(DESCRIPTION_IS_BLANK_MESSAGE));
    }

    /*
     * Sends the request to the reportFeedback endpoint with the given body
     */
    private ResultActions sendRequest(String body) throws Exception{
        return this.mvc.perform(post("/reportFeedback")
            .content(body));
    }

}
