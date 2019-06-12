package com.javaweb.MichaelKai.controller;

import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.enums.FileTypeEnum;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.PageResult;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.fileUpload.FileUpload;
import com.javaweb.MichaelKai.pojo.Attachment;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.service.AttachmentService;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @program: project_base
 * @description: 文件图片上传
 * @author: YuKai Fan
 * @create: 2019-05-24 13:51:55
 *
 */
@Controller
@RequestMapping("/api")
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;

    /**
     * 上传附件
     * @param request
     * @return
     */
    @PostMapping("/attachment")
    @ResponseBody
    public Result addAttachment(HttpServletRequest request) throws Exception {
        User user = ShiroKit.getUser();
        Attachment attachment = attachmentService.addAttachment(request, user.getId(), FileTypeEnum.PIC.getAttachType());
        return new Result(true, ResultEnum.SUCCESS.getValue(), "新增" + ResultEnum.SUCCESS.getMessage(), attachment);
    }

    /**
     * 编辑修改
     * @param attachment
     * @return
     */
    @PutMapping("/attachment")
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
    public Result getAttachments(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> list = attachmentService.getAttachments(map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

    /**
     * 获取所有
     * @param start 开始记录
     * @param limit 分页大小
     * @param map 参数
     * @return
     */
    @GetMapping("/getAttachments")
    @ResponseBody
    public Result getAttachments(@RequestParam(value = "start", required = false, defaultValue = "0") int start,
                           @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                           @RequestParam Map<String, Object> map) {
        PageInfo<Map<String, Object>> pageList = attachmentService.getAttachments(start, limit, map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }

    /**
     * 显示图片
     * @param request
     * @param response
     * @param attId
     */
    @GetMapping("/attachments/{attId}/{type}/image")
    @ResponseBody
    public void getAttachment(HttpServletRequest request, HttpServletResponse response, @PathVariable("attId") String attId, @PathVariable("type")String type){
        FileUpload.showPic(response, attId, type);
    }


}
