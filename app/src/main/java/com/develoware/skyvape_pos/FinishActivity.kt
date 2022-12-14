package com.develoware.skyvape_pos

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.develoware.skyvape_pos.databinding.ActivityFinishBinding
import com.develoware.skyvape_pos.databinding.ActivityPaymentBinding
import com.develoware.skyvape_pos.databinding.ActivitySplashBinding
import com.develoware.skyvape_pos.databinding.ActivityTransferBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FinishActivity : AppCompatActivity() {
    private var mBinding: ActivityFinishBinding? = null
    private val binding get() = mBinding!!

    val basket_data = hashMapOf(
        "total_price" to 0,
        "discount_price" to 0
    )

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            basketReset("Eunhaeng")
            val intent = Intent(this, PosActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        },DURATION)
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