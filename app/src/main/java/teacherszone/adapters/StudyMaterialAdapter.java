package com.refat.teacherszone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.refat.teacherszone.R;
import com.refat.teacherszone.database.StudyMaterial;

import java.util.List;

public class StudyMaterialAdapter extends RecyclerView.Adapter<StudyMaterialAdapter.StudyMaterialViewHolder> {

    private List<StudyMaterial> materialList;
    private OnItemActionListener listener;

    public interface OnItemActionListener {
        void onEditClick(StudyMaterial material);
        void onDeleteClick(StudyMaterial material);
    }

    public StudyMaterialAdapter(List<StudyMaterial> materialList, OnItemActionListener listener) {
        this.materialList = materialList;
        this.listener = listener;
    }

    public void setMaterialList(List<StudyMaterial> newMaterialList) {
        this.materialList = newMaterialList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudyMaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_study_material, parent, false);
        return new StudyMaterialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudyMaterialViewHolder holder, int position) {
        StudyMaterial material = materialList.get(position);
        holder.textViewMaterialTitle.setText(material.getTitle());
        holder.textViewMaterialSubject.setText("Subject: " + material.getSubject());
        holder.textViewMaterialType.setText("Type: " + material.getMaterialType());
        holder.textViewContentPath.setText("Content: " + material.getContentPath());
        holder.textViewNotes.setText("Notes: " + material.getNotes());

        holder.buttonEditMaterial.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(material);
            }
        });

        holder.buttonDeleteMaterial.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(material);
            }
        });
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }

    static class StudyMaterialViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMaterialTitle, textViewMaterialSubject, textViewMaterialType, textViewContentPath, textViewNotes;
        Button buttonEditMaterial, buttonDeleteMaterial;

        public StudyMaterialViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMaterialTitle = itemView.findViewById(R.id.textViewMaterialTitle);
            textViewMaterialSubject = itemView.findViewById(R.id.textViewMaterialSubject);
            textViewMaterialType = itemView.findViewById(R.id.textViewMaterialType);
            textViewContentPath = itemView.findViewById(R.id.textViewContentPath);
            textViewNotes = itemView.findViewById(R.id.textViewNotes);
            buttonEditMaterial = itemView.findViewById(R.id.buttonEditMaterial);
            buttonDeleteMaterial = itemView.findViewById(R.id.buttonDeleteMaterial);
        }
    }
}