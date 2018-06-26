import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.DatagramPacket;


public class Client extends JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel contentPanel;

    String name, address;
    private int port;
    private JTextField txtMessage;
    private JTextArea history;
    private boolean connected = false;
    private Net net = null;
    private Thread recieve;
    public Client(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
        net = new Net(port);
        connected = net.openConnection(address);
        if(!connected) {
            System.out.println("Connection Failed...");
            console("Connection Failed...");
        }
        createWindow();
        recieve();
        String connectionPacket = "/c/" + name;
        net.send(connectionPacket + "Connected");
        console("You are trying to connect to: " + address + ", port: " + port + ", user name: " + name);
    }
    private void createWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        setTitle("Messenger Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(contentPanel);

        GridBagLayout gb1_contentPane = new GridBagLayout();
        gb1_contentPane.columnWidths = new int[] {16, 857, 7};
        gb1_contentPane.columnWidths = new int[] {16, 827,30,7};
        gb1_contentPane.rowHeights = new int[]{35, 475, 40};
        gb1_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gb1_contentPane.columnWeights = new double[]{1.0, 1.0};
        gb1_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        contentPanel.setLayout(gb1_contentPane);

        history = new JTextArea();
        history.setEditable(false);
        history.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(history);
        GridBagConstraints scrollConstraints = new GridBagConstraints();
        scrollConstraints.insets = new Insets(0, 0, 5, 5);
        scrollConstraints.fill = GridBagConstraints.BOTH;
        scrollConstraints.gridx = 0;
        scrollConstraints.gridy = 0;
        scrollConstraints.gridwidth = 3;
        scrollConstraints.gridheight = 2;
        scrollConstraints.insets = new Insets(0, 7, 0, 0);
        contentPanel.add(scroll, scrollConstraints);

        txtMessage = new JTextField();
        txtMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    send(txtMessage.getText());
                }
            }
        });
        GridBagConstraints gbc_txtMessage = new GridBagConstraints();
        gbc_txtMessage.insets = new Insets(0, 0, 0, 5);
        gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtMessage.gridx = 0;
        gbc_txtMessage.gridy = 2;
        gbc_txtMessage.gridwidth = 2;
        contentPanel.add(txtMessage, gbc_txtMessage);
        txtMessage.setColumns(10);

        JButton btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                send(txtMessage.getText());
            }
        });
        GridBagConstraints gbc_btnSend = new GridBagConstraints();
        gbc_btnSend.insets = new Insets(0, 0, 0, 5);
        gbc_btnSend.gridx = 2;
        gbc_btnSend.gridy = 2;
        contentPanel.add(btnSend, gbc_btnSend);
        setVisible(true);

        txtMessage.requestFocusInWindow();
    }
    public void recieve() {
        recieve = new Thread(new Runnable() {
            @Override
            public void run() {
                if(net.recieve() != "") {
                    console(net.recieve());
                }
            }
        });
    }
    public void send(String message) {
        if(message.equals("")) return;
        message = name + ": " + message;
        net.send(message);
        txtMessage.setText("");
    }
    public void  console(String message) {
        history.setCaretPosition(history.getDocument().getLength());
        history.append(message + "\n\r");
    }
}
