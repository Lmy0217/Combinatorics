package cn.myluo.combinatorics.homework1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Generator {
	
	private List<Block> m_BlockList;
	
	private Random m_Random;
	
	private Generator() {
		m_BlockList = new ArrayList<Block>();
		m_Random = new Random(System.currentTimeMillis());
	}
	
	public Generator(int n) {
		this();
		init(n);
	}
	
	private void init(int n) {
		for(int i = 0; i < n * n; i++) {
			m_BlockList.add(new Block(n));
		}
	}
	
	private List<Integer> RandList(int length) {
		List<Integer> randList = new ArrayList<Integer>();
		for(int i = 0; i < length; i++) {
			randList.add(i);
		}
		Collections.shuffle(randList);
		return randList;
	}

}
