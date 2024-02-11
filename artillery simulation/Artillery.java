import java.util.Random;

//=============================================================================================================================================================
/**
 * Defines the artillery model.
 *
 * DO NOT CHANGE ANYTHING HERE (WRITTEN BY PROFESSOR)
 */
public class Artillery
{
   private static final double G = 32.2;

   private double _time = 0;

   private double _x = 0;
   private double _y = 0;

   private final double _elevation;

   private final double _initialVelocity;

   private final Random _random;

   private final double _variationVelocityX;
   private final double _variationVelocityY;

   private final double _windLayerAltitude;
   private final double _windSpeedLayerUpper;
   private final double _windSpeedLayerLower;

   private boolean _hasFuzed = false;

   private final boolean _isOutputEnabled;

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   /**
    * Creates an artillery munition with variation and wind.
    *
    * @param elevation - the barrel elevation in degrees (0 level, positive up)
    * @param initialVelocity - the initial velocity of the munition in feet per second
    * @param randomSeed - the random seed for the variations
    * @param variationElevation - the variation in barrel elevation
    * @param variationInitialVelocity - the variation in munition initial velocity
    * @param variationVelocityX - the variation in munition x velocity per time step
    * @param variationVelocityY - the variation in munition y velocity per time step
    * @param windLayerAltitude - the altitude dividing the lower and upper wind layers; use 0 for no wind
    * @param windSpeedLayerUpper - the wind speed in feet per second in the upper layer; positive is right; use 0 for no wind
    * @param windSpeedLayerLower - the wind speed in feet per second in the lower layer; positive is right ; use 0 for no wind
    * @param isOutputEnabled - whether to write the output to standard output
    */
   public Artillery(final double elevation,
                    final double initialVelocity,
                    final int randomSeed,
                    final double variationElevation,
                    final double variationInitialVelocity,
                    final double variationVelocityX,
                    final double variationVelocityY,
                    final double windLayerAltitude,
                    final double windSpeedLayerUpper,
                    final double windSpeedLayerLower,
                    final boolean isOutputEnabled)
   {
      _random = new Random(randomSeed);

      _elevation = Math.toRadians(elevation + (_random.nextGaussian() * variationElevation));

      _initialVelocity = (initialVelocity + (_random.nextGaussian() * variationInitialVelocity));

      _variationVelocityX = variationVelocityX;
      _variationVelocityY = variationVelocityY;

      _windLayerAltitude = windLayerAltitude;
      _windSpeedLayerUpper = windSpeedLayerUpper;
      _windSpeedLayerLower = windSpeedLayerLower;

      _isOutputEnabled = isOutputEnabled;

      if (_isOutputEnabled)
      {
         System.out.println("time,x,y,velocity_x,velocity_y,error_velocity_x,error_velocity_y");
      }
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
    * Gets the x position of the munition.
    *
    * @return the position
    */
   public double getX()
   {
      return _x;
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   /**
    * Gets the y position of the munition.
    *
    * @return the position
    */
   public double getY()
   {
      return _y;
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   /**
    * Updates the munition.
    *
    * @param timeStep - the number of seconds since the previous update
    *
    * @return whether the munition is at or below ground level (0 feet)
    */
   public boolean update(final double timeStep)
   {
      // update the munition
      double velocityX = (_initialVelocity * Math.cos(_elevation));
      double velocityY = ((_initialVelocity * Math.sin(_elevation)) - (G * _time));

      double errorX = (_random.nextGaussian() * _variationVelocityX);
      double errorY = (_random.nextGaussian() * _variationVelocityY);

      if (_isOutputEnabled)
      {
         System.out.println(_time + "," + _x + "," + _y + "," + velocityX + "," + velocityY + "," + errorX + "," + errorY);
      }

      _x += ((velocityX * timeStep) + errorX);
      _y += ((velocityY * timeStep) + errorY);

      // factor in any wind
      if (_y > _windLayerAltitude)
      {
         _x += (_windSpeedLayerUpper * timeStep);
      }
      else
      {
         _x += (_windSpeedLayerLower * timeStep);
      }

      _time += timeStep;

      // hack to prevent negative errors from going underground at launch
      if (_y > 20)
      {
         _hasFuzed = true;
      }

      boolean isAtGround = ((_y <= 0) && _hasFuzed);

      return isAtGround;
   }
}
