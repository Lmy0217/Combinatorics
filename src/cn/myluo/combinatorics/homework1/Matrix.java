/**
 *   Matrix.java
 *   Copyright (C) 2017 Mingyuan Luo
 */

package cn.myluo.combinatorics.homework1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Class of Sudoku matrix, including square of blocks and some information
 * structure.
 * 
 * @version 1710
 * @author Mingyuan Luo
 */
public class Matrix {

    /**
     * The list of square of blocks, in which indexes are increased from left to
     * right and top to bottom by the square.
     * 
     * @see cn.myluo.combinatorics.homework1.Block
     */
    private List<Block> m_BlockList;

    /**
     * The indexes list of grids which record written grids in turn. The indexes of
     * grids in this list are same as the run possible list
     * {@linkplain cn.myluo.combinatorics.homework1.Matrix#m_Possible m_Possible}
     * indexes.
     */
    private List<Integer> m_Record;

    /**
     * The indexes list of grids which save the run possible grids in turn. The
     * indexes of grids in this list is {@code k * s + i}, where {@code k} is the
     * index of block in which the grid is in the square blocks list
     * {@linkplain cn.myluo.combinatorics.homework1.Matrix#m_BlockList m_BlockList},
     * {@code s} is the number of girds in one block, {@code i} is the index of grid
     * in the square grids list
     * {@linkplain cn.myluo.combinatorics.homework1.Block#m_GridList m_GridList} in
     * block in which the grid is.
     */
    private List<Integer> m_Possible;

    /**
     * The indexes map of grids based on the run possible list
     * {@linkplain cn.myluo.combinatorics.homework1.Matrix#m_Possible m_Possible},
     * easy to search.
     */
    private Map<Integer, Integer> m_PossibleMap;

    /**
     * The indexes list of grids which is not written. The indexes of grids in this
     * list are same as the run possible list
     * {@linkplain cn.myluo.combinatorics.homework1.Matrix#m_Possible m_Possible}
     * indexes.
     */
    private List<Integer> m_RandList;

    /**
     * The indexes list of grids based on the not written list
     * {@linkplain cn.myluo.combinatorics.homework1.Matrix#m_RandList m_RandList},
     * easy to delete.
     */
    private List<Integer> m_RandIndex;

    /**
     * The Suduku matrix size which is the number of blocks in each row and is equal
     * to the number of grids in each row in one block. In other words, a matrix has
     * {@code m_N * m_N} blocks and a block has {@code m_N * m_N} grids.
     */
    private int m_N;

    /**
     * The number of girds which has been written.
     * 
     * @see cn.myluo.combinatorics.homework1.Grid#m_Value
     */
    private int m_Count;

    /**
     * The random generator used to generate random numbers.
     */
    private Random m_Random;

    /**
     * No parameter construction method for initialize some lists.
     */
    private Matrix() {

        m_BlockList = new ArrayList<Block>();
        m_Record = new ArrayList<Integer>();
        m_Possible = new ArrayList<Integer>();
        m_PossibleMap = new HashMap<Integer, Integer>();
        m_RandList = new ArrayList<Integer>();
        m_RandIndex = new ArrayList<Integer>();
        m_Random = new Random(System.currentTimeMillis());
    }

    /**
     * Construction method calling the no parameter construction method
     * {@linkplain cn.myluo.combinatorics.homework1.Matrix#Matrix() Matrix()} and
     * the initialization method
     * {@linkplain cn.myluo.combinatorics.homework1.Matrix#init() init()}.
     * 
     * @param n
     *            the Suduku size
     * @see cn.myluo.combinatorics.homework1.Matrix#m_N
     */
    public Matrix(int n) {

        this();
        // determine the legitimacy
        if (n < 1)
            System.out.println("n must be positive!");
        m_N = n;
        init();
    }

    /**
     * Construction method to create a Sudoku matrix with Sudoku puzzle.
     * 
     * @param puzzle
     *            the integer array including the Sudoku puzzle values in each grid
     *            from left to right and top to bottom in turn, and the value 0 in
     *            puzzle if this grid is not written specially
     */
    public Matrix(int[] puzzle) {

        this();
        // puzzle has error such as numerical contradiction or length error
        if (!init(puzzle))
            System.out.println("Puzzle error!");
    }

