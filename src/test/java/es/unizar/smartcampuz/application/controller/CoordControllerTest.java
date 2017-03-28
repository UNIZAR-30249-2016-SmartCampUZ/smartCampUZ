package es.unizar.smartcampuz.application.controller;


import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CoordControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Environment environment;

    /**
     * This method will check if configured datasource platform is PostgreSQl. If not, it will skip the tests.
     *
     */
    @Before
    public void before() throws Exception {
        Assume.assumeTrue("This test is only runnable over local machine with PostGIS installed and maps loaded.",
            "postgresql".equals(environment.getProperty("spring.datasource.platform")));
    }

    @Test
    public void locationFromCoordsAda() throws Exception{
        String response = getRoom(-98889.06172,5113700.86492, new String[]{"00","00","00"});
        assertTrue(response.contains("\"id_UTC\":\"A00.00.080\""));
        assertTrue(response.contains("\"type\":6"));
        assertTrue(response.contains("\"text\":\"_SOLID\""));
    }

    @Test
    public void locationFromCoordsTorres() throws Exception{
        String response = getRoom(-98781.2735711412,5113660.033812136, new String[]{"00","00","00"});
        assertTrue(response.contains("\"id_UTC\":\"T00.00.250\""));
        assertTrue(response.contains("\"type\":11"));
        assertTrue(response.contains("\"text\":\"SOLID\""));
    }

    @Test //@Ignore("Only runnable over local machine with PostGIS installed and maps loaded.")
    public void locationFromCoordsBetan() throws Exception{
        String response = getRoom(-98512.10177036352,5113706.164753033, new String[]{"00","00","00"});
        assertTrue(response.contains("\"id_UTC\":\"B00.00.400\""));
        assertTrue(response.contains("\"type\":38"));
        assertTrue(response.contains("\"text\":\"_SOLID\""));
    }

    /**
     * Checks /locationFromCoords endpoint which gives the selected room's information
     */
    private String getRoom(double x, double y, String[] buildingFloors) throws Exception{
        MvcResult result = mvc.perform(get("/locationFromCoords")
            .requestAttr("x",x)
            .requestAttr("y",y)
            .requestAttr("buildingFloors", buildingFloors ))
            .andExpect(status().is2xxSuccessful())
            .andReturn();
        return result.getResponse().getContentAsString();
    }

}
