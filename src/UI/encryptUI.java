package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import Main.cryptFile;

public class encryptUI {
    public static JPanel init() {

        JPanel jp = new JPanel();
        jp.setBounds(0,0,700,500);
        jp.setLayout(null);

        JLabel title = new JLabel("加密");
        title.setBounds(330,10,40,20);
        title.setFont(new Font("微软雅黑", Font.BOLD, 20));
        jp.add(title);

        JLabel jl1 = new JLabel("需加密文件路径：");
        jl1.setBounds(20,40,120,30);
        jp.add(jl1);

        JTextField srcPath1 = new JTextField();
        JTextArea srcPath2 = new JTextArea();
        srcPath1.setBounds(120,40,540,30);
        srcPath1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { }
            @Override
            public void focusLost(FocusEvent e) {
                srcPath2.setText(srcPath1.getText());
                srcPath2.setFont(new Font("微软雅黑", Font.PLAIN, 18));
            }
        });
        srcPath2.setText("\n\n            拖拽你需加密的文件到此区域");
        srcPath2.setLineWrap(true);
        srcPath2.setFont(new Font("微软雅黑", Font.PLAIN, 30));
        srcPath2.setEditable(false);
        srcPath2.setBounds(20,80,640,250);
        // 文本域拖拽文件功能
        new DropTarget(srcPath2, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter()
        {
            @Override
            public void drop(DropTargetDropEvent dtde)
            {
                try
                {
                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))//如果拖入的文件格式受支持
                    {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);//接收拖拽来的数据
                        srcPath2.setFont(new Font("微软雅黑", Font.PLAIN, 18));
                        String tempPath = dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor).toString();
                        srcPath1.setText(tempPath.substring(1, tempPath.length()-1));
                        srcPath2.setText(tempPath.substring(1, tempPath.length()-1));
                        dtde.dropComplete(true);//指示拖拽操作已完成
                    }
                    else
                    {
                        dtde.rejectDrop();//否则拒绝拖拽来的数据
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        jp.add(srcPath1);
        jp.add(srcPath2);

        JLabel jl2 = new JLabel("输出文件路径：");
        jl2.setBounds(20,350,120,30);
        jp.add(jl2);

        JTextField descPath = new JTextField();
        descPath.setBounds(120,350,540,30);
        descPath.setText("该值为空时，加密文件存在程序数据中，否则存入该路径（不支持指定文件名）");
        descPath.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (descPath.getText().equals("该值为空时，加密文件存在程序数据中，否则存入该路径（不支持指定文件名）")) {
                    descPath.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (descPath.getText().equals("")) {
                    descPath.setText("该值为空时，加密文件存在程序数据中，否则存入该路径（不支持指定文件名）");
                }
            }
        });
        jp.add(descPath);

        JButton jb = new JButton("加密");
        jb.setBounds(560,400,100,30);
        jb.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String src = srcPath1.getText().trim();
                String desc = descPath.getText().trim();
                File temp = new File(src);
                if (src.equals("")) {
                    JOptionPane.showMessageDialog(jp,"请输入需加密文件路径！","错误 ", JOptionPane.ERROR_MESSAGE);
                } else if (!temp.exists()) {
                    JOptionPane.showMessageDialog(jp,"文件路径错误！","错误 ", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (desc.equals("该值为空时，加密文件存在程序数据中，否则存入该路径（不支持指定文件名）")) {
                        desc = "data/encryptedFile";
                    }
                    cryptFile.encrypt(src, desc);
                    srcPath1.setText("");
                    srcPath2.setText("\n\n            拖拽你需加密的文件到此区域");
                    srcPath2.setFont(new Font("微软雅黑", Font.PLAIN, 30));
                    JOptionPane.showMessageDialog(jp, "操作成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });
        jp.add(jb);

        return jp;
    }
}
