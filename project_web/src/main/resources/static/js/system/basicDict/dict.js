/**
 * Created by YuKai Fan on 2019-05-24 16:13:29.
 */
layui.use(['table', 'layer', 'form', 'formSelects'], function() {
    var layer = layui.layer;
    var table = layui.table;

    var tableObject = table.render({
        id:"id"
        ,elem: '#dictListTable'
        , height: 500
        , url: '/api/getDicts'//数据接口
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
            , {field: 'dicCode', title: '字典编码', align: 'center'}
            , {field: 'dicName', title: '字典名称', align: 'center'}
            , {field: 'remark', title: '备注', align: 'center'}
            , {field: 'status', title: '状态', align: 'center', templet: function (data) {
                        var result;
                        if (data.status === 1) {
                            result = '正常';
                        } else {
                            result = '禁用';
                        }
                        return result;
                    }}
            , {title: '操作', toolbar: '#btn', align: 'center'}
        ]]
    });
    /*
     表格点击事件写法,其中tool('这是表格的lay-filter'),监听点击事件         */
    table.on('tool(tblist)', function (obj) {
        if (obj.event === 'edit') {
            active.edit(obj.data);
        } else if(obj.event === 'dictItemsView') {
            active.dictItemsView(obj.data);
        } else {
            active.delete(obj.data);
        }

    });

    var active = {
        //编辑
        edit:function (data) {
            layer.open({
                type: 2
                ,title: '编辑'
                ,content: '/system/basicDict/editDict?id=' + data.id
                ,maxmin: true
                ,area: ['500px', '450px']
            })
        },

        //查看数据字典项
        dictItemsView: function (data) {
            var dictItemsView = layer.open({
                type: 2
                ,title: '数据字典项'
                ,content: '/system/basicDict/dictItemsView?id=' + data.id
                ,maxmin: true
                ,area: ['700px', '700px']
            });

            layer.full(dictItemsView);
        },

        //删除
        delete:function (data) {
            layer.confirm ('确定删除吗?', function (index) {
                $.ajax({
                    url: '/api/dict?id=' + data.id,
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

    };
    $('.layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});


