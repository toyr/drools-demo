package com.example.droolsdemo.servcie;

import com.example.droolsdemo.entity.QueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xiegaobing
 * @description: 添加规则实现
 * @date 2023/5/22 2:23 下午
 */
@Slf4j
@Service
public class RuleEngineService {

    /**
     * 插入规则
     *
     * @param queryParam
     */
    public void executeAddRule(QueryParam queryParam) {
        log.info("参数数据:" + queryParam.getParamId() + ";" + queryParam.getParamSign());
        log.info("插入规则");
    }

    /**
     * 移除规则
     *
     * @param param
     */
    public void executeRemoveRule(QueryParam param) {
        log.info("参数数据:" + param.getParamId() + ";" + param.getParamSign());
        log.info("移除规则");
    }

}
