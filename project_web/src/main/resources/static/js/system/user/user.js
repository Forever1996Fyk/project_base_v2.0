/**
 * Created by Administrator on 2019/5/21.
 */
layui.use(['table', 'layer', 'form', 'formSelects'], function() {
    var layer = layui.layer;
    var table = layui.table;
    var formSelects = layui.formSelects;

    formSelects.data('select1', 'local', {
        arr: [
            {"name": "分组-1", "type": "optgroup"},
            {"name": "北京", "value": 1},
            {"name": "上海", "value": 2},
            {"name": "分组-2", "type": "optgroup"},
            {"name": "广州", "value": 3},
            {"name": "深圳", "value": 4},
            {"name": "天津", "value": 5}
        ]
    });

    var tableObject = table.render({
        id:"id"
        ,elem: '#userListTable'
        , height: 500
        , url: '/api/getUsers'//数据接口
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
            , {field: 'account', title: '用户账号', align: 'center'}
            , {field: 'userName', title: '用户名称', align: 'center'}
            , {field: 'nickName', title: '用户昵称', align: 'center'}
            , {field: 'userIcon', title: '头像',  align: 'center'}
            , {field: 'age', title: '年龄', align: 'center'}
            , {field: 'sex', title: '性别', align: 'center', templet: function (data) {
                var result = '';
                if (data.sex === 1) {
                    result = '男';
                } else {
                    result = '女';
                }
                return result;
            }}
            , {field: 'marryFlag', title: '婚否', align: 'center', templet: function (data) {
                var result = '';
                if (data.marry_flag) {
                    if (data.marry_flag === 1) {
                        result = '已婚';
                    } else if (data.marry_flag === 0) {
                        result = '未婚';
                    }
                }
                return result;
            }}
            , {field: 'phone', title: '手机号',align: 'center'}
            , {field: 'email', title: '邮箱', align: 'center'}
            , {field: 'address', title: '地址', align: 'center'}
            , {field: 'idcard', title: '身份证号', align: 'center'}
            , {field: 'createTime', title: '创建时间', align: 'center'}
            , {field: 'status', title: '是否禁用', align: 'center', templet: function (data) {
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
        , done: function () {
            $('td').find('checkbox').attr('class', 'tb-checkbox');
        }
    });
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
                ,title: '编辑用户'
                ,content: '/system/user/edit?id=' + data.id
                ,maxmin: true
                ,area: ['500px', '450px']
            })
        },

        //删除
        delete:function (data) {
            layer.confirm ('确定删除吗?', function (index) {
                $.ajax({
                    url: '/api/user?id=' + data.id,
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
                    userName: $('#frmSearch [name=userName]').val(),
                    nickName: $('#frmSearch [name=nickName]').val()
                }
            })
        },

        //分配角色
        assignRole: function () {
            var checkStatus = table.checkStatus('id');//注意这个id不是html中table元素上的id，而是table:render中定义的id
            if (checkStatus.data.length === 0) {
                layer.msg('请选择用户');
                return;
            } else if (checkStatus.data.length > 1) {
                layer.msg('只能选择一条数据');
                return;
            }
            layer.open({
                type: 2
                ,title: '角色分配'
                ,content: '/system/roleManager/assignRole'
                ,maxmin: true
                ,area: ['500px', '450px']
                ,btn: ['确定', '取消']
                ,yes: function(index, layero){
                    $scope.layerFrame('layui-layer-iframe', 'roleSubmit', index, layero, tableObject, checkStatus.data);
                }
                ,success:function (layero, index) {//弹出框,弹出成功后操作

                    $scope.findRole(layero, checkStatus.data[0]);
                    //iframeWindow.layui.form.render();
                }
            })
        }
    };
    $('.layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});