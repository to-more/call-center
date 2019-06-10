package com.almundo.callcenter.consumer

import com.almundo.callcenter.model.CallCenter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import java.util.concurrent.CountDownLatch

/**
 * Created by tomReq on 6/9/19.
 */
class KafkaConsumer {

  private val LOGGER = LoggerFactory.getLogger(KafkaConsumer::class.java)

  private val latch = CountDownLatch(1)

  @Autowired
  lateinit var callCenter: CallCenter

  fun getLatch() = latch

  @KafkaListener(topics = ["\${kafka.topic}"])
  fun receive(payload: String) {
    LOGGER.info("received payload='{}'", payload)
    callCenter.dispatchAsync().invoke().invokeOnCompletion {
      LOGGER.info("Completion call of payload='{}'", payload)
    }
    latch.countDown()
  }
}