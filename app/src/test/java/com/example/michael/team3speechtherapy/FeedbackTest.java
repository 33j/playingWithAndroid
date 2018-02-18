package com.example.michael.team3speechtherapy;


import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by j on 2/16/18.
 */

public class FeedbackTest  {

    @Test
    public void scoreTest () throws Exception{
       double actual_value= Feedback.score(3,4, "M", "ee");
        assertEquals(5.0,actual_value,0);

    }
    @Test
    public void visualTest() throws  Exception{
        String[] expected_value= new String[]{"Open mouth wider and lower tongue.","Advance tongue more."};


        String[] actual_value= Feedback.simluationFeedback(220,2290,"M","ee");
        System.out.println(actual_value[0]);
        assertEquals(expected_value[0],actual_value[0]);
        assertEquals(expected_value[1],actual_value[1]);

    }

}
