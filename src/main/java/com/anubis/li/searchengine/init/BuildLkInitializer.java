package com.anubis.li.searchengine.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.dic.Dictionary;

@Component
public class BuildLkInitializer implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        //加载ik分词器配置 防止第一次查询慢
        Dictionary.initial(DefaultConfig.getInstance());
    }
}
