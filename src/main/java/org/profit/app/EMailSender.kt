package org.profit.app

import java.util.*
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

/**
 * 邮件发送者
 *
 * @author TangYing
 */
object EMailSender {

    private val props = Properties()
    private val auth = MyAuthenticator()

    init {
        //设置邮件服务器地址，连接超时时限等信息
        props["mail.smtp.host"] = "smtp.126.com"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.connectiontimeout"] = "10000"
        props["mail.smtp.timeout"] = "25000"
    }

    /**
     * 邮件发送
     *
     * @param receiver
     * @param subject
     * @param content
     */
    fun send(receiver: String, subject: String, content: String) {
        if (receiver == "") {
            return
        }

        //创建缺省的session对象
        val session = Session.getDefaultInstance(props, auth)

        //创建message对象
        val msg = MimeMessage(session)

        //设置发件人和收件人
        try {
            // 设置发件人
            val internetAddress = InternetAddress("xlzxbyd@126.com")
            internetAddress.personal = "Stock Profit Result"
            msg.setFrom(internetAddress)

            // 设置收件人
            val addressTo = InternetAddress(receiver)
            msg.setRecipient(Message.RecipientType.TO, addressTo)

            //设置邮件主题
            msg.subject = subject
            msg.setText(content)

            // 设置传输协议
            val transport = session.getTransport("smtp")
            transport.connect("smtp.126.com", "xlzxbyd@126.com", "TYOKUAGAOPWIGNPM")
            transport.sendMessage(msg, msg.allRecipients)
            transport.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class MyAuthenticator: Authenticator() {

        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication("oookay_sao2", "WASWYARBXAVGJZUN")
        }
    }
}
