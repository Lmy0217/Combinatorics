/**
 *   Block.java
 *   Copyright (C) 2017 Nanchang University, JiangXi, China
 */

package cn.myluo.combinatorics.homework1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class of Sudoku based structure, including square of girds and some
 * information structure.
 * 
 * @version 1710
 * @author Mingyuan Luo
 */
public class Block {

    /**
     * The list of square of girds, in which indexes are increased from left to
     * right and top to bottom by the square.
     * 
     * @see cn.myluo.combinatorics.homework1.Grid
     */
    private List<Grid> m_GridList;

    /**
     * The list of the constraint values of each column and each row in square of
     * girds, constrained by other blocks. The size of this list is the number of
     * column and row in square. The indexes of this list are increased from left
     * column to right column and next from top row to bottom row.
     */
    private List<Map<Integer, Object>> m_ConstraintList;

    /**
     * The map of the constraint values of this block, including the written values
     * in this block.
     */
    private Map<Integer, Object> m_ConstraintMap;

    /**
     * The number of girds which has been written in this block.
     * 
     * @see cn.myluo.combinatorics.homework1.Grid#m_Value
     */
    private int m_Count;

    /**
     * The storage of Suduku size value.
     * 
     * @see cn.myluo.combinatorics.homework1.Matrix#m_N
     */
    private int m_N;

    /**
     * The index of this block in Sudoku.
     * 
     * @see cn.myluo.combinatorics.homework1.Matrix#m_BlockList
     */
    private int m_Index;

    /**
     * No parameter construction method for initialize some lists.
     */
    private Block() {

        m_GridList = new ArrayList<Grid>();
        m_ConstraintList = new ArrayList<Map<Integer, Object>>();
        m_ConstraintMap = new HashMap<Integer, Object>();
    }

    /**
     * Construction method calling the no parameter construction method
     * {@linkplain cn.myluo.combinatorics.homework1.Block#Block() Block()} and the
     * initialization method
     * {@linkplain cn.myluo.combinatorics.homework1.Block#init() init()}.
     * 
     * @param n
     *            the Suduku size
     * @param index
     *            the index of this block in Sudoku
     * @see cn.myluo.combinatorics.homework1.Matrix#m_N
     */
    public Block(int n, int index) {

        this();
        m_N = n;
        m_Index = index;
        init();
    }

    /**
     * Initializes the square girds list
     * {@linkplain cn.myluo.combinatorics.homework1.Block#m_GridList m_GridList} and
     * the constraint values list
     * {@linkplain cn.myluo.combinatorics.homework1.Block#m_ConstraintList
     * m_ConstraintList}.
     */
    private void init() {

        for (int i = 0; i < m_N * m_N; i++) {
            m_GridList.add(new Grid(m_N));
        }
        for (int i = 0; i < 2 * m_N; i++) {
            m_ConstraintList.add(new HashMap<Integer, Object>());
        }
    }

    /**
     * Gets the number of girds which has been written in this block.
     * 
     * @return the number of girds which has been written in this block
     * @see cn.myluo.combinatorics.homework1.Block#m_Count
     */
    public int getCount() {

        return m_Count;
    }

    /**
     * Constraints the grid indexed by the given index and the constraint values
     * list {@linkplain cn.myluo.combinatorics.homework1.Block#m_ConstraintList
     * m_ConstraintList} with the given constraint value.
     * 
     * @param index
     *            the given index of grid in the square girds list
     *            {@linkplain cn.myluo.combinatorics.homework1.Block#m_GridList
     *            m_GridList}
     * @param value
     *            the given constraint value
     * @return the indexes list of grids whether to enter the run possible list in
     *         this block, the indexes are same as the run possible list
     *         {@linkplain cn.myluo.combinatorics.homework1.Matrix#m_Possible
     *         m_Possible} indexes of grids
     * @see cn.myluo.combinatorics.homework1.Matrix#m_Possible
     */
    public List<Integer> limit(int index, int value) {

        List<Integer> next = new ArrayList<Integer>();

        // the constraint values list has been constrained
        if (m_ConstraintList.get(index).get(value) != null)
            return next;

        // constraint the constraint values list
        m_ConstraintList.get(index).put(value, value);

        // the constraint values map has been constrained
        if (m_ConstraintMap.get(value) != null)
            return next;

        // constraint the other grids in this block
        for (int i = ((index < m_N) ? index : ((index - m_N) * m_N)); i < ((index < m_N) ? (index + m_N * m_N)
                : ((index - m_N) * m_N + m_N)); i += ((index < m_N) ? m_N : 1)) {
            if (m_GridList.get(i).limit(value))
                next.add(i + m_Index * m_N * m_N);
        }

        return next;
    }

    /**
     * Expands the grid indexed by the given index and the constraint values list
     * {@linkplain cn.myluo.combinatorics.homework1.Block#m_ConstraintList
     * m_ConstraintList} with the given expand value.
     * 
     * @param index
     *            the given index of grid in the square girds list
     *            {@linkplain cn.myluo.combinatorics.homework1.Block#m_GridList
     *            m_GridList}
     * @param value
     *            the given expand value
     */
    public void expand(int index, int value) {

        // expand the constraint values list
        if (m_ConstraintList.get(index).get(value) == null)
            return;

        // expand the constraint values list
        m_ConstraintList.get(index).remove(value);

        // the constraint values map has not been constrained
        if (m_ConstraintMap.get(value) != null)
            return;

        // expand the other grids in this block
        for (int i = ((index < m_N) ? index : ((index - m_N) * m_N)); i < ((index < m_N) ? (index + m_N * m_N)
                : ((index - m_N) * m_N + m_N)); i += ((index < m_N) ? m_N : 1)) {
            if (m_ConstraintList.get((index < m_N) ? (i / m_N + m_N) : (i % m_N)).get(value) == null)
                m_GridList.get(i).expand(value);
        }
    }

