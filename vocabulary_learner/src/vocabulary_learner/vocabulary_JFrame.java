package vocabulary_learner;

import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import javax.swing.*;
import java.awt.*;//Dimension,LayoutManager
import java.math.*;

import static java.lang.Math.random;


public class vocabulary_JFrame extends JFrame implements Runnable, WindowListener, MouseListener {
    private BufferedReader server_input;
    private PrintStream user_output;
    FileWriter fileWriter_right;
    FileWriter fileWriter_wrong;

    private JPanel jpl_background_choose = new JPanel();
    private JPanel jpl_background_word = new JPanel();
    private JPanel jpl_background_tips = new JPanel();
    private JPanel jpl_background_set = new JPanel();
    private JButton jbt_choose_A = new JButton();
    private JButton jbt_choose_B = new JButton();
    private JButton jbt_choose_C = new JButton();
    private JButton jbt_choose_D = new JButton();
    private JButton jbt_increase = new JButton("↑");
    private JButton jbt_decrease = new JButton("↓");
    private JLabel jlb_word = new JLabel();
    private JLabel jlb_life = new JLabel();
    private JLabel jlb_grade = new JLabel();

    boolean game_is_running = true;
    String tmp = null;
    String[] chinese = new String[4];
    String[] english = new String[4];
    String right_answer = null;
    boolean time_is_over = false;
    boolean answer_is_right = false;
    boolean answer_is_wrong = false;
    int life = 10;
    int grade = 1;
    int time_ = 10;


