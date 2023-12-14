package com.example.droolsdemo.parse;

/**
 * @author xiegaobing
 * @description: 特征值支持的类型枚举
 * @date 2023/6/2 10:31 上午
 */
public enum FeatureTypeEnum {

    /**
     * 字符型
     */
    STRING("string", "字符串"),
    /**
     * 整形
     */
    INT("int", "整形"),
    /**
     * 浮点型
     */
    FLOAT("float", "浮点型"),
    /**
     * 日期
     */
    DATE("date", "日期");

    private String code;
    private String name;


    FeatureTypeEnum(String code, String name) {
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

    public static FeatureTypeEnum getType(String code) {
        for (FeatureTypeEnum value : FeatureTypeEnum.values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        // 理论上不会走进这里
        throw new IllegalArgumentException("unknown msgType:" + code);
    }
}
