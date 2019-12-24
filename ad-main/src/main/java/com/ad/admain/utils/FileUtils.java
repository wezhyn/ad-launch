package com.ad.admain.utils;

import com.ad.admain.config.web.ResourceConstant;
import com.wezhyn.project.StringEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 默认保存地址 {@link ResourceConstant#RESOURCE_SAVE_LOCATION}
 *
 * @author : wezhyn
 * @date : 2019/09/20
 */
public class FileUtils {
    private static final Random RANDOM_SEED=new Random();


    public static List<String> readFromFile(Path path) {
        try {
            FileInputStream fileInputStream=new FileInputStream(path.toFile());
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException var3) {
            var3.printStackTrace();
            return Collections.emptyList();
        }
    }


    /*public static String saveImage(MultipartFile file,  boolean isCompress) throws Exception {
        FileWrap fileWrap=getBaseInfo(file, FileType.IMAGE, "");
        Thumbnails
                .of(new InputStream[]{file.getInputStream()}).scale(1.0D).toFile(fileWrap.getDest());
        String resultPath=fileWrap.getRealPath().toString();
        if (isCompress) {
            FileWrap copyWrap=getBaseInfo(file, FileType.IMAGE, ResourceConstant.RESOURCE_SAVE_COMPRESS);
            long size=file.getSize();
            double scale;
            if (size < 1048576L) {
                scale=0.75D;
            } else if (size < 3145728L) {
                scale=0.4D;
            } else {
                scale=0.2D;
            }
            Thumbnails
                    .of(new InputStream[]{file.getInputStream()})
                    .scale(scale)
                    .toFile(copyWrap.getDest());
            resultPath=copyWrap.getRealPath().toString();
        }

        return resultPath;
    }*/

    /**
     * 包装文件基本信息
     *
     * @param file              上传文件流
     * @param fileType          文件类型 ：图片，其他
     * @param relativeDirectory 相对于{@link ResourceConstant#RESOURCE_SAVE_LOCATION}
     *                          ->    E:/res/{type}/relativeDirectory/fileName,若无,则 /res/{type}
     * @return FileWrap
     */
    private static FileWrap getBaseInfo(MultipartFile file, StringEnum fileType, String relativeDirectory) {
        String fileName=getFileName(file.getOriginalFilename());
        Path realPath=Paths.get(ResourceConstant.RESOURCE_SAVE_LOCATION, fileType.getValue(), relativeDirectory, fileName);
        File dest=realPath.toFile();
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        return new FileWrap(fileName, realPath, dest);
    }


    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return .jpg .png
     */
    private static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 删除文件
     *
     * @param path 文件绝对路径
     * @throws IOException 删除异常
     */
    public static void deleteFile(Path path) throws IOException {
        Files.delete(path);
    }

    /**
     * 为原始文件添加 随机名称,并保留原始后缀
     *
     * @param fileOriginName 文件原始名称
     * @return RandomString
     */
    private static String getFileName(String fileOriginName) {
        return getRandomString(RANDOM_SEED.nextInt(40) + 10) + getSuffix(fileOriginName);
    }

    /**
     * 生成随机字符串
     *
     * @param length 字符串长度
     * @return random str
     */
    private static String getRandomString(int length) {
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for (int i=0; i < length; i++) {
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    @Data
    @NoArgsConstructor
    private static class FileWrap {
        private String fileName;
        private Path realPath;
        File dest;

        public FileWrap(String fileName, Path realPath, File dest) {
            this.fileName=fileName;
            this.realPath=realPath;
            this.dest=dest;
        }
    }
}
