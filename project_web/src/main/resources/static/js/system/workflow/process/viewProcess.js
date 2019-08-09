/**
 * Created by YuKai Fan on 2019-08-08 10:30:23.
 */
layui.use(['form', 'layer'], function(){
    var layer = layui.layer
        ,form = layui.form
        ,countNum = 0;


    var processInstanceId = $("#processInstanceId").val();
    if (processInstanceId === undefined) {
        layer.msg("页面错误!!!");
        return;
    }

    $.ajax({
        url: ctxPath + '/api/activiti/getHighLightProcImage?processInstanceId=' + processInstanceId,
        type: 'GET',
        success: function (res) {
            if (res.code === 200) {
                var data = res.data.images;

                var imgObj1 = document.getElementById("showImages1");
                imgObj1.src = "data:image/png;base64," + data[0];

                var imgObj2 = document.getElementById("showImages2");
                imgObj2.src = "data:image/png;base64," + data[1];

                window.setInterval(function () {
                    if (countNum == 0) {
                        $("#showImages1").show();
                        $("#showImages2").hide();
                    } else {
                        $("#showImages1").hide();
                        $("#showImages2").show();
                    }

                    countNum++;

                    if (countNum == 2) {
                        countNum = 0; //返回原图
                    }
                }, 1000)
            }
            console.log(res);
        }

    })



});


