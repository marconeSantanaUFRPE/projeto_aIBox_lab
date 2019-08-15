package com.example.projeto_aibox_lab.GUI;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projeto_aibox_lab.Entidades.Livro;
import com.example.projeto_aibox_lab.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class adapterLivros extends BaseAdapter {

    private ArrayList<Livro> livroArrayList ;
    private final Activity activity;

    public adapterLivros(ArrayList<Livro> livroArrayList, Activity activity) {
        this.livroArrayList = livroArrayList;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return livroArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return livroArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

       View view = activity.getLayoutInflater()
                .inflate(R.layout.livro_adapter, viewGroup, false);


        Livro livro = livroArrayList.get(i);

        TextView nome = (TextView)
                view.findViewById(R.id.titulolivro);
        nome.setText(livro.getTitle());

        ImageView imageView = view.findViewById(R.id.fotoLivroadapter);

        String urlDaFoto = livro.getThumbnailUrl();
        try {
            if (!urlDaFoto.equals(null)) {
                //Cuidado com a foto
                Picasso.get().load(urlDaFoto).into(imageView);
            }

        } catch (Exception e){

        }


        return view;





    }
}
