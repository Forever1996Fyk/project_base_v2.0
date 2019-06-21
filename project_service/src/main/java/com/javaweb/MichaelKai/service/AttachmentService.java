package com.javaweb.MichaelKai.service;

import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.pojo.Attachment;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-05-24 13:51:55
 **/
public interface AttachmentService {


  /**
    * 新增
    * @param attachment 实体
    * @return
    */
   Attachment addAttachment(Attachment attachment);

   /**
    * 根据id查询
    * @param id  主键
    * @return
    */
   Attachment getAttachmentById(String id);

   /**
    * 根据id修改
    * @param attachment 实体
    * @return
    */
   void editAttachmentById(Attachment attachment);

   /**
    * 批量修改
    *
    * @param attachment 实体
    * @param ids 主键集合
    */
   void editAttachmentByIds(@Param("map") Attachment attachment, @Param("list") List<String> ids);

   /**
    * 根据id删除
    * @param id
    * @return
    */
   void delAttachmentById(String id);

   /**
    * 批量删除
    *
    * @param ids 主键集合
    * @return dao成功失败标志
    */
   void delAttachmentByIds(List<String> ids);

   /**
    * 获取所有(分页)
    * @param start 开始记录
    * @param pageSize 分页大小
    * @param map 参数
    * @return
    */
   PageInfo<Map<String, Object>> getAttachments(int start, int pageSize, Map<String, Object> map);

   /**
    * 获取所有数据.
    * @param map 页面表单
    * @return 结果集合
    */
   List<Map<String, Object>> getAttachments(Map<String, Object> map);

   /**
    * 添加附件
    * @param request
    * @return
    */
   Attachment addAttachment(HttpServletRequest request);

 /**
  * 添加附件
  * @param request
  * @param userId
  * @return
  */
   Attachment addAttachment(HttpServletRequest request, String userId, Integer attachType) throws Exception;

 /**
  * 显示图片
  * @param response
  * @param attId
  * @param type
  */
  void showPic(HttpServletResponse response, String attId, String type);
}
