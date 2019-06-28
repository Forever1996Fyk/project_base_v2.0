package com.javaweb.MichaelKai.mapper;

import java.util.List;
import java.util.Map;
import com.javaweb.MichaelKai.pojo.ScheduleJob;
import org.apache.ibatis.annotations.Param;


/**
 * 
 * 
 * @author YuKai Fan
 * @date 2019-06-27 11:00:32
 */
public interface ScheduleJobMapper {



    /**
     * 新增
     * @param scheduleJob 实体
     * @return
     */
    int addScheduleJob(ScheduleJob scheduleJob);

    /**
     * 批量新增
     * @param list 实体集合
     */
    void addScheduleJobs(@Param(value = "list") List<ScheduleJob> list);

    /**
     * 根据id查询
     * @param id  主键
     * @return
     */
    Map<String, Object> getScheduleJobById(String id);

    /**
     * 根据id修改
     * @param scheduleJob 实体
     * @return
     */
    int editScheduleJobById(ScheduleJob scheduleJob);

    /**
     * 批量修改
     *
     * @param scheduleJob 实体
     * @param ids 主键集合
     */
    void editScheduleJobByIds(@Param("map") ScheduleJob scheduleJob, @Param("list") List<String> ids);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int delScheduleJobById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delScheduleJobByIds(List<String> ids);

    /**
     * 真删除
     *
     * @param id 主键
     * @return dao成功失败标志
     */
    int delScheduleJobRealById(String id);

    /**
     * 真批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delScheduleJobRealByIds(List<String> ids);

    /**
     * 全部真删除
     * @return
     */
    int delScheduleJobReals();

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getScheduleJobs(Map<String, Object> map);

}
