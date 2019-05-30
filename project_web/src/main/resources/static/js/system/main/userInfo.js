/**
 * Created by Administrator on 2019/5/30.
 */
layui.use(['jquery','upload'], function () {
    var $ = layui.jquery;
    var upload = layui.upload;

    $(".user-edit").on("mousedown", function () {
        $(this).css("opacity", 1);
    });

    /* 修改头像 */
    var image = new Image();
    var panel = $(".canvas-panel");
    var bgImg = $(".canvas-bg");
    var canvas = $(".canvas-crop");

    // 激活或停止移动
    var moveEvent = false;
    var screenX = 0, screenY = 0;
    var moveTop = 0, moveLeft = 0;
    panel.on("mousedown",function (e) {
        screenX = e.screenX;
        screenY = e.screenY;
        moveTop = parseFloat(bgImg.css("top"));
        moveLeft = parseFloat(bgImg.css("left"));
        moveEvent = true;
    });
    panel.on("mouseup",function (e) {
        moveEvent = false;
    });
    panel.on("mousemove", function (e) {
        if (moveEvent){
            bgImg.css("left", moveLeft + e.screenX - screenX);
            bgImg.css("top", moveTop + e.screenY - screenY);
            renderPanel();
        }
    });
    panel.on("mousewheel", function(event, delta) {
        var dir = delta > 0 ? 'Up' : 'Down';
        var width = parseFloat(bgImg.css("width"));
        var height = parseFloat(bgImg.css("height"));
        if (dir == 'Up') {
            delta = 1;
        } else {
            delta = -1;
        }
        bgImg.css("width", width * (1 + 0.1 * delta));
        bgImg.css("height", height * (1 + 0.1 * delta));
        bgImg.css("left", parseFloat(bgImg.css("left")) - (width * 0.1 / 2) * delta);
        bgImg.css("top", parseFloat(bgImg.css("top")) - (height * 0.1 / 2) * delta);
        renderPanel();
        return false;
    });

    // 渲染画布面板
    function renderPanel() {
        canvas[0].width = 256;
        canvas[0].height = 256;
        var imgScale = image.width / bgImg.width();
        var context = canvas[0].getContext('2d');
        var sx = (bgImg.width() * imgScale / 2 ) - canvas.width() / 2 * imgScale,
            sy = (bgImg.height() * imgScale / 2) - canvas.height() / 2 * imgScale,
            sw = canvas.width() * imgScale, sh = canvas.height() * imgScale;
        var moveX = panel.width() / 2 - parseFloat(bgImg.css("left")) - bgImg.width() / 2;
        var moveY = panel.height() / 2 - parseFloat(bgImg.css("top")) - bgImg.height() / 2;
        context.drawImage(image, sx + moveX * imgScale, sy + moveY * imgScale, sw, sh, 0, 0, canvas[0].width, canvas[0].height);
    }

    var files = [];
    var uploadInst = upload.render({
        elem: '.edit-avatar'
        ,field: 'picture'
        ,url: $('.edit-avatar').data('url')
        ,exts: 'jpg|png|gif|jpeg'
        ,auto: false
        ,bindAction: ".upload-btn"
        ,multiple: true
        // 选择文件回调
        ,choose: function(obj){
            obj.preview(function(index, file, result){
                panel.show();
                $(".canvas-group").show();
                image.src = result;
                image.onload = function(){
                    bgImg.attr("src", result);
                    if(bgImg.width() >= bgImg.height()){
                        bgImg.css("height", canvas.width());
                    }else {
                        bgImg.css("width", canvas.height());
                    }
                    bgImg.css("top", (panel.height() - bgImg.height()) / 2);
                    bgImg.css("left", (panel.width() - bgImg.width()) / 2);
                    renderPanel();
                }
            });
        }
        ,before: function(obj){
            files = obj.pushFile();
            var index, file;
            for(index in files) {
                file = files[index];
            }
            var dataurl = canvas[0].toDataURL(file.type, 0.92);
            var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
                bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
            while(n--){
                u8arr[n] = bstr.charCodeAt(n);
            }
            files[index] = new File([u8arr], file.name, {type:mime});
        }
        ,done: function(res, index){
            if(res.code === 200){
                panel.hide();
                $(".canvas-group").hide();
                layer.msg("修改头像成功", {offset: '15px', time: 3000, icon: 1});
                $(".user-avatar").attr("src", canvas[0].toDataURL());
                delete files[index];
            }else {
                layer.msg(res.msg, {offset: '15px', time: 3000, icon: 2});
            }
        }
    });

    // 关闭裁切面板及清空文件
    $(".upload-close").on("click", function () {
        panel.hide();
        $(".canvas-group").hide();
    });
});
