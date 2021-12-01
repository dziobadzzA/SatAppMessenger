package com.satellite.messenger

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.satellite.messenger.databinding.LoginActivityBinding
import com.satellite.messenger.ui.login.LoginFragment
import com.satellite.messenger.ui.login.activity.StartApp
import com.satellite.messenger.utils.state.UserModel

class LoginActivity: AppCompatActivity(), StartApp {

    private lateinit var binding:LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.login, LoginFragment(), "").commit()
        }

    }

    override fun startApp(userModel: UserModel) {
        val intentNav = Intent(this@LoginActivity, MainActivity()::class.java)
        intentNav.putExtra("key", userModel)
        finishAffinity()
        startActivity(intentNav)
    }

}