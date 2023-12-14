package com.example.droolsdemo;

import com.example.droolsdemo.entity.Order;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xiegaobing
 * @description:
 * @date 2023/5/19 10:10 上午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DroolsDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DroolsJunitTest {

    @Autowired
    KieSession kieSession;

    @Test
    public void test1() {
        kieSession.fireAllRules();
    }

    //执行指定名称的规则
    @Test
    public void test2() {

        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("rules1"));
    }
    @Test
    public void test3() {
        Order order=new Order();
        order.setPrice(100);
        kieSession.insert(order); //多个参数那么就使用多次insert
        kieSession.fireAllRules();
    }

}
