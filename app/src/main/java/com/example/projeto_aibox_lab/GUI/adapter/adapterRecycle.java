package com.example.projeto_aibox_lab.GUI.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projeto_aibox_lab.Entidades.Livro;

import java.util.ArrayList;

public class adapterRecycle extends RecyclerView.Adapter<adapterRecycle.minhaViewHolder> {

    private ArrayList<Livro> livros;
    private LayoutInflater layoutInflater;


    public adapterRecycle(Context c, ArrayList<Livro> l){

        livros = l;
        layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @NonNull
    @Override
    public minhaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull minhaViewHolder minhaViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class minhaViewHolder extends RecyclerView.ViewHolder{

        public minhaViewHolder(View itemView){
            super(itemView);
        }

}


}
