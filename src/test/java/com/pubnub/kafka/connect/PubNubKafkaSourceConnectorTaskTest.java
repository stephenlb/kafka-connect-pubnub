package com.pubnub.kafka.connect;

import org.apache.kafka.connect.source.SourceRecord;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static com.pubnub.kafka.connect.PubNubKafkaConnectorConfig.*;

public class PubNubKafkaSourceConnectorTaskTest {

    @Test
    public void taskVersionShouldMatch() {
        String version = PropertiesUtil.getConnectorVersion();
        assertEquals(version, new PubNubKafkaSourceConnectorTask().version());
    }

    @Test
    public void checkNumberOfRecords() {
        final String value = "sameValue";
        Map<String, String> props = new HashMap<>();
        props.put("pubnub.publish_key", value);
        props.put("pubnub.subscribe_key", value);
        props.put("pubnub.secret_key", value);
        Map<String, String> taskProps = getTaskProps(props);
        PubNubKafkaSourceConnectorTask task = new PubNubKafkaSourceConnectorTask();
    }

    private Map<String, String> getTaskProps(Map<String, String> props) {
        PubNubKafkaSourceConnector connector = new PubNubKafkaSourceConnector();
        connector.start(props);
        List<Map<String, String>> taskConfigs = connector.taskConfigs(1);
        return taskConfigs.get(0);
    }
    
}
