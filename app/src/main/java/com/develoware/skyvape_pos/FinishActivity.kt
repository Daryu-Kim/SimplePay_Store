package com.develoware.skyvape_pos

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.develoware.skyvape_pos.databinding.ActivityFinishBinding
import com.develoware.skyvape_pos.databinding.ActivityPaymentBinding
import com.develoware.skyvape_pos.databinding.ActivitySplashBinding
import com.develoware.skyvape_pos.databinding.ActivityTransferBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FinishActivity : AppCompatActivity() {
    private var mBinding: ActivityFinishBinding? = null
    private val binding get() = mBinding!!

    val basket_data = hashMapOf(
        "total_price" to 0,
        "discount_price" to 0
    )

    val db = Firebase.firestore

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val current_timestamp = LocalDateTime.now()
        val format_timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val receipt_timestamp = current_timestamp.format(format_timestamp)
        val receipt_data = hashMapOf(
            "timestamp" to receipt_timestamp
        )

        db.collection("Eunhaeng_Receipt")
            .document(receipt_timestamp)
            .set(receipt_data)
        // basket_data에 있는 데이터를 Firestore의 Eunhaeng_Receipt -> 날짜 document에 넣어주기.

        Handler().postDelayed({
            val intent = Intent(this, PosActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        },DURATION)
    }

    override fun onDestroy() {
        super.onDestroy()
        basketReset("Eunhaeng")
    }

    companion object {
        private const val DURATION : Long = 3000
    }

    override fun onBackPressed() {
    }

    private fun basketReset(key : String) {
        db.collection("${key}_Basket")
            .get()
            .addOnSuccessListener { it ->
                it.forEach {
                    it.reference.delete()
                }
            }

        db.collection("${key}_Basket_Data")
            .document("basket_data")
            .set(basket_data)
    }
}