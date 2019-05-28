if(window.top!==window.self){window.top.location=window.location};
layui.use(['element', 'layer', 'form'], function () {
    var $ = layui.jquery;
    var form = layui.form;

    //刷新验证码
    $(document).on('click', '.captcha-img', function () {
        var src = this.src.split("?")[0];
        this.src = src + "?" + Math.random();
    });


    form.on('submit(loginSubmit)', function (obj) {
        var data = obj.field;
        console.log(data);
        $.ajax({
            url: '/login',
            type: 'POST',
            data: JSON.stringify(data),
            success: function (res) {
                if (res.code === 200) {
                    layer.msg(res.message, {
                        offset: '15px'
                        ,icon: 1
                        ,time: 1000
                    }, function () {
                        window.location.href = res.data;
                    })
                } else {
                    $('.captcha-img').click();
                    return layer.msg(res.message);
                }
            }
        })
    });
    /*$(document).on('click', '.ajax-login', function (e) {
        e.preventDefault();
        var form = $(this).parents("form");
        var url = form.attr("action");
        var serializeArray = form.serializeArray();
        $.post(url, serializeArray, function (result) {
            if(result.code != 200){
                $('.captcha-img').click();
            }
            $.fn.Messager(result);
        });
    })*/
});