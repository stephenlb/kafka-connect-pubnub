package com.pubnub.kafka.connect;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.pubnub.api.UserId;
import com.pubnub.api.PubNub;
import com.pubnub.api.PNConfiguration;

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

    private final Logger log = LoggerFactory.getLogger(PubNubKafkaSinkConnectorTask.class);

    private PubNub pubnub;
    private PubNubKafkaConnectorConfig config;
    private List<String> sources;

    @Override
    public String version() {
        return PropertiesUtil.getConnectorVersion();
    }

    @Override
    public void start(Map<String, String> properties) {
        config = new PubNubKafkaConnectorConfig(properties);
        try {
            final UserId userId = new UserId("myUniqueUserId");
            String publishKey = properties.get("pubnub.publish_key");
            String subscribeKey = properties.get("pubnub.subscribe_key");
            String secretKey = properties.get("pubnub.secret_key");

            PNConfiguration pnConfiguration = new PNConfiguration(userId);
            pnConfiguration.setPublishKey(publishKey);
            pnConfiguration.setSubscribeKey(subscribeKey);
            pnConfiguration.setSecretKey(secretKey);
            pubnub = new PubNub(pnConfiguration);

            String sourcesStr = properties.get("sources"); // "channels"
            sources = Arrays.asList(sourcesStr.split(","));
        }
        catch(Exception error) {
            log.error("Unable to initialize PubNub Connection", error);
        }
    }

    private void publish(SinkRecord record) {
        pubnub.publish()
            .channel(record.topic())
            .message(record.value())
            .async((result, publishStatus) -> {
                if (publishStatus.isError()) {
                    log.error("⛔️ Channel: '{}' Message: '{}' Published to PubNub Failed!", record.topic());
                }
                else {
                    log.info("✅ Channel: '{}' Message: '{}' Published to PubNub Successfully!", record.topic(), record.value());
                }
            });
    }

    @Override
    public void put(Collection<SinkRecord> records) {
        for (final SinkRecord record : records) {
            publish(record);
        }
    }

    @Override
    public void flush(Map<TopicPartition, OffsetAndMetadata> map) {
        return;
    }

    @Override
    public void stop() {
        log.info("Stopping PubNub Sink Connector Task");
    }

}
