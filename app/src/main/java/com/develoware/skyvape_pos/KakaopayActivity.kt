package com.develoware.skyvape_pos

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.develoware.skyvape_pos.databinding.ActivityKakaopayBinding
import com.develoware.skyvape_pos.databinding.ActivityPaymentBinding
import com.develoware.skyvape_pos.databinding.ActivitySplashBinding
import com.develoware.skyvape_pos.databinding.ActivityTransferBinding

class KakaopayActivity : AppCompatActivity() {
    private var mBinding: ActivityKakaopayBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityKakaopayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.kakaopayLayout.setOnClickListener {
            val intent = Intent(this, FinishActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {

    }
}