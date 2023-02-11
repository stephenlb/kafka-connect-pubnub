## PubNub Kafka Connector

This codebase includes a PubNub Sink Connector.
Kafka topic records can be coppied to a PubNub channel.
The Kafka topic name will match PubNub channel name.

### ✅ Requirements

* [Docker](https://www.docker.com/get-started)

## ⬆️  Starting the local environment

With the connector properly built, you need to have a local environment to test it.
This project includes a Docker Compose file that can spin up container instances for Apache Kafka and Kafka Connect.
Additionally a sample producer feed starts on the `"pubnub"` topic.
This feed emits a sample message every few seconds.

Start the containers using Docker Compose.

```bash
docker compose up
```

Wait until the containers `kafka` and `connect` are started and healthy.

## ⏯ Deploying and the Sink Connector

After the containers `kafka` and `connect` are started and healthy, then proceed.
The following command will copy data from the configured
`Kafka Topic` name to the `PubNub Channel` with the same name.

* Deploy the connector.

```bash
curl -X POST \
    -d @examples/pubnub-sink-connector.json \
    -H "Content-Type:application/json" \
    http://localhost:8083/connectors
```

Now you can see the message deliveries with this web tool:
[PubNub Web Console](https://stephenlb.github.io/pubnub-tools/console/console.html?channel=pubnub&origin=ps.pndsn.com&sub=demo&pub=demo&ssl=true)

You can see messages are being delivered successfully using the PubNub Web Console.

## 📝 Modify Settings

Edit the configuration file: `./examples/pubnub-sink-connector.json`.
Update the file to include your `publish_key`, `subscribe_key` and `secret_key`.
Update the `topics` to match the topics you want to sink to PubNub.
**Add `topics.regex` to match topic patterns!**

```json
{
    "name": "pubnub-sink-connector",
    "config": {
        "topics":"pubnub,pubnub1,pubnub2",
        "topics.regex":"",
        "pubnub.publish_key": "demo",
        "pubnub.subscribe_key": "demo",
        "pubnub.secret_key": "demo",
        "connector.class": "com.pubnub.kafka.connect.PubNubKafkaSinkConnector",
        "tasks.max": "3",
        "value.deserializer":"custom.class.serialization.JsonDeserializer",
        "value.serializer":"custom.class.serialization.JsonSerializer"
    }
}
```

## ⏹ Undeploy the connector

* Use the following command to undeploy the connector from Kafka Connect:

```bash
curl -X DELETE \
    http://localhost:8083/connectors/pubnub-sink-connector
```

## ⬇️  Stopping the local environment

* Stop the containers using Docker Compose.

```bash
docker compose down
```

## ⚙️  Building the connector locally

Build the Kafka Connect connector locally

* [Java 11+](https://openjdk.org/install)
* [Maven 3.8.6+](https://maven.apache.org/download.cgi)

```bash
mvn clean package
```

💡 A file named `target/pubnub-kafka-connector-1.x.jar` will be created.
