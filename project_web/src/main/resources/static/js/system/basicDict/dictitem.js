/**
 * Created by YuKai Fan on 2019-05-24 16:13:29.
 */
layui.use(['table', 'layer', 'form', 'formSelects'], function() {
    var layer = layui.layer;

    var active = {

        //添加
        add: function () {
            layer.open({
                type: 2
                ,title: '编辑'
                ,content: '/system/basicDict/addDictItem/' + $('#dictItemAdd').data('dicid')
                ,maxmin: true
                ,area: ['500px', '450px']
            })
        }

    };
    $('.layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});


