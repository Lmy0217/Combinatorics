package cn.myluo.combinatorics.homework1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Matrix {
    
    private List<Block> m_BlockList;
    
    private List<Integer> m_Record;
    
    private List<Integer> m_Possible;
    
    private Map<Integer, Integer> m_PossibleMap;
    
    private List<Integer> m_RandList;
    
    private List<Integer> m_RandIndex;
    
    private int m_N;
    
    private int m_Count;
    
    private Random m_Random;
    
    private Matrix() {
        m_BlockList = new ArrayList<Block>();
        m_Record = new ArrayList<Integer>();
        m_Possible = new ArrayList<Integer>();
        m_PossibleMap = new HashMap<Integer, Integer>();
        m_RandList = new ArrayList<Integer>();
        m_RandIndex = new ArrayList<Integer>();
        m_Random = new Random(System.currentTimeMillis());
    }
    
    public Matrix(int n) {
        this();
        m_N = n;
        init();
    }
    
    private void init() {
        for(int i = 0; i < m_N * m_N; i++) {
            m_BlockList.add(new Block(m_N, i));
            for(int j = 0; j < m_N * m_N; j++) {
                m_RandList.add(i * m_N * m_N + j);
                m_RandIndex.add(i * m_N * m_N + j);
            }
        }
    }
    
    public void increase() {
        while(m_Count != m_N * m_N) {
            if(m_Possible.size() != 0) {
                if(!choose(m_Possible.get(0), m_Random.nextInt(m_N * m_N))) {
                	int value = m_Record.get(m_Record.size() - 1);
                    m_Possible.add(0, value);
                    m_PossibleMap.put(value, value);
                    cancel(m_Possible.get(0));
                } else {
                	m_PossibleMap.remove(m_Possible.get(0));
                    m_Possible.remove(0);
                }
            } else {
                while(!choose(m_RandList.get(m_Random.nextInt(m_RandList.size())), m_Random.nextInt(m_N * m_N)));
            }
            System.out.println(getChooseCount());
        }
    }
    
    private boolean choose(int index, int rand) {
        int blockIndex = index / (m_N * m_N);
        int gridIndex = index % (m_N * m_N);
        List<Integer> next = m_BlockList.get(blockIndex).choose(gridIndex, rand);
        int value = next.get(0);
        if(value == -1) return false;
        next.remove(0);
        for(int i = 0; i < next.size(); i++) {
        	int possibleValue = next.get(i);
        	if(m_PossibleMap.get(possibleValue) == null) {
        		m_Possible.add(possibleValue);
        		m_PossibleMap.put(possibleValue, possibleValue);
        	}
        }
        m_Record.add(index);
        int rankIndex = m_RandIndex.get(index);
        m_RandList.set(rankIndex, m_RandList.get(m_RandList.size() - 1));
        m_RandIndex.set(m_RandList.get(rankIndex), rankIndex);
        m_RandIndex.set(index, -1);
        m_RandList.remove(m_RandList.size() - 1);
        if(m_BlockList.get(blockIndex).getCount() == m_N * m_N) m_Count++;
        for(int i = blockIndex % m_N; i < blockIndex % m_N + m_N * m_N; i += m_N) {
            if(i == blockIndex) continue;
            List<Integer> possibleList = m_BlockList.get(i).limit(gridIndex % m_N, value);
            for(int j = 0; j < possibleList.size(); j++) {
            	int possibleValue = possibleList.get(j);
            	if(m_PossibleMap.get(possibleValue) == null) {
            		m_Possible.add(possibleValue);
            		m_PossibleMap.put(possibleValue, possibleValue);
            	}
            }
        }
        for(int i = blockIndex / m_N * m_N; i < blockIndex / m_N * m_N + m_N; i++) {
            if(i == blockIndex) continue;
            List<Integer> possibleList = m_BlockList.get(i).limit(gridIndex / m_N + m_N, value);
            for(int j = 0; j < possibleList.size(); j++) {
            	int possibleValue = possibleList.get(j);
            	if(m_PossibleMap.get(possibleValue) == null) {
            		m_Possible.add(possibleValue);
            		m_PossibleMap.put(possibleValue, possibleValue);
            	}
            }
        }
        return true;
    }
    
    private void cancel(int index) {
        int blockIndex = index / (m_N * m_N);
        int gridIndex = index % (m_N * m_N);
        int value = m_BlockList.get(blockIndex).cancel(gridIndex);
        m_RandList.add(index);
        m_RandIndex.set(index, m_RandList.size() - 1);
        m_Record.remove(m_Record.size() - 1);
        if(m_BlockList.get(blockIndex).getCount() == m_N * m_N - 1) m_Count--;
        for(int i = blockIndex % m_N; i < blockIndex % m_N + m_N * m_N; i += m_N) {
            if(i == blockIndex) continue;
            m_BlockList.get(i).expand(gridIndex % m_N, value);
        }
        for(int i = blockIndex / m_N * m_N; i < blockIndex / m_N * m_N + m_N; i++) {
            if(i == blockIndex) continue;
            m_BlockList.get(i).expand(gridIndex / m_N + m_N, value);
        }
    }
    
    public int getChooseCount() {
    	int count = 0;
    	for(int i = 0; i < m_BlockList.size(); i++) {
    		count += m_BlockList.get(i).getCount();
    	}
    	return count;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int row = 0; row < m_N * m_N; row++) {
            for(int col = 0; col < m_N; col++) {
                sb.append(m_BlockList.get(row / m_N * m_N + col).getRow(row % m_N));
            }
            sb.append("  ");
            for(int col = 0; col < m_N; col++) {
                sb.append(m_BlockList.get(row / m_N * m_N + col).getConstraintRow(row % m_N));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
    	long startTime = Calendar.getInstance().getTimeInMillis();
        Matrix matrix = new Matrix(3);
        matrix.increase();
        long stopTime = Calendar.getInstance().getTimeInMillis();
        System.out.println(matrix);
        System.out.println(stopTime - startTime);
    }

}
