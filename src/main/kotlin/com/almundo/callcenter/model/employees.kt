package com.almundo.callcenter.model

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Created by tomReq
 */
open class Employee {

  private fun randomTime() = (5..10).shuffled().first().toLong() * 100

  open fun <T> dispatchAsync(delayTime: Int = 0, execution: Employee.() -> T) = runBlocking {
    withContext(Dispatchers.Default) {
      async {
        delay(randomTime())
        execution()
      }
    }
  }
}

class Operator: Employee() {
  override fun toString() = "Operator() ${Thread.currentThread().name}"
}

class Supervisor: Employee() {
  override fun toString() = "Supervisor() ${Thread.currentThread().name}"
}

class Manager: Employee() {
  override fun toString() = "Manager() ${Thread.currentThread().name}"
}

class CallCenter {

  private val LOGGER = LoggerFactory.getLogger(CallCenter::class.java)

  private val operators = LinkedList(
    listOf(
      Operator(),
      Operator(),
      Operator(),
      Operator(),
      Operator(),
      Operator(),
      Operator(),
      Operator(),
      Operator(),
      Operator()
    )
  )

  private val supervisors = LinkedList(
    listOf(
      Supervisor(),
      Supervisor(),
      Supervisor(),
      Supervisor(),
      Supervisor(),
      Supervisor(),
      Supervisor(),
      Supervisor(),
      Supervisor(),
      Supervisor()
    )
  )

  private val managers = LinkedList(
    listOf(
      Manager(),
      Manager(),
      Manager(),
      Manager(),
      Manager(),
      Manager(),
      Manager(),
      Manager(),
      Manager(),
      Manager()
    )
  )

  fun dispatchAsync(): () -> Unit = {
    when {
      operators.isNotEmpty() -> operators.pop()
        .dispatchAsync {
          dispatchingCall()
        }.invokeOnCompletion { LOGGER.info("Completion on Operator") }

      supervisors.isNotEmpty() -> supervisors.pop()
        .dispatchAsync {
          dispatchingCall()
        }.invokeOnCompletion { LOGGER.info("Completion on Supervisor") }

      managers.isNotEmpty() -> managers.pop()
        .dispatchAsync {
          dispatchingCall()
        }.invokeOnCompletion { LOGGER.info("Completion on Manager") }
    }
  }

  private fun Employee.dispatchingCall() {
    LOGGER.info("dispatch call $this")
  }
}