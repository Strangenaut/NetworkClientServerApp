package controller

import receiver.PacketReceiver
import receiver.impl.TcpPacketReceiver
import receiver.impl.UdpPacketReceiver

import java.awt.BorderLayout
import java.awt.FlowLayout
import java.lang.Thread
import javax.swing.*

class ServerController {

    private val frame: JFrame = JFrame("TCP/UDP Server")
    private val messageArea: JTextArea = JTextArea()
    private val portField: JTextField = JTextField("", 6)
    private val startStopButton: JButton = JButton("Start")

    private val packetReceivers = mutableListOf<PacketReceiver>()
    private val receiverThreads = mutableListOf<Thread>()
    private var isRunning = false

    fun launch() {
        frame.setSize(500, 400)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.layout = BorderLayout()
        frame.setLocationRelativeTo(null)

        messageArea.isEditable = false
        val scrollPane = JScrollPane(messageArea)
        frame.add(scrollPane, BorderLayout.CENTER)

        val controlPanel = JPanel(FlowLayout())
        controlPanel.add(JLabel("Port:"))
        controlPanel.add(portField)
        controlPanel.add(startStopButton)
        frame.add(controlPanel, BorderLayout.NORTH)

        startStopButton.addActionListener {
            if (isRunning) {
                stopServer()
            } else {
                startServer()
            }
        }

        frame.isVisible = true
    }

    private fun startServer() {
        try {
            isRunning = true
            val port = portField.text.toInt()

            packetReceivers += TcpPacketReceiver(port = port, onResult = this::appendMessage)
            packetReceivers += UdpPacketReceiver(port = port, onResult = this::appendMessage)

            packetReceivers.forEach {
                receiverThreads += Thread(it)
                receiverThreads.last().start()
            }

            startStopButton.text = "Stop"

            appendMessage("Server started on port $port\n", true)
        } catch (e: Exception) {
            appendMessage("Error starting server: ${e.message}")
        }
    }

    private fun stopServer() {
        try {
            isRunning = false
            startStopButton.text = "Start"

            packetReceivers.forEach {
                it.stopServer()
            }

            receiverThreads.clear()
            packetReceivers.clear()

            appendMessage("Server stopped", true)
        } catch (e: Exception) {
            appendMessage("Error stopping server: ${e.message}")
        }
    }

    private fun appendMessage(message: String, clearBefore: Boolean = false) {
        if (clearBefore) {
            messageArea.text = ""
        }

        messageArea.append("$message\n")
    }
}