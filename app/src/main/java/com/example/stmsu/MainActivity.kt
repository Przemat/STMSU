package com.example.stmsu

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val imageView = findViewById<ImageView>(R.id.responde)

        val up = findViewById<EditText>(R.id.up_lat)
        val left = findViewById<EditText>(R.id.left_lon)
        val down = findViewById<EditText>(R.id.down_lat)
        val right = findViewById<EditText>(R.id.right_lon)

        val min_up = findViewById<Button>(R.id.btn_min_up)
        val max_down = findViewById<Button>(R.id.btn_max_down)
        val min_left = findViewById<Button>(R.id.btn_min_left)
        val max_rigth = findViewById<Button>(R.id.btn_max_right)

        button.setOnClickListener {
            val up_d = up.text.toString().toDouble()
            val down_d = down.text.toString().toDouble()
            val left_d = left.text.toString().toDouble()
            val right_d = right.text.toString().toDouble()
            if (up_d>down_d &&
                left_d<right_d &&
                up_d<=53.12890 &&
                down_d>=53.11604 &&
                left_d>=17.98786 &&
                right_d<=18.00929) {
                val web = WebService()
                web.setImageView(imageView)
                web.setButton(button)
                web.execute(left_d, up_d, right_d, down_d)
            }else{
                Toast.makeText(this, "Wprowadź prawidłowe wartości",
                    Toast.LENGTH_LONG).show();
            }
        }

        min_up.setOnClickListener {
            up.setText(resources.getString(R.string.min_lat))
        }
        max_down.setOnClickListener {
            down.setText(resources.getString(R.string.max_lat))
        }
        min_left.setOnClickListener {
            left.setText(resources.getString(R.string.min_lon))
        }
        max_rigth.setOnClickListener {
            right.setText(resources.getString(R.string.max_lon))
        }


    }
}