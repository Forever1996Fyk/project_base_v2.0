/**
 * Created by Administrator on 2019/5/21.
 */
layui.use(['table', 'layer', 'form'], function() {
    var layer = layui.layer;
    var table = layui.table;

    var tableObject = table.render({
        id:"id"
        ,elem: '#userListTable'
        , height: 500
        , url: ctxPath + '/api/getUsers'//数据接口
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
            , {field: 'marryFlagName', title: '婚否', align: 'center'}
            , {field: 'phone', title: '手机号',align: 'center'}
            , {field: 'educationName', title: '学历', align: 'center'}
            , {field: 'email', title: '邮箱', align: 'center'}
            , {field: 'address', title: '地址', align: 'center'}
            , {field: 'idcard', title: '身份证号', align: 'center'}
            , {field: 'createTime', title: '创建时间', align: 'center'}
            , {field: 'statusName', title: '是否禁用', align: 'center'}
            , {title: '操作', toolbar: '#btn', align: 'center'}
        ]]
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
                ,content: ctxPath + '/system/user/edit?id=' + data.id
                ,maxmin: true
                ,area: ['500px', '450px']
            })
        },

        //删除
        delete:function (data) {
            layer.confirm ('确定删除吗?', function (index) {
                $.ajax({
                    url: ctxPath + '/api/user?id=' + data.id,
                    type: 'delete',
                    contentType: 'Application/JSON; charset=utf-8',
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
                    userName: $('#frmSearch [name=userName]').val(),
                    nickName: $('#frmSearch [name=nickName]').val()
                }
            })
        }
    };
    $('.layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });


});