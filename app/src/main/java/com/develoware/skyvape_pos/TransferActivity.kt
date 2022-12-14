package com.develoware.skyvape_pos

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.develoware.skyvape_pos.databinding.ActivityPaymentBinding
import com.develoware.skyvape_pos.databinding.ActivitySplashBinding
import com.develoware.skyvape_pos.databinding.ActivityTransferBinding

class TransferActivity : AppCompatActivity() {
    private var mBinding: ActivityTransferBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.transferLayout.setOnClickListener {
            val intent = Intent(this, FinishActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {

    }
}