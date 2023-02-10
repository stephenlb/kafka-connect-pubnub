package com.pubnub.kafka.connect;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.config.ConfigException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.pubnub.kafka.connect.PubNubKafkaConnectorConfig.*;

public class PubNubConnectorConfigTest {

    @Test
    public void basicParamsAreMandatory() {
        assertThrows(ConfigException.class, () -> {
            Map<String, String> props = new HashMap<>();
            new PubNubKafkaConnectorConfig(props);
        });
    }

    public void checkingNonRequiredDefaults() {
        Map<String, String> props = new HashMap<>();
        PubNubKafkaConnectorConfig config = new PubNubKafkaConnectorConfig(props);
    }

}
