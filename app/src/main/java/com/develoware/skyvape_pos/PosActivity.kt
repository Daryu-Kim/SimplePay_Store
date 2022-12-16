package com.develoware.skyvape_pos

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.develoware.skyvape_pos.databinding.ActivityPosBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.DecimalFormat

class PosActivity : AppCompatActivity() {
    private var mBinding: ActivityPosBinding? = null
    private val binding get() = mBinding!!
    var total_price = 0

    val db = Firebase.firestore

    var category: String = "mouth_device"

    var basketData = arrayListOf<BasketData>()

    val basket_data = hashMapOf(
        "total_price" to 0,
        "discount_price" to 0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.posProductList.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 4)
            adapter = RecyclerViewAdapter()
        }

        binding.posCategoryMouthDevice.setOnClickListener {
            category = it.tag as String
            binding.posProductList.apply {
                adapter = RecyclerViewAdapter()
            }
        }

        binding.posCategoryLungDevice.setOnClickListener {
            category = it.tag as String
            binding.posProductList.apply {
                adapter = RecyclerViewAdapter()
            }
        }

        binding.posCategoryOnceDevice.setOnClickListener {
            category = it.tag as String
            binding.posProductList.apply {
                adapter = RecyclerViewAdapter()
            }
        }

        binding.posCategoryMouthLiquid.setOnClickListener {
            category = it.tag as String
            binding.posProductList.apply {
                adapter = RecyclerViewAdapter()
            }
        }

        binding.posCategoryLungLiquid.setOnClickListener {
            category = it.tag as String
            binding.posProductList.apply {
                adapter = RecyclerViewAdapter()
            }
        }

        binding.posCategoryMouthAtomizer.setOnClickListener {
            category = it.tag as String
            binding.posProductList.apply {
                adapter = RecyclerViewAdapter()
            }
        }

        binding.posCategoryLungAtomizer.setOnClickListener {
            category = it.tag as String
            binding.posProductList.apply {
                adapter = RecyclerViewAdapter()
            }
        }

        binding.posCategoryOnceAtomizer.setOnClickListener {
            category = it.tag as String
            binding.posProductList.apply {
                adapter = RecyclerViewAdapter()
            }
        }

        binding.posCategoryAccesory.setOnClickListener {
            category = it.tag as String
            binding.posProductList.apply {
                adapter = RecyclerViewAdapter()
            }
        }

        binding.posBasketResetBtn.setOnClickListener {
            basketReset("Eunhaeng")
            binding.posBasketTotalPrice.text = "${DecimalFormat("#,###").format(0)}원"
        }

        binding.posBasketDiscountBtn.setOnClickListener {
            if (binding.posBasketTotalPrice.text == "0원") {
                Toast.makeText(this, "상품이 담기지 않았습니다!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, PaymentActivity::class.java)
                startActivity(intent)
            }
        }

        binding.posBasketPayBtn.setOnClickListener {
            if (binding.posBasketTotalPrice.text == "0원") {
                Toast.makeText(this, "상품이 담기지 않았습니다!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, PaymentActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        basketData.clear()

        binding.posBasketRv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@PosActivity)
            adapter = BasketAdapter("Eunhaeng")
        }

        binding.posBasketTotalPrice.apply {
            text = "${DecimalFormat("#,###").format(total_price)}원"
        }


    }

    override fun onBackPressed() {
        dialog()
    }

    override fun onResume() {
        super.onResume()

    }

    fun dialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("알림")
            .setMessage("처음으로 돌아가시겠습니까?\n처음으로 돌아가시면 상품을 새로 담아야 합니다!")
            .setPositiveButton("확인",DialogInterface.OnClickListener{ dialog, id ->
                finish()
            })
            .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, id ->

            })

        builder.show()
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

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var productData: ArrayList<ProductData> = arrayListOf()

        init {
            db.collection(category)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    productData.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(ProductData::class.java)
                        productData.add(item!!)
                    }
                    notifyDataSetChanged()
                }
        }

        // xml파일을 inflate하여 ViewHolder를 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
            return ViewHolder(view)
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {}

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = (holder as ViewHolder).itemView

            var storage: FirebaseStorage = FirebaseStorage.getInstance()
            var storageRef: StorageReference =
                storage.reference.child("${category}/${productData[position].img.toString()}")

            val name_tv = viewHolder.findViewById<TextView>(R.id.product_name)
            val img_iv = viewHolder.findViewById<ImageView>(R.id.product_img)
            val price_tv = viewHolder.findViewById<TextView>(R.id.product_price)

            name_tv.text = productData[position].name
            price_tv.text = "${DecimalFormat("#,###").format(productData[position].price)}원"
            storageRef.downloadUrl.addOnSuccessListener {
                Glide.with(this@PosActivity)
                    .load(it.toString())
                    .centerCrop()
                    .into(img_iv)
            }

                .addOnFailureListener {
                    Glide.with(this@PosActivity)
                        .load(R.drawable.logo)
                        .centerCrop()
                        .into(img_iv)
                }

            holder.itemView.setOnClickListener {
                db.collection("Eunhaeng_Basket")
                    .document(productData[position].name.toString())
                    .get()
                    .addOnSuccessListener { result ->
                        var temp = result.get("name")
                        if (temp != null) {
                            Toast.makeText(this@PosActivity, "이미 추가된 상품입니다!", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            inputBasket(position)
                        }
                    }
                    .addOnFailureListener {
                        inputBasket(position)
                    }



            }
        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount(): Int {
            return productData.size
        }

        fun inputBasket(position: Int) {
            db.collection("Eunhaeng_Basket")
                .document(productData[position].name.toString())
                .set(BasketData(productData[position].name, 1, productData[position].price))
                .addOnSuccessListener {
                    Toast.makeText(this@PosActivity, "장바구니에 담았습니다!", Toast.LENGTH_SHORT).show()
                    total_price += productData[position].price!!
                    db.collection("Eunhaeng_Basket_Data")
                        .document("basket_data")
                        .update("total_price", total_price)
                    binding.posBasketTotalPrice.text = "${DecimalFormat("#,###").format(total_price)}원"
                }
                .addOnFailureListener {
                    Toast.makeText(this@PosActivity, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }
    }
}