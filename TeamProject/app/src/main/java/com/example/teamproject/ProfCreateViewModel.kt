package com.example.teamproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ProfCreateViewModel : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: MutableLiveData<String> = _name

    private val _degree = MutableLiveData<String>()
    val degree: MutableLiveData<String> = _degree

    private val _university = MutableLiveData<String>()
    val university: MutableLiveData<String> = _university

    private val _field = MutableLiveData<String>()
    val field: MutableLiveData<String> = _field

    private val _email = MutableLiveData<String>()
    val email: MutableLiveData<String> = _email

    private val _lab = MutableLiveData<String>()
    val lab: MutableLiveData<String> = _lab
}