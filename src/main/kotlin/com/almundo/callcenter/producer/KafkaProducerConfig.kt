package com.almundo.callcenter.producer

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import java.util.*



/**
 * Created by tomReq on 6/9/19.
 */
@Configuration
class KafkaProducerConfig {

  @Value("\${kafka.bootstrap-servers}")
  lateinit var bootstrapServers: String

  @Bean
  fun producerConfigs(): Map<String, Any> {
    val props = HashMap<String, Any>()

    props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
    props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
    props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

    return props
  }

  @Bean
  fun producerFactory(): ProducerFactory<String, String> {
    return DefaultKafkaProducerFactory(producerConfigs())
  }

  @Bean
  fun kafkaTemplate(): KafkaTemplate<String, String> {
    return KafkaTemplate(producerFactory())
  }

  @Bean
  fun kafkaProducer() = KafkaProducer()

}