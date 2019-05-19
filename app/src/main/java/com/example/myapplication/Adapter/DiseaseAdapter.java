package com.example.myapplication.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.Activity.HealthQuestionActivity;
import com.example.myapplication.Bean.Disease;
import com.example.myapplication.R;

import java.util.List;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.ViewHolder> {
    private List<Disease> mDiseaseList;
    private int type;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=(CardView)itemView;
            textView=itemView.findViewById(R.id.text);
        }
    }

    public DiseaseAdapter(List<Disease> mDiseaseList,int type) {
        this.mDiseaseList = mDiseaseList;
        this.type=type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.disease_item,viewGroup,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Intent intent=new Intent(viewGroup.getContext(),HealthQuestionActivity.class);
                intent.putExtra("disease",mDiseaseList.get(position));
                intent.putExtra("type",type);
                viewGroup.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String disease=mDiseaseList.get(i).getName();
        viewHolder.textView.setText(disease);
    }
    @Override
    public int getItemCount() {
        return mDiseaseList.size();
    }

}
