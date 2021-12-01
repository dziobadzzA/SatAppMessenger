package com.satellite.messenger

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.ui.*
import com.google.firebase.auth.FirebaseAuth
import com.satellite.messenger.database.Database
import com.satellite.messenger.databinding.ActivityMainBinding
import com.satellite.messenger.databinding.HeaderMainBinding
import com.satellite.messenger.ui.login.activity.CloseApp
import com.satellite.messenger.ui.profile.GetProfile
import com.satellite.messenger.utils.state.UserModel
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.header_main.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity: AppCompatActivity(), CloseApp, GetProfile {

    private var binding: ActivityMainBinding? = null
    private var headerBinding: HeaderMainBinding? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater, container, false)
        headerBinding = HeaderMainBinding.inflate(layoutInflater, container, false)
        setContentView(binding!!.root)

        binding!!.navView.setupWithNavController(findNavController(R.id.nav_host_fragment_content_main))

        binding!!.navView.getHeaderView(0).imageView.setOnClickListener {
            this.findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_profile)
        }

        if (intent.extras?.get("key") is UserModel) {
            val userText: UserModel = intent.extras?.get("key") as UserModel
            binding!!.navView.getHeaderView(0).textView.text = userText.Mail
        }
        else {
            binding!!.navView.getHeaderView(0).textView.text =
                FirebaseAuth.getInstance().currentUser?.email ?: "unknown user"
        }

        changeImage()

        if (savedInstanceState == null) {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_AUTO
            setTheme(R.style.Theme_SatAppMessenger)
        }
    }

    override fun getAuth():String? = FirebaseAuth.getInstance().currentUser?.email

    override fun changeImage() {

        GlobalScope.launch(Dispatchers.Main) {

            val url = Database.getInstance(application.applicationContext).databaseDao.getProfile().iconUrl

            if (url.isNotEmpty()) {
                binding?.navView?.getHeaderView(0)?.imageView?.setImageBitmap(
                    BitmapFactory.decodeFile(url))
            }

        }

    }

    override fun closeApp() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
    }

   // override fun onDestroy() {
   //     super.onDestroy()
   //     binding = null
  //      headerBinding = null
  //  }

}