package com.refatwashere.teacherszone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.refatwashere.teacherszone.R;
import com.refatwashere.teacherszone.database.Syllabus;

import java.util.List;

public class SyllabusAdapter extends RecyclerView.Adapter<SyllabusAdapter.SyllabusViewHolder> {

    public interface OnItemActionListener {
        void onEditClick(Syllabus syllabus);
        void onDeleteClick(Syllabus syllabus);
    }

    private List<Syllabus> syllabusList;
    private final OnItemActionListener listener;

    public SyllabusAdapter(List<Syllabus> syllabusList, OnItemActionListener listener) {
        this.syllabusList = syllabusList;
        this.listener = listener;
    }

    public void setSyllabusList(List<Syllabus> list) {
        this.syllabusList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SyllabusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_syllabus, parent, false);
        return new SyllabusViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SyllabusViewHolder holder, int position) {
        Syllabus s = syllabusList.get(position);
        holder.title.setText(s.getTitle());
        holder.code.setText("Course Code: " + s.getCourseCode());
        holder.description.setText("Description: " + s.getDescription());

        holder.edit.setOnClickListener(v -> listener.onEditClick(s));
        holder.delete.setOnClickListener(v -> listener.onDeleteClick(s));
    }

    @Override
    public int getItemCount() {
        return syllabusList.size();
    }

    static class SyllabusViewHolder extends RecyclerView.ViewHolder {
        TextView title, code, description;
        Button edit, delete;

        SyllabusViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewSyllabusTitle);
            code = itemView.findViewById(R.id.textViewCourseCode);
            description = itemView.findViewById(R.id.textViewDescription);
            edit = itemView.findViewById(R.id.buttonEditSyllabus);
            delete = itemView.findViewById(R.id.buttonDeleteSyllabus);
        }
    }
}
