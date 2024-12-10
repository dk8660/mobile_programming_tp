package com.example.teamproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.Professor

class ProfessorAdapter(private val professors: List<Professor>, private val onItemClick: (Professor) -> Unit) :
    RecyclerView.Adapter<ProfessorAdapter.ProfessorViewHolder>() {

    inner class ProfessorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.professor_name)
        val field: TextView = itemView.findViewById(R.id.professor_field)
        val email: TextView = itemView.findViewById(R.id.professor_email)

        fun bind(professor: Professor) {
            name.text = professor.name
            field.text = professor.field
            email.text = professor.email

            itemView.setOnClickListener {
                onItemClick(professor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_professor, parent, false)
        return ProfessorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfessorViewHolder, position: Int) {
        val professor = professors[position]
        holder.name.text = professor.name
        holder.field.text = professor.field
        holder.email.text = professor.email
        holder.bind(professor)
    }

    override fun getItemCount(): Int = professors.size
}
