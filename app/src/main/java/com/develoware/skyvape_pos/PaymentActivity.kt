package com.develoware.skyvape_pos

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.develoware.skyvape_pos.databinding.ActivityPaymentBinding
import com.develoware.skyvape_pos.databinding.ActivitySplashBinding

class PaymentActivity : AppCompatActivity() {
    private var mBinding: ActivityPaymentBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.paymentContentCashLayout.setOnClickListener {
            Toast.makeText(this, "현금거래", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FinishActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.paymentContentTransferLayout.setOnClickListener {
            Toast.makeText(this, "계좌이체", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TransferActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.paymentContentCreditcardLayout.setOnClickListener {
            Toast.makeText(this, "신용카드", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FinishActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.paymentContentKakaopayLayout.setOnClickListener {
            Toast.makeText(this, "카카오페이", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, KakaopayActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {

    }
}