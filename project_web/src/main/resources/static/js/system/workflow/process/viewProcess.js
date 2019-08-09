/**
 * Created by YuKai Fan on 2019-08-08 10:30:23.
 */
layui.use(['form', 'layer'], function(){
    var layer = layui.layer
        ,form = layui.form;

    var processInstanceId = $("#processInstanceId").val();
    if (processInstanceId === undefined) {
        layer.msg("页面错误!!!");
        return;
    }

    $.ajax({
        url: ctxPath + '/api/activiti/getHighLightProcImage?processInstanceId=' + processInstanceId,
        type: 'GET',
        success: function (res) {
            console.log(res);
        }

    })



});


