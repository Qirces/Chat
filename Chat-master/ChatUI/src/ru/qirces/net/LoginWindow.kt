package ru.qirces.net

import java.awt.Container
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.*


class LoginWindow : JFrame() {
    val nicknameField: JTextField = JTextField(20)
    val passwordField: JPasswordField = JPasswordField(20)
    val loginBtn: JButton = JButton("Войти")
    val regBtn: JButton = JButton("Зарегестрироваться")

    init {
        title = "Войти"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(300, 200)
        minimumSize = Dimension(200, 200)
        isResizable = false
        setLocationRelativeTo(null)

        val contentPane: Container = contentPane
        contentPane.layout = GridLayout(3, 1)

        val nicknamePanel = JPanel()
        nicknamePanel.add(JLabel("Имя:"))
        nicknamePanel.add(nicknameField)

        val passwordPanel = JPanel()
        passwordPanel.add(JLabel("Пароль:"))
        passwordPanel.add(passwordField)

        val buttonPanel = JPanel()
        buttonPanel.add(loginBtn)
        buttonPanel.add(regBtn)

        contentPane.add(nicknamePanel)
        contentPane.add(passwordPanel)
        contentPane.add(buttonPanel)
    }
}