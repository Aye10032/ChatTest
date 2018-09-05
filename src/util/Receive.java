package util;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Receive implements Runnable {

    private DataInputStream dis;
    private boolean isRunning = true;
    private JTextArea JA;

    @Override
    public void run() {
        while (isRunning) {
            JA.append(receiveText() + "\n");
        }
    }

    public Receive() {

    }

    public Receive(Socket client, JTextArea txtlog) {
        try {
            dis = new DataInputStream(client.getInputStream());
            this.JA = txtlog;
        } catch (IOException e) {
            isRunning = false;
            CloseUtil.closeAll(dis);
        }
    }

    public String receiveText() {
        String msg = "";
        try {
            msg = dis.readUTF();
        } catch (IOException e) {
            isRunning = false;
            CloseUtil.closeAll(dis);
        }
        return msg;
    }

}
