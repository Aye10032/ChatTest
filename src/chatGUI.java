import util.LayoutUtil;
import util.Receive;
import util.Send;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.Socket;

public class chatGUI extends JFrame implements ActionListener, KeyListener {

    JLabel name = new JLabel("");
    public JTextArea txtlog = new JTextArea();
    JScrollPane jsp = new JScrollPane(txtlog);
    JScrollBar JSB = jsp.getVerticalScrollBar();
    JTextField inputJF = new JTextField();
    JButton send = new JButton("send");
    String id;
    Socket client;

    public chatGUI(Socket client, String name) {

        jsp.doLayout();
        txtlog.setLineWrap(true);
        txtlog.setWrapStyleWord(true);

        this.name.setText(client.getInetAddress().getHostAddress());
        this.id = name;
        this.client = client;

        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createEtchedBorder());
        p.setLayout(new GridBagLayout());

        txtlog.setEditable(false);
        LayoutUtil.add(p, GridBagConstraints.NONE, GridBagConstraints.CENTER, 0, 0, 0, 0, 4, 1, this.name, new Insets(10, 5, 5, 5));
        LayoutUtil.add(p, GridBagConstraints.BOTH, GridBagConstraints.CENTER, 1, 1, 0, 1, 4, 1, jsp, new Insets(5, 5, 50, 5));

        getContentPane().add(p, BorderLayout.CENTER);

        JPanel pd = new JPanel();
        pd.setLayout(new GridBagLayout());

        LayoutUtil.add(pd, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, 1, 0, 0, 0, 1, 1, inputJF, new Insets(3, 5, 3, 5));
        LayoutUtil.add(pd, GridBagConstraints.NONE, GridBagConstraints.CENTER, 0, 0, 4, 0, 1, 1, send, new Insets(3, 5, 3, 5));

        getContentPane().add(pd, BorderLayout.SOUTH);

        new Thread(new Send(client, name)).start();
        new Thread(new Receive(client, txtlog, JSB)).start();

        send.addActionListener(this);
        inputJF.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == send) {
            Send.send(inputJF.getText());
            inputJF.setText("");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == inputJF) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                Send.send(inputJF.getText());
                inputJF.setText("");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
