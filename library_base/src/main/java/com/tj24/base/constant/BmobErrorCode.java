package com.tj24.base.constant;

import java.util.HashMap;

public class BmobErrorCode {
    public static BmobErrorCode instance;
    private HashMap<Integer, String> erro = new HashMap<>();

    public BmobErrorCode() {
        erro = init();
    }

    public static BmobErrorCode getInstance(){
        if(instance==null){
            instance = new BmobErrorCode();
        }
        return instance;
    }

    private HashMap<Integer,String> init() {
        erro.put(9001, "Application Id为空，请初始化 ");
        erro.put(9002, "解析返回数据出错 ");
        erro.put(9003, "上传文件出错 ");
        erro.put(9004, "文件上传失败");
        erro.put(9005, "批量操作只支持最多50条 ");
        erro.put(9006, "objectId为空 ");
        erro.put(9007, "文件大小超过10M ");
        erro.put(9008, "上传文件不存在 ");
        erro.put(9009, " 没有缓存数据");
        erro.put(9010, "网络超时 ");
        erro.put(9011, "BmobUser类不支持批量操作 ");
        erro.put(9012, "上下文为空 ");
        erro.put(9013, "BmobObject（数据表名称）格式不正确 ");
        erro.put(9014, "第三方账号授权失败 ");
        erro.put(9015, "其他错误均返回此code ");
        erro.put(9016, "无网络连接，请检查您的手机网络. ");
        erro.put(9017, "与第三方登录有关的错误，具体请看对应的错误描述 ");
        erro.put(9018, "参数不能为空 ");
        erro.put(9019, "格式不正确：手机号码、邮箱地址、验证码 ");

        erro.put(401, "unauthorized");
        erro.put(500, "It is busy...Try it later! ");
        erro.put(101, "登录接口的用户名或密码不正确");
        erro.put(102, "查询中的字段名是大小写敏感的，且必须以英文字母开头，有效的字符仅限在英文字母、数字以及下划线。，或查询对应的字段值不匹配，或提供的地理位置格式不正确");
        erro.put(103, "查询单个对象或更新对象时必须提供objectId 或 非法的 class 名称，class 名称是大小写敏感的，并且必须以英文字母开头，有效的字符仅限在英文字母、数字以及下划线.");
        erro.put(104, "关联的class名称不存在");
        erro.put(105, "字段名是大小写敏感的，且必须以英文字母开头，有效的字符仅限在英文字母、数字以及下划线 或 字段名是Bmob默认保留的，如objectId,createdAt,updateAt,ACL");
        erro.put(106, "不是一个正确的指针类型");
        erro.put(107, "格式不正确");
        erro.put(108, "用户名和密码是必需的");
        erro.put(111, "传入的字段值与字段类型不匹配");
        erro.put(112, "requests的值必须是数组");
        erro.put(113, "json格式不正确");
        erro.put(114, "requests数组大于50");
        erro.put(117, "纬度范围在[-90, 90] 或 经度范围在[-180, 180]");
        erro.put(120, "请在Bmob后台应用设置中打开邮箱认证功能开关");
        erro.put(131, "不正确的deviceToken");
        erro.put(132, "不正确的installationId");
        erro.put(133, "不正确的deviceType");
        erro.put(134, "deviceToken已经存在");
        erro.put(135, "installationId已经存在");
        erro.put(136, "只读属性不能修改 或 android设备不需要设置deviceToken");
        erro.put(138, "表是只读的");
        erro.put(139, "角色名称是大小写敏感的，并且必须以英文字母开头，有效的字符仅限在英文字母、数字、空格、横线以及下划线");
        erro.put(141, "缺失推送需要的data参数");
        erro.put(142, "时间格式应该如下： 2013-12-04 00:51:13");
        erro.put(143, "必须是一个数字");
        erro.put(144, "不能是之前的时间");
        erro.put(145, "文件大小错误");
        erro.put(146, "文件名错误");
        erro.put(147, "文件分页上传偏移量错误");
        erro.put(148, "文件上下文错误");
        erro.put(149, "空文件");
        erro.put(150, "文件上传错误");
        erro.put(151, "文件删除错误");
        erro.put(160, "图片错误");
        erro.put(161, "图片模式错误");
        erro.put(162, "图片宽度错误");
        erro.put(163, "图片高度错误");
        erro.put(164, "图片长边错误");
        erro.put(165, "图片短边错误");
        erro.put(201, "缺失数据");

        erro.put(202, "用户名已经存在");
        erro.put(203, "邮箱已经存在");
        erro.put(204, "必须提供一个邮箱地址");
        erro.put(205, "不能修改或删除用户");
        erro.put(206, "没有找到此用户名的用户");
        erro.put(207, "验证码错误");
        erro.put(208, "authData不正确");
        erro.put(209, "该手机号码已经存在");
        erro.put(210, "旧密码不正确");
        erro.put(301, "验证错误详细提示，如邮箱格式不正确");
        erro.put(302, "'不允许SDK创建表 '");
        erro.put(310, "云端逻辑运行错误的详细信息");
        erro.put(311, "云端逻辑名称是大小写敏感的，且必须以英文字母开头，有效的字符仅限在英文字母、数字以及下划线");
        erro.put(401, "唯一键不能存在重复的值");
        erro.put(402, "查询的wher语句长度大于具体多少个字节");
        erro.put(601, "不正确的BQL查询语句");

        erro.put(1002, "该应用能创建的表数已达到限制");
        erro.put(1003, "该表的行数已达到限制");
        erro.put(1004, "该表的列数已达到限制");
        erro.put(1005, "每月api请求数量已达到限制");
        erro.put(1006, "该应用能创建定时任务数已达到限制");
        erro.put(1007, "该应用能创建云端逻辑数已达到限制");
        erro.put(1500, "你上传的文件大小已超出限制");

        erro.put(10010, "该手机号发送短信达到限制");
        erro.put(10011, "该账户无可用的发送短信条数");
        erro.put(10012, "身份信息必须审核通过才能使用该功能");
        erro.put(10013, "非法短信内容");
        return erro;
    }

    public String getErro(int erroCode){
        erro = init();
        if(erro.containsKey(erroCode)){
            return erro.get(erroCode);
        }else{
            return "未知错误";
        }
    }
}
