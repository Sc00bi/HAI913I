package sudokusTests;

import org.junit.Test;

import sudokus.SudokuBT;

public class TestBT {

	@Test
	public void sudokuBT()
	{
		SudokuBT test = new SudokuBT(4);
		test.findSolution(0, 0);
	}
}
