
# TestingExercise

- [Gradle加入測試函式庫](https://github.com/kennethya2/TestingExercise#gradle加入測試函式庫) 
- [單元測試](https://github.com/kennethya2/TestingExercise#單元測試) 
- [實機測試](https://github.com/kennethya2/TestingExercise#實機測試) 


### Gradle加入測試函式庫
----

<pre><code>
dependencies {
	...
    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })
    testCompile 'junit:junit:4.12' 
    testCompile 'org.mockito:mockito-core:1.10.19'
}
</code></pre>

###### 單元測試函式庫

junit:junit:4.12

org.mockito:mockito-core:1.10.19

###### UI測試函式庫

com.android.support.test.espresso:espresso-core:2.2.2

### 單元測試
----

路徑：module-name/src/test/java/<package_name>/uitl/CalculatorTest.java

##### 1. 測試Calculator運算是否符合預期

Assert.assertEquals(expected, actual, 誤差範圍)

<pre><code>	

public class CalculatorTest {

    private Calculator mCalculator;

    @Before
    public void setUp() throws Exception {
        mCalculator = new Calculator();
    }
	...
	
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
        ...
        Assert.assertEquals(4, mCalculator.divide(20, 5), 0);
    }

    @Test
    public void testMultiply() throws Exception {
		...
		Assert.assertEquals(10, mCalculator.multiply(2, 5), 0);
    } 
}

</code></pre>


##### 2. 相依注入Dependency Injection

###### 模擬函式回傳結果

利用Mockito模擬Calculator所相依類別MathUtils其呼叫函式``checkZero(int num)``回傳結果

當checkZero傳入0時，回傳true

當checkZero傳入1時，回傳false

<pre><code>

@Test
    public void testDivide() throws Exception {
        Calculator mCalculator = new Calculator();

        MathUtils mockMathUtils = mock(MathUtils.class);
        when(mockMathUtils.checkZero(1)).thenReturn(false); 
        // when num==1 checkZero(num)return false
        when(mockMathUtils.checkZero(0)).thenReturn(true); 
        // when num==0 checkZero(num)return true
        
		mCalculator.setCalculator(mockMathUtils);
        ...

        try {
            mCalculator.divide(2, 0); // 預期出現錯誤
            throw new RuntimeException("no expectant exception"); 
            // 未出現預期錯誤 例外產生
        } catch (Exception e) {
            Assert.assertEquals("dividend is zero", e.getMessage()); 
            // 驗證錯誤
        }...
    }
    
</code></pre>

###### 驗證函式呼叫次數

每呼叫一次運算function觸發Logger``logAction(String action)``


<pre><code>

    public double divide(double a, double b){
        mLogger.logAction("divide");
        if (mMathUtils.checkZero((int) b)) {
            throw new RuntimeException("dividend is zero");
        }
        return a / b;
	}
	
</code></pre>   

Mockito.verify() 驗證Logger呼叫``logAction(String action)``次數

期望為2次``int expectCallTimes = 2``

<pre><code>

@Test
    public void testDivide() throws Exception {
        Calculator mCalculator = new Calculator();
		...
        Logger mockLogger = mock(Logger.class);
        mCalculator.setLogger(mockLogger);
        ...
		Assert.assertEquals(4, mCalculator.divide(20, 5), 0);
  		mCalculator.divide(2, 0); // 預期出現錯誤

		int expectCallTimes = 2;
        Mockito.verify(mockLogger, Mockito.times(expectCallTimes))
        .logAction(Mockito.anyString());
    }
    
</code></pre>


###### 注意 Error: "Method ... not mocked"

單元測試無法無法執行android api函式，必須使用mock模擬android呼叫行為。

build.gradle解決方式如下：

<pre><code>
android {
  ...
  testOptions {
    unitTests.returnDefaultValues = true
  }
}
</code></pre>

參考：[Building Local Unit Tests](https://developer.android.com/training/testing/unit-testing/local-unit-tests.html)

### 實機測試
----

路徑：module-name/src/androidTest/java/<package_name>/MainActivityEspressoTest.java

1.首先定義欲測試的Activity


 ``ActivityTestRule<MainActivity> mActivityRule 
    = new ActivityTestRule<>(MainActivity.class)``
    

2.模擬輸入字串
	
``onView(withId(R.id.editText)).perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard()) ``
        
3.點擊包含字串``"Who Are You?"``的按鈕，將自顯示於TextView

MainActivity.java
<pre><code>
	...
    public void hello(View v){
        TextView textView = (TextView) findViewById(R.id.textView);
        EditText editText = (EditText) findViewById(R.id.editText);
        textView.setText(COOL + editText.getText().toString() );
    }
 </code></pre>

4.比對TextView顯示字串是否為預期節果

 ``onView(withId(R.id.textView))
        .check(matches(withText(expectedText)))``
        

<pre><code>
public class MainActivityEspressoTest {

    private static final String STRING_TO_BE_TYPED = "Ken";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule 
    = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void sayHello(){
        onView(withId(R.id.editText))
        .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard()); //line 1

        onView(withText("Who Are You?"))
        .perform(click()); //line 2

        String expectedText = MainActivity.COOL + STRING_TO_BE_TYPED ;
        onView(withId(R.id.textView))
        .check(matches(withText(expectedText))); //line 3
    }
}
</code></pre>

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/AjpQtPBtRUM/0.jpg)](https://youtu.be/AjpQtPBtRUM)


參考：[Testing UI for a Single App](https://developer.android.com/training/testing/ui-testing/espresso-testing.html)

