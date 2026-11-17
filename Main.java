package Projects.GameProject;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean keepPlaying = true;

        while (keepPlaying) {
            Difficulty difficulty = chooseDifficulty(input);
            Game game = new Game(difficulty, input);
            game.startGame();               // runs the whole game and calls endGame()
            keepPlaying = game.playAgain(); // asks the player
        }

        System.out.println("Thanks for playing!");
        input.close();
    }

    // Helper: repeat until the player enters a valid choice
    private static Difficulty chooseDifficulty(Scanner input) {
        while (true) {
            System.out.println("Select difficulty: 1) Easy  2) Medium  3) Hard");
            String choice = input.nextLine().trim();
            if (choice.equals("1")) return Difficulty.EASY;
            if (choice.equals("2")) return Difficulty.MEDIUM;
            if (choice.equals("3")) return Difficulty.HARD;
            System.out.println("Invalid choice. Please enter 1, 2, or 3.");
        }
    }
}
