package com.demon.fit;

import com.demon.fit.learningK.CalculateResult;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    /**
     *
     */
    @Test
    public void learning_1(){
        List<String> list = new ArrayList<>();

        String result = CalculateResult.getResult(list);

        System.out.println(result);
    }
}