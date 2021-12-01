package com.satellite.messenger.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.satellite.messenger.databinding.FragmentLoginBinding
import com.satellite.messenger.di.DaggerAppComponent
import com.satellite.messenger.ui.login.activity.StartApp

class LoginFragment: Fragment() {

    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var loginViewModel: LoginViewModel

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var mAuthListener: FirebaseAuth.AuthStateListener? = null

    private lateinit var interface_obmen:StartApp

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModelFactory = DaggerAppComponent.create().viewFactory()
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        activity?.let { loginViewModel.repository.setApp(it.application) }

        loginViewModel.inizialisation()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.emailSignInButton.setOnClickListener {

            if (loginViewModel.validateForm(binding.fieldEmail.text.toString(), binding.fieldPassword.text.toString()).state) {

                activity?.let { result ->
                    loginViewModel.mAuth!!.value?.signInWithEmailAndPassword(loginViewModel.user, loginViewModel.password)?.addOnCompleteListener(result) {
                            task ->
                            if (!task.isSuccessful)
                                stateLoginPrint("Вы не авторизованы")
                            else {
                                loginViewModel.getCorrectByte(loginViewModel.mAuth!!.value?.currentUser?.uid.toString())
                                replaceState(true)
                            }
                    }
                }
            }
        }

        binding.emailCreateAccountButton.setOnClickListener {

            if (loginViewModel.validateForm(binding.fieldEmail.text.toString(), binding.fieldPassword.text.toString()).state) {
                activity?.let { create ->
                    loginViewModel.mAuth!!.value?.createUserWithEmailAndPassword(loginViewModel.user, loginViewModel.password)?.addOnCompleteListener(create) {
                            task ->
                            if (!task.isSuccessful)
                                stateLoginPrint("Вы не авторизованы или пользователь уже существует")
                            else {
                                stateLoginPrint("Проверьте почту")
                                loginViewModel.mFirebaseUser?.value?.sendEmailVerification()
                                loginViewModel.addBytesFirebase(
                                    loginViewModel.initKey(),
                                    loginViewModel.mAuth!!.value?.currentUser?.uid.toString()
                                )
                            }
                    }
                }
            }
        }

        loginViewModel.stateLogin?.observe(viewLifecycleOwner, {

            if (!it.state){
                binding.fieldEmail.error = it.emailError
                binding.fieldPassword.error = it.passwordError
            }

        })

        loginViewModel.updateFirebaseAuth(FirebaseAuth.getInstance())

        loginViewModel.correctByte?.observe(
            viewLifecycleOwner, {

                loginViewModel.getModel(loginViewModel.mAuth!!.value?.currentUser?.email, "")

                if (loginViewModel.mFirebaseUser?.value?.isEmailVerified == true)
                    successIn()
                else {
                    stateLoginPrint("Ошибка подключения сети и авторизации почты")
                }
            })


        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            loginViewModel.updateFirebaseUser(firebaseAuth.currentUser)
        }

        if (loginViewModel.mAuth?.value != null) {
            loginViewModel.getCorrectByte(loginViewModel.mAuth!!.value?.currentUser?.uid.toString())
            replaceState(true)
        }

        loginViewModel.liveState?.observe(viewLifecycleOwner, {
            replaceState(it)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun stateLoginPrint(text:String) {
        Toast.makeText(activity?.applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        loginViewModel.mAuth!!.value?.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null)
            loginViewModel.mAuth!!.value?.removeAuthStateListener(mAuthListener!!)
    }

    private fun successIn() {
        if (loginViewModel.correctByte?.value == true) {

            if (loginViewModel.modelIn.Mail.isEmpty())
                return

            interface_obmen.startApp(loginViewModel.modelIn)
        }
    }

   override fun onAttach(context: Context) {
       super.onAttach(context)
       if (context is StartApp)
           interface_obmen = context
   }

    private fun replaceState(state:Boolean) {
        binding.relativeLayout.isVisible = !state
        binding.progressBar.isVisible = state
        binding.relativeLayout.isClickable = !state
        binding.progressBar.isClickable = state
    }

}