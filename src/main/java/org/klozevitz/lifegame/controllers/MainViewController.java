package org.klozevitz.lifegame.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.Data;
import org.klozevitz.lifegame.util.Game;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

@Data
public class MainViewController implements Initializable {
    private final int CANVAS_SIZE = 800;
    @FXML
    public TextField dimensionTextField;
    @FXML
    public Canvas canvasField;
    @FXML
    public Button startButton;
    @FXML
    public Button stopButton;
    private Game game;
    private GraphicsContext gc;
    private int cellSize;
    private int dimension;
    private boolean progress;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = canvasField.getGraphicsContext2D();
        dimension = Integer.parseInt(dimensionTextField.getText());
        game = new Game(dimension);
        cellSize = CANVAS_SIZE / dimension;
        setCanvas();
        dimensionTextField.textProperty().addListener(textFieldValueChangeHandler);
    }

    public void setGame(Game game) {
        this.game = game;
        dimension = game.getDimension();
        cellSize = CANVAS_SIZE / dimension;
        dimensionTextField.textProperty().removeListener(textFieldValueChangeHandler); // если не убрать лиснер, будет срабатывать на смене значения текстового поля
        dimensionTextField.setText(String.valueOf(dimension));
        dimensionTextField.textProperty().addListener(textFieldValueChangeHandler);
        drawGeneration();
    }

    public void onStartButtonClickHandler() {
        String configFile = System.getProperty("setupConfig");
        try {
            Files.writeString(Path.of(configFile), "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        game.serialize(configFile);
        start();
    }

    private void start() {
        progress = true;
        final Timeline timeLine = new Timeline(
                new KeyFrame(Duration.millis(150), actionEvent -> round())
        );
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    private void round() {
        game.generation();
        drawGeneration();
    }

    private void setCanvas() {
        cellSize = CANVAS_SIZE / dimension;
        int canvasSize = Math.min(cellSize * dimension, CANVAS_SIZE);
        canvasField.setHeight(canvasSize);
        canvasField.setWidth(canvasSize);
        gc = canvasField.getGraphicsContext2D();
        drawGeneration();
        canvasField.setOnMouseClicked((e) -> {
            canvasMouseClickListener(e, dimension);
        });
    }

    public void drawGeneration() {
        gc.setFill(Color.ORANGE);
        gc.fillRect(0, 0, canvasField.getWidth(), canvasField.getHeight());
        for (int i = 0; i < game.getField().length; i++) {
            for (int j = 0; j < game.getField()[0].length; j++) {
                if (game.getField()[i][j]) {
                    gc.setFill(Color.BLACK);
                } else {
                    gc.setFill(Color.ORANGE);
                }
                gc.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
        grid();
    }

    private void grid() {
        gc.setStroke(Color.DARKSLATEGRAY);
        for (int i = 0; i < game.getDimension(); i++) {
            gc.strokeLine(0, i * cellSize, canvasField.getWidth(), i * cellSize);
            gc.strokeLine(i * cellSize, 0, i * cellSize, canvasField.getHeight());
        }
    }

    private void canvasMouseClickListener(MouseEvent e, int dim) {
        int x = (int) (dim * e.getX() / canvasField.getWidth());
        int y = (int) (dim * e.getY() / canvasField.getHeight());
        game.changeAlive(x, y);
        drawGeneration();
    }

    private final ChangeListener<String> textFieldValueChangeHandler = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
            try {
                dimension = Integer.parseInt(newValue);
                if (dimension < 10 || dimension > 100) {
                    dimension = dimension < 10 ? 10 : 100;
                }
                changeView(String.valueOf(dimension));
            } catch (Exception e) {
                System.out.println("Не число- сетап возвращается к дефолтному значению");
                changeView("10");
            }
        }

        private void changeView(String dim) {
            dimensionTextField.textProperty().removeListener(textFieldValueChangeHandler);
            game = new Game(dimension);
            setup(dim);
            dimensionTextField.textProperty().addListener(textFieldValueChangeHandler);
        }

        private void setup(String dimension) {
            dimensionTextField.setText(dimension);
            setCanvas();
        }
    };

    public void onStopButtonClickHandler(ActionEvent actionEvent) {
        System.exit(130);
    }
}
