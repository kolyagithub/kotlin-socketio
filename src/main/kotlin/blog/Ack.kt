package blog

import io.javalin.ApiBuilder.*
import io.javalin.Javalin
import com.corundumstudio.socketio.listener.*
import com.corundumstudio.socketio.*

fun main(args: Array<String>) {

    val config = Configuration()
    config.setHostname("localhost")
    config.setPort(9092)

    val server = SocketIOServer(config)
    val onData = DataListener<ChatObject> { client, data, ackRequest ->
        // check is ack requested by client,
        // but it's not required check
        if (ackRequest.isAckRequested()) {
            // send ack response with data to client
            ackRequest.sendAckData("client message was delivered to server!", "yeah!")
        }

        // send message back to client with ack callback WITH data
        val ackChatObjectData = ChatObject(data.getUserName(), "message with ack data")
        client.sendEvent("ackevent2", object : AckCallback<String>(String::class.java) {
            override fun onSuccess(result: String) {
                System.out.println("ack from client: " + client.getSessionId() + " data: " + result)
            }
        }, ackChatObjectData)

        val ackChatObjectData1 = ChatObject(data.getUserName(), "message with void ack")
        client.sendEvent("ackevent3", object : VoidAckCallback() {

            override fun onSuccess() {
                System.out.println("void ack from: " + client.getSessionId())
            }

        }, ackChatObjectData1)
    }

    server.addEventListener("ackevent1", ChatObject::class.java, onData)

    server.start()

    Thread.sleep(Integer.MAX_VALUE.toLong())

    server.stop()

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
