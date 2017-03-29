package es.unizar.smartcampuz.application.controller;

import es.unizar.smartcampuz.application.database.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Controller
public class CoordController {

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------
    private static final Logger LOG = LoggerFactory
        .getLogger(CoordController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Room> rowMapper = new RowMapper<Room>() {
        @Override
        public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
            Room room = new Room();
            room.setId(rs.getString("id_utc"));
            room.setType(rs.getInt("tipo_de_us"));
            room.setName(rs.getString("id_centro"));
            return room;
        }
    };

    private enum TableCode{A,T,B};
    private final double[][] boundingBoxes={
        {
            5113778.96946, //Max Y
            5113597.12105, //Min Y
            -98872.8585277, //Max X
            -99010.8946963, //Min X
        }, //Ada
        {
            5113778.96946, //Max Y
            5113597.12105, //Min Y
            -98679.1626137, //Max X
            -98872.8585277, //Min X
        }, //Torres
        {
            5113778.96946, //Max Y
            5113597.12105, //Min Y
            -98227.2054811, //Max X
            -98602.352165, //Min X
        }, //Betan
    };

    // ------------------------
    // PRIVATE METHODS
    // ------------------------
    private String getTableCode(double x, double y, String[] buildingFloors){
        for (int i = 0; i < boundingBoxes.length; i++) { //For each bounding box
            if(boundingBoxes[i][2]>x && boundingBoxes[i][3]<x &&
                boundingBoxes[i][0]>y && boundingBoxes[i][1]<y
                ){

                return TableCode.values()[i].toString() + buildingFloors[i];
            }
        }
        return null; //TODO:Change this
    }

    // ------------------------
    // PUBLIC METHOD
    // ------------------------
    @GetMapping("/locationFromCoords")
    @ResponseBody
    public ResponseEntity<Room> getRoom(@RequestParam double x, @RequestParam double y,
                                        @RequestParam String[] buildingFloors ) throws IOException, SQLException{

        String tableName = getTableCode(x,y,buildingFloors); //TODO: Be carefull with floor typo

        if(tableName==null){
            return null;
        }

        String query = String.format("SELECT * FROM \"%s\" WHERE st_contains(geom ,st_geomfromtext(" +
            "'point(%f %f)', 3857))",tableName,x,y);
        ArrayList<Room> obj = (ArrayList<Room>) jdbcTemplate.query(query, rowMapper);


        if(obj.size()>0) {
            obj.get(0).setId(tableName + '.' + obj.get(0).getId());
            return new ResponseEntity<Room>(obj.get(0), HttpStatus.OK);
        } else {
            return new ResponseEntity<Room>(HttpStatus.NOT_FOUND);
        }
    }
}
