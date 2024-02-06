package com.example.a7_minutes_app

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7_minutes_app.databinding.NumberOfExerciseBinding

class ExerciseNumberAdapter(val tasklist:List<ExerciseModel>):RecyclerView.Adapter<ExerciseNumberAdapter.ViewHolder>() {
    class ViewHolder(val itemBinding : NumberOfExerciseBinding):RecyclerView.ViewHolder(itemBinding.root){
        val textViewItem = itemBinding.textViewItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(NumberOfExerciseBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model:ExerciseModel= tasklist[position]
        holder.textViewItem.text = model.getId().toString()

        when{
            model.getIsSelected()->{
                holder.textViewItem.background=ContextCompat.getDrawable(holder.itemView.context,R.drawable.tv_item_number_is_selected)
                holder.textViewItem.setTextColor(Color.parseColor("#FFFE4B11"))

            }
            model.getIsCompleted()->{
                holder.textViewItem.background=ContextCompat.getDrawable(holder.itemView.context,R.drawable.tv_item_number_is_completed)
                holder.textViewItem.setTextColor(Color.parseColor("#FFFE4B11"))

            }
            else->{
                holder.textViewItem.background=ContextCompat.getDrawable(holder.itemView.context,R.drawable.tv_item_number_bg)
                holder.textViewItem.setTextColor(Color.parseColor("#FFFE4B11"))

            }
        }
    }

    override fun getItemCount(): Int {
        return tasklist.size
    }
}