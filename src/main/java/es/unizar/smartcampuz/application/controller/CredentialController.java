package es.unizar.smartcampuz.application.controller;

import es.unizar.smartcampuz.infrastructure.auth.Credential;
import es.unizar.smartcampuz.infrastructure.auth.CredentialRepository;
import es.unizar.smartcampuz.infrastructure.service.JwtService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

/**
 * A class to test interactions with the MySQL database using the CredentialRepository class.
 *
 * @author netgloo
 */
@Controller
public class CredentialController {

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------

    @Autowired
    private CredentialRepository credentialRepository;

    private static final Logger LOG = LoggerFactory
        .getLogger(CredentialController.class);

    private final JwtService jwtService;

    // ------------------------
    // CONSTRUCTORS
    // ------------------------

    @SuppressWarnings("unused")
    public CredentialController(){
        this(null);
    }

    @Autowired
    public CredentialController(JwtService jwtService){
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
        String email = info.substring(0, index);
        String pass = info.substring(index+1);
        LOG.info("Credential: "+email+" Pass: "+pass);

        if(notBlank(email) && notBlank(pass)){
            Credential credential = credentialRepository.findByEmail(email);
            if(credential == null){
                LOG.info("El usuario no existe");
                return new ResponseEntity<>("\"El usuario no existe\"", HttpStatus.BAD_REQUEST);
            }
            else if( !(credential.checkPassword(pass) )){
                LOG.info("Contraseña incorrecta");
                return new ResponseEntity<>("\"Contraseña incorrecta\"", HttpStatus.BAD_REQUEST);
            }
            else{
                LOG.info("Usuario y contraseña correctos");
                HttpHeaders headers = new HttpHeaders();
                try{
                    //Creo un token para el usuario y lo añado al header "Token"
                    headers.add("Token", jwtService.tokenFor(credential));
                }
                catch (URISyntaxException e){
                    e.printStackTrace();
                }
                JSONObject jUser = new JSONObject();
                jUser.element("userName", "Paco");
                jUser.element("email", credential.getEmail());
                jUser.element("type", credential.getRole());
                return new ResponseEntity<>(jUser.toString(), headers, HttpStatus.OK);
            }
        }
        else{
            LOG.info("Usuario o contraseña incorrectos");
            return new ResponseEntity<>("\"Usuario o contraseña incorrectos\"", HttpStatus.BAD_REQUEST);
        }

    }

    /*
     * Checks if the username and password fields are null or empty.
     */
    private boolean notBlank(String field){
        return !(field==null || field.trim().equals(""));
    }

} // class UserController
