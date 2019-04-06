package com.example.mytrainstation.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mytrainstation.R
import com.example.mytrainstation.model.TrainMovement

class TrainMovementFragment: Fragment() {

    private var trainMovement: List<TrainMovement>? = null

    private var recyclerView: RecyclerView? = null

    companion object {

        fun newInstance(): TrainMovementFragment {
            return TrainMovementFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_movement, container, false)

        recyclerView = view.findViewById(R.id.recycler)
        recyclerView?.layoutManager = LinearLayoutManager(activity)

        return view
    }

    public fun setData(data: List<TrainMovement>) {
        trainMovement = data
        recyclerView?.adapter = MovementAdapter(data, context!!)
    }
}