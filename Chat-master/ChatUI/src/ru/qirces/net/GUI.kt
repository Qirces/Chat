package ru.qirces.net

import java.awt.*
import javax.swing.*

class GUI : JFrame("Чат"), ChatUI {

    private val chatArea: JTextArea = JTextArea().apply {
        isEditable = false
        isFocusable = false
        font = Font("Arial", Font.PLAIN, 18)
    }

    private val scrPane = JScrollPane(chatArea)

    private val messageField: JTextField = JTextField().apply {
        addActionListener {
            val message = text
            if (message.isNotBlank()) {
                text = ""
                sendMessage("MSG:$message")
            }
        }
        preferredSize = Dimension(300, 50)
        font = Font("Arial", Font.PLAIN, 20)
        border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        )
    }

    private val sendBtn: JButton = JButton("Отправить").apply {
        addActionListener {
            val message = messageField.text
            if (message.isNotBlank()) {
                messageField.text = ""
                sendMessage("MSG:$message")
            }
        }
        preferredSize = Dimension(130, 50)
        font = Font("Arial", Font.PLAIN, 18)
        border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        )
    }
    
    private val loginWindow: LoginWindow = LoginWindow().apply {
        isVisible = false

        loginBtn.addActionListener {
            val nickname = nicknameField.text
            val password = passwordField.text
            sendMessage("LOG:${nickname}:$password")
        }

        regBtn.addActionListener {
            isVisible = false
            regWindow.isVisible = true
        }
    }

    private val regWindow = RegWindow().apply {
        isVisible = false

        registerBtn.addActionListener {
            val nickname = nicknameField.text
            val password = passwordField.text
            val repPassword = repeatPasswordField.text
            
            if (password == repPassword)
                sendMessage("REG:${nickname}:${password}")
            else
                JOptionPane.showMessageDialog(
                    this,
                    "Пароли не совпадают",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
                )
        }
        
        loginBtn.addActionListener { 
            isVisible = false
            loginWindow.isVisible = true
        }
    }

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        
        layout = BorderLayout()

        setLocation(300, 50)
        size = Dimension(400, 600)
        minimumSize = Dimension(200, 200)
        isVisible = true

        add(scrPane, BorderLayout.CENTER)

        val messagePanel = Box.createHorizontalBox().apply {
            add(messageField)
            add(Box.createHorizontalStrut(10))
            add(sendBtn)
            border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
        }

        add(messagePanel, BorderLayout.SOUTH)

        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }

    override var send: ((String) -> Unit)? = null

    override fun parse(data: String) {
        data.split(":", limit = 2).let {
            when (it[0]) {
                "INTR" -> {
                    requestLogin()
                }

                "REINTR" -> {
                    JOptionPane.showMessageDialog(
                        this,
                        it[1],
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                    )
                }

                "REGOK" -> {
                    regWindow.isVisible = false
                    loginWindow.isVisible = true
                }

                "REREG" -> {
                    JOptionPane.showMessageDialog(
                        this,
                        it[1],
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                    )
                }

                "NAMEOK" -> {
                    isEnabled = true
                    requestFocus()
                    loginWindow.isVisible = false
                    printInChat("Вы успешно вошли в чат")
                }

                "USRS" -> {
//                    it[1].split(":").forEach { v -> usersList.add(v) }
//                    writeListInLabel()
                }

                "SYS" -> {
                    printInChat(it[1])
                }

                "MSG" -> {
                    val split2 = it[1].split(":", limit = 2)
                    printInChat(split2[1])
                }

                "NEW" -> {
                    printInChat("Пользователь ${it[1]} вошёл в чат")
//                    usersList.add(it[1])
//                    writeListInLabel()
                }

                "EXIT" -> {
                    printInChat("Пользователь ${it[1]} покинул чат")
//                    usersList.remove(it[1])
//                    writeListInLabel()
                }

                else -> {
                    printInChat("Получена неизвестная команда: " + it[0])
                }
            }
        }
    }

    private fun requestLogin() {
        this.isEnabled = false
        loginWindow.isVisible = true
    }

    private fun printInChat(message: String) {
        chatArea.append(message + "\n")
    }
    
    private fun sendMessage(message: String) {
        send?.let {
            it(message)
        }
    }
}