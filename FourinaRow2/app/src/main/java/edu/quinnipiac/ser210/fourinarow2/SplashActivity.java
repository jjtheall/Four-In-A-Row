//@author: jackjtheall
//20 Feb 2022
//Four In A Row

package edu.quinnipiac.ser210.fourinarow2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    //grab text from playerName editText field and pass it in an explicit intent
    //to switch activities & display playerName on GameActivity
    public void onStartGame(View view){
        EditText playerName = (EditText)findViewById(R.id.playerName);
        String name = playerName.getText().toString();
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra(GameActivity.EXTRA_NAME, name);
        startActivity(intent);
    }

}