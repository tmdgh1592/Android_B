package com.umc.clone_flo.view

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.clone_flo.data.db.SongDatabase
import com.umc.clone_flo.view.adapter.LockerVpAdapter
import com.umc.clone_flo.databinding.FragmentLockerBinding

class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private val information = arrayListOf<String>("저장한 곡", "음악파일")
    private lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val albumAdapter = LockerVpAdapter(this@LockerFragment)
            lockerContentVp.adapter = albumAdapter
            TabLayoutMediator(lockerTb, lockerContentVp) { tab, position ->
                tab.text = information[position]
            }.attach()


        }
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    private fun getJwt(): Int {
        val spf = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf.getInt("jwt", -1)
    }

    private fun initViews() {
        val jwt : Int = getJwt()
        if (jwt == -1) {
            binding.lockerLoginTv.text = "로그인"
            binding.lockerNameTv.text = ""
            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
            }
        } else {
            binding.lockerLoginTv.text = "로그아웃"
            binding.lockerNameTv.text = requireActivity().getSharedPreferences("auth", MODE_PRIVATE).getString("name", "")
            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                logout()
            }
        }
    }

    private fun logout() {
        val pref = requireActivity().getSharedPreferences("auth", MODE_PRIVATE)
        pref.edit().remove("jwt").apply()
    }
}