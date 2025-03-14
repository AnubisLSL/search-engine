package com.anubis.li.searchengine.config;


import lombok.Getter;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component("config")
public class Config {

    @Value("${lucene.indexLibrary}")
    private String indexLibrary;

}
