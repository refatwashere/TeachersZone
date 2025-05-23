package com.refat.teacherszone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.refat.teacherszone.R;
import com.refat.teacherszone.database.Syllabus;

import java.util.List;

public class SyllabusAdapter extends RecyclerView.Adapter<SyllabusAdapter.SyllabusViewHolder> {

    private List<Syllabus> syllabusList;
    private OnItemActionListener listener;

    // Interface for callbacks to the Activity/Fragment
    public interface OnItemActionListener {
        void onEditClick(Syllabus syllabus);
        void onDeleteClick(Syllabus syllabus);
    }

    public SyllabusAdapter(List<Syllabus> syllabusList, OnItemActionListener listener) {
        this.syllabusList = syllabusList;
        this.listener = listener;
    }

    // Method to update the data in the adapter
    public void setSyllabusList(List<Syllabus> newSyllabusList) {
        this.syllabusList = newSyllabusList;
        notifyDataSetChanged(); // Notify RecyclerView that data has changed
    }

    @NonNull
    @Override
    public SyllabusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_syllabus, parent, false);
        return new SyllabusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SyllabusViewHolder holder, int position) {
        Syllabus syllabus = syllabusList.get(position);
        holder.textViewSyllabusTitle.setText(syllabus.getTitle());
        holder.textViewCourseCode.setText("Course Code: " + syllabus.getCourseCode());
        holder.textViewDescription.setText(syllabus.getDescription());

        // Show/hide file path based on content
        if (syllabus.getFilePath() != null && !syllabus.getFilePath().isEmpty()) {
            holder.textViewFilePath.setText("File/Link: " + syllabus.getFilePath());
            holder.textViewFilePath.setVisibility(View.VISIBLE);
        } else {
            holder.textViewFilePath.setVisibility(View.GONE);
        }

        // Set up click listeners for Edit and Delete buttons
        holder.buttonEditSyllabus.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(syllabus);
            }
        });

        holder.buttonDeleteSyllabus.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(syllabus);
            }
        });
    }

    @Override
    public int getItemCount() {
        return syllabusList.size();
    }

    // ViewHolder class to hold references to the views in each item layout
    static class SyllabusViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSyllabusTitle, textViewCourseCode, textViewDescription, textViewFilePath;
        Button buttonEditSyllabus, buttonDeleteSyllabus;

        public SyllabusViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSyllabusTitle = itemView.findViewById(R.id.textViewSyllabusTitle);
            textViewCourseCode = itemView.findViewById(R.id.textViewCourseCode);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewFilePath = itemView.findViewById(R.id.textViewFilePath);
            buttonEditSyllabus = itemView.findViewById(R.id.buttonEditSyllabus);
            buttonDeleteSyllabus = itemView.findViewById(R.id.buttonDeleteSyllabus);
        }
    }
}