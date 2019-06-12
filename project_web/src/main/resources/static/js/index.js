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
});
/*
layui.use(['element', 'layer', 'form', 'notice'], function () {
    var layer = layui.layer
        , webScoket
        , commWebScoket
        , notice = layui.notice
        , userId = $("#userId").val();
    if ("WebSocket" in window ) {
        webScoket = new WebSocket("ws://localhost:8093/webSocketServer/" + userId);

        //连接之后的回调事件
        webScoket.onopen = function () {
            console.log("已经连通了websocket");
            layerFun("已经连通了websocket");
        };

        //接受后台服务端的消息
        webScoket.onmessage = function (p1) {
            var receivedMsg = p1.data;
            console.log("数据已接收：" + receivedMsg);
            var obj = JSON.parse(receivedMsg);
            console.log("可以解析成json:" + obj.messageType);
            //1代表上线 2代表下线 3代表在线名单 4代表普通消息
            switch (obj.messageType) {
                case 1:
                    layerFun(obj.userName + "上线了");
                    break;
                case 2:
                    layerFun(obj.userName + "下线了，所有在线人员：" + obj.onlineUsers);
                    break;
                case 3:
                    layerFun("在线人员名单：" + obj.onlineUsers);
                    break;
                case 4:
                    layerFun("来自" + obj.fromUserName + "的消息对" + obj.toUserName + "说：" + obj.textMessage);
                    break;
            }
        };

        //连接关闭的回调事件
        webScoket.onclose = function () {
            console.log("连接已关闭");
            notice.error("连接已关闭");
            notice.warning("连接已关闭");
            notice.info("连接已关闭");
            notice.success("连接已关闭");
            //layerFun("连接已关闭");
        }
    }


    var layerFun = function (content) {
        layer.open({
            type: 1,
            title: '消息提醒',
            content: '<div style="padding: 20px 100px;">'+ content + '</div>',
            offset: 'rb',
            time: 3000,
            anim: 2,
            shade: 0,
            skin: 'layui-layer-lan'
        })
    }

});*/
