/**
 * Created by YuKai Fan on 2019-05-24 16:13:29.
 */
layui.use(['table', 'layer', 'form', 'formSelects'], function() {
    var layer = layui.layer;
    var table = layui.table;

    var tableObject = table.render({});
    /*
     表格点击事件写法,其中tool('这是表格的lay-filter'),监听点击事件         */
    table.on('tool(tblist)', function (obj) {
        if (obj.event === 'edit') {
            active.edit(obj.data);
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
                ,content: '/system/dictItem/edit?id=' + data.id
                ,maxmin: true
                ,area: ['500px', '450px']
            })
        },

        //删除
        delete:function (data) {
            layer.confirm ('确定删除吗?', function (index) {
                $.ajax({
                    url: '/api/dictItem?id=' + data.id,
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


