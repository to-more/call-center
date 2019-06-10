package com.almundo.callcenter

import com.almundo.callcenter.consumer.KafkaConsumer
import com.almundo.callcenter.producer.KafkaProducer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.KafkaContainer
import java.util.concurrent.TimeUnit
/**
 * Created by tomReq on 6/9/19.
 */
@SpringBootTest(classes = [ CallCenterApplication::class ], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KafkaTestContainerSpec {

  @Autowired
  lateinit var consumer: KafkaConsumer
  @Autowired
  lateinit var producer: KafkaProducer

  @Value("\${callcenter.limit-call}")
  var limit: Int = 0

  companion object {
    private val kafkaContainer = KafkaContainer()

    @BeforeAll
    @JvmStatic
    fun setUp(){
      kafkaContainer.start()
      System.setProperty("kafka.bootstrap-servers", kafkaContainer.bootstrapServers)
      System.setProperty("spring.embedded.kafka.brokers", kafkaContainer.bootstrapServers)
      System.setProperty("callcenter.limit-call", 10.toString())
    }

    @AfterAll
    @JvmStatic
    fun afterAll() {
      kafkaContainer.stop()
    }
  }

  @Test
  fun sending10Messages(){
    buildCallerAsync(producer, limit)
    consumer.getLatch().await(10, TimeUnit.SECONDS)
    assertThat(consumer.getLatch().count).isEqualTo(0)
  }

  @Test
  fun sending30Messages(){
    buildCallerAsync(producer, 300)
    consumer.getLatch().await(10, TimeUnit.SECONDS)
    assertThat(consumer.getLatch().count).isEqualTo(0)
  }

  fun buildCallerAsync(sender: KafkaProducer, limit: Int) = runBlocking {
    withContext(Dispatchers.Default) {
      (1..limit).map {
        async(Dispatchers.Default) {
          sender.send("call $it")
        }
      }.map { it.await() }
    }
  }
}
