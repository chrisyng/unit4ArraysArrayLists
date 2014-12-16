
/**
 * The model for radar scan and accumulator
 * 
 * @author @gcschmit
 * @version 19 July 2014
 */
public class Radar
{

    // stores whether each cell triggered detection for the current scan of the radar
    private boolean[][] currentScan;
    // holds values for the previous scan
    private boolean[][] previousScan;

    // value of each cell is incremented for each scan in which that cell triggers detection 
    private int[][] accumulator;

    // accumulator that gets incrememted for each dx/dy value detected
    private int[][] constantVelocityAccumulator;

    // location of the monster
    private int monsterLocationRow;
    private int monsterLocationCol;

    // probability that a cell will trigger a false detection (must be >= 0 and < 1)
    private double noiseFraction;

    // number of scans of the radar since construction
    private int numScans;

    //velocity of the monster
    private int changeX;
    private int changeY;

    // keeps track of whether monster is in the scan or not
    public boolean monsterExists;

    /**
     * Constructor for static monster objects of class radar
     * @param   rows    the number of rows in the radar grid
     * @param   cols    the number of columns in the radar grid
     */
    public Radar(int rows, int cols)
    {
        // initialize instance variables
        currentScan = new boolean[rows][cols]; // elements will be set to false
        previousScan = currentScan;
        accumulator = new int[rows][cols]; // elements will be set to 0

        // randomly set the location of the monster (can be explicity set through the
        //  setMonsterLocation method
        monsterLocationRow = (int)(Math.random() * rows);
        monsterLocationCol = (int)(Math.random() * cols);

        // make the constantVelocityAccumulator big enough to store values of the monster in different

        noiseFraction = 0.05;
        numScans = 0;
        monsterExists = true;
    }

    /**
     * Constructor for objects of class radar with constant-velocity motion
     * 
     * @param   rows    the number of rows in the radar grid
     * @param   cols    the number of columns in the radar grid
     */
    public Radar(int rows, int cols, int changeX, int changeY)
    {
        // initialize instance variables
        currentScan = new boolean[rows][cols]; // elements will be set to false
        previousScan = new boolean[rows][cols];
        // zero previousScan
        for(int row = 0; row < previousScan.length; row++)
        {
            for(int col = 0; col < previousScan[0].length; col++)
            {
                previousScan[row][col] = false;                
            }
        }
        
        constantVelocityAccumulator = new int[11][11]; // holds change in x and change in y values--cannot exceed 5 pixels (includes 0, so size 6)

        // randomly set the location of the monster (can be explicity set through the
        //  setMonsterLocation method
        monsterLocationRow = (int)(Math.random() * rows);
        monsterLocationCol = (int)(Math.random() * cols);

        //set the constant-velocity motion of the monster
        this.changeX = changeX;
        this.changeY = changeY;

        // make the constantVelocityAccumulator big enough to store values of the monster in different 

        noiseFraction = 0.05;
        numScans= 0;
        monsterExists = true;
    }

