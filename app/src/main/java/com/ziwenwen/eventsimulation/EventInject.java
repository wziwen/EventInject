package com.ziwenwen.eventsimulation;


import android.annotation.SuppressLint;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ziwen.wen on 2017/4/10.
 * 按键, 触摸事件注入
 */
@SuppressLint("DefaultLocale")
public class EventInject {
    private static final String TAG = "EventInject";
    private Process process;
    private OutputStream outputStream;

    public EventInject() {
    }

    public void init() {
        try {
            process = Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tap(int x, int y) {
        checkProcessInitialized();
        String cmd = String.format("input tap %d %d", x, y);
        runCmd(cmd);
    }

    public void swipe(int x1, int y1, int x2, int y2) {
        checkProcessInitialized();
        String cmd = String.format("input swipe %d %d %d %d", x1, y1, x2, y2);
        runCmd(cmd);
    }

    public void longPress(int x, int y, int time) {
        checkProcessInitialized();
        // 长按也是使用swipe命令, 但是起点和重点坐标一样
        String cmd = String.format("input swipe %d %d %d %d %d", x, y, x, y, time);
        runCmd(cmd);
    }

    public void inputKey(int keyCode) {
        checkProcessInitialized();
        String cmd = String.format("input keyevent %d", keyCode);
        runCmd(cmd);
    }

    public void inputText(String text) {
        checkProcessInitialized();
        String cmd = String.format("input text '%s'", text);
        runCmd(cmd);
    }

    private void checkProcessInitialized() {
        if (process == null) {
            throw new RuntimeException("You should call init() first");
        }
    }

    private void runCmd(String cmd) {
        Log.d(TAG, "runCmd start:" + System.currentTimeMillis());
        cmd = cmd + "\n";
        try {
            if (outputStream == null) {
                outputStream = process.getOutputStream();
            }

            outputStream.write(cmd.getBytes());
            outputStream.flush();
//                    if (logOutput) {
//                        String input = loadStream(process.getInputStream());
//                        log(input, false);
//
//                        String error = loadStream(process.getErrorStream());
//                        log(error, true);
//                    }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "runCmd end:" + System.currentTimeMillis());
    }

    private void execShellCmd(String cmd) {

        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void log(String input, boolean error) {
        if (input != null && input.length() > 0) {
            if (error) {
                Log.e(TAG, input);
            } else {
                Log.d(TAG, input);
            }
        }
    }

    private String loadStream(InputStream in) throws IOException {
        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();
        int count = 3;
        while ((ptr = in.read()) != -1 && 0 == count--) {
            buffer.append((char) ptr);
        }
        return buffer.toString();
    }
}
