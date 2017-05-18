package com.eztech.deep.learning.config;

import java.io.IOException;
import java.security.PrivilegedAction;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.security.UserGroupInformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.hadoop.config.annotation.EnableHadoop;
import org.springframework.data.hadoop.config.annotation.SpringHadoopConfigurerAdapter;
import org.springframework.data.hadoop.config.annotation.builders.HadoopConfigConfigurer;
import org.springframework.data.hadoop.fs.FsShell;

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
     * @param config {@inheritDoc}
     */
    @Override
    public void configure(HadoopConfigConfigurer config) throws Exception {
        config.fileSystemUri("hdfs://localhost:32781");
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
                //TODO add logger
            }
            return null;
        });
    }

}






