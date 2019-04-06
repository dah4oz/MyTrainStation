package com.example.mytrainstation.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mytrainstation.R
import com.example.mytrainstation.model.StationData

class TrainAdapter(private val items: MutableList<StationData>, private val context: Context): RecyclerView.Adapter<TrainAdapter.ViewHolder>() {

    private lateinit var callback: TrainItemClicked

    interface TrainItemClicked {
        fun onTrainClicked(code: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_train_list, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.trainDate?.text = "Date: ${items[position].trainDate}"
        holder.trainOrigin?.text = "Origin: ${items[position].origin}"
        holder.trainDestination?.text = "Destination: ${items[position].destination}"
        holder.expArrival?.text = "Expected arrival: ${items[position].expectedArrival}"
        holder.expDepart?.text = "Expected depart: ${items[position].expectedDepart}"
        holder.schArrival?.text = "Destination: ${items[position].schArr}"
        holder.schDepart?.text = "Destination: ${items[position].schDepart}"
        holder.direction?.text = "Destination: ${items[position].direction}"

        if(::callback.isInitialized) {
            holder.container.setOnClickListener {
                val trainCode = items[position].trainCode ?: ""
                callback.onTrainClicked(trainCode)
            }
        }
    }

    fun setTrainClickListener(listener: TrainItemClicked) {
        callback = listener
    }

    fun setTrainData(data: List<StationData>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val container = view.findViewById<LinearLayout>(R.id.train_container)
        val trainDate = view.findViewById<TextView>(R.id.train_date)
        val trainOrigin = view.findViewById<TextView>(R.id.train_origin)
        val trainDestination = view.findViewById<TextView>(R.id.train_destination)
        val expArrival = view.findViewById<TextView>(R.id.train_exp_arrival)
        val expDepart = view.findViewById<TextView>(R.id.train_exp_dep)
        val schArrival = view.findViewById<TextView>(R.id.train_sch_arrival)
        val schDepart = view.findViewById<TextView>(R.id.train_sch_dep)
        val direction = view.findViewById<TextView>(R.id.train_direction)
    }
}