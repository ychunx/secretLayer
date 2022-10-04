package Main;

import UI.loginUI;
import Utils.fileIO;

import java.io.File;

public class mainExecution {

    public static String passwordHash;
    public static String password;

    public static void main(String[] args) {

        passwordHash = fileIO.readPass("data/password.txt");

        // 如果是第一次使用，则不存在目录，需删除残留文件，提高安全性
        // 即使突破了密码登录，但由于文件是由登录密码加密的，所以文件本身没有安全问题
        if (passwordHash.equals("")) {
            File catalog = new File("data/catalog.crypt");
            catalog.delete();
        }

        new loginUI().setVisible(true);
    }
}
