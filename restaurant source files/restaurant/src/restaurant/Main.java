package restaurant;

import restaurant.control.GameConfig;
import restaurant.control.GameController;
import restaurant.ui.ConsoleUI;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        long seed = 42;
        

        GameController game = new GameController(GameConfig.base(seed));
        new ConsoleUI(game, new Scanner(System.in), System.out, false).run();
        }
}