    //
    public vocabulary_JFrame(BufferedReader server_input, PrintStream user_output, String title_name) {
        this.server_input = server_input;
        this.user_output = user_output;
        //delivery the Stream;
        this.addWindowListener(this);
        this.setResizable(false);
        //cannot change the size
        this.setUndecorated(true);
        // 去掉窗口的装饰
        this.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
        //采用指定的窗口装饰风格
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(title_name + " 's vocabulary_learner");
        String filename_right = "D:\\CODE\\" + "the_right_answered_words_of_" + title_name + ".txt";
        String filename_wrong = "D:\\CODE\\" + "the wrong answered words of " + title_name + ".txt";
        try {
            fileWriter_right = new FileWriter(filename_right, true);
            fileWriter_wrong = new FileWriter(filename_wrong, true);
        } catch (Exception e) {
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //the size of computer screen
//        int width = (int)screenSize.getWidth();
//        int height = (int)screenSize.getHeight();
        this.setSize((int) (screenSize.getWidth() * 0.5), (int) (screenSize.getHeight() * 0.9));
        this.setLocation((int) (screenSize.getWidth() * 0.25), (int) (screenSize.getHeight() * 0.05));
        this.setVisible(true);

        this.setLayout(new BorderLayout());

        Dimension screenSize_of_game_frame = this.getSize();
        //the size of game_frame screen

        this.add(jpl_background_word, BorderLayout.CENTER);

        jpl_background_word.setBackground(new Color(226, 177, 137));
        this.add(jpl_background_choose, BorderLayout.SOUTH);

        jpl_background_choose.setBackground(new Color(130, 167, 216));
        jpl_background_choose.setPreferredSize(new Dimension((int) (screenSize_of_game_frame.getWidth()), (int) (screenSize_of_game_frame.getHeight() * 0.2)));

        this.add(jpl_background_tips, BorderLayout.WEST);
        jpl_background_tips.setBackground(new Color(255, 255, 255));
        jpl_background_tips.setPreferredSize(new Dimension((int) (screenSize_of_game_frame.getWidth() * 0.1), (int) (screenSize_of_game_frame.getHeight() * 0.8)));

        jpl_background_choose.setLayout(new GridLayout(2, 2, 20, 20));
        jpl_background_choose.add(jbt_choose_A);
        jpl_background_choose.add(jbt_choose_B);
        jpl_background_choose.add(jbt_choose_C);
        jpl_background_choose.add(jbt_choose_D);

        jbt_choose_A.addMouseListener(this);
        jbt_choose_B.addMouseListener(this);
        jbt_choose_C.addMouseListener(this);
        jbt_choose_D.addMouseListener(this);

        jbt_choose_A.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        jbt_choose_B.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        jbt_choose_C.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        jbt_choose_D.setFont(new Font("微软雅黑", Font.PLAIN, 20));

        jbt_choose_A.setForeground(new Color(0, 0, 0));
        jbt_choose_B.setForeground(new Color(0, 0, 0));
        jbt_choose_C.setForeground(new Color(0, 0, 0));
        jbt_choose_D.setForeground(new Color(0, 0, 0));

        jbt_choose_A.setBackground(new Color(255, 255, 255));
        jbt_choose_B.setBackground(new Color(255, 255, 255));
        jbt_choose_C.setBackground(new Color(255, 255, 255));
        jbt_choose_D.setBackground(new Color(255, 255, 255));

        jlb_word.setFont(new Font("微软雅黑", Font.BOLD, 30));
//        jlb_word.setText("word");
        jpl_background_word.add(jlb_word);
        jlb_word.setPreferredSize(new Dimension((int) (screenSize_of_game_frame.getWidth() * 0.9), (int) (screenSize_of_game_frame.getHeight() * 0.05)));
        jlb_word.setLocation(250, 0);


        jpl_background_tips.setLayout(new GridLayout(2, 1, 0, 0));
        jpl_background_tips.add(jpl_background_set);
        jpl_background_set.setLayout(new GridLayout(3, 1, 1, 1));
        jbt_increase.setFont(new Font("微软雅黑", Font.BOLD, 30));
        jbt_increase.setBackground(new Color(130, 167, 216));
        jpl_background_set.add(jbt_increase);
        jlb_grade.setFont(new Font("微软雅黑", Font.BOLD, 15));
        jpl_background_set.add(jlb_grade);
        jlb_grade.setText(" grade: " + grade);
        jbt_decrease.setFont(new Font("微软雅黑", Font.BOLD, 30));
        jbt_decrease.setBackground(new Color(130, 167, 216));
        jpl_background_set.add(jbt_decrease);

        jbt_increase.addMouseListener(this);
        jbt_decrease.addMouseListener(this);

        jlb_life.setFont(new Font("微软雅黑", Font.BOLD, 20));
        jpl_background_tips.add(jlb_life);
        jlb_life.setPreferredSize(new Dimension(100, 30));
        jlb_life.setText(" life: " + life);


        new Thread(this).start();
    }

    @Override
    public void run() {
        while (game_is_running) {
            int word_location_high = 0;
            jbt_choose_A.setBackground(new Color(255, 255, 255));
            jbt_choose_B.setBackground(new Color(255, 255, 255));
            jbt_choose_C.setBackground(new Color(255, 255, 255));
            jbt_choose_D.setBackground(new Color(255, 255, 255));
            time_is_over = false;
            answer_is_right = false;
            answer_is_wrong = false;

            jlb_word.setLocation(250, word_location_high);

            for (int i = 0; i < 4; i++) {
                user_output.println("next");
                try {
                    tmp = server_input.readLine();
//                    System.out.println(tmp);
                    if (tmp.equals("-9999")) {
                        game_is_running = false;
//                        user_output.println("exit");
                        try {
                            fileWriter_right.close();
                            fileWriter_wrong.close();
                        } catch (Exception e1) {
                        }
                        JOptionPane.showMessageDialog(null, "the administrator has ended your game!");
                        this.dispose();
                    } else {
                        english[i] = tmp.substring(0, 24);
                        chinese[i] = tmp.substring(24);
                        english[i].trim();
                        chinese[i].trim();
                    }
                } catch (Exception e) {
                }
            }
            float a = (float) random();
            if (a >= 0.75) {
                right_answer = "A";
                //set A is right answer
                jlb_word.setText(english[0]);
                jbt_choose_A.setText(chinese[0]);
                jbt_choose_B.setText(chinese[1]);
                jbt_choose_C.setText(chinese[2]);
                jbt_choose_D.setText(chinese[3]);
            } else if (a >= 0.5) {
                right_answer = "B";
                //set B is right answer
                jlb_word.setText(english[1]);
                jbt_choose_A.setText(chinese[0]);
                jbt_choose_B.setText(chinese[1]);
                jbt_choose_C.setText(chinese[2]);
                jbt_choose_D.setText(chinese[3]);
            } else if (a >= 0.25) {
                right_answer = "C";
                //set C is right answer
                jlb_word.setText(english[2]);
                jbt_choose_A.setText(chinese[0]);
                jbt_choose_B.setText(chinese[1]);
                jbt_choose_C.setText(chinese[2]);
                jbt_choose_D.setText(chinese[3]);
            } else if (a >= 0) {
                right_answer = "D";
                //set D is right answer
                jlb_word.setText(english[3]);
                jbt_choose_A.setText(chinese[0]);
                jbt_choose_B.setText(chinese[1]);
                jbt_choose_C.setText(chinese[2]);
                jbt_choose_D.setText(chinese[3]);
            }
            while (!(time_is_over || answer_is_right || answer_is_wrong)) {
                jlb_word.setLocation(250, word_location_high);
                word_location_high += 1;
                try {
                    Thread.sleep(time_);
                } catch (Exception e) {
                }
                if (word_location_high >= 550) {
                    time_is_over = true;
                }
            }
            if (time_is_over) {
                life = life - 1;
                jlb_word.setText(null);
                jlb_life.setText(" life: " + life);
                try {
                    if (right_answer.equals("A")) {
                        fileWriter_wrong.write(english[0] + "    " + chinese[0] + "\n");
                    } else if (right_answer.equals("B")) {
                        fileWriter_wrong.write(english[1] + "    " + chinese[1] + "\n");
                    } else if (right_answer.equals("C")) {
                        fileWriter_wrong.write(english[2] + "    " + chinese[2] + "\n");
                    } else if (right_answer.equals("D")) {
                        fileWriter_wrong.write(english[3] + "    " + chinese[3] + "\n");
                    }
                    fileWriter_wrong.flush();
                } catch (Exception e) {
                }
                switch (right_answer) {
                    case "A" -> {
                        jbt_choose_A.setBackground(new Color(146, 216, 148));
                        jbt_choose_B.setBackground(new Color(235, 121, 121));
                        jbt_choose_C.setBackground(new Color(235, 121, 121));
                        jbt_choose_D.setBackground(new Color(235, 121, 121));
                    }
                    case "B" -> {
                        jbt_choose_A.setBackground(new Color(235, 121, 121));
                        jbt_choose_B.setBackground(new Color(146, 216, 148));
                        jbt_choose_C.setBackground(new Color(235, 121, 121));
                        jbt_choose_D.setBackground(new Color(235, 121, 121));
                    }
                    case "C" -> {
                        jbt_choose_A.setBackground(new Color(235, 121, 121));
                        jbt_choose_B.setBackground(new Color(235, 121, 121));
                        jbt_choose_C.setBackground(new Color(146, 216, 148));
                        jbt_choose_D.setBackground(new Color(235, 121, 121));
                    }
                    default -> {
                        jbt_choose_A.setBackground(new Color(235, 121, 121));
                        jbt_choose_B.setBackground(new Color(235, 121, 121));
                        jbt_choose_C.setBackground(new Color(235, 121, 121));
                        jbt_choose_D.setBackground(new Color(146, 216, 148));
                    }
                }
                try {
                    Thread.sleep(2000);
                } catch (Exception ignored) {
                }
            } else if (answer_is_wrong) {
                life = life - 2;
                jlb_word.setText(null);
                jlb_life.setText(" life: " + life);
                if (life > 0) {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception ignored) {
                    }
                }
            } else if (answer_is_right) {
                life = life + 1;
                jlb_word.setText(null);
                jlb_life.setText(" life: " + life);
                try {
                    Thread.sleep(500);
                } catch (Exception ignored) {
                }
            }

            if (life <= 0) {
                game_is_running = false;
                user_output.println("exit");
                JOptionPane.showMessageDialog(null, "life point is 0\ngame over!");
                this.dispose();
            }


        }


    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        user_output.println("exit");
        try {
            fileWriter_right.close();
            fileWriter_wrong.close();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource().equals(jbt_choose_A) || e.getSource().equals(jbt_choose_B) || e.getSource().equals(jbt_choose_C) || e.getSource().equals(jbt_choose_D)) {
            try {
                if (e.getSource().equals(jbt_choose_A)) {
                    if (right_answer.equals("A")) {
                        answer_is_right = true;
                        fileWriter_right.write(english[0] + "    " + chinese[0] + "\n");
                    } else {
                        answer_is_wrong = true;
                    }
                } else if (e.getSource().equals(jbt_choose_B)) {
                    if (right_answer.equals("B")) {
                        answer_is_right = true;
                        fileWriter_right.write(english[1] + "    " + chinese[1] + "\n");
                    } else {
                        answer_is_wrong = true;
                    }
                } else if (e.getSource().equals(jbt_choose_C)) {
                    if (right_answer.equals("C")) {
                        answer_is_right = true;
                        fileWriter_right.write(english[2] + "    " + chinese[2] + "\n");
                    } else {
                        answer_is_wrong = true;
                    }
                } else if (e.getSource().equals(jbt_choose_D)) {
                    if (right_answer.equals("D")) {
                        answer_is_right = true;
                        fileWriter_right.write(english[3] + "    " + chinese[3] + "\n");
                    } else {
                        answer_is_wrong = true;
                    }

                }
                fileWriter_right.flush();
                if (answer_is_wrong) {
                    switch (right_answer) {
                        case "A" -> fileWriter_wrong.write(english[0] + "    " + chinese[0] + "\n");
                        case "B" -> fileWriter_wrong.write(english[1] + "    " + chinese[1] + "\n");
                        case "C" -> fileWriter_wrong.write(english[2] + "    " + chinese[2] + "\n");
                        case "D" -> fileWriter_wrong.write(english[3] + "    " + chinese[3] + "\n");
                    }
                }
                fileWriter_wrong.flush();
            } catch (Exception ignored) {
            }

            switch (right_answer) {
                case "A" -> {
                    jbt_choose_A.setBackground(new Color(146, 216, 148));
                    jbt_choose_B.setBackground(new Color(235, 121, 121));
                    jbt_choose_C.setBackground(new Color(235, 121, 121));
                    jbt_choose_D.setBackground(new Color(235, 121, 121));
                }
                case "B" -> {
                    jbt_choose_A.setBackground(new Color(235, 121, 121));
                    jbt_choose_B.setBackground(new Color(146, 216, 148));
                    jbt_choose_C.setBackground(new Color(235, 121, 121));
                    jbt_choose_D.setBackground(new Color(235, 121, 121));
                }
                case "C" -> {
                    jbt_choose_A.setBackground(new Color(235, 121, 121));
                    jbt_choose_B.setBackground(new Color(235, 121, 121));
                    jbt_choose_C.setBackground(new Color(146, 216, 148));
                    jbt_choose_D.setBackground(new Color(235, 121, 121));
                }
                default -> {
                    jbt_choose_A.setBackground(new Color(235, 121, 121));
                    jbt_choose_B.setBackground(new Color(235, 121, 121));
                    jbt_choose_C.setBackground(new Color(235, 121, 121));
                    jbt_choose_D.setBackground(new Color(146, 216, 148));
                }
            }
        } else {
            if (e.getSource().equals(jbt_increase)) {
                if (grade < 5) {
                    grade++;
                    time_--;
                    jlb_grade.setText(" grade: " + grade);
                }
            } else if (e.getSource().equals(jbt_decrease)) {
                if (grade > 1) {
                    grade--;
                    time_++;
                    jlb_grade.setText(" grade: " + grade);
                }
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

    public static void main(String args[]) throws Exception {
//        new vocabulary_JFrame();
    }


}
