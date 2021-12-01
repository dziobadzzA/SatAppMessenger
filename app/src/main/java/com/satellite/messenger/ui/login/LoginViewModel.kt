package com.satellite.messenger.ui.login

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.satellite.messenger.database.Repository
import com.satellite.messenger.database.entity.Auth
import com.satellite.messenger.database.entity.Profile
import com.satellite.messenger.utils.array
import com.satellite.messenger.utils.state.StateLogin
import com.satellite.messenger.utils.state.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import kotlin.math.abs

class LoginViewModel @Inject constructor(val repository: Repository): ViewModel() {

    // auth
    private var _mAuth: MutableLiveData<FirebaseAuth>? = null
    val mAuth: LiveData<FirebaseAuth>? get() = _mAuth

    // firebase user
    private var _mFirebaseUser:MutableLiveData<FirebaseUser>? = null
    val mFirebaseUser: LiveData<FirebaseUser>? get() = _mFirebaseUser

    var password: String = ""
    var user: String = ""

    private var _stateLogin: MutableLiveData<StateLogin>? = null
    val stateLogin: LiveData<StateLogin>? get() = _stateLogin!!

    val modelIn: UserModel = UserModel()

    private var _correctByte: MutableLiveData<Boolean>? = null
    val correctByte: LiveData<Boolean>? get() = _correctByte!!

    private var _liveState: MutableLiveData<Boolean>? = null
    val liveState: LiveData<Boolean>? get() = _liveState!!

    // auth profile
    var initKey: ByteArray? = null
    var key: SecretKeySpec? = null

    init {
        _stateLogin = MutableLiveData<StateLogin>()
        _correctByte = MutableLiveData<Boolean>()
        _mAuth = MutableLiveData<FirebaseAuth>()
        _mFirebaseUser = MutableLiveData<FirebaseUser>()
        _liveState = MutableLiveData<Boolean>()
    }

    fun validateForm(email: String, password: String): StateLogin {

        val valid = StateLogin()

        if ((TextUtils.isEmpty(email)) and (email.split('@').size == 2)) {
            valid.emailError = "Required."
            valid.state = false
        }
        else {
            valid.state = true
            valid.emailError = null
            this.user = email
        }

        if (TextUtils.isEmpty(password)) {
            valid.passwordError = "Required."
            valid.state = false
        }
        else {
            valid.state = true
            valid.passwordError = null
            this.password = password
        }

        return valid
    }

    fun updateFirebaseUser(user: FirebaseUser?) {
        _mFirebaseUser?.value = user
    }

    fun updateFirebaseAuth(auth: FirebaseAuth?) {
        _mAuth?.value = auth
    }

    fun getModel(email: String?, link: String?)  {
        if (!email.isNullOrEmpty())
            modelIn.Mail = email

        if (!link.isNullOrEmpty())
            modelIn.link = link
    }

    fun inizialisation() {
        GlobalScope.launch(Dispatchers.Main) {
            val auth = repository.getRoomDatabase().getAuth()
            if (auth != null) {
                key = SecretKeySpec(auth.keyAuth, "AES")
                initKey = auth.initKey
            }
        }
    }

    fun initKey(): String? {

        val tempKey:SecureRandom

        if (initKey == null) {
            tempKey = SecureRandom.getInstance("SHA1PRNG") // надо сохранить
            initKey = tempKey.toString().toByteArray()
        }

        if (key == null)
            try {
                val kg: KeyGenerator = KeyGenerator.getInstance("AES")
                kg.init(256, SecureRandom.getInstance("SHA1PRNG"))
                val saveKey = kg.generateKey().encoded
                key = SecretKeySpec(saveKey, "AES")

                GlobalScope.launch(Dispatchers.Main) {
                   repository.getRoomDatabase().insertAuth(Auth(Id=1L, initKey = initKey, keyAuth = saveKey))
                   repository.getRoomDatabase().insertProfile(Profile(Id=1L))
                }

            } catch (e: Exception) {
                Log.e("Crypto", "AES secret key spec error")
            }

        var encodedBytes: ByteArray? = null

        try {
            val c: Cipher = Cipher.getInstance("AES")
            c.init(Cipher.ENCRYPT_MODE, key)
            encodedBytes = c.doFinal(initKey)
        } catch (e: Exception) {
            Log.e("Crypto", "AES encryption error")
        }

        var test:String? = null

        if (encodedBytes != null) {
            test = ""
            for (i in encodedBytes.indices)
                test += array[abs(encodedBytes[i].toInt())]
        }

        return test
    }

    fun addBytesFirebase(test:String?, string: String) {

        if (!test.isNullOrEmpty()) {
            FirebaseDatabase.getInstance().reference.child(string).push().setValue(
                test
            )
        }
    }

    fun getCorrectByte(string: String) {
        GlobalScope.launch(Dispatchers.Main) {

            FirebaseDatabase.getInstance().reference.child(string).get().addOnSuccessListener {
                if (it.value is HashMap<*, *>) {
                    val a = it.value as HashMap<*, *>
                    _correctByte?.value = a.containsValue(initKey())
                }
                else
                    badInternet()

            }.addOnFailureListener {
                badInternet()
            }

        }
    }

    private fun badInternet() {
        _correctByte?.value = false
        _liveState?.value = false
    }
}