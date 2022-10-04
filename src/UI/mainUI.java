package UI;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class mainUI extends JFrame {

    public static JSplitPane jsp = new JSplitPane();   //分割面板

    public mainUI() {

        super("文件加密工具");

        int WIDTH = 800;
        int HEIGHT = 500;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((int)(screenSize.getWidth()-WIDTH)/2,(int)(screenSize.getHeight()-HEIGHT)/2,WIDTH,HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        init();
    }

    public void init() {

        jsp.setContinuousLayout(true);  //连续布局
        jsp.setDividerLocation(150);
        jsp.setDividerSize(7);
        this.add(jsp);

        //左侧内容
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("系统功能");
        DefaultMutableTreeNode encrypt = new DefaultMutableTreeNode("加密模块");
        DefaultMutableTreeNode decrypt = new DefaultMutableTreeNode("解密模块");
        root.add(encrypt);
        root.add(decrypt);
        JTree tree = new JTree(root);
        jsp.setLeftComponent(tree);

        //设置页面
        //当选中时执行
        tree.addTreeSelectionListener(e -> {
            Object path = e.getNewLeadSelectionPath().getLastPathComponent();
            if(encrypt.equals(path)){
                JPanel jp = encryptUI.init();
                jsp.setRightComponent(jp);
                jsp.setDividerLocation(100);
            } else if (decrypt.equals(path)){
                JPanel jp = decryptUI.init();
                jsp.setRightComponent(jp);
                jsp.setDividerLocation(100);
            }
        });
        tree.setSelectionRow(1);
    }
}
