/**
 *   Grid.java
 *   Copyright (C) 2017 Mingyuan Luo
 */

package cn.myluo.combinatorics.homework1;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of Sudoku basic element(grid), including the written value and some
 * information structure.
 * 
 * @version 1710
 * @author Mingyuan Luo
 */
public class Grid {

    /**
     * The written value, the value 0 if not written or cancel.
     */
    private int m_Value;

    /**
     * The list of possible written values, constrained by other grids or blocks.
     */
    private List<Integer> m_ConstraintList;

    /**
     * The indexes of the possible written values list
     * {@linkplain cn.myluo.combinatorics.homework1.Grid#m_ConstraintList
     * m_ConstraintList}, easy to delete operation.
     * 
     * @see cn.myluo.combinatorics.homework1.Grid#m_ConstraintList
     */
    private List<Integer> m_ConstraintIndex;

    /**
     * The list of next written values, based on possible written values list
     * {@linkplain cn.myluo.combinatorics.homework1.Grid#m_ConstraintList
     * m_ConstraintList} and except previous written values.
     */
    private List<Integer> m_NextList;

    /**
     * The indexes of the next written values list
     * {@linkplain cn.myluo.combinatorics.homework1.Grid#m_NextList m_NextList},
     * easy to delete operation.
     * 
     * @see cn.myluo.combinatorics.homework1.Grid#m_NextList
     */
    private List<Integer> m_NextIndex;

    /**
     * The storage of Suduku size value.
     * 
     * @see cn.myluo.combinatorics.homework1.Matrix#m_N
     */
    private int m_N;

    /**
     * The flag of the written value, the value true if
     * {@linkplain cn.myluo.combinatorics.homework1.Grid#m_Value m_Value} is not 0,
     * the value false otherwise.
     */
    private boolean m_isChoose;

    /**
     * The flag of written, the value true if the written value
     * {@linkplain cn.myluo.combinatorics.homework1.Grid#m_Value m_Value} has been
     * written and the next written values list
     * {@linkplain cn.myluo.combinatorics.homework1.Grid#m_NextList m_NextList} is
     * not empty, the value false otherwise.
     */
    private boolean m_isStart;

    /**
     * The storage of the written value
     * {@linkplain cn.myluo.combinatorics.homework1.Grid#m_Value m_Value} for verify
     * the only solution when digging holes.
     */
    private int m_Storage;

    /**
     * No parameter construction method for initialize some lists.
     */
    private Grid() {

        m_ConstraintList = new ArrayList<Integer>();
        m_ConstraintIndex = new ArrayList<Integer>();
        m_NextList = new ArrayList<Integer>();
        m_NextIndex = new ArrayList<Integer>();
    }

    /**
     * Construction method calling the no parameter construction method
     * {@linkplain cn.myluo.combinatorics.homework1.Grid#Grid() Grid()} and the
     * initialization method
     * {@linkplain cn.myluo.combinatorics.homework1.Grid#init() init()}.
     * 
     * @param n
     *            the Suduku size
     * @see cn.myluo.combinatorics.homework1.Matrix#m_N
     */
    public Grid(int n) {

        this();
        m_N = n;
        init();
    }

    /**
     * Makes the possible written values list including all possible values (1 to
     * the square of the Suduku size.
     * 
     * @see cn.myluo.combinatorics.homework1.Matrix#m_N
     */
    private void init() {

        for (int i = 0; i < m_N * m_N; i++) {
            m_ConstraintList.add(1 + i);
            m_ConstraintIndex.add(i);
        }
    }

    /**
     * Gets the written value.
     * 
     * @return the written value
     * @see cn.myluo.combinatorics.homework1.Grid#m_Value
     */
    public int getValue() {

        return m_Value;
    }

    /**
     * Gets the flag of the written value.
     * 
     * @return the flag of the written value
     * @see cn.myluo.combinatorics.homework1.Grid#m_isChoose
     */
    public boolean isChoose() {

        return m_isChoose;
    }

    /**
     * Gets the flag of written.
     * 
     * @return the flag of written
     * @see cn.myluo.combinatorics.homework1.Grid#m_isStart
     */
    public boolean isStart() {

        return m_isStart;
    }

    /**
     * Gets the number of elements in the possible written values list.
     * 
     * @return the number of elements in the possible written values list
     * @see cn.myluo.combinatorics.homework1.Grid#m_ConstraintList
     */
    public int getConstraintCount() {

        return m_ConstraintList.size();
    }

    /**
     * Gets the number of elements in the next written values list.
     * 
     * @return the number of elements in the next written values list
     * @see cn.myluo.combinatorics.homework1.Grid#m_NextList
     */
    public int getNextCount() {

        return m_NextList.size();
    }

