package cn.myluo.combinatorics.homework1;

import java.util.Calendar;

public class Sudoku {
	
	public static final boolean isTest = false;
	
	private static final int[][] puzzles = {
	{
		8, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 3, 6, 0, 0, 0, 0, 0,
	    0, 7, 0, 0, 9, 0, 2, 0, 0,
	    0, 5, 0, 0, 0, 7, 0, 0, 0,
	    0, 0, 0, 0, 4, 5, 7, 0, 0,
	    0, 0, 0, 1, 0, 0, 0, 3, 0,
	    0, 0, 1, 0, 0, 0, 0, 6, 8,
	    0, 0, 8, 5, 0, 0, 0, 1, 0,
	    0, 9, 0, 0, 0, 0, 4, 0, 0
	},
	{
		0, 0, 7, 0, 0, 0, 8, 2, 0,
        0, 9, 0, 0, 0, 1, 0, 0, 0,
        0, 4, 0, 9, 7, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 5, 4, 0, 6,
        0, 0, 3, 0, 0, 0, 7, 0, 0,
        5, 0, 6, 7, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 8, 4, 0, 5, 0,
        0, 0, 0, 6, 0, 0, 0, 1, 0,
        0, 2, 4, 0, 0, 0, 6, 0, 0
    },
	{
    	0, 8, 1, 3, 0, 2, 6, 0, 0,
    	6, 0, 9, 5, 0, 1, 0, 2, 0,
    	2, 3, 0, 0, 0, 0, 0, 0, 0,
    	5, 0, 2, 0, 3, 0, 7, 8, 9,
    	0, 0, 0, 0, 0, 0, 0, 0, 0,
    	4, 6, 3, 0, 8, 0, 2, 0, 1,
    	0, 0, 0, 0, 0, 0, 0, 6, 2,
    	0, 2, 0, 7, 0, 9, 5, 0, 3,
    	0, 0, 6, 8, 0, 3, 9, 4, 0
	}
	};
	
	public static void main(String[] args) {
		
    	long startTime = Calendar.getInstance().getTimeInMillis();
        Matrix matrix = new Matrix(3);
        matrix.increase();
        matrix.reduce(0.6);
        System.out.println(matrix);
        matrix.increase();
        System.out.println(matrix);
        long stopTime = Calendar.getInstance().getTimeInMillis();
        System.out.println(stopTime - startTime);
        
        startTime = Calendar.getInstance().getTimeInMillis();
    	matrix = new Matrix(puzzles[1]);
        matrix.increase();
        System.out.println(matrix);
        stopTime = Calendar.getInstance().getTimeInMillis();
        System.out.println(stopTime - startTime);
        
    	startTime = Calendar.getInstance().getTimeInMillis();
    	matrix = new Matrix(puzzles[2]);
        matrix.increase();
        System.out.println(matrix);
        stopTime = Calendar.getInstance().getTimeInMillis();
        System.out.println(stopTime - startTime);
    }

}
