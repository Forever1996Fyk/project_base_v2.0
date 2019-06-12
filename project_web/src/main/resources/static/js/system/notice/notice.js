/**
 * Created by YuKai Fan on 2019-06-10 09:56:24.
 */
layui.use(['table', 'layer', 'form', 'formSelects'], function() {
    var layer = layui.layer;
    var table = layui.table;

    var tableObject = table.render({
        id:"id"
        ,elem: '#noticeListTable'
        , height: 500
        , url: '/api/getNotices'//数据接口
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
        , {field: 'title', title: '通告标题', align: 'center'}
        , {field: 'content', title: '通告内容', align: 'center'}
        , {field: 'publishTime', title: '发布时间', align: 'center'}
        , {field: 'cancelName', title: '是否撤销', align: 'center'}
        , {field: 'cancelTime', title: '撤销时间', align: 'center'}
        , {field: 'priorityName', title: '优先级', align: 'center'}
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
                ,content: '/system/notice/edit?id=' + data.id
                ,maxmin: true
                ,area: ['1068px', '650px']
            })
        },

        //删除
        delete:function (data) {
            layer.confirm ('确定删除吗?', function (index) {
                $.ajax({
                    url: '/api/notice?id=' + data.id,
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
            tableObject.reload({
                where:{
                    status: $('#frmSearch [name=status]').val(),
                    priority: $('#frmSearch [name=priority]').val(),
                    cancel: $('#frmSearch [name=cancel]').val(),
                    title: $('#frmSearch [name=title]').val(),
                    content: $('#frmSearch [name=content]').val()
                }
            })
        }

    };
    $('.layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});


