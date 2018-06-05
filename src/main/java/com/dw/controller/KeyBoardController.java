package com.dw.controller;

import com.dw.Main;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by lodi on 2018/1/3.
 */
@Getter
@Setter
@FXMLController
public class KeyBoardController implements Initializable {
    @FXML
    private VBox keyBoardPane;
    @FXML
    private FlowPane searchPane;
    @FXML
    private HBox hbox;
    @FXML
    private FlowPane abcPane;
    @FXML
    private FlowPane abcrow1;
    @FXML
    private FlowPane abcrow2;
    @FXML
    private FlowPane abcrow3;
    @FXML
    private FlowPane bottomPane;
    @FXML
    private GridPane numberPane;
    @FXML
    private Button clearBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private TextField tableNumberTextField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainComponent();
    }

    public void mainComponent() {
        double keyboardWidht = Main.primaryScreenBounds.getWidth() * 0.85;
        double keyboardHeight = Main.primaryScreenBounds.getHeight() * 0.7;
        keyBoardPane.setPrefSize(keyboardWidht, keyboardHeight);
    }


    public void addTextField(String s) {
        tableNumberTextField.setText(tableNumberTextField.getText() + s);
    }

    @FXML
    public void clearAll() {
        tableNumberTextField.setText("");
    }

    @FXML
    public void deleteOne() {
        if (tableNumberTextField.getText().length() > 0) {
            tableNumberTextField.setText(tableNumberTextField.getText().substring(0, tableNumberTextField.getText().length() - 1));
        }
    }


}
