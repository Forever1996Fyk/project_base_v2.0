package com.javaweb.MichaelKai.activiti.service.impl;

import com.github.pagehelper.PageHelper;
import com.javaweb.MichaelKai.activiti.service.ProcessClassService;
import com.javaweb.MichaelKai.common.constants.Constant;
import com.javaweb.MichaelKai.common.enums.FileTypeEnum;
import com.javaweb.MichaelKai.common.utils.AppUtil;
import com.javaweb.MichaelKai.common.utils.MapUtil;
import com.javaweb.MichaelKai.pojo.Attachment;
import com.javaweb.MichaelKai.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.javaweb.MichaelKai.mapper.ProcessClassMapper;
import com.javaweb.MichaelKai.pojo.ProcessClass;
import com.javaweb.MichaelKai.common.enums.StatusEnum;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * @program: project_base
 * @description: 流程分类表
 * @author: YuKai Fan
 * @create: 2019-08-12 10:14:44
 **/
@Service
@Transactional
@SuppressWarnings("SpringJavaAutowiringInspection")
public class ProcessClassServiceImpl implements ProcessClassService {

    @Autowired
    private ProcessClassMapper processClassMapper;
    @Autowired
    private AttachmentService attachmentService;

    @Override
    public ProcessClass addProcessClass(HttpServletRequest request, String userId) throws Exception {

        Attachment attachment = attachmentService.addAttachment(request, userId, FileTypeEnum.PIC.getAttachType());

        Map<String, Object> parameterMap = MapUtil.getParameterMap(request);
        ProcessClass processClass = MapUtil.mapToObject(ProcessClass.class, parameterMap, false);
        processClass.setId(AppUtil.randomId());
        processClass.setStatus(StatusEnum.Normal.getValue());

        if (attachment != null) {
            processClass.setClassIcon(attachment.getId());
        }
        processClassMapper.addProcessClass(processClass);
        return processClass;
    }

    @Override
    public Map<String, Object> getProcessClassById(String id) {
        return processClassMapper.getProcessClassById(id);
    }

    @Override
    public void editProcessClassById(HttpServletRequest request, String userId) throws Exception {
        Attachment attachment = attachmentService.addAttachment(request, userId, FileTypeEnum.PIC.getAttachType());

        Map<String, Object> parameterMap = MapUtil.getParameterMap(request);
        ProcessClass processClass = MapUtil.mapToObject(ProcessClass.class, parameterMap, false);

        if (attachment != null) {
            processClass.setClassIcon(attachment.getId());
        }
        processClassMapper.editProcessClassById(processClass);
    }

    @Override
    public void editProcessClassByIds(ProcessClass processClass, List<String> ids) {

    }

    @Override
    public void delProcessClassById(String id) {
        processClassMapper.delProcessClassById(id);
    }

    @Override
    public void delProcessClassByIds(List<String> ids) {
        processClassMapper.delProcessClassByIds(ids);
    }

    @Override
    public PageInfo<Map<String, Object>> getProcessClasss(int pageNum, int pageSize, Map<String, Object> map) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = this.getProcessClasss(map);
        PageInfo<Map<String, Object>> page = new PageInfo<>(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> getProcessClasss(Map<String, Object> map) {
        return processClassMapper.getProcessClasss(map);
    }
}
