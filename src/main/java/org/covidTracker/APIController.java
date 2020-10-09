package org.covidTracker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * JavaFX App built with Maven
 * @author: Gaous Muhammad Saklaen
 */

public class APIController {

    public static String initializeAPI() throws IOException{
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
            return inline.toString();
        }
    }

    public static Model getDataFromAPI(String countryCategory, boolean isGlobal, String countryName) throws IOException{
        Model model = new Model();
        String data_obj = initializeAPI();
        JSONObject obj = new JSONObject(data_obj);
        if(countryCategory.equals("Global") && isGlobal && countryName.equals("")){
            JSONObject tempObj = obj.getJSONObject("Global");
            model.setTotalConfirmed(tempObj.get("TotalConfirmed").toString());
            model.setTotalDeaths(tempObj.get("TotalDeaths").toString());
            model.setTotalRecovered(tempObj.get("TotalRecovered").toString());
        }
        else if(countryCategory.equals("Countries") && !isGlobal && !countryName.equals("")){
            JSONArray tempObj = obj.getJSONArray("Countries");
            for (Object o : tempObj) {
                JSONObject new_obj = (JSONObject) o;
                if (new_obj.get("Country").toString().toLowerCase().equals(countryName.toLowerCase())) {
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