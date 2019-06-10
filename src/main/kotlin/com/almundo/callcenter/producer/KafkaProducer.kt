package com.almundo.callcenter.producer

import org.apache.kafka.clients.producer.internals.Sender
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate


/**
 * Created by tomReq on 6/9/19.
 */
class KafkaProducer {
  private val LOGGER = LoggerFactory.getLogger(Sender::class.java)

  @Value("\${kafka.topic}")
  lateinit var topic: String

  @Autowired
  lateinit var kafkaTemplate: KafkaTemplate<String, String>

  fun send(payload: String) {
    LOGGER.info("sending payload='{}'", payload)
    kafkaTemplate.send(topic, payload)
  }
}