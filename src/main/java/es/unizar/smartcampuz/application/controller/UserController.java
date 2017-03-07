package es.unizar.smartcampuz.application.controller;

import es.unizar.smartcampuz.model.user.User;
import es.unizar.smartcampuz.model.user.UserRepository;
import es.unizar.smartcampuz.application.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URISyntaxException;
import net.sf.json.JSONObject;

/**
 * A class to test interactions with the MySQL database using the UserRepository class.
 *
 * @author netgloo
 */
@Controller
public class UserController {

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------

    @Autowired
    private UserRepository userRepository;

    private static final Logger LOG = LoggerFactory
        .getLogger(UserController.class);

    private final JwtService jwtService;

    // ------------------------
    // CONSTRUCTORS
    // ------------------------

    @SuppressWarnings("unused")
    public UserController(){
        this(null);
    }

    @Autowired
    public UserController(JwtService jwtService){
        this.jwtService = jwtService;
    }

    // ------------------------
    // PUBLIC METHODS
    // ------------------------

    /**
     * GET/login  --> Returns true if authentication is correct.
     *
     */
    @PostMapping("/signIn")
    @ResponseBody
    public ResponseEntity<String> signIn(HttpServletRequest request) throws IOException{
        String header = request.getHeader("Authorization");
        header = header.substring(6);
        byte [] decoded = Base64Utils.decode(header.getBytes());
        String info = "";
        try{
            info = new String(decoded, "UTF8");
        }
        catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        int index = info.indexOf(":");
        String username = info.substring(0, index);
        String pass = info.substring(index+1);
        LOG.info("User: "+username+" Pass: "+pass);

        if(notBlank(username) && notBlank(pass)){
            User user = userRepository.findByName(username);
            if(user == null){
                LOG.info("El usuario no existe");
                return new ResponseEntity<>("\"El usuario no existe\"", HttpStatus.BAD_REQUEST);
            }
            else if( !(user.checkPassword(pass) )){
                LOG.info("Contraseña incorrecta");
                return new ResponseEntity<>("\"Contraseña incorrecta\"", HttpStatus.BAD_REQUEST);
            }
            else{
                LOG.info("Usuario y contraseña correctos");
                HttpHeaders headers = new HttpHeaders();
                try{
                    //Creo un token para el usuario y lo añado al header "Token"
                    headers.add("Token", jwtService.tokenFor(user));
                }
                catch (URISyntaxException e){
                    e.printStackTrace();
                }
                JSONObject jUser = new JSONObject();
                jUser.element("userName", user.getName());
                jUser.element("email", user.getEmail());
                jUser.element("type", "");
                return new ResponseEntity<>(jUser.toString(), headers, HttpStatus.OK);
            }
        }
        else{
            LOG.info("Usuario o contraseña incorrectos");
            return new ResponseEntity<>("\"Usuario o contraseña incorrectos\"", HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * POST/user  --> Create a new user and save it in the database.
     *
     * @param email User's email
     * @param name User's name
     * @param password User's password
     * @return A string describing if the user is succesfully created or not.
     */
    @PostMapping("/user")
    @ResponseBody
    public ResponseEntity<User> create(String email, String name, String password) {
        User user = null;
        try {
            user = new User(email, name, password);
            userRepository.save(user);
            return new ResponseEntity<User>(user,HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DELETE/user  --> Delete the user having the passed id.
     *
     * @param id The id of the user to delete
     * @return A string describing if the user is succesfully deleted or not.
     */
    @DeleteMapping("/user")
    @ResponseBody
    public ResponseEntity<User> delete(long id) {
        try {
            User user = new User(id);
            userRepository.delete(user);
            return new ResponseEntity<User>(HttpStatus.ACCEPTED);
        }
        catch (Exception ex) {
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET/user  --> Return the user identified by id.
     *
     * @param id The id to search in the database.
     * @return The user id or a message error if the user is not found.
     */
    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<User> get(long id) {
        try {
            User user = userRepository.findOne(id);
            return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
        }
        catch (Exception ex) {
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * PUT/user  --> Update the email and the name for the user in the database
     * having the passed id.
     *
     * @param id The id for the user to update.
     * @param email The new email.
     * @param name The new name.
     * @param password The new password.
     * @return A string describing if the user is succesfully updated or not.
     */
    @PutMapping("/user")
    @ResponseBody
    public ResponseEntity<User> updateUser(long id, String email, String name, String password) {
        try {
            User user = userRepository.findOne(id);
            user.setEmail(email);
            user.setName(name);
            user.setPassword(password);
            userRepository.save(user);
            return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
        }
        catch (Exception ex) {
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Checks if the username and password fields are null or empty.
     */
    private boolean notBlank(String field){
        return field!=null || !field.trim().equals("");
    }

} // class UserController
