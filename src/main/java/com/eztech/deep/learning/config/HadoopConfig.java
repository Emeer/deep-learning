package com.eztech.deep.learning.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.security.UserGroupInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.hadoop.config.annotation.EnableHadoop;
import org.springframework.data.hadoop.config.annotation.SpringHadoopConfigurerAdapter;
import org.springframework.data.hadoop.config.annotation.builders.HadoopConfigConfigurer;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

import java.io.IOException;
import java.security.PrivilegedAction;

/**
 * Hadoop Configuration.
 */
@org.springframework.context.annotation.Configuration
@EnableHadoop
public class HadoopConfig extends SpringHadoopConfigurerAdapter {

    /** The hadoop config.s */
    @Autowired
    private Configuration configuration;


    /**
     * @param config
     * {@inheritDoc}
     */
    @Override
    public void configure(HadoopConfigConfigurer config) throws Exception {
        config.fileSystemUri("hdfs://localhost:9000");
    }

    /**
     * Build FsShell.
     *
     * @return The FsShell.
     */
    @Bean
    public FsShell buildShell() {
        UserGroupInformation.setConfiguration(configuration);
        UserGroupInformation ugi = UserGroupInformation.createRemoteUser("jia");
        return ugi.doAs((PrivilegedAction<FsShell>) () -> new FsShell(configuration));
    }

    /**
     * Build FileSystem.
     *
     * @return The File System.
     */
    @Bean
    public DistributedFileSystem buildFileSystem() {
        UserGroupInformation.setConfiguration(configuration);
        UserGroupInformation ugi = UserGroupInformation.createRemoteUser("jia");
        return ugi.doAs((PrivilegedAction<DistributedFileSystem>) () -> {
            try {
                return (DistributedFileSystem) FileSystem.get(configuration);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * Build hbase template.
     *
     * @return The hbase template.
     */
    @Bean
    public HbaseTemplate buildHbaseTemplate() {
        Configuration hbConfig = HBaseConfiguration.create(configuration);
        hbConfig.set("zk-quorum", "localhost");
        hbConfig.set("zk-port", "2181");
        return new HbaseTemplate(hbConfig);
    }

}






