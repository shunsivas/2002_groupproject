package restaurant;

import restaurant.control.GameConfig;
import restaurant.control.GameController;
import restaurant.ui.ConsoleUI;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        long seed = 42;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--seed") && i + 1 < args.length) {
                seed = Long.parseLong(args[++i]);
            } else {
                System.err.println("Usage: Main [--seed N]");
                System.exit(2);
            }
        }
        

        GameController game = new GameController(GameConfig.base(seed));
        new ConsoleUI(game, new Scanner(System.in), System.out).run();
        }
}
