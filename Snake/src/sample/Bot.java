package sample;

import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;

public class Bot {

    public static void solution(int FoodPosX, int FoodPosY, int posX, int posY, int GridSizeSquared) {
        Robot r = new Robot();
        System.out.println("posY = " + posY + " posX " + posX);
        boolean flag = false;
        if (FoodPosX == posX) {
            if (FoodPosY < posY) {
                r.keyPress(KeyCode.W);
                r.keyRelease(KeyCode.W);
                flag = true;
            } else {
                r.keyPress(KeyCode.S);
                r.keyRelease(KeyCode.S);
                flag = true;
            }
        }
        if (FoodPosY == posY) {
            if (FoodPosX < posX) {
                r.keyPress(KeyCode.A);
                r.keyRelease(KeyCode.A);
                flag = true;
            } else {
                r.keyPress(KeyCode.D);
                r.keyRelease(KeyCode.D);
                flag = true;
            }
        }
        if (flag == false) {
            if (posX + 1 == GridSizeSquared || posX - 1 == -1) {
                if (posY + 1 != GridSizeSquared && posY - 1 != -1) {
                    r.keyPress(KeyCode.W);
                    r.keyRelease(KeyCode.W);
                }
                if (posY + 1 == GridSizeSquared) {
                    r.keyPress(KeyCode.S);
                    r.keyRelease(KeyCode.S);
                }
                if (posY - 1 == -1) {
                    r.keyPress(KeyCode.W);
                    r.keyRelease(KeyCode.W);
                }
            }
            if (posY + 1 == GridSizeSquared || posY - 1 == -1) {
                if (posX + 1 != GridSizeSquared && posX - 1 != -1) {
                    r.keyPress(KeyCode.A);
                    r.keyRelease(KeyCode.A);
                }
                if (posX + 1 == GridSizeSquared) {
                    r.keyPress(KeyCode.A);
                    r.keyRelease(KeyCode.A);
                }
                if (posX - 1 == -1) {
                    r.keyPress(KeyCode.D);
                    r.keyRelease(KeyCode.D);
                }
            }
        }
    }
}
