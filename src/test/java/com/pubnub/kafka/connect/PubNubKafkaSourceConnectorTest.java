package com.pubnub.kafka.connect;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static com.pubnub.kafka.connect.PubNubKafkaConnectorConfig.*;

public class PubNubKafkaSourceConnectorTest {

    @Test
    public void connectorVersionShouldMatch() {
        String version = PropertiesUtil.getConnectorVersion();
        assertEquals(version, new PubNubKafkaSourceConnector().version());
    }

    @Test
    public void checkClassTask() {
        Class<? extends Task> taskClass = new PubNubKafkaSourceConnector().taskClass();
        assertEquals(PubNubKafkaSourceConnectorTask.class, taskClass);
    }

    @Test
    public void checkSpecialCircumstance() {
        final String value = "sameValue";
        assertThrows(ConnectException.class, () -> {
            Map<String, String> props = new HashMap<>();
            props.put("pubnub.publish_key", value);
            props.put("pubnub.subscribe_key", value);
            props.put("pubnub.secret_key", value);
            new PubNubKafkaSourceConnector().validate(props);
        });
    }

}
