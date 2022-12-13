package com.develoware.skyvape_pos

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import kotlin.coroutines.coroutineContext

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
        val price_00_tv = viewHolder.findViewById<TextView>(R.id.basket_price_00)
        val add_btn = viewHolder.findViewById<ImageButton>(R.id.basket_add)
        val remove_btn = viewHolder.findViewById<ImageButton>(R.id.basket_remove)
        val delete_btn = viewHolder.findViewById<ImageButton>(R.id.basket_delete)

        var temp_count = Integer.parseInt(count_tv.text.toString())
        var temp_price = basketData[position].price

        name_tv.text = basketData[position].name
        price_tv.text = basketData[position].price?.let { decimalPrice(it) }
        price_00_tv.text = basketData[position].price.toString()

        add_btn.setOnClickListener {
            price_00_tv.text = basketData[position].price.toString()
            temp_count++
            db.collection("Eunhaeng_Basket")
                .document(name_tv.text.toString())
                .update("count", temp_count)
            count_tv.text = temp_count.toString()

            val price_temp = Integer.parseInt(price_00_tv.text.toString())
            val count_temp = Integer.parseInt(count_tv.text.toString())
            val price = count_temp * temp_price!!

            price_tv.text = price.toString()
            db.collection("Eunhaeng_Basket")
                .document(name_tv.text.toString())
                .update("price", price)

            Toast.makeText(it.context, "${temp_count}: ${count_tv.text}", Toast.LENGTH_SHORT).show()
        }

        remove_btn.setOnClickListener {
            if (temp_count == 1) {
                Toast.makeText(it.context, "현재 최소 수량입니다!", Toast.LENGTH_SHORT).show()
            } else {
                price_00_tv.text = basketData[position].price.toString()
                temp_count--
                db.collection("Eunhaeng_Basket")
                    .document(name_tv.text.toString())
                    .update("count", temp_count)
                count_tv.text = temp_count.toString()

                var price_temp = Integer.parseInt(price_00_tv.text.toString())
                var count_temp = Integer.parseInt(count_tv.text.toString())
                var price = count_temp * price_temp

                price_tv.text = price.toString()
                db.collection("Eunhaeng_Basket")
                    .document(name_tv.text.toString())
                    .update("price", price)

                Toast.makeText(it.context, "${temp_count}: ${price_tv.text}", Toast.LENGTH_SHORT).show()
            }
        }

        delete_btn.setOnClickListener {
            db.collection("Eunhaeng_Basket")
                .document(name_tv.text.toString())
                .delete()
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
}