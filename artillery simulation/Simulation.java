//=============================================================================================================================================================

import java.util.Scanner;

/**
 * Defines the simulation.
 *
 * Written By Cody Large (OTHER CLASSES PROVIDED BY PROFESSOR)
 */
public class Simulation
{
   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   public static void main(final String[] arguments)
   {
      Simulation simulation = new Simulation();
      Scanner sc = new Scanner(System.in);
      System.out.print("Enter the test number: ");
      int testNum = sc.nextInt();
      simulation.doTest(testNum);
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   private void doTest(int testNum)
   {
      switch (testNum)
      {
         // Test Calculate Elevation method
         case 0:
            System.out.println("FINAL ELEVATION: " + calculateReqElevation(500, 5000, 5));
            break;
         case 1:
            int initialVelocity = 500, expectedX = 5000, elevation = 5;
            System.out.println("\nRequired elevation to hit target at x = " + expectedX + " given initial velocity of " + initialVelocity + " is: " + calculateReqElevation(initialVelocity, expectedX, elevation) ); // 1A: When the time step is 0.01, the average_error is ~-0.8
            System.out.println("\nTime step variation: 0.1");
            runArtilleryA(4);
            System.out.println("\nTime step variation: 0.001");
            runArtilleryA(5);
            break;

         case 2:
            System.out.println("\nElevation variation: 0.5");
            runArtilleryB(1);
            System.out.println("\nInitial velocity variation: 6.5");
            runArtilleryB(2);
            break;

         case 3:
            System.out.println("\nX velocity variation: 4.5");
            runArtilleryC(1);
            System.out.println("\nY velocity variation: 1.6");
            runArtilleryC(2);
            break;

         case 4:
            // Deterministic solution has predictable error, nondeterministic changes with seed.
            System.out.println("\nDeterministic solution: ");
            runArtilleryD(true);
            System.out.println("\nNondeterministic solution: ");
            runArtilleryD(false);
            break;

         case 5:
            /* 1: Positive wind value Error 158
               2: Negative wind value Error -160 */
            System.out.println("\nWind going with projectile: ");
            runArtilleryE(1);
            System.out.println("\nWind going against projectile: ");
            runArtilleryE(2);
            break;

         case 6:
            /* Altitude    Speed
              1  500         300
              2  500         600
              3  1000        300
              4  1000        600 */
            System.out.println("\nAltitude 500, Speed 300: ");
            runBomberA(1);

            System.out.println("\nAltitude 500, Speed 600: ");
            runBomberA(2);

            System.out.println("\nAltitude 1000, Speed 300: ");
            runBomberA(3);

            System.out.println("\nAltitude 1000, Speed 600: ");
            runBomberA(4);
            break;
         case 7:
            /* VarSpeed    VarAltitude
              1  22             0
              2  0             65 */
            System.out.println("\nVelocity variation 22: ");
            runBomberB(1);
            System.out.println("\nAltitude variation 65: ");
            runBomberB(2);
            break;
         case 8:
              /* VarVX    VarVY
              1   4         0
              2   0         2 */
            System.out.println("\nX velocity variation 4: ");
            runBomberC(1);
            System.out.println("\nY velocity variation 2: ");
            runBomberC(2);
            break;
         case 9:
            System.out.println("\nDeterministic solution: ");
            runBomberD(true);
            System.out.println("\nNondeterministic solution: ");
            runBomberD(false);
            break;
         default:
            throw new RuntimeException("bad test "+ testNum);
      }
   }
   
   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   public Simulation()
   {

   }

   // Elevation should always start at 0
   private double calculateReqElevation(int initialVelocity, int expectedX, double elevation)
   {
      if(elevation < 5) throw new IllegalArgumentException("Elevation must be at least 5 for accurate results");

      boolean isDone = false;
      Artillery test = new Artillery(elevation, initialVelocity, 69, 0, 0, 0, 0, 0, 0, 0, false);

      while (!isDone)
      {
         isDone = test.update(0.1);
         //System.out.println("x: " + test.getX());
         //System.out.println("y: " + test.getY());
      }

      double finalX = test.getX();

      //double finalY = test.getY() - targetHeightDiff;

      if(finalX < expectedX)
         return calculateReqElevation(initialVelocity, expectedX, ++elevation);
      else
         return elevation;
   }

   private void runArtilleryA(int testNum)
   {
      double timeStep = switch (testNum)
      {
          case 1 -> 0.01;
          case 4 -> 0.1;
          case 5 -> 0.001;
          default -> 0.01;
      };

       // runs 1000 tests
      int expectedX = 5000;
      int numRuns = 1;
      double sumX = 0;

      int initialVelocity = 500;
      double elevation = calculateReqElevation(initialVelocity, expectedX, 5);

      //System.out.println("run,range");

      for (int iRun = 1; iRun <= numRuns; iRun++)
      {
         Artillery artillery = new Artillery(elevation, initialVelocity, iRun, 0, 0, 0, 0, 0, 0, 0, false);

         boolean isDone = false;

         while (!isDone)
         {
            isDone = artillery.update(timeStep);
            //System.out.println("x: " + artillery.getX());
            //System.out.println("y: " + artillery.getY());
         }

         double finalX = artillery.getX();

         sumX += finalX;
      }

      double average = (sumX / (double) numRuns);
      double averageError = (average - expectedX);

      System.out.println("average       = "+ average);
      System.out.println("average_error = "+ averageError);
   }

   private void runArtilleryB(int testNum)
   {
      double hitsInDistance = 0;
      double MAX_CIRCULAR_ERROR_PROB = 100;

      // runs 1000 tests
      int expectedX = 5000;
      int numRuns = 1000;
      int initialVelocity = 500;

      double timeStep = 0.01;
      double sumX = 0;
      double elevation = calculateReqElevation(initialVelocity, expectedX, 5);
      //System.out.println("run,range");

      for (int iRun = 1; iRun <= numRuns; iRun++)
      {
         Artillery artillery = switch (testNum)
         {
             case 1 -> new Artillery(elevation, initialVelocity, iRun, .5, 0, 0, 0, 0, 0, 0, false);
             case 2 -> new Artillery(elevation, initialVelocity, iRun, 0, 6.5, 0, 0, 0, 0, 0, false);
             default -> new Artillery(elevation, initialVelocity, iRun, 0, 0, 0, 0, 0, 0, 0, false);
         };


         boolean isDone = false;

         while (!isDone)
         {
            isDone = artillery.update(timeStep);
            //System.out.println("x: " + artillery.getX());
            //System.out.println("y: " + artillery.getY());
         }

         double finalX = artillery.getX();

         if(!(finalX > expectedX + MAX_CIRCULAR_ERROR_PROB || finalX < expectedX - MAX_CIRCULAR_ERROR_PROB)) // if the finalX is INSIDE of the expected range
            hitsInDistance++;


         //System.out.println(iRun + "," + finalX);

         sumX += finalX;
      }



      double CEP = hitsInDistance/numRuns;
      double average = (sumX / (double) numRuns);
      double averageError = (average - expectedX);

      System.out.println("average_error = "+ averageError);
      System.out.println("average       = "+ average);
      System.out.println("CEP = " + CEP); // Satsified: CEP(d) = (num_hits_within_distance_d / num_shots) >= 0.5
   }

   private void runArtilleryC(int testNum)
   {
      double hitsInDistance = 0;
      double MAX_CIRCULAR_ERROR_PROB = 100;

      // runs 1000 tests
      int expectedX = 5000;
      int numRuns = 1000;
      int initialVelocity = 500;

      double timeStep = 0.01;
      double sumX = 0;
      double elevation = calculateReqElevation(initialVelocity, expectedX, 5);

      for (int iRun = 1; iRun <= numRuns; iRun++)
      {
         Artillery artillery = switch (testNum)
         {
            case 1 -> new Artillery(elevation, initialVelocity, iRun, 0, 0, 4.5, 0, 0, 0, 0, false);
            case 2 -> new Artillery(elevation, initialVelocity, iRun, 0, 0, 0, 1.6, 0, 0, 0, false);
            default -> new Artillery(elevation, initialVelocity, iRun, 0, 0, 0, 0, 0, 0, 0, false);
         };

         boolean isDone = false;

         while (!isDone)
         {
            isDone = artillery.update(timeStep);
            //System.out.println("x: " + artillery.getX());
            //System.out.println("y: " + artillery.getY());
         }

         double finalX = artillery.getX();

         if(!(finalX > expectedX + MAX_CIRCULAR_ERROR_PROB || finalX < expectedX - MAX_CIRCULAR_ERROR_PROB)) // if the finalX is INSIDE of the expected range
            hitsInDistance++;

         sumX += finalX;
      }

      double CEP = hitsInDistance/numRuns;
      double average = (sumX / (double) numRuns);
      double averageError = (average - expectedX);

      System.out.println("average       = "+ average);
      System.out.println("average_error = "+ averageError);
      System.out.println("CEP = " + CEP); // Satsified: CEP(d) = (num_hits_within_distance_d / num_shots) >= 0.5
   }

   private void runArtilleryD(boolean deterministic)
   {
      // runs 1000 tests
      int expectedX = 5000;
      int numRuns = 1000;
      double sumX = 0;
      double timeStep = 0.01;

      int initialVelocity = 500;

      //System.out.println("run,range");

      for (int iRun = 1; iRun <= numRuns; iRun++)
      {
         Artillery artillery;

         if(deterministic)
            artillery = new Artillery(45, initialVelocity, iRun, 0, 0, 0, 0, 0, 0, 0, false);
         else
            artillery = new Artillery(45, initialVelocity, iRun+1, 2, 2, 2, 2, 0, 0, 0, false);

         boolean isDone = false;

         while (!isDone)
         {
            isDone = artillery.update(timeStep);
         }

         double finalX = artillery.getX();

         sumX += finalX;
      }

      double average = (sumX / (double) numRuns);
      double averageError = (average - expectedX);

      System.out.println("average       = "+ average);
      System.out.println("average_error = "+ averageError);
   }

   private void runArtilleryE(int testNum)
   {
      double timeStep = 0.01;
      // runs 1000 tests
      int expectedX = 5000;
      int numRuns = 1;
      double sumX = 0;

      int initialVelocity = 500;
      double elevation = calculateReqElevation(initialVelocity, expectedX, 5);

      //System.out.println("run,range");

      for (int iRun = 1; iRun <= numRuns; iRun++)
      {
         // Negative wind speed means wind is blowing AGAINST the munition
         Artillery artillery = switch(testNum)
         {
            case 1 -> new Artillery(elevation, initialVelocity, iRun, 0, 0, 0, 0, 0, 15, 15, false);
            case 2 -> new Artillery(elevation, initialVelocity, iRun, 0, 0, 0, 0, 0, -15, -15, false);
            default -> new Artillery(elevation, initialVelocity, iRun, 0, 0, 0, 0, 0, 0, 0, false);
         };

         boolean isDone = false;

         while (!isDone)
         {
            isDone = artillery.update(timeStep);
            //System.out.println(artillery.getX());
            System.out.println(artillery.getY());
         }

         double finalX = artillery.getX();

         sumX += finalX;
      }

      double average = (sumX / (double) numRuns);
      double averageError = (average - expectedX);

      System.out.println("average       = "+ average);
      System.out.println("average_error = "+ averageError);
   }

   private void runBomberA(int testNum)
   {
      double timeStep = 0.01;

      int expectedX = 5000;
      int numRuns = 1;
      double sumX = 0;
      int initialVelocity = 500;

      for (int iRun = 1; iRun <= numRuns; iRun++)
      {
         Bomber bomber = switch(testNum)
         {
            case 1 -> new Bomber(500, 300, iRun, 0, 0, 0, 0, false);
            case 2 -> new Bomber(500, 600, iRun, 0, 0, 0, 0, false);
            case 3 -> new Bomber(1000, 300, iRun, 0, 0, 0, 0, false);
            case 4 -> new Bomber(1000, 600, iRun, 0, 0, 0, 0, false);
            default -> new Bomber(500, 500, iRun, 0, 0, 0, 0, false);
         };

         boolean isDone = false;

         while (!isDone) {
            isDone = bomber.update(timeStep);
            //System.out.println(bomber.getBombX());
            //System.out.println(bomber.getBombY());
         }

         double finalX = bomber.getBombX();
         //System.out.println("Final x: " + finalX);

         sumX += finalX;
      }

      double average = (sumX / (double) numRuns);
      double averageError = (average - expectedX);

      System.out.println("average       = "+ average);
      System.out.println("average_error = "+ averageError);
   }

   private void runBomberB(int testNum)
   {
      double hitsInDistance = 0;
      double MAX_CIRCULAR_ERROR_PROB = 100;

      double timeStep = 0.01;

      int expectedX = 3945;
      int numRuns = 10;
      double sumX = 0;
      int initialVelocity = 500;
      int altitude = 1000;

      for (int iRun = 1; iRun <= numRuns; iRun++)
      {
         Bomber bomber = switch(testNum)
         {
            case 1 -> new Bomber(altitude, initialVelocity, iRun, 0, 22, 0, 0, false);
            case 2 -> new Bomber(altitude, initialVelocity, iRun, 65, 0, 0, 0, false);
            default -> new Bomber(altitude, initialVelocity, iRun, 0, 0, 0, 0, false);
         };

         boolean isDone = false;

         while (!isDone)
         {
            isDone = bomber.update(timeStep);
            //System.out.print("x: " + (int) bomber.getBombX());
            //System.out.println("\ty: " + (int) bomber.getBombY());
         }

         double finalX = bomber.getBombX();
         //System.out.println("Final x: " + finalX);

         if(!(finalX > expectedX + MAX_CIRCULAR_ERROR_PROB || finalX < expectedX - MAX_CIRCULAR_ERROR_PROB)) // if the finalX is INSIDE of the expected range
            hitsInDistance++;

         sumX += finalX;
      }

      double CEP = hitsInDistance/numRuns;
      double average = (sumX / (double) numRuns);
      double averageError = (average - expectedX);

      System.out.println("average       = "+ average);
      System.out.println("average_error = "+ averageError);
      System.out.println("CEP = " + CEP);
   }

   private void runBomberC(int testNum)
   {
      double hitsInDistance = 0;
      double MAX_CIRCULAR_ERROR_PROB = 100;

      double timeStep = 0.01;

      int expectedX = 3945;
      int numRuns = 10;
      double sumX = 0;
      int initialVelocity = 500;
      int altitude = 1000;

      for (int iRun = 1; iRun <= numRuns; iRun++)
      {
         Bomber bomber = switch(testNum)
         {
            case 1 -> new Bomber(altitude, initialVelocity, iRun, 0, 0, 4, 0, false);
            case 2 -> new Bomber(altitude, initialVelocity, iRun, 0, 0, 0, 2, false);
            default -> new Bomber(altitude, initialVelocity, iRun, 0, 0, 0, 0, false);
         };

         boolean isDone = false;

         while (!isDone)
         {
            isDone = bomber.update(timeStep);
            //System.out.print("x: " + (int) bomber.getBombX());
            //System.out.println("\ty: " + (int) bomber.getBombY());
         }

         double finalX = bomber.getBombX();
         //System.out.println("Final x: " + finalX);

         if(!(finalX > expectedX + MAX_CIRCULAR_ERROR_PROB || finalX < expectedX - MAX_CIRCULAR_ERROR_PROB)) // if the finalX is INSIDE of the expected range
            hitsInDistance++;

         sumX += finalX;
      }

      double CEP = hitsInDistance/numRuns;
      double average = (sumX / (double) numRuns);
      double averageError = (average - expectedX);

      System.out.println("average       = "+ average);
      System.out.println("average_error = "+ averageError);
      System.out.println("CEP = " + CEP);
   }

   private void runBomberD(boolean deterministic)
   {
      double timeStep = 0.01;

      int expectedX = 5000;
      int numRuns = 1000;
      double sumX = 0;
      int initialVelocity = 450;
      int altitude = 1200;

      for (int iRun = 1; iRun <= numRuns; iRun++)
      {
         Bomber bomber = (deterministic)
                 ? new Bomber(altitude, initialVelocity, iRun, 0, 0, 0, 0, false)
                 : new Bomber(altitude, initialVelocity, iRun+1, 5, 5, 5, 5, false);

         boolean isDone = false;

         while (!isDone) {
            isDone = bomber.update(timeStep);
            //System.out.print("x: " + (int) bomber.getBombX());
            //System.out.println("\ty: " + (int) bomber.getBombY());
         }

         double finalX = bomber.getBombX();
         //System.out.println("Final x: " + finalX);

         sumX += finalX;
      }

      double average = (sumX / (double) numRuns);
      double averageError = (average - expectedX);

      System.out.println("average       = "+ average);
      System.out.println("average_error = "+ averageError);
   }


   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   private void doTestArtillery1()
   {
      // runs 1000 tests
      double expectedX = 1348.7351190199681;

      int numRuns = 1000;

      double timeStep = 0.01;

      double sumX = 0;
      
      System.out.println("run,range,y");

      for (int iRun = 1; iRun <= numRuns; iRun++)
      {
         Artillery artillery = new Artillery(85, 500, iRun, 0, 0, 0, 0, 0, 0, 0, false);

         boolean isDone = false;

         while (!isDone)
         {
            isDone = artillery.update(timeStep);
         }
         
         double finalX = artillery.getX(); 

         System.out.println(iRun + "," + finalX);
         
         sumX += finalX;
      }
      
      double average = (sumX / (double) numRuns);
      double averageError = (average - expectedX);
      
      System.out.println("average       = "+ average);
      System.out.println("average_error = "+ averageError);
   }

   // ---------------------------------------------------------------------------------------------------------------------------------------------------------
   private void doTestBomber1()
   {
      double timeStep = 0.01;

      Bomber bomber = new Bomber(500, 500, 0, 0, 0, 0, 0, true);

      boolean isDone = false;

      while (!isDone)
      {
         isDone = bomber.update(timeStep);
      }
   }
}
