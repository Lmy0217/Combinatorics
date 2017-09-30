package cn.myluo.combinatorics.homework1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Block {
	
	private List<Grid> m_GridList;
	
	private List<Map<Integer, Object>> m_ConstraintList;
	
	private Map<Integer, Object> m_ConstraintMap;
	
	private int m_Count;
	
	private int m_N;
	
	private int m_Index;
	
	private Block() {
		m_GridList = new ArrayList<Grid>();
		m_ConstraintList = new ArrayList<Map<Integer, Object>>();
		m_ConstraintMap = new HashMap<Integer, Object>();
	}
	
	public Block(int n, int index) {
		this();
		m_N = n;
		m_Index = index;
		init();
	}
	
	private void init() {
		for(int i = 0; i < m_N * m_N; i++) {
			m_GridList.add(new Grid(m_N));
		}
		for(int i = 0; i < 2 * m_N; i++) {
			m_ConstraintList.add(new HashMap<Integer, Object>());
		}
	}
	
	public int getCount() {
	    return m_Count;
	}
	
	public List<Integer> limit(int index, int value) {
	    List<Integer> next = new ArrayList<Integer>();
	    if(m_Count == m_N * m_N || m_ConstraintMap.get(value) != null || m_ConstraintList.get(index).get(value) != null) return next;
	    m_ConstraintList.get(index).put(value, value);
	    for(int i = ((index < m_N) ? index : ((index - m_N) * m_N)); i < ((index < m_N) ? (index + m_N * m_N) : ((index - m_N) * m_N + m_N)); i += ((index < m_N) ? m_N : 1)) {
	        if(m_GridList.get(i).limit(value)) {
	            next.add(i + m_Index * m_N * m_N);
	        }
	    }
	    return next;
	}
	
	public void expand(int index, int value) {
	    if(m_Count == m_N * m_N || m_ConstraintMap.get(value) != null || m_ConstraintList.get(index).get(value) == null) return;
	    m_ConstraintList.get(index).remove(value);
        for(int i = ((index < m_N) ? index : ((index - m_N) * m_N)); i < ((index < m_N) ? (index + m_N * m_N) : ((index - m_N) * m_N + m_N)); i += ((index < m_N) ? m_N : 1)) {
            m_GridList.get(i).expand(value);
        }
	}
	
	public List<Integer> choose(int index, int rand) {
	    List<Integer> next = new ArrayList<Integer>();
        int value = m_GridList.get(index).choose(rand);
        next.add(value);
        if(value == -1) return next;
        m_ConstraintMap.put(value, value);
        for(int i = 0; i < m_N * m_N; i++) {
            if(i == index) continue;
            if(m_GridList.get(i).limit(value)) {
                next.add(i + m_Index * m_N * m_N);
            }
        }
        m_Count++;
        return next;
    }
	
	public int cancel(int index) {
	    int value = m_GridList.get(index).cancel();
	    m_ConstraintMap.remove(value);
        for(int i = 0; i < m_N * m_N; i++) {
            if(i == index || m_ConstraintList.get(i % m_N).get(value) != null || m_ConstraintList.get(i / m_N + m_N).get(value) != null) continue;
            m_GridList.get(i).expand(value);
        }
        m_Count--;
        return value;
	}
	
	public String getConstraintRow(int index) {
	    StringBuilder sb = new StringBuilder();
        for(int i = index * m_N; i < (1 + index) * m_N; i++) {
            sb.append(m_GridList.get(i).getConstraintCount());
        }
        return sb.toString();
	}
	
	public String getRow(int index) {
	    StringBuilder sb = new StringBuilder();
	    for(int i = index * m_N; i < (1 + index) * m_N; i++) {
	        sb.append(m_GridList.get(i).toString());
	    }
	    return sb.toString();
	}
	
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    for(int i = 0; i < m_N; i++) {
	        sb.append(getRow(i) + "\n");
	    }
	    return sb.toString();
	}

}
