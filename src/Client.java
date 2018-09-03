import util.Send;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        InetAddress inet = InetAddress.getByName(/*"106.12.35.79"*/"127.0.0.1");
        Socket client = new Socket(inet, 6655);

        new Thread(new Send(client)).start();
    }

}
