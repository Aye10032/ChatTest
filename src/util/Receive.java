package util;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Receive implements Runnable {

    private DataInputStream dis;
    private boolean isRunning = true;
    private JTextArea JA;
    private JScrollBar JSB;

    @Override
    public void run() {
        while (isRunning) {
            JA.append(receiveText() + "\n");
            if (JSB != null)
                JSB.setValue(JSB.getMaximum());
        }
    }

    public Receive() {

    }

    public Receive(Socket client, JTextArea txtlog, JScrollBar JSB) {
        try {
            dis = new DataInputStream(client.getInputStream());
            this.JA = txtlog;
            this.JSB = JSB;
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
