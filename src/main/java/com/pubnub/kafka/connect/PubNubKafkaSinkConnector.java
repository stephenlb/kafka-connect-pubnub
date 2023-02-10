package com.pubnub.kafka.connect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.common.config.Config;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigValue;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkConnector;
import org.apache.kafka.connect.util.ConnectorUtils;

import static com.pubnub.kafka.connect.PubNubKafkaConnectorConfig.*;

public class PubNubKafkaSinkConnector extends SinkConnector {

    private final Logger log = LoggerFactory.getLogger(PubNubKafkaSinkConnector.class);

    private Map<String, String> originalProps;
    private PubNubKafkaConnectorConfig config;
    private NewPartitionsCheckerThread checker;

    @Override
    public String version() {
        return PropertiesUtil.getConnectorVersion();
    }

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }

    @Override
    public Class<? extends Task> taskClass() {
        return PubNubKafkaSinkConnectorTask.class;
    }

    @Override
    public Config validate(Map<String, String> connectorConfigs) {
        Config config = super.validate(connectorConfigs);
        return config;
    }

    @Override
    public void start(Map<String, String> originalProps) {
        this.originalProps = originalProps;
        config = new PubNubKafkaConnectorConfig(originalProps);
        int monitorThreadTimeout = config.getInt("monitor.thread.timeout");
        checker = new NewPartitionsCheckerThread(context, monitorThreadTimeout);
        checker.start();
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        List<Map<String, String>> taskConfigs = new ArrayList<>();
        // The partitions below represent the source's part that
        // would likely to be broken down into tasks... such as
        // tables in a database.
        List<String> partitions = checker.getCurrentSources();
        if (partitions.isEmpty()) {
            taskConfigs = Collections.emptyList();
            log.warn("No tasks created because there is zero to work on");
        } else {
            int numTasks = Math.min(partitions.size(), maxTasks);
            List<List<String>> partitionSources = ConnectorUtils.groupPartitions(partitions, numTasks);
            for (List<String> source : partitionSources) {
                Map<String, String> taskConfig = new HashMap<>(originalProps);
                taskConfig.put("sources", String.join(",", source));
                taskConfigs.add(taskConfig);
            }
        }
        return taskConfigs;
    }

    @Override
    public void stop() {
        checker.shutdown();
    }

}
