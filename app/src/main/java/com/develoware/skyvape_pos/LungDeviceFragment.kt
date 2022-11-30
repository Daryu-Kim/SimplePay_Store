package com.develoware.skyvape_pos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LungDeviceFragment : Fragment() {

    val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)

        view.findViewById<RecyclerView>(R.id.product_rv).apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 4)
            adapter = RecyclerViewAdapter()
        }

        return view
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Person 클래스 ArrayList 생성성
        var productData : ArrayList<ProductData> = arrayListOf()

        init {  // telephoneBook의 문서를 불러온 뒤 Person으로 변환해 ArrayList에 담음
            db.collection("lung_device")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // ArrayList 비워줌
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
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
            return ViewHolder(view)
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }

        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView

            val name_tv = viewHolder.findViewById<TextView>(R.id.product_name)
            val img_iv = viewHolder.findViewById<ImageView>(R.id.product_img)
            val price_tv = viewHolder.findViewById<TextView>(R.id.product_price)

            name_tv.text = productData[position].name
            price_tv.text = "${productData[position].price.toString()}원"
        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount(): Int {
            return productData.size
        }
    }

}