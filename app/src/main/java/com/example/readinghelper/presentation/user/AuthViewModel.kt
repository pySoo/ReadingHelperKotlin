package com.example.readinghelper.presentation.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.readinghelper.data.model.User
import com.example.readinghelper.data.repository.AuthRepository
import com.google.firebase.auth.AuthCredential


class AuthViewModel(application: Application?) : AndroidViewModel(application!!) {
    private val authRepository: AuthRepository
    var authUserLiveData: LiveData<User>? = null
    var createdUserLiveData: LiveData<User>? = null
    fun signInWithGoogle(googleAuthCredential: AuthCredential?) {
        authUserLiveData = authRepository.firebaseSignInGoogle(googleAuthCredential)
        System.out.println("ssssssAuthUser" + authUserLiveData.toString())
    }

    fun createUser(authenticatedUser: User?) {
        createdUserLiveData = authenticatedUser?.let { authRepository.createUserInFirestore(it) }
        System.out.println("ssssssCreatedUser" + createdUserLiveData.toString())
    }
    fun getUserData(): LiveData<User>? {
        return authUserLiveData
    }

    init {
        authRepository = AuthRepository()
    }
}