    /**
     * Constraints the possible written values list
     * {@linkplain cn.myluo.combinatorics.homework1.Grid#m_ConstraintList
     * m_ConstraintList} by the given value.
     * 
     * @param value
     *            the given value from all possible values
     * @return the flag of this grid whether to enter the run possible list, the
     *         value true if this grid is not written and the size of the possible
     *         written values list
     *         {@linkplain cn.myluo.combinatorics.homework1.Grid#m_ConstraintList
     *         m_ConstraintList} is smaller than threshold, the value false
     *         otherwise.
     * @see cn.myluo.combinatorics.homework1.Matrix#m_Possible
     */
    public boolean limit(int value) {

        int index = m_ConstraintIndex.get(value - 1);
        // has been constrained
        if (index == -1)
            return false;

        // cross delete with two lists
        m_ConstraintList.set(index, m_ConstraintList.get(m_ConstraintList.size() - 1));
        m_ConstraintIndex.set(m_ConstraintList.get(index) - 1, index);
        m_ConstraintIndex.set(value - 1, -1);
        m_ConstraintList.remove(m_ConstraintList.size() - 1);

        return !m_isChoose && m_ConstraintList.size() <= (new Double(Math.ceil(m_N * m_N / 2.0))).intValue();
    }

    /**
     * Expands the possible written values list
     * {@linkplain cn.myluo.combinatorics.homework1.Grid#m_ConstraintList
     * m_ConstraintList} by the given value.
     * 
     * @param value
     *            the given value from all possible values
     */
    public void expand(int value) {

        if (m_ConstraintIndex.get(value - 1) != -1 || value == m_Storage)
            return;

        // cross add with two lists
        m_ConstraintList.add(value);
        m_ConstraintIndex.set(value - 1, m_ConstraintList.size() - 1);
    }

    /**
     * Writes the value {@linkplain cn.myluo.combinatorics.homework1.Grid#m_Value
     * m_Value} by the given value or random value.
     * 
     * @param rand
     *            the given random number to choose written value if parameter
     *            isValue is false, the given written value if parameter isValue is
     *            true
     * @param isValue
     *            the flag of parameter rand is or not the given written value
     * @return the written value
     */
    public int choose(int rand, boolean isValue) {

        // restrictive conditions
        if ((!isValue && (m_ConstraintList.isEmpty() || (m_isStart && m_NextList.isEmpty())))
                || (isValue && m_ConstraintIndex.get(rand - 1) == -1)) {
            m_isStart = false;
            return -1;
        }

        // reset the next written values list
        if (!m_isStart) {
            m_NextList.clear();
            m_NextList.addAll(m_ConstraintList);
            m_NextIndex.clear();
            m_NextIndex.addAll(m_ConstraintIndex);
            m_isStart = true;
        }

        // choose the written value
        int index = isValue ? m_NextIndex.get(rand - 1) : (rand % m_NextList.size());
        m_Value = isValue ? rand : m_NextList.get(index);

        // cross delete with two lists
        m_NextList.set(index, m_NextList.get(m_NextList.size() - 1));
        m_NextIndex.set(m_NextList.get(index) - 1, index);
        m_NextIndex.set(m_Value - 1, -1);
        m_NextList.remove(m_NextList.size() - 1);
        int constraintIndex = m_ConstraintIndex.get(m_Value - 1);
        m_ConstraintList.set(constraintIndex, m_ConstraintList.get(m_ConstraintList.size() - 1));
        m_ConstraintIndex.set(m_ConstraintList.get(constraintIndex) - 1, constraintIndex);
        m_ConstraintIndex.set(m_Value - 1, -1);
        m_ConstraintList.remove(m_ConstraintList.size() - 1);

        // set the flags
        m_isStart = false;
        m_isChoose = true;

        return m_Value;
    }

    /**
     * Cancels the written value
     * {@linkplain cn.myluo.combinatorics.homework1.Grid#m_Value m_Value} with
     * different operations.
     * 
     * @param isStorage
     *            the value true if cancels with save to the storage
     *            {@linkplain cn.myluo.combinatorics.homework1.Grid#m_Storage
     *            m_Storage} and without add into the possible written values list
     *            {@linkplain cn.myluo.combinatorics.homework1.Grid#m_ConstraintList
     *            m_ConstraintList}, the value false otherwise
     * @return the written value
     */
    public int cancel(boolean isStorage) {

        // different operations
        if (!isStorage) {
            // cross add with two lists
            m_ConstraintList.add(m_Value);
            m_ConstraintIndex.set(m_Value - 1, m_ConstraintList.size() - 1);
        } else {
            m_Storage = m_Value;
        }

        // cancel the written value
        int temp = m_Value;
        m_Value = 0;

        // set the flags
        m_isChoose = false;
        m_isStart = !isStorage;

        return temp;
    }

    /**
     * Restore the written value
     * {@linkplain cn.myluo.combinatorics.homework1.Grid#m_Value m_Value} from the
     * storage {@linkplain cn.myluo.combinatorics.homework1.Grid#m_Storage
     * m_Storage}.
     * 
     * @see cn.myluo.combinatorics.homework1.Grid#cancel(boolean)
     */
    public void restore() {

        // cross add with two lists
        m_ConstraintList.add(m_Storage);
        m_ConstraintIndex.set(m_Storage - 1, m_ConstraintList.size() - 1);

        // set the flags
        m_Storage = 0;
        m_isStart = false;
    }

    /**
     * Returns a description of this grid.
     * 
     * @return the string of written value if it is not 0, the "x" if it is 0.
     */
    @Override
    public String toString() {

        return m_Value == 0 ? "x" : Integer.toString(m_Value);
    }

}
