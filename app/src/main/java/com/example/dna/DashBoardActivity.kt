package com.example.dna

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.LoginAPI.MainActivity
import com.fragment.dna.BebanFragment
import com.fragment.dna.KinerjaFragment
import com.MemberAPI.DashBoardFragment
import com.google.android.material.navigation.NavigationView

class DashBoardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {



    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)

        val btnKlik = findViewById<ImageView>(R.id.garis3)

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


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> replaceFragment(DashBoardFragment())
            R.id.nav_beban -> replaceFragment(BebanFragment())
            R.id.nav_laporan -> replaceFragment(KinerjaFragment())
            R.id.nav_logout -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }


}

