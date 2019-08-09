/**
 * Created by YuKai Fan on 2019-08-08 10:30:23.
 */
layui.use(['table', 'layer', 'form', 'formSelects'], function () {
    var layer = layui.layer;
    var table = layui.table;

    var tableObject = table.render({
        id: "id"
        , elem: '#userLeaveListTable'
        , height: 500
        , url: ctxPath + '/api/activiti/getUserLeaves'//数据接口
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
            , {field: 'userId', title: '用户标识', align: 'center'}
            , {field: 'startTime', title: '开始时间', align: 'center'}
            , {field: 'endTime', title: '结束时间', align: 'center'}
            , {field: 'reason', title: '请假原因', align: 'center'}
            , {field: 'leaveDays', title: '请假天数', align: 'center'}
            , {field: 'processInstanceId', title: '流程实例id', align: 'center'}
            , {field: 'urlPath', title: '只读申请信息url路径', align: 'center'}
            , {
                field: 'status', title: '状态', align: 'center', templet: function (data) {
                    var result;
                    if (data.status === 1) {
                        result = '正常';
                    } else {
                        result = '禁用';
                    }
                    return result;
                }
            }
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
            case 'del':
                active.delete(obj.data);
                break;
        }
    });

    var active = {
        //编辑
        edit: function (data) {
            layer.open({
                type: 2
                , title: '编辑'
                , content: ctxPath + '/system/userLeave/edit?id=' + data.id
                , maxmin: true
                , area: ['500px', '450px']
            })
        },

        //删除
        delete: function (data) {
            layer.confirm('确定删除吗?', function (index) {
                $.ajax({
                    url: ctxPath + '/api/userLeave?id=' + data.id,
                    type: 'delete',
                    success: function (res) {
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


