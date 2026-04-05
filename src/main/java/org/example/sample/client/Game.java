package org.example.sample.client;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.sample.core.SnakeSegment;

import java.io.*;
import java.net.Socket;
import java.util.*;


public class Game extends Application {

    private static Socket client;
    private static BufferedWriter out;
    private static BufferedReader in;
    public boolean botOn = false;
    private int games = 0;

    public static void main(String[] args) {
        launch(args);
    }

    Field field = new Field(20);

    Window window = new Window();

    private int dx = 1;
    private int dy = 0;

    List<KeyCode> keyCodes = List.of(KeyCode.W, KeyCode.A, KeyCode.S, KeyCode.D);


    //This will be used to keep the game running
    Timeline Loop;

    //How often the Loop is refreshed
    double LoopSpeed = 1 / 3.0;

    //Keeps Score
    int foodN = 0;

    //True = Game is Running
    //False = Game is Paused
    boolean start = false;

    //Did you GameOver?
    boolean dead = false;

    public void start(Stage PrimaryStage) {
        try {
            client = new Socket("localhost", 8080);
            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            //Fills Grid with Gray Squares
            field.fillGrid();

            //Adding Food and Snake Head in their initial positions
            field.initFoodAndSnakePosition();

            //Allows us to use both grids in the same screen
            FlowPane Screen = new FlowPane(Orientation.VERTICAL, field.getGameGrid(), window.getTextGrid());


            //Creating Game Scene with a black background
            Scene Game = new Scene(Screen);
            Game.setFill(Color.BEIGE);

            //Detects a Key Being Pressed
            Game.setOnKeyPressed(this::KeyPressedProcess);

            //Generates Window
            PrimaryStage.setTitle("Game");
            PrimaryStage.setScene(Game);
            PrimaryStage.show();

            //Initializing Loop as timeline.
            Loop = new Timeline(new KeyFrame(Duration.seconds(LoopSpeed),
                    event -> {
                        //Moves Snake
                        MoveChar();
                    }));
            Loop.setCycleCount(Timeline.INDEFINITE);
            //^ Loop will run endlessly
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void MoveChar() {
        //if-elses for when the snake crashes into a wall
        //if crash happens then die
        field.moveSnake(dx, dy);

        if (field.isGameOver()) {
            die();
            return;
        }

        field.updateSnakePosition();

        //If You find food the snake will grow
        if (field.isSnakeEatFood()) {
            //Grows Snake duh
            grow();
        }

    }

    //Detects Key Presses
    public void KeyPressedProcess(KeyEvent event) {
        try {
            //If you GameOver and Restart
            if (!start && dead && event.getCode() == KeyCode.ENTER) {
                window.pause();
                window.refreshScoreLabel();
                Loop.play();
                start = true;
                dead = false;
            }
            //If Paused and Resumed
            else if (!start && !dead) {
                window.pause();
                Loop.play();
                start = true;
            }

            //If Enter is pressed, game will pause
            if (event.getCode() == KeyCode.ENTER) {
                window.resume();
                Loop.stop();
                start = false;
            }
            if (keyCodes.contains(event.getCode())) {
                List<SnakeSegment> snake = field.getSnake();
                out.write(event.getCode() + " " + dy + " " + dx + "\n");
                out.flush();
                String str;
                if ((str = in.readLine()) != null) {
                    String[] commands = str.split(" ");
                    dy = Integer.parseInt(commands[0]);
                    dx = Integer.parseInt(commands[1]);
//                        snake.getFirst().setPos(snake.getFirst().getX() + dx, snake.getFirst().getY() + dy);
                }
            }
            //Closes program when escape is pressed
            if (event.getCode() == KeyCode.ESCAPE) {
                out.write("close");
                out.flush();
                out.close();
                in.close();
                client.close();
                Platform.exit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Grows Snake's Body
    public void grow() {
        //Adds new Tail where last Tail's position was
        List<SnakeSegment> snake = field.getSnake();
        snake.add(new SnakeSegment(snake.getLast().getXOld(), snake.getLast().getYOld()));

        field.increaseSnake();

        //Increases Score
        foodN++;
        window.setScoreLabel(foodN);

        //Randomly Places new Food
        field.placeFood();
    }

    //Makes you Game Over
    public void die() {
        //Clear field
        field.clearSnakeBody();
        field.clearFoodBody();

        //Pauses Game and lets game know that you lost
        start = false;
        dead = true;
        Loop.stop();

        //Changes Text onscreen
        window.gameOver();

        //Get start new Game
        field.initFoodAndSnakePosition();

        //Resets score
        foodN = 0;
    }

}