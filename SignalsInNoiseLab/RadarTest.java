

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class RadarTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class RadarTest
{
    /**
     * Default constructor for test class RadarTest
     */
    public RadarTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }       

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }
    
    @Test
    public void test()
    {
        Radar radar = new Radar(200, 200, 3, 2);
        radar.setNoiseFraction(0.005);
        for (int i=0; i<1000 && radar.monsterExists; i++)
        {
            radar.scanConstantVelocity();            
        }
        VelocityChangePos constantVelocity = radar.getConstantVelocity();
        assertEquals(3, constantVelocity.getDx(), 1e-6);
        assertEquals(2, constantVelocity.getDy(), 1e-6);
    }
}
