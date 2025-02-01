package tech.bnuuy.anigiri.core.network.encoder

import io.ktor.client.plugins.compression.ContentEncodingConfig
import io.ktor.util.ContentEncoder
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import org.brotli.dec.BrotliInputStream
import kotlin.coroutines.CoroutineContext

internal object BrotliEncoder : ContentEncoder {
    override val name = "br"
    
    override fun decode(source: ByteReadChannel, coroutineContext: CoroutineContext): ByteReadChannel {
        return BrotliInputStream(source.toInputStream()).toByteReadChannel()
    }

    override fun encode(source: ByteReadChannel, coroutineContext: CoroutineContext): ByteReadChannel {
        error("Brotli encoding is not available")
    }

    override fun encode(source: ByteWriteChannel, coroutineContext: CoroutineContext): ByteWriteChannel {
        error("Brotli encoding is not available")
    }
}

fun ContentEncodingConfig.brotli(quality: Float? = null) {
    customEncoder(BrotliEncoder, quality)
}
