package com.example.dna

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dna.TeamsResponse
import com.example.dna.RetrofitClient
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

        val btnLogin = findViewById<Button>(R.id.Login)
        btnLogin.setOnClickListener {
            val intent = Intent(this, DashBoardActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 🔥 Retrofit request ke API Golang
        RetrofitClient.instance.getTeams().enqueue(object : Callback<TeamsResponse> {
            override fun onResponse(call: Call<TeamsResponse>, response: Response<TeamsResponse>) {
                if (response.isSuccessful) {
                    val projects = response.body()?.projects ?: emptyList()
                    for (project in projects) {
                        Log.d("API_RESPONSE", "Project: ${project.name}")
                        project.tasks?.forEach { task ->
                            Log.d("API_RESPONSE", "- Task: ${task.name} (status: ${task.status})")
                        }
                    }
                } else {
                    Log.e("API_RESPONSE", "Error response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TeamsResponse>, t: Throwable) {
                Log.e("API_RESPONSE", "Failed to connect: ${t.message}")
            }
        })
    }
}