package com.javaweb.MichaelKai.fileUpload;

import com.javaweb.MichaelKai.common.constants.Constant;
import com.javaweb.MichaelKai.common.enums.FileTypeEnum;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.enums.StatusEnum;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.common.properties.FileUploadProperties;
import com.javaweb.MichaelKai.common.utils.DateUtil;
import com.javaweb.MichaelKai.common.utils.SpringContextUtil;
import com.javaweb.MichaelKai.common.utils.ToolUtil;
import com.javaweb.MichaelKai.pojo.Attachment;
import com.javaweb.MichaelKai.service.AttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @program: project_base
 * @description: 文件上传处理工具
 * @author: YuKai Fan
 * @create: 2019-05-30 10:28
 **/
@Slf4j
public class FileUpload {
    /**
     * 创建Attachment
     * @param file 文件对象
     * @param modulePath 文件路径
     * @return
     */
    public static Attachment getAttachment(MultipartFile file, String modulePath) {
        if (file.getSize() == 0) {
            throw new ResultException(ResultEnum.NO_FILE_NULL.getValue(), ResultEnum.NO_FILE_NULL.getMessage());
        }
        Attachment attachment = new Attachment();
        attachment.setAttachSize(file.getSize());
        attachment.setStatus(StatusEnum.Normal.getValue());
        attachment.setAttachOriginTitle(file.getOriginalFilename());
        attachment.setAttachName(FileUpload.genFileName(file.getOriginalFilename()));
        attachment.setAttachPostfix(ToolUtil.getFileSuffix(file.getOriginalFilename()));
        attachment.setAttachPath(getPathPattren() + modulePath + FileUpload.genDateMkdir() + attachment.getAttachName());

        return attachment;
    }

    /**
     * 生成随机且唯一的文件名
     * @param originalFilename
     * @return
     */
    private static String genFileName(String originalFilename) {
        String fileSuffix = ToolUtil.getFileSuffix(originalFilename);
        return  UUID.randomUUID().toString().replace("-", "") + fileSuffix;
    }

    /**
     * 生成指定格式的目录名称(日期格式)
     */
    private static String genDateMkdir() {
        return "/" + DateUtil.dateToString(new Date(), Constant.DATE_FORMAT_FILE_INDEX) + "/";
    }

    /**
     * 获取文件上传目录的资源路径
     * @return
     */
    private static String getPathPattren() {
        FileUploadProperties properties = SpringContextUtil.getBean(FileUploadProperties.class);
        return properties.getFILE_PATH();
    }

    /**
     * 获取文件MD5值和SHA1值
     * @param multipartFile MultipartFile对象
     * @param attachment Attachment
     */
    public static void transferTo(MultipartFile multipartFile, Attachment attachment) throws IOException, NoSuchAlgorithmException {

        byte[] buffer = new byte[4096];
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        try (OutputStream fos = Files.newOutputStream(getDestFile(attachment).toPath()); InputStream fis = multipartFile.getInputStream()) {
            int len = 0;
            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                md5.update(buffer, 0, len);
                sha1.update(buffer, 0, len);
            }
            fos.flush();
        }
        BigInteger MD5Bi = new BigInteger(1, md5.digest());
        BigInteger SHA1Bi = new BigInteger(1, sha1.digest());

        attachment.setAttachMd5(MD5Bi.toString(16));
        attachment.setAttachSha1(SHA1Bi.toString(16));
    }

    /**
     * 获取目标文件对象
     * @param attachment
     * @return
     * @throws IOException
     */
    private static File getDestFile(Attachment attachment) throws IOException {
        File file = new File(attachment.getAttachPath());
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return file;
    }


    /**
     * 判断文件格式是否支持
     * @param multipartFile
     * @param attachType
     * @return
     */
    public static boolean isContentType(MultipartFile multipartFile, Integer attachType) {
        //如果是图片类型，判断格式
        if (attachType == FileTypeEnum.PIC.getAttachType()) {
            String[] types = {
                    "image/gif",
                    "image/jpg",
                    "image/jpeg",
                    "image/png"
            };

            List<String> typeList = Arrays.asList(types);
            return typeList.contains(multipartFile.getContentType());
        }

        return true;
    }

    /**
     * 获取文件的SHA1值
     * @param multipartFile
     * @return
     */
    public static String getFileSHA1(MultipartFile multipartFile) {
        if (multipartFile.getSize() == 0) {
            throw new ResultException(ResultEnum.NO_FILE_NULL.getValue(), ResultEnum.NO_FILE_NULL.getMessage());
        }
        byte[] buffer = new byte[4096];
        try (InputStream fis = multipartFile.getInputStream()) {
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            int len = 0;
            while ((len = fis.read(buffer)) != -1) {
                sha1.update(buffer, 0, len);
            }
            BigInteger SHA1Bi = new BigInteger(1, sha1.digest());
            return SHA1Bi.toString(16);
        } catch (IOException | NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 显示图片
     * @param response
     * @param attId
     * @param defaultImg
     */
    public static void showPic(HttpServletResponse response, String attId, String defaultImg) {
        AttachmentService attachmentService = SpringContextUtil.getBean(AttachmentService.class);
        byte[] image = null;
        if ("null".equals(attId) || "undefined".equals(attId)) {
            image = getDefaultImg(defaultImg);
        } else {
            Attachment attachment = attachmentService.getAttachmentById(attId);
            if (attachment == null || attachment.getAttachment() == null) {
                image = getDefaultImg(defaultImg);
            } else {
                image = attachment.getAttachment();
            }
        }

        outputStream(response, image);
    }

    /**
     * 获取默认图片
     * @param type
     * @return
     */
    private static byte[] getDefaultImg(String type) {
        String projectPath = ToolUtil.getProjectPath();
        String defaultImg = projectPath;
        if (!StringUtils.isBlank(type)) {
            switch (type) {
                case "userIcon":
                    defaultImg += "/static/images/user-picture.jpg";
                    break;
            }
        }
        return getContentByte(defaultImg);
    }

    /**
     * 读出默认图片
     * @param fileName
     * @return
     */
    private static byte[] getContentByte(String fileName) {
        try {
            return FileUtils.readFileToByteArray(new File(fileName));
        } catch (IOException e) {
            log.error("文件下载失败", e);
        }
        return null;
    }

    /**
     * 输出图片流
     * @param response
     * @param image
     */
    private static void outputStream(HttpServletResponse response, byte[] image) {
        //在response的hearder中添加附件大小
        response.setContentType("image/jpeg;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            out.write(image);//直接显示到网页上
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("获取图片数据失败", e);
        }
    }

    /**
     * 读取文件
     * @param path
     * @return
     */
    public static byte[] readAttachment(String path) {
        try {
            return FileUtils.readFileToByteArray(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件读取失败", e);
        }
        return null;
    }
}