package vocabulary_learner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class vocabulary_Server extends JFrame implements Runnable, ActionListener {
    private ServerSocket ss = null;
    private ArrayList<UserThread> users = new ArrayList<UserThread>();
    Vector<String> vocabulary_list = new Vector<String>();
    ArrayList<File> files = new ArrayList<File>();

    private JPanel jpl = new JPanel();
    private BorderLayout borderLayout = new BorderLayout();

    private JPanel jpl_center = new JPanel();
    private JPanel jpl_up = new JPanel();
    private JPanel jpl_center0 = new JPanel();
    private JPanel jpl_down = new JPanel();
    private JLabel jLabel0 = new JLabel("Welcome to the management system!");
    private JLabel jLabel1 = new JLabel("Online User:");
    private JLabel jLabel2 = new JLabel("Document User Can Select:");
    private JLabel jlb_the_num_of_users = new JLabel();
    private JComboBox jComboBox1 = new JComboBox();
    private JComboBox jComboBox2 = new JComboBox();
    private JTextArea tip1 = new JTextArea(" select the player to quit the game");
    private JTextArea tip2 = new JTextArea("select the document to check");

    public vocabulary_Server() throws Exception {
        ss = new ServerSocket(8889);
        new Thread(this).start();
        //define port:8888
        //start
        String str = null;
        File f_vocabulary_list_CET6 = new File("D:\\CODE\\vocabulary_learner\\src\\vocabulary_learner\\CET_6");
        files.add(f_vocabulary_list_CET6);
        //add to the array
        if (f_vocabulary_list_CET6.exists()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f_vocabulary_list_CET6), "UTF-8"));
            while (null != (str = br.readLine())) {
                vocabulary_list.add(str);
            }
            br.close();
        } else {
            System.out.println(f_vocabulary_list_CET6.getAbsolutePath() + "didn't exist");
        }
        for (int i = 0; i < vocabulary_list.size(); i++) {
            String tmp = vocabulary_list.get(i);
            int randIndex = (int) (Math.random() * (vocabulary_list.size() - i) + i);
            vocabulary_list.set(i, vocabulary_list.get(randIndex));
            vocabulary_list.set(randIndex, tmp);
        }
        //distribute cord

        construct_Server();

    }

    public void run() {
        while (true) {
            try {
//                System.out.println("1");
                Socket s = ss.accept();
//                System.out.println("2");
                UserThread ut = new UserThread(s);
                if (ut.thread_is_exist) {
                    users.add(ut);
                    jComboBox1.addItem(ut.nickName);
                }
                ut.start();
//                System.out.println("3");
            } catch (Exception ex) {
                System.out.println("error1");
            }
        }
    }

    public class UserThread extends Thread {
        String vocabulary_text = null;
        BufferedReader user_input = null;
        PrintStream server_output = null;
        String nickName = null;
        String fileName = null;
        int wordIndex = 0; // 下一个该读取的单词的索引
        StringBuffer sb = null;
        boolean thread_is_exist = true;
        String tmp = null;

        public UserThread(Socket s) throws Exception {
            boolean name_is_exist = false;
            user_input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            server_output = new PrintStream(s.getOutputStream());
            fileName = user_input.readLine();
            System.out.println(fileName);
            nickName = user_input.readLine();
            System.out.println(nickName);
            for (Iterator<UserThread> it = users.iterator(); it.hasNext(); ) {
                if (it.next().nickName.equals(this.nickName)) {
                    server_output.println("no");
                    thread_is_exist = false;
                    //if the name is existed already,notifies the client and close this Thread
                    name_is_exist = true;
                    break;
                }
            }
            if (!name_is_exist) {
                server_output.println("ok");
            }
        }

        public void run() {
            while (thread_is_exist) {
                try {
                    tmp = user_input.readLine();
                    if (tmp.equals("next")) {
                        vocabulary_text = vocabulary_list.get(wordIndex++ % vocabulary_list.size());
//                  System.out.println(tmp);
                        sb = new StringBuffer(vocabulary_text);
//                   System.out.println(sb);
                        try {
                            server_output.println(sb);
                        } catch (Exception e) {
                            thread_is_exist = false;
                            for (Iterator<UserThread> it = users.iterator(); it.hasNext(); ) {
                                if (it.next().equals(this)) {
                                    it.remove();
                                }
                            }
                            jComboBox1.removeItem(this.nickName);
                        }
                        jComboBox1.setSelectedIndex(0);
                    } else {
                        for (Iterator<UserThread> it = users.iterator(); it.hasNext(); ) {
                            if (it.next().equals(this)) {
                                it.remove();
                            }
                        }
//                        users.remove(c);
                        jComboBox1.removeItem(this.nickName);
                        thread_is_exist = false;
                    }
                } catch (Exception e1) {
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jComboBox1)) {
            Object name = jComboBox1.getSelectedItem();
            if (null == name || name.equals(" "))
                return;
            int choice = JOptionPane.showConfirmDialog(this, "Whether to end " + name + " 's game progress");
            if (JOptionPane.YES_OPTION == choice) {
                for (UserThread c : users) {
                    if (name.equals(c.nickName)) {
                        c.server_output.println("-9999");
                        for (Iterator<UserThread> it = users.iterator(); it.hasNext(); ) {
                            if (it.next().equals(c)) {
                                it.remove();
//                                System.out.println("f");

                            }
                        }
//                        users.remove(c);
                        jComboBox1.removeItem(c.nickName);
                        c.thread_is_exist = false;
                        JOptionPane.showMessageDialog(null, "End successfully");
                        break;
                        //the break is necessary!!!otherwise for_each will throw unmatched Exception
                    }
                }
            }
            jComboBox1.setSelectedIndex(0);
        } else if (e.getSource().equals(jComboBox2)) {
            Object filename = jComboBox2.getSelectedItem();
            if (null == filename || filename.equals(" "))
                return;
            int choice = JOptionPane.showConfirmDialog(this, "Whether to view Document: " + filename);
            if (JOptionPane.YES_OPTION == choice) {
                for (File c : files) {
                    if (filename.equals(c.getName())) {
                        try {
                            new vocabulary_JFrame_check(filename.toString(), c.getAbsolutePath());
                        } catch (Exception ignored) {
                        }
                        JOptionPane.showMessageDialog(null, "Open successfully");
                    }
                }
            }
            jComboBox2.setSelectedIndex(0);
        }
    }


    public static void main(String args[]) throws Exception {
        new vocabulary_Server();
    }

    private void construct_Server() {
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("The Server of Vocabulary_Learner");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int width = (int)screenSize.getWidth();
//        int height = (int)screenSize.getHeight();
        this.setSize((int) (screenSize.getWidth() * 0.5), (int) (screenSize.getHeight() * 0.9));
        this.setLocation((int) (screenSize.getWidth() * 0.25), (int) (screenSize.getHeight() * 0.05));
        this.setVisible(true);

        jpl.setLayout(borderLayout);


        jpl_center.setLayout(new GridLayout(4, 1, 1, 1));
        jLabel0.setFont(new Font("微软雅黑", Font.BOLD, 35));
        jpl_up.add(jLabel0);

        jpl_center0.setLayout(null);
        jLabel1.setFont(new Font("微软雅黑", Font.BOLD, 20));
        jpl_center0.add(jLabel1);
        jLabel1.setSize(200, 20);
        jLabel1.setLocation(200, 5);
        jpl_center0.add(jComboBox1);
        jComboBox1.setSize(new Dimension(200, 30));
        jComboBox1.setLocation(350, 5);
        tip1.setForeground(new Color(52, 143, 180));
        tip1.setFont(new Font("Calibri", Font.PLAIN, 15));
        tip1.setEditable(false);
        jpl_center0.add(tip1);
        tip1.setSize(210, 20);
        tip1.setLocation(120, 40);


        jpl_down.setLayout(null);
        jLabel2.setFont(new Font("微软雅黑", Font.BOLD, 20));
        jpl_down.add(jLabel2);
        jLabel2.setSize(300, 20);
        jLabel2.setLocation(50, 5);
        jpl_down.add(jComboBox2);
        jComboBox2.setSize(new Dimension(200, 30));
        jComboBox2.setLocation(350, 5);
        tip2.setForeground(new Color(52, 143, 180));
        tip2.setFont(new Font("Calibri", Font.PLAIN, 15));
        tip2.setEditable(false);
        jpl_down.add(tip2);
        tip2.setSize(180, 20);
        tip2.setLocation(150, 40);

        jpl_center.add(jpl_up);
        jpl_center.add(jpl_center0);
        jpl_center.add(jpl_down);
        this.add(jpl_center, BorderLayout.CENTER);
        this.setVisible(true);

        jComboBox1.addItem(" ");
        jComboBox2.addItem(" ");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);

        jComboBox2.addItem(files.get(0).getName());


        jComboBox1.addActionListener(this);
        jComboBox2.addActionListener(this);
    }
}