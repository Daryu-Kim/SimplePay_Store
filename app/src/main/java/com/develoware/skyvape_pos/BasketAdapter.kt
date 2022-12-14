package com.develoware.skyvape_pos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat

class BasketAdapter(key : String?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var basketData: ArrayList<BasketData> = arrayListOf()
    val db = Firebase.firestore

    var t_count : Int = 1
    var t_price : Int = 0
    var t_f_price : Int = 0

    init {
        db.collection("${key}_Basket")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                basketData.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(BasketData::class.java)
                    basketData.add(item!!)
                    // firestore에 저장되어 있는 basket_data에서 전체 가격 처리 및 할인액 처리.
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
        val add_btn = viewHolder.findViewById<ImageButton>(R.id.basket_add)
        val remove_btn = viewHolder.findViewById<ImageButton>(R.id.basket_remove)

        name_tv.text = basketData[position].name
        price_tv.tag = basketData[position].price
        price_tv.text = basketData[position].price?.let { decimalPrice(it) }

        t_count = Integer.parseInt(count_tv.text.toString())
        t_price = Integer.parseInt(price_tv.tag.toString())



        add_btn.setOnClickListener {
            price_tv.tag = basketData[position].price
            upCount()

            db.collection("Eunhaeng_Basket")
                .document(name_tv.text.toString())
                .update("count", t_count)
            count_tv.text = t_count.toString()

            price_tv.text = t_f_price.toString()
            db.collection("Eunhaeng_Basket")
                .document(name_tv.text.toString())
                .update("price", t_f_price)

            Toast.makeText(it.context, "price_tag: ${price_tv.tag}", Toast.LENGTH_SHORT).show()
        }

        remove_btn.setOnClickListener {
            if (t_count == 1) {
                Toast.makeText(it.context, "현재 최소 수량입니다!", Toast.LENGTH_SHORT).show()
            } else {
                t_count--
                db.collection("Eunhaeng_Basket")
                    .document(name_tv.text.toString())
                    .update("count", t_count)
                count_tv.text = t_count.toString()

                price_tv.text = t_f_price.toString()
                db.collection("Eunhaeng_Basket")
                    .document(name_tv.text.toString())
                    .update("price", t_f_price)

                Toast.makeText(it.context, "${t_count}: ${price_tv.text}", Toast.LENGTH_SHORT).show()
            }
        }

        holder.itemView.setOnClickListener {

        }
    }

    // 리사이클러뷰의 아이템 총 개수 반환
    override fun getItemCount(): Int {
        return basketData.size
    }

    fun removeItem(position: Int) {
        basketData.removeAt(position)
    }

    fun removeAllItem() {
        basketData.clear()
    }

    fun decimalPrice(temp : Int): String {
        return "${DecimalFormat("#,###").format(temp)}원"
    }

    fun upCount() {
        t_count += 1
        t_f_price = t_count * t_price
    }
}