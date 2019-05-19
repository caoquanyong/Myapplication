package com.example.myapplication.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.Bean.Question;
import com.example.myapplication.R;
import com.example.myapplication.View.RecordButton;
import com.example.myapplication.View.TextArea;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class HealthQuestionAdapter extends RecyclerView.Adapter<HealthQuestionAdapter.ViewHolder> {
    List<Question> mQuestionList;

    private MediaPlayer player;
    private Context mContext;

    public HealthQuestionAdapter(List<Question> questionList, Context context) {
        mQuestionList=questionList;
        mContext=context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView question;
        TextArea answer;
        RecordButton recordButton;
        LinearLayout bofang;

        public ViewHolder(View itemView) {
            super(itemView);
            question=(TextView)itemView.findViewById(R.id.question);
            answer=(TextArea)itemView.findViewById(R.id.answer);
            recordButton=itemView.findViewById(R.id.recordButton);
            bofang=itemView.findViewById(R.id.bofang);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.health_question_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String question=mQuestionList.get(position).getQuestion();
        int questionNo=mQuestionList.get(position).getQuestionNo();
        holder.question.setText(questionNo+"、"+question);
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/video_"+position+".amr";
        Log.d("hhhhhhhhhhdsds", "onBindViewHolder: "+path);
        File file=new File(path);
        file.delete();
        holder.recordButton.setSavePath(path);
        holder.recordButton.setOnFinishedRecordListener(new RecordButton.OnFinishedRecordListener() {
            @Override
            public void onFinishedRecord(String audioPath) {
                Log.i("RECORD!!!", "finished!!!!!!!!!! save to " + audioPath);
                holder.bofang.setVisibility(View.VISIBLE);
            }
        });
        player=new MediaPlayer();
        holder.bofang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player!=null){
                    player.reset();
                }else {
                    player=new MediaPlayer();
                }
                try {
                    player.setDataSource(path);
                    //初始化
                    player.prepare();
                    //开始播放
                    player.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return mQuestionList.size();
    }

}
