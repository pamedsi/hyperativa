package com.hyperativa.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}")
    private String password;

    @Value("${jasypt.encryptor.algorithm:PBEWithMD5AndDES}")
    private String algorithm;

    @Value("${jasypt.encryptor.key-obtention-iterations:1000}")
    private String iterations;

    @Value("${jasypt.encryptor.pool-size:1}")
    private String poolSize;

    @Value("${jasypt.encryptor.iv-generator-classname:org.jasypt.iv.NoIvGenerator}")
    private String ivGenerator;

    @Bean
    @Primary
    public StringEncryptor jasyptStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword(password);
        config.setAlgorithm(algorithm);
        config.setKeyObtentionIterations(iterations);
        config.setPoolSize(poolSize);
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName(ivGenerator);
        config.setStringOutputType("base64");

        encryptor.setConfig(config);
        return encryptor;
    }
}