package org.apiapplication;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class APIController {

    public static JSONObject initializeAPI() throws IOException, ParseException {
        URL url = new URL("https://api.covid19api.com/summary");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int response_code = conn.getResponseCode();
        if (response_code != 200) {
            throw new RuntimeException("HttpResponseCode: " + response_code);
        }
        else {
            StringBuilder inline = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();
            JSONParser parse = new JSONParser();
            return (JSONObject) parse.parse(inline.toString());
        }
    }

    public static Model getDataFromAPI(String countryCategory, boolean isGlobal, String countryName) throws IOException, ParseException {
        Model model = new Model();
        JSONObject data_obj = initializeAPI();
        if(countryCategory.equals("Global") && isGlobal && countryName.equals("")){
            JSONObject obj = (JSONObject) data_obj.get("Global");
            model.setTotalConfirmed(obj.get("TotalConfirmed").toString());
            model.setTotalDeaths(obj.get("TotalDeaths").toString());
            model.setTotalRecovered(obj.get("TotalRecovered").toString());
        }
        else if(countryCategory.equals("Countries") && !isGlobal && !countryName.equals("")){
            JSONArray arr = (JSONArray) data_obj.get("Countries");
            for (Object o : arr) {
                JSONObject new_obj = (JSONObject) o;
                if (new_obj.get("Country").toString().toLowerCase().equals(countryName)) {
                    model.setSlug_name(new_obj.get("Slug").toString());
                    model.setCountryName(new_obj.get("Country").toString());
                    model.setNewConfirmed(new_obj.get("NewConfirmed").toString());
                    model.setTotalConfirmed(new_obj.get("TotalConfirmed").toString());
                    model.setTotalDeaths(new_obj.get("TotalDeaths").toString());
                    model.setTotalRecovered(new_obj.get("TotalRecovered").toString());
                    model.setNewDeaths(new_obj.get("NewDeaths").toString());
                    model.setNewRecovered(new_obj.get("NewRecovered").toString());
                }
            }
        }
        return model;
    }
}