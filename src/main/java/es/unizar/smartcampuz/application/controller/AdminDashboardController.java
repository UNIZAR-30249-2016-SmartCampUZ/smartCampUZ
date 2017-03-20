package es.unizar.smartcampuz.application.controller;

import es.unizar.smartcampuz.application.service.JsonService;
import net.sf.json.JSONArray;
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

@Controller
public class AdminDashboardController {

    private static final Logger LOG = LoggerFactory
        .getLogger(CredentialController.class);

    @PostMapping("/listFeedback")
    @ResponseBody
    public ResponseEntity<String> listFeedbacks(HttpServletRequest request) throws IOException{
        String location = "";

        JSONObject json = null;

        try{
            json = JsonService.readJson(request.getReader());
            location = json.getString("location");
        }
        catch (Exception e){
            return new ResponseEntity<>("\"Error interno en el servidor.\"", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(location==null){
            return new ResponseEntity<>("\"Debe introducir una localización válida.\"", HttpStatus.BAD_REQUEST);
        }

        JSONArray feedback = new JSONArray();
        JSONObject response = new JSONObject();

        if(location.trim().equals("")){
            //TODO: Pedir a la BD todos los feedback existentes y meterlos en el JSONArray

        }
        else{
            //TODO: Pedir a la BD los feedback de la localización indicada y meterlos en el JSONArray
        }
        response.put("feedbacks", feedback);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }
}
