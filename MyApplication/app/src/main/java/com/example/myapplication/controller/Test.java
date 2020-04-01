package com.example.myapplication.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class Test extends AppCompatActivity {

    public TextView greetingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(">>> Test.onCreate() : init layout !");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        greetingTextView = findViewById(R.id.test_txt_greeting);
        greetingTextView.setText(".....");
//        String greetingText = "prout";
//        for (int period = 5; period > 0; period--) {
//            greetingText = "Application will start in " + period + " seconds ...";
//            if (greetingTextView != null)
//                greetingTextView.setText(greetingText);
//            System.out.println(">>> " + greetingText);
//            try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
//        }
    }

    @Override
    protected void onStart() {
        System.out.println(">>> Test.onStart() .... do nothing ....");
        super.onStart();
    }

    @Override
    protected void onResume() {
        System.out.println(">>> Test.onResume() : start runnable");
        super.onResume();
        //Handler greetingHandler = new Handler();
        //greetingHandler.postDelayed(new Runnable() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println(">>> Test.handler.run() : start main !");
                main();
            }
        }, 2000);
    }

    public void main() {
        //while (true) {
        System.out.println(">>> Test.main() : Hello World!");
        String greetingText = "prout";
        // sleep 3 sec
        for (int period = 5; period > 0; period--) {
            greetingText = "Application will start in " + period + " seconds ...";
            //if (greetingTextView != null)
//                this.setVisible(false);
                Test.this.greetingTextView.setText(greetingText);
            //else
                System.out.println(">>> " + greetingText + " ("+greetingTextView.getText()+")");
//                this.setVisible(true);
            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
//            new Handler().post(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println(">>> Test.main().handler().run() : sleep 1000 ms !");
//                    try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
//                }
//            });
        }
        //}

        System.out.println(">>> Test.main() : Start Main ....");
        // start Game
        Intent mainActivity = new Intent(this, MainActivity.class);
        //startActivity(gameActivity);
        startActivityForResult(mainActivity, 0);
    }
}
