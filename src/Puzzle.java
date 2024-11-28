
import java.io.*;
import java.util.*;

public class Puzzle {

	//Declare fields
	static String[][] puzzle;
	static String[][] solved3x3 = { { "1", "2", "3" }, { "4", "5", "6" }, { "7", "8", " " } };
	static String[][] solved4x4 = { { "1", "2", "3", "4" }, { "5", "6", "7", "8" }, { "9", "10", "11", "12" },{ "13", "14", "15", " " } };
	static String[][] solved5x5 = { { "1", "2", "3", "4", "5" }, { "6", "7", "8", "9", "10" },{ "11", "12", "13", "14", "15" }, { "16", "17", "18", "19", "20" }, { "21", "22", "23", "24", " " } };
	static List <String[][]> unsolvedPuzzles = new ArrayList<>();
	

	 public static void puzzleSelector(int size) {
	        List<String> numbers = new ArrayList<>();

	        // Populate the list with numbers from 1 to size*size - 1 and one empty space
	        for (int i = 1; i < size * size; i++) {
	            numbers.add(String.valueOf(i));
	        }
	        numbers.add(" ");  // Add the empty space

	        // Shuffle the list to randomize the numbers
	        Collections.shuffle(numbers);

	        // Assign the shuffled numbers to the puzzle grid
	        puzzle = new String[size][size];
	        int index = 0;
	        for (int i = 0; i < size; i++) {
	            for (int j = 0; j < size; j++) {
	                puzzle[i][j] = numbers.get(index++);
	            }
	        }

	        // Add the puzzle to unsolvedPuzzles list
	        String[][] copiedPuzzle = new String[size][size];
	        for (int i = 0; i < size; i++) {
	            copiedPuzzle[i] = Arrays.copyOf(puzzle[i], size);
	        }
	        unsolvedPuzzles.add(copiedPuzzle);
	    }

	//Get solved array value method which supports the is solved method
	public static String getSolvedArrayValue(int i, int j, int size) {
		if (size == 3) {
			return solved3x3[i][j];
		} else if (size == 4) {
			return solved4x4[i][j];
		} else {
			return solved5x5[i][j];
		}
	}

	//Is solved method
	public static boolean isSolved() {
		for (int i = 0; i < puzzle.length; i++) {
			for (int j = 0; j < puzzle[i].length; j++) {
				if (!puzzle[i][j].equals(getSolvedArrayValue(i, j, puzzle.length))) {
					return false;
				}
			}
		}
		return true;
	}

	


	//Check move method that supports solve method
	public static boolean checkMove(String move) {
		for (String[] row : puzzle) {
			for (String value : row) {
				if (value.equals(move)) {
					return true;
				}
			}
		}
		return false;
	}

	//Make move method that supports solved method
	public static boolean makeMove(String move) {
		int emptySlotRow = -1;
		int emptySlotCol = -1;
		int moveRow = -1;
		int moveCol = -1;

		for (int i = 0; i < puzzle.length; i++) {
			for (int j = 0; j < puzzle[i].length; j++) {
				if (puzzle[i][j].equals(" ")) {
					emptySlotRow = i;
					emptySlotCol = j;
				} else if (puzzle[i][j].equals(move)) {
					moveRow = i;
					moveCol = j;
				}
			}
		}

		if ((Math.abs(emptySlotRow - moveRow) == 1 && emptySlotCol == moveCol)	|| (Math.abs(emptySlotCol - moveCol) == 1 && emptySlotRow == moveRow)) {
			puzzle[emptySlotRow][emptySlotCol] = move;
			puzzle[moveRow][moveCol] = " ";
			return true;
		} 
		else {
			return false;
		}
	}
}
