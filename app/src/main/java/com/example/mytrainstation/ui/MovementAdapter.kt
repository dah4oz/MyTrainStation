package com.example.mytrainstation.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mytrainstation.R
import com.example.mytrainstation.model.TrainMovement

class MovementAdapter(private val items: List<TrainMovement>, private val context: Context):
    RecyclerView.Adapter<MovementAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_train_movement, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.location?.text = "Station: ${items[position].locationFullName}"
        holder.expArrival?.text = "Expected arrival: ${items[position].expArrival}"
        holder.expDepart?.text = "Expected depart: ${items[position].expDeparture}"
        holder.schArrival?.text = "Destination: ${items[position].schArrival}"
        holder.schDepart?.text = "Destination: ${items[position].schDeparture}"
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val location = view.findViewById<TextView>(R.id.train_location)
        val expArrival = view.findViewById<TextView>(R.id.exp_arrival)
        val expDepart = view.findViewById<TextView>(R.id.exp_dep)
        val schArrival = view.findViewById<TextView>(R.id.sch_arrival)
        val schDepart = view.findViewById<TextView>(R.id.sch_dep)
    }

}