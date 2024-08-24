package com.example.mvvm.data.source.api

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

interface FirebaseApi

class FirebaseApiImpl : FirebaseApi {
    private val firebase: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    val database = firebase.database.reference
}
