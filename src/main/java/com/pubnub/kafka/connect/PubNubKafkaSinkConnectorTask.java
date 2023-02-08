package com.pubnub.kafka.connect;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;

import static com.pubnub.kafka.connect.PubNubKafkaConnectorConfig.*;

public class PubNubKafkaSinkConnectorTask extends SinkTask {

    private static final String STRING_COLUMN = "string-column";
    private static final String NUMERIC_COLUMN = "numeric-column";
    private static final String BOOLEAN_COLUMN = "boolean-column";

    private final Random random = new Random(System.currentTimeMillis());
    private final Logger log = LoggerFactory.getLogger(PubNubKafkaSinkConnectorTask.class);

    private PubNubKafkaConnectorConfig config;
    private int taskSleepTimeout;
    private List<String> sources;

    @Override
    public String version() {
        return PropertiesUtil.getConnectorVersion();
    }

    @Override
    public void start(Map<String, String> properties) {
        config = new PubNubKafkaConnectorConfig(properties);
        taskSleepTimeout = config.getInt(TASK_SLEEP_TIMEOUT_CONFIG);
        String sourcesStr = properties.get("sources");
        sources = Arrays.asList(sourcesStr.split(","));
    }

    @Override
    public void put(Collection<SinkRecord> records) {
        for (final SinkRecord record : records) {
            //pubnub.publishFrom(record);
            log.error("PUBLISHING TO PUBNUB");
        }
    }

    @Override
    public void flush(Map<TopicPartition, OffsetAndMetadata> map) {
        return;
    }

    @Override
    public void stop() {
        log.error("Stopping PubNub Sink Connector Task");
    }

}
