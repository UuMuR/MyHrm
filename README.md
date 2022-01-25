# 1/25
改动：
1、加入了feign远程调用组件, nacos注册中心
        -- springcloud(Hoxton.SR8)，springboot(2.2.6_R)，nacos(2.2.5_R)
2、添加了employee模块下批量导入信息功能
3、添加了employee模块下生成当月报表功能
4、完善了permission模块逻辑
        -- 自动为平台管理员与企业管理员绑定所有相关权限

问题：
1、平台管理员没有companyId，在department模块和employee模块下会报错
        -- 目前简单处理，抛出异常提示
2、生成当月报表，页面无法弹出保存
        -- 数据流正常返回，问题应该处在页面代码，找不出bug，我很抱歉
