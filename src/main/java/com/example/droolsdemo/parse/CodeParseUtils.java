package com.example.droolsdemo.parse;

import cn.hutool.core.collection.CollectionUtil;
import javassist.CtClass;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.droolsdemo.parse.FeatureTypeEnum.INT;
import static com.example.droolsdemo.parse.FeatureTypeEnum.STRING;
import static com.example.droolsdemo.parse.LogicTypeEnum.AND;

/**
 * @author xiegaobing
 * @description: 代码生成
 * @date 2023/5/31 7:29 下午
 */
public class CodeParseUtils {

    private static final String PACKAGE_NAME = "com.example.droolsdemo.parse.";

    /**
     * 根据规则生成或更新Bean
     *
     * @param rule
     * @param beanName
     * @param methodName
     */
    public static void createBeanOrUpdateMethod(RuleNode rule, String beanName, String methodName) {
        Set<String> feature = new HashSet<>();
        if (CollectionUtil.isEmpty(rule.getChildNodes())) {
            rule = RuleNode.builder().logic(AND).childNodes(Arrays.asList(rule)).build();
        }
        String parse = parse("", rule, 0, feature);
        System.out.println(methodName + "需要的特征值包括:" + feature);
        createBeanOrUpdateMethod(parse, beanName, methodName);
    }

    /**
     * 生成class文件
     *
     * @param parse
     * @param beanName
     * @param methodName
     */
    private static void createBeanOrUpdateMethod(String parse, String beanName, String methodName) {
        StringBuilder sb = new StringBuilder();
        sb.append("public ").append("boolean ").append(methodName).append("(java.util.Map params) {\n")
                .append("    boolean result = ").append(parse).append(";\n")
                .append("    return result;\n")
                .append("}");
        System.out.println(sb.toString());
        CtClass beanOrUpdateMethod = CtClassUtils.createBeanOrUpdateMethod(beanName, sb.toString());
        CtClassUtils.writeFile(beanOrUpdateMethod);
    }


    /**
     * 解析为代码逻辑
     *
     * @param s
     * @param rule
     * @param layer
     * @param feature
     * @return
     */
    private static String parse(String s, RuleNode rule, int layer, Set<String> feature) {
        List<RuleNode> childNodes = rule.getChildNodes();
        layer++;
        String placeholder = "#" + layer + "#";
        String placeholderPreLayer = "#" + (layer - 1) + "#";
        if (CollectionUtil.isNotEmpty(childNodes)) {
            if (StringUtils.isNotBlank(rule.getLogic().getCode())) {
                int size = childNodes.size();
                StringBuilder sb = new StringBuilder();
                sb.append(" (").append(placeholder).append(") ");
                for (int i = 1; i < size; i++) {
                    sb.append(rule.getLogic().getCode()).append(" (").append(placeholder).append(") ");
                }
                if (StringUtils.isBlank(s)) {
                    s = sb.toString();
                } else {
                    s = s.replaceFirst(placeholderPreLayer, sb.toString());
                }
            }
            for (RuleNode childNode : childNodes) {
                s = parse(s, childNode, layer, feature);
            }
        } else {
            // 收集一下规则里包括的特征值
            feature.add(rule.getFeature());
            s = s.replaceFirst(placeholderPreLayer, getStringByType(rule));
        }
        return s;
    }

    /**
     * 根据Rule生成代码块
     *
     * @param rule
     * @return
     */
    private static String getStringByType(RuleNode rule) {
        String s = "";
        switch (rule.getType()) {
            case INT:
                s = getStringByInt(rule);
                break;
            case STRING:
                s = getStringByString(rule);
                break;
            case FLOAT:
                s = getStringByLong(rule);
                break;
            case DATE:
                s = getStringByDate(rule);
                break;
            default:
                break;
        }
        return s;
    }

    /**
     * 解析string
     *
     * @param rule 目前支持的操作类型 ==, contains(value需要用)
     * @return
     */
    private static String getStringByString(RuleNode rule) {
        StringBuilder sb = new StringBuilder();
        switch (rule.getOperator()) {
            case NEQ:
                sb.append("!")
                        .append(mapGetValueStr(STRING, rule.getFeature(), ""))
                        .append(".equals(\\\"")
                        .append(rule.getValue())
                        .append("\\\")");
                break;
            case EQ:
                sb.append(mapGetValueStr(STRING, rule.getFeature(), ""))
                        .append(".equals(\\\"")
                        .append(rule.getValue())
                        .append("\\\")");
                break;
            case CONTAINS:
                sb.append("\\\"").append(rule.getValue())
                        .append("\\\".contains(").append("\\\",\\\" + ")
                        .append(mapGetValueStr(STRING, rule.getFeature(), ""))
                        .append(" + \\\",\\\"").append(")");
                break;
            default:
                break;
        }
        return sb.toString();
    }


    /**
     * 解析int
     *
     * @param rule 目前支持的操作类型 > , >=, <, <=, ==
     * @return
     */
    private static String getStringByInt(RuleNode rule) {
        StringBuilder sb = new StringBuilder();
        sb.append(mapGetValueStr(INT, rule.getFeature(), 0))
                .append(rule.getOperator().getCode())
                .append(rule.getValue());
        return sb.toString();
    }

    /**
     * 解析long
     *
     * @param rule 目前支持的操作类型 > , >=, <, <=, ==
     * @return
     */
    private static String getStringByLong(RuleNode rule) {
        StringBuilder sb = new StringBuilder();
        sb.append("new java.math.BigDecimal(")
                .append(mapGetValueStr(STRING, rule.getFeature(), "0"))
                .append(").compareTo(new java.math.BigDecimal(\\\"").append(rule.getValue()).append("\\\"))")
                .append(rule.getOperator().getCode())
                .append("0");
        return sb.toString();
    }

    /**
     * 解析日期
     *
     * @param rule 开始日期（yyyy-MM-dd hh:mm:ss）/结束日期（yyyy-MM-dd hh:mm:ss）
     * @return
     */
    private static String getStringByDate(RuleNode rule) {
        String[] split = rule.getValue().split("/");
        String begin = split[0];
        String end = split[1];
        StringBuilder sb = new StringBuilder();
        sb.append(PACKAGE_NAME + "JavassistDateUtils.compare(")
                .append(mapGetValueStr(STRING, rule.getFeature(), "1900-01-01 00:00:00"))
                .append(" , \\\"").append(begin).append("\\\", \\\"").append(end).append("\\\")");
        return sb.toString();
    }

    /**
     * 恒为真的情况
     *
     * @return
     */
    private static String getStringByTrue() {
        StringBuilder sb = new StringBuilder();
        sb.append("(1==1)");
        return sb.toString();
    }


    /**
     * 拼接Map获取值代码串
     *
     * @param type
     * @param key
     * @param defaultVale
     * @return
     */
    private static String mapGetValueStr(FeatureTypeEnum type, String key, Object defaultVale) {
        StringBuilder sb = new StringBuilder();
        sb.append("org.apache.commons.collections4.MapUtils");
        switch (type) {
            case STRING:
                sb.append(".getString(\\$1, \\\"").append(key).append("\\\", \\\"").append(defaultVale).append("\\\")");
                break;
            case INT:
                sb.append(".getIntValue(\\$1, \\\"").append(key).append("\\\", ").append(defaultVale).append(")");
            default:
                break;
        }
        return sb.toString();
    }

}
