package com.develoware.skyvape_pos

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.develoware.skyvape_pos.databinding.ActivityPosBinding
import com.google.android.material.tabs.TabLayoutMediator

class PosActivity : AppCompatActivity() {
    private var mBinding: ActivityPosBinding? = null
    private val binding get() = mBinding!!

    val tabTitles = listOf<String>("입호흡 기기", "폐호흡 기기")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.posProductList.adapter = ViewpagerFragmentAdapter(this)
        TabLayoutMediator(binding.posCategoryLayout, binding.posProductList, {tab, position -> tab.text = tabTitles[position]}).attach()
    }
}