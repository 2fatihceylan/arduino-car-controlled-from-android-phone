package com.example.araba;

import androidx.appcompat.app.AppCompatActivity;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    EditText bluetoothSend;
    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;

    private static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private OutputStream mmOutStream;
    private InputStream mmInStream;

    private BluetoothSocket mmSocket;
    private byte[] mmBuffer; // mmBuffer store for the stream
   // private TextView textView,textViewns;

    private Handler mHandler; // handler that gets info from Bluetooth service

    ImageButton ileri,geri,fren,sag,sol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //bluetoothSend = (EditText) findViewById(R.id.bluetooth_word);

       // textView = (TextView) findViewById(R.id.textView2);
       // textViewns=findViewById(R.id.textViewns);
        ileri=findViewById(R.id.buttongaz);
        geri=findViewById(R.id.button6);
        fren=findViewById(R.id.buttonfren);
        sag=findViewById(R.id.buttonsag);
        sol=findViewById(R.id.buttonsol);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mmSocket = null;


        on();
        connector();
        th.start();



        ileri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    up(v);
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    down(v);

                }
                return true;
            }
        });
        geri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    back(v);
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    down(v);

                }
                return true;
            }
        });
        fren.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    down(v);

                }
                if (event.getAction()==MotionEvent.ACTION_UP){


                }
                return true;
            }
        });
        sag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    right(v);
                }
                if (event.getAction()==MotionEvent.ACTION_UP){

                    orta(v);

                }
                return true;
            }
        });
        sol.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    left(v);
                }
                if (event.getAction()==MotionEvent.ACTION_UP){

                    orta(v);

                }
                return true;
            }
        });

    }

    // Bluetooth acık degilse acar
    public void on() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Bluetooth Açıldı...", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth Açık...", Toast.LENGTH_LONG).show();
        }
    }

    // Bluetooth kapatma
    public void off(View v) {
        bluetoothAdapter.disable();
        Toast.makeText(getApplicationContext(), "Bluetooth Kapatıldı...", Toast.LENGTH_LONG).show();
    }

    // Eger bluetooth baglantisi kurulu degilse baglanır
    public void connect(View v) {


        try {
            String name = "CONNECTED";
            byte[] bytes = name.getBytes();
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Bağlanıyor...", Toast.LENGTH_LONG).show();
            connector();


        }
    }

    //Baglantı metodu
    public void connector() {

        OutputStream tmpOut = null;
        InputStream tmpIn = null;


        BluetoothSocket tmp = null;

        String dname;

        //eşlenmiş cihazların listeler
        pairedDevices = bluetoothAdapter.getBondedDevices();
        BluetoothDevice device = null;
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                Log.d("TAG", bt.getName());
                dname = bt.getName();
                if (dname.equals("HC-05")) {
                    device = bt;
                    Log.d("TAG", "HC-05 Eşlendi!!!");
                    //Toast.makeText(getApplicationContext(), device.getName(), Toast.LENGTH_LONG).show();


                } else {
                    Log.d("TAG", "HC-05 degil");
                }

            }

            try {
                // ///
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);

            } catch (IOException e) {
                Log.d("TAG", "Socket's listen() hatası", e);
                Toast.makeText(getApplicationContext(), "Hata1...", Toast.LENGTH_LONG).show();
            }
            mmSocket = tmp;


            bluetoothAdapter.cancelDiscovery();


            try {
                //bluetooth cihaza baglanma: taki başarılı olana yada hata gönderene kadar
                mmSocket.connect();


                Log.d("TAG", "Socket bağlandı!!!!!");
                Toast.makeText(getApplicationContext(), "Bağlandı", Toast.LENGTH_LONG).show();
            } catch (IOException connectException) {
            }


            try {
                tmpIn = mmSocket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "input streamde hata var", e);
            }


            try {

                tmpOut = mmSocket.getOutputStream();


            } catch (IOException e) {
                Log.e(TAG, "output streamde hata var", e);
                Toast.makeText(getApplicationContext(), "Hata2...", Toast.LENGTH_LONG).show();
            }

            mmOutStream = tmpOut;
            mmInStream = tmpIn;


        } else {
            Log.d("TAG", "Cihaz Yok");
            Toast.makeText(getApplicationContext(), "HC-05 eşlenmedi", Toast.LENGTH_LONG).show();
        }


    }



    //hc05 modulunden gelen serial monitor verisini alır (iyi calışmıyor sonra bak)
    Thread th = new Thread(new Runnable() {
        public void run() {


            mmBuffer = new byte[4096];
            int numBytes; //

            // IOexception gelene kadar veri al

            while (true) {
                try {
                    if (mmInStream.available() > 2) {
                        Log.d("TAG", "mmInStream.available()>2");

                        // gelen veriyi okuma
                        numBytes = mmInStream.read(mmBuffer);


                        final String readMessage = new String(mmBuffer, 0, numBytes);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // textView.setText(readMessage);//textview da serial monitor yazdırma

                                //parse işlemi ile verilerin hangi sensorlerden geldiğini ayır ona göre ayrı textviewlara yazdır




                            }
                        });


                        Log.d("TAG", readMessage);
                    } else {
                        SystemClock.sleep(100);
                        Log.d("TAG", "Veri Yok");
                    }


                } catch (IOException e) {
                    Log.d("TAG", "Input stream  disconnected", e);
                    break;
                }
            }


            ////////////////////////////

        }
    });


    // hc05 üzerinden gönderilecek komutlar
    public void write(View v) {

        String name = bluetoothSend.getText().toString();
        byte[] bytes = name.getBytes();
        Log.d("TAG", "Pressed: " + name);

        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", "");
            Toast.makeText(getApplicationContext(), "write hatası", Toast.LENGTH_LONG).show();
        }


    }


    public void up(View v) {
        String name = "ileri";
        byte[] bytes = name.getBytes();
        Log.d("TAG", "Pressed: " + name);
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", "" + e);
            Toast.makeText(getApplicationContext(), "up hatası", Toast.LENGTH_LONG).show();
        }

    }

    public void down(View v) {
        String name = "fren";
        byte[] bytes = name.getBytes();
        Log.d("TAG", "Pressed: " + name);
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", "" + e);
            Toast.makeText(getApplicationContext(), "down hatası", Toast.LENGTH_LONG).show();
        }

    }
    public void back(View v) {
        String name = "geri";
        byte[] bytes = name.getBytes();
        Log.d("TAG", "Pressed: " + name);
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", "" + e);
            Toast.makeText(getApplicationContext(), "down hatası", Toast.LENGTH_LONG).show();
        }

    }


    public void left(View v) {
        String name = "sol";
        byte[] bytes = name.getBytes();
        Log.d("TAG", "Pressed: " + name);
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", "" + e);
            Toast.makeText(getApplicationContext(), "left hatası", Toast.LENGTH_LONG).show();
        }

    }

    public void right(View v) {
        String name = "sag";
        byte[] bytes = name.getBytes();
        Log.d("TAG", "Pressed: " + name);
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", "" + e);
            Toast.makeText(getApplicationContext(), "right hatası", Toast.LENGTH_LONG).show();
        }

    }
    public void orta(View v) {
        String name = "orta";
        byte[] bytes = name.getBytes();
        Log.d("TAG", "Pressed: " + name);
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", "" + e);
            Toast.makeText(getApplicationContext(), "right hatası", Toast.LENGTH_LONG).show();
        }

    }
   


}