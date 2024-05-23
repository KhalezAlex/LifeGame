package org.klozevitz.lifegame.util;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Setup {
    private static Canvas canvas;
    public static void setup(VBox box) {
        setCanvas(10);
        box.getChildren().add(canvas);
        grid(10);
    }

    private static void setCanvas(int dim) {
        canvas = new Canvas(800, 800);
        canvas.getStyleClass().add("canvas");
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.ORANGE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.setOnMouseClicked((e) -> {
            canvasMouseClickListener(e, dim);
        });
    }

    private static void grid(int dim) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.DARKSLATEGRAY);
        int size = (int) (canvas.getWidth() / dim);
        for (int i = 0; i < dim; i++) {
            gc.strokeLine(0, i * size, canvas.getWidth(), i * size);
            gc.strokeLine(i * size, 0, i * size, canvas.getHeight());
        }
    }

    private static void canvasMouseClickListener(MouseEvent e, int dim) {
        int x = (int) (dim * e.getX() / canvas.getWidth());
        int y = (int) (dim * e.getY() / canvas.getHeight());

        System.out.println(x);
        System.out.println(y);
    }
}
