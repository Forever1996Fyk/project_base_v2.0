package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.javaweb.MichaelKai.common.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.javaweb.MichaelKai.service.AttachmentService;
import com.javaweb.MichaelKai.mapper.AttachmentMapper;
import com.javaweb.MichaelKai.pojo.Attachment;

import java.util.List;
import java.util.Map;


 /**
  * @program: project_base
  * @description: 
  * @author: YuKai Fan
  * @create: 2019-05-20 16:53:28
  **/
@Service
@Transactional
@SuppressWarnings("SpringJavaAutowiringInspection")
public class AttachmentServiceImpl implements  AttachmentService {

	@Autowired
	private AttachmentMapper attachmentMapper;
	@Autowired
    private IdWorker idWorker;

    @Override
    public Attachment addAttachment(Attachment attachment) {
        attachment.setId(String.valueOf(idWorker.nextId()));
        attachmentMapper.addAttachment(attachment);
        return attachment;
    }

    @Override
    public Map<String, Object> getAttachmentById(String id) {
        return attachmentMapper.getAttachmentById(id);
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
    public List<Map<String, Object>> getAttachments(int start, int pageSize, Map<String, Object> map) {
        PageHelper.offsetPage(start, pageSize);
        return this.getAttachments(map);
    }

    @Override
    public List<Map<String, Object>> getAttachments(Map<String, Object> map) {
        return attachmentMapper.getAttachments(map);
    }
}
