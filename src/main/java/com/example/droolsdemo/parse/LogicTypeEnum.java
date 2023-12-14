package com.example.droolsdemo.parse;

/**
 * @author xiegaobing
 * @description: 逻辑类型枚举
 * @date 2023/6/2 10:31 上午
 */
public enum LogicTypeEnum {

    /**
     * 与
     */
    AND("&&", "与"),
    /**
     * 或
     */
    OR("||", "或");

    private String code;
    private String name;


    LogicTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static LogicTypeEnum getType(String code) {
        for (LogicTypeEnum value : LogicTypeEnum.values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        // 理论上不会走进这里
        throw new IllegalArgumentException("unknown msgType:" + code);
    }
}
