package com.good.hie.server;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
//        assertTrue( true );
        
    	System.out.println("测试1:");
        assertEquals(3, new App().sum(1,2));
        
        System.out.println("测试2:");
        assertEquals(6, new App().sum(1,5));
        
        System.out.println("测试3:");
        assertEquals(51, new App().sum(1,50));
        
        System.out.println("测试4:");
        assertEquals(11, new App().sum(5,6));
    }
    
}
