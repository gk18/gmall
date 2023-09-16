package com.king.gmall.product.controller;

import com.king.gmall.common.result.Result;
import com.king.gmall.product.utils.FileUtil;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/***
 * ClassName: FileController
 * Package: com.king.gmall.product.controller
 * @author GK
 * @date 2023/9/16 15:57
 * @description 文件管理控制层
 * @version 1.0
 */
@RestController
@RequestMapping("/admin/product")
public class FileController {
    @Value("${fileServer.url}")
    private String url;

    /**
     * 上传文件到fastDFS
     *
     * @param file
     * @return
     */
    @PostMapping("/fileUpload")
    public Result upload(@RequestParam("file") MultipartFile file) throws MyException, IOException {
        String path = FileUtil.upload(file);
        return Result.ok(url + "/" + path);

    }
}
