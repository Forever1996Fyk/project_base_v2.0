/**
 * Created by YuKai Fan on 2019-08-08 10:30:23.
 */
layui.use(['form', 'layer', 'table'], function(){
    var layer = layui.layer
        ,form = layui.form
        ,table = layui.table
        ,countNum = 0;

    var processInstanceId = $("#processInstanceId").val();
    if (processInstanceId === undefined) {
        layer.msg("页面错误!!!");
        return;
    }

    var tableObject = table.render({
        id: "id"
        , elem: '#viewProcDetailListTable'
        , height: 'full-160'
        , url: ctxPath + '/api/activiti/getProcessDetail?processInstanceId=' + processInstanceId//数据接口
        , page: false
        , cols: [[ //表头,field要与实体类字段相同
            {type: 'checkbox'}
            , {field: 'taskId', title: '任务编码', align: 'center', width: '20%'}
            , {field: 'userName', title: '审批人', align: 'center', width: '20%'}
            , {field: 'opinion', title: '审批信息', align: 'center', width: '30%'}
            , {field: 'createTime', title: '审批时间', align: 'center', width: '20%', templet: "<div>{{layui.util.toDateString(d.ordertime, 'yyyy-MM-dd HH:mm:ss')}}</div>"}
            , {field: 'flag', title: '是否通过', width: '10%',templet:'#flag'}
        ]]
    });


});


