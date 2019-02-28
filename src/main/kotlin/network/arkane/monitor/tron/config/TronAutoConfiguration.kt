package network.arkane.monitor.tron.config

import network.arkane.provider.tron.grpc.GrpcClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TronAutoConfiguration {
    companion object Logging {
        val log = LoggerFactory.getLogger(TronAutoConfiguration::class.java)
    }

    @Bean
    fun provideGrpcClient(@Value("\${fullnode}") fullNode: String,
                          @Value("\${soliditynode}") solidityNode: String): GrpcClient {
        log.info("Starting grpcClient:\nfull-node: {}\nsolidity-node: {}", fullNode, solidityNode)
        return GrpcClient(fullNode, solidityNode)
    }
}