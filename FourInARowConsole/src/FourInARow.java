import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * TicTacToe class implements the interface
 * @author relkharboutly
 * @author jackjtheall
 * @date 2/12/2022
 */
public class FourInARow implements IGame {

	private static final int ROWS = 6, COLS = 6; // number of rows and columns
	private int[][] board = new int[ROWS][COLS]; // game board in 2D array
	private boolean cpuFirstTurn = true;
	Random rand = new Random();

	/**
	 * empty
	 */
	public FourInARow(){
		
	}

	//returns true if location is empty
	public boolean checkEmpty(int location){
		int row = location/6;
		int col = location%6;
		return (board[row][col] == EMPTY);
	}

	@Override
	public void clearBoard() {
		for(int r=0; r<ROWS; r++){
			for(int c=0; c<COLS; c++){
				board[r][c] = EMPTY;
			}
		}
	}

	@Override
	//take location and calculate row #,col #
	public void setMove(int player, int location) {
		int row = location/6;
		int col = location%6;
		board[row][col] = player;
	}

	@Override
	public int getComputerMove() {

		//if it is the cpu's first turn, place piece on random empty spot
		if(cpuFirstTurn){
			Random rand = new Random();
			int randRow;
			int randCol;
			int startingLoc;
			do{
				startingLoc = rand.nextInt(35);
				randRow = startingLoc/6;
				randCol = startingLoc%6;
			} while (board[randRow][randCol] != EMPTY);
			cpuFirstTurn = false;
			return startingLoc;
		}

		//rated board to track best moves for cpu
		int[][] rated = new int[ROWS][COLS];

		//interating through entire array, generating a new 2D array of rated board locations
		for(int r=0; r<ROWS; r++){
			for(int c=0; c<COLS; c++){
				if(board[r][c] == EMPTY){
					rated[r][c] += 1;
				}

				//if we find a red piece, check all adjacent tiles and keep going if we find more reds
				//each if statement before each while loop determines if it is possible to create a sequence
				//of 4 pieces in valid locations in the direction checked by the while loop
				//that way, the cpu will not prioritize "dead ends"
				if(board[r][c] == RED){
					//count
					int count=0;
					//checking upwards
					if(r-3 >= 0 || r-2 >= 0 && r+1 < ROWS || r-1 >= 0 && r+2 < ROWS){
						while(board[r-count][c] == RED && r-count > 0){
							count++;
							rated[r-count][c] += count;
						}
					}

					//checking downwards
					count=0;
					if(r+3 < ROWS || r+2 < ROWS && r-1 >= 0 || r+1 < ROWS && r-2 >= 0){
						while(board[r+count][c] == RED && r+count < ROWS-1){
							count++;
							rated[r+count][c] += count;
						}
					}

					//checking leftwards
					count=0;
					if(c-3 >= 0 || c-2 >= 0 && c+1 < COLS || c-1 >= 0 && c+2 < COLS){
						while(board[r][c-count] == RED && c-count > 0){
							count++;
							rated[r][c-count] += count;
						}
					}

					//checking rightwards
					count = 0;
					if(c+3 < COLS || c+2 < COLS && c-1 >= 0 || c+1 < COLS && c-2 >= 0){
						while(board[r][c+count] == RED && c+count < COLS-1){
							count++;
							rated[r][c+count] += count;
						}
					}

					//checking top left
					count = 0;
					if(r-3 >= 0 && c-3 >= 0 || (r-2 >= 0 && c-2 >= 0 && r+1 < ROWS && c+1 < COLS) || (r-1 >= 0 && c-1 >= 0 && r+2 < ROWS && c+2 < COLS)){
						while(board[r-count][c-count] == RED && r-count > 0 && c-count > 0){
							count++;
							rated[r-count][c-count] += count;
						}
					}

					//checking top right
					count=0;
					if(r-3>=0 && c+3<COLS || (r-2>=0 && c+2<COLS && r+1<ROWS && c-1>=0) || (r-1>=0 && c+1<COLS && r+2<ROWS && c-2>=0)){
						while(board[r-count][c+count] == RED && r-count > 0 && c+count < COLS-1){
							count++;
							rated[r-count][c+count] += count;
						}
					}

					//checking bot left
					count=0;
					if(r+3<ROWS && c-3>=0 || (r+2<ROWS && c-2>=0 && r-1>=0 && c+1<COLS) || (r+1<ROWS && c-1>=0 && r-2>=0 && c+2<COLS)){
						while(board[r+count][c-count] == RED && r+count < ROWS-1 && c-count > 0){
							count++;
							rated[r+count][c-count] += count;
						}
					}

					//checking bot right
					count=0;
					if(r+3<ROWS && c+3<COLS || (r+2<ROWS && c+2<COLS && r-1>=0 && c-1>=0) || (r+1<ROWS && c+1<COLS && r-2>=0 && c-2>=0)){
						while(board[r+count][c+count] == RED && r+count < ROWS-1 && c+count < COLS-1){
							count++;
							rated[r+count][c+count] += count;
						}
					}
				}

				//now we want to check for blues, prioritizing blocking moves over
				//"building" moves if the player has 2 or more in a row
				if(board[r][c] == BLUE){
					//count
					int count=0;
					//checking upwards
					while(board[r-count][c] == BLUE && r-count > 0){
						count++;
						if(count > 1) rated[r-count][c] += count;
					}
					//checking downwards
					count=0;
					while(board[r+count][c] == BLUE && r+count < ROWS-1){
						count++;
						if(count > 1) rated[r+count][c] += count;
					}
					//checking leftwards
					count=0;
					while(board[r][c-count] == BLUE && c-count > 0){
						count++;
						if(count > 1) rated[r][c-count] += count;
					}
					//checking rightwards
					count = 0;
					while(board[r][c+count] == BLUE && c+count < COLS-1){
						count++;
						if(count > 1) rated[r][c+count] += count;
					}
					//checking top left
					count = 0;
					while(board[r-count][c-count] == BLUE && r-count > 0 && c-count > 0){
						count++;
						if(count > 1) rated[r-count][c-count] += count;
					}
					//checking top right
					count=0;
					while(board[r-count][c+count] == BLUE && r-count > 0 && c+count < COLS-1){
						count++;
						if(count > 1) rated[r-count][c+count] += count;
					}
					//checking bot left
					count=0;
					while(board[r+count][c-count] == BLUE && r+count < ROWS-1 && c-count > 0){
						count++;
						if(count > 1) rated[r+count][c-count] += count;
					}
					//checking bot right
					count=0;
					while(board[r+count][c+count] == BLUE && r+count < ROWS-1 && c+count < COLS-1){
						count++;
						if(count > 1) rated[r+count][c+count] += count;
					}
				}
			}
		}

		//find locations with highest rating and add them to a list
		//also clear ratings on locations with pieces
		int max = 0;
		ArrayList<Integer> bestMoves = new ArrayList<>();

		for(int r=0; r<ROWS; r++){
			for(int c=0; c<COLS; c++){
				if(board[r][c] != EMPTY) rated[r][c] = 0;
				if(rated[r][c] > max) max = rated[r][c];
			}
		}

		//adding all locations with highest rating
		for(int r=0; r<ROWS; r++){
			for(int c=0; c<COLS; c++){
				if(rated[r][c] == max){
					bestMoves.add(r*6 + c);
				}
			}
		}

		//return a random selection from bestMoves
		return bestMoves.get(rand.nextInt(bestMoves.size()));

	}

