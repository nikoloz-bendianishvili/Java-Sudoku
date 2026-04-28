import junit.framework.TestCase;

public class SudokuTests extends TestCase {

    public void testSpot() {
        String puzzle = "1 6 4 0 0 0 0 0 2\n" +
                "2 0 0 4 0 3 9 1 0\n" +
                "0 0 5 0 8 0 4 0 7\n" +
                "0 9 0 0 0 6 5 0 0\n" +
                "5 0 0 1 0 2 0 0 8\n" +
                "0 0 8 9 0 0 0 3 0\n" +
                "8 0 9 0 4 0 2 0 0\n" +
                "0 7 3 5 0 9 0 0 1\n" +
                "4 0 0 0 0 0 6 7 9";

        Sudoku sudo = new Sudoku(puzzle);

        Sudoku.Spot empty = sudo.getSpot(3, 2);
        assertEquals(3, empty.getRow());
        assertEquals(2, empty.getCol());
        assertEquals(0, empty.getValue());
        assertEquals(3, empty.getPossibleNums());

        empty.setValue(7);
        assertEquals(7, empty.getValue());
        assertEquals(3, empty.getRow());
        assertEquals(2, empty.getCol());

        Sudoku.Spot filled = sudo.getSpot(0, 0);
        assertEquals(0, filled.getRow());
        assertEquals(0, filled.getCol());
        assertEquals(1, filled.getValue());
        assertEquals(0, filled.getPossibleNums());

        String updated = "1 6 4 0 0 0 0 0 2\n" +
                "2 0 0 4 0 3 9 1 0\n" +
                "0 0 5 0 8 0 4 0 7\n" +
                "0 9 7 0 0 6 5 0 0\n" +
                "5 0 0 1 0 2 0 0 8\n" +
                "0 0 8 9 0 0 0 3 0\n" +
                "8 0 9 0 4 0 2 0 0\n" +
                "0 7 3 5 0 9 0 0 1\n" +
                "4 0 0 0 0 0 6 7 9";
        assertEquals(updated, sudo.toString());
    }


    public void testToString() {
        String puzzle =  "5 3 4 6 7 8 9 1 2\n" +
                "6 7 2 1 9 5 3 4 8\n" +
                "1 9 8 3 4 2 5 6 7\n" +
                "8 5 9 7 6 1 4 2 3\n" +
                "4 2 6 8 5 3 7 9 1\n" +
                "7 0 3 9 2 4 8 5 6\n" +
                "9 6 1 5 3 7 2 8 4\n" +
                "2 8 7 4 1 9 6 3 5\n" +
                "3 4 5 2 8 6 1 7 9";

        String expected = "5 3 4 6 7 8 9 1 2\n" +
                "6 7 2 1 9 5 3 4 8\n" +
                "1 9 8 3 4 2 5 6 7\n" +
                "8 5 9 7 6 1 4 2 3\n" +
                "4 2 6 8 5 3 7 9 1\n" +
                "7 1 3 9 2 4 8 5 6\n" +
                "9 6 1 5 3 7 2 8 4\n" +
                "2 8 7 4 1 9 6 3 5\n" +
                "3 4 5 2 8 6 1 7 9";

        Sudoku sudoku = new Sudoku(puzzle);
        sudoku.solve();
        assertEquals(expected, sudoku.getSolutionText());
        assertEquals(puzzle, sudoku.toString());
    }

    public void testOneSolutionGrid() {
        String puzzle = "5 3 4 6 7 8 9 1 2\n" +
                "6 7 2 1 9 5 3 4 8\n" +
                "1 9 8 3 4 2 5 6 7\n" +
                "8 5 9 7 6 1 4 2 3\n" +
                "4 2 6 8 5 3 7 9 1\n" +
                "7 0 3 9 2 4 8 5 6\n" +
                "9 6 1 5 3 7 2 8 4\n" +
                "2 8 7 4 1 9 6 3 5\n" +
                "3 4 5 2 8 6 1 7 9";

        String expectedSolution = "5 3 4 6 7 8 9 1 2\n" +
                "6 7 2 1 9 5 3 4 8\n" +
                "1 9 8 3 4 2 5 6 7\n" +
                "8 5 9 7 6 1 4 2 3\n" +
                "4 2 6 8 5 3 7 9 1\n" +
                "7 1 3 9 2 4 8 5 6\n" +
                "9 6 1 5 3 7 2 8 4\n" +
                "2 8 7 4 1 9 6 3 5\n" +
                "3 4 5 2 8 6 1 7 9";

        Sudoku sudoku = new Sudoku(puzzle);

        long timer = System.currentTimeMillis();
        int count = sudoku.solve();
        timer = System.currentTimeMillis() - timer;

        assertEquals(1, count);

        assertFalse(sudoku.getSolutionText().isEmpty());
        assertEquals(expectedSolution, sudoku.getSolutionText());

        assertTrue(sudoku.getElapsed() >= 0);
        assertTrue(sudoku.getElapsed() <= timer);

        assertEquals(puzzle, sudoku.toString());
    }

