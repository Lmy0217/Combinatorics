/**
 *   ThreeSum.java
 *   Copyright (C) 2017 Mingyuan Luo
 */

package cn.myluo.combinatorics.homework2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Class of solver of the 3Sum puzzle.
 *
 * @version 20171014
 * @author Mingyuan Luo
 */
public class ThreeSum {

    /**
     * The set of some unique floating numbers.
     */
    private double[] m_Set;

    /**
     * The value of the sum of three floating numbers in the numbers set.
     *
     * @see cn.myluo.combinatorics.homework2.ThreeSum#m_Set
     */
    private double m_Value;

    /**
     * The results list of 3Sum question with the numbers set
     * {@linkplain cn.myluo.combinatorics.homework2.ThreeSum#m_Set m_Set} and the
     * value {@linkplain cn.myluo.combinatorics.homework2.ThreeSum#m_Value m_Value},
     * including many three numbers which the sum is nearest the value
     * {@linkplain cn.myluo.combinatorics.homework2.ThreeSum#m_Value m_Value}.
     *
     * @see cn.myluo.combinatorics.homework2.ThreeSum#m_Value
     */
    private List<List<Double>> m_Result;

    /**
     * The difference between the sum of each three numbers in the results list
     * {@linkplain cn.myluo.combinatorics.homework2.ThreeSum#m_Result m_Result} and
     * the value {@linkplain cn.myluo.combinatorics.homework2.ThreeSum#m_Value
     * m_Value}.
     *
     * @see cn.myluo.combinatorics.homework2.ThreeSum#m_Result
     */
    private double m_Residual;

    /**
     * No parameter construction method for initialize the numbers list
     * {@linkplain cn.myluo.combinatorics.homework2.ThreeSum#m_Set m_Set} and the
     * difference {@linkplain cn.myluo.combinatorics.homework2.ThreeSum#m_Residual
     * m_Residual}.
     */
    private ThreeSum() {

        m_Result = new ArrayList<List<Double>>();
        m_Residual = Double.MAX_VALUE;
    }

    /**
     * Construction method calling the other parameter construction method
     * {@linkplain cn.myluo.combinatorics.homework2.ThreeSum#ThreeSum(double[], double)
     * ThreeSum(double[], double)} to solve the 3Sum question of the random set with
     * the given parameters and the given value of three numbers' sum.
     *
     * @param size
     *            the random set's size
     * @param min
     *            the minimum number (inclusive) in the random set
     * @param max
     *            the maximum number (exclusive) in the random set
     * @param value
     *            the given value of three numbers' sum
     *
     * @see cn.myluo.combinatorics.homework2.ThreeSum#randSet(int, double, double)
     */
    public ThreeSum(int size, double min, double max, double value) {

        this(randSet(size, min, max), value);
    }

    /**
     * Construction method to solve the 3Sum question of the given set and the given
     * value of three numbers' sum.
     *
     * @param set
     *            the given set
     * @param value
     *            the given value of three numbers' sum
     */
    public ThreeSum(double[] set, double value) {

        this();
        // determine the legitimacy
        if (set == null) {
            System.out.println("set must be exist!");
            return;
        }
        if (set.length < 3) {
            System.out.println("size must be greater than 3!");
            return;
        }

        m_Set = set;
        m_Value = value;

        solve();
    }

