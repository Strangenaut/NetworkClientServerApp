package receiver.impl

import receiver.PacketReceiver
import java.net.DatagramPacket
import java.net.DatagramSocket

class UdpPacketReceiver(
    port: Int,
    private val onResult: (message: String) -> Unit
) : PacketReceiver {

    private var serverSocket: DatagramSocket? = DatagramSocket(port)
    private val bufferSize = 1024
    private val buffer = ByteArray(bufferSize)
    private var isRunning = false

    override fun run() {
        isRunning = true

        while (isRunning) {
            try {
                val p = DatagramPacket(buffer, buffer.size)
                serverSocket?.receive(p)

                val message = String(p.data, 0, p.length)

                val clientIp = p.address.toString().substringAfter('/')
                val clientPort = p.port

                onResult("UDP:$clientIp:$clientPort> $message")
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
    }
}