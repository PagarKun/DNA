package com.example.dna

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.LoginAPI.MainActivity
import com.LoginAPI.RetrofitClient
import com.fragment.dna.BebanFragment
import com.fragment.dna.KinerjaFragment
import com.fragment.dna.DashBoardFragment
import android.view.animation.AnimationUtils
import com.google.android.material.card.MaterialCardView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import com.RangeAPI.RetrofitClient as RangeRetrofitClient

class DashBoardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)

        val btnKlik = findViewById<ImageView>(R.id.garis3)
        val logout = findViewById<LinearLayout>(R.id.nav_logout)

        logout.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        navView.setNavigationItemSelectedListener(this)

        btnKlik.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        if (savedInstanceState == null) {
            replaceFragment(DashBoardFragment())
            navView.setCheckedItem(R.id.nav_home)
        }

        setupFooterClickLitener()


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> replaceFragment(DashBoardFragment())
            R.id.nav_beban -> replaceFragment(BebanFragment())
//            R.id.nav_laporan -> replaceFragment(KinerjaFragment())
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun setupFooterClickLitener() {
        val cardSync = findViewById<MaterialCardView>(R.id.cardSync)

        if (cardSync == null) {
            Log.e("DashboardActivtity", "cardSync tidak ditemukan di layout")
            return
        }

        cardSync.setOnClickListener {
            Log.d("DashboardActivity", "Memulai Sinkronisasi")

            Toast.makeText(this, "Prosses Sinkronisasi Sedang berlangsung...", Toast.LENGTH_SHORT)
                .show()

            performSync()

            drawerLayout.closeDrawers()
        }
    }

    private fun showCustomNotif(message: String, isSuccess: Boolean) {
        val decorView = window.decorView as ViewGroup
        val rootView = decorView.findViewById<View>(android.R.id.content) as FrameLayout

        val inflater = LayoutInflater.from(this)
        val notifView = inflater.inflate(R.layout.custom_notif_layout, null)

        val notifText = notifView.findViewById<TextView>(R.id.notif_text)
        val notifIcon = notifView.findViewById<ImageView>(R.id.notif_icon)
        val notifRoot = notifView.findViewById<LinearLayout>(R.id.notif_root)
        val closeNotif = notifView.findViewById<ImageView>(R.id.closeNotif)


        notifText.text = message

        if (isSuccess) {
            notifIcon.setImageResource(R.drawable.check)
            notifRoot.setBackgroundResource(R.color.hijau5)
        } else {
            notifIcon.setImageResource(R.drawable.ic_error)
            notifIcon.setBackgroundColor(ContextCompat.getColor(this, R.color.red2))
            notifRoot.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        }

        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
        ).apply {
            gravity = Gravity.TOP
        }

        rootView.addView(notifView, params)

        val slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_top)
        notifView.startAnimation(slideIn)


        closeNotif.setOnClickListener {

            val slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_top)


            slideOut.setAnimationListener(object : Animation.AnimationListener {

                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {

                    rootView.removeView(notifView)
                }
            })

            notifView.startAnimation(slideOut)
        }
    }

    private fun performSync() {

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.syncClickUpData()

                if (response.isSuccessful) {
                    // Tampilkan pesan sukses
                    showCustomNotif("Sinkronisasi Berhasil", true)


                } else {
                    // Tampilkan pesan error
                    val errorMsg = "Gagal sinkronisasi: ${response.code()}"
                    showCustomNotif(errorMsg, false)
                    Log.e("DashBoardActivity", errorMsg)
                }

            } catch (e: Exception) {

                val errorMsg = "Error koneksi: ${e.message}"
                showCustomNotif(errorMsg, false)
                Log.e("DashBoardActivity", errorMsg, e)
            }
        }
    }
}

