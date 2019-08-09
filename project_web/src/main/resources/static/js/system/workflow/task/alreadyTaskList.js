/**
 * Created by YuKai Fan on 2019-08-08 10:30:23.
 */
layui.use(['table', 'layer', 'form', 'formSelects'], function () {
    var layer = layui.layer;
    var table = layui.table;

    var tableObject = table.render({
        id: "id"
        , elem: '#alreadyTaskListTable'
        , height: 500
        , url: ctxPath + '/api/activiti/getUserHistoryTask'//数据接口
        , page: true
        , limits: [10, 20, 30, 40, 50]
        , limit: 10
        , parseData: function (res) {
            return {
                "code": res.code,
                "msg": res.message,
                "count": res.data.total,
                "data": res.data.rows
            }
        }
        , cols: [[ //表头,field要与实体类字段相同
            {type: 'checkbox'}
            , {field: 'id', title: '任务编码', align: 'center'}
            , {field: 'name', title: '任务名称', align: 'center'}
            , {field: 'startTime', title: '开始时间', align: 'center', templet : "<div>{{layui.util.toDateString(d.ordertime, 'yyyy-MM-dd HH:mm:ss')}}</div>"}
            , {field: 'endTime', title: '结束时间', align: 'center', templet : "<div>{{layui.util.toDateString(d.ordertime, 'yyyy-MM-dd HH:mm:ss')}}</div>"}
            , {field: 'createTime', title: '创建时间', align: 'center', templet : "<div>{{layui.util.toDateString(d.ordertime, 'yyyy-MM-dd HH:mm:ss')}}</div>"}
            , {field: 'assignee', title: '委派人', align: 'center'}
            , {field: 'processInstanceId', title: '流程实例id', align: 'center'}
        ]]
    });
    /*
     表格点击事件写法,其中tool('这是表格的lay-filter'),监听点击事件         */
    table.on('tool(tblist)', function (obj) {
    });

    var active = {
        //搜索
        search: function () {
            tableObject.reload({
                where: {}
            })
        },

    };
    $('.layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});


