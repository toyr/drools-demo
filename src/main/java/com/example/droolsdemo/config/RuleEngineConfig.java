package com.example.droolsdemo.config;

import com.example.droolsdemo.utils.KieUtils;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.spring.KModuleBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 * @author xiegaobing
 * @description:
 * @date 2023/5/22 1:56 下午
 */

@Slf4j
@Configuration
public class RuleEngineConfig {

    public static final String RULES_PATH = "rules/";
    public static final String BASE_RULES_PATH = "classpath*:";

    private final KieServices kieServices = KieServices.Factory.get();

    /**
     * @return
     * @throws IOException
     * @ConditionalOnMissingBean，它是修饰bean的一个注解， 主要实现的是，当你的bean被注册之后，如果而注册相同类型的bean，就不会成功，
     * 它会保证你的bean只有一个，即你的实例只有一个，当你注册多个相同的bean时，会出现异常
     */
    @Bean
    @ConditionalOnMissingBean(KieFileSystem.class)
    public KieFileSystem kieFileSystem() throws IOException {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        //获取初始化规则文件所在路径
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] files = resourcePatternResolver.getResources(BASE_RULES_PATH + RULES_PATH + "*.*");
        String path = null;
        for (int i = 0; i < 1000; i++) {
            for (Resource file : files) {
                path = RULES_PATH + file.getFilename();
                log.info("path=" + path);
                //将规则文件写规则引擎系统内
//                kieFileSystem.write(ResourceFactory.newClassPathResource(path, "UTF-8"));
                rule1.replace("unknow", "rule" + file.getFilename() + i);
                kieFileSystem.write(path + i, rule1);
            }
        }
        return kieFileSystem;
    }

    /**
     * 创建KIE内部容器
     *
     * @return
     * @throws IOException
     */
    @Bean
    @ConditionalOnMissingBean(KieContainer.class)
    public KieContainer kieContainer() throws IOException {

        final KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(kieRepository::getDefaultReleaseId);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem());
        kieBuilder.buildAll();
        KieContainer kieContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
        KieUtils.setKieContainer(kieContainer);
        return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
    }

    @Bean
    @ConditionalOnMissingBean(KieBase.class)
    public KieBase kieBase() throws IOException {
        return kieContainer().getKieBase();
    }

    @Bean
    @ConditionalOnMissingBean(KieSession.class)
    public KieSession kieSession() throws IOException {
        KieSession kieSession = kieContainer().newKieSession();
        KieUtils.setKieSession(kieSession);
        return kieSession;
    }

    @Bean
    @ConditionalOnMissingBean(KModuleBeanFactoryPostProcessor.class)
    public KModuleBeanFactoryPostProcessor kModuleBeanFactoryPostProcessor() {
        return new KModuleBeanFactoryPostProcessor();
    }

    String rule1 = "import com.example.droolsdemo.entity.QueryParam\n" +
            "\n" +
            "dialect  \"java\"\n" +
            "\n" +
            "rule \"unknow\"\n" +
            "    when queryParam : QueryParam(paramId != null && paramSign.equals(\"unknow\"))\n" +
            "    then\n" +
            "        System.out.println(queryParam.getParamId() +  \"是unknow\");\n" +
            "        queryParam.setResult(\"{'code': 123, msy: '123'}\");\n" +
            "end\n";
}