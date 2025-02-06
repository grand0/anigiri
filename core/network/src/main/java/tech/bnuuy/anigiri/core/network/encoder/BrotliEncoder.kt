package tech.bnuuy.anigiri.core.network.encoder

import io.ktor.client.HttpClient
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.compression.ContentEncodingConfig
import io.ktor.util.ContentEncoder
import io.ktor.util.IdentityEncoder
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import org.brotli.dec.BrotliInputStream
import kotlin.coroutines.CoroutineContext

internal object BrotliEncoder : ContentEncoder {
    override val name = "br"
    
    override fun decode(source: ByteReadChannel, coroutineContext: CoroutineContext): ByteReadChannel {
        if (source.isClosedForRead) {
            return source
        }
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

internal object IgnoreBrotliEncoder : ContentEncoder by IdentityEncoder {
    override val name = "br"
}

fun HttpClient.ignoringBrotli() = config {
    install(ContentEncoding) {
        customEncoder(IgnoreBrotliEncoder)
    }
}
