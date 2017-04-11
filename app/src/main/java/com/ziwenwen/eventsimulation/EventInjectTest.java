package com.ziwenwen.eventsimulation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EventInjectTest extends AppCompatActivity {

    private EditText etTarget;
    EventInject eventInject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_inject_text);
        etTarget = (EditText) findViewById(R.id.et_target);

        eventInject = new EventInject();
        eventInject.init();

        findViewById(R.id.btn_input_key)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etTarget.requestFocus();
                        etTarget.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                eventInject.inputKey(50);
                                eventInject.inputKey(66);
                            }
                        }, 800);
                    }
                });
        findViewById(R.id.btn_input_text)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etTarget.requestFocus();
                        etTarget.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                eventInject.inputText("input text");
                                eventInject.inputKey(66);
                            }
                        }, 800);
                    }
                });
        findViewById(R.id.btn_tap)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etTarget.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                eventInject.tap(600,600);
                            }
                        }, 2000);
                    }
                });
        findViewById(R.id.btn_swipe)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etTarget.post(new Runnable() {
                            @Override
                            public void run() {
                                eventInject.swipe(400, 800, 400, 200);
                            }
                        });
                    }
                });
        findViewById(R.id.btn_long_press)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etTarget.post(new Runnable() {
                            @Override
                            public void run() {
                                eventInject.longPress(300, 300, 2000);
                            }
                        });
                    }
                });

    }
}
