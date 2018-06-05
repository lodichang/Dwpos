package com.dw.service;

import com.dw.dto.TableViewDto;
import com.dw.enums.FontSizeEnum;
import com.dw.extended.DwLabel;
import com.dw.util.AppUtils;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * Created by li.yongliang on 2018/4/17.
 */
public class AddItemTableViewCell extends TableCell<TableViewDto,String> {
    final GridPane gridPane = new GridPane();
    // records the y com.dw.pos of the last button press so that the add person dialog can be shown next to the cell.
    //final DoubleProperty buttonY = new SimpleDoubleProperty();

    /**
     * AddPersonCell constructor
     * @param table the table to which a new person can be added.
     */
    public AddItemTableViewCell(final TableView table,int colNum) {
        if(colNum==4) {
            DwLabel detailBtn = new DwLabel(FontSizeEnum.font14);
            detailBtn.setGraphic(new ImageView(AppUtils.loadImage("rightArrow.png")));
            detailBtn.setContentDisplay(ContentDisplay.TOP);
            detailBtn.setAlignment(Pos.CENTER);
            detailBtn.setPrefSize(this.getMinWidth(), this.getPrefHeight());
            detailBtn.setAccessibleText("rightArrow");
            detailBtn.setOnMouseClicked(event -> {
                if (detailBtn.getAccessibleText().equals("rightArrow")) {
                    detailBtn.setGraphic(new ImageView(AppUtils.loadImage("leftArrow.png")));
                    detailBtn.setAccessibleText("leftArrow");
                } else {
                    detailBtn.setGraphic(new ImageView(AppUtils.loadImage("rightArrow.png")));
                    detailBtn.setAccessibleText("rightArrow");
                }

                table.getSelectionModel().select(getTableRow().getIndex());
                //Person person = new Person();
                //table.getSelectionModel().select(getIndex());
                //person = table.getSelectionModel().getSelectedItem();
            });
            gridPane.getChildren().add(detailBtn);
        }


    }

    /** places an add button in the row only if the row is not empty. */
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            setText(item);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(gridPane);
        } else {
            setText(null);
            setGraphic(null);
        }
    }
}
