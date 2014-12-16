import javax.swing.JFrame;

/**
 * Class that contains the main method for the program and creates the frame containing the component.
 * 
 * @author @gcschmit
 * @version 19 July 2014
 */
public class RadarViewer
{
    /**
     * main method for the program which creates and configures the frame for the program
     *
     */
    public static void staticMonster() throws InterruptedException
    {
        // create the radar, set the monster location, and perform the initial scan
        final int ROWS = 100;
        final int COLS = 100;
        Radar radar = new Radar(ROWS, COLS);
        radar.setNoiseFraction(0.10);
        radar.scan();

        JFrame frame = new JFrame();

        frame.setTitle("Signals in Noise Lab");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // a frame contains a single component; create the radar component and add it to the frame
        RadarComponent component = new RadarComponent(radar);
        frame.add(component);

        // set the size of the frame to encompass the contained component
        frame.pack();

        // make the frame visible which will result in the paintComponent method being invoked on the
        //  component.
        frame.setVisible(true);

        // perform 100 scans of the radar wiht a slight pause between each
        // after each scan, instruct the Java Run-Time to redraw the window
        for(int i = 0; i < 100; i++)
        {
            Thread.sleep(100); // sleep 100 milliseconds (1/10 second)

            radar.scan();

            frame.repaint();
        }        

    }

    public static void constantVelocityMonster() throws InterruptedException
    {
        final int ROWS = 100;
        final int COLS = 100;
        Radar radar = new Radar(ROWS, COLS, 2, 2);
        radar.setNoiseFraction(0.01);
        radar.scanConstantVelocity();

        for(int i = 0; (i < 1000 && radar.monsterExists == true); i++)
        {
            radar.scanConstantVelocity();
        }
        
        for (int[] bleh : radar.getCVAccumulator())
        {
            for (int thing: bleh)
            {
                System.out.print(thing + " ");
            }
            System.out.print("\n");
        }

        VelocityChangePos constantVelocity = radar.getConstantVelocity();
        System.out.println("The dx and dy is: " + constantVelocity.getDx() + ", " + constantVelocity.getDy());
        
    }

}
