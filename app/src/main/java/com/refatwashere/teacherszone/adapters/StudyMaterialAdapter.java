package com.refatwashere.teacherszone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.refatwashere.teacherszone.R;
import com.refatwashere.teacherszone.database.StudyMaterial;

import java.util.List;

public class StudyMaterialAdapter extends RecyclerView.Adapter<StudyMaterialAdapter.MaterialViewHolder> {

    public interface OnItemActionListener {
        void onEditClick(StudyMaterial material);
        void onDeleteClick(StudyMaterial material);
    }

    private List<StudyMaterial> materialList;
    private final OnItemActionListener listener;

    public StudyMaterialAdapter(List<StudyMaterial> list, OnItemActionListener listener) {
        this.materialList = list;
        this.listener = listener;
    }

    public void setMaterialList(List<StudyMaterial> list) {
        this.materialList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_material, parent, false);
        return new MaterialViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        StudyMaterial m = materialList.get(position);
        holder.title.setText(m.getTitle());
        holder.subject.setText("Subject: " + m.getSubject());
        holder.type.setText("Type: " + m.getMaterialType());
        holder.path.setText("Path: " + m.getContentPath());
        holder.notes.setText("Notes: " + m.getNotes());

        holder.edit.setOnClickListener(v -> listener.onEditClick(m));
        holder.delete.setOnClickListener(v -> listener.onDeleteClick(m));
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }

    static class MaterialViewHolder extends RecyclerView.ViewHolder {
        TextView title, subject, type, path, notes;
        Button edit, delete;

        MaterialViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewMaterialTitle);
            subject = itemView.findViewById(R.id.textViewSubject);
            type = itemView.findViewById(R.id.textViewMaterialType);
            path = itemView.findViewById(R.id.textViewContentPath);
            notes = itemView.findViewById(R.id.textViewNotes);
            edit = itemView.findViewById(R.id.buttonEditMaterial);
            delete = itemView.findViewById(R.id.buttonDeleteMaterial);
        }
    }
}