    /**
     * Performs a scan of the radar. Noise is injected into the grid and the accumulator is updated.
     * 
     */
    public void scan()
    {
        // zero the current scan grid
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                currentScan[row][col] = false;
            }
        }

        // detect the monster
        currentScan[monsterLocationRow][monsterLocationCol] = true;

        // inject noise into the grid
        injectNoise();

        // udpate the accumulator
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                if(currentScan[row][col] == true)
                {
                    accumulator[row][col]++;
                }
            }
        }

        // keep track of the total number of scans
        numScans++;
    }

    /**
     * Performs a scan of the radar. Noise is injected into the grid and the accumulator is updated.
     * 
     */
    public void scanConstantVelocity()
    {
        // zero the current scan grid
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                currentScan[row][col] = false;                
            }
        }

        // detect the monster
        currentScan[monsterLocationRow][monsterLocationCol] = true;

        // inject noise into the grid
        injectNoise();

        // udpate the accumulator
       
        for(int row = 0; row < previousScan.length; row++)
        {
            for(int col = 0; col < previousScan[0].length; col++)
            {
                if(currentScan[row][col] == true)
                {
                    for(int row2 = 0; row2 < previousScan.length; row2++)
                    {
                        for(int col2 = 0; col2 < previousScan[0].length; col2++)
                        {
                            if(previousScan[row2][col2] == true)
                            {
                                int dx = col-col2;
                                int dy = row-row2;
                                if ((Math.abs(dx) <= 5 && Math.abs(dy) <=5) && dx!= 0 && dy !=0)
                                {
                                    this.constantVelocityAccumulator[dy+5][dx+5]++;
                                    /*
                                    System.out.println("dx: " + dx);
                                    System.out.println("dy: " + dy);
                                    */
                                }                                
                            }
                        }
                    }
                }
            }
        }        

        
        //make previousArray the current array for the next analaysis
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                previousScan[row][col] = currentScan[row][col];                
            }
        }
        
        
        // update the monster's location, but remove monster from the radar if it goes off the screen
        if (monsterLocationRow < currentScan.length-changeX && monsterLocationCol < currentScan[0].length-changeY)
        {
            this.setMonsterLocation(monsterLocationRow + changeX, monsterLocationCol + changeY);
        }
        else if (monsterLocationRow >= currentScan.length && monsterLocationCol >= currentScan[0].length)
        {
            currentScan[monsterLocationRow][monsterLocationCol] = false;
            monsterExists = false;            
        }
        // keep track of the total number of scans        
        numScans++;        
    }

    /**
     * Sets the location of the monster
     * 
     * @param   row     the row in which the monster is located
     * @param   col     the column in which the monster is located
     * @pre row and col must be within the bounds of the radar grid
     */
    public void setMonsterLocation(int row, int col)
    {
        // remember the row and col of the monster's location
        monsterLocationRow = row;
        monsterLocationCol = col;

        // update the radar grid to show that something was detected at the specified location
        currentScan[row][col] = true;
    }

    /**
     * Sets the probability that a given cell will generate a false detection
     * 
     * @param   fraction    the probability that a given cell will generate a flase detection expressed
     *                      as a fraction (must be >= 0 and < 1)
     */
    public void setNoiseFraction(double fraction)
    {
        noiseFraction = fraction;
    }

    /**
     * Returns true if the specified location in the radar grid triggered a detection.
     * 
     * @param   row     the row of the location to query for detection
     * @param   col     the column of the location to query for detection
     * @return true if the specified location in the radar grid triggered a detection
     */
    public boolean isDetected(int row, int col)
    {
        return currentScan[row][col];
    }

    /**
     * Returns the number of times that the specified location in the radar grid has triggered a
     *  detection since the constructor of the radar object.
     * 
     * @param   row     the row of the location to query for accumulated detections
     * @param   col     the column of the location to query for accumulated detections
     * @return the number of times that the specified location in the radar grid has
     *          triggered a detection since the constructor of the radar object
     */
    public int getAccumulatedDetection(int row, int col)
    {
        return accumulator[row][col];
    }

    /**
     * Returns the number of rows in the radar grid
     * 
     * @return the number of rows in the radar grid
     */
    public int getNumRows()
    {
        return currentScan.length;
    }

    /**
     * Returns the number of columns in the radar grid
     * 
     * @return the number of columns in the radar grid
     */
    public int getNumCols()
    {
        return currentScan[0].length;
    }

    /**
     * Returns the number of scans that have been performed since the radar object was constructed
     * 
     * @return the number of scans that have been performed since the radar object was constructed
     */
    public int getNumScans()
    {
        return numScans;
    }

    /**
     * Sets cells as falsely triggering detection based on the specified probability
     * 
     */
    private void injectNoise()
    {
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                // each cell has the specified probablily of being a false positive
                if(Math.random() < noiseFraction)
                {
                    currentScan[row][col] = true;
                }
            }
        }
    }

    public VelocityChangePos getConstantVelocity()
    {
        // variables to store the index value of the greatest accumulator value
        int i = 0;
        int j = 0;
        int maxValue = 0;
        for(int row = 0; row < constantVelocityAccumulator.length; row++)
        {
            for(int col = 0; col < constantVelocityAccumulator[0].length; col++)
            {
                if (constantVelocityAccumulator[row][col] > maxValue)
                {
                    maxValue = constantVelocityAccumulator[row][col];
                    i = row;
                    j = col;
                }
            }
        }
        System.out.println ("Dx: " + i + "Dy:" + j);
        VelocityChangePos velocityChange = new VelocityChangePos(i-5, j-5);
        return velocityChange;
    }
    
    public int[][] getCVAccumulator()
    {
        return this.constantVelocityAccumulator;
    }

}
