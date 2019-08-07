/**
 * Created by YuKai Fan on 2019-05-31 15:39:53.
 */
layui.use(['table', 'layer', 'form', 'formSelects'], function() {
    var layer = layui.layer;
    var table = layui.table;

    var tableObject = table.render({
        id:"id"
        ,elem: '#processListTable'
        , height: 500
        , url: '/api/activiti/getActProcessDeploys'//数据接口
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
            , {field: 'id', title: 'ID', align: 'center'}
            , {field: 'name', title: '流程名称', align: 'center'}
            , {field: 'key', title: 'key', align: 'center'}
            , {field: 'version', title: '版本', align: 'center'}
            , {field: 'deploymentId', title: '部署ID', align: 'center'}
            , {field: 'category', title: '类别', align: 'center'}
            , {field: 'resourceName', title: '资源名称', align: 'center'}
        ]]
    });
    /*
     表格点击事件写法,其中tool('这是表格的lay-filter'),监听点击事件         */
    table.on('tool(tblist)', function (obj) {
        switch (obj.event) {
            case 'del':
                active.delete(obj.data);
                break;
        }
    });

    var active = {

        //删除
        delete:function () {
            var checkStatus = table.checkStatus('id')//注意这个id不是html中table元素上的id，而是table:render中定义的id
                ,ids = [];
            if (checkStatus.data.length !== 1) {
                layer.msg('请选择一条数据');
                return;
            }

            var deploymentId = checkStatus.data[0].deploymentId;
            layer.confirm ('确定删除吗?', function (index) {
                $.ajax({
                    url: ctxPath + '/api/activiti/delProcessDeploy?id=' + deploymentId,
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
                    deploymentId: $('#frmSearch [name=deploymentId]').val(),
                    name: $('#frmSearch [name=name]').val(),
                    key: $('#frmSearch [name=key]').val()
                }
            })
        },

    };
    $('.layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});