    public void testImpossibleGrid3x3() {
        // row 0 col 0 needs 9, but col 0 already has 9 in row 1
        // → 0 possible values → sorted first → solver returns 0 instantly
        String puzzle = "3 1 2 0 0 0 0 0 0\n" +
                "5 6 4 0 0 0 0 0 0\n" +
                "7 8 0 0 0 9 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0";

        Sudoku sudoku = new Sudoku(puzzle);

        long timer = System.currentTimeMillis();
        int count = sudoku.solve();
        timer = System.currentTimeMillis() - timer;

        assertEquals(0, count);
        assertTrue(sudoku.getSolutionText().isEmpty());
        assertTrue(sudoku.getElapsed() >= 0);
        assertTrue(sudoku.getElapsed() <= timer);
        assertEquals(puzzle, sudoku.toString());
    }

    public void testImpossibleGridRow() {
        // row 0 col 0 needs 9, but col 0 already has 9 in row 1
        // → 0 possible values → sorted first → solver returns 0 instantly
        String puzzle = "0 0 0 0 0 0 0 0 0\n" +
                "1 2 3 4 5 6 0 8 9\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 7 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0";

        Sudoku sudoku = new Sudoku(puzzle);

        long timer = System.currentTimeMillis();
        int count = sudoku.solve();
        timer = System.currentTimeMillis() - timer;

        assertEquals(0, count);
        assertTrue(sudoku.getSolutionText().isEmpty());
        assertTrue(sudoku.getElapsed() >= 0);
        assertTrue(sudoku.getElapsed() <= timer);
        assertEquals(puzzle, sudoku.toString());
    }

    public void testImpossibleGridCol() {
        // row 0 col 0 needs 9, but col 0 already has 9 in row 1
        // → 0 possible values → sorted first → solver returns 0 instantly
        String puzzle = "0 0 1 0 0 0 0 0 0\n" +
                "0 0 2 0 0 0 0 0 0\n" +
                "0 0 3 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 5 0 0 0 0 0 0\n" +
                "4 0 6 0 0 0 0 0 0\n" +
                "0 0 7 0 0 0 0 0 0\n" +
                "0 0 8 0 0 0 0 0 0\n" +
                "0 0 9 0 0 0 0 0 0";

        Sudoku sudoku = new Sudoku(puzzle);

        long timer = System.currentTimeMillis();
        int count = sudoku.solve();
        timer = System.currentTimeMillis() - timer;

        assertEquals(0, count);
        assertTrue(sudoku.getSolutionText().isEmpty());
        assertTrue(sudoku.getElapsed() >= 0);
        assertTrue(sudoku.getElapsed() <= timer);
        assertEquals(puzzle, sudoku.toString());
    }



    public void testEmptyGrid() {
        String puzzle = "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0";

        Sudoku sudoku = new Sudoku(puzzle);

        long timer = System.currentTimeMillis();
        int count = sudoku.solve();
        timer = System.currentTimeMillis() - timer;

        assertEquals(100, count);
        assertFalse(sudoku.getSolutionText().isEmpty());
        assertTrue(sudoku.getElapsed() >= 0);
        assertTrue(sudoku.getElapsed() <= timer);
        assertEquals(puzzle, sudoku.toString());
    }

    public void testManySolutionGrid() {
        String puzzle = "3 0 0 0 0 0 0 8 0\n" +
                "0 0 1 0 9 3 0 0 0\n" +
                "0 4 0 7 8 0 0 0 3\n" +
                "0 9 3 8 0 0 0 1 2\n" +
                "0 0 0 0 4 0 0 0 0\n" +
                "5 2 0 0 0 6 7 9 0\n" +
                "6 0 0 0 2 1 0 4 0\n" +
                "0 0 0 5 3 0 9 0 0\n" +
                "0 3 0 0 0 0 0 5 1";

        Sudoku sudoku = new Sudoku(puzzle);

        long timer = System.currentTimeMillis();
        int count = sudoku.solve();
        timer = System.currentTimeMillis() - timer;

        assertEquals(6, count);
        assertFalse(sudoku.getSolutionText().isEmpty());
        assertTrue(sudoku.getElapsed() >= 0);
        assertTrue(sudoku.getElapsed() <= timer);
        assertEquals(puzzle, sudoku.toString());
    }



}