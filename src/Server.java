import util.CloseUtil;

import javax.naming.Name;
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
        new Server().RunVoid();
    }

    public void RunVoid() throws IOException {
        ServerSocket serverSocket = new ServerSocket(14514);
        while (true) {
            Socket client = serverSocket.accept();
            System.out.println(client.getInetAddress());
            Channel channel = new Channel(client);
            clients.add(channel);
            new Thread(channel).start();
        }
    }

    private class Channel implements Runnable {
        private DataInputStream dis;
        private DataOutputStream dos;
        private boolean isRunning = true;
        private String name;

        @Override
        public void run() {
            while (isRunning) {
                sendAll(receiveText());
            }
        }

        public Channel(Socket client) {
            try {
                dis = new DataInputStream(client.getInputStream());
                dos = new DataOutputStream(client.getOutputStream());

                this.name = dis.readUTF();
                send(this.name + " ,welcome!");
                sendOthers("          "+this.name +"加入了");
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
                sendOthers(this.name + "离开了");
                isRunning = false;
                CloseUtil.closeAll(dis,dos);
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
                    sendOthers(this.name + "离开了");
                    CloseUtil.closeAll(dos);
                    isRunning = false;
                    clients.remove(this);
                }
        }

        private void sendOthers(String msg) {
            for (Channel other : clients) {
                if (other == this){
                    continue;
                }
                other.send(msg);
            }
        }

        private void sendAll(String msg){
            if (msg == null || msg.equals("")){
                return;
            }else {
                for (Channel other : clients) {
                    if (other == this) {
                        continue;
                    }
                    other.send(this.name + " : " + msg);
                }
                send("我 : "+msg);
            }
        }
    }

}
