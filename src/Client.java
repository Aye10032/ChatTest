import util.Receive;
import util.Send;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        System.out.println("input name");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String name = br.readLine();

        InetAddress inet = InetAddress.getByName("106.12.35.79"/*"127.0.0.1"*/);
        Socket client = new Socket(inet, 3614);

        new Thread(new Send(client, name)).start();

        new Thread(new Receive(client)).start();
    }

}
