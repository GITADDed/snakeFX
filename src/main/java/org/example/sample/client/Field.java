package org.example.sample.client;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.sample.core.Food;
import org.example.sample.core.SnakeSegment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Field {
    private final GridPane gameGrid = new GridPane();

    //Area of the Grid, ex. 20 means 20 columns x 20 rows
    private final int fieldSize;

    private final Food food = new Food();
    private final List<SnakeSegment> snake;

    public Field(int fieldSize) {
        this.fieldSize = fieldSize;
        this.snake = new ArrayList<>();
    }

    {
        //Separates Grids to make it look better
        gameGrid.setVgap(1.5);
        gameGrid.setHgap(1.5);

        //Setting Grid Positions
        gameGrid.setAlignment(Pos.CENTER);
    }

    //Fills Grid with rectangles
    public void fillGrid() {
        for (int x = 0; x < fieldSize; x++) {
            gameGrid.addColumn(x, new Rectangle(12, 12, Color.GRAY));

            for (int y = 1; y < fieldSize; y++)
                gameGrid.addRow(y, new Rectangle(12, 12, Color.GRAY));

        }
    }

    public void clearSnakeBody() {
        ListIterator<SnakeSegment> it = snake.listIterator(snake.size());
        while (it.hasPrevious()) {
            SnakeSegment snakeSegment = it.previous();
            gameGrid.getChildren().remove(snakeSegment.getBody());
            it.remove();
        }
    }

    //Changes randomly Food's position
    public void placeFood() {
        food.nextPosition(fieldSize);
        GridPane.setConstraints(food.getBody(), food.getFoodPosX(), food.getFoodPosY());
    }

    public void initFoodAndSnakePosition() {
        SnakeSegment head = new SnakeSegment(fieldSize);

        snake.add(head);
        food.nextPosition(fieldSize);

        //Adding Food and Snake Head in their initial positions
        gameGrid.add(food.getBody(), food.getFoodPosX(), food.getFoodPosY());
        gameGrid.add(head.getBody(), head.getX(), head.getY());
    }

    public void increaseSnake() {
        gameGrid.add(snake.getLast().getBody(), snake.getLast().getXOld(), snake.getLast().getYOld());
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public List<SnakeSegment> getSnake() {
        return snake;
    }

    public GridPane getGameGrid() {
        return gameGrid;
    }

    public boolean isSnakeEatFood() {
        if (snake.getFirst().getX() == food.getFoodPosX() && snake.getFirst().getY() == food.getFoodPosY()) {
            return true;
        }

        return false;
    }

    public void updateSnakePosition() {
        for (SnakeSegment segment : snake) {
            GridPane.setConstraints(segment.getBody(), segment.getX(), segment.getY());
        }
    }

    public void moveSnake(int dx, int dy) {
        for (int i = snake.size() - 1; i > 0; i--) {
            SnakeSegment current = snake.get(i);
            SnakeSegment prev = snake.get(i - 1);
            current.setPos(prev.getX(), prev.getY());
        }

        SnakeSegment head = snake.getFirst();
        head.setPos(head.getX() + dx, head.getY() + dy);
    }

    public boolean isGameOver() {
        int x = snake.getFirst().getX();
        int y = snake.getFirst().getY();

        return isMeetWall(x, y) || isSnakeMeetBody(x, y);
    }

    private boolean isMeetWall(int x, int y) {
        return x < 0 || y < 0 || x > fieldSize - 1 || y > fieldSize - 1;
    }

    private boolean isSnakeMeetBody(int x, int y) {
        if (snake.size() > 1) {
            ListIterator<SnakeSegment> it = snake.listIterator(1);

            while (it.hasNext()) {
                SnakeSegment snakeSegment = it.next();

                if (snakeSegment.getX() == x && snakeSegment.getY() == y) {
                    return true;
                }
            }
        }
        return false;
    }

    public void clearFoodBody() {
        gameGrid.getChildren().remove(food.getBody());
    }
}
