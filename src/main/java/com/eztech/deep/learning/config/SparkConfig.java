package com.eztech.deep.learning.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spark Config for Application.
 * <p>
 * Created by jia on 02/06/2017.
 */
@Configuration
public class SparkConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${master.uri}")
    private String masterUri;

    @Value("${hdfs.uri}")
    private String hdfsConfig;


    @Bean
    public SparkConf sparkConf() {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName(appName);
        sparkConf.setMaster(masterUri);
        sparkConf.set("spark.hadoop.fs.defaultFS", hdfsConfig);
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        sparkConf.set("spark.kryo.registrator", "org.nd4j.Nd4jRegistrator");
        return sparkConf;
    }


    @Bean
    public JavaSparkContext javaSparkContext() {
        return new JavaSparkContext(sparkConf());
    }

}
