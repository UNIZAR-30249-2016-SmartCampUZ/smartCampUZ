package es.unizar.smartcampuz.application.controller;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import es.unizar.smartcampuz.infrastructure.service.*;

@Controller
public class DashboardController {

    private static final Logger LOG = LoggerFactory
        .getLogger(CredentialController.class);

    @PostMapping("/reportFeedback")
    @ResponseBody
    public ResponseEntity<String> newFeedback(HttpServletRequest request) throws IOException{
        String description = "";
        String location = "";

        JSONObject json = null;

        try{
            json = JsonService.readJson(request.getReader());
            description = json.getString("description");
            location = json.getString("location");
        }
        catch (Exception e){
            return new ResponseEntity<>("\"Error interno en el servidor.\"", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOG.info("Location: "+location+" Description: "+description);
        if((description==null) || (description.trim().equals(""))){
            return new ResponseEntity<>("\"Debe introducir una descripción.\"", HttpStatus.BAD_REQUEST);
        }
        if((location==null) || (location.trim().equals(""))){
            return new ResponseEntity<>("\"Debe introducir una localización.\"", HttpStatus.BAD_REQUEST);
        }
        //TODO: ¿Comprobar que la localización existe?

        //TODO: Crear el nuevo report e insertarlo en la BD
        return new ResponseEntity<>("\"Feedback guardado correctamente.\"", HttpStatus.OK);
    }
}
