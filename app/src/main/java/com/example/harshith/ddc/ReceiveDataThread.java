package com.example.harshith.ddc;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by harshith on 17/6/16.
 */

public class ReceiveDataThread extends Thread {
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    Handler deliveryHandler;
//    Handler processHandler = null;
//    GlobalClass globalClass;
    public int readStatus;
    private StringBuilder stringBuilder = null;
    ArrayList<Integer> readings;
    Gesture []a;
    Context context;

//    public ReceiveDataThread(BluetoothSocket bluetoothSocket, Handler handler, GlobalClass globalClass, Context mContext) {
    public ReceiveDataThread(BluetoothSocket bluetoothSocket, Handler deliveryHandler, Context mContext) {
        this.bluetoothSocket = bluetoothSocket;
        this.deliveryHandler = deliveryHandler;
//        this.globalClass = globalClass;
        this.context = mContext;
        stringBuilder = new StringBuilder();

//        a = new Gesture[8];
//        for (int i = 0; i<8; i++) {
//            a[i] = new StaticGesture();
//        }
//
//        int []hand = {0,0,0,0,0,0,0,0,0,0,0}; int q=0;
//
//        q=0; hand = new int [] {0,0,0,0,0,8,8,8,8,8,8};
//         neutral
//        a[q].updateFrame(hand);
//
//        q=1; hand = new int [] {1,1,0,0,0,8,8,8,8,1,8};
//         voice search
//        a[q].updateFrame(hand);
//
//        q=2; hand = new int [] {1,0,1,1,1,8,8,8,8,1,8};
        // tap
//        a[q].updateFrame(hand);
//
//        q=3; hand = new int [] {0,1,1,1,0,8,8,8,8,1,8};
        // call
//        a[q].updateFrame(hand);
//
//        q=4; hand = new int [] {0,0,1,1,1,8,8,8,-1,8,8};
        // cam open
//        a[q].updateFrame(hand);
//
//        q=5; hand = new int [] {0,0,0,1,1,8,8,8,-1,8,8};
        // cam click
//        a[q].updateFrame(hand);
//
//        q=6; hand = new int [] {1,0,1,1,0,8,8,8,8,1,8};
        // music open
//        a[q].updateFrame(hand);
//
//        q=7; hand = new int [] {0,0,1,1,0,8,8,8,8,1,8};
        // music play
//        a[q].updateFrame(hand);
//
    }

//    public void setProcessHandler(Handler processHandler) {
//        this.processHandler = processHandler;
//    }

    @Override
    public void run(){
        try {
            InputStream tempIn = null;
            tempIn = bluetoothSocket.getInputStream();
            inputStream = tempIn;

            byte[] buffer = new byte[64];
            int bytes = -1;

//            ProcessThread processThread = new ProcessThread(globalClass);
//            processThread.start();
//
//            processHandler = globalClass.getProcessHandler();
            Live staticLive = new Live();
            int no = 50;
            //StaticLive.print();
            int count = 0;
            int gestActive = -1; boolean stay = true;

            while(true) {
                bytes = inputStream.read(buffer);
                String readMessage = new String(buffer,0,bytes);
                stringBuilder.append(readMessage);
                int endOfLineIndex = stringBuilder.indexOf("~");
                if (endOfLineIndex > 0) {
                    int startOfLineIndex = stringBuilder.indexOf("#");
                    if (startOfLineIndex > endOfLineIndex || startOfLineIndex == -1) {
                        startOfLineIndex = 0;
                    }
                    else if (startOfLineIndex == 0) {
                        startOfLineIndex = 1;
                    }
                    String dataIn = stringBuilder.substring(startOfLineIndex, endOfLineIndex);

                    String[] readingStrings = dataIn.split("\\+");
                    readings = new ArrayList<>(readingStrings.length);
                    for (int i = 0; i != readingStrings.length; i++) {
                        try {
                            readings.set(i, Integer.valueOf(readingStrings[i]));
                        } catch (NumberFormatException e) {

                        }
                        catch (NullPointerException e) {

                        }
                    }
                    String testConvert = "";
                    for (int reading : readings) {
                        testConvert += " " + reading;
                    }
                    L.m(testConvert);
                    stringBuilder.delete(0, endOfLineIndex + 2);

                    //// Recognition part
                    // Broadcasting Part
//                    LocalBroadcastManager.getInstance(this.context).registerReceiver(LearningActivity.getBroadcastReceiver(), new IntentFilter("com.example.harshith.ddc.DATA_STREAM"));
                    Bundle readingsBundle = new Bundle();
                    readingsBundle.putIntegerArrayList("READINGS_INSTANCE", readings);
                    Message readingsMessage = new Message();
                    readingsMessage.setData(readingsBundle);
                    deliveryHandler.sendMessage(readingsMessage);

                }
            }


        }
        catch (IOException e){
            readStatus = Constants.READ_STATUS_NOT_OK;
        }
    }

    public void convert(){
        int endOfLineIndex = stringBuilder.indexOf("~");
        if (endOfLineIndex > 0) {
            int startOfLineIndex = stringBuilder.indexOf("#");
            if (startOfLineIndex > endOfLineIndex || startOfLineIndex == -1) {
                startOfLineIndex = 0;
            }
            else if (startOfLineIndex == 0) {
                startOfLineIndex = 1;
            }
            String dataIn = stringBuilder.substring(startOfLineIndex, endOfLineIndex);

            String[] readingStrings = dataIn.split("\\+");
            readings = new ArrayList<>(readingStrings.length);
            for (int i = 0; i != readingStrings.length; i++) {
                try {
                    readings.set(i, Integer.valueOf(readingStrings[i]));
                } catch (NumberFormatException e) {

                }
                catch (NullPointerException e) {

                }
            }
            String testConvert = "";
            for (int reading : readings) {
                testConvert += " " + reading;
            }
            L.m(testConvert);
            stringBuilder.delete(0, endOfLineIndex + 2);

//            int[] flex = new int[5];
//            for (int i = 0; i != 5; i++) {
//                try {
//                    flex[i] = readings[i];
//                }
//                catch (ArrayIndexOutOfBoundsException e) {
//
//                }
//            }
//
//            int[] mpu = new int[6];
//            for (int i = 0; i != 6; i++) {
//                try {
//                    mpu[i] = readings[5 + i];
//                }
//                catch (ArrayIndexOutOfBoundsException e) {
//
//                }
//            }
        }
    }
}
