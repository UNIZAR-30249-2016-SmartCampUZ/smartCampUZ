package es.unizar.smartcampuz.controller;

import es.unizar.smartcampuz.model.User;
import es.unizar.smartcampuz.model.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    // ------------------------
    // PUBLIC METHODS
    // ------------------------

    /**
     * GET/login  --> Returns true if authentication is correct.
     *
     * @param email User's email
     * @param password User's loged.
     */
    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<User> login(String email, String password) {
        try {
            User user = userRepository.findByEmail(email);
            if( user.getPassword().equals(password) ){
                return new ResponseEntity<User>(HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
            }
        }
        catch (Exception ex) {
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
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

} // class UserController
