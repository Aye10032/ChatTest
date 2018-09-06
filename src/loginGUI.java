import util.LayoutUtil;
import util.Receive;
import util.Send;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class loginGUI extends JFrame implements ActionListener {

    private JLabel inputJL = new JLabel("输入ID");
    private JTextField inputJF = new JTextField("");
    private JButton cancel = new JButton("取消");
    private JButton next1 = new JButton("确定");

    public loginGUI() {
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createEtchedBorder());
        p.setLayout(new GridBagLayout());

        LayoutUtil.add(p, GridBagConstraints.VERTICAL, GridBagConstraints.CENTER, 0, 1, 0, 0, 1, 1, new JLabel());
        LayoutUtil.add(p, GridBagConstraints.NONE, GridBagConstraints.CENTER, 0, 0, 0, 1, 1, 1, inputJL, new Insets(0, 50, 0, 10));
        LayoutUtil.add(p, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, 1, 0, 1, 1, 5, 1, inputJF, new Insets(0, 10, 0, 50));
        LayoutUtil.add(p, GridBagConstraints.VERTICAL, GridBagConstraints.CENTER, 0, 1, 0, 2, 1, 1, new JLabel());

        getContentPane().add(p, BorderLayout.CENTER);

        JPanel pd = new JPanel();
        pd.setLayout(new GridBagLayout());
        LayoutUtil.add(pd, GridBagConstraints.NONE, GridBagConstraints.CENTER, 0, 0, 0, 0, 1, 1, cancel, new Insets(5, 30, 10, 10));
        LayoutUtil.add(pd, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, 1, 0, 1, 0, 2, 1, new JLabel());
        LayoutUtil.add(pd, GridBagConstraints.NONE, GridBagConstraints.CENTER, 0, 0, 4, 0, 1, 1, next1, new Insets(5, 10, 10, 30));
        getContentPane().add(pd, BorderLayout.SOUTH);

        next1.addActionListener(this);
        cancel.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == cancel) {
            int y = JOptionPane.showConfirmDialog(this, "确定要退出吗？", "", JOptionPane.YES_NO_OPTION);

            if (y == 0) {
                System.exit(0);
            }
        } else if (source == next1) {
            String name = inputJF.getText();

            InetAddress inet = null;
            try {
                inet = InetAddress.getByName("106.12.35.79"/*"127.0.0.1"*/);
                Socket client = new Socket(inet, 14514);

                this.hide();

                chatGUI win = new chatGUI(client, name);
                win.setTitle("chat");
                int x = Toolkit.getDefaultToolkit().getScreenSize().width;
                int y = Toolkit.getDefaultToolkit().getScreenSize().height;
                win.setBounds(x / 2 - 350, y / 2 - 250, 700, 500);
                win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                win.show();
                //new Thread(new Send(client, name)).start();
                //new Thread(new Receive(client)).start();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
