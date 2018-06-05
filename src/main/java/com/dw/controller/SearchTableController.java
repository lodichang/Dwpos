package com.dw.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lodi on 2018/1/3.
 */
@Getter
@Setter
public class SearchTableController {
    @FXML
    private TextField tableNumberTextField;

    public void addTextField(String s){
       tableNumberTextField.setText(tableNumberTextField.getText() + s);
    }

    @FXML
    public void clearAll(){
        tableNumberTextField.setText("");
    }

    @FXML
    public void deleteOne(){
        if(tableNumberTextField.getText().length()>0){
            tableNumberTextField.setText(tableNumberTextField.getText().substring(0,tableNumberTextField.getText().length()-1));
        }
    }


}
