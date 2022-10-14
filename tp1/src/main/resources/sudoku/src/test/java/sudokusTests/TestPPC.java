package sudokusTests;

import org.junit.Test;

import sudokus.SudokuPPC;

public class TestPPC {

	@Test
	public void sudokuPPC() {
		SudokuPPC test = new SudokuPPC();
		test.solve();
	}
}
