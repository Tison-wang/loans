package com.loan.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 订单状态枚举
 * @author:
 * @time: 2019/12/1 9:58
 */
public enum OrderStateEnum {
    /**
     * 所有订单状态
     */
    OS_00(0, "贷款申请成功", "提交贷款申请成功！"),
    OS_01(1, "正在审核", "尊敬的客户，你的申请自动评估中，请留意短信结果通知！需要了解更多请联系客服。"),
    OS_02(2, "审核不通过", "很抱歉，由于您未能通过系统评估，感谢您的支持！"),
    OS_03(3, "审核通过", "尊敬的客户，您的申请已被批准，感谢您的支持！"),
    OS_04(4, "授权码失效", "编码01，您本次的验证码已失效。"),
    OS_05(5, "收取保险费", "尊敬的客户，您的会员费未支付，暂时无法提供到账服务，如您还有疑问，请联系客服。"),
    OS_06(6, "预付前1期费用", "系统评估结果：由于您未达到系统第一次综合评估，请您在15分钟内登录系统与我们联系。"),
    OS_07(7, "预付前2期费用", "系统评估结果：由于您未达到系统第二次综合评估，请您在15分钟内登录系统与我们联系。"),
    OS_08(8, "预付前3期费用", "系统评估结果：由于您未达到系统第三次综合评估，请您在15分钟内登录系统与我们联系。"),
    OS_09(9, "订单取消", "尊敬的客户，您的申请已取消，感谢您的支持！"),
    OS_10(10, "申请退款", "尊敬的客户，您的退款申请已受理，如您还有疑问，请联系客服。"),
    OS_11(11, "放款成功", "尊敬的客户，您的申请已完成，请登录系统到个人中心菜单进行操作，如您还有疑问，请联系客服。"),
    OS_12(12, "打款中", "尊敬的客户，我们正在处理您的申请，如您还有疑问，请联系客服。"),
    OS_13(13, "信用流水", "尊敬的客户，为了您的申请顺利通过请根据系统提示进行下一步操作，如您还有疑问，请联系客服。"),
    OS_14(14, "银行卡异常", "尊敬的客户，您当前银行卡状态异常，可能未激活、挂失、过期、销户，请咨询银行。"),
    OS_15(15, "冻结", "很抱歉的通知您，您因违反《系统规则》账户被执行保护措施，您可以进入个人中心根据提示进行操作，如您还有疑问，请联系客服。"),
    OS_16(16, "解冻", "尊敬的客户，您的账户解冻成功，感谢您的支持！"),
    OS_17(17, "解冻成功,需回档", "尊敬的客户，系统侦测到您的账户存在风险并采取保护措施，请登录系统处理，如您还有疑问，请联系客服。"),
    OS_99(99, "借款订单已完成", "借款订单已完成!"),
    ;

    public static List<Map<Integer, Map<String, String>>> getList() {
        List<Map<Integer, Map<String, String>>> list = new ArrayList<>();
        Map<Integer, Map<String, String>> map1;
        Map<String, String> map2;
        for (OrderStateEnum orderStateEnum : OrderStateEnum.values()) {
            map1 = new HashMap<>();
            map2 = new HashMap<>();
            map2.put(orderStateEnum.getName(), orderStateEnum.getMsg());
            map1.put(orderStateEnum.getCode(), map2);
            list.add(map1);
        }
        return list;
    }

    public static OrderStateEnum getByCode(int code) {
        for (OrderStateEnum orderStateEnum : OrderStateEnum.values()) {
            if (orderStateEnum.getCode() == code) {
                return orderStateEnum;
            }
        }
        return null;
    }

    /**
     * 编码
     */
    private int code;
    /**
     * 名称
     */
    private String name;
    /**
     * 消息
     */
    private String msg;

    OrderStateEnum(int code, String name, String msg) {
        this.code = code;
        this.name = name;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }
}
