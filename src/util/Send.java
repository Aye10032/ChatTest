package util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Send implements Runnable {

    private static DataOutputStream dos;
    private static boolean isRunning = true;

    @Override
    public void run() {
        while (isRunning) {

        }
    }

    public Send() {

    }

    public Send(Socket client, String name) {
        this();
        try {
            dos = new DataOutputStream(client.getOutputStream());
            send(name);
        } catch (IOException e) {
            isRunning = true;
            CloseUtil.closeAll(dos);
        }
    }

    public static void send(String msg) {
        if (null != msg && !msg.equals("")) {
            try {
                dos.writeUTF(msg);
                dos.flush();
            } catch (IOException e) {
                isRunning = false;
                CloseUtil.closeAll(dos);

            }
        }
    }

}
