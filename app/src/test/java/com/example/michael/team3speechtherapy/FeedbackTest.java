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
}
