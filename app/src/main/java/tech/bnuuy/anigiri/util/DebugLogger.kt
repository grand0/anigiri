package tech.bnuuy.anigiri.util

import android.util.Log
import coil3.request.NullRequestDataException
import coil3.util.Logger

class DebugLogger @JvmOverloads constructor(
    override var minLevel: Logger.Level = Logger.Level.Debug,
) : Logger {

    override fun log(tag: String, level: Logger.Level, message: String?, throwable: Throwable?) {
        if (message != null) {
            println(level, tag, message)
        }
        if (throwable != null && throwable !is NullRequestDataException) {
            println(level, tag, throwable.stackTraceToString())
        }
    }

    private fun println(level: Logger.Level, tag: String, message: String) {
        Log.println(level.toInt(), tag, message)
    }
    
    private fun Logger.Level.toInt() = when (this) {
        Logger.Level.Verbose -> Log.VERBOSE
        Logger.Level.Debug -> Log.DEBUG
        Logger.Level.Info -> Log.INFO
        Logger.Level.Warn -> Log.WARN
        Logger.Level.Error -> Log.ERROR
    }
}
