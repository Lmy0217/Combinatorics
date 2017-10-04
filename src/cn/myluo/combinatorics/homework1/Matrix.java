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
        if(n < 1) {
        	System.out.println("n must be positive!");
        }
        m_N = n;
        init();
    }
    
    public Matrix(int[] puzzle) {
    	this();
    	if(!init(puzzle)) {
    		System.out.println("Puzzle error!");
    	}
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
    
    private boolean init(int[] puzzle) {
    	if(puzzle == null) return false;
    	double dn = Math.sqrt(Math.sqrt((double)puzzle.length));
    	if(Math.floor(dn) != dn) return false;
    	m_N = (new Double(dn)).intValue();
    	init();
    	for(int i = 0; i < puzzle.length; i++) {
    		if(puzzle[i] < 1 || puzzle[i] > m_N * m_N) continue;
    		int row = i / (m_N * m_N);
    		int col = i % (m_N * m_N);
    		int blockIndex = row / m_N * m_N + col / m_N;
    		int gridIndex = row % m_N * m_N + col % m_N;
    		int index = blockIndex *  m_N * m_N + gridIndex;
    		if(!choose(index, puzzle[i], true)) return false;
    	}
    	return true;
    }
    
    public boolean increase() {
        while(m_Count != m_N * m_N) {
            if(m_Possible.size() != 0) {
                if(!choose(m_Possible.get(0), m_Random.nextInt(m_N * m_N), false)) {
                	if(m_Record.size() == 0) {
                		return false;
                	}
                	int value = m_Record.get(m_Record.size() - 1);
                    m_Possible.add(0, value);
                    m_PossibleMap.put(value, value);
                    cancel(m_Possible.get(0), false);
                } else {
                	m_PossibleMap.remove(m_Possible.get(0));
                    m_Possible.remove(0);
                }
            } else {
                while(!choose(m_RandList.get(m_Random.nextInt(m_RandList.size())), m_Random.nextInt(m_N * m_N), false));
            }
            //System.out.println(getChooseCount());
        }
        return true;
    }
    
    public int[] reduce(double rank) {
    	for(int i = 0; i < m_N * m_N * m_N * m_N; i++) {
    		//System.out.println("======" + i);
    		if(m_Random.nextDouble() >= rank) {
    			continue;
    		}
    		m_Record.clear();
    		m_Possible.clear();
    		m_PossibleMap.clear();
    		int row = i / (m_N * m_N);
    		int col = i % (m_N * m_N);
    		int blockIndex = row / m_N * m_N + col / m_N;
    		int gridIndex = row % m_N * m_N + col % m_N;
    		int index = blockIndex *  m_N * m_N + gridIndex;
    		m_Record.add(index);
    		int value = cancel(index, true);
    		if(choose(index, m_Random.nextInt(m_N * m_N), false) && increase()) {
    			for(int j = m_Record.size() - 1; j > 0; j--) {
    				int recordValue = m_Record.get(j);
    				cancel(recordValue, true);
    				m_BlockList.get(recordValue / (m_N * m_N)).restore(recordValue % (m_N * m_N));
    			}
    			cancel(m_Record.get(0), false);
    			m_BlockList.get(blockIndex).restore(gridIndex);
    			choose(index, value, true);
    		} else {
    			m_BlockList.get(blockIndex).restore(gridIndex);
    		}
    	}
    	return toArray();
    }
    
    private boolean choose(int index, int rand, boolean isValue) {
        int blockIndex = index / (m_N * m_N);
        int gridIndex = index % (m_N * m_N);
        List<Integer> next = m_BlockList.get(blockIndex).choose(gridIndex, rand, isValue);
        int value = next.get(0);
        if(value == -1) return false;
        next.remove(0);
        if(isValue && m_PossibleMap.get(index) != null) {
        	m_PossibleMap.remove(index);
        	m_Possible.remove((Object)index);
        }
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
            List<Integer> possibleList = m_BlockList.get(i).limit(gridIndex % m_N, isValue ? rand : value);
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
            List<Integer> possibleList = m_BlockList.get(i).limit(gridIndex / m_N + m_N, isValue ? rand : value);
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
    
    private int cancel(int index, boolean isStorage) {
        int blockIndex = index / (m_N * m_N);
        int gridIndex = index % (m_N * m_N);
        int value = m_BlockList.get(blockIndex).cancel(gridIndex, isStorage);
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
        return value;
    }
    
    private int getChooseCount() {
    	int count = 0;
    	for(int i = 0; i < m_BlockList.size(); i++) {
    		count += m_BlockList.get(i).getCount();
    	}
    	return count;
    }
    
    public int[] toArray() {
    	int[] array = new int[m_N * m_N * m_N * m_N];
    	for(int row = 0; row < m_N * m_N; row++) {
    		for(int col = 0; col < m_N; col++) {
    			System.arraycopy(m_BlockList.get(row / m_N * m_N + col).getRowArray(row % m_N), 0, array, row * m_N * m_N + col * m_N, m_N);
    		}
    	}
    	return array;
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
//    	Matrix matrix = new Matrix(new int[]{
//    			8, 0, 0, 0, 0, 0, 0, 0, 0,
//    			0, 0, 3, 6, 0, 0, 0, 0, 0,
//    			0, 7, 0, 0, 9, 0, 2, 0, 0,
//    			0, 5, 0, 0, 0, 7, 0, 0, 0,
//    			0, 0, 0, 0, 4, 5, 7, 0, 0,
//    			0, 0, 0, 1, 0, 0, 0, 3, 0,
//    			0, 0, 1, 0, 0, 0, 0, 6, 8,
//    			0, 0, 8, 5, 0, 0, 0, 1, 0,
//    			0, 9, 0, 0, 0, 0, 4, 0, 0});
//    	int[][] puzzles = {
//    			{
//    				8, 0, 0, 0, 0, 0, 0, 0, 0,
//        		    0, 0, 3, 6, 0, 0, 0, 0, 0,
//        		    0, 7, 0, 0, 9, 0, 2, 0, 0,
//        		    0, 5, 0, 0, 0, 7, 0, 0, 0,
//        		    0, 0, 0, 0, 4, 5, 7, 0, 0,
//        		    0, 0, 0, 1, 0, 0, 0, 3, 0,
//        		    0, 0, 1, 0, 0, 0, 0, 6, 8,
//        		    0, 0, 8, 5, 0, 0, 0, 1, 0,
//        		    0, 9, 0, 0, 0, 0, 4, 0, 0
//        		},
//    			{
//        			0, 0, 7, 0, 0, 0, 8, 2, 0,
//        	        0, 9, 0, 0, 0, 1, 0, 0, 0,
//        	        0, 4, 0, 9, 7, 0, 0, 0, 0,
//        	        0, 0, 0, 0, 0, 5, 4, 0, 6,
//        	        0, 0, 3, 0, 0, 0, 7, 0, 0,
//        	        5, 0, 6, 7, 0, 0, 0, 0, 0,
//        	        0, 0, 0, 0, 8, 4, 0, 5, 0,
//        	        0, 0, 0, 6, 0, 0, 0, 1, 0,
//        	        0, 2, 4, 0, 0, 0, 6, 0, 0
//        	    },
//    			{
//        	    	0, 8, 1, 3, 0, 2, 6, 0, 0,
//        	    	6, 0, 9, 5, 0, 1, 0, 2, 0,
//        	    	2, 3, 0, 0, 0, 0, 0, 0, 0,
//        	    	5, 0, 2, 0, 3, 0, 7, 8, 9,
//        	    	0, 0, 0, 0, 0, 0, 0, 0, 0,
//        	    	4, 6, 3, 0, 8, 0, 2, 0, 1,
//        	    	0, 0, 0, 0, 0, 0, 0, 6, 2,
//        	    	0, 2, 0, 7, 0, 9, 5, 0, 3,
//        	    	0, 0, 6, 8, 0, 3, 9, 4, 0
//    			}
//    	};
//    	Matrix matrix = new Matrix(puzzles[0]);
        matrix.increase();
        System.out.println(matrix);
        int[] puzzle = matrix.reduce(0.8);
        for(int i = 0; i < 9; i++) {
        	for(int j = 0; j < 9; j++) {
        		System.out.print(puzzle[i * 9 + j]);
        	}
        	System.out.println();
        }
        System.out.println();
        System.out.println(matrix);
        Matrix m1 = new Matrix(puzzle);
        m1.increase();
        System.out.println(m1);
        Matrix m2 = new Matrix(puzzle);
        m2.increase();
        System.out.println(m2);
        long stopTime = Calendar.getInstance().getTimeInMillis();
        System.out.println(stopTime - startTime);
    }

}
