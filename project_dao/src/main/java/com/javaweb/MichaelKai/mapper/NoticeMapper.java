package com.javaweb.MichaelKai.mapper;

import java.util.List;
import java.util.Map;
import com.javaweb.MichaelKai.pojo.Notice;
import com.javaweb.MichaelKai.pojo.NoticeUser;
import org.apache.ibatis.annotations.Param;


/**
 * 通知通告表
 * 
 * @author YuKai Fan
 * @date 2019-06-10 09:56:24
 */
public interface NoticeMapper {



    /**
     * 新增
     * @param notice 实体
     * @return
     */
    int addNotice(Notice notice);

    /**
     * 批量新增
     * @param list 实体集合
     */
    void addNotices(@Param(value = "list") List<Notice> list);

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
    int editNoticeById(Notice notice);

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
    int delNoticeById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delNoticeByIds(List<String> ids);

    /**
     * 真删除
     *
     * @param id 主键
     * @return dao成功失败标志
     */
    int delNoticeRealById(String id);

    /**
     * 真批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delNoticeRealByIds(List<String> ids);

    /**
     * 全部真删除
     * @return
     */
    int delNoticeReals();

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getNotices(Map<String, Object> map);

    /**
     * 撤销
     * @param ids
     */
    void cancelNoticesByIds(List<String> ids);

    /**
     * 删除已发送的通知通告
     * @param ids
     */
    void delNoticeUserByNoticeIds(List<String> ids);

    /**
     * 发送保存通知通告
     * @param list
     */
    void saveNoticeUser(@Param(value = "list")List<Map<String,Object>> list);

    /**
     * 获取我的通告
     * @param map
     * @return
     */
    List<Map<String,Object>> getMyNotices(Map<String, Object> map);

    /**
     * 更新通告用户
     * @param noticeUser
     */
    void editNoticeUserById(NoticeUser noticeUser);
}
