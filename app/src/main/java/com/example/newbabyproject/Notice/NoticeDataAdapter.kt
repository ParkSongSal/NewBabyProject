package com.example.newbabyproject.Notice

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newbabyproject.R
import com.example.newbabyproject.utils.Common
import org.greenrobot.eventbus.EventBus

open class NoticeDataAdapter     //this.saveList = saveList;
    (//private final List<getServerImage> saveList;
    private val context: Context,
    private val mData: MutableList<ResultNotice>
) :
    RecyclerView.Adapter<NoticeDataAdapter.ViewHolder>() {

    //Event Bus 클래스
    class ItemClickEvent     //this.id = id;
        (  //public long id;
        var position: Int
    )

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val convertView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notice, parent, false)
        return ViewHolder(convertView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.user_txt.text = mData[position].insertId
        var msg: String? = ""
        msg = Common.formatTimeString(mData[position].insertDate)
        viewHolder.date_txt.text = msg
        viewHolder.title_txt.text = mData[position].title
        viewHolder.title_txt.isSelected = true
        viewHolder.itemView.setOnClickListener { // MainActivity에 onItemClick이 받음
            EventBus.getDefault().post(ItemClickEvent(position))
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title_txt: TextView
        var user_txt: TextView
        var date_txt: TextView

        init {
            // 레이아웃 들고 오기
            val title_txt = itemView.findViewById<TextView>(R.id.titleTxt)
            val user_txt = itemView.findViewById<TextView>(R.id.userTxt)
            val date_txt = itemView.findViewById<TextView>(R.id.dateTxt)
            this.title_txt = title_txt
            this.user_txt = user_txt
            this.date_txt = date_txt
        }
    }
}