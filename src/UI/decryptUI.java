package UI;

import Main.cryptFile;
import Main.mainExecution;
import Utils.aesUtil;
import Utils.fileIO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
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
import java.util.ArrayList;

public class decryptUI {
    public static JPanel init() {

        JPanel jp = new JPanel();
        jp.setBounds(0,0,650,500);
        jp.setLayout(null);

        JTable jt = new JTable();
        DefaultTableModel dtm = new DefaultTableModel();
        String[] colName = {"文件名","加密时间"};

        // 获取已加密文件目录
        // 获取前先解密目录文件
        byte[] catalogBytes = fileIO.readFile("data/catalog.crypt");
        fileIO.writeFile(aesUtil.decrypt(catalogBytes, mainExecution.password), "data", "catalog.txt", 1);
        ArrayList<String> catalog = fileIO.readTxt("data/catalog.txt");
        // 读取目录后将目录明文删除
        File catalogFile = new File("data/catalog.txt");
        catalogFile.delete();

        String[][] allFiles = new String[catalog.size()][];
        for (int i = 0; i < catalog.size(); i++) {
            allFiles[i] = catalog.get(i).split(",");
        }
        dtm.setDataVector(allFiles,colName);
        jt.setModel(dtm);
        // 表格内容居中
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(JLabel.CENTER);
        jt.setDefaultRenderer(Object.class, tcr);
        JScrollPane jtsp = new JScrollPane(jt);
        jtsp.setBounds(10,40,350,415);
        jp.add(jtsp);

        JLabel jl1 = new JLabel("需解密文件路径：");
        jl1.setBounds(370,40,110,30);
        jp.add(jl1);

        JTextField srcPath1 = new JTextField();
        JTextArea srcPath2 = new JTextArea();
        srcPath1.setBounds(470,40,190,30);
        srcPath1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { }
            @Override
            public void focusLost(FocusEvent e) {
                srcPath2.setText(srcPath1.getText());
                srcPath2.setFont(new Font("微软雅黑", Font.PLAIN, 18));
            }
        });
        srcPath2.setText("\n\n\n   拖拽你需加密的文件到此区域\n\n\n             解密外部文件时\n      因程序没有记录文件数据\n 故需自行修改文件名和后缀格式");
        srcPath2.setLineWrap(true);
        srcPath2.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        srcPath2.setEditable(false);
        srcPath2.setBounds(370,80,290,250);
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
        jl2.setBounds(370,350,110,30);
        jp.add(jl2);

        JTextField descPath = new JTextField();
        descPath.setBounds(470,350,190,30);
        descPath.setText("请输入输出文件路径");
        descPath.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (descPath.getText().equals("请输入输出文件路径")) {
                    descPath.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (descPath.getText().equals("")) {
                    descPath.setText("请输入输出文件路径");
                }
            }
        });
        jp.add(descPath);

        JRadioButton jrb1 = new JRadioButton();
        JRadioButton jrb2 = new JRadioButton();
        jrb1.setBounds(105, 10, 200, 20);
        jrb2.setBounds(435, 10, 200, 20);
        jrb1.setText("解密内部文件");
        jrb2.setText("解密外部文件");
        jrb1.setFont(new Font("微软雅黑", Font.BOLD, 20));
        jrb2.setFont(new Font("微软雅黑", Font.BOLD, 20));
        ButtonGroup bg = new ButtonGroup();
        bg.add(jrb1);
        bg.add(jrb2);
        jrb1.setSelected(true);
        jp.add(jrb1);
        jp.add(jrb2);

        JButton jb = new JButton("解密");
        jb.setBounds(560,400,100,30);
        jb.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String desc = descPath.getText().trim();
                if (jrb1.isSelected()) {
                    int selected = jt.getSelectedRow();
                    if (selected < 0 || desc.equals("") || desc.equals("请输入输出文件路径")) {
                        JOptionPane.showMessageDialog(jp,"请在表中选择需解密文件或键入输出文件路径！","错误 ", JOptionPane.ERROR_MESSAGE);
                    } else {
                        cryptFile.decrypt("", desc, selected);
                        JOptionPane.showMessageDialog(jp, "操作成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    String src = srcPath1.getText().trim();
                    File temp = new File(src);
                    if (src.equals("") || desc.equals("") || desc.equals("请输入输出文件路径")) {
                        JOptionPane.showMessageDialog(jp,"请输入需解密文件路径或输出文件路径！","错误 ", JOptionPane.ERROR_MESSAGE);
                    } else if (!temp.exists()) {
                        JOptionPane.showMessageDialog(jp,"文件路径错误！","错误 ", JOptionPane.ERROR_MESSAGE);
                    } else {
                        cryptFile.decrypt(src, desc, -1);
                        srcPath1.setText("");
                        srcPath2.setText("\n\n\n   拖拽你需加密的文件到此区域\n\n\n             解密外部文件时\n      因程序没有记录文件数据\n 故需自行修改文件名和后缀格式");
                        srcPath2.setFont(new Font("微软雅黑", Font.PLAIN, 20));
                        JOptionPane.showMessageDialog(jp, "操作成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    }
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
