package ru.stiznt.mapinkotlin.ui.chat

import android.os.Bundle

import android.content.Intent
import android.net.Uri
import android.widget.TextView
import android.widget.ImageButton
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import java.lang.Exception


class ChatFragment : Fragment() {

    private var telega: ImageButton? = null
    private var mail: ImageButton? = null
    private var vk: ImageButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View {

        val root: View = inflater.inflate(ru.stiznt.mapinkotlin.R.layout.fragment_chat, container, false)
        telega = root.findViewById(ru.stiznt.mapinkotlin.R.id.telega)
        mail = root.findViewById(ru.stiznt.mapinkotlin.R.id.mail)
        vk = root.findViewById(ru.stiznt.mapinkotlin.R.id.vk)
        telega!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                try {
                    val telegram =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/denis_bratusev"))
                    startActivity(telegram)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
        mail!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                try {
                    val mail =
                        Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "bratusevd@mail.ru"))
                    startActivity(mail)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
        vk!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                try {
                    val vk = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.me/denis_bratusev"))
                    startActivity(vk)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
        return root
    }
}
