package com.umc.clone_flo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.clone_flo.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener {
            signUp()
        }
    }

    private fun getUser(): User {
        val email: String =
            binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val pwd: String = binding.signUpPasswordEt.text.toString()
        val name = binding.signUpNameEt.text.toString()
        return User(email, pwd, name)
    }

    private fun signUp() {
        if (binding.signUpNameEt.text.toString().isEmpty()) {
            Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signUpIdEt.text.toString()
                .isEmpty() || binding.signUpDirectInputEt.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signUpPasswordEt.text.toString() != binding.signUpPasswordCheckEt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())

        val users = userDB.userDao().getUsers()
        Log.d("SIGNUPACT", users.toString())

        finish()
    }
}