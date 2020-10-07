package org.covidTracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecondaryController {

//    @FXML
//    private void switchToPrimary() throws IOException {
//        App.setRoot("primary");
//    }

    @FXML
    private ListView<String> listView;
    @FXML
    private TextArea detailsTextArea;

    ObservableList<String> countryNameList = FXCollections.observableArrayList();
    ObservableList<String> list = FXCollections.observableArrayList();

    public Model m;
    public List<Model> arraysOfModel = new ArrayList<>();


    @FXML
    public void initialize(List<String> countryNameList, List<Model> model){
        list.addAll(countryNameList);
        System.out.println(model);
        for (Model a:
                model) {
            m = a;
            System.out.println("Country name from model is " + m.getCountryName());
            arraysOfModel.add(m);
            this.countryNameList.add(m.getCountryName());
        }
        arraysOfModel.addAll(model);
        listView.getItems().setAll(this.countryNameList);
        listView.getSelectionModel().getSelectionMode();
    }

    @FXML
    public void handleClickListView(){
        List<String> listOfSelectedItem = Collections.singletonList(listView.getSelectionModel().getSelectedItem());
        String temp = listOfSelectedItem.toString();
        temp = temp.replaceAll("[^A-Za-z ]", "");
        for (Model model : arraysOfModel) {
            if (model.getCountryName().equals(temp)) {
                detailsTextArea.setText(model.getDetails(true));
            }
        }
    }

    public void handleBackButton(ActionEvent event) throws IOException {
//        App.setRoot("primary");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("primary.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }
}