    /**
     * Initializes the square blocks list
     * {@linkplain cn.myluo.combinatorics.homework1.Matrix#m_BlockList m_BlockList}
     * and makes the not written list
     * {@linkplain cn.myluo.combinatorics.homework1.Matrix#m_RandList m_RandList}
     * including all indexes of grids.
     */
    private void init() {

        for (int i = 0; i < m_N * m_N; i++) {
            m_BlockList.add(new Block(m_N, i));

            for (int j = 0; j < m_N * m_N; j++) {
                m_RandList.add(i * m_N * m_N + j);
                m_RandIndex.add(i * m_N * m_N + j);
            }
        }
    }

    /**
     * Initialize method calling the no parameter initialize method
     * {@linkplain cn.myluo.combinatorics.homework1.Matrix#init() init()} and write
     * grids values with the given Sudoku puzzle.
     * 
     * @param puzzle
     *            the integer array including the Sudoku puzzle values in each grid
     *            from left to right and top to bottom in turn, and the value 0 in
     *            puzzle if this grid is not written specially
     * @return the value true if the given puzzle has error, the value false
     *         otherwise
     */
    private boolean init(int[] puzzle) {

        // determine the legitimacy
        if (puzzle == null)
            return false;
        double dn = Math.sqrt(Math.sqrt((double) puzzle.length));
        if (Math.floor(dn) != dn)
            return false;

        m_N = (new Double(dn)).intValue();

        // calling no parameter initialize method
        init();

        // writing grids values
        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i] < 1 || puzzle[i] > m_N * m_N)
                continue;
            int row = i / (m_N * m_N);
            int col = i % (m_N * m_N);
            int blockIndex = row / m_N * m_N + col / m_N;
            int gridIndex = row % m_N * m_N + col % m_N;
            int index = blockIndex * m_N * m_N + gridIndex;
            if (!choose(index, puzzle[i], true))
                return false;
        }

        return true;
    }

    /**
     * Finds one final solution with empty or puzzle matrix and write random if the
     * solution is not only one. The method is based on write (calling
     * {@linkplain cn.myluo.combinatorics.homework1.Matrix#choose(int, int, boolean)
     * choose(int, int, boolean)}) in turn or random and go back if failed.
     * 
     * @return the value true if this matrix has the final solution, the value false
     *         otherwise
     * @see cn.myluo.combinatorics.homework1.Matrix#choose(int, int, boolean)
     */
    public boolean increase() {

        // until filled
        while (m_Count != m_N * m_N) {

            // written grid in turn if the run possible list is not empty, random written
            // otherwise
            if (m_Possible.size() != 0) {
                if (!choose(m_Possible.get(0), m_Random.nextInt(m_N * m_N), false)) {
                    // written failed, go back
                    if (m_Record.size() == 0)
                        return false;
                    int value = m_Record.get(m_Record.size() - 1);
                    m_Possible.add(0, value);
                    m_PossibleMap.put(value, value);
                    cancel(m_Possible.get(0), false);
                } else {
                    m_PossibleMap.remove(m_Possible.get(0));
                    m_Possible.remove(0);
                }
            } else {
                // random written
                while (!choose(m_RandList.get(m_Random.nextInt(m_RandList.size())), m_Random.nextInt(m_N * m_N), false))
                    ;
            }

            // testing logging
            if (Sudoku.m_isTest)
                System.out.println("Choose count: " + getChooseCount());
        }
        return true;
    }

    /**
     * Creates a puzzle from final solution with the given rank. The method is based
     * on random dig holes in turn.
     * 
     * @param rank
     *            the value of puzzle difficulty between 0.0 and 1.0
     * @return the integer array including the values at the created puzzle in turn
     * @see cn.myluo.combinatorics.homework1.Matrix#increase()
     * @see cn.myluo.combinatorics.homework1.Matrix#toArray()
     */
    public int[] reduce(double rank) {

        // determine the legitimacy
        if (rank <= 0 || rank >= 1)
            System.out.println("rank must between 0.0 and 1.0!");

        List<Integer> randList = randList(m_N * m_N * m_N * m_N);
        for (int i = 0; i < m_N * m_N * m_N * m_N; i++) {

            // testing logging
            if (Sudoku.m_isTest)
                System.out.println("    reduce" + i);

            int randIndex = randList.get(i);

            // random dig holes with rank
            if (m_Random.nextDouble() >= rank)
                continue;

            // clear some lists
            m_Record.clear();
            m_Possible.clear();
            m_PossibleMap.clear();

            // computer index
            int row = randIndex / (m_N * m_N);
            int col = randIndex % (m_N * m_N);
            int blockIndex = row / m_N * m_N + col / m_N;
            int gridIndex = row % m_N * m_N + col % m_N;
            int index = blockIndex * m_N * m_N + gridIndex;

            // cancel written value and save value to storage
            m_Record.add(index);
            int value = cancel(index, true);

            // check if has other solution
            if (choose(index, m_Random.nextInt(m_N * m_N), false) && increase()) {
                // go back
                for (int j = m_Record.size() - 1; j > 0; j--) {
                    int recordValue = m_Record.get(j);
                    cancel(recordValue, true);
                    m_BlockList.get(recordValue / (m_N * m_N)).restore(recordValue % (m_N * m_N));
                }
                cancel(m_Record.get(0), false);
                // restore value from storage
                m_BlockList.get(blockIndex).restore(gridIndex);
                // written current value
                choose(index, value, true);
            } else {
                // restore value from storage
                m_BlockList.get(blockIndex).restore(gridIndex);
            }
        }

        return toArray();
    }

    /**
     * Writes the value to the grid indexed by the given index with the given value
     * or random value.
     * 
     * @param index
     *            the given index of grid and it is same as the run possible list
     *            {@linkplain cn.myluo.combinatorics.homework1.Matrix#m_Possible
     *            m_Possible} indexes of grids
     * @param rand
     *            the given random number to choose written value if parameter
     *            isValue is false, the given written value if parameter isValue is
     *            true
     * @param isValue
     *            the flag of parameter rand is or not the given written value
     * @return the value true if written success, the value false otherwise
     */
    private boolean choose(int index, int rand, boolean isValue) {

        int blockIndex = index / (m_N * m_N);
        int gridIndex = index % (m_N * m_N);

        // write the value and get the possible list including written return
        List<Integer> next = m_BlockList.get(blockIndex).choose(gridIndex, rand, isValue);

        // written failed
        int value = next.get(0);
        if (value == -1)
            return false;

        // add to possible list with uniqueness
        next.remove(0);
        if (isValue && m_PossibleMap.get(index) != null) {
            m_PossibleMap.remove(index);
            m_Possible.remove((Object) index);
        }
        for (int i = 0; i < next.size(); i++) {
            int possibleValue = next.get(i);
            if (m_PossibleMap.get(possibleValue) == null) {
                m_Possible.add(possibleValue);
                m_PossibleMap.put(possibleValue, possibleValue);
            }
        }

        // add to record list
        m_Record.add(index);

        // cross delete with not written lists
        int rankIndex = m_RandIndex.get(index);
        m_RandList.set(rankIndex, m_RandList.get(m_RandList.size() - 1));
        m_RandIndex.set(m_RandList.get(rankIndex), rankIndex);
        m_RandIndex.set(index, -1);
        m_RandList.remove(m_RandList.size() - 1);

        // update count
        if (m_BlockList.get(blockIndex).getCount() == m_N * m_N)
            m_Count++;

        // constraint the other blocks
        for (int i = blockIndex % m_N; i < blockIndex % m_N + m_N * m_N; i += m_N) {
            if (i == blockIndex)
                continue;
            List<Integer> possibleList = m_BlockList.get(i).limit(gridIndex % m_N, isValue ? rand : value);
            for (int j = 0; j < possibleList.size(); j++) {
                int possibleValue = possibleList.get(j);
                if (m_PossibleMap.get(possibleValue) == null) {
                    m_Possible.add(possibleValue);
                    m_PossibleMap.put(possibleValue, possibleValue);
                }
            }
        }
        for (int i = blockIndex / m_N * m_N; i < blockIndex / m_N * m_N + m_N; i++) {
            if (i == blockIndex)
                continue;
            List<Integer> possibleList = m_BlockList.get(i).limit(gridIndex / m_N + m_N, isValue ? rand : value);
            for (int j = 0; j < possibleList.size(); j++) {
                int possibleValue = possibleList.get(j);
                if (m_PossibleMap.get(possibleValue) == null) {
                    m_Possible.add(possibleValue);
                    m_PossibleMap.put(possibleValue, possibleValue);
                }
            }
        }

        return true;
    }

    /**
     * Cancels the written value from the grid indexed by the given index with
     * different operations.
     * 
     * @param index
     *            the given index of grid and it is same as the run possible list
     *            {@linkplain cn.myluo.combinatorics.homework1.Matrix#m_Possible
     *            m_Possible} indexes of grids
     * @param isStorage
     *            the flag of different operations, see
     *            {@linkplain cn.myluo.combinatorics.homework1.Grid#cancel(boolean)
     *            Grid.cancel(boolean)}
     * @return the written value from the grid indexed by the given index
     */
    private int cancel(int index, boolean isStorage) {

        int blockIndex = index / (m_N * m_N);
        int gridIndex = index % (m_N * m_N);

        // cancel the written value and get the value with different operations
        int value = m_BlockList.get(blockIndex).cancel(gridIndex, isStorage);

        // cross add with not written lists
        m_RandList.add(index);
        m_RandIndex.set(index, m_RandList.size() - 1);

        // delete from record list
        m_Record.remove(m_Record.size() - 1);

        // expand the other blocks
        if (m_BlockList.get(blockIndex).getCount() == m_N * m_N - 1)
            m_Count--;
        for (int i = blockIndex % m_N; i < blockIndex % m_N + m_N * m_N; i += m_N) {
            if (i == blockIndex)
                continue;
            m_BlockList.get(i).expand(gridIndex % m_N, value);
        }
        for (int i = blockIndex / m_N * m_N; i < blockIndex / m_N * m_N + m_N; i++) {
            if (i == blockIndex)
                continue;
            m_BlockList.get(i).expand(gridIndex / m_N + m_N, value);
        }

        return value;
    }

    /**
     * Gets a random integer list with the given length including {@code 1} to
     * {@code length - 1}.
     * 
     * @param length
     *            the given length value
     * @return a random integer list with the given length including {@code 1} to
     *         {@code length - 1}
     */
    private List<Integer> randList(int length) {

        List<Integer> randList = new ArrayList<Integer>();
        for (int i = 0; i < length; i++) {
            randList.add(i);
        }

        // randomly permute
        Collections.shuffle(randList);

        return randList;
    }

    /**
     * Gets the number of girds which has been written.
     * 
     * @return the number of girds which has been written
     * @see cn.myluo.combinatorics.homework1.Block#m_Count
     */
    private int getChooseCount() {

        int count = 0;
        for (int i = 0; i < m_BlockList.size(); i++) {
            count += m_BlockList.get(i).getCount();
        }

        return count;
    }

    /**
     * Returns the integer array consists the value of each grid.
     * 
     * @return the integer array consists the value of each grid
     * @see cn.myluo.combinatorics.homework1.Block#getRowArray(int)
     */
    public int[] toArray() {

        int[] array = new int[m_N * m_N * m_N * m_N];
        for (int row = 0; row < m_N * m_N; row++) {
            for (int col = 0; col < m_N; col++) {
                System.arraycopy(m_BlockList.get(row / m_N * m_N + col).getRowArray(row % m_N), 0, array,
                        row * m_N * m_N + col * m_N, m_N);
            }
        }

        return array;
    }

    /**
     * Returns a description of this Sudoku matrix.
     * 
     * @return the string of values from grids in this matrix
     * @see cn.myluo.combinatorics.homework1.Block#getRow(int)
     */
    public String toString() {

        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < m_N * m_N; row++) {
            for (int col = 0; col < m_N; col++) {
                sb.append(m_BlockList.get(row / m_N * m_N + col).getRow(row % m_N));
            }

            // testing logging
            if (Sudoku.m_isTest) {
                sb.append("  ");
                for (int col = 0; col < m_N; col++) {
                    sb.append(m_BlockList.get(row / m_N * m_N + col).getConstraintRow(row % m_N));
                }
            }

            // line feed
            sb.append("\n");
        }

        return sb.toString();
    }

}
