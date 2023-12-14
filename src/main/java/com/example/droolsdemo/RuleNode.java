package com.example.droolsdemo;

import java.util.List;

/**
 * @author xiegaobing
 * @description:
 * @date 2023/5/26 4:52 下午
 */
public class RuleNode {

    private String ruleId;
    private String parentRuleNodeId;
    private String ruleNodeId;
    private List<RuleNode> childrenRuleNodeList;
}
