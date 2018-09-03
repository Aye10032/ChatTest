package util;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Receive implements Runnable{

    private DataInputStream dis;
    private boolean isRunning = true;

    @Override
    public void run() {
        while (isRunning) {
            System.out.println(receiveText());
        }
    }

    public Receive() {

    }

    public Receive(Socket client) {
        try {
            dis = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            isRunning = false;
            CloseUtil.closeAll(dis);
        }
    }

    public String receiveText() {
        String msg = "";
        try {
            msg = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            isRunning = false;
            CloseUtil.closeAll(dis);
        }
        return msg;
    }

}
