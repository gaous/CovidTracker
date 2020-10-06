package org.apiapplication;

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

public class PrimaryController {

//    @FXML
//    private void switchToSecondary() throws IOException {
//        App.setRoot("secondary");
//    }
    private String countriesText = "";
    @FXML
    private Label globalDataLabel;
    @FXML
    private TextField countriesTextField;
    @FXML
    private Label countriesLabel;
    Model model = new Model();

    ArrayList<String> countriesTextArray = new ArrayList<>();
    ArrayList<String> slugTextArray = new ArrayList<>();
    ArrayList<Model> modelArray = new ArrayList<>();

    @FXML
    private void handleEnterButton(KeyEvent e){
        if(e.getCode().equals(KeyCode.ENTER)) {
            if (countriesTextField.getText().trim().toLowerCase().equals("")) {
                //TODO Create a function for the alert thing
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please write a country name in the text box to include");
                alert.show();
            } else if (countriesText.contains(countriesTextField.getText().trim().toLowerCase())) {
                //TODO Create a function for the alert thing
                Alert alert = new Alert(Alert.AlertType.WARNING, "The country name is already included");
                alert.show();
            } else {
                String tempCountryName = countriesTextField.getText().trim().toLowerCase();
                if (countriesText.length() == 0 && tempCountryName.length() == 0) {
                    countriesText = countriesText + tempCountryName.toLowerCase();
                    //TODO Create a function for the alert thing
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Please do not include any special characters including numbers");
                    alert.show();
                } else if (countriesText.contains(tempCountryName.toLowerCase())) {
                    //TODO Create a function for the alert thing
                    Alert alert = new Alert(Alert.AlertType.WARNING, "The country name is already included");
                    alert.show();
                } else {
                    try {
                        model = APIController.getDataFromAPI("Countries", false, tempCountryName);
                        if(model.getCountryName().length() == 0)
                            throw new Exception();
                        else {
                            countriesTextArray.add(model.getCountryName());
                            slugTextArray.add(model.getSlug_name());
                            modelArray.add(model);
                        }
                    }
                    catch ( NullPointerException n1){
                        //TODO Create a function for the alert thing
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Please input a valid country name");
                        alert.show();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                if(countriesTextArray.size() < 2){
                    countriesLabel.setText(model.getCountryName());
                }
                else{
                    countriesLabel.setText(countriesLabel.getText() + ", " + model.getCountryName());
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
            countriesText = countriesLabel.getText().trim().toLowerCase();
            String tempWord = countriesTextField.getText().trim().toLowerCase();
            if (!countriesText.contains(countriesTextField.getText().trim().toLowerCase())){
                //TODO Create a function for the alert thing
                Alert alert = new Alert(Alert.AlertType.WARNING, "You have not input the country name yet");
                alert.show();
            }
            else{
                if (countriesText.endsWith(tempWord)){
                    countriesText = countriesText.replaceAll(", " + tempWord, "");
                }
                countriesText = countriesText.replaceAll(tempWord + ", ", "");
                countriesText = countriesText.replaceAll(tempWord, "");
                countriesLabel.setText(countriesText);
                countriesTextField.setText("");
            }
        }
        countriesTextField.requestFocus();
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
            List<Model> b = (modelArray);
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

}