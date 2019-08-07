/**
 * Created by YuKai Fan on 2019-05-31 15:39:53.
 */
layui.use(['table', 'layer', 'form', 'formSelects'], function() {
    var layer = layui.layer;
    var table = layui.table;

    var tableObject = table.render({
        id:"id"
        ,elem: '#modelListTable'
        , height: 500
        , url: '/api/activiti/getModels'//数据接口
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
            , {field: 'name', title: '模型名称', align: 'center'}
            , {field: 'key', title: 'key', align: 'center'}
            , {field: 'version', title: '版本', align: 'center'}
            , {field: 'deploymentId', title: '部署ID', align: 'center'}
            , {field: 'createTime', title: '创建时间', align: 'center'}
            , {field: 'lastUpdateTime', title: '最后更新时间', align: 'center'}
            , {title: '操作', toolbar: '#btn', align: 'center'}
        ]]
    });
    /*
     表格点击事件写法,其中tool('这是表格的lay-filter'),监听点击事件         */
    table.on('tool(tblist)', function (obj) {
        switch (obj.event) {
            case 'deploy':
                active.deploy(obj.data);
                break;
            case 'start':
                active.start(obj.data);
                break;
            case 'export':
                active.export(obj.data);
                break;
        }
    });

    var active = {
        //发布
        deploy:function (data) {
            layer.confirm ('确定部署流程吗?', function (index) {
                $.ajax({
                    url: ctxPath + '/api/activiti/deployment',
                    type: 'post',
                    data: {id : data.id},
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

        //启动流程
        start:function (data) {
            layer.confirm ('确定启动流程吗?', function (index) {
                $.ajax({
                    url: ctxPath + '/api/activiti/startProcess',
                    type: 'post',
                    data: {deploymentId : data.deploymentId},
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

        //导出
        export:function (data) {
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
            var checkStatus = table.checkStatus('id')//注意这个id不是html中table元素上的id，而是table:render中定义的id
                ,ids = [];
            if (checkStatus.data.length !== 1) {
                layer.msg('请选择一条数据');
                return;
            }

            var id = checkStatus.data[0].id;
            layer.confirm ('确定删除吗?', function (index) {
                $.ajax({
                    url: ctxPath + '/api/activiti/delModel?id=' + id,
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
        //创建模型
        createModel: function () {
            layer.open({
                type: 2
                ,title: '创建模型'
                ,content: ctxPath + '/system/activiti/createModel'
                ,maxmin: true
                ,area: ['500px', '450px']
            })
        },

        synchronizeData: function () {
              $.ajax({
                  url: ctxPath + '/api/activiti/synchronizeData',
                  type: 'POST',
                  success: function (res) {
                      layer.msg(res.message);
                  }
              })
        },

        //设计流程图
        design: function () {
            var checkStatus = table.checkStatus('id')//注意这个id不是html中table元素上的id，而是table:render中定义的id
                ,ids = [];
            if (checkStatus.data.length !== 1) {
                layer.msg('请选择一条数据');
            }
            var id = checkStatus.data[0].id;

            var url = '/modeler.html?modelId=' + id;
            window.location.href = ctxPath + url;
        },

    };
    $('.layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});


