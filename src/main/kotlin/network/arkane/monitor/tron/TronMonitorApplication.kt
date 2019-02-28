package network.arkane.monitor.tron

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TronMonitorApplication

fun main(args: Array<String>) {
	runApplication<TronMonitorApplication>(*args)
}
