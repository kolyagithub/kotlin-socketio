package blog

import io.javalin.ApiBuilder.*
import io.javalin.Javalin
import com.corundumstudio.socketio.listener.*
import com.corundumstudio.socketio.*

fun main(args: Array<String>) {

    val config: Configuration = Configuration()
    val host: String = "192.168.1.188"
    val port: Int = 7001
    config.setHostname(host)
    config.setPort(port)


    val server = SocketIOServer(config)

    // Without NSP
    val onData1 = DataListener<ChatObject> { client, data, ackRequest ->
        server.broadcastOperations.sendEvent("chatevent", data)
    }
    server.addEventListener("chatevent", ChatObject::class.java, onData1)

    // With NSP
    val chat1namespace = server.addNamespace("/nsp1")

    val onData2 = DataListener<Any> { client, data, ackRequest ->
        println(data)
        chat1namespace.getBroadcastOperations().sendEvent("message", data)
    }

    chat1namespace.addEventListener("message", Any::class.java, onData2)


    val chat2namespace = server.addNamespace("/nsp2")


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
