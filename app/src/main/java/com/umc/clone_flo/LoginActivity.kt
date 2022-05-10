package com.umc.clone_flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.clone_flo.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginSignInBtn.setOnClickListener {
            login()
        }
    }

    private fun login() {
        if (binding.loginIdEt.text.toString()
                .isEmpty() || binding.loginDirectInputEt.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.loginPasswordEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email =
            binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val password = binding.loginPasswordEt.text.toString()

        val songDB = SongDatabase.getInstance(this)!!

        val user = songDB.userDao().getUser(email, password)

        user?.let {
            Log.d("LOGIN_ACT/GET_USER", "userId: ${user.id}, $user")
            saveJwt(it.id)
            saveName(it.name)
            startMainActivity()
        } ?: Toast.makeText(this, "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun saveJwt(jwt: Int) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt", jwt)
        editor.apply()
    }

    private fun saveName(userName: String) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("name", userName)
        editor.apply()
    }

}