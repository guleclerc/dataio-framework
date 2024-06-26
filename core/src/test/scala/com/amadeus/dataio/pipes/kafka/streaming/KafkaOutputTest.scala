package com.amadeus.dataio.pipes.kafka.streaming

import com.amadeus.dataio.testutils.JavaImplicitConverters._
import com.typesafe.config.{ConfigException, ConfigFactory}
import org.apache.spark.sql.streaming.Trigger
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.duration.Duration
class KafkaOutputTest extends AnyWordSpec with Matchers {

  "KafkaOutput" should {
    "be initialized according to configuration" in {

      val config = ConfigFactory.parseMap(
        Map(
          "Output" -> Map(
            "Type"     -> "com.amadeus.dataio.output.streaming.KafkaOutput",
            "Name"     -> "my-test-kafka-output",
            "Brokers"  -> "bktv001:9000, bktv002.amadeus.net:8000",
            "Topic"    -> "test.topic",
            "Mode"     -> "append",
            "Duration" -> "60 seconds",
            "Timeout"  -> "24"
          )
        )
      )

      val kafkaStreamOutput = KafkaOutput(config.getConfig("Output"))

      kafkaStreamOutput.outputName shouldEqual Some("my-test-kafka-output")
      kafkaStreamOutput.brokers shouldEqual "bktv001:9000, bktv002.amadeus.net:8000"
      kafkaStreamOutput.topic shouldEqual Some("test.topic")
      kafkaStreamOutput.trigger shouldEqual Some(Trigger.ProcessingTime(Duration("60 seconds")))
    }

    "be initialized with all optional properties" in {

      val config = ConfigFactory.parseMap(
        Map(
          "Output" -> Map(
            "Type"     -> "com.amadeus.dataio.output.streaming.KafkaOutput",
            "Brokers"  -> "bktv001:9000, bktv002.amadeus.net:8000",
            "Topic"    -> "test.topic",
            "Mode"     -> "append",
            "Trigger"  -> "Continuous",
            "Duration" -> "60 seconds",
            "Timeout"  -> "24",
            "Options" -> Map(
              "failOnDataLoss"                       -> "false",
              "maxOffsetsPerTrigger"                 -> "20000000",
              "\"kafka.security.protocol\""          -> "SASL_PLAINTEXT",
              "\"kafka.sasl.kerberos.service.name\"" -> "kafka"
            )
          )
        )
      )

      val kafkaStreamOutput = KafkaOutput(config.getConfig("Output"))

      kafkaStreamOutput.outputName shouldEqual None
      kafkaStreamOutput.brokers shouldEqual "bktv001:9000, bktv002.amadeus.net:8000"
      kafkaStreamOutput.topic shouldEqual Some("test.topic")
      kafkaStreamOutput.options shouldEqual Map(
        "failOnDataLoss"                   -> "false",
        "maxOffsetsPerTrigger"             -> "20000000",
        "kafka.security.protocol"          -> "SASL_PLAINTEXT",
        "kafka.sasl.kerberos.service.name" -> "kafka"
      )
      kafkaStreamOutput.trigger shouldEqual Some(Trigger.Continuous(Duration("60 seconds")))
    }

    "be initialized with trigger AvailableNow" in {

      val config = ConfigFactory.parseMap(
        Map(
          "Output" -> Map(
            "Type"    -> "com.amadeus.dataio.output.streaming.KafkaOutput",
            "Brokers" -> "bktv001:9000, bktv002.amadeus.net:8000",
            "Topic"   -> "test.topic",
            "Mode"    -> "append",
            "Trigger" -> "AvailableNow",
            "Timeout" -> "24",
            "Options" -> Map(
              "failOnDataLoss"                       -> "false",
              "maxOffsetsPerTrigger"                 -> "20000000",
              "\"kafka.security.protocol\""          -> "SASL_PLAINTEXT",
              "\"kafka.sasl.kerberos.service.name\"" -> "kafka"
            )
          )
        )
      )

      val kafkaStreamOutput = KafkaOutput(config.getConfig("Output"))

      kafkaStreamOutput.outputName shouldEqual None
      kafkaStreamOutput.brokers shouldEqual "bktv001:9000, bktv002.amadeus.net:8000"
      kafkaStreamOutput.topic shouldEqual Some("test.topic")
      kafkaStreamOutput.options shouldEqual Map(
        "failOnDataLoss"                   -> "false",
        "maxOffsetsPerTrigger"             -> "20000000",
        "kafka.security.protocol"          -> "SASL_PLAINTEXT",
        "kafka.sasl.kerberos.service.name" -> "kafka"
      )
      kafkaStreamOutput.trigger shouldEqual Some(Trigger.AvailableNow())
    }

    "be initialized with None trigger" in {

      val config = ConfigFactory.parseMap(
        Map(
          "Output" -> Map(
            "Type"    -> "com.amadeus.dataio.output.streaming.KafkaOutput",
            "Brokers" -> "bktv001:9000, bktv002.amadeus.net:8000",
            "Topic"   -> "test.topic",
            "Mode"    -> "append",
            "Timeout" -> "24",
            "Options" -> Map(
              "failOnDataLoss"                       -> "false",
              "maxOffsetsPerTrigger"                 -> "20000000",
              "\"kafka.security.protocol\""          -> "SASL_PLAINTEXT",
              "\"kafka.sasl.kerberos.service.name\"" -> "kafka"
            )
          )
        )
      )

      val kafkaStreamOutput = KafkaOutput(config.getConfig("Output"))

      kafkaStreamOutput.outputName shouldEqual None
      kafkaStreamOutput.brokers shouldEqual "bktv001:9000, bktv002.amadeus.net:8000"
      kafkaStreamOutput.topic shouldEqual Some("test.topic")
      kafkaStreamOutput.options shouldEqual Map(
        "failOnDataLoss"                   -> "false",
        "maxOffsetsPerTrigger"             -> "20000000",
        "kafka.security.protocol"          -> "SASL_PLAINTEXT",
        "kafka.sasl.kerberos.service.name" -> "kafka"
      )
      kafkaStreamOutput.trigger shouldEqual None
    }

    "be initialized without topic" in {

      val config = ConfigFactory.parseMap(
        Map(
          "Output" -> Map(
            "Type"    -> "com.amadeus.dataio.output.streaming.KafkaOutput",
            "Brokers" -> "bktv001:9000, bktv002.amadeus.net:8000",
            "Mode"    -> "append",
            "Timeout" -> "24",
            "Options" -> Map(
              "failOnDataLoss"                       -> "false",
              "maxOffsetsPerTrigger"                 -> "20000000",
              "\"kafka.security.protocol\""          -> "SASL_PLAINTEXT",
              "\"kafka.sasl.kerberos.service.name\"" -> "kafka"
            )
          )
        )
      )

      val kafkaStreamOutput = KafkaOutput(config.getConfig("Output"))

      kafkaStreamOutput.outputName shouldEqual None
      kafkaStreamOutput.brokers shouldEqual "bktv001:9000, bktv002.amadeus.net:8000"
      kafkaStreamOutput.topic shouldEqual None
      kafkaStreamOutput.options shouldEqual Map(
        "failOnDataLoss"                   -> "false",
        "maxOffsetsPerTrigger"             -> "20000000",
        "kafka.security.protocol"          -> "SASL_PLAINTEXT",
        "kafka.sasl.kerberos.service.name" -> "kafka"
      )
      kafkaStreamOutput.trigger shouldEqual None
    }

    "throw an exception given missing brokers" in {

      val config = ConfigFactory.parseMap(
        Map(
          "Output" -> Map(
            "Type"     -> "com.amadeus.dataio.output.streaming.KafkaOutput",
            "Name"     -> "my-test-kafka",
            "Topic"    -> "test.topic",
            "Mode"     -> "append",
            "Duration" -> "60 seconds",
            "Timeout"  -> "24",
            "Options" -> Map(
              "failOnDataLoss"                       -> "false",
              "maxOffsetsPerTrigger"                 -> "20000000",
              "\"kafka.security.protocol\""          -> "SASL_PLAINTEXT",
              "\"kafka.sasl.kerberos.service.name\"" -> "kafka"
            )
          )
        )
      )

      intercept[ConfigException.Missing] {
        KafkaOutput(config.getConfig("Output"))
      }
    }
  }

