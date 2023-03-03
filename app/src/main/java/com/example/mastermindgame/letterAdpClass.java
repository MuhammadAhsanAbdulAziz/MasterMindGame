package com.example.mastermindgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class letterAdpClass extends RecyclerView.Adapter<letterAdpClass.ViewHolder> {

    Context context;
    ArrayList<letterClass> letterArrayList = new ArrayList<>();
    private  RecyclerViewClickListener itemListener;


    public letterAdpClass(Context context, ArrayList<letterClass> letterArrayList, RecyclerViewClickListener itemListener) {
        this.context = context;
        this.letterArrayList = letterArrayList;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.letterbutton,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String Alphabet = letterArrayList.get(position).alphabet;

        holder.setData(Alphabet);
    }

    @Override
    public int getItemCount() {
        return letterArrayList.size();
    }

    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(View v, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button alphabetbtn;

        public ViewHolder(View itemView) {
            super(itemView);
            alphabetbtn = itemView.findViewById(R.id.btnview);
            alphabetbtn.setOnClickListener(this);
        }

        public void setData(String alphabet) {
            alphabetbtn.setText(alphabet);

        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());
        }
    }
}
