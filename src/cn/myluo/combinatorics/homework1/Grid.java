/**
 *   Grid.java
 *   Copyright (C) 2017 Nanchang University, JiangXi, China
 */

package cn.myluo.combinatorics.homework1;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of Sudoku basic element(grid), including the written value and some information structure.
 * 
 * @author Mingyuan Luo
 * @version 1710
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
     * The indexes of possible written values list {@linkplain cn.myluo.combinatorics.homework1.Grid#m_ConstraintList m_ConstraintList}, easy to delete operation. 
     * @see cn.myluo.combinatorics.homework1.Grid#m_ConstraintList
     */
    private List<Integer> m_ConstraintIndex;
    
    /**
     * The list of next written values, based on possible written values list {@linkplain cn.myluo.combinatorics.homework1.Grid#m_ConstraintList m_ConstraintList} and except previous written values.
     */
    private List<Integer> m_NextList;
    
    /** 
     * The indexes of next written values list {@linkplain cn.myluo.combinatorics.homework1.Grid#m_NextList m_NextList}, easy to delete operation. 
     * @see cn.myluo.combinatorics.homework1.Grid#m_NextList
     */
    private List<Integer> m_NextIndex;
    
    /** 
     * The storage of Suduku size value.
     * @see cn.myluo.combinatorics.homework1.Matrix#m_N
     */
    private int m_N;
    
    /**
     * The flag of the written value, the value true if {@linkplain cn.myluo.combinatorics.homework1.Grid#m_Value m_Value} is not 0, the value false otherwise.
     */
    private boolean m_isChoose;
    
    /**
     * The flag of written, the value true if the written value {@linkplain cn.myluo.combinatorics.homework1.Grid#m_Value m_Value} has been written and the next written values list {@linkplain cn.myluo.combinatorics.homework1.Grid#m_NextList m_NextList} is not empty, the value false otherwise.
     */
    private boolean m_isStart;
    
    /**
     * The storage of the written value {@linkplain cn.myluo.combinatorics.homework1.Grid#m_Value m_Value} for verify the only solution when digging holes.
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
     * Construction method calling the no parameter construction method {@linkplain cn.myluo.combinatorics.homework1.Grid#Grid() Grid()} and the initialization method {@linkplain cn.myluo.combinatorics.homework1.Grid#init() init()}.
     * @param n the storage of Suduku size, see {@link cn.myluo.combinatorics.homework1.Matrix#m_N}
     */
    public Grid(int n) {
        
        this();
        m_N = n;
        init();
    }
    
    /**
     * Making the possible written values list contains all possible values (1 to the square of the Suduku size value, see {@link cn.myluo.combinatorics.homework1.Matrix#m_N}).
     */
    private void init() {
        
        for(int i = 0; i < m_N * m_N; i++) {
            m_ConstraintList.add(1 + i);
            m_ConstraintIndex.add(i);
        }
    }
    
    /**
     * Gets the written value.
     * @return the written value
     * @see cn.myluo.combinatorics.homework1.Grid#m_Value
     */
    public int getValue() {
        
        return m_Value;
    }
    
    /**
     * Gets the flag of the written value.
     * @return the flag of the written value
     * @see cn.myluo.combinatorics.homework1.Grid#m_isChoose
     */
    public boolean isChoose() {
        
        return m_isChoose;
    }
    
    /**
     * Gets the flag of written.
     * @return the flag of written
     * @see cn.myluo.combinatorics.homework1.Grid#m_isStart
     */
    public boolean isStart() {
        
        return m_isStart;
    }
    
    /**
     * Gets the number of elements in the possible written values list.
     * @return the number of elements in the possible written values list
     * @see cn.myluo.combinatorics.homework1.Grid#m_ConstraintList
     */
    public int getConstraintCount() {
        
        return m_ConstraintList.size();
    }
    
    /**
     * Gets the number of elements in the next written values list.
     * @return the number of elements in the next written values list
     * @see cn.myluo.combinatorics.homework1.Grid#m_NextList
     */
    public int getNextCount() {
        
        return m_NextList.size();
    }
    
    /**
     * 
     * @param value
     * @return
     */
    public boolean limit(int value) {
        
        int index = m_ConstraintIndex.get(value - 1);
        if(index == -1) return false;
        m_ConstraintList.set(index, m_ConstraintList.get(m_ConstraintList.size() - 1));
        m_ConstraintIndex.set(m_ConstraintList.get(index) - 1, index);
        m_ConstraintIndex.set(value - 1, -1);
        m_ConstraintList.remove(m_ConstraintList.size() - 1);
        return !m_isChoose && m_ConstraintList.size() <= (new Double(Math.ceil(m_N * m_N / 2.0))).intValue();
    }
    
    public void expand(int value) {
        
        if(m_ConstraintIndex.get(value - 1) != -1 || value == m_Storage) return;
        m_ConstraintList.add(value);
        m_ConstraintIndex.set(value - 1, m_ConstraintList.size() - 1);
    }
    
    public int choose(int rand, boolean isValue) {
        
        if((!isValue && (m_ConstraintList.size() == 0 || (m_isStart && m_NextList.size() == 0))) || (isValue && m_ConstraintIndex.get(rand - 1) == -1)) {
            m_isStart = false;
            return -1;
        }
        if(!m_isStart) {
            m_NextList.clear();
            m_NextList.addAll(m_ConstraintList);
            m_NextIndex.clear();
            m_NextIndex.addAll(m_ConstraintIndex);
            m_isStart = true;
        }
        int index = isValue ? m_NextIndex.get(rand - 1) : (rand % m_NextList.size());
        m_Value = isValue ? rand : m_NextList.get(index);
        m_NextList.set(index, m_NextList.get(m_NextList.size() - 1));
        m_NextIndex.set(m_NextList.get(index) - 1, index);
        m_NextIndex.set(m_Value - 1, -1);
        m_NextList.remove(m_NextList.size() - 1);
        int constraintIndex = m_ConstraintIndex.get(m_Value - 1);
        m_ConstraintList.set(constraintIndex, m_ConstraintList.get(m_ConstraintList.size() - 1));
        m_ConstraintIndex.set(m_ConstraintList.get(constraintIndex) - 1, constraintIndex);
        m_ConstraintIndex.set(m_Value - 1, -1);
        m_ConstraintList.remove(m_ConstraintList.size() - 1);
        m_isStart = false;
        m_isChoose = true;
        return m_Value;
    }
    
    public int cancel(boolean isStorage) {
        
        if(!isStorage) {
            m_ConstraintList.add(m_Value);
            m_ConstraintIndex.set(m_Value - 1, m_ConstraintList.size() - 1);
        } else {
            m_Storage = m_Value;
        }
        int temp = m_Value;
        m_Value = 0;
        m_isChoose = false;
        m_isStart = !isStorage;
        return temp;
    }
    
    public void restore() {
        
        m_ConstraintList.add(m_Storage);
        m_ConstraintIndex.set(m_Storage - 1, m_ConstraintList.size() - 1);
        m_Storage = 0;
        m_isStart = false;
    }
    
    public String toString() {
        
        int width = 1 + ("" + (m_N * m_N)).length();
        String str = m_Value == 0 ? "x" : ("" + m_Value);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < width - str.length(); i++) {
            sb.append(" ");
        }
        sb.append(str);
        return sb.toString();
    }

}
