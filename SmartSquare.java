import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Functionality for finding number tiles and bombs
 * 
 * @author Callum Penny
 */

public class SmartSquare extends GameSquare {
	private boolean thisSquareHasBomb = false;
	public static final int MINE_PROBABILITY = 10;
	public boolean revealed = false;
	public static int squareCount = 0;
	public static int boardSize = 0;
	public static int bombCount = 0;

	public SmartSquare(int x, int y, GameBoard board) {
		super(x, y, "images/blank.png", board);
		boardSize = (x + 1) * (y + 1);
		Random r = new Random();
		thisSquareHasBomb = (r.nextInt(MINE_PROBABILITY) == 0);
		if (thisSquareHasBomb == true) {
			bombCount++;
		}
	}

	/**
	 * Handle what happens when a square is clicked
	 */
	public void clicked() {
		SmartSquare yourSquare = (SmartSquare) board.getSquareAt(xLocation, yLocation);
		// if square has bomb, game over
		if (yourSquare.thisSquareHasBomb == true) {
			this.setImage("images/bomb.png");
			end();
		}

		// if square doesnt have bomb set square image
		if (yourSquare != null && yourSquare.thisSquareHasBomb == false) {
			yourSquare.setImage("images/" + yourSquare.count() + ".png");
			count();
		}

		// set square clicked revealed to true, increase square count
		// if square has been revealed and is clicked, do nothing
		if (yourSquare != null && yourSquare.revealed == false && yourSquare.thisSquareHasBomb == false
				&& yourSquare.count() > 0) {
			yourSquare.revealed = true;
			squareCount++;
		} else if (yourSquare != null && yourSquare.revealed == true && yourSquare.thisSquareHasBomb == false
				&& yourSquare.count() > 0) {
			return;
		}

		// if no bombs surrounding square, recusively open surrounding squares
		if (yourSquare.count() == 0) {
			open(xLocation, yLocation);
		}

		// if the squares left is same amount as bombs, game is won
		if (squareCount == (boardSize - bombCount)) {
			win();
		}
	}

	/**
	 * Count the amount of bombs surrounding a square
	 * 
	 * @return count
	 */
	public int count() {
		int count = 0;
		for (int xOff = -1; xOff <= 1; xOff++) {
			for (int yOff = -1; yOff <= 1; yOff++) {
				SmartSquare yourSquare = (SmartSquare) board.getSquareAt(xLocation + xOff, yLocation + yOff);
				if (yourSquare != null && yourSquare.thisSquareHasBomb == true) {
					count++;
				}
			}
		}
		return count;
	}

	private void msgbox(String s) {
		JOptionPane.showMessageDialog(null, s);
	}

	public void end() {
		msgbox("Game over!");
		System.exit(0);
	}

	public void win() {
		msgbox("Game won!");
		System.exit(0);
	}

	/**
	 * Handle opening squares If square hasn't been revealed and has bomb
	 * surrounding, then set revealed to true, set bomb image and increase
	 * squareCount If square doesn't have bombs surrounding, increase squareCount,
	 * set revealed to true and recursively open surrounding squares
	 * 
	 * @param xLocation
	 * @param yLocation
	 */
	public void open(int xLocation, int yLocation) {
		SmartSquare yourSquare = (SmartSquare) board.getSquareAt(xLocation, yLocation);
		if (yourSquare != null && yourSquare.revealed == false && yourSquare.thisSquareHasBomb == false
				&& yourSquare.count() > 0) {
			squareCount++;
			yourSquare.revealed = true;
			yourSquare.setImage("images/" + yourSquare.count() + ".png");
			return;
		}

		else if (yourSquare != null && yourSquare.revealed == false && yourSquare.thisSquareHasBomb == false
				&& yourSquare.count() == 0) {
			squareCount++;
			yourSquare.revealed = true;

			open(xLocation + 1, yLocation);
			open(xLocation - 1, yLocation);
			open(xLocation, yLocation - 1);
			open(xLocation, yLocation + 1);
			open(xLocation + 1, yLocation - 1);
			open(xLocation - 1, yLocation + 1);
			open(xLocation - 1, yLocation - 1);
			open(xLocation + 1, yLocation + 1);

			yourSquare.setImage("images/0.png");
		}
	}
}