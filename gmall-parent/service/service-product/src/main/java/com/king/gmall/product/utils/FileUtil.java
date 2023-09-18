package com.king.gmall.product.utils;

import com.king.gmall.common.execption.GmallException;
import com.king.gmall.common.result.Result;
import com.king.gmall.common.result.ResultCodeEnum;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/***
 * ClassName: FileUtil
 * Package: com.king.gmall.product.utils
 * @author GK
 * @date 2023/9/16 16:38
 * @description 文件管理工具类
 * @version 1.0
 */
public class FileUtil {
    private static StorageClient storageClient;

    static {
        try {
            //加载配置文件
            ClassPathResource conf = new ClassPathResource("FastDFS.conf");
            //初始化fastDFS对象
            ClientGlobal.init(conf.getPath());
            //初始化trackerClient
            TrackerClient trackerClient = new TrackerClient();
            //获取连接初始化信息
            TrackerServer connection = trackerClient.getConnection();
            //初始化storageClient
            storageClient = new StorageClient(connection, null);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 文件上传,返回图片路径
     * @param file
     * @return
     * @throws IOException
     * @throws MyException
     */
    public static String upload(MultipartFile file) throws IOException, MyException {
        //获取文件拓展名
        String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        //上传文件,返回组名,路径
        String[] strings = storageClient.upload_file(
                file.getBytes(),
                filenameExtension,
                null);
        //返回图片的路径
        return strings[0] + "/" + strings[1];
    }

    /**
     * 删除文件
     * @param groupName
     * @param remoteFilename
     * @throws MyException
     * @throws IOException
     */
    public static boolean delete(String groupName, String remoteFilename) throws MyException, IOException {
        int deleteFile = storageClient.delete_file(groupName, remoteFilename);
        return deleteFile == 0 ? true : false;

    }

    /**
     * 下载文件
     * @param groupName
     * @param remoteFilename
     * @return
     * @throws MyException
     * @throws IOException
     */
    public static byte[] download(String groupName, String remoteFilename) throws MyException, IOException {
        byte[] bytes = storageClient.download_file(groupName, remoteFilename);
        return bytes;
    }

    public static void main(String[] args) throws MyException, IOException {
        //System.out.println(delete("group1", "M00/00/04/wKjIgGUFgmuAJAbzAAgNZFhfC28811.gif"));
        //下载文件到d盘
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("d:/" + "wKjIgGRjag6ARNLqAAAVNu_LJ4k315.png");
            byte[] file = download("group1", "M00/00/04/wKjIgGRjag6ARNLqAAAVNu_LJ4k315.png");
            fileOutputStream.write(file);
            fileOutputStream.flush();
        } finally {
            fileOutputStream.close();
        }


    }

}
