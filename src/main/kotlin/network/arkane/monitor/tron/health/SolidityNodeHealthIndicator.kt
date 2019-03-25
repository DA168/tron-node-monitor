package network.arkane.monitor.tron.health

import network.arkane.provider.tron.grpc.GrpcClient
import org.springframework.boot.actuate.health.AbstractReactiveHealthIndicator
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.Status
import org.springframework.stereotype.Component
import org.tron.api.GrpcAPI
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class SolidityNodeHealthIndicator(val grpcClient: GrpcClient) : AbstractReactiveHealthIndicator() {

    private val latestBlock: Optional<GrpcAPI.BlockExtention>
        get() = try {
            Optional.ofNullable(grpcClient.blockingStubSolidity.getNowBlock2(GrpcAPI.EmptyMessage.getDefaultInstance()))
        } catch (ex: Exception) {
            ex.printStackTrace()
            Optional.empty()
        }

    override fun doHealthCheck(builder: Health.Builder): Mono<Health> {
        try {
            val block = latestBlock
            if (block.isPresent) {
                val date = Date(block.get().blockHeader.rawData.timestamp * 1000)
                val blockTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
                return if (blockTime.plus(10, ChronoUnit.MINUTES).isBefore(LocalDateTime.now(ZoneId.systemDefault()))) {
                    Mono.just(builder.status(Status.DOWN)
                            .withDetail("tron.soliditynode", "last block is older than 30 minutes")
                            .build())
                } else {
                    block.map {
                        Mono.just(builder.up().withDetail("tronnode", "latest block is " + it.blockHeader.rawData.number).build())
                    }.get()
                }
            } else {
                return Mono.just(
                        builder.down()
                                .withDetail("tron.soliditynode.down", "Unable to fetch status for ethereum node").build())
            }
        } catch (ex: Exception) {
            return Mono.just(
                    builder.down()
                            .withDetail("tron.soliditynode.exception", ex.message).build())
        }
    }
}