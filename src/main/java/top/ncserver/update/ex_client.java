package top.ncserver.update;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ex_client {
    public static final Logger logger = Logger.getLogger(ex_client.class);
    public static boolean ex_check() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        File folder = new File(".minecraft");
        File directory = new File("");
        if (!folder.exists() && !folder.isDirectory()) {

            org.apache.commons.net.ftp.FTPClient ftpClient = FTPClient.getFTPClient("nas.ncserver.top", 21, "update", "n~7z26");
            String server_version = FTPClient.check_Weeks_orders(ftpClient);
            assert server_version != null;
            JOptionPane.showMessageDialog(null, "未检测到任何服务器文件，开始下载最新客户端，最新版本：" + server_version);
            FTPClient.readFileByFolder(ftpClient, server_version.charAt(0)+"周目/");
            String flag = FTPClient.downFile(ftpClient, server_version.charAt(0) + "周目/",
                    "V" + server_version + ".zip", directory.getAbsolutePath());
            if (flag.equals("下载成功 ！")) {

                JOptionPane.showMessageDialog(null, "下载完成，开始解压");
                if (zip.decompressZip(directory.getAbsolutePath() + "/" + "V" + server_version + ".zip", directory.getAbsolutePath() + "/")) {

                    JOptionPane.showMessageDialog(null, "解压完成，客户端安装成功");
                    File client=new File("version.txt");
                    if(!client.exists())
                    {
                        FileWriter writer =new FileWriter("version.txt");
                        writer.write("V"+server_version);
                        writer.close();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "解压失败", "错误", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            } else {
                JOptionPane.showMessageDialog(null, "下载失败", "错误", JOptionPane.ERROR_MESSAGE);
                logger.error(flag);
                System.exit(0);
            }

            return false;
        } else {
            return true;
        }
        //return false;
    }

}
