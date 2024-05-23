package org.klozevitz.lifegame.util;

import lombok.Data;

import java.io.*;

@Data
public class Game implements Serializable {
    private final String SOURCE = "setup.config";
    int dimension;                      // размерность поля
    private boolean[][] field;          // поле
    private boolean[][] beforeRound;    // буферный массив для перехода к следующему поколению

    public Game(int dimension, boolean isInfiniteField) {
        this.field = new boolean[isInfiniteField ? dimension + 2 : dimension][isInfiniteField ? dimension + 2 : dimension];
        this.beforeRound = new boolean[field.length][field[0].length];
        this.dimension = dimension;
    }

    public Game(int dimension) {
        this.field = new boolean[dimension][dimension];
        this.beforeRound = new boolean[field.length][field[0].length];
        this.dimension = dimension;
    }

    public boolean[][] getField() {
        return field;
    }

    public void changeAlive(int x, int y) {
        field[y][x] = !field[y][x];
    }

    public void generation() {
        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field[x].length; y++) {
                fate(x, y);
            }
        }
        completeGeneration();
    }

    private void completeGeneration() {
        for (int x = 0; x < field.length; x++) {
            System.arraycopy(beforeRound[x], 0, field[x], 0, field[0].length);
        }
    }

    private void fate(int x, int y) { // судьба клетки
        int neighboursAlive;
        neighboursAlive = neighboursAlive(x, y);
        beforeRound[x][y] = neighboursAlive == 3 || (neighboursAlive == 2 && field[x][y]);
    }

    private int neighboursAlive(int x, int y) {
        int alive = 0;
        alive += field[(x + dimension - 1) % dimension][(y + dimension - 1) % dimension] ? 1 : 0;
        alive += field[(x + dimension) % dimension][(y + dimension - 1) % dimension] ? 1 : 0;

        alive += field[(x + dimension + 1) % dimension][(y + dimension - 1) % dimension] ? 1 : 0;
        alive += field[(x + dimension - 1) % dimension][(y + dimension) % dimension] ? 1 : 0;

        alive += field[(x + dimension + 1) % dimension][(y + dimension) % dimension] ? 1 : 0;
        alive += field[(x + dimension - 1) % dimension][(y + dimension + 1) % dimension] ? 1 : 0;

        alive += field[(x + dimension) % dimension][(y + dimension + 1) % dimension] ? 1 : 0;
        alive += field[(x + dimension + 1) % dimension][(y + dimension + 1) % dimension] ? 1 : 0;
        return alive;
    }

    public void serialize(String source) {
        try (FileInputStream fis = new FileInputStream(source)) {
            if (fis.readAllBytes().length == 0) {
                try {
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(source));
                    out.writeObject(this);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (boolean[] row : field) {
            for (boolean isAlive : row) {
                sb.append(isAlive ? '.' : '*').append(' ');
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static Game fromConfig(String source) {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(source))) {
            return (Game) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new Game(10, true);
        }
    }
}