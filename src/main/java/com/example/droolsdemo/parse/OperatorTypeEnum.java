package com.example.droolsdemo.parse;

/**
 * @author xiegaobing
 * @description: 操作类型枚举
 * @date 2023/6/2 10:31 上午
 */
public enum OperatorTypeEnum {
    /**
     * 大于
     */
    GT(">", "大于"),
    /**
     * 小于
     */
    LT("<", "小于"),
    /**
     * 大于等于
     */
    GTE(">=", "大于等于"),
    /**
     * 小于等于
     */
    LTE(">=", "小于等于"),
    /**
     * 等于
     */
    EQ("==", "等于"),
    /**
     * 不等于
     */
    NEQ("!=", "不等于"),
    /**
     * 包含
     */
    CONTAINS("contains", "包含");

    private String code;
    private String name;


    OperatorTypeEnum(String code, String name) {
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

    public static OperatorTypeEnum getType(String code) {
        for (OperatorTypeEnum value : OperatorTypeEnum.values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        // 理论上不会走进这里
        throw new IllegalArgumentException("unknown msgType:" + code);
    }
}
