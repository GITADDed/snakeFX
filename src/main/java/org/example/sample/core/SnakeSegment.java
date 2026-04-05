package org.example.sample.core;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

//Snake Class
//Contains X and Y positions for every node
//With their respective get and sets
public class SnakeSegment {
    private Rectangle body;
    private int x;
    private int y;
    private int xOld;
    private int yOld;
    private final Random random  = new Random();

    public SnakeSegment(int fieldSize) {
        this.xOld = this.x = random.nextInt(fieldSize);
        this.yOld = this.y = random.nextInt(fieldSize);
        this.body =  new Rectangle(12, 12, Color.WHITE);
    }

    public SnakeSegment(int x, int y) {
        this.xOld = this.x = x;
        this.yOld = this.y = y;
        this.body =  new Rectangle(12, 12, Color.WHITE);
    }

    public void setPos(int x, int y) {
        xOld = this.x;
        yOld = this.y;

        this.x = x;
        this.y = y;

    }

    public int getXOld() {
        return xOld;
    }

    public int getYOld() {
        return yOld;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getBody() {
        return body;
    }
}
