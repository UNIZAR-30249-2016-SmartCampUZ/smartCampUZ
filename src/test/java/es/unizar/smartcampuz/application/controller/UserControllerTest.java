package es.unizar.smartcampuz.application.controller;

import es.unizar.smartcampuz.model.user.*;
import org.junit.Before;
import org.junit.BeforeClass;
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

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String USERNAME = "test";
    private static final String EMAIL = "test@testemail.com";
    private static final String PASS = "testPass";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private static UserRepository userRepository;

    @BeforeClass
    public static void initializer(){
        userRepository.save(new User(EMAIL, USERNAME, PASS));
    }

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();

    }

    @Test
    @Ignore
    public void login() throws Exception {
        String header = "Basic " + Base64.getEncoder().encodeToString((USERNAME + ":" + PASS).getBytes());
        this.mvc.perform(post("/signIn")
                .header("Authorization", header))
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