	@Override
	public int checkForWinner() {
		//loop through board, checking if there are consecutive reds or blues
		int blues = 0;
		int reds = 0;

		//horizontal check
		for(int r=0; r<ROWS; r++){
			for(int c=0; c<COLS; c++){
				if(board[r][c] == BLUE) blues++;
				else blues = 0;
				if(board[r][c] == RED) reds++;
				else reds = 0;
				if(blues >= 4) return BLUE_WON;
				if(reds >= 4) return RED_WON;
			}
		}
		//vertical check
		for(int c=0; c<COLS; c++){
			for(int r=0; r<ROWS; r++){
				if(board[r][c] == BLUE) blues++;
				else blues = 0;
				if(board[r][c] == RED) reds++;
				else reds = 0;
				if(blues >= 4) return BLUE_WON;
				if(reds >= 4) return RED_WON;
			}
		}
		//diagonal check bot left to top right
		for(int r=0; r<ROWS*2; r++){
			for(int c=0; c<=r; c++){
				int i = r-c;
				if(i<ROWS && c<COLS){
					if(board[i][c] == BLUE) blues++;
					else blues = 0;
					if(board[i][c] == RED) reds++;
					else reds = 0;
					if(blues >= 4) return BLUE_WON;
					if(reds >= 4) return RED_WON;
				}
			}
		}
		//diagonal check top right to bot left
		for(int n = -ROWS; n<=ROWS; n++){
			for(int i=0; i<COLS; i++){
				if((i-n>=0)&&(i-n<ROWS)){
					if(board[i][i-n] == BLUE) blues++;
					else blues = 0;
					if(board[i][i-n] == RED) reds++;
					else reds = 0;
					if(blues >= 4) return BLUE_WON;
					if(reds >= 4) return RED_WON;
				}
			}
		}
		//check tie
		int empties = 0;
		for(int r=0; r<ROWS; r++){
			for(int c=0; c<COLS; c++) {
				if(board[r][c] == EMPTY) empties++;
			}
		}
		if(empties == 0) return TIE;
		//return 0 if no win and no tie
		return 0;
	}
	
	  /**
	   *  Print the game board 
	   */
	   public  void printBoard() {

	      for (int row = 0; row < ROWS; ++row) {
	         for (int col = 0; col < COLS; ++col) {
	            printCell(board[row][col]); // print each of the cells
	            if (col != COLS - 1) {
	               System.out.print("|");   // print vertical partition
	            }
	         }
	         System.out.println();
	         if (row != ROWS - 1) {
	            System.out.println("-----------------------"); // print horizontal partition
	         }
	      }
	      System.out.println(); 
	   }
	 
	   /**
	    * Print a cell with the specified "content" 
	    * @param content either BLUE, RED or EMPTY
	    */
	   public  void printCell(int content) {

	      switch (content) {
	         case EMPTY:  System.out.print("   "); break;
	         case BLUE: System.out.print(" B "); break;
	         case RED:  System.out.print(" R "); break;
	      }

	   }

}
