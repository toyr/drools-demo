package com.example.droolsdemo.parse;

import cn.hutool.json.JSONUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.droolsdemo.parse.FeatureTypeEnum.*;
import static com.example.droolsdemo.parse.LogicTypeEnum.AND;
import static com.example.droolsdemo.parse.LogicTypeEnum.OR;
import static com.example.droolsdemo.parse.OperatorTypeEnum.*;

/**
 * @author xiegaobing
 * @description:
 * @date 2023/6/2 3:23 下午
 */
public class RuleTest {

    private static final String packageName = "com.example.droolsdemo.parse.";


    @Test
    public void test_RuleBean_rule1() {
        RuleNode ageGT70 = RuleNode.builder().feature("age").type(INT).operator(GT).value("70").build();
        System.out.println(JSONUtil.toJsonStr(ageGT70));
        CodeParseUtils.createBeanOrUpdateMethod(ageGT70, packageName + "RuleBean", "rule1");
    }

    @Test
    public void test_RuleBean_rule2() {
        RuleNode age70 = RuleNode.builder().feature("age").type(INT).operator(GT).value("70").build();
        RuleNode age30 = RuleNode.builder().feature("age").type(INT).operator(LT).value("30").build();
        RuleNode age70or30 = RuleNode.builder().logic(OR).childNodes(Arrays.asList(age70, age30)).build();
        System.out.println(JSONUtil.toJsonStr(age70or30));
        CodeParseUtils.createBeanOrUpdateMethod(age70or30, packageName + "RuleBean", "rule2");
    }

    @Test
    public void test_RuleBean_rule3() {
        RuleNode age70 = RuleNode.builder().feature("age").type(INT).operator(LT).value("70").build();
        RuleNode age30 = RuleNode.builder().feature("age").type(INT).operator(GT).value("30").build();
        RuleNode age70or30 = RuleNode.builder().logic(AND).childNodes(Arrays.asList(age70, age30)).build();
        RuleNode occupationDoctor = RuleNode.builder().feature("occupation").type(STRING).operator(EQ).value("doctor").build();
        RuleNode rule = RuleNode.builder().logic(AND).childNodes(Arrays.asList(age70or30, occupationDoctor)).build();
        System.out.println(JSONUtil.toJsonStr(rule));
        CodeParseUtils.createBeanOrUpdateMethod(rule, packageName + "RuleBean", "rule3");
    }

    @Test
    public void test_createBean_rule123() {
        RuleNode age70 = RuleNode.builder().feature("age").type(INT).operator(GT).value("70").build();
        RuleNode age30 = RuleNode.builder().feature("age").type(INT).operator(LT).value("30").build();
        RuleNode age70or30 = RuleNode.builder().logic(OR).childNodes(Arrays.asList(age70, age30)).build();
        RuleNode occupationDoctor = RuleNode.builder().feature("occupation").type(STRING).operator(EQ).value("doctor").build();
        RuleNode sexFemale = RuleNode.builder().feature("sex").type(STRING).operator(EQ).value("female").build();
        RuleNode age70or30AndOccupationDoctor = RuleNode.builder().logic(AND).childNodes(Arrays.asList(age70or30, occupationDoctor, sexFemale)).build();

        RuleNode age50 = RuleNode.builder().feature("age").type(INT).operator(GT).value("50").build();
        RuleNode age18 = RuleNode.builder().feature("age").type(INT).operator(LT).value("18").build();
        RuleNode age50or18 = RuleNode.builder().logic(OR).childNodes(Arrays.asList(age50, age18)).build();
        RuleNode occupationProgrammer = RuleNode.builder().feature("occupation").type(STRING).operator(CONTAINS).value(",programmer,doctor,").build();
        RuleNode amount100000 = RuleNode.builder().feature("amount").type(FLOAT).operator(GT).value("100000.00").build();
        RuleNode birthday = RuleNode.builder().feature("birthday").type(DATE).value("1990-01-01 00:00:00/2000-01-01 00:00:00").build();
        RuleNode age50or18AndOccupationProgrammer = RuleNode.builder().logic(AND)
                .childNodes(Arrays.asList(age50or18, occupationProgrammer, amount100000, birthday)).build();

        RuleNode rule = RuleNode.builder().logic(OR).childNodes(Arrays.asList(age50or18AndOccupationProgrammer, age70or30AndOccupationDoctor)).build();
        System.out.println(JSONUtil.toJsonStr(rule));

        CodeParseUtils.createBeanOrUpdateMethod(rule, packageName + "RuleBean", "rule123");
    }

    @Test
    public void test_apply() {
        Map<String, Object> params = new HashMap<>();
        params.put("age", "51");
        params.put("occupation", "doctor");
        params.put("amount", "100000.01");
        params.put("birthday", "1993-01-01 00:00:00");
        RuleUtils.matchAllRules(packageName + "RuleBean", params);
        RuleUtils.matchWithRule(packageName + "RuleBean", "rule1", params);
        RuleUtils.matchWithRule(packageName + "RuleBean", "rule1sfsdas", params);
    }
}
