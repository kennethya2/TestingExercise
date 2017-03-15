package com.leafplain.excercise.testingexercise.util;

/**
 * Created by kennethyeh on 2017/3/9.
 */

public class Calculator {

    private MathUtils mMathUtils    = new MathUtils();
    private Logger mLogger          = new Logger();

    // method for mock testing
    public void setCalculator (MathUtils mathUtils){
        this.mMathUtils = mathUtils;
    }
    // method for mock testing
    public void setLogger (Logger logger){
        this.mLogger = logger;
    }


    public double sum(double a, double b){
        mLogger.logAction("sum");
        return a + b;
    }

    public double substract(double a, double b){
        mLogger.logAction("substract");
        return a - b;
    }

    public double divide(double a, double b){
        mLogger.logAction("divide");
        if (mMathUtils.checkZero((int) b)) {
            throw new RuntimeException("dividend is zero");
        }
        return a / b;
    }

    public double multiply(double a, double b){
        mLogger.logAction("multiply");
        return a * b;
    }
}