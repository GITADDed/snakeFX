package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.*;
import java.io.*;

import java.util.ArrayList;
import java.util.Random;


//Snake Class
//Contains X and Y positions for every node
//With their respective get and sets
class Snake {
    public Rectangle body = new Rectangle(12, 12, Color.WHITE);
    private int Xpos;
    private int Ypos;
    private int oldXpos;
    private int oldYpos;

    Snake(int X, int Y) {
        oldXpos = Xpos = X;
        oldYpos = Ypos = Y;
    }

    public void setPos(int X, int Y) {
        oldXpos = Xpos;
        oldYpos = Ypos;

        Xpos = X;
        Ypos = Y;

    }

    public int getOldXpos() {
        return oldXpos;
    }

    public int getOldYpos() {
        return oldYpos;
    }

    public int getXpos() {
        return Xpos;
    }

    public int getYpos() {
        return Ypos;
    }
}

public class Game extends Application {

    private static Socket client;
    private static BufferedWriter out;
    private static BufferedReader in;
    public boolean botOn = false;
    private int games = 0;

    public static void main(String[] args) {
        launch(args);
    }


    GridPane GameGrid = new GridPane();
    GridPane TextGrid = new GridPane();

    //Area of the Grid, ex. 20 means 20 columns x 20 rows
    int GridSizeSquared = 20;

    //Score Text
    Label Score = new Label("Score: 0");
    //Game Over Text
    Label GameOver = new Label("");

    //Instructions/Controls
    Label Pause = new Label("Press Any Key to Start");

    Label ControlU = new Label("W");
    Label ControlD = new Label("S");
    Label ControlL = new Label("A");
    Label ControlR = new Label("D");

    Label Close = new Label("Press the Escape Key to Close");


    //ArrayList for the Snake and its body
    //Index 0 of this ArrayList is treated as the Head, never remove the head
    ArrayList<Snake> SnakeP = new ArrayList<>(0);


    //This will be used to keep the game running
    Timeline Loop;

    //How often the Loop is refreshed
    double LoopSpeed = 1 / 3.0;

    //movementX and movementY, these indicate the directid of the snake head
    //Initial movement is 0
    int mX = 0, mY = 0;

    //Snake Head's Initial Position
    int posX = new Random().nextInt(GridSizeSquared), posY = new Random().nextInt(GridSizeSquared);


    //Snake Food, same size as snake
    Rectangle Food = new Rectangle(12, 12, Color.ORANGE);

    //Keeps Score
    int foodN = 0;

    //Random Position for the food, within the grid
    int FoodPosX = new Random().nextInt(GridSizeSquared);
    int FoodPosY = new Random().nextInt(GridSizeSquared);

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
            FillGrid(GameGrid);

            //Constructing Snake's Head
            SnakeP.add(new Snake(posX, posY));

            //Seperates Grids to make it look better
            GameGrid.setVgap(1.5);
            GameGrid.setHgap(1.5);
            TextGrid.setVgap(1.5);
            TextGrid.setHgap(2);

            //Setting Grid Positions
            GameGrid.setAlignment(Pos.CENTER);
            TextGrid.setAlignment(Pos.CENTER);

            //Adding Food and Snake Head in their initial positions
            GameGrid.add(Food, FoodPosX, FoodPosY);
            GameGrid.add(SnakeP.get(0).body, posX, posY);

            //Adding Text to Grid
            TextGrid.add(Score, 1, 0, 3, 1);
            TextGrid.add(GameOver, 1, 1, 3, 2);
            TextGrid.add(Pause, 1, 3, 3, 1);
            TextGrid.add(ControlU, 2, 4, 1, 1);
            TextGrid.add(ControlL, 1, 5, 1, 1);
            TextGrid.add(ControlD, 2, 5, 1, 1);
            TextGrid.add(ControlR, 3, 5, 1, 1);

            TextGrid.add(Close, 1, 6, 3, 1);


