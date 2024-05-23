package org.klozevitz.lifegame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.klozevitz.lifegame.controllers.MainViewController;
import org.klozevitz.lifegame.util.Game;

import java.io.IOException;
import java.util.Objects;


public class LifeGame extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stageInit(stage);
        stage.show();
    }

    private void stageInit(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LifeGame.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("LIFE-GAME");
        stage.setScene(scene);
        scene
                .getStylesheets()
                .add(Objects
                        .requireNonNull(this
                        .getClass()
                        .getResource("/styles/main-view.css"))
                        .toExternalForm());
        stage.setResizable(false);

        MainViewController controller = fxmlLoader.getController();
        Game game = Game.fromConfig("setup.config");
        controller.setGame(game);
    }

    public static void main(String[] args) {
        launch();
    }
}