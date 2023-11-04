package ru.qirces.net

import java.awt.Container
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.*

class RegWindow : JFrame() {
    val nicknameField: JTextField = JTextField(20)
    val passwordField: JPasswordField = JPasswordField(20)
    val repeatPasswordField: JPasswordField = JPasswordField(20)
    val registerBtn: JButton = JButton("Зарегистрироваться")
    val loginBtn: JButton = JButton("Войти")

    init {
        title = "Регистрация"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(500, 250)
        minimumSize = Dimension(500, 200)
        isResizable = false
        setLocationRelativeTo(null)

        val contentPane: Container = contentPane
        contentPane.layout = GridLayout(4, 1)

        val nicknamePanel = JPanel()
        nicknamePanel.add(JLabel("Имя:"))
        nicknamePanel.add(nicknameField)

        val passwordPanel = JPanel()
        passwordPanel.add(JLabel("Пароль:"))
        passwordPanel.add(passwordField)

        val repeatPasswordPanel = JPanel()
        repeatPasswordPanel.add(JLabel("Повторите пароль:"))
        repeatPasswordPanel.add(repeatPasswordField)

        val buttonPanel = JPanel()
        buttonPanel.add(registerBtn)
        buttonPanel.add(loginBtn)

        contentPane.add(nicknamePanel)
        contentPane.add(passwordPanel)
        contentPane.add(repeatPasswordPanel)
        contentPane.add(buttonPanel)
    }
}