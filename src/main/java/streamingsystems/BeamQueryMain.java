package streamingsystems;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.io.kafka.KafkaRecord;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Mean;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Test the main functionality
 */
public class BeamQueryMain {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(BeamQueryMain.class);
        logger.info("Starting...");

        PipelineOptions options = PipelineOptionsFactory.create();
        //        options.setRunner(FlinkRunner.class);
        Pipeline pipeline = Pipeline.create(options);


        PCollection<KafkaRecord<Integer, String>> kafkaRecords = pipeline.apply(KafkaIO
                                                                                        .<Integer, String>read()
                                                                                        .withBootstrapServers(
                                                                                                ConfigManager.INSTANCE.getKafkaUrl())
                                                                                        .withTopic(
                                                                                                ConfigManager.INSTANCE.getKafkaTopicName())
                                                                                        .withKeyDeserializer(
                                                                                                IntegerDeserializer.class)
                                                                                        .withValueDeserializer(
                                                                                                StringDeserializer.class)
                                                                                        .withStartReadTime(LocalDate
                                                                                                                   .parse("1990-01-01")
                                                                                                                   .toDateTimeAtCurrentTime()
                                                                                                                   .toInstant()));
        // pardo into a new PCollection as arrays with keys
        PCollection<KV<Integer, Double>> parsedRecords = kafkaRecords.apply(
                ParDo.of(new DoFn<KafkaRecord<Integer, String>, KV<Integer, Double>>() {
                    @ProcessElement
                    public void processElement(@Element KafkaRecord<Integer, String> inputRecord,
                                               OutputReceiver<KV<Integer, Double>> outputRecord) {
                        String[] splitSensorValueStrings = inputRecord.getKV().getValue().split(",");
                        Arrays.stream(splitSensorValueStrings).forEach(sensorValueString -> {
                            double splitSensorValue = Double.parseDouble(sensorValueString);
                            if (splitSensorValue > 0) {
                                outputRecord.output(KV.of(inputRecord.getKV().getKey(), splitSensorValue));
                            }
                        });
                    }
                }));

        // Window the last 30 seconds
        PCollection<KV<Integer, Double>> windowedSpeedInLast30Seconds = parsedRecords
                .apply(Window.into(FixedWindows.of(Duration.standardSeconds(30))))
                .apply(Mean.perKey());

        // Print the collection
        windowedSpeedInLast30Seconds.apply(ParDo.of(new DoFn<KV<Integer, Double>, Void>() {
            @ProcessElement
            public void processElement(@Element KV<Integer, Double> inputRecord) {
                System.out.println("Key: " + inputRecord.getKey() + " Value: " + inputRecord.getValue());
            }
        }));

        // Start the execution
        pipeline.run().waitUntilFinish();

        logger.info("Terminating...");
    }
}