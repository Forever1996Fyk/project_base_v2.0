package com.javaweb.MichaelKai.service;

import com.javaweb.MichaelKai.pojo.Attachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

 /**
  * @program: project_base
  * @description: 
  * @author: YuKai Fan
  * @create: 2019-05-20 16:53:28
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
    Map<String, Object> getAttachmentById(String id);

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
    List<Map<String, Object>> getAttachments(int start, int pageSize, Map<String, Object> map);

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getAttachments(Map<String, Object> map);
}
