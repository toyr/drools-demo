package com.example.droolsdemo.parse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xiegaobing
 * @description:
 * @date 2023/5/31 7:24 下午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleNode {

    /**
     * 逻辑类型
     *
     * @see LogicTypeEnum
     */
    private LogicTypeEnum logic;
    /**
     * 特征
     */
    private String feature;
    /**
     * 特征类型，
     *
     * @see FeatureTypeEnum
     */
    private FeatureTypeEnum type;
    /**
     * 操作类型
     *
     * @see OperatorTypeEnum
     */
    private OperatorTypeEnum operator;
    /**
     * 取值
     */
    private String value;
    /**
     * 子节点
     */
    private List<RuleNode> childNodes;
}
