package com.mengwei.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mengwei.ktea.common.logger


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        ByteString.encodeString("").md5().hex()
        logger("abcd")
    }
}
