package Projects.GameProject;

import java.util.Scanner;

public class Game {
    private Player player;
    private Player enemy;
    private Difficulty difficulty;
    private boolean gameOver;
    private Scanner input;

    public Game(Difficulty difficulty, Scanner input) {
        this.difficulty = difficulty;
        this.input = input;
        this.gameOver = false;
        this.player = new Player(difficulty, input);
        this.enemy = new AI(difficulty);
    }

    public void startGame() {
        player.placeShips();
        enemy.placeShips();
        while (!gameOver) {          // while the game is not over
            playTurn();
            gameOver = checkWinner();
        }
        endGame();
    }

    // One round: the player's turn, then (if the game continues) the AI's turn
    public void playTurn() {
        takeTurn(player, enemy);
        if (!checkWinner()) {
            takeTurn(enemy, player);
        }
    }

    // Attacker keeps attacking until a miss or until the defender has lost
    private void takeTurn(Player attacker, Player defender) {
        boolean hit;
        do {
            if (attacker == player) {
                System.out.println("Enemy board:");
                System.out.println(enemy.getGrid().toString(false));
                System.out.println("Your board:");
                System.out.println(player.getGrid().toString(true));
            }
            Coordinate target = attacker.chooseAttack(defender.getGrid());
            hit = defender.receiveAttack(target);

            Ship sunkShip = null;
            if (hit) {
                Ship struck = defender.getGrid().getShipAt(target);
                if (struck.isSunk()) {
                    sunkShip = struck;
                }
            }
            attacker.notifyResult(target, hit, sunkShip);
            System.out.println(describeAttack(attacker, target, hit, sunkShip));
        } while (hit && !defender.hasLost());
    }

    // Helper: builds the message such as "You attacked B3: HIT! You sunk the Cruiser!"
    private String describeAttack(Player attacker, Coordinate target, boolean hit, Ship sunkShip) {
        String who = (attacker == player) ? "You" : "The enemy";
        String result = hit ? "HIT!" : "miss.";
        String sunk = (sunkShip != null) ? " Sunk the " + sunkShip.getType().getDisplayName() + "!" : "";
        return who + " attacked " + target + ": " + result + sunk;
    }

    public boolean checkWinner() {
        return player.hasLost() || enemy.hasLost();
    }

    public boolean playAgain() {
        while (true) {
            System.out.print("Play again? (y/n): ");
            String answer = input.nextLine().trim().toLowerCase();
            if (answer.equals("y")) return true;
            if (answer.equals("n")) return false;
        }
    }

    public void endGame() {
        if (enemy.hasLost()) {
            System.out.println("All enemy ships sunk. You win!");
        } else {
            System.out.println("All your ships were sunk. The enemy wins.");
        }
    }
}
