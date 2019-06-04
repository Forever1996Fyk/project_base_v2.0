layui.use(['element', 'layer', 'form'], function () {
    var layer = layui.layer;
    setInterval(function () {
        layer.open({
            type: 1,
            title: '消息提醒',
            content: '消息提醒',
            offset: 'rb',
            time: 3000,
            anim: 2,
            shade: 0,
            skin: 'layui-layer-lan',
            area: ['320px', '140px']
        })
    },6000)

});