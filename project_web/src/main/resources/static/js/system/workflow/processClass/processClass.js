/**
 * Created by YuKai Fan on 2019-08-12 10:14:44.
 */
layui.use(['table', 'layer', 'form', 'formSelects'], function () {
    var layer = layui.layer;
    var table = layui.table;

    var tableObject = table.render({
        id: "id"
        , elem: '#processClassListTable'
        , height: 500
        , url: ctxPath + '/api/getProcessClasss'//数据接口
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
            , {field: 'name', title: '流程名称', align: 'center'}
            , {field: 'type', title: '流程类型', align: 'center'}
            , {field: 'classIcon', title: '流程分类图标', align: 'center', templet: function (data) {
                var result = "";
                if (data.classIcon) {
                    result = '<img src=/api/attachments/' + data.classIcon + '/0/image style="width: 30px;height: 30px">';
                }

                return result;
            }}
            , {field: 'applyUrl', title: '流程申请页面url', align: 'center'}
            , {field: 'applyListUrl', title: '我的申请列表页面url', align: 'center'}
            , {field: 'upcomingListUrl', title: '我的待办列表页面url', align: 'center'}
            , {field: 'alreadyListUrl', title: '我的已办列表页面url', align: 'center'}
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
                , content: ctxPath + '/system/activiti/editProcessClass?id=' + data.id
                , maxmin: true
                , area: ['800px', '500px']
            })
        },

        //删除
        delete: function (data) {
            layer.confirm('确定删除吗?', function (index) {
                $.ajax({
                    url: ctxPath + '/api/processClass?id=' + data.id,
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


