package server;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server{

    private List<ServerClient> clients = new ArrayList<ServerClient>();
    private int port;
    private ServerSocket serverSocket;
    private Thread serverRun, manage, receive;
    private boolean running = false;

    public Server(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        serverRun = new Thread(new Runnable() {
            @Override
            public void run() {
                running = true;
                System.out.println("Server started on port " + port);
                manage();
                receive();
            }
        }, "serverRun");

        serverRun.start();
    }

    private void manage() {
        manage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {


                }
            }
        }, "manage");
        manage.start();
    }

    private void receive() {
        receive = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try {
                        Socket socketConnection = serverSocket.accept();
                        BufferedReader messageFromClient =
                                new BufferedReader(new InputStreamReader(socketConnection.getInputStream()));
                        String clientSentence = messageFromClient.readLine();
                        clients.add(new ServerClient("john", socketConnection.getInetAddress(), socketConnection.getPort(), 3434));
                        System.out.println(clients.get(0).address.toString() + ":" + socketConnection.getPort());
                        if(!clientSentence.isEmpty()) {
                            Socket clientSocket = new Socket("localhost", port);
                            DataOutputStream outputToClient = new DataOutputStream(clientSocket.getOutputStream());
                            outputToClient.writeBytes(clientSentence);
                            System.out.println("Recieved: " + clientSentence);
                            clientSocket.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
        receive.start();
    }

    private void process(DatagramPacket packet ) {
        String str = new String(packet.getData());
        if(str.startsWith("/c/")) {
            int id = UniqueID.getID();
            System.out.println("ID:" + id);
            clients.add(new ServerClient(str.substring(3, str.length()), packet.getAddress(), packet.getPort(), id));
            System.out.println(str.substring(3, str.length()));
        } else {
            System.out.println(str);
        }
    }
}
