/**
 * Created by Administrator on 2019/6/3.
 */
layui.use(['layer', 'carousel'], function() {
    var $ = layui.jquery
        , carousel = layui.carousel
    , echart = echarts.init(document.getElementById('dataView'))
    , echartMap = echarts.init(document.getElementById('mapEch'))
    , option = {
        title: {text: '用户登陆日志', x: 'center', textStyle: {fontSize: 14}},
        tooltip: {trigger: "axis"},
        toolbox: {
            show: true,
            feature: {
                myTool1: {
                    show: true,
                    title: '按年份显示',
                    icon: 'image://'+ ctxPath +'/images/icon/year.png',
                    onclick: function () {
                        getData("year");
                    }
                },
                myTool2: {
                    show: true,
                    title: '按月份显示',
                    icon: 'image://'+ ctxPath +'/images/icon/month.png',
                    onclick: function () {
                        getData("month");
                    }
                },
                myTool3: {
                    show: true,
                    title: '按日期显示',
                    icon: 'image://'+ ctxPath +'/images/icon/date.png',
                    onclick: function () {
                        getData("date");
                    }
                },
                magicType: {type: ['bar']},
                restore: {},
                saveAsImage: {}
            },
            top: 'auto'
        },
        legend: {data: ["", ""]},
        xAxis: [{
            type: 'category',
            boundaryGap: !1,
            name: '日期',
            data: []
        }],
        yAxis: [{type: 'value', name: '总数'}],
        series: [{
            type: 'line',
            name: '登陆日志',
            smooth: !0,
            itemStyle: {normal: {areaStyle: {type: "default"}}},
            data: []
        }]

    }
    , mapOption = {
        title: {text: "全国的 layui 用户分布", subtext: "不完全统计"},
        tooltip: {trigger: "item"},
        dataRange: {orient: "horizontal", min: 0, max: 6e4, text: ["高", "低"], splitNumber: 0},
        series: [{
            name: "全国的 layui 用户分布",
            type: "map",
            mapType: "china",
            selectedMode: "multiple",
            itemStyle: {normal: {label: {show: !0}}, emphasis: {label: {show: !0}}},
            data: [{name: "西藏", value: 60}, {name: "青海", value: 167}, {name: "宁夏", value: 210}, {
                name: "海南",
                value: 252
            }, {name: "甘肃", value: 502}, {name: "贵州", value: 570}, {name: "新疆", value: 661}, {
                name: "云南",
                value: 8890
            }, {name: "重庆", value: 10010}, {name: "吉林", value: 5056}, {name: "山西", value: 2123}, {
                name: "天津",
                value: 9130
            }, {name: "江西", value: 10170}, {name: "广西", value: 6172}, {name: "陕西", value: 9251}, {
                name: "黑龙江",
                value: 5125
            }, {name: "内蒙古", value: 1435}, {name: "安徽", value: 9530}, {name: "北京", value: 51919}, {
                name: "福建",
                value: 3756
            }, {name: "上海", value: 59190}, {name: "湖北", value: 37109}, {name: "湖南", value: 8966}, {
                name: "四川",
                value: 31020
            }, {name: "辽宁", value: 7222}, {name: "河北", value: 3451}, {name: "河南", value: 9693}, {
                name: "浙江",
                value: 62310
            }, {name: "山东", value: 39231}, {name: "江苏", value: 35911}, {name: "广东", value: 55891}]
        }]
    };

    $('.layadmin-carousel').each(function () {
        carousel.render({
            elem: this,
            width: '100%',
            autoplay: false,
            arrow: 'none',
            trigger: 'click'
        });
    });

    $(function () {
        getData("date");
    });

    var getData = function (type) {
        $.ajax({
            url: ctxPath + "/api/getLogGroupByType?type=" + type,
            type: 'get',
            conentType: 'Application/JSON; charset=utf-8',
            success: function (res) {
                if (res.code === 200) {
                    console.log(res);
                    if (res.data.length > 0) {
                        option.xAxis[0].data = [];
                        option.series[0].data = [];
                        if (type === 'year') {
                            option.xAxis[0].name = '年份';
                        } else if (type === 'month') {
                            option.xAxis[0].name = '月份';
                        }
                        echart.clear();
                        for (var i = 0; i < res.data.length; i++) {
                            option.xAxis[0].data.push(res.data[i].createTime);
                            option.series[0].data.push(res.data[i].count);
                        }

                    }
                    echart.setOption(option);
                }
            }
        })
    };

    echartMap.setOption(mapOption);


});