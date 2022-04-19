package com.example.sihati_client.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sihati_client.R
import com.example.sihati_client.database.Schedule
import com.example.sihati_client.viewModels.ScheduleViewModel

class ScheduleAdapter(
    val context: Context,
    private val onClickInterface: OnClickInterface,
    val viewModel: ScheduleViewModel
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    // on below line we are creating a
    // variable for our all notes list.
    private val allSchedules = ArrayList<Schedule>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        // inflating our layout file for each item of recycler view.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.schedule_item,
            parent, false
        )
        return ScheduleViewHolder(itemView)
    }

    @SuppressLint("ResourceType", "SetTextI18n")
    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        // on below line we are setting data to item of recycler view.
        holder.date.text = allSchedules[position].date
        holder.time.text = allSchedules[position].time_Start+" - "+allSchedules[position].time_end
        holder.persons.text = allSchedules[position].person.toString()+"/"+allSchedules[position].limite.toString()
        allSchedules[position].laboratory_id?.let {
            viewModel.getLaboratoryById(it)
            holder.laboratoryName.text = viewModel.laboratory?.name
        }

        val progress = ((allSchedules[position].person)!! *100)/ allSchedules[position].limite!!
        holder.progressBar.progress = progress
        when (progress) {
            in 0..45 -> {
                holder.linearLayout.setBackgroundResource(R.color.green)
                holder.persons.setTextColor(Color.parseColor("#2EB086"))
                holder.submit_button.setBackgroundResource(R.drawable.back_green_round)
            }
            in 46..80 -> {
                holder.linearLayout.setBackgroundResource(R.color.yellow)
                holder.persons.setTextColor(Color.parseColor("#BBBA12"))
                holder.submit_button.setBackgroundResource(R.drawable.back_yellow_round)
            }
            else -> {
                holder.linearLayout.setBackgroundResource(R.color.red)
                holder.persons.setTextColor(Color.parseColor("#B8405E"))
                holder.submit_button.setBackgroundResource(R.drawable.back_red_round)
            }
        }

        holder.submit_button.setOnClickListener { onClickInterface.onClick(allSchedules[position]) }

    }

    override fun getItemCount() = allSchedules.size

    // below method is use to update our list of notes.
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Schedule>) {
        // on below line we are clearing
        // our notes array list
        allSchedules.clear()
        // on below line we are adding a
        // new list to our all notes list.
        allSchedules.addAll(newList)
        // on below line we are calling notify data
        // change method to notify our adapter.
        notifyDataSetChanged()
    }

    inner class ScheduleViewHolder(itView: View) :
        RecyclerView.ViewHolder(itView) {
        // on below line we are creating an initializing all our
        // variables which we have added in layout file.
        val laboratoryName: TextView = itemView.findViewById(R.id.laboratory_name)
        val date: TextView = itemView.findViewById(R.id.date)
        val name: TextView = itemView.findViewById(R.id.laboratory_name)
        val time: TextView = itemView.findViewById(R.id.time)
        val persons: TextView = itemView.findViewById(R.id.number)
        val submit_button: Button = itemView.findViewById(R.id.add)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout)
    }

    interface OnClickInterface {
        // creating a method for click action
        // on recycler view item for updating it.
        fun onClick(schedule: Schedule)
    }
}
