layui.config({
    base: ctxPath + '/lib/dist/'
}).extend({
    //消息提示框插件
   notice: 'notice'
});
var curUserId = $("#curUserId").val(),
    socket = io('http://localhost:9090?userId=' + curUserId);

layui.use(['element', 'layer', 'form', 'notice', 'element'], function () {
    var layer = layui.layer
        , notice = layui.notice
        , element = layui.element
        , tabFlag = false;

    notice.options = {
        iconClass: 'toast-info',//因为是通知所以图标一致, 颜色不同
        onclick: function () {
            var url = ctxPath + '/system/notice/myNotice';
            var iframeUrl = url.replace('//','/');
            if (!tabFlag) {
                element.tabAdd('iframe-tabs', {
                    title: "我的通告"
                    , content: '<iframe src="' + iframeUrl + '" frameborder="0" class="layui-layout-iframe"></iframe>'
                    , id: ctxPath + '/system/notice/myNotice'
                });
                element.tabChange('iframe-tabs', url);
                tabFlag = true;
            } else {
                element.tabChange('iframe-tabs', url);
            }
        }
    };

    socket.on('connect', function () {
        notice.success("客户端已连接");
    });

    socket.on('chatEvent', function (data) {
        switch (data.priority) {//info 无-4表示优先级越高
            case 0:
                notice.success(data.message);
                break;
            case 1:
                notice.info(data.message);
                break;
            case 2:
                notice.warning(data.message);
                break;
            case 3:
                notice.error(data.message);
                break;
        }
    });

    socket.on('disconnect', function () {
        notice.warning("连接已关闭");
    });


    //获取当前用户位置, 采用腾讯的webservice api定位需要在腾讯位置服务官网中申请key
    $.getScript('https://apis.map.qq.com/ws/location/v1/ip?callback=showLocation&key=U7CBZ-HSCOX-6YT44-ZBBUK-RXYXT-E4F3U&output=jsonp');

    $('div.skin > div.skinChild > div').on('click', function() {
        var $that = $(this);
        var skin = $that.children('a').data('skin');
        switchSkin(skin);
    });
    var setSkin = function(value) {
            layui.data('kit_skin', {
                key: 'skin',
                value: value
            });
        },
        getSkinName = function() {
            return layui.data('kit_skin').skin;
        },
        switchSkin = function(value) {
            var _target = $('link[kit-skin]')[0];
            _target.href = _target.href.substring(0, _target.href.lastIndexOf('/') + 1) + value + _target.href.substring(_target.href.lastIndexOf('.'));
            setSkin(value);
        },
        initSkin = function() {
            var skin = getSkinName();
            switchSkin(skin === undefined ? 'static.css.themes.default' : skin);
        }();
});

var showLocation = function (data) {
    var adInfo = data.result.ad_info;
    $("#currLocal").html(adInfo.nation + " " + adInfo.province + " " + adInfo.city + " " + adInfo.district);
};