  "createQueryName" should {

    val uuidPattern = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"

    "return a query name based on Topic" in {

      val kafkaOutput =
        KafkaOutput(
          brokers = "bktv001:9000, bktv002.amadeus.net:8000",
          topic = Some("test.topic"),
          trigger = Some(Trigger.AvailableNow()),
          timeout = 0,
          mode = "append",
          outputName = None
        )

      val queryName = kafkaOutput.createQueryName()

      queryName should fullyMatch regex "^QN_test.topic_" + uuidPattern + "$"

    }

    "return a query name based on Topic and output name" in {

      val kafkaOutput =
        KafkaOutput(
          brokers = "bktv001:9000, bktv002.amadeus.net:8000",
          topic = Some("test.topic"),
          trigger = Some(Trigger.AvailableNow()),
          timeout = 0,
          mode = "append",
          outputName = Some("myTestOutput")
        )

      val queryName = kafkaOutput.createQueryName()

      queryName should fullyMatch regex "^QN_myTestOutput_test.topic_" + uuidPattern + "$"

    }

    "return a query name based on output name" in {

      val kafkaOutput =
        KafkaOutput(
          brokers = "bktv001:9000, bktv002.amadeus.net:8000",
          topic = None,
          trigger = Some(Trigger.AvailableNow()),
          timeout = 0,
          mode = "append",
          outputName = Some("myTestOutput")
        )

      val queryName = kafkaOutput.createQueryName()

      queryName should fullyMatch regex "^QN_myTestOutput_" + uuidPattern + "$"

    }

    "return a query name based without name or topic" in {

      val kafkaOutput =
        KafkaOutput(
          brokers = "bktv001:9000, bktv002.amadeus.net:8000",
          topic = None,
          trigger = Some(Trigger.AvailableNow()),
          timeout = 0,
          mode = "append",
          outputName = None
        )

      val queryName = kafkaOutput.createQueryName()

      queryName should fullyMatch regex "^QN_KafkaOutput_" + uuidPattern + "$"

    }
  }
}
