package org.covidTracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * JavaFX App built with Maven
 * @author: Gaous Muhammad Saklaen
 */

public class PrimaryController {

//    @FXML
//    private void switchToSecondary() throws IOException {
//        App.setRoot("secondary");
//    }
    @FXML
    private Label globalDataLabel;
    @FXML
    private TextField countriesTextField;
    @FXML
    private Label countriesLabel;

    Model model = new Model();
    ArrayList<String> countriesTextArray = new ArrayList<>();
    ArrayList<Model> countryArray = new ArrayList<>();

    @FXML
    private void handleEnterButton(KeyEvent e){
        if(e.getCode().equals(KeyCode.ENTER)) {
            if (countriesTextField.getText().trim().toLowerCase().equals("")) {
                //TODO Create a function for the alert thing
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please write a country name in the text box to include");
                alert.show();
            } else
                {
                    String tempCountryName = countriesTextField.getText().trim();
                    try {
                        model = APIController.getDataFromAPI("Countries", false, tempCountryName);
                        if(model.getCountryName().length() == 0)
                            throw new Exception();
                        else {
                                if (!countriesTextArray.toString().toLowerCase().contains(tempCountryName.toLowerCase())) {
                                    countriesTextArray.add(model.getCountryName());
                                    countryArray.add(model);
                                }
                                else{
                                    //TODO Create a function for the alert thing
                                    Alert alert = new Alert(Alert.AlertType.WARNING, "The country name is already included");
                                    alert.show();
                                }
                            if(countriesTextArray.size() < 2){
                                countriesLabel.setText(model.getCountryName());
                            }
                            else{
                                String temp = countriesTextArray.toString();
                                temp = temp.replaceAll("[^A-Za-z, ]", "");
                                countriesLabel.setText(temp);
                            }
                        }
                    }
                    catch ( NullPointerException n1){
                        //TODO Create a function for the alert thing
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Please input a valid country name");
                        alert.show();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    countriesTextField.setText("");
                    countriesTextField.requestFocus();

                }
        }
    }

    @FXML
    public void handleDeleteCountriesFromLabels() {
        if(countriesTextField.getText().trim().toLowerCase().equals("")){
            //TODO Create a function for the alert thing
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please write a country name in the text box to remove");
            alert.show();
        }
        else{
            String tempWord = countriesTextField.getText().trim();
            if (!countriesTextArray.toString().toLowerCase().contains(tempWord.toLowerCase())){
                //TODO Create a function for the alert thing
                Alert alert = new Alert(Alert.AlertType.WARNING, "You have not input the country name yet");
                alert.show();
            }
            else{
                countryArray.removeIf(removeCountryModel -> removeCountryModel.getCountryName().toLowerCase().equals(tempWord.toLowerCase()));
                countriesTextArray.removeIf(removeCountryText -> removeCountryText.toLowerCase().equals(tempWord.toLowerCase()));
                String temp = countriesTextArray.toString();
                temp = temp.replaceAll("[^A-Za-z, ]", "");
                countriesLabel.setText(temp);
                countriesTextField.setText("");
            }
        }
        countriesTextField.requestFocus();
    }

    @FXML
    private void handleAboutButton() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("About.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage window = new Stage();
        window.setScene(scene);
        window.setTitle("COVID Tracker About page");
        window.show();
    }

    @FXML
    private void handleSearchButton(ActionEvent event) throws IOException{
//            App.setRoot("secondary");
        if(isSearchEligibleNow()){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("secondary.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            SecondaryController controller = loader.getController();
            List<String> a = (countriesTextArray);
            List<Model> b = (countryArray);
            controller.initialize(a, b);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }

    private boolean isSearchEligibleNow(){
        if(countriesTextArray.size()< 1){
            //TODO Create a function for the alert thing
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please input at least 1 country name to search for the data");
            alert.show();
            countriesTextField.requestFocus();
            return false;
        }
        else{
            return true;
        }
    }

    public void handleRefreshButton() throws IOException, ParseException {

        model = APIController.getDataFromAPI("Global", true, "");
        globalDataLabel.setText(model.getDetails(false));
    }

    @FXML
    public void initialize(Set<String> countryNameList, Set<Model> modelList){
        countriesTextArray.clear();
        countriesTextArray.addAll(countryNameList);
        countryArray.clear();
        countryArray.addAll(modelList);
        String temp = countriesTextArray.toString();
        temp = temp.replaceAll("[^A-Za-z, ]", "");
        countriesLabel.setText(temp);
    }
}