package org.example.sample.client;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;

public class Window {
    private final GridPane textGrid = new GridPane();
    //Score Text
    private Label scoreLable = new Label("Score: 0");
    //Game Over Text
    private final Label gameOver = new Label("");

    // Instructions
    private final Label close = new Label("Press the Escape Key to Close");
    private Label pause = new Label("Press Any Key to Start");

    // Controls
    private final Map<String, Label> controlCommands = new HashMap<>();

    {
        controlCommands.put("W", new Label("W"));
        controlCommands.put("S", new Label("S"));
        controlCommands.put("A", new Label("A"));
        controlCommands.put("D", new Label("D"));
    }

    {
        //Separates Grids to make it look better
        textGrid.setVgap(1.5);
        textGrid.setHgap(2);

        // Setting Grid position
        textGrid.setAlignment(Pos.CENTER);

        //Adding Text to Grid
        textGrid.add(scoreLable, 1, 0, 3, 1);
        textGrid.add(gameOver, 1, 1, 3, 2);
        textGrid.add(pause, 1, 3, 3, 1);
        textGrid.add(controlCommands.get("W"), 2, 4, 1, 1);
        textGrid.add(controlCommands.get("A"), 1, 5, 1, 1);
        textGrid.add(controlCommands.get("D"), 2, 5, 1, 1);
        textGrid.add(controlCommands.get("S"), 3, 5, 1, 1);

        textGrid.add(close, 1, 6, 3, 1);
    }

    public void refreshScoreLabel() {
        this.scoreLable.setText("Score: 0");
    }

    public void gameOver() {
        gameOver.setText("Game Over, Start Again?");
        pause.setText("Press Enter to Restart");
    }

    public void setScoreLabel(int score) {
        this.scoreLable.setText("Score:" + score);
    }

    public void pause() {
        this.pause.setText("Press Enter to Pause");
    }

    public void resume() {
        this.pause.setText("Press Any Key to Resume");
    }

    public GridPane getTextGrid() {
        return textGrid;
    }
}
