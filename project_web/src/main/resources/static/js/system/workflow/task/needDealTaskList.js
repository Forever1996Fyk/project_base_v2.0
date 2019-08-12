/**
 * Created by YuKai Fan on 2019-08-08 10:30:23.
 */
layui.use(['table', 'layer', 'form', 'formSelects'], function () {
    var layer = layui.layer;
    var table = layui.table;

    var tableObject = table.render({
        id: "id"
        , elem: '#needDealTaskListTable'
        , height: 500
        , url: ctxPath + '/api/activiti/getTasksForSL'//数据接口
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
            , {field: 'userName', title: '申请人', align: 'center'}
            , {field: 'reason', title: '原因', align: 'center'}
            , {field: 'name', title: '任务名称', align: 'center'}
            , {field: 'createTime', title: '创建时间', align: 'center', templet : "<div>{{layui.util.toDateString(d.ordertime, 'yyyy-MM-dd HH:mm:ss')}}</div>"}
            , {field: 'assignee', title: '委派人', align: 'center'}
            , {field: 'processInstanceId', title: '流程实例id', align: 'center'}
            , {title: '操作', toolbar: '#btn', align: 'center'}
        ]]
    });
    /*
     表格点击事件写法,其中tool('这是表格的lay-filter'),监听点击事件         */
    table.on('tool(tblist)', function (obj) {
        switch (obj.event) {
            case 'edit':
                active.edit(obj.data);
                break;
            case 'handle':
                active.handle(obj.data);
                break;
            case 'detail':
                active.detail(obj.data);
                break;
        }
    });

    var active = {
        //编辑
        edit: function (data) {
            layer.open({
                type: 2
                , title: '编辑'
                , content: ctxPath + '/system/activiti/needDealTaskEdit/' + data.id
                , maxmin: true
                , area: ['500px', '450px']
            })
        },

        //办理
        handle: function (data) {
            layer.open({
                type: 2
                , title: '办理'
                , content: ctxPath + '/system/activiti/handleTask/' + data.id
                , maxmin: true
                , area: ['700px', '500px']
            })
        },

        //查看详情
        detail: function (data) {
            layer.open({
                type: 2
                , title: '详情'
                , content: ctxPath + '/system/activiti/viewProcDetail/' + data.processInstanceId
                , maxmin: true
                , area: ['1600px', '400px']
            })
        },

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


