import java.util.Scanner;
/**
 * Four in a row: Two-player console, non-graphics
 * @author relkharboutly
 * @author jackjtheall
 * @date 1/22/2020
 */
public class FIRConsole  {

   public static Scanner sc = new Scanner(System.in); // the input Scanner

   public static FourInARow FIRboard = new FourInARow();


   /** The entry main method (the program starts here) */
   public static void main(String[] args) {

	   int currentState = FourInARow.PLAYING;
	   String userInput ="";
	   boolean invalidInput = false;

	   //game loop
	   do {
		   FIRboard.printBoard();

          //	1- accept user move
           do{
               if(invalidInput) System.out.println("Cell is occupied!");
               System.out.println("Enter move (0-35): ");
               userInput = sc.nextLine();
               if(!FIRboard.checkEmpty(Integer.parseInt(userInput))) invalidInput = true;
           } while(!FIRboard.checkEmpty(Integer.parseInt(userInput)));
           invalidInput = false;

           FIRboard.setMove(IGame.BLUE, Integer.parseInt(userInput));

           // check for winner between user move & cpu move
           int gameStatus = FIRboard.checkForWinner();

           if(gameStatus == IGame.TIE){
               FIRboard.printBoard();
               System.out.println("Tie game!");
               currentState = IGame.TIE;
           }
           else if(gameStatus == IGame.BLUE_WON){
               FIRboard.printBoard();
               System.out.println("You win!");
               currentState = IGame.BLUE_WON;
           }
           else if(gameStatus == IGame.RED_WON){
               FIRboard.printBoard();
               System.out.println("You lose!");
               currentState = IGame.RED_WON;
           }

           //if game is still playing, continue
           if(currentState == IGame.PLAYING){
               //    2- call getComputerMove
               int cpuMove = FIRboard.getComputerMove();
               FIRboard.setMove(IGame.RED, cpuMove);

               //    3- Check for winner again
               gameStatus = FIRboard.checkForWinner();

               //    4- Print game end messages in case of Win , Lose or Tie !
               if(gameStatus == IGame.TIE){
                   FIRboard.printBoard();
                   System.out.println("Tie game!");
                   currentState = IGame.TIE;
               }
               else if(gameStatus == IGame.BLUE_WON){
                   FIRboard.printBoard();
                   System.out.println("You win!");
                   currentState = IGame.BLUE_WON;
               }
               else if(gameStatus == IGame.RED_WON){
                   FIRboard.printBoard();
                   System.out.println("You lose!");
                   currentState = IGame.RED_WON;
               }
           }

      } while ((currentState == IGame.PLAYING) && (!userInput.equals("q"))); // repeat if not game-over

   }
 
     
}