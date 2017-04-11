package com.ziwenwen.eventsimulation;

import android.hardware.input.InputManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_simulate)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cmd = translate(COMMANDs);
                        simulate(cmd);
                    }
                });

        findViewById(R.id.btn_simulate_2)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cmd = translate(COMMANDs2);
                        simulate(cmd);
                    }
                });
    }

    Process process;
    private void simulate(String cmd) {
        try {
            if (process == null) {
                process = Runtime.getRuntime().exec("su");
            }
            OutputStream outputStream = process.getOutputStream();
            outputStream.write(cmd.getBytes());
            outputStream.flush();
            outputStream.close();
            System.out.print(loadStream(process.getInputStream()));
            System.err.print(loadStream(process.getErrorStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String loadStream(InputStream in) throws IOException {
        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();
        while ((ptr = in.read()) != -1) {
            buffer.append((char) ptr);
        }
        return buffer.toString();
    }

    private String translate(String commanDs) {
        StringBuilder builder = new StringBuilder();
        String[] rawCmds = commanDs.split("\n");
        for (String rawCmd : rawCmds) {
            String[] values = rawCmd.replace(":", "").split(" ");
            BigInteger type = new BigInteger(values[1], 16);
            BigInteger code = new BigInteger(values[2], 16);
            BigInteger value = new BigInteger(values[3], 16);
            Log.d(TAG, "translate raw: " + rawCmd + "  output:" + type + "   " + code + "   " + value);
            if(builder.length() != 0) {
                builder.append(";");
            }
            builder.append("sendevent")
                    .append(" ")
                    .append(values[0])
                    .append(" ")
                    .append(type)
                    .append(" ")
                    .append(code)
                    .append(" ")
                    .append(value);
        }
        String result = builder.toString();
        Log.d(TAG, "translate result:" + result);
        return result;
    }


    private static String COMMANDs = "/dev/input/event1: 0003 0039 00000004\n" +
            "/dev/input/event1: 0003 0035 000000a0\n" +
            "/dev/input/event1: 0003 0036 0000051a\n" +
            "/dev/input/event1: 0003 003a 00000031\n" +
            "/dev/input/event1: 0003 0030 00000005\n" +
            "/dev/input/event1: 0000 0000 00000000\n" +
            "/dev/input/event1: 0003 0039 ffffffff\n" +
            "/dev/input/event1: 0000 0000 00000000\n" +
            "\n";

    private static String COMMANDs2 = "/dev/input/event2: 0001 0072 00000001\n" +
            "/dev/input/event2: 0000 0000 00000000\n" +
            "/dev/input/event2: 0001 0072 00000000\n" +
            "/dev/input/event2: 0000 0000 00000000";
}
