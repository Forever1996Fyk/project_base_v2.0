/**
 * Created by YuKai Fan on 2019-06-27 11:00:32.
 */
layui.use(['table', 'layer', 'form', 'formSelects'], function() {
    var layer = layui.layer;
    var table = layui.table;

    var tableObject = table.render({
        id:"id"
        ,elem: '#scheduleJobListTable'
        , height: 500
        , url: ctxPath + '/api/quartz/getAllJobs'//数据接口
        , page: false
        // , limits: [10,20,30,40,50]
        // , limit: 10
        // , parseData: function (res) {
        //     return {
        //         "code": res.code,
        //         "msg": res.message,
        //         "count": res.data.total,
        //         "data": res.data.rows
        //     }
        // }
        , cols: [[ //表头,field要与实体类字段相同
            {title: '序号', type: 'numbers', align: 'center'}
            //{type: 'checkbox'}
            //, {field: 'jobId', title: '任务Id', align: 'center'}
            , {field: 'jobName', title: '任务名称', align: 'center'}
            , {field: 'jobGroup', title: '任务组名', align: 'center'}
            , {field: 'jobStatus', title: '任务状态', align: 'center'}
            , {field: 'cronExpression', title: 'cron表达式', align: 'center'}
            , {field: 'description', title: '描述', align: 'center'}
            , {field: 'beanName', title: '执行bean', align: 'center'}
            , {field: 'methodName', title: '执行方法', align: 'center'}
            , {field: 'lastTriggerTime', title: '上次执行时间', align: 'center'}
            , {field: 'nextTriggerTime', title: '下次执行时间', align: 'center'}
            , {title: '操作', toolbar: '#btn', align: 'center', width:'15%'}
        ]]
    });
    /*
     表格点击事件写法,其中tool('这是表格的lay-filter'),监听点击事件         */
    table.on('tool(tblist)', function (obj) {
        switch (obj.event) {
            case 'edit':
                active.edit(obj.data);
                break;
            case 'del':
                active.delete(obj.data);
                break;
            case 'pause':
                active.pause(obj.data);
                break;
            case 'resume':
                active.resume(obj.data);
                break;
        }
    });

    var active = {
        //编辑
        edit:function (data) {
            layer.open({
                type: 2
                ,title: '编辑'
                ,content: ctxPath + '/system/quartz/scheduleJob/edit?id=' + data.id
                ,maxmin: true
                ,area: ['500px', '450px']
            })
        },

        //删除
        delete:function (data) {
            layer.confirm ('确定删除吗?', function (index) {
                $.ajax({
                    url: ctxPath + '/api/quartz/deleteJob?jobName=' + data.jobName + "&jobGroup=" + data.jobGroup,
                    type: 'delete',
                    success:function(res){
                        if (res.code === 200) {
                            layer.msg(res.message);
                            //刷新表格
                            tableObject.reload({
                                page: '{curr:1}'
                            });
                        } else {
                            layer.msg(res.message);
                        }
                    }
                })
            })
        },

        //暂停任务
        pause: function (data) {
            layer.confirm ('确定暂停吗?', function (index) {
                $.ajax({
                    url: ctxPath + '/api/quartz/pauseJob',
                    type: 'post',
                    data: {jobName: data.jobName, jobGroup: data.jobGroup},
                    success:function(res){
                        if (res.code === 200) {
                            layer.msg(res.message);
                            //刷新表格
                            tableObject.reload({
                                page: '{curr:1}'
                            });
                        } else {
                            layer.msg(res.message);
                        }
                    }
                })
            })
        },

        //重启
        resume: function (data) {
            layer.confirm ('确定重启吗?', function (index) {
                $.ajax({
                    url: ctxPath + '/api/quartz/resumeJob',
                    type: 'post',
                    data: {jobName: data.jobName, jobGroup: data.jobGroup},
                    success:function(res){
                        if (res.code === 200) {
                            layer.msg(res.message);
                            //刷新表格
                            tableObject.reload({
                                page: '{curr:1}'
                            });
                        } else {
                            layer.msg(res.message);
                        }
                    }
                })
            })
        },
        //搜索
        search:function () {
            tableObject.reload({
                where:{
                    
                }
            })
        },

    };
    $('.layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});


