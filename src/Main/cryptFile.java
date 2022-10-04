package Main;

import UI.decryptUI;
import UI.mainUI;
import Utils.aesUtil;
import Utils.fileIO;

import javax.swing.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class cryptFile {

    public static void encrypt(String src, String desc) {

        File tempFile = new File(src);
        // 读取源文件名
        String fileName = tempFile.getName();
        // 获取时间
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String curDate = dateFormat.format(date);

        byte[] srcBytes = fileIO.readFile(src);

        fileIO.writeFile(aesUtil.encrypt(srcBytes, mainExecution.password), desc, curDate, 0);

        // 存储在外部的加密文件不记录
        if (desc.equals("data/encryptedFile")) {
            // 获取已加密文件目录
            // 获取前先解密目录文件
            byte[] catalogBytes = fileIO.readFile("data/catalog.crypt");
            fileIO.writeFile(aesUtil.decrypt(catalogBytes, mainExecution.password), "data", "catalog.txt", 1);
            // 将加密目录文件删除，后续会新生成
            File catalogFile = new File("data/catalog.crypt");
            catalogFile.delete();
            ArrayList<String> catalog = fileIO.readTxt("data/catalog.txt");
            // 保持新增文件目录
            catalog.add(fileName + ',' + curDate);
            fileIO.writeTxt(catalog, "data/catalog.txt");
        }
    }

    public static void decrypt(String src, String desc, int selected) {

        if (selected < 0) {
            File tempFile = new File(src);
            String fileName = tempFile.getName();

            byte[] srcBytes = fileIO.readFile(src);

            fileIO.writeFile(aesUtil.decrypt(srcBytes, mainExecution.password), desc, fileName, 1);
        } else {
            // 获取已加密文件目录
            // 获取前先解密目录文件
            byte[] catalogBytes = fileIO.readFile("data/catalog.crypt");
            fileIO.writeFile(aesUtil.decrypt(catalogBytes, mainExecution.password), "data", "catalog.txt", 1);
            ArrayList<String> catalog = fileIO.readTxt("data/catalog.txt");
            // 读取目录后将目录明文删除
            File catalogFile = new File("data/catalog.txt");
            catalogFile.delete();

            String srcFileName = catalog.get(selected).split(",")[0];   // 源文件名
            String fileName = catalog.get(selected).split(",")[1];  // 加密后文件名
            String srcPath = "data/encryptedFile/" + fileName + ".crypt";
            srcPath = srcPath.replace("\n","");

            byte[] srcBytes = fileIO.readFile(srcPath);
            fileIO.writeFile(aesUtil.decrypt(srcBytes, mainExecution.password), desc, srcFileName, 1);

            File tempFile = new File(srcPath);
            if (tempFile.delete()) {
                catalog.remove(selected);
                fileIO.writeTxt(catalog, "data/catalog.txt");
            }

            // 刷新页面
            JPanel jp = decryptUI.init();
            mainUI.jsp.setRightComponent(jp);
            mainUI.jsp.setDividerLocation(100);
        }
    }
}
