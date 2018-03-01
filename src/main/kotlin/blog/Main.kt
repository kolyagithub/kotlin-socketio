package blog

import io.javalin.ApiBuilder.*
import io.javalin.Javalin
import com.corundumstudio.socketio.listener.*
import com.corundumstudio.socketio.*
import io.javalin.Handler
import mu.KotlinLogging

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {

    /*
        * Use as server
     */
    /*
    val config: Configuration = Configuration()
    val host: String = "127.0.0.1"
    val port: Int = 3004
    config.setHostname(host)
    config.setPort(port)


    val server = SocketIOClient (config)

    // Without NSP
    val onData1 = DataListener<ChatObject> { client, data, ackRequest ->
        server.broadcastOperations.sendEvent("chatevent", data)
    }
    server.addEventListener("chatevent", ChatObject::class.java, onData1)

    // With NSP
    //val chat1namespace = server.addNamespace("/nsp1")
    val chat1namespace = server.addNamespace("70204c8e-d46f-4273-b920-bf879f2b16ed")

    val onData2 = DataListener<Any> { client, data, ackRequest ->
        println(data)
        chat1namespace.getBroadcastOperations().sendEvent("data", data)
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
            logger.info { "hello 1" }
            logger.warn { "hello 2" }
            logger.error { "hello 3" }
            ctx.result("Hello SocketIO")
        }

    }
*/


    /*
        * Use as client
     */
    val socket = IO.socket("http://127.0.0.1:3004/70204c8e-d46f-4273-b920-bf879f2b16ed")
    socket.connect()
            .on(Socket.EVENT_CONNECT, { println("connected") })
            .on(Socket.EVENT_DISCONNECT, { println("disconnected") })
            .on("data", { args ->
                val obj = args[0] as JSONObject
                println("TSP: ${obj["tsp"]}")
                println("VAL_1: ${obj["val1"]}")
                println("VAL_2: ${obj["val2"]}")

                val iter = obj.keys()
                while (iter.hasNext()) {
                    val key = iter.next()
                    try {
                        println("AAAAAAAAAAAAAAAA: $key")
                        //val pos = obj.getJSONObject(key)

                        //Main thread
                        /*Handler(Looper.getMainLooper()).post {
                            if (markers[key] == null) {
                                markers[key] = mMap.addMarker(marker.position(LatLng(pos.getDouble("lat"), pos.getDouble("lng"))))
                            } else {
                                markers[key]!!.position = LatLng(pos.getDouble("lat"), pos.getDouble("lng"))
                            }
                        }*/
                    } catch (e: JSONException) {
                        // Something went wrong!
                    }
                }
            })

}