    /**
     * The solver of the 3Sum puzzle with the {@code O(n^2*logn)} time complexity.
     */
    private void solve() {

        // sort the numbers set
        Arrays.sort(m_Set);

        // search the sum of two numbers
        for (int i = 0; i < m_Set.length - 2; i++) {

            for (int j = 1 + i; j < m_Set.length - 1; j++) {
                // the difference between two numbers' sum and the value
                double residual = m_Value - m_Set[i] - m_Set[j];
                // search the nearest number's index of the difference
                int index = Arrays.binarySearch(m_Set, 1 + j, m_Set.length, residual);
                if (index >= 0) {
                    // if the difference is a number in set
                    if (Double.compare(0, m_Residual) != 0) {
                        m_Result.clear();
                        m_Residual = 0;
                    }
                    // add result
                    m_Result.add(new ArrayList<Double>(Arrays.asList(m_Set[i], m_Set[j], m_Set[index])));

                } else {
                    // if the difference is not in set
                    index = -index - 1;
                    int smallIndex = index - ((index == 1 + j) ? 0 : 1);
                    int greatIndex = index - ((index == m_Set.length) ? 1 : 0);
                    double smallResidual = Math.abs(residual - m_Set[smallIndex]);
                    double greatResidual = Math.abs(residual - m_Set[greatIndex]);
                    // choose near number
                    double nextResidual;
                    if (Double.compare(smallResidual, greatResidual) <= 0) {
                        nextResidual = smallResidual;
                        index = smallIndex;
                    } else {
                        nextResidual = greatResidual;
                        index = greatIndex;
                    }
                    // compare the difference of three number
                    int smaller = Double.compare(nextResidual, m_Residual);
                    if (smaller < 0) {
                        m_Result.clear();
                        m_Residual = nextResidual;
                        m_Result.add(new ArrayList<Double>(Arrays.asList(m_Set[i], m_Set[j], m_Set[index])));
                    } else if (smaller == 0) {
                        m_Result.add(new ArrayList<Double>(Arrays.asList(m_Set[i], m_Set[j], m_Set[index])));
                    }
                }
            }
        }
    }

    /**
     * Creates a random floating numbers array with the given size and the given
     * range.
     *
     * @param size
     *            the given size
     * @param min
     *            the minimum number (inclusive) in the random set
     * @param max
     *            the maximum number (exclusive) in the random set
     * @return the random floating numbers array with the given size and the given
     *         range
     */
    public static double[] randSet(int size, double min, double max) {

        // determine the legitimacy
        if (size < 1) {
            System.out.println("size must be positive!");
            return null;
        }
        if (Double.compare(min, max) >= 0) {
            System.out.println("min must be smaller than max!");
            return null;
        }

        Map<Double, Double> map = new HashMap<Double, Double>();
        Random random = new Random(System.currentTimeMillis());
        // keep uniqueness of each number
        while (map.size() < size) {
            Double rand = min + random.nextDouble() * (max - min);
            map.put(rand, rand);
        }
        // to array
        double[] set = new double[size];
        int i = 0;
        for (Double value : map.values()) {
            set[i++] = value.doubleValue();
        }

        return set;
    }

    /**
     * Returns a description of this 3Sum puzzle and its solution.
     *
     * @return the description of this 3Sum puzzle and its solution.
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("Set: {");
        for (int i = 0; i < m_Set.length; i++) {
            sb.append(" " + m_Set[i] + ",");
        }
        if (m_Set.length > 0)
            sb.delete(sb.length() - 1, sb.length());
        sb.append(" }\n");

        sb.append("Value: " + m_Value + "\n");

        sb.append("Residual: " + m_Residual + "\n");

        sb.append("Results: {");
        for (int i = 0; i < m_Result.size(); i++) {
            sb.append("\n    {");
            for (int j = 0; j < m_Result.get(i).size(); j++) {
                sb.append(" " + m_Result.get(i).get(j) + ",");
            }
            if (!m_Result.get(i).isEmpty())
                sb.delete(sb.length() - 1, sb.length());
            sb.append(" },");
        }
        if (!m_Result.isEmpty())
            sb.delete(sb.length() - 1, sb.length());
        sb.append("\n}\n");

        return sb.toString();
    }

    /**
     * Tests method of ThreeSum.
     *
     * @param args
     *            ignore
     */
    public static void main(String[] args) {

        // random set of size 100, minimum 1, maximum 200 and the sum of three number is
        // 60.
        System.out.println(new ThreeSum(100, 1, 200, 60));
    }

}
