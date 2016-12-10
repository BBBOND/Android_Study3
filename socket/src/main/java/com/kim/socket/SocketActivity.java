package com.kim.socket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SocketActivity extends AppCompatActivity {

    private Button tcp;
    private Button udp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        tcp = (Button) findViewById(R.id.tcp);
        udp = (Button) findViewById(R.id.udp);

        tcp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ServerSocket serverSocket = null;
                        try {
                            serverSocket = new ServerSocket(4567);
                            Socket socket = serverSocket.accept();
                            InputStream inputStream = socket.getInputStream();
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            byte buffer[] = new byte[1024 * 4];
                            int temp = 0;
                            while ((temp = inputStream.read(buffer)) != -1) {
                                byteArrayOutputStream.write(buffer, 0, temp);
                                Log.d("SocketActivity", new String(buffer, 0, temp));
                            }
                            String str = new String(byteArrayOutputStream.toByteArray());
                            Log.d("SocketActivity", "------>" + str);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                serverSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });

        udp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatagramSocket socket = new DatagramSocket(4567);
                    byte[] date = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(date, date.length);
                    socket.receive(packet);
                    String str = new String(date, packet.getOffset(), packet.getLength());
                    Log.d("SocketActivity", "---->" + str);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 常用于文件传输
     */
    public void tcpClient() {
        try {
            Socket socket = new Socket("ip", 4567);
            byte[] date = "hello".getBytes();
            InputStream inputStream = new ByteArrayInputStream(date);
            OutputStream outputStream = socket.getOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int temp = 0;
            while ((temp = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, temp);
            }
            outputStream.flush();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 普通文字等的传输
     */
    public void udpClient() {
        try {
            DatagramSocket socket = new DatagramSocket(4567);
            InetAddress serverAddress = InetAddress.getByName("ip");
            byte[] date = "hello".getBytes();
            DatagramPacket packet = new DatagramPacket(date, date.length, serverAddress, 4567);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
