/***************************************************/
/*    https://www.guru99.com/java-swing-gui.html   */
/***************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Form {

    private static JFrame form;
    private static JTextArea ta;
    private static JPanel panel;
    private static JButton yes;
    private static JButton no;

/****************************************************/
/**************         BUILD      ******************/
/****************************************************/
/****************************************************/
    public Form(NodeMap map) {
        createFrame(map);
        nodeDisplay(map);
    }

    private static void createFrame(NodeMap map){
        form = new JFrame("Perec");
        form.setSize(400,600);
        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form.setResizable(false);
        form.setLocationRelativeTo(null);
        form.setVisible(true);

        form.setLayout(new BorderLayout());
        form.setContentPane(new JLabel(new ImageIcon("C:\\Users\\adamt\\background.jpg")));
        form.setLayout(new FlowLayout());

        form.setSize(399,599);
        form.setSize(400,600);

        JTextArea text = createTextArea3();
        JTextArea t = createTextArea4();
        createPanel();
        createButtons(map);
        createTextArea();
        JTextArea txt = createTextArea2();

        panel.add(yes);
        panel.add(no);
        //Adding Components to the frame.

        form.getContentPane().add(BorderLayout.CENTER, txt);

        //form.getContentPane().add(BorderLayout.CENTER, text);
        form.getContentPane().add(BorderLayout.CENTER, ta);
        form.getContentPane().add(BorderLayout.CENTER, t);

        form.getContentPane().add(BorderLayout.NORTH, panel);
    }

    private static void createPanel(){
        panel = new JPanel(); // the panel is not visible in output
        panel.setOpaque(false);
        setColors(panel, Color.lightGray,Color.darkGray);
    }

    private static void createButtons(NodeMap map){
        yes = new JButton("Yes");
        yes.setFont(createFont(50));
        setColors(yes,Color.darkGray, Color.green);
        yes.addActionListener((ActionEvent e) -> {
            move(map,1);
            nodeDisplay(map);
        });

        no = new JButton("No");
        no.setFont(createFont(50));
        setColors(no,Color.darkGray, Color.red);
        no.addActionListener((ActionEvent e) -> {
            move(map,2);
            nodeDisplay(map);
        });
    }


    private static void createTextArea(){
        ta = new JTextArea();
        int width = 350;
        int height = 300;
        ta.setSize(width, height);
        //ta.setOpaque(false);
        ta.setFont(createFont(28));
        ta.setBackground(Color.lightGray);
        ta.setForeground(Color.blue);
    }

    public static JTextArea createTextArea2(){
        JTextArea txt = new JTextArea();
        int width = 350;
        int height = 300;
        txt.setSize(width, height);
        //txt.setOpaque(false);
        txt.setFont(createFont(28));
        txt.setBackground(Color.lightGray);
        txt.setForeground(Color.blue);
        txt.setText("Which Sport should I take up?");
        txt.append("\nThis is a quiz that will advise\n you on a sport to take up");
        return txt;
    }

    public static JTextArea createTextArea3(){
        JTextArea text = new JTextArea();
        int width = 350;
        int height = 300;
        text.setSize(width, height);
        //text.setOpaque(false);
        text.setFont(createFont(28));
        text.setBackground(Color.lightGray);
        text.setForeground(Color.BLACK);
        text.setText("This is a quiz that will advise");
        text.append("\n you on a sport to take up");
        return text;
    }

    public static JTextArea createTextArea4(){
        JTextArea t = new JTextArea();
        t.setOpaque(false);
        t.setFont(createFont(28));
        t.setBackground(Color.lightGray);
        t.setForeground(Color.BLACK);
        t.setText("\n\n\n\n\n\n\n\n");
        return t;
    }

/****************************************************/
/**************      NAVIGATE       *****************/
/****************************************************/
/****************************************************/
    private static void move(NodeMap map, int direction){
        if (map.currentNode().getQuestion().equals("-"))
        {
            map.noDecision(); }
        else {
            map.decision(direction); }
    }

    private static void nodeDisplay(NodeMap map){
        //ta.setText( map.currentNode().getDescription() + "\n");
        ta.setText(map.currentNode().getQuestion());
        ta.append("\n\n" + "You must answer the questions either Yes or No");
        ta.setLineWrap(true);
        if (map.currentNode().getQuestion().equals("-")){
            yes.setFont(createFont(40));
            yes.setText("pressEnterToContinue");
            no.setVisible(false);
        }
        else {
            no.setVisible(true);
            yes.setFont(createFont(50));
            yes.setText("Yes");
        }
    }



/***************************************************/
/*                      HELPERS                    */
/***************************************************/

    private static void setColors(JComponent object, Color bg, Color fg){
        object.setBackground(bg);
        object.setForeground(fg);
    }
    private static Font createFont(int size){
        return  new Font("Courier", Font.PLAIN,size);
        //return  new Font("Atlantis International", Font.PLAIN,size);
    }
}
