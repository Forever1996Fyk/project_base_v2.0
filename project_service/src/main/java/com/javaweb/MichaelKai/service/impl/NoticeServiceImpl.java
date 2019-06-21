package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.javaweb.MichaelKai.common.constants.Constant;
import com.javaweb.MichaelKai.common.utils.AppUtil;
import com.javaweb.MichaelKai.common.utils.DateUtil;
import com.javaweb.MichaelKai.pojo.NoticeUser;
import com.javaweb.MichaelKai.vo.NoticeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.javaweb.MichaelKai.service.NoticeService;
import com.javaweb.MichaelKai.mapper.NoticeMapper;
import com.javaweb.MichaelKai.pojo.Notice;
import com.javaweb.MichaelKai.common.enums.StatusEnum;
import com.github.pagehelper.PageInfo;
import org.springframework.util.CollectionUtils;

import java.util.*;


/**
  * @program: project_base
  * @description: 通知通告表
  * @author: YuKai Fan
  * @create: 2019-06-10 09:56:24
  **/
@Service
@Transactional
@SuppressWarnings("SpringJavaAutowiringInspection")
public class NoticeServiceImpl implements  NoticeService {

	@Autowired
	private NoticeMapper noticeMapper;

    @Override
    public Notice addNotice(Notice notice) {
        notice.setId(AppUtil.randomId());
                                                                                                                                                                                                    notice.setStatus(StatusEnum.Normal.getValue());
                                                                                                            noticeMapper.addNotice(notice);
        return notice;
    }

    @Override
    public Map<String, Object> getNoticeById(String id) {
        return noticeMapper.getNoticeById(id);
    }

    @Override
    public void editNoticeById(Notice notice) {
        noticeMapper.editNoticeById(notice);
    }

    @Override
    public void editNoticeByIds(Notice notice, List<String> ids) {

    }

    @Override
    public void delNoticeById(String id) {
        noticeMapper.delNoticeById(id);
    }

    @Override
    public void delNoticeByIds(List<String> ids) {
        noticeMapper.delNoticeByIds(ids);
    }

    @Override
    public PageInfo<Map<String, Object>> getNotices(int pageNum, int pageSize, Map<String, Object> map) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = this.getNotices(map);
        PageInfo<Map<String, Object>> page = new PageInfo<>(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> getMyNotices(Map<String, Object> map) {
        return noticeMapper.getMyNotices(map);
    }

    @Override
    public List<Map<String, Object>> getNotices(Map<String, Object> map) {
        return noticeMapper.getNotices(map);
    }

    @Override
    public PageInfo<Map<String, Object>> getMyNotices(int pageNum, int pageSize, Map<String, Object> map) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = this.getMyNotices(map);
        PageInfo<Map<String, Object>> page = new PageInfo<>(list);
        return page;
    }

    @Override
     public void cancelNoticesByIds(List<String> ids) {

         //撤销通知通告 只能撤销五分钟前
         noticeMapper.cancelNoticesByIds(ids);

         //删除已经发送给用户的通知 只能删除五分钟前
         noticeMapper.delNoticeUserByNoticeIds(ids);
     }

     @Override
     public NoticeVo saveNoticeUser(NoticeVo noticeVo) {
         Set<String> userIds = noticeVo.getUserIds();

         //发送保存通知
         if (!CollectionUtils.isEmpty(userIds)) {
             List<Map<String, Object>> list = new ArrayList<>();
             for (String userId : userIds) {
                 Map<String, Object> map = new HashMap<>();
                 map.put("noticeId", noticeVo.getNoticeId());
                 map.put("userId", userId);
                 map.put("readed", 0);

                 list.add(map);
             }
             noticeMapper.saveNoticeUser(list);
         }

         //更新通告信息
         Notice notice = new Notice();
         notice.setId(noticeVo.getNoticeId());
         notice.setPublishTime(DateUtil.dateToString(new Date(), Constant.DATE_FORMAT_NOTICE_PUBLIC));
         noticeMapper.editNoticeById(notice);
         return noticeVo;
     }

    @Override
    public void editNoticeUserById(NoticeUser noticeUser) {
        noticeMapper.editNoticeUserById(noticeUser);
    }
}