    /**
     * Writes the value to the grid indexed by the given index with the given value
     * or random value.
     * 
     * @param index
     *            the given index of grid in the square girds list
     *            {@linkplain cn.myluo.combinatorics.homework1.Block#m_GridList
     *            m_GridList}
     * @param rand
     *            the given random number to choose written value if parameter
     *            isValue is false, the given written value if parameter isValue is
     *            true
     * @param isValue
     *            the flag of parameter rand is or not the given written value
     * @return the indexes list of grids whether to enter the run possible list in
     *         this block, the indexes are same as the run possible list
     *         {@linkplain cn.myluo.combinatorics.homework1.Matrix#m_Possible
     *         m_Possible} indexes of grids
     * @see cn.myluo.combinatorics.homework1.Matrix#m_Possible
     */
    public List<Integer> choose(int index, int rand, boolean isValue) {

        List<Integer> next = new ArrayList<Integer>();

        int value = m_GridList.get(index).choose(rand, isValue);
        next.add(value);
        // write failed
        if (value == -1)
            return next;

        // constraint the constraint values map
        m_ConstraintMap.put(isValue ? rand : value, isValue ? rand : value);

        // constraint the other grids in this block
        for (int i = 0; i < m_N * m_N; i++) {
            if (i == index)
                continue;
            if (m_GridList.get(i).limit(isValue ? rand : value))
                next.add(i + m_Index * m_N * m_N);
        }

        // update the count
        m_Count++;

        return next;
    }

    /**
     * Cancels the written value from the grid indexed by the given index with
     * different operations.
     * 
     * @param index
     *            the given index of grid in the square girds list
     *            {@linkplain cn.myluo.combinatorics.homework1.Block#m_GridList
     *            m_GridList}
     * @param isStorage
     *            the flag of different operations, see
     *            {@linkplain cn.myluo.combinatorics.homework1.Grid#cancel(boolean)
     *            Grid.cancel(boolean)}
     * @return the written value from the grid indexed by the given index
     */
    public int cancel(int index, boolean isStorage) {

        // cancel the written value
        int value = m_GridList.get(index).cancel(isStorage);

        // expand the constraint values list
        m_ConstraintMap.remove(value);

        // expand the other grids in this block
        for (int i = 0; i < m_N * m_N; i++) {
            if (i == index || m_ConstraintList.get(i % m_N).get(value) != null
                    || m_ConstraintList.get(i / m_N + m_N).get(value) != null)
                continue;
            m_GridList.get(i).expand(value);
        }

        // update the count
        m_Count--;

        return value;
    }

    /**
     * Restore the written value in the grid indexed by the given index.
     * 
     * @param index
     *            the given index of grid in the square girds list
     *            {@linkplain cn.myluo.combinatorics.homework1.Block#m_GridList
     *            m_GridList}
     * @see cn.myluo.combinatorics.homework1.Grid#restore()
     */
    public void restore(int index) {

        m_GridList.get(index).restore();
    }

    /**
     * Returns the string consists the number of the possible written values in each
     * grid in the row by the given index and appropriate interval.
     * 
     * @param index
     *            the given index of row increased from top (value 0) to bottom
     * @return the string consists the number of the possible written values in each
     *         grid in the row by the given index and appropriate interval
     */
    public String getConstraintRow(int index) {

        StringBuilder sb = new StringBuilder();

        // the largest string length
        int width = 1 + ("" + (m_N * m_N)).length();
        for (int i = index * m_N; i < (1 + index) * m_N; i++) {
            // get the number of the possible written values
            String count = "" + m_GridList.get(i).getConstraintCount();
            // blank fill
            for (int j = 0; j < width - count.length(); j++) {
                sb.append(" ");
            }
            sb.append(count);
        }

        return sb.toString();
    }

    /**
     * Returns the string consists the description of each grid in the row by the
     * given index and appropriate interval.
     * 
     * @param index
     *            the given index of row increased from top (value 0) to bottom
     * @return the string consists the description of each grid in the row by the
     *         given index and appropriate interval
     */
    public String getRow(int index) {

        StringBuilder sb = new StringBuilder();

        // the largest string length
        int width = 1 + ("" + (m_N * m_N)).length();
        for (int i = index * m_N; i < (1 + index) * m_N; i++) {
            // get the description of the grid
            String description = m_GridList.get(i).toString();
            // blank fill
            for (int j = 0; j < width - description.length(); j++) {
                sb.append(" ");
            }
            sb.append(description);
        }

        return sb.toString();
    }

    /**
     * Returns the integer array consists the value of each grid in the row by the
     * given index.
     * 
     * @param index
     *            the given index of row increased from top (value 0) to bottom
     * @return the integer array consists the value of each grid in the row by the
     *         given index
     */
    public int[] getRowArray(int index) {

        int[] array = new int[m_N];
        for (int i = index * m_N; i < (1 + index) * m_N; i++) {
            // get the value from grid
            array[i - index * m_N] = m_GridList.get(i).getValue();
        }

        return array;
    }

    /**
     * Returns a description of this block.
     * 
     * @return the string of values from grids in each row
     */
    public String toString() {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m_N; i++) {
            // get values from grids in each row
            sb.append(getRow(i) + "\n");
        }

        return sb.toString();
    }

}
