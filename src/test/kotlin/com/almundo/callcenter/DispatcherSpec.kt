package com.almundo.callcenter

import com.almundo.callcenter.model.CallCenter
import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * Created by tomReq
 */
class DispatcherSpec: StringSpec({

  "test call execution" {

    println("Start ==========")
    val callCenter = CallCenter()

    val run = run(callCenter)

    println(run)
  }
})

fun run(callCenter: CallCenter) = (1..30).map {
    callCenter.dispatchAsync()
  }.map {
    it.invoke()
  }.map {
    it to it.invokeOnCompletion { println("Completed Call") }
  }

fun buildCallerAsync(callCenter: CallCenter) = runBlocking {
  withContext(Dispatchers.Default) {
    async(Dispatchers.Default) {
      callCenter.dispatchAsync()
    }
  }
}
