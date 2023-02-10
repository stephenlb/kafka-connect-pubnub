package com.pubnub.kafka.connect;

import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

public class PubNubKafkaConnectorConfig extends AbstractConfig {

    public PubNubKafkaConnectorConfig(final Map<?, ?> originalProps) {
        super(CONFIG_DEF, originalProps);
    }

    public static final ConfigDef CONFIG_DEF = createConfigDef();
    private static ConfigDef createConfigDef() {
        ConfigDef configDef = new ConfigDef();
        addParams(configDef);
        return configDef;
    }

    private static void addParams(final ConfigDef configDef) {
        configDef.define(
            "pubnub.subscribe_key",
            Type.STRING,
            Importance.HIGH,
            "The PubNub Subscribe API KEY")
        .define(
            "pubnub.publish_key",
            Type.STRING,
            Importance.HIGH,
            "The PubNub Publish API KEY")
        .define(
            "pubnub.secret_key",
            Type.STRING,
            Importance.HIGH,
            "The PubNub Secret API KEY")
        .define(
            "task.sleep.timeout",
            Type.INT,
            5000,
            Importance.HIGH,
            "Sleep timeout used by tasks during each poll")
        .define(
            "monitor.thread.timeout",
            Type.INT,
            10000,
            Importance.LOW,
            "Timeout used by the monitoring thread");
    }

}
