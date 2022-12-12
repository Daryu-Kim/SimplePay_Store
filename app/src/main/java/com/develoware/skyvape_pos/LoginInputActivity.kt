package com.develoware.skyvape_pos

import android.R
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.develoware.skyvape_pos.databinding.ActivityLoginInputBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat


class LoginInputActivity : AppCompatActivity() {
    private var mBinding: ActivityLoginInputBinding? = null
    private val binding get() = mBinding!!
    val db = Firebase.firestore

    val basket_data = hashMapOf(
        "total_price" to 0,
        "discount_price" to 0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()

        binding.loginInputBtnLayout.setOnClickListener {
            db.collection("Eunhaeng_Basket")
                .get()
                .addOnSuccessListener { it ->
                    it.forEach {
                        it.reference.delete()
                    }
                }

            db.collection("Eunhaeng_Basket")
                .document("basket_data")
                .set(basket_data)

            val intent = Intent(this, PosActivity::class.java)
            startActivity(intent)
        }

    }
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())

            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}