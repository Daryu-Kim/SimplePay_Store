package com.develoware.skyvape_pos

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat

class BasketAdapter(key : String?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var basketData: ArrayList<BasketData> = arrayListOf()
    val db = Firebase.firestore

    init {
        db.collection("${key}_Basket")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                basketData.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(BasketData::class.java)
                    basketData.add(item!!)
                    // 현재 BasketAdapter에서 PosActivity의 total_price에 값을 저장할 수 없음. 해결 조치바람.
                    PosActivity().total_price += item.price!!
                }
                notifyDataSetChanged()
            }
    }

    // xml파일을 inflate하여 ViewHolder를 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_basket, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = (holder as ViewHolder).itemView

        val name_tv = viewHolder.findViewById<TextView>(R.id.basket_name)
        val count_tv = viewHolder.findViewById<TextView>(R.id.basket_count)
        val price_tv = viewHolder.findViewById<TextView>(R.id.basket_price)

        name_tv.text = basketData[position].name
        price_tv.text = "${DecimalFormat("#,###").format(basketData[position].price)}원"


        holder.itemView.setOnClickListener {

        }
    }

    // 리사이클러뷰의 아이템 총 개수 반환
    override fun getItemCount(): Int {
        return basketData.size
    }
}