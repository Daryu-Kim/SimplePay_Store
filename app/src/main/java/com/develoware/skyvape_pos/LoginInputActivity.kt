package com.develoware.skyvape_pos

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.develoware.skyvape_pos.databinding.ActivityLoginInputBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


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

            val intent = Intent(this, PosActivity::class.java)

            if (binding.loginInputIdEunhaeng.isChecked) {
                val id = binding.loginInputIdEunhaeng
                if (binding.loginInputPwEt.text.toString() == id.hint) {
                    saveKey(id.tag.toString())
                    basketReset(id.tag.toString())
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "비밀번호가 맞지 않습니다!", Toast.LENGTH_SHORT).show()
                }
            } else if (binding.loginInputIdYongjeon.isChecked) {
                val id = binding.loginInputIdYongjeon
                if (binding.loginInputPwEt.text.toString() == id.hint) {
                    saveKey(id.tag.toString())
                    basketReset(id.tag.toString())
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "비밀번호가 맞지 않습니다!", Toast.LENGTH_SHORT).show()
                }
            } else if (binding.loginInputIdOryu.isChecked) {
                val id = binding.loginInputIdOryu
                if (binding.loginInputPwEt.text.toString() == id.hint) {
                    saveKey(id.tag.toString())
                    basketReset(id.tag.toString())
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "비밀번호가 맞지 않습니다!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "지점을 선택해주세요!", Toast.LENGTH_SHORT).show()
            }

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

    private fun saveKey(key : String) {
        val pref = getSharedPreferences("key", 0)
        val edit = pref.edit()

        edit.putString("key", key)
        edit.apply()
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