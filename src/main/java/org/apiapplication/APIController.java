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

    public static Model getDataFromAPI(String countryCategory, boolean isGlobal, String countryName) throws IOException, ParseException {
        Model model = new Model();
        URL url = new URL("https://api.covid19api.com/summary");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        //Getting the response code
        int response_code = conn.getResponseCode();
        if (response_code != 200) {
            throw new RuntimeException("HttpResponseCode: " + response_code);
        }
        else {
            StringBuilder inline = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());
            //Write all the JSON data into a string using a scanner
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            //Close the scanner
            scanner.close();
            //Using the JSON simple library parse the string into a json object
            JSONParser parse = new JSONParser();
            JSONObject data_obj = (JSONObject) parse.parse(inline.toString());
            //Get the required object from the above created object
            if(countryCategory.equals("Global") && isGlobal && countryName.equals("")){
                JSONObject obj = (JSONObject) data_obj.get("Global");
                //Get the required data using its key
//                System.out.println(obj.get("TotalRecovered"));
                model.setTotalConfirmed(obj.get("TotalConfirmed").toString());
                model.setTotalDeaths(obj.get("TotalDeaths").toString());
                model.setTotalRecovered(obj.get("TotalRecovered").toString());
            }
            else if(countryCategory.equals("Countries") && !isGlobal && !countryName.equals("")){
                JSONArray arr = (JSONArray) data_obj.get("Countries");
                for (Object o : arr) {
                    JSONObject new_obj = (JSONObject) o;
//                    model.setTotalConfirmed(new_obj.get("TotalConfirmed").toString());
                    if (new_obj.get("Slug").equals(countryName)) {
                        model.setCountryName(countryName);
                        model.setNewConfirmed(new_obj.get("NewConfirmed").toString());
                        model.setTotalConfirmed(new_obj.get("TotalConfirmed").toString());
                        model.setTotalDeaths(new_obj.get("TotalDeaths").toString());
                        model.setTotalRecovered(new_obj.get("TotalRecovered").toString());
                        model.setNewDeaths(new_obj.get("NewDeaths").toString());
                        model.setNewRecovered(new_obj.get("NewRecovered").toString());
//                        System.out.println("Total Recovered: " + new_obj.get("TotalRecovered"));
                    }
                }
            }
        }
        return model;
    }
}