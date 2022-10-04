package Utils;

import Main.mainExecution;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class fileIO {

    public static String readPass(String txtPath) {
        StringBuilder str = new StringBuilder();
        String strALine;    // 按行读取
        File file = new File(txtPath);  // 读取文件
        try {
            // 获取输入字节流
            FileInputStream bs = new FileInputStream(file);
            // 将字节流解码为字符流
            InputStreamReader cs = new InputStreamReader(bs, StandardCharsets.UTF_8);
            // 缓冲区，提高字符流读写的效率
            BufferedReader bf = new BufferedReader(cs);
            // 拼接字符串
            while ((strALine = bf.readLine()) != null) {
                str.append(strALine);
            }
            // 关闭资源
            bs.close();
            cs.close();
            bf.close();
        } catch (IOException e) {
            //e.printStackTrace();
            return "";
        }
        return str.toString();
    }

    public static void savePass(String str,String txtPath){
        File file = new File(txtPath);  // 写入 txtPath 路径文件

        // 第一次使用则不存在密码文件则先创建文件夹
        if (!file.exists()) {
            File folder = new File("data");
            folder.mkdirs();
        }

        try {
            FileWriter fw = new FileWriter(file, false);     // false 为替换
            fw.write(str);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] readFile(String srcPath) {
        byte[] bs = new byte[0];
        try {
            // 如果第一次使用且读取为目录文件，则需要先创建该文件
            if (srcPath.equals("data/catalog.crypt")) {
                File file = new File(srcPath);
                file.createNewFile();
            }

            InputStream is = Files.newInputStream(Paths.get(srcPath));
            int iAvail = is.available();
            bs = new byte[iAvail];
            is.read(bs);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bs;
    }

    public static void writeFile(byte[] descBytes, String descPath, String fileName, int type){

        String fullPath;
        if (type == 0) {
            fullPath = descPath + '/' + fileName + ".crypt";
        } else {
            fullPath = descPath + '/' + fileName;
        }
        File file = new File(fullPath);

        File tempFile = new File(descPath);
        // 如果不存在文件夹则先创建文件夹
        if (!tempFile.exists()) {
            File folder = new File(descPath);
            folder.mkdirs();
        }

        try {
            FileOutputStream fow = new FileOutputStream(file);
            fow.write(descBytes);
            // 关闭资源
            fow.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readTxt(String txtPath) {
        ArrayList<String> catalog = new ArrayList<>();
        String strALine;    // 按行读取
        File file = new File(txtPath);  // 读取文件

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileInputStream bs = new FileInputStream(file);
            InputStreamReader cs = new InputStreamReader(bs, StandardCharsets.UTF_8);
            BufferedReader bf = new BufferedReader(cs);
            while ((strALine = bf.readLine()) != null) {
                catalog.add(strALine + '\n');
            }
            // 关闭资源
            bs.close();
            cs.close();
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return catalog;
    }

    public static void writeTxt(ArrayList<String> catalog,String txtPath){
        File file = new File(txtPath);  // 写入 txtPath 路径文件
        try {
            FileWriter fw = new FileWriter(file, false);
            for (String s : catalog) {
                fw.write(s);
            }
            // 关闭资源
            fw.close();

            // 加密目录文件
            byte[] catalogBytes = readFile("data/catalog.txt");
            writeFile(aesUtil.encrypt(catalogBytes, mainExecution.password), "data", "catalog", 0);
            // 删除未加密文件
            File catalogFile = new File("data/catalog.txt");
            catalogFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
