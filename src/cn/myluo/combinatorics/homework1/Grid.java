package cn.myluo.combinatorics.homework1;

import java.util.ArrayList;
import java.util.List;

public class Grid {
	
	private int m_Value;
	
	private List<Integer> m_ConstraintList;
	
	private List<Integer> m_ConstraintIndex;
	
	private List<Integer> m_NextList;
	
	private List<Integer> m_NextIndex;
	
	private boolean m_isChoose;
	
	private boolean m_isStart;
	
	private Grid() {
		m_ConstraintList = new ArrayList<Integer>();
		m_ConstraintIndex = new ArrayList<Integer>();
		m_NextList = new ArrayList<Integer>();
		m_NextIndex = new ArrayList<Integer>();
	}
	
	public Grid(int n) {
		this();
		init(n);
	}
	
	private void init(int n) {
		for(int i = 0; i < n * n; i++) {
			m_ConstraintList.add(i);
			m_ConstraintIndex.add(i);
		}
	}
	
	public int getValue() {
		return m_Value;
	}
	
	public boolean isChoose() {
	    return m_isChoose;
	}
	
	public boolean isStart() {
        return m_isStart;
    }
	
	public int getConstraintCount() {
	    return m_ConstraintList.size();
	}
	
	public int getNextCount() {
        return m_NextList.size();
    }
	
	public boolean limit(int value) {
	    if(m_isChoose) return false;
		int index = m_ConstraintIndex.get(value);
		if(index == -1) return false;
		m_ConstraintList.set(index, m_ConstraintList.get(m_ConstraintList.size() - 1));
		m_ConstraintIndex.set(m_ConstraintList.get(index), index);
		m_ConstraintIndex.set(value, -1);
		m_ConstraintList.remove(m_ConstraintList.size() - 1);
		return m_ConstraintList.size() == 1;
	}
	
	public void expand(int value) {
	    if(m_isChoose || m_ConstraintIndex.get(value) != -1) return;
		m_ConstraintList.add(value);
		m_ConstraintIndex.set(value, m_ConstraintList.size() - 1);
	}
	
	public int choose(int rand) {
	    if(!m_isStart) {
	        m_NextList.clear();
	        m_NextList.addAll(m_ConstraintList);
	        m_NextIndex.clear();
	        m_NextIndex.addAll(m_ConstraintIndex);
	        m_isStart = true;
	    }
	    if(m_NextList.size() == 0) {
	        m_isStart = false;
	        return -1;
	    }
	    int index = rand % m_NextList.size();
	    m_Value = m_NextList.get(index);
	    m_NextList.set(index, m_NextList.get(m_NextList.size() - 1));
	    m_NextIndex.set(m_NextList.get(index), index);
        m_NextIndex.set(m_Value, -1);
        m_NextList.remove(m_NextList.size() - 1);
	    m_isChoose = true;
	    return m_Value;
	}
	
	public int cancel() {
	    int temp = m_Value;
	    m_Value = 0;
	    m_isChoose = false;
	    return temp;
	}

}
