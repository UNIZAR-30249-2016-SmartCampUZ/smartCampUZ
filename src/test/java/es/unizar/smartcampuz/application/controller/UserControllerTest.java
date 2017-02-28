package es.unizar.smartcampuz.application.controller;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();

    }

    @Test
    @Ignore
    public void login() throws Exception {
        this.mvc.perform(get("/login")
                .requestAttr("email","test")
                .requestAttr("password","pass"))
            .andExpect(status().isOk());
    }

    @Test
    @Ignore
    public void create() throws Exception {
        this.mvc.perform(post("/user")
            .requestAttr("email","test")
            .requestAttr("name","test")
            .requestAttr("password","pass"))
            .andExpect(status().isOk());
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

}
