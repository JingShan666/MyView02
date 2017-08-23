package com.example.administrator.myview02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btn_start;
    private LofterImageView lofterImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lofterImageView= (LofterImageView) findViewById(R.id.my_view);
        btn_start= (Button) findViewById(R.id.start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lofterImageView.setPercent(0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 写子线程中的操作

                        for (int i=0;i<100;i++){
                            try {
                                Thread.sleep(50);
                                lofterImageView.setProgress(i);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();


                lofterImageView.setTotalProgress(99);
            }
        });
    }
}
