package com.umc.clone_flo.view

import com.umc.clone_flo.data.entity.response.Result

interface LoginView {
    fun onLoginSuccess(code : Int, result : Result)
    fun onLoginFailure()
}