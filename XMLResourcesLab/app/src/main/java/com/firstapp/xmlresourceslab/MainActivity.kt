package com.firstapp.xmlresourceslab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.button)
        Log.i("DEBUG_TAG", "MainActivity started successfully")
        Log.d("DEBUG_TAG", "Debugging message");
        Log.w("DEBUG_TAG", "Warning message");
        Log.e("DEBUG_TAG", "Error message");





    }
}
