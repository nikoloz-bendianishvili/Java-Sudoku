import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}

	public class Spot {
		int row;
		int col;
		int num;
		int possibleNums;

		Spot(int row, int col, int num, int possibleNums) {
			this.row = row;
			this.col = col;
			this.num = num;
			this.possibleNums = possibleNums;
		}

		public int getValue() {
			return num;
		}

		public int getPossibleNums() {
			return possibleNums;
		}

		public int getCol() {
			return col;
		}

		public int getRow() {
			return row;
		}

		public void setValue(int value) {
			this.num = value;
		}
	}

	public Spot getSpot(int row, int col) {
		return sudoku[row][col];
	}
	
	
	private Spot[][] sudoku;
	private ArrayList<Spot> list;
	private String firstSolution;
	private long elapsed;

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		// YOUR CODE HERE
		sudoku = new Spot[SIZE][SIZE];
		list = new ArrayList<>();
		firstSolution = "";
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				int possibleNums = count(i, j, ints);
				Spot curr = new Spot(i, j, ints[i][j], possibleNums);
				sudoku[i][j] = curr;
				if(ints[i][j] == 0) {
					putInList(curr, list);
				}
			}
		}
	}

	public Sudoku(String text) {
		this(textToGrid(text));
	}

	private int count(int i, int j, int[][] ints) {
		if(ints[i][j] != 0) {
			return 0;
		}
        HashSet<Integer> found = new HashSet<>();
		rowCount(i, ints, found);
		colCount(j, ints, found);
		squareCount(i, j, ints, found);
		return SIZE - found.size();
	}

	private void rowCount(int i, int[][] ints, HashSet<Integer> found) {
		for (int j = 0; j < SIZE; j++) {
			if (ints[i][j] != 0) {
				found.add(ints[i][j]);
			}
		}
	}

	private void colCount(int j, int[][] ints, HashSet<Integer> found) {
		for (int i = 0; i < SIZE; i++) {
			if (ints[i][j] != 0) {
				found.add(ints[i][j]);
			}
		}
	}

	private void squareCount(int i, int j, int[][] ints, HashSet<Integer> found) {
		int rStart = i / PART * PART;
		int cStart = j / PART * PART;
		for(int k = rStart; k < rStart + PART; k++) {
			for(int f = cStart; f < cStart + PART; f++) {
				if(ints[k][f] != 0) {
					found.add(ints[k][f]);
				}
			}
		}
	}

	private void putInList(Spot curr, ArrayList<Spot> list) {
		int i = 0;
		for(; i < list.size(); i++) {
			if(curr.getPossibleNums() < list.get(i).getPossibleNums()) {
				break;
			}
		}
		list.add(i, curr);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				if(j != 0)sb.append(" ");
				sb.append(String.valueOf(sudoku[i][j].getValue()));
			}

			if(i != SIZE - 1) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		long startTime = System.currentTimeMillis();
		int res = countSolutions(0);
		elapsed = System.currentTimeMillis() - startTime;
		return res; // YOUR CODE HERE
	}

	private int countSolutions(int index) {
		if(index == list.size()) {
			if(firstSolution.isEmpty()) {
				firstSolution = toString();
			}
			return 1;
		}
		Spot curr = list.get(index);
		Set<Integer> possibilities = new HashSet<>();
		for(int i = 1; i <= SIZE; i++) {
			possibilities.add(i);
		}
		remove(possibilities, curr);
		int count = 0;
		for(Integer s : possibilities) {
			curr.setValue(s);
			count += countSolutions(index + 1);
			curr.setValue(0);
			if(count >= MAX_SOLUTIONS) {
				return MAX_SOLUTIONS;
			}
		}
		return count;
	}

	private void remove(Set<Integer> possibilities, Spot curr) {
		int currI = curr.getRow();
		int currJ = curr.getCol();
		for(int i = 0; i < SIZE; i++) {
			possibilities.remove(sudoku[i][currJ].getValue());
		}
		for(int j = 0; j < SIZE; j++) {
			possibilities.remove(sudoku[currI][j].getValue());
		}
		int rStart = currI / PART * PART;
		int cStart = currJ / PART * PART;
		for(int i = rStart; i < rStart + PART; i++) {
			for(int j = cStart; j < cStart + PART; j++) {
				possibilities.remove(sudoku[i][j].getValue());
			}
		}
	}
	
	public String getSolutionText() {
		return firstSolution; // YOUR CODE HERE
	}
	
	public long getElapsed() {
		return elapsed; // YOUR CODE HERE
	}

}
