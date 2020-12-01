package com.codemobiles.myandroid

import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.codemobiles.myandroid.databinding.ActivityMainBinding
import com.codemobiles.myandroid.utilities.PREFS_TOKEN
import com.codemobiles.myandroid.utilities.PREFS_USERNAME
import com.pixplicity.easyprefs.library.Prefs

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val token = Prefs.getString(PREFS_TOKEN, "")
        if (token != "") {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupEventWidget()
        }
    }

    private fun setupEventWidget() {
        binding.loginButton.setOnClickListener {
            val username =  binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

//            Toast.makeText(applicationContext, "$username ,$password", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
            finish()

            Prefs.putString(PREFS_TOKEN, "dsfadsfaweoifjllk")
            Prefs.putString(PREFS_USERNAME, username)
        }

        val username = Prefs.getString(PREFS_USERNAME, "")
        if(username != "") {
            binding.usernameEditText.setText(username)
        }
    }
}

// 80%
// Build number (about, system)  // click many many time
// Developer option (about, system -> advance option) developer option -> on
// Enable usb degub mode ->  debugging -> on , USB debugging -> on
