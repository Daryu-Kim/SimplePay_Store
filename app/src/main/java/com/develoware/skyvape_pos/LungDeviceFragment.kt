package com.develoware.skyvape_pos

import android.app.AlertDialog
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.develoware.skyvape_pos.databinding.ActivityLoginInputBinding
import com.develoware.skyvape_pos.databinding.ActivityPosBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.DecimalFormat

class LungDeviceFragment : Fragment() {

    val db = Firebase.firestore
    var product_option_select_layout : ConstraintLayout? = null
    var product_option_title : TextView? = null
    var product_option_select_rv : RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)

        product_option_select_layout = view.findViewById(R.id.product_option_select_layout)
        product_option_title = view.findViewById(R.id.product_option_select_title_tv)
        product_option_select_rv = view.findViewById(R.id.product_option_select_rv)

        view.findViewById<RecyclerView>(R.id.product_rv).apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 4)
            adapter = RecyclerViewAdapter()
        }

        return view
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var productData: ArrayList<ProductData> = arrayListOf()

        init {
            db.collection("lung_device")
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
                storage.reference.child("Lung_Device/${productData[position].img.toString()}")

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
                Glide.with(this@LungDeviceFragment)
                    .load(it.toString())
                    .centerCrop()
                    .into(img_iv)
            }

                .addOnFailureListener {
                    Glide.with(this@LungDeviceFragment)
                        .load(R.drawable.logo)
                        .centerCrop()
                        .into(img_iv)
                }

            holder.itemView.setOnClickListener {
                if (productData[position].count == 0) {
                    Toast.makeText(context, "현재 남아있는 재고가 없습니다!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, price_tv.text, Toast.LENGTH_SHORT).show()
                    product_option_select_layout?.visibility = View.VISIBLE
                    product_option_title?.text = "${productData[position].name} 옵션 선택"

                    product_option_select_rv?.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context)
                        adapter = OptionAdapter()
                    }

                }
            }
        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount(): Int {
            return productData.size
        }
    }

    inner class OptionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var productData: ArrayList<ProductData> = arrayListOf()

        init {
            db.collection("lung_device")
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
                storage.reference.child("Lung_Device/${productData[position].img.toString()}")

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
                Glide.with(this@LungDeviceFragment)
                    .load(it.toString())
                    .centerCrop()
                    .into(img_iv)
            }

                .addOnFailureListener {
                    Glide.with(this@LungDeviceFragment)
                        .load(R.drawable.logo)
                        .centerCrop()
                        .into(img_iv)
                }

            holder.itemView.setOnClickListener {
                if (productData[position].count == 0) {
                    Toast.makeText(context, "현재 남아있는 재고가 없습니다!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, price_tv.text, Toast.LENGTH_SHORT).show()
                    product_option_select_layout?.visibility = View.VISIBLE
                    product_option_title?.text = "${productData[position].name} 옵션 선택"
                }
            }
        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount(): Int {
            return productData.size
        }
    }


}