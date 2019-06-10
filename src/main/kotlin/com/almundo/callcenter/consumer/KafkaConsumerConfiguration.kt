package com.almundo.callcenter.consumer


import com.almundo.callcenter.model.CallCenter
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import java.util.*



/**
 * Created by tomReq on 6/9/19.
 */
@EnableKafka
@Configuration
class KafkaConsumerConfig {

  @Value("\${kafka.bootstrap-servers}")
  lateinit var bootstrapServers: String

  @Bean
  fun consumerConfigs(): Map<String, Any> {
    val props = HashMap<String, Any>()

    props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
    props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    props[ConsumerConfig.GROUP_ID_CONFIG] = "callcenter"
    props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"

    return props
  }

  @Bean
  fun consumerFactory(): ConsumerFactory<String, String> {
    return DefaultKafkaConsumerFactory(consumerConfigs())
  }

  @Bean
  fun kafkaListenerContainerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> {
    val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
    factory.consumerFactory = consumerFactory()
    return factory
  }

  @Bean
  fun receiver() = KafkaConsumer()

  @Bean
  fun callCenter() = CallCenter()

}