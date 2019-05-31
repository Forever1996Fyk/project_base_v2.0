/**
 * Created by YuKai Fan on 2019-05-31 15:39:53.
 */
layui.use(['table', 'layer', 'form', 'formSelects'], function() {
    var layer = layui.layer;
    var table = layui.table;
    var formSelects = layui.formSelects;

    var tableObject = table.render({
        id:"id"
        ,elem: '#actionLogListTable'
        , height: 500
        , url: '/api/getActionLogs'//数据接口
        , page: true
        , limits: [10,20,30,40,50]
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
            , {field: 'name', title: '日志名称', align: 'center'}
            , {field: 'typeName', title: '日志类型', align: 'center'}
            , {field: 'ipaddr', title: '操作IP地址', align: 'center'}
            , {field: 'clazz', title: '产生日志的类', align: 'center'}
            , {field: 'method', title: '产生日志的方法', align: 'center'}
            , {field: 'message', title: '日志消息', align: 'center'}
            , {field: 'createTime', title: '创建时间', align: 'center'}
            , {field: 'createUserName', title: '创建人', align: 'center'}
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
        edit:function (data) {
            layer.open({
                type: 2
                ,title: '编辑'
                ,content: '/system/actionLog/edit?id=' + data.id
                ,maxmin: true
                ,area: ['500px', '450px']
            })
        },

        //删除
        delete:function (data) {
            layer.confirm ('确定删除吗?', function (index) {
                $.ajax({
                    url: '/api/actionLog?id=' + data.id,
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
        //搜索
        search:function () {
            debugger;
            tableObject.reload({
                where:{
                    
                }
            })
        },
        //清空日志
        clearLog: function () {
            layer.confirm ('确定清空吗?', function (index) {
                $.ajax({
                    url: '/api/delActionLogReals',
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
        }

    };
    $('.layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});


