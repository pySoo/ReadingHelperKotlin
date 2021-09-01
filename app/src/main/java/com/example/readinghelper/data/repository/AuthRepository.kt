package com.example.readinghelper.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.readinghelper.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


internal class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection("users")
    fun firebaseSignInGoogle(googleAuthCredential: AuthCredential?): MutableLiveData<User> {
        val userMutableLiveData: MutableLiveData<User> = MutableLiveData<User>()
        firebaseAuth.signInWithCredential(googleAuthCredential!!).addOnCompleteListener { authTask: Task<AuthResult> ->
            if (authTask.isSuccessful) {
                val isNewUser = authTask.result!!.additionalUserInfo!!.isNewUser
                val firebaseUser = firebaseAuth.currentUser
                // 등록된 유저가 없다면 정보 등록
                if (firebaseUser != null) {
                    val uid = firebaseUser.uid
                    val name = firebaseUser.displayName
                    val user = name?.let { User(uid, it) }
                    if (user != null) {
                        user.isNew = isNewUser
                    }
                    userMutableLiveData.setValue(user)
                }
            }
        }
        return userMutableLiveData
    }

    // 유저를 Firestore에 등록하는 메서드
    fun createUserInFirestore(user: User): MutableLiveData<User> {
        val newUserLiveData: MutableLiveData<User> = MutableLiveData<User>()
        val uidRef = usersRef.document(user.uid)
        uidRef.get().addOnCompleteListener { uidTask: Task<DocumentSnapshot?> ->
            if (uidTask.isSuccessful) {
                val document = uidTask.result
                if (!document!!.exists()) {
                    uidRef.set(user).addOnCompleteListener { userCreationTask: Task<Void?> ->
                        if (userCreationTask.isSuccessful) {
                            user.isCreated = true
                            newUserLiveData.setValue(user)
                        }
                    }
                } else {
                    newUserLiveData.setValue(user)
                }
            }
        }
        return newUserLiveData
    }
}