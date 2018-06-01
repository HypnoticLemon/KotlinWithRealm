package com.example.vikrant.demoone.Adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import com.example.vikrant.demoone.R
import com.example.vikrant.demoone.Interface.RecyclerListListener
import com.example.vikrant.demoone.Model.DataModel
import com.example.vikrant.demoone.Model.RealmDataModel
import io.realm.RealmResults

class DataAdapter(private var context: Context?, private var dataList: RealmResults<RealmDataModel>, private var listener: RecyclerListListener) : RecyclerView.Adapter<DataAdapter.SingleRowHolder>() {

    //RealmResults<RealmDataModel>?
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SingleRowHolder {
        return SingleRowHolder(LayoutInflater.from(context).inflate(R.layout.single_row, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    override fun onBindViewHolder(holder: SingleRowHolder?, position: Int) {
        holder?.txtBody?.text = dataList?.get(position)?.body
        holder?.txtTitle?.text = dataList?.get(position)?.title
        holder?.cardView?.setOnClickListener {
            listener.ItemClick(position, "", it, 0)
        }
    }

    class SingleRowHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtTitle: TextView? = itemView.findViewById(R.id.txtTitle) as TextView
        var txtBody: TextView? = itemView.findViewById(R.id.txtBody) as TextView
        var cardView: CardView? = itemView.findViewById(R.id.cardView) as CardView
    }

}