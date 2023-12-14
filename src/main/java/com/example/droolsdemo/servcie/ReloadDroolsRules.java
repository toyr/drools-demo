package com.example.droolsdemo.servcie;

import com.example.droolsdemo.config.RuleEngineConfig;
import com.example.droolsdemo.utils.KieUtils;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.util.StringUtils;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author xiegaobing
 * @description: 重载实现，可通过链接数据库刷新规则
 * @date 2023/5/22 2:10 下午
 */
@Slf4j
@Service
public class ReloadDroolsRules {

    @Autowired
    private KieSession kieSession;

    @Autowired
    private KieContainer kieContainer;

    /**
     * 重新加载规则
     *
     * @param drlName 规则名称
     * @throws Exception
     */
    public void reload(String drlName) throws Exception {

        KieFileSystem kfs = KieUtils.getKieServices().newKieFileSystem();
//        loadDBRules(drlName, kfs);
        loadFileRules(drlName, kfs);
        KieBuilder kieBuilder = KieUtils.getKieServices().newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new IllegalStateException("### errors ###");
        }
        KieContainer kieContainer = KieUtils.getKieServices().newKieContainer(KieUtils.getKieServices().getRepository().getDefaultReleaseId());
        KieUtils.setKieContainer(kieContainer);
        System.out.println("新规则重载成功");
    }

    /**
     * 重新读取数据库配置内容
     *
     * @param drlName
     * @param kfs
     * @throws IOException
     */
    private void loadDBRules(String drlName, KieFileSystem kfs) throws IOException {
        //        String path = "src/main/resources/rules/address.drl";
        String path = "src/main/resources/" + RuleEngineConfig.RULES_PATH + "/" + drlName + ".drl";
        // 从数据库加载的规则
        kfs.write(path, "package plausibcheck.adress\n\n import com.leopard.drools.pojo.QueryParam;\n\n rule \"Postcode 6 numbers\"\n\n    when\n  then\n        System.out.println(\"打印日志：更新rules成功!\");\n end");
    }

    /**
     * 重新配置文件
     *
     * @param drlName
     * @param kfs
     * @throws IOException
     */
    private void loadFileRules(String drlName, KieFileSystem kfs) throws IOException {
        // 从classess/rules加载的规则
        //获取初始化规则文件所在路径
        String path = null;
        for (Resource file : getRuleFiles(drlName)) {
            path = RuleEngineConfig.RULES_PATH + file.getFilename();
            log.info("path=" + path);
            //将规则文件写规则引擎系统内
            kfs.write(ResourceFactory.newClassPathResource(path, "UTF-8"));
        }
    }

    private Resource[] getRuleFiles(String drlName) throws IOException {
        if (StringUtils.isEmpty(drlName)) {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            return resourcePatternResolver.getResources(RuleEngineConfig.BASE_RULES_PATH + RuleEngineConfig.RULES_PATH + "**/*.*");
        }
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        return resourcePatternResolver.getResources(RuleEngineConfig.BASE_RULES_PATH + RuleEngineConfig.RULES_PATH + "**/" + drlName + ".*");
    }
}