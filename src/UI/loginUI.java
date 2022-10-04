package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import Main.mainExecution;
import Utils.fileIO;
import Utils.md5CalcUtil;

public class loginUI extends JFrame {

    public loginUI() {

        super("文件加密工具");

        int WIDTH = 500;
        int HEIGHT = 350;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((int)(screenSize.getWidth()-WIDTH)/2,(int)(screenSize.getHeight()-HEIGHT)/2,WIDTH,HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        init();
    }

    public void init() {

        JPanel JP = new JPanel();
        JP.setLayout(null);
        JP.add(setPage());
        this.setResizable(false);
        this.add(JP);
    }

    public void close() {
        this.dispose();
    }

    public JPanel setPage() {

        JPanel jp = new JPanel();
        jp.setBounds(0,0,500,350);
        jp.setLayout(null);

        JLabel title = new JLabel("登录界面");
        title.setBounds(200,40,240,30);
        title.setFont(new Font("微软雅黑", Font.BOLD, 20));
        jp.add(title);

        JLabel jl2 = new JLabel("密码：");
        jl2.setBounds(100,80,120,40);
        jp.add(jl2);

        JPasswordField dbpass = new JPasswordField();
        dbpass.setBounds(100,120,280,40);
        dbpass.addKeyListener(new KeyAdapter() {
            @Override
            //enter 添加事件
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER ) {
                    char[] passArr = dbpass.getPassword();
                    StringBuilder pass = new StringBuilder();
                    String correctPass = mainExecution.passwordHash;
                    for (char c : passArr) {
                        pass.append(c);
                    }
                    if (pass.length() > 16 || pass.length() < 6) {
                        JOptionPane.showMessageDialog(jp,"请输入6-16位密码以保证安全性！","错误 ", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (correctPass.equals("")){
                            mainExecution.password = pass.toString();
                            fileIO.savePass(md5CalcUtil.md5Calc(pass.toString()), "data/password.txt");
                            new mainUI().setVisible(true);
                            close();
                        }else if (correctPass.equals(md5CalcUtil.md5Calc(pass.toString()))) {
                            mainExecution.password = pass.toString();
                            new mainUI().setVisible(true);
                            close();
                        } else {
                            JOptionPane.showMessageDialog(jp,"密码错误！","错误 ", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        jp.add(dbpass);

        JButton jb = new JButton("登录");
        jb.setBounds(100,190,280,40);
        jb.setBackground(new Color(202,204,209));
        jb.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                char[] passArr = dbpass.getPassword();
                StringBuilder pass = new StringBuilder();
                String correctPass = mainExecution.passwordHash;
                for (char c : passArr) {
                    pass.append(c);
                }
                if (pass.length() > 16 || pass.length() < 6) {
                    JOptionPane.showMessageDialog(jp,"请输入6-16位密码以保证安全性！","错误 ", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (correctPass.equals("")){
                        mainExecution.password = pass.toString();
                        fileIO.savePass(md5CalcUtil.md5Calc(pass.toString()), "data/password.txt");
                        new mainUI().setVisible(true);
                        close();
                    }else if (correctPass.equals(md5CalcUtil.md5Calc(pass.toString()))) {
                        mainExecution.password = pass.toString();
                        new mainUI().setVisible(true);
                        close();
                    } else {
                        JOptionPane.showMessageDialog(jp,"密码错误！","错误 ", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        jp.add(jb);

        JLabel footer = new JLabel("如果是第一次使用则直接输入设置密码登录即可");
        footer.setBounds(10,275,460,30);
        jp.add(footer);

        return jp;
    }
}