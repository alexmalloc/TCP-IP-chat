import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;

public class Net {
    private Socket ClientSocket = null;
    private InetAddress ip = null;

    private int port;
    private Thread netRun;
    private Thread send;

    public Net(int port) {
        this.port = port;
    }

    public boolean openConnection(String address) {


        return true;
    }
    public String recieve() {

        try {
            BufferedReader messageFromServer = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
            String message = messageFromServer.readLine();
            return message;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void send(final String data) {
        send = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket clientSocket = new Socket("localHost", port);
                    DataOutputStream outputToServer = new DataOutputStream(clientSocket.getOutputStream());
                    outputToServer.writeBytes(data + "\n");
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