            //Allows us to use both grids in the same screen
            FlowPane Screen = new FlowPane(Orientation.VERTICAL, GameGrid, TextGrid);


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
                    new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {

                            //Moves Snake
                            MoveChar();
                        }
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
        if (mX == -1 && (posX == 0)) {
            Die();
            mX = 0;
        } else if (mY == -1 && (posY == 0)) {
            Die();
            mY = 0;
        } else if (mX == 1 && (posX == GridSizeSquared - 1)) {
            Die();
            mX = 0;
        } else if (mY == 1 && (posY == GridSizeSquared - 1)) {
            Die();
            mY = 0;
        } else {

            //Updates head position
            GameGrid.getChildren().remove(SnakeP.get(0).body);
            posX += mX;
            posY += mY;
            GameGrid.add(SnakeP.get(0).body, posX, posY);
            SnakeP.get(0).setPos(posX, posY);

            //Update for rest of body
            if (SnakeP.size() > 1) {
                for (int x = 1; x < SnakeP.size(); x++) {
                    GameGrid.getChildren().remove(SnakeP.get(x).body);
                    GameGrid.add(SnakeP.get(x).body, SnakeP.get(x - 1).getOldXpos(), SnakeP.get(x - 1).getOldYpos());
                    SnakeP.get(x).setPos(SnakeP.get(x - 1).getOldXpos(), SnakeP.get(x - 1).getOldYpos());
                }

            }
            //If You find food the snake will grow
            if (posX == FoodPosX && posY == FoodPosY) {
                //Grows Snake duh
                Grow();
            }

            //If you crash into any part of your body, then die
            for (int x = 1; x < SnakeP.size(); x++) {
                if (posX == SnakeP.get(x).getXpos() && posY == SnakeP.get(x).getYpos()) {
                    Die();
                }
            }
            if (botOn)
                Bot.solution(FoodPosX, FoodPosY, posX, posY, GridSizeSquared);
        }


    }

    //Detects Key Presses
    public void KeyPressedProcess(KeyEvent event) {
        try {
            //If you GameOver and Restart
            if (start == false && dead && event.getCode() == KeyCode.ENTER) {
                Pause.setText("Press Enter to Pause");
                Score.setText("Score: 0");
                GameOver.setText("");
                Loop.play();
                start = true;
                dead = false;
            }
            //If Paused and Resumed
            else if (start == false && dead == false) {
                Pause.setText("Press Enter to Pause");
                Loop.play();
                start = true;
            }

            //If Enter is pressed, game will pause
            if (event.getCode() == KeyCode.ENTER) {
                Pause.setText("Press Any Key to Resume");
                Loop.stop();
                start = false;
            }
            if (event.getCode() == KeyCode.W || event.getCode() == KeyCode.A || event.getCode() == KeyCode.S || event.getCode() == KeyCode.D) {
                out.write(event.getCode() + " " + mY + " " + mX + "\n");
                out.flush();
                String str;
                    if ((str = in.readLine()) != null) {
                        String[] commands = str.split(" ");
                        mY = Integer.parseInt(commands[0]);
                        mX = Integer.parseInt(commands[1]);
                    }
            }
            //Closes program when escape is pressed
            if (event.getCode() == KeyCode.ESCAPE) {
                out.write("close");
                out.flush();
                out.close();
                in.close();
                client.close();
//                System.exit(0);
                Platform.exit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Fills Grid with rectangles
    public void FillGrid(GridPane Grid) {
        for (int x = 0; x < GridSizeSquared; x++) {
            GameGrid.addColumn(x, new Rectangle(12, 12, Color.GRAY));

            for (int y = 1; y < GridSizeSquared; y++)
                GameGrid.addRow(y, new Rectangle(12, 12, Color.GRAY));

        }


    }

    //Changes randomly Food's position
    public void PlaceFood() {
        Random rPos = new Random();

        int newPosX = rPos.nextInt(GridSizeSquared);
        int newPosY = rPos.nextInt(GridSizeSquared);

        FoodPosX = newPosX;
        FoodPosY = newPosY;

        GameGrid.getChildren().remove(Food);
        GameGrid.add(Food, newPosX, newPosY);

    }

    //Grows Snake's Body
    public void Grow() {
        //Adds new Tail where last Tail's position was
        SnakeP.add(new Snake(SnakeP.get(SnakeP.size() - 1).getOldXpos(),
                SnakeP.get(SnakeP.size() - 1).getOldYpos()));

        GameGrid.add(SnakeP.get(SnakeP.size() - 1).body,
                SnakeP.get(SnakeP.size() - 1).getOldXpos(),
                SnakeP.get(SnakeP.size() - 1).getOldYpos());

        foodN++;
        Score.setText("Score:" + foodN);
        //Increases Score

        //Randomly Places new Food
        PlaceFood();

    }

    //Makes you Game Over
    public void Die() {

        int size = SnakeP.size();


        //First Removes all but the head from the grid
        for (int x = size - 1; x > 0; x--)
            GameGrid.getChildren().remove(SnakeP.get(x).body);

        //Now Removes all but the head from the arrayList
        for (int x = size - 1; x > 0; x--)
            SnakeP.remove(x);

        //Pauses Game and lets game know that you lost
        start = false;
        dead = true;
        Loop.stop();

        //Changes Text onscreen
        GameOver.setText("Game Over, Start Again?");
        Pause.setText("Press Enter to Restart");

        //Generates new Position for the snake to start again from
        posX = new Random().nextInt(GridSizeSquared);
        posY = new Random().nextInt(GridSizeSquared);

        //Places Snake in that new Postion
        GameGrid.getChildren().remove(SnakeP.get(0).body);
        GameGrid.add(SnakeP.get(0).body, posX, posY);
        SnakeP.get(0).setPos(posX, posY);

        //Resets score
        foodN = 0;

    }

}