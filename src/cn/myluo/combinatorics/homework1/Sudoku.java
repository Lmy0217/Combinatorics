/**
 *   Sudoku.java
 *   Copyright (C) 2017 Mingyuan Luo
 */

package cn.myluo.combinatorics.homework1;

import java.util.Calendar;

/**
 * Class of Sudoku methods testing.
 *
 * @see cn.myluo.combinatorics.homework1.Matrix
 * @version 1710
 * @author Mingyuan Luo
 */
public class Sudoku {

    /**
     * The flag of testing. Output the log to the console if the value is true, not
     * otherwise.
     */
    static final boolean m_isTest = false;

    /**
     * The Sudoku puzzles including two homework's Sudoku matrixes. If you want to
     * test the other Sudoku matrixes, you could add in.
     *
     * @see cn.myluo.combinatorics.homework1.Matrix#Matrix(int[])
     */
    private static final int[][] m_Puzzles = {
            // homework's puzzle 1
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
            // homework's puzzle 2
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

    /**
     * Tests method of Sudoku.
     * @param args ignore
     */
    public static void main(String[] args) {

        /**
         * Randomly create puzzle and solve
         * Note: perhaps take a long time.
         */
        System.out.println("Perhaps take a long time...");
        long startTime = Calendar.getInstance().getTimeInMillis();

        // create a empty matrix
        Matrix matrix = new Matrix(3);
        // randomly find a final solution
        matrix.increase();
        // create a random puzzle which has unique solution by digging holes with rank 0.6
        matrix.reduce(0.6);
        // output the random puzzle
        System.out.println("Randomly create puzzle:");
        System.out.print(matrix);

        // solve the random puzzle
        matrix.increase();
        // output the solution
        System.out.println("Solution:");
        System.out.print(matrix);

        long stopTime = Calendar.getInstance().getTimeInMillis();
        System.out.println(
                "Spent " + (stopTime - startTime) + " millisecond" + ((stopTime - startTime) > 1 ? "s" : "") + "\n\n");

        /**
         * Solution of homework's puzzle 1
         * Note: perhaps take a long time.
         */
        System.out.println("Perhaps take a long time...");
        startTime = Calendar.getInstance().getTimeInMillis();

        // create a matrix with homework's puzzle 1
        matrix = new Matrix(m_Puzzles[0]);

        // solve the puzzle
        matrix.increase();

        // output the solution
        System.out.println("Solution of homework's puzzle 1:");
        System.out.print(matrix);

        stopTime = Calendar.getInstance().getTimeInMillis();
        System.out.println(
                "Spent " + (stopTime - startTime) + " millisecond" + ((stopTime - startTime) > 1 ? "s" : "") + "\n\n");

        /**
         * Solution of homework's puzzle 2
         */
        startTime = Calendar.getInstance().getTimeInMillis();

        // create a matrix with homework's puzzle 2
        matrix = new Matrix(m_Puzzles[1]);

        // solve the puzzle
        matrix.increase();

        // output the solution
        System.out.println("Solution of homework's puzzle 2:");
        System.out.print(matrix);

        stopTime = Calendar.getInstance().getTimeInMillis();
        System.out.println(
                "Spent " + (stopTime - startTime) + " millisecond" + ((stopTime - startTime) > 1 ? "s" : "") + "\n\n");
    }

}
