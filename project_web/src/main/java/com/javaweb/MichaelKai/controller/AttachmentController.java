package com.javaweb.MichaelKai.controller;

import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.Attachment;
import com.javaweb.MichaelKai.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @program: project_base
 * @description: 
 * @author: YuKai Fan
 * @create: 2019-05-20 16:53:28
 *
 */
@RestController
@RequestMapping("/api")
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;

    /**
     * 添加
     * @param attachment
     * @return
     */
    @PostMapping("/attachment")
    public Result addAttachment(@RequestBody Attachment attachment) {
        return new Result(true, ResultEnum.SUCCESS.getValue(), "新增" + ResultEnum.SUCCESS.getMessage(), attachmentService.addAttachment(attachment));
    }

    /**
     * 编辑修改
     * @param attachment
     * @return
     */
    @PutMapping("/attachment")
    public Result editAttachmentById(@RequestBody Attachment attachment) {
        attachmentService.editAttachmentById(attachment);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "修改" + ResultEnum.SUCCESS.getMessage(), attachment);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/attachment")
    public Result editAttachmentById(@RequestParam String id) {
        attachmentService.delAttachmentById(id);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/attachments/{ids}")
    public Result editAttachmentByIds(@PathVariable("ids") String[] ids) {
        attachmentService.delAttachmentByIds(Arrays.asList(ids));
        return new Result(true, ResultEnum.SUCCESS.getValue(), "批量删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 获取所有(不分页)
     * @param map 参数
     * @return
     */
    @GetMapping("/getAttachments/noPage")
    public Result getAttachments(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> list = attachmentService.getAttachments(map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

    /**
     * 获取所有
     * @param start 开始记录
     * @param pageSize 分页大小
     * @param map 参数
     * @return
     */
    @GetMapping("/getAttachments")
    public Result getUsers(@RequestParam(value = "start", required = false, defaultValue = "0") int start,
                           @RequestParam(value = "length", required = false, defaultValue = "0") int pageSize,
                           @RequestParam Map<String, Object> map) {
        List<Map<String, Object>> list = attachmentService.getAttachments(start, pageSize, map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }


}
