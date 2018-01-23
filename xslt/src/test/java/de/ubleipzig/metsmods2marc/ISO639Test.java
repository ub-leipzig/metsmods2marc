package de.ubleipzig.metsmods2marc;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class ISO639Test {

    @Test
    public void getCodesFromJson() throws IOException, ParseException {
        final URL res = JSONReader.class.getResource("/iso639-2.json");
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(res.getPath()));
        JSONObject jsonObject =  (JSONObject) obj;
        JSONArray codes = (JSONArray) jsonObject.get("codes");
        codes.stream()
        .filter(code -> ((JSONObject) code).containsValue("lo"))
        .forEach(System.out::println);
    }
}
