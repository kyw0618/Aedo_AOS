package com.aedo.my_heaven.util.network

sealed class MyState {
    object Fetched : MyState()
    object Error : MyState()
}