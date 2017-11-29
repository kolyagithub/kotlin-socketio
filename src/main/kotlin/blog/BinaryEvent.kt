package blog

import io.javalin.ApiBuilder.*
import io.javalin.Javalin
import com.corundumstudio.socketio.listener.*
import com.corundumstudio.socketio.*

fun main(args: Array<String>) {

    val config = Configuration()
    config.hostname = "localhost"
    config.port = 9092
    config.maxFramePayloadLength = 1024 * 1024
    config.maxHttpContentLength = 1024 * 1024

    val server = SocketIOServer(config)
    val onData = DataListener<ByteArray> { client, data, ackRequest ->
        client.sendEvent("msg", data)
    }
    server.addEventListener("msg", ByteArray::class.java, onData)

    server.start()

    val app = Javalin.create().apply {
        port(7000)
        exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
        error(404) { ctx -> ctx.json("Page not found") }
    }.start()

    app.routes {

        get("/") { ctx ->
            print("route /")
            ctx.result("Hello SocketIO")
        }

    }

}
