package com.javaweb.MichaelKai.service;

import com.javaweb.MichaelKai.pojo.Notice;
import com.javaweb.MichaelKai.pojo.NoticeUser;
import com.javaweb.MichaelKai.vo.NoticeVo;
import org.apache.ibatis.annotations.Param;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

 /**
  * @program: project_base
  * @description: 通知通告表
  * @author: YuKai Fan
  * @create: 2019-06-10 09:56:24
  **/
public interface NoticeService {
	

   /**
     * 新增
     * @param notice 实体
     * @return
     */
    Notice addNotice(Notice notice);

    /**
     * 根据id查询
     * @param id  主键
     * @return
     */
    Map<String, Object> getNoticeById(String id);

    /**
     * 根据id修改
     * @param notice 实体
     * @return
     */
    void editNoticeById(Notice notice);

    /**
     * 批量修改
     *
     * @param notice 实体
     * @param ids 主键集合
     */
    void editNoticeByIds(@Param("map") Notice notice, @Param("list") List<String> ids);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    void delNoticeById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    void delNoticeByIds(List<String> ids);

    /**
     * 获取所有(分页)
     * @param start 开始记录
     * @param pageSize 分页大小
     * @param map 参数
     * @return
     */
    PageInfo<Map<String, Object>> getNotices(int start, int pageSize, Map<String, Object> map);

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getNotices(Map<String, Object> map);

    /**
     * 获取我的通告(分页).
     * @param map 页面表单
     * @return 结果集合
     */
    PageInfo<Map<String, Object>> getMyNotices(int start, int pageSize, Map<String, Object> map);

     /**
      * 获取所有数据.
      * @param map 页面表单
      * @return 结果集合
      */
     List<Map<String, Object>> getMyNotices(Map<String, Object> map);

  /**
   * 撤销
   * @param ids
   */
  void cancelNoticesByIds(List<String> ids);

  /**
   * 发送通知通告
   * @param noticeVo
   * @return
   */
  NoticeVo saveNoticeUser(NoticeVo noticeVo);

 /**
  * 更新通告用户
  * @param noticeUser
  */
  void editNoticeUserById(NoticeUser noticeUser);
 }
