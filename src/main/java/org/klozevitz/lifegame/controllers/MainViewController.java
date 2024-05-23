package com.klozevitz.life.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MainViewController {
    @FXML
    public HBox mainBox;
    @FXML
    public HBox imageBox;
    @FXML
    public ImageView image;
    @FXML
    public VBox settingsBox;
    @FXML
    public VBox modeBox;
    @FXML
    public VBox sizeBox;
    @FXML
    public TextField rowCount;
    @FXML
    public TextField colCount;
    @FXML
    public Button startButton;

    public MainViewController() {
        mainBox.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
    }
}
