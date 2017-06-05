package com.eztech.deep.learning;

import com.beust.jcommander.Parameter;
import com.eztech.deep.learning.kafka.consumer.Receiver;
import com.eztech.deep.learning.kafka.producer.Sender;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.spark.api.RDDTrainingApproach;
import org.deeplearning4j.spark.api.TrainingMaster;
import org.deeplearning4j.spark.impl.multilayer.SparkDl4jMultiLayer;
import org.deeplearning4j.spark.impl.paramavg.ParameterAveragingTrainingMaster;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Big data Application
 *
 * @author Jia ZHOU
 */
@SpringBootApplication
@Slf4j
public class Application implements CommandLineRunner {


    @Autowired
    private Sender sender;

    @Autowired
    private Receiver receiver;

    @Autowired
    private JavaSparkContext javaSparkContext;

    @Value("${topic.receiver}")
    private String topic;

    @Parameter(names = "-batchSizePerWorker", description = "Number of examples to fit each worker with")
    private int batchSizePerWorker = 128;

    @Parameter(names = "-numEpochs", description = "Number of epochs for training")
    private int numEpochs = 15;

    /** @param args  */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        //Send message to detect content.
        //Detection detection = new Detection();
        //detection.setText("test");
        //detection.setType(DetectionType.TEXT);
        //sender.send(topic, detection);


        DataSetIterator iterTrain = new MnistDataSetIterator(batchSizePerWorker, true, 12345);
        DataSetIterator iterTest = new MnistDataSetIterator(batchSizePerWorker, true, 12345);
        List<DataSet> trainDataList = new ArrayList<>();
        List<DataSet> testDataList = new ArrayList<>();
        while (iterTrain.hasNext()) {
            trainDataList.add(iterTrain.next());
        }
        while (iterTest.hasNext()) {
            testDataList.add(iterTest.next());
        }

        JavaRDD<DataSet> trainData = javaSparkContext.parallelize(trainDataList);
        JavaRDD<DataSet> testData = javaSparkContext.parallelize(testDataList);


        //----------------------------------
        //Create network configuration and conduct network training
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(12345)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).iterations(1)
                .activation(Activation.LEAKYRELU)
                .weightInit(WeightInit.XAVIER)
                .learningRate(0.02)
                .updater(Updater.NESTEROVS).momentum(0.9)
                .regularization(true).l2(1e-4)
                .list()
                .layer(0, new DenseLayer.Builder().nIn(28 * 28).nOut(500).build())
                .layer(1, new DenseLayer.Builder().nIn(500).nOut(100).build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX).nIn(100).nOut(10).build())
                .pretrain(false).backprop(true)
                .build();

        //Configuration for Spark training: see http://deeplearning4j.org/spark for explanation of these configuration options
        TrainingMaster tm = new ParameterAveragingTrainingMaster.Builder(batchSizePerWorker)    //Each DataSet object: contains (by default) 32 examples
                .averagingFrequency(5).workerPrefetchNumBatches(2).rddTrainingApproach(RDDTrainingApproach.Direct)      //Async prefetching: 2 examples per worker
                .batchSizePerWorker(batchSizePerWorker).build();

        //Create the Spark network
        SparkDl4jMultiLayer sparkNet = new SparkDl4jMultiLayer(javaSparkContext, conf, tm);

        //Execute training:
        for (int i = 0; i < numEpochs; i++) {
            sparkNet.fit(trainData);
            log.info("Completed Epoch {}", i);
        }

        //Perform evaluation (distributed)
        Evaluation evaluation = sparkNet.evaluate(testData);
        log.info("***** Evaluation *****");
        log.info(evaluation.stats());

        //Delete the temp training files, now that we are done with them
        tm.deleteTempFiles(javaSparkContext);

        log.info("***** Example Complete *****");
    }
}





