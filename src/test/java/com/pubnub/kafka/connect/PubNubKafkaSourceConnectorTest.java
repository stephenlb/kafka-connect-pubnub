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
}
