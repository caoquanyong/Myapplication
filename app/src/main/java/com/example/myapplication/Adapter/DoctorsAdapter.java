package com.example.myapplication.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.myapplication.Activity.OnlinePayActivity;
import com.example.myapplication.Bean.DoctorData;
import com.example.myapplication.R;

import java.util.List;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {
     List<DoctorData> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView text;
        LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout=(LinearLayout) itemView.findViewById(R.id.layout);
            icon=(ImageView)itemView.findViewById(R.id.icon);
            text=(TextView)itemView.findViewById(R.id.text);

        }
    }
    public DoctorsAdapter(List<DoctorData> list){
        mList=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctors_item,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position= viewHolder.getAdapterPosition();
                DoctorData doctorData=mList.get(position);
                Intent intent =new Intent(parent.getContext(), OnlinePayActivity.class);
                //intent.putExtra("doctor",doctorData);
                parent.getContext().startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DoctorData doctorData=mList.get(position);
        holder.icon.setImageDrawable(doctorData.getIcon());
        holder.text.setText(doctorData.getText());

    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

}
