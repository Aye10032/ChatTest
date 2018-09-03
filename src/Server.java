import util.CloseUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<Channel> clients = new ArrayList<Channel>();

    public static void main(String[] args) throws IOException {
        new Server().start();
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(6655);
        while (true) {
            Socket client = serverSocket.accept();

            Channel channel = new Channel(client);
            clients.add(channel);
            new Thread(channel).start();
        }
    }

    private class Channel implements Runnable {
        private DataInputStream dis;
        private DataOutputStream dos;
        private boolean isRunning = true;

        @Override
        public void run() {
            while (isRunning) {
                sendOthers();
            }
        }

        public Channel(Socket client) {
            try {
                dis = new DataInputStream(client.getInputStream());
                dos = new DataOutputStream(client.getOutputStream());
            } catch (IOException e) {
                isRunning = false;
                CloseUtil.closeAll(dis, dos);
            }
        }

        private String receiveText() {
            String msg = "";
            try {
                msg = dis.readUTF();
            } catch (IOException e) {
                isRunning = false;
                CloseUtil.closeAll(dis);
                clients.remove(this);
            }
            return msg;
        }

        private void send(String msg) {
            if (null == msg || msg.equals("")) {
                return;
            }
            try {
                dos.writeUTF(msg);
                dos.flush();
            } catch (IOException e) {
                CloseUtil.closeAll(dos);
                isRunning = false;
                clients.remove(this);
            }
        }

        private void sendOthers() {
            String msg = receiveText();

            for (Channel other : clients) {
                if (other == this) {
                    continue;
                }
                other.send(msg);
            }
        }
    }

}
