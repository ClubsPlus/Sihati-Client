package com.example.sihati_client.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.sihati_client.R
import com.example.sihati_client.database.Test
import com.example.sihati_client.viewModels.ScheduleViewModel

class AppointementAdapter(
    val context: Context,
    private val taskClickInterface: TaskClickInterface,
    private val viewModel: ScheduleViewModel
) : RecyclerView.Adapter<AppointementAdapter.AppointementViewHolder>(){

    // on below line we are creating a
    // variable for our all notes list.
    private val allTests = ArrayList<Test>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointementViewHolder {
        // inflating our layout file for each item of recycler view.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.appointement_item,
            parent, false
        )
        return AppointementViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AppointementViewHolder, position: Int) {
        // on below line we are setting data to item of recycler view.
        allTests[position].schedule_id?.let {
            viewModel.getScheduleByIdAndSet(it,holder.date,holder.startTime,holder.endTime, full = true)
        }

        allTests[position].laboratory_id?.let {
            viewModel.getLaboratoryByIdAndSet(it,holder.laboratoryName)
        }
        holder.itemView.setOnClickListener { taskClickInterface?.onClick(allTests[position]) }
    }

    override fun getItemCount() = allTests.size

    // below method is use to update our list of notes.
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Test>) {
        // on below line we are clearing
        // our notes array list
        allTests.clear()
        // on below line we are adding a
        // new list to our all notes list.
        allTests.addAll(newList)
        // on below line we are calling notify data
        // change method to notify our adapter.
        notifyDataSetChanged()
    }

    inner class AppointementViewHolder(itView: View) :
        RecyclerView.ViewHolder(itView){
        // on below line we are creating an initializing all our
        // variables which we have added in layout file.
        val date: TextView = itemView.findViewById(R.id.date)
        val startTime: TextView = itemView.findViewById(R.id.startTime)
        val endTime: TextView = itemView.findViewById(R.id.endTime)
        val laboratoryName: TextView = itemView.findViewById(R.id.laboratoryName)
        val schedule: ConstraintLayout = itemView.findViewById(R.id.schedule)
    }

    interface TaskClickInterface {
        // creating a method for click action
        // on recycler view item for updating it.
        fun onClick(test: Test)
    }
}