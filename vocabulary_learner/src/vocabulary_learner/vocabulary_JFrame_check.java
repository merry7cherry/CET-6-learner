package vocabulary_learner;

import javax.swing.*;
import java.awt.*;
import java.io.*;
//Dimension,LayoutManager

public class vocabulary_JFrame_check extends JFrame{
    //private JPanel jpl = new JPanel();
    //private BorderLayout borderLayout=new BorderLayout();

    public JPanel createPC(String pathname)throws Exception {
        BufferedReader input=null;
        File text=new File(pathname);
        if(text.exists()){
            input = new BufferedReader(new InputStreamReader(new FileInputStream(text)));
        }else{
            System.out.println("error!");
        }
        JPanel pc=new JPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JTextArea jTextArea=new JTextArea();
        jTextArea.setEditable(false);
        jTextArea.read(input,"READING FILE :-)");

        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setPreferredSize(new Dimension((int) (screenSize.getWidth() * 0.49), (int) (screenSize.getHeight() * 0.85)));
        //the size are obtained by trying many times
        pc.add(jScrollPane);


////分别设置水平和垂直滚动条自动出现
//        jScrollPane.setHorizontalScrollBarPolicy(
//                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        jScrollPane.setVerticalScrollBarPolicy(
//                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

//分别设置水平和垂直滚动条总是出现
        jScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

////分别设置水平和垂直滚动条总是隐藏
//        jScrollPane.setHorizontalScrollBarPolicy(
//                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        jScrollPane.setVerticalScrollBarPolicy(
//                JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        return pc;
    }
    public JPanel createPN() {
        JPanel pn=new JPanel();
        return pn;
    }
    public JPanel createPS() {
        JPanel ps=new JPanel();
        return ps;
    }
    public JPanel createPE() {
        JPanel pe=new JPanel();
        return pe;
    }
    public JPanel createPW() {
        JPanel pw=new JPanel();
        return pw;
    }
    public vocabulary_JFrame_check(String title_name,String pathname)throws Exception {
        this.setResizable(false);
        //the size of the frame cannot be change
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(title_name);
        //component
        this.setLayout(new BorderLayout());
        this.add(createPC(pathname),BorderLayout.CENTER);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int width = (int)screenSize.getWidth();
//        int height = (int)screenSize.getHeight();
        this.setSize((int) (screenSize.getWidth() * 0.5), (int) (screenSize.getHeight() * 0.9));
        this.setLocation((int) (screenSize.getWidth() * 0.25), (int) (screenSize.getHeight() * 0.05));
        this.setVisible(true);


    }

    public static void main(String args[])throws Exception {

        new vocabulary_JFrame_check("learned","D:\\CODE\\vocabulary_learner\\src\\vocabulary_learner\\CET_6");
    }

}
