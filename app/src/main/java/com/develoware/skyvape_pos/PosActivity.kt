package com.develoware.skyvape_pos

import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.develoware.skyvape_pos.databinding.ActivityPosBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.DecimalFormat

class PosActivity : AppCompatActivity() {
    private var mBinding: ActivityPosBinding? = null
    private val binding get() = mBinding!!

    val db = Firebase.firestore

    var category: String = "mouth_device"

    var basketData = arrayListOf<BasketData>()

    //val tabTitles = listOf<String>("입호흡 기기", "폐호흡 기기")

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

        basketData.clear()

        binding.posBasketLv.adapter = BasketAdapter(basketData)

        //binding.posProductList.adapter = ViewpagerFragmentAdapter(this)
        //TabLayoutMediator(binding.posCategoryLayout, binding.posProductList, {tab, position -> tab.text = tabTitles[position]}).attach()


    }
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var productData: ArrayList<ProductData> = arrayListOf()
        var basketData: ArrayList<BasketData> = arrayListOf()

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
            val count_tv = viewHolder.findViewById<TextView>(R.id.product_count)

            name_tv.text = productData[position].name
            price_tv.text = "${DecimalFormat("#,###").format(productData[position].price)}원"
            count_tv.text = "재고: ${DecimalFormat("#,###").format(productData[position].count)}개"
            storageRef.downloadUrl.addOnSuccessListener {
                if (productData[position].count == 0) {
                    val matrix = ColorMatrix()
                    matrix.setSaturation(0F)
                    val filter = ColorMatrixColorFilter(matrix)
                    img_iv.colorFilter = filter
                } else {
                    img_iv.colorFilter = null
                }
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
                if (productData[position].count == 0) {
                    Toast.makeText(this@PosActivity, "현재 남아있는 재고가 없습니다!", Toast.LENGTH_SHORT).show()
                } else {
                    val item = BasketData(productData[position].name, productData[position].count, productData[position].price)
                    basketData.add(item)
                    binding.posBasketLv.adapter = BasketAdapter(basketData)
                    Toast.makeText(this@PosActivity, basketData.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount(): Int {
            return productData.size
        }
    }
}