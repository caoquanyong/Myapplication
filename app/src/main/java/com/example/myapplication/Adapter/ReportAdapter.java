package com.example.myapplication.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.myapplication.Activity.ViewHistoryActivity;
import com.example.myapplication.Bean.Report;
import com.example.myapplication.R;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private List<Report> mReportList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView reportItem;
        TextView reportvalue;
        Button button;
        public ViewHolder(View view){
            super(view);
            reportItem=(TextView)view.findViewById(R.id.item);
            reportvalue=(TextView)view.findViewById(R.id.value);
            button=(Button)view.findViewById(R.id.history_button);
        }
    }
    public ReportAdapter(List<Report> reportList){
        mReportList=reportList;
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Report report=mReportList.get(position);
                Intent intent=new Intent(parent.getContext(), ViewHistoryActivity.class);
                intent.putExtra("itemName",report.getName());
                parent.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Report report=mReportList.get(position);
        holder.reportItem.setText(report.getName());
        holder.reportvalue.setText(report.getValue());
    }
    @Override
    public int getItemCount() {
        return mReportList.size();
    }

}
