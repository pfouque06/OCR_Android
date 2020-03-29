package com.example.myapplication.test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class Test extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main();
    }

    public void main() {
        //while (true) {
            System.out.println(">>> Test.main() : Hello World!");
            // sleep 2 sec
            try { Thread.sleep(3000); }
            catch (InterruptedException e) { e.printStackTrace(); }
        //}

        // start Game
        Intent mainActivity = new Intent(this, MainActivity.class);
        //startActivity(gameActivity);
        startActivityForResult(mainActivity, 0);
    }
}
