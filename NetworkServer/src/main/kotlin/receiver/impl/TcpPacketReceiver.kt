package receiver.impl

import receiver.PacketReceiver
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket

class TcpPacketReceiver(
    port: Int,
    private val onResult: (message: String) -> Unit
) : PacketReceiver {

    private var serverSocket: ServerSocket? = ServerSocket(port)
    private var isRunning = false

    override fun run() {
        isRunning = true

        while (isRunning) {
            try {
                val clientSocket = serverSocket?.accept()

                val reader = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
                val message = reader.readLine()

                val clientIp = clientSocket.inetAddress.toString().substringAfter('/')
                val clientPort = clientSocket.port


                onResult("TCP:$clientIp:$clientPort> $message")

                clientSocket.close()
            } catch (e: Exception) {
                if (isRunning) {
                    onResult("Error: ${e.message}")
                }
            }
        }
    }

    override fun stopServer() {
        isRunning = false
        serverSocket?.close()
        serverSocket = null

        onResult("Server stopped")
    }
}