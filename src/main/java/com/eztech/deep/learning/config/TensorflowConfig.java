package com.eztech.deep.learning.config;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.tensorflow.Graph;

@Slf4j
@Configuration
public class TensorflowConfig {

    @Bean
    public Graph tfModelGraph(@Value("${tf.frozenModelPath}") String tfFrozenModelPath) throws IOException {
        Resource graphResource = getResource(tfFrozenModelPath);

        try (Graph graph = new Graph()) {
            graph.importGraphDef(IOUtils.toByteArray(graphResource.getInputStream()));
            log.info("Loaded Tensorflow model");
            return graph;
        }
    }

    private Resource getResource(@Value("${tf.frozenModelPath}") String tfFrozenModelPath) {
        Resource graphResource = new FileSystemResource(tfFrozenModelPath);
        if (!graphResource.exists()) {
            graphResource = new ClassPathResource(tfFrozenModelPath);
        }
        if (!graphResource.exists()) {
            throw new IllegalArgumentException(String.format("File %s does not exist", tfFrozenModelPath));
        }
        return graphResource;
    }

    @Bean
    public List<String> tfModelLabels(@Value("${tf.labelsPath}") String labelsPath) throws IOException {
        Resource labelsRes = getResource(labelsPath);
        log.info("Loaded model labels");
        return IOUtils.readLines(labelsRes.getInputStream(), Charset.forName("UTF-8"))
                .stream().map(label -> label.substring(label.contains(":") ? label.indexOf(":") + 1 : 0))
                .collect(Collectors.toList());
    }
}
