package cn.myluo.combinatorics.homework1;

import java.util.ArrayList;
import java.util.List;

public class Matrix {
    
    private List<Block> m_BlockList;
    
    private List<Integer> m_Record;
    
    private List<Integer> m_Possible;
    
    private int m_N;
    
    private int m_Count;
    
    private Matrix() {
        m_BlockList = new ArrayList<Block>();
    }
    
    public Matrix(int n) {
        this();
        m_N = n;
        init();
    }
    
    private void init() {
        for(int i = 0; i < m_N * m_N; i++) {
            m_BlockList.add(new Block(m_N));
        }
    }
    
    public void increase() {
        while(m_Count != m_N * m_N) {
            if(m_Possible.size() != 0) {
                
            } else {
                
            }
        }
    }
    
    public boolean choose(int index, int rand) {
        int blockIndex = index / (m_N * m_N);
        int gridIndex = index % (m_N * m_N);
        int value = m_BlockList.get(blockIndex).choose(gridIndex, rand);
        if(value == -1) return false;
        if(m_BlockList.get(blockIndex).getCount() == m_N * m_N) m_Count++;
        for(int i = blockIndex % m_N; i < blockIndex % m_N + m_N * m_N; i += m_N) {
            if(i == blockIndex) continue;
            m_Possible.addAll(m_BlockList.get(i).limit(gridIndex % m_N, value));
        }
        for(int i = blockIndex / m_N + m_N; i < blockIndex / m_N + 2 * m_N; i++) {
            if(i == blockIndex) continue;
            m_Possible.addAll(m_BlockList.get(i).limit(gridIndex / m_N + m_N, value));
        }
        return true;
    }
    
    public void cancel(int index) {
        int blockIndex = index / (m_N * m_N);
        int gridIndex = index % (m_N * m_N);
        int value = m_BlockList.get(blockIndex).cancel(gridIndex);
        if(m_BlockList.get(blockIndex).getCount() == m_N * m_N - 1) m_Count--;
        for(int i = blockIndex % m_N; i < blockIndex % m_N + m_N * m_N; i += m_N) {
            if(i == blockIndex) continue;
            m_BlockList.get(i).expand(gridIndex % m_N, value);
        }
        for(int i = blockIndex / m_N + m_N; i < blockIndex / m_N + 2 * m_N; i++) {
            if(i == blockIndex) continue;
            m_BlockList.get(i).expand(gridIndex / m_N + m_N, value);
        }
    }

}
