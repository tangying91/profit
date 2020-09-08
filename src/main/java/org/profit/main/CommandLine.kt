package org.profit.main

import org.profit.ProfitServer
import org.profit.config.spring.AppSpringConfig
import org.springframework.context.annotation.AnnotationConfigApplicationContext

class CommandLine protected constructor() {
    private fun addShutdownHook(server: ProfitServer) {
        val shutdownHook = Runnable { server.stop() }
        // Add shutdown hook
        val runtime = Runtime.getRuntime()
        runtime.addShutdownHook(Thread(shutdownHook))
    }

    @Throws(Exception::class)
    protected fun getConfiguration(args: Array<String>): ProfitServer? {
        var server: ProfitServer? = null
        if (args.size == 0) {
            println("Using default configuration.")
            val ctx = AnnotationConfigApplicationContext(AppSpringConfig::class.java)
            //ctx.registerShutdownHook();
            server = ProfitServer()
        }
        return server
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val cli = CommandLine()
            try { // Get configuration
                val server = cli.getConfiguration(args) ?: return
                // Start the server
                server.start()
                cli.addShutdownHook(server)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}