package com.example.droolsdemo.filter;

import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

/**
 * @author xiegaobing
 * @description:
 * @date 2023/5/19 9:55 上午
 */
public class TestAgendaFilter implements AgendaFilter {

    private String startName;

    public TestAgendaFilter(String startName) {
        this.startName = startName;
    }

    @Override
    public boolean accept(Match match) {
        String ruleName = match.getRule().getName();
        // 字符串开头是否包含
        if (ruleName.startsWith(this.startName)) {
            return true;
        }
        return false;
    }
}
