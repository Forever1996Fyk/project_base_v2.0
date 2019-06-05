layui.use(['element', 'layer', 'form'], function () {
    var layer = layui.layer
        , webScoket
        , commWebScoket
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
            layerFun("连接已关闭");
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

});