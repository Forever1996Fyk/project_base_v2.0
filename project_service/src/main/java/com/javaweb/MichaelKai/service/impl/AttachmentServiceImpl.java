package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.constants.Constant;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.common.utils.AppUtil;
import com.javaweb.MichaelKai.common.utils.DateUtil;
import com.javaweb.MichaelKai.common.utils.MapUtil;
import com.javaweb.MichaelKai.fileUpload.FileUpload;
import com.javaweb.MichaelKai.mapper.AttachmentMapper;
import com.javaweb.MichaelKai.pojo.Attachment;
import com.javaweb.MichaelKai.service.AttachmentService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


 /**
  * @program: project_base
  * @description: 
  * @author: YuKai Fan
  * @create: 2019-05-24 13:51:55
  **/
@Service
@Transactional
@SuppressWarnings("SpringJavaAutowiringInspection")
public class AttachmentServiceImpl implements  AttachmentService {

	@Autowired
	private AttachmentMapper attachmentMapper;

    @Override
    public Attachment addAttachment(Attachment attachment) {
        attachment.setId(AppUtil.randomId());
        return attachment;
    }

    @Override
    public Attachment getAttachmentById(String id) {
        Map<String, Object> map = attachmentMapper.getAttachmentById(id);
        if (CollectionUtils.isEmpty(map)) {
            return null;
        }
        Attachment data = new Attachment();

        data.setAttachment(FileUpload.readAttachment(map.get("attachPath").toString()));
        return data;
    }

    @Override
    public void editAttachmentById(Attachment attachment) {
        attachmentMapper.editAttachmentById(attachment);
    }

    @Override
    public void editAttachmentByIds(Attachment attachment, List<String> ids) {

    }

    @Override
    public void delAttachmentById(String id) {
        attachmentMapper.delAttachmentById(id);
    }

    @Override
    public void delAttachmentByIds(List<String> ids) {
        attachmentMapper.delAttachmentByIds(ids);
    }

    @Override
    public PageInfo<Map<String, Object>> getAttachments(int start, int pageSize, Map<String, Object> map) {
        PageHelper.offsetPage(start, pageSize);
        List<Map<String, Object>> list = this.getAttachments(map);
        PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> getAttachments(Map<String, Object> map) {
        return attachmentMapper.getAttachments(map);
    }

     @Override
     public Attachment addAttachment(HttpServletRequest request) {
         return null;
     }

     @Override
     public Attachment addAttachment(HttpServletRequest request, String userId, Integer attachType) throws Exception {

        Attachment attachment = null;
        if (request instanceof MultipartHttpServletRequest) {
            Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
            if (CollectionUtils.isEmpty(fileMap) || CollectionUtils.isEmpty(fileMap.keySet())) {
                return null;
            }

            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                if (StringUtils.isBlank(entry.getKey())) {
                    continue;
                }
                if (!entry.getValue().isEmpty()) {
                    //判断是否支持格式
                    if (!FileUpload.isContentType(entry.getValue(), attachType)) {
                        throw new ResultException(ResultEnum.NO_FILE_TYPE.getValue(), ResultEnum.NO_FILE_TYPE.getMessage());
                    }
                    //判断文件是否已存在
                    Map<String, Object> map = new HashMap<>();
                    map.put("attachSha1", FileUpload.getFileSHA1(entry.getValue()));
                    List<Map<String, Object>> attachments = attachmentMapper.getAttachments(map);
                    if (attachments.size() > 0) {
                        attachment = (Attachment) MapUtil.mapToObject(Attachment.class, attachments.get(0));
                        return attachment;
                    }
                    attachment = FileUpload.getAttachment(entry.getValue(), attachType == 0 ? "picture" : "other");
                    FileUpload.transferTo(entry.getValue(), attachment);
                    attachment = this.addAttachment(attachment, userId, attachType);
                }
            }
        }
        return attachment;
     }

     /**
      * 上传文件到指定路径
      * @param file
      * @param attachment
      *//*
     private File uploadFile(MultipartFile file, Attachment attachment) {
         FileUploadProperties properties = SpringContextUtil.getBean(FileUploadProperties.class);
         File filePath = new File(attachment.getAttachPath(), attachment.getAttachName() + "." + attachment.getAttachPostfix());
         //如果父文件夹不存在，则创建
         if (!filePath.getParentFile().exists()) {
             filePath.getParentFile().mkdirs();
         }
         try {
             file.transferTo(filePath);

             return filePath;
         } catch (IOException e) {
             e.printStackTrace();
             throw new ResultException(ResultEnum.UPLOAD_FAIL.getValue(), ResultEnum.UPLOAD_FAIL.getMessage());
         } catch (IllegalStateException e) {
             e.printStackTrace();
             throw new ResultException(ResultEnum.UPLOAD_FAIL.getValue(), ResultEnum.UPLOAD_FAIL.getMessage());
         }
     }*/

     /**
      * 文件上传保存附件信息到数据库
      * @param attachment
      * @param userId
      * @param attachType
      * @return
      */
     private Attachment addAttachment(Attachment attachment, String userId, Integer attachType) {

         attachment.setId(AppUtil.randomId());
         attachment.setAttachType(attachType);
         attachment.setCreateUserId(userId);
         attachment.setUpdateUserId(userId);
         attachment.setCreateTime(DateUtil.dateToString(new Date(), Constant.DATE_FORMAT_CREATE_UPDATE));
         attachment.setUpdateTime(DateUtil.dateToString(new Date(), Constant.DATE_FORMAT_CREATE_UPDATE));

         attachmentMapper.addAttachment(attachment);
         return attachment;
     }
 }
