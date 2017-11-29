package blog

import io.javalin.ApiBuilder.*
import io.javalin.Javalin
import com.corundumstudio.socketio.listener.*
import com.corundumstudio.socketio.*

class SslChatLauncher {
    fun main(args: Array<String>) {

        val config = Configuration()
        config.hostname = "localhost"
        config.port = 10443

        config.keyStorePassword = "test1234"
        val stream = SslChatLauncher::class.java.getResourceAsStream("/keystore.jks")
        config.keyStore = stream

        val server = SocketIOServer(config)
        val onData = DataListener<ChatObject> { client, data, ackRequest ->
            server.broadcastOperations.sendEvent("chatevent", data)
        }
        server.addEventListener("chatevent", ChatObject::class.java, onData)

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
}