package es.unizar.smartcampuz.infrastructure.service;

import net.sf.json.JSONObject;

import java.io.BufferedReader;

public class JsonService {

    public static JSONObject readJson (BufferedReader bf) throws Exception {
        StringBuffer jb = new StringBuffer();
        String line = null;

        while ((line = bf.readLine()) != null) {
            jb.append(line);
        }

        return JSONObject.fromObject(jb.toString());
    }
}
