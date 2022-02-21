//@author: jackjtheall
//20 Feb 2022
//Four In A Row

package edu.quinnipiac.ser210.fourinarow2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "playerName";
    public static FourInARow FIRboard = new FourInARow();
    TextView winnerText;
    TextView whosTurn;
    boolean playing = true;
    Intent restartIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        winnerText = findViewById(R.id.winnerText);
        whosTurn = findViewById(R.id.whosTurn);
        restartIntent = getIntent();

        //grab playerName from intent and set playerName textView
        Intent intent = getIntent();
        String playerName = intent.getStringExtra(EXTRA_NAME);
        TextView textViewPlayerName = (TextView) findViewById(R.id.playerName);
        textViewPlayerName.setText(playerName);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
    }


    //when a board button is clicked, get the buttons tag and convert it to row and column number
    //then update FourInARow board object, disable the button and call computer move
    public void onSpaceClicked(View view) {
        if (playing) {
            Button clickedButton = findViewById(view.getId());
            //disable clicked button so that player cannot selected occupied space
            clickedButton.setEnabled(false);
            String clickedButtonTag = (String) clickedButton.getTag();
            int row = Integer.parseInt(clickedButtonTag.substring(1, 2));
            int col = Integer.parseInt(clickedButtonTag.substring(3));

            //convert row and col into one number, setMove() on board with IGame.BLUE & location
            int playerLocation = (6 * row) + col;
            FIRboard.setMove(IGame.BLUE, playerLocation);

            //check for winner, set winner text based on gameStatus
            int gameStatus = FIRboard.checkForWinner();
            switch (gameStatus) {
                case IGame.BLUE_WON:
                    playing=false;
                    winnerText.setText("You win!");
                    findViewById(R.id.reset).setEnabled(true);
                    break;
                case IGame.RED_WON:
                    playing=false;
                    winnerText.setText("You lose...");
                    findViewById(R.id.reset).setEnabled(true);
                    break;
                case IGame.TIE:
                    playing=false;
                    winnerText.setText("Tie game");
                    findViewById(R.id.reset).setEnabled(true);
                    break;
                default:
                    break;
            }

            //computer move
            if (gameStatus == IGame.PLAYING) {
                //set turn text to cpu's turn
                whosTurn.setText(R.string.cpuTurn);
                //gets cpu move and sets board
                int cpuMove = FIRboard.getComputerMove();
                FIRboard.setMove(IGame.RED, cpuMove);
                //disables button at cpu turn and changes background color to red
                int cpuRow = cpuMove / 6;
                int cpuCol = cpuMove % 6;
                int cpuButtonID = getResources().getIdentifier("r" + cpuRow + "c" + cpuCol, "id", getPackageName());
                Button toDisable = findViewById(cpuButtonID);
                toDisable.setEnabled(false);
                toDisable.setBackgroundColor(Color.RED);

            }

            //check for winner, set winner text based on gameStatus
            gameStatus = FIRboard.checkForWinner();
            switch (gameStatus) {
                case IGame.BLUE_WON:
                    playing=false;
                    winnerText.setText("You win!");
                    findViewById(R.id.reset).setEnabled(true);
                    break;
                case IGame.RED_WON:
                    playing=false;
                    winnerText.setText("You lose...");
                    findViewById(R.id.reset).setEnabled(true);
                    break;
                case IGame.TIE:
                    playing=false;
                    winnerText.setText("Tie game");
                    findViewById(R.id.reset).setEnabled(true);
                    break;
                default:
                    break;
            }
            //it is now players turn again, so we set whosTurn text back
            whosTurn.setText(R.string.playerTurn);
        }
    }

    //clear board, then restart activity using intent that started GameActivity
    public void onReset(View view) {
        FIRboard.clearBoard();
        finish();
        startActivity(restartIntent);
    }
}

