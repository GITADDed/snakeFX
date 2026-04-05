package org.example.sample.core;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Food {
    private final Rectangle body;

    private int foodPosX;
    private int foodPosY;

    private final Random random = new Random();

    public Food() {
        this.body = new Rectangle(12, 12, Color.ORANGE);
        this.foodPosX = 0;
        this.foodPosY = 0;
    }

    public Rectangle getBody() {
        return body;
    }

    public void nextPosition(int fieldSize) {
        foodPosX = random.nextInt(fieldSize);
        foodPosY = random.nextInt(fieldSize);
    }

    public int getFoodPosY() {
        return foodPosY;
    }

    public int getFoodPosX() {
        return foodPosX;
    }
}
