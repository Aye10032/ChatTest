package util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Send implements Runnable {

    private BufferedReader consol;
    private DataOutputStream dos;
    private boolean isRunning = true;
    private String name;

    @Override
    public void run() {
        while (isRunning) {
            send(getText());
        }
    }

    public Send() {
        consol = new BufferedReader(new InputStreamReader(System.in));
    }

    public Send(Socket client, String name) {
        this();
        try {
            dos = new DataOutputStream(client.getOutputStream());
            this.name = name;
            send(name);
        } catch (IOException e) {
            isRunning = true;
            CloseUtil.closeAll(dos, consol);
        }
    }

    private String getText() {
        try {
            return consol.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void send(String msg) {
        if (null != msg && !msg.equals("")) {
            try {
                dos.writeUTF(msg);
                dos.flush();
            } catch (IOException e) {
                isRunning = false;
                CloseUtil.closeAll(dos, consol);

            }
        }
    }

}
