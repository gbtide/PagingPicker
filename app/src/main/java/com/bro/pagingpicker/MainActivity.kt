package com.bro.pagingpicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bro.pagingpicker.model.Cons
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_text.setText(Cons.SUB_MODULE)
    }
}