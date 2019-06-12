/**
 * Created by YuKai Fan on 2019-06-10 09:56:24.
 */
layui.use(['table', 'layer', 'form', 'formSelects'], function() {
    var layer = layui.layer;
    var table = layui.table;

    var tableObject = table.render({
        id:"id"
        ,elem: '#myNoticeListTable'
        , height: 500
        , url: '/api/getMyNotices'//数据接口
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
        , {field: 'createUserName', title: '发送人', align: 'center'}
        , {field: 'content', title: '通告内容', align: 'center'}
        , {field: 'priorityName', title: '优先级', align: 'center'}
        , {field: 'readed', title: '是否已读', align: 'center', templet: function (data) {
            var result;
            if (data.readed === 0) {
                result = '<span style="color: red">未读</span>';
            } else {
                result = '<span style="color: #316AC5">已读</span>';
            }
            return result;
        }}
        , {field: 'readedTime', title: '阅读时间', align: 'center'}
        , {field: 'receiveTime', title: '接收时间', align: 'center'}
        , {field: 'remark', title: '备注', align: 'center'}
        , {title: '操作', toolbar: '#btn', align: 'center'}
        ]]
    });
    /*
     表格点击事件写法,其中tool('这是表格的lay-filter'),监听点击事件         */
    table.on('tool(tblist)', function (obj) {
        switch (obj.event) {
            case 'contentView':
                active.contentView(obj.data);
                break;
            case 'reply':
                active.reply(obj.data);
                break;
        }
    });

    var active = {
        //查看
        contentView:function (data) {
            layer.open({
                type: 2
                ,title: '查看内容'
                ,content: ctxPath + '/system/notice/contentView?id=' + data.id + '&noticeId=' + data.noticeId
                ,maxmin: true
                ,area: ['1068px', '650px']
                ,success: function () {
                    if (data.readed === 0) {
                        tableObject.reload({})
                    }
                }
            })
        },

        //删除
        reply:function (data) {
        },

        //搜索
        search:function () {
            tableObject.reload({
                where:{
                    readed: $('#frmSearch [name=readed]').val(),
                    priority: $('#frmSearch [name=priority]').val(),
                    title: $('#frmSearch [name=title]').val(),
                    content: $('#frmSearch [name=content]').val()
                }
            })
        },

        //全部标记已读
        allMarkReaded: function () {
            $.ajax({
                url: ctxPath + '/api/notice/allMarkReaded',
                type: 'GET',
                success: function (res) {
                    tableObject.reload({});
                }
            })
        }

    };
    $('.layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});


