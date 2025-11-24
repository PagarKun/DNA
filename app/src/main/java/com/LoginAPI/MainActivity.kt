package com.LoginAPI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dna.DashBoardActivity
import com.example.dna.R
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etUsername = findViewById<TextInputEditText>(R.id.Username)
        val etPassword = findViewById<TextInputEditText>(R.id.Password)

        val btnLogin = findViewById<Button>(R.id.Login)
        btnLogin.setOnClickListener {





            val USERNAME = etUsername.text.toString()
            val PASSWORD = etPassword.text.toString()

            Log.d("LOGIN_INPUT", "USERNAME='$USERNAME' PASSWORD='$PASSWORD'")

            val req = LoginRequest(USERNAME, PASSWORD)

            RetrofitClient.instance.login(req).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {

                    if (response.isSuccessful) {
                        val token = response.body()?.data?.token
                        Log.d("LOGIN","TOKEN:$token")

                        startActivity(Intent(this@MainActivity, DashBoardActivity::class.java))
                        finish()
                    } else {
                        Log.e("LOGIN", "Login Gagal:${response.code()}")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("LOGIN", "ERROR: ${t.message}")
                }
            })
        }




        }
    }