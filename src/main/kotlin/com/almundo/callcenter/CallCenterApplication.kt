package com.almundo.callcenter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CallCenterApplication

fun main(args: Array<String>) {
	runApplication<CallCenterApplication>(*args)
}
