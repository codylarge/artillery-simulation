import java.util.Random;

//=============================================================================================================================================================
/**
 * Defines the bomber model.
 *
 * DO NOT CHANGE ANYTHING HERE (WRITTEN BY PROFESSOR)
 */
public class Bomber
{
   private static final double G = 32.2;

   private double _time = 0;

   private double _bomberX;
   private double _bomberY;

   private final double _bomberSpeed;

   private double _bombX;
   private double _bombY;

   private final Random _random;

   private final double _variationBombVelocityX;
   private final double _variationBombVelocityY;

   private final boolean _isOutputEnabled;

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   /**
    * Creates a bomber with variation.
    *
    * @param bomberAltitude - the initial altitude in feet
    * @param bomberSpeed - the initial speed in feet per second
    * @param randomSeed - the random seed for the variations
    * @param variationBomberAltitude - the variation in bomber altitude per time step
    * @param variationBomberSpeed - the variation in speed altitude per time step
    * @param variationBombVelocityX - the variation in bomb x velocity per time step
    * @param variationBombVelocityY - the variation in bomb y velocity per time step
    * @param isOutputEnabled - whether to write the output to standard output
    */
   public Bomber(final double bomberAltitude,
                 final double bomberSpeed,
                 final int randomSeed,
                 final double variationBomberAltitude,
                 final double variationBomberSpeed,
                 final double variationBombVelocityX,
                 final double variationBombVelocityY,
                 final boolean isOutputEnabled)
   {
      _random = new Random(randomSeed);

      _bomberX = 0;
      _bomberY = (bomberAltitude + (_random.nextGaussian() * variationBomberAltitude));
      _bomberSpeed = (bomberSpeed + (_random.nextGaussian() * variationBomberSpeed));

      _bombX = _bomberX;
      _bombY = _bomberY;

      _variationBombVelocityX = variationBombVelocityX;
      _variationBombVelocityY = variationBombVelocityY;

      _isOutputEnabled = isOutputEnabled;

      if (_isOutputEnabled)
      {
         System.out.println("time,bomber_x,bomber_y,bomber_error_x,bomber_error_y,bomb_x,bomb_y,bomb_error_x,bomb_error_y");
      }
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   /**
    * Gets the x position of the bomber.
    *
    * @return the position
    */
   public double getBomberX()
   {
      return _bomberX;
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   /**
    * Gets the y position of the bomber.
    *
    * @return the position
    */
   public double getBomberY()
   {
      return _bomberY;
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   /**
    * Gets the x position of the bomb.
    *
    * @return the position
    */
   public double getBombX()
   {
      return _bombX;
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   /**
    * Gets the y position of the bomb.
    *
    * @return the position
    */
   public double getBombY()
   {
      return _bombY;
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   /**
    * Gets the elapsed time of updates.
    *
    * @return the time
    */
   public double getTime()
   {
      return _time;
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   /**
    * Updates the bomber and bomb.
    *
    * @param timeStep - the number of seconds since the previous update
    *
    * @return whether the bomb is at or below ground level (0 feet)
    */
   public boolean update(final double timeStep)
   {
      // update the bomber
      double bomberVelocityX = _bomberSpeed;
      double bomberVelocityY = 0;

      _bomberX += (bomberVelocityX * timeStep);
      _bomberY += (bomberVelocityY * timeStep);

      // update the bomb
      double bombVelocityX = _bomberSpeed;
      double bombVelocityY = (-G * _time);

      double bombErrorX = (_random.nextGaussian() * _variationBombVelocityX);
      double bombErrorY = (_random.nextGaussian() * _variationBombVelocityY);

      if (_isOutputEnabled)
      {
         System.out.println(_time + "," + _bomberX + "," + _bomberY + "," + _bombX + "," + _bombY + "," + bombErrorX + "," + bombErrorY);
      }

      _bombX += ((bombVelocityX * timeStep) + bombErrorX);
      _bombY += ((bombVelocityY * timeStep) + bombErrorY);

      _time += timeStep;

      boolean isAtGround = (_bombY <= 0);

      return isAtGround;
   }
}
