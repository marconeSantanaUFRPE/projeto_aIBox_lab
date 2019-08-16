package com.example.projeto_aibox_lab.GUI.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.projeto_aibox_lab.Entidades.Livro;
import com.example.projeto_aibox_lab.GUI.Interface.RecyclerViewOnClickListenerHack;
import com.example.projeto_aibox_lab.GUI.activities.MainActivity;
import com.example.projeto_aibox_lab.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapterRecycle extends RecyclerView.Adapter<adapterRecycle.minhaViewHolder> {

    private ArrayList<Livro> livros;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public adapterRecycle(Context c, ArrayList<Livro> l){

        livros = l;
        layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public Livro imprimirItem(int position){

        return livros.get(position);

    }


    @NonNull
    @Override
    public minhaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View v = layoutInflater.inflate(R.layout.livro_adapter, viewGroup,false);
        minhaViewHolder minhaViewHolder = new minhaViewHolder(v);
        return minhaViewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull minhaViewHolder minhaViewHolder, final int i) {

    minhaViewHolder.title.setText(livros.get(i).getTitle());

    minhaViewHolder.isbn.setText(livros.get(i).getIsbn());

    //minhaViewHolder.autores.setText("Authors");

//    minhaViewHolder.autores.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//
//            final AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
//
//            View v = layoutInflater.inflate(R.layout.autores, null);
//            ListView autores = v.findViewById(R.id.autores);
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
//                    android.R.layout.simple_list_item_1,livros.get(i).getAuthors());
//
//            alert.setView(v);
//            final AlertDialog dialog = alert.create();
//            dialog.show();
//
//        }
//    });
//

    //carregar Foto
    String urlDaFoto = livros.get(i).getThumbnailUrl();
        try {
            if (!urlDaFoto.equals(null)) {
                //Cuidado com a foto
                Picasso.get().load(urlDaFoto).into(minhaViewHolder.imageView);
            }

        } catch (Exception e){

        }


    }

    @Override
    public int getItemCount() {
        return livros.size();
    }


    public  void addListItem(Livro l, int position){

        livros.add(l);
        notifyItemInserted(position);

    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){

        recyclerViewOnClickListenerHack = r;
    }




    public class minhaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imageView;
        public TextView title;
        public TextView isbn;
        public TextView autores;

        public minhaViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.fotoLivroadapter);
            title = (TextView) itemView.findViewById(R.id.titulolivro);
            isbn = (TextView) itemView.findViewById(R.id.qqCoisa);
            //autores = (TextView) itemView.findViewById(R.id.autores);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {

            if (recyclerViewOnClickListenerHack != null){

                recyclerViewOnClickListenerHack.onClickListener(view, getPosition());






            }



        }
    }


}
