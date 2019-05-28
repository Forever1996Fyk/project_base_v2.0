/**
 * Created by Administrator on 2019/5/24.
 */
layui.use(['table', 'layer', 'form', 'formSelects'], function() {
    var layer = layui.layer;
    var table = layui.table;
    var formSelects = layui.formSelects;

    var tableObject = table.render({
        id: "id"
        , elem: '#tableColumnListTable'
        , height: 500
        , url: ctxPath + '/api/generator/getDBTables'//数据接口
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
            , {field: 'tableName', title: '数据库表名', align: 'center'}
            , {field: 'tableComment', title: '数据库表注释', align: 'center'}
            , {field: 'engine', title: '数据库表引擎', align: 'center'}
            , {field: 'createTime', title: '创建时间', align: 'center'}
            , {title: '操作', toolbar: '#btn', align: 'center'}
        ]]
    });

    /*
     表格点击事件写法,其中tool('这是表格的lay-filter'),监听点击事件         */
    table.on('tool(tblist)', function (obj) {
        if (obj.event === 'columsView') {
            active.columsView(obj.data);
        }

    });

    var active = {

        //查看
        columsView:function (data) {
            var columsView = layer.open({
                type: 2
                ,title: '查看数据库表字段'
                ,content: ctxPath + '/system/generator/columnsView/' + data.tableName
                ,maxmin: true
                ,area: ['700px', '700px']
            });
            layer.full(columsView);
        },

        //搜索
        search:function () {
            tableObject.reload({
                where:{
                    tableName: $('#frmSearch [name=tableName]').val(),
                }
            })
        },

        //生成代码
        generatorCode: function () {
            var checkStatus = table.checkStatus('id')//注意这个id不是html中table元素上的id，而是table:render中定义的id
                ,tableNames = [];
            if (checkStatus.data.length === 0) {
                return layer.msg('请选择数据行');
            }

            for(var i = 0; i < checkStatus.data.length; i++) {
                tableNames.push(checkStatus.data[i].tableName);
            }

            window.location.href = ctxPath + '/api/generator/code/' + tableNames.join(",");
        }
    };
    $('.layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});
