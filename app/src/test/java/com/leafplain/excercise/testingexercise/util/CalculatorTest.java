package com.leafplain.excercise.testingexercise.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by kennethyeh on 2017/3/9.
 */
public class CalculatorTest {

    private Calculator mCalculator;

    @Before
    public void setUp() throws Exception {
        mCalculator = new Calculator();
    }

    @Test
    public void testSum() throws Exception {
        //expected: 6, sum of 1 and 5
        Assert.assertEquals(6, mCalculator.sum(1, 5), 0);
    }

    @Test
    public void testSubstract() throws Exception {
        Assert.assertEquals(1, mCalculator.substract(5, 4), 0);
    }

    @Test
    public void testDivide() throws Exception {
        Calculator mCalculator = new Calculator();

        MathUtils mockMathUtils = mock(MathUtils.class);
        when(mockMathUtils.checkZero(1)).thenReturn(false); // when num==1 checkZero(num)return false
        when(mockMathUtils.checkZero(0)).thenReturn(true); // when num==0 checkZero(num)return true

        Logger mockLogger = mock(Logger.class);

        mCalculator.setCalculator(mockMathUtils);
        mCalculator.setLogger(mockLogger);

        Assert.assertEquals(4, mCalculator.divide(20, 5), 0);

        try {
            mCalculator.divide(2, 0); // 預期出現錯誤
            throw new RuntimeException("no expectant exception"); // 未出現預期錯誤 例外產生
        } catch (Exception e) {
            Assert.assertEquals("dividend is zero", e.getMessage()); // 驗證錯誤
        }
//        int expectCallTimes = 3;
        int expectCallTimes = 2;
        Mockito.verify(mockLogger, Mockito.times(expectCallTimes)).logAction(Mockito.anyString());
    }

    @Test
    public void testMultiply() throws Exception {
        Assert.assertEquals(10, mCalculator.multiply(2, 5), 0);
    }
}