package top.ncserver.update;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


import java.util.Enumeration;
import java.util.zip.ZipInputStream;

public class zip {
    /**
     * 解压文件
     *
     * @param zipPath 要解压的目标文件
     * @param descDir 指定解压目录
     * @return 解压结果：成功，失败
     */
    public static final Logger logger = Logger.getLogger(zip.class);

    public static boolean decompressZip(String zipPath, String descDir) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        logger.info("开始解压");
        Info.type=1;
        Info msg=new Info("文件下载完成，开始解压，请耐心等待提示解压完成");
        new Thread(msg).start();
        File zipFile = new File(zipPath);
        boolean flag = false;
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = null;
        try {
            zip = new ZipFile(zipFile, Charset.forName("gbk"));//防止中文目录，乱码
            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                //指定解压后的文件夹+当前zip文件的名称
                String outPath = (descDir + zipEntryName).replace("/", File.separator);
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
                if (!file.exists()) {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                //保存文件路径信息（可利用md5.zip名称的唯一性，来判断是否已经解压）
                OutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[2048];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }
            flag = true;
            zip.close();
            zipFile.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (flag){
            Info msg1=new Info("解压完成");

            new Thread(msg1).start();
            logger.info("解压成功");
            Info.setVisible(false);
        }else {
            JOptionPane.showMessageDialog(INIT.alwaysOnTop, "解压失败", "错误", JOptionPane.ERROR_MESSAGE);
            logger.error("解压失败");
            System.exit(1);
        }

        return flag;
    }
}