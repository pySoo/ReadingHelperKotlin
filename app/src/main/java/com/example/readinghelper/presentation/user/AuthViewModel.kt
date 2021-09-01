package com.example.readinghelper.presentation.user

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.readinghelper.presentation.common.Observer
import com.example.readinghelper.util.MyUtils
import com.google.firebase.auth.FirebaseAuth


class LoginViewModel : BaseObservable() {
    @get:Bindable
    var isAuthDone = false
        set(authDone) {
            field = authDone
            notifyPropertyChanged(BR.authDone)
        }

    @get:Bindable
    var isAuthInProgress = false
        set(authInProgress) {
            field = authInProgress
            notifyPropertyChanged(BR.authInProgress)
        }
    var observers: ArrayList<Observer> = ArrayList()
    fun firebaseAnonymousAuth() {
        isAuthInProgress = true
        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener { task ->
                    isAuthInProgress = false
                    if (!task.isSuccessful) {
                        notifyObservers(MyUtils.SHOW_TOAST, MyUtils.MESSAGE_AUTHENTICATION_FAILED)
                    } else {
                        isAuthDone = true
                    }
                }
    }

    fun invalidateRoomName(roomName: String) {
        if (roomName.trim { it <= ' ' }.isEmpty()) {
            notifyObservers(MyUtils.SHOW_TOAST, MyUtils.MESSAGE_INVALIDE_ROOM_NAME)
        } else {
            notifyObservers(MyUtils.OPEN_ACTIVITY, roomName)
        }
    }

    fun addObserver(client: Observer) {
        if (!observers.contains(client)) {
            observers.add(client)
        }
    }

    fun removeObserver(clientToRemove: Observer) {
        if (observers.contains(clientToRemove)) {
            observers.remove(clientToRemove)
        }
    }

    fun notifyObservers(eventType: Int, message: String?) {
        for (i in 0 until observers.size) {
            observers[i].onObserve(eventType, message)
        }
    }

}