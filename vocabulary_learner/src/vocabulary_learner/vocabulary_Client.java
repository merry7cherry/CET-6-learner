package vocabulary_learner;

import javax.swing.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;


public class vocabulary_Client extends JFrame implements ActionListener, FocusListener, MouseListener {
    private BufferedReader server_input = null;
    private PrintStream user_output = null;
    private String nickName = null;
    private String select_filename = null;
    private String select_ip = null;

    Socket s;

    private JPanel jpl = new JPanel();
    private BorderLayout borderLayout = new BorderLayout();

    private JPanel jpl_center = new JPanel();
    private JPanel jpl_first = new JPanel();
    private JPanel jpl_second = new JPanel();
    private JPanel jpl_third = new JPanel();
    private JPanel jpl_fourth = new JPanel();
    private JLabel jLabel0 = new JLabel("Welcome to the Vocabulary_Learner!");
    private JLabel jLabel1 = new JLabel("Your Nickname:");
    private JLabel jLabel2 = new JLabel("Your Server:");
    private JLabel jLabel3 = new JLabel("Your File:");
    private JLabel jlb_the_num_of_users = new JLabel();
    private JTextField jTextField = new JTextField();
    private JComboBox jComboBox1 = new JComboBox();
    private JComboBox jComboBox2 = new JComboBox();
    private JTextArea tip1 = new JTextArea(" select the Server you want to join");
    private JTextArea tip2 = new JTextArea("enter your nickname");
    private JTextArea tip3 = new JTextArea(" select the file you want to study");
    private JButton jButton = new JButton("Login");

    public vocabulary_Client() throws Exception {
        construct_client();
    }

    private boolean name_is_legal(String name) {
        if (name == null) {
            return false;
        }
        return name.length() > 0 && name.length() < 10;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jComboBox1)) {
            Object filename = jComboBox1.getSelectedItem();
            if (null == filename || filename.equals(" "))
                return;
            select_filename = filename.toString();
        } else if (e.getSource().equals(jComboBox2)) {
            Object ip = jComboBox2.getSelectedItem();
            if (null == ip || ip.equals(" "))
                return;
            select_ip = ip.toString();
        }
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        nickName = jTextField.getText();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        String tmp = null;
        if (select_ip != null && select_filename != null && name_is_legal(nickName)) {
            try {
                s = new Socket(select_ip, 8889);
                server_input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                user_output = new PrintStream(s.getOutputStream());
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "the Server you selected isn't exist!");
                System.exit(0);
            }
            user_output.println(select_filename);
            user_output.println(nickName);
            try {
                tmp = server_input.readLine();
            } catch (Exception e2) {
            }
            if (tmp.equals("no")) {
                JOptionPane.showMessageDialog(null, "your nickname is existed!\nplease enter a new one!");
            } else if (tmp.equals("ok")) {
                JOptionPane.showMessageDialog(null, "Login successfully!\nGame begins now!");
                new vocabulary_JFrame(server_input, user_output, nickName);

            }

        } else {

            if (select_ip == null) {
                JOptionPane.showMessageDialog(null, "please choose your Server!");
            } else if (select_filename == null) {
                JOptionPane.showMessageDialog(null, "please choose your File!");
            } else if (!name_is_legal(nickName)) {
                JOptionPane.showMessageDialog(null, "the nickname is illegal!");
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public static void main(String[] args) throws Exception {
        new vocabulary_Client();
    }

    private void construct_client() {
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Vocabulary_Learner");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int width = (int)screenSize.getWidth();
//        int height = (int)screenSize.getHeight();
        this.setSize((int) (screenSize.getWidth() * 0.5), (int) (screenSize.getHeight() * 0.9));
        this.setLocation((int) (screenSize.getWidth() * 0.25), (int) (screenSize.getHeight() * 0.05));
        this.setVisible(true);

        jpl_center.setLayout(new GridLayout(4, 1, 1, 1));
        jLabel0.setFont(new Font("微软雅黑", Font.BOLD, 35));
        jpl_first.add(jLabel0);
        jpl_second.setLayout(null);
        jLabel1.setFont(new Font("微软雅黑", Font.BOLD, 20));
        jpl_second.add(jLabel1);
        jLabel1.setSize(200, 20);
        jLabel1.setLocation(170, 5);
        jpl_second.add(jTextField);
        jTextField.setSize(new Dimension(200, 30));
        jTextField.setLocation(350, 5);
        jTextField.setToolTipText("The input name must be less than 10 characters");
        tip2.setForeground(new Color(52, 143, 180));
        tip2.setFont(new Font("Calibri", Font.PLAIN, 15));
        tip2.setEditable(false);
        jpl_second.add(tip2);
        tip2.setSize(130, 20);
        tip2.setLocation(200, 40);
        jButton.setBackground(new Color(143, 203, 227));
        jButton.setFont(new Font("Calibri", Font.BOLD, 25));
        jpl_second.add(jButton);
        jButton.setSize(90, 30);
        jButton.setLocation(325, 100);

        jpl_third.setLayout(null);
        jLabel2.setFont(new Font("微软雅黑", Font.BOLD, 20));
        jpl_third.add(jLabel2);
        jLabel2.setSize(300, 20);
        jLabel2.setLocation(205, 5);
        jpl_third.add(jComboBox2);
        jComboBox2.setSize(new Dimension(200, 30));
        jComboBox2.setLocation(350, 5);
        tip1.setForeground(new Color(52, 143, 180));
        tip1.setFont(new Font("Calibri", Font.PLAIN, 15));
        tip1.setEditable(false);
        jpl_third.add(tip1);
        tip1.setSize(210, 20);
        tip1.setLocation(120, 40);

        jpl_fourth.setLayout(null);
        jLabel3.setFont(new Font("微软雅黑", Font.BOLD, 20));
        jpl_fourth.add(jLabel3);
        jLabel3.setSize(300, 20);
        jLabel3.setLocation(235, 5);
        jpl_fourth.add(jComboBox1);
        jComboBox1.setSize(new Dimension(200, 30));
        jComboBox1.setLocation(350, 5);
        tip3.setForeground(new Color(52, 143, 180));
        tip3.setFont(new Font("Calibri", Font.PLAIN, 15));
        tip3.setEditable(false);
        jpl_fourth.add(tip3);
        tip3.setSize(200, 20);
        tip3.setLocation(130, 40);

        jpl_center.add(jpl_first);
        jpl_center.add(jpl_third);
        jpl_center.add(jpl_fourth);
        jpl_center.add(jpl_second);

        this.add(jpl_center, BorderLayout.CENTER);
        this.setVisible(true);

        jComboBox2.addItem(" ");
        jComboBox2.addItem("127.0.0.1");
        jComboBox1.addItem(" ");
        jComboBox1.addItem("CET_6");

        jComboBox1.addActionListener(this);
        jComboBox2.addActionListener(this);
        jTextField.addFocusListener(this);
        jButton.addMouseListener(this);
    }


}
