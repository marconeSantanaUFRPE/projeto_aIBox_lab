package com.example.projeto_aibox_lab.GUI.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.projeto_aibox_lab.Entidades.Livro;
import com.example.projeto_aibox_lab.GUI.Interface.RecyclerViewOnClickListenerHack;
import com.example.projeto_aibox_lab.GUI.adapter.adapterRecycle;
import com.example.projeto_aibox_lab.Negocio.baixar_json;
import com.example.projeto_aibox_lab.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {


    private RecyclerView recyclerView;
    private ArrayList<Livro> livros = new ArrayList<>();


    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pdialog;

    private ListView listView;


    private  ArrayList<Livro> arrayListlivros = new ArrayList<>();
    private static String url = "https://api.myjson.com/bins/h8xi7/";
    ArrayList<HashMap<String, String>> contactList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        listView = findViewById(R.id.lista_livros);
        contactList = new ArrayList<>();

        //listView = (listView) findViewById(R.id.listview);

        new getLivros().execute();



    }

    @Override
    public void onClickListener(View view, int position) {
        adapterRecycle adapter = (adapterRecycle) recyclerView.getAdapter();
        final Livro livrinho ;
        livrinho = (Livro) adapter.imprimirItem(position);

        //dialog_apresentacao_livro dialog = new dialog_apresentacao_livro();
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        View v = getLayoutInflater().inflate(R.layout.detalhes_livro, null);
        TextView title = v.findViewById(R.id.titulo_detalhe);
        ImageView fotoLivro = v.findViewById(R.id.foto_livro);

        String urlDaFoto = livrinho.getThumbnailUrl();
        try {
            if (!urlDaFoto.equals(null)) {
                //Cuidado com a foto
                Picasso.get().load(urlDaFoto).into(fotoLivro);
            }

        } catch (Exception e){

        }

        TextView autores = v.findViewById(R.id.autores);
        autores.setText("Authors");
        autores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

                View v = getLayoutInflater().inflate(R.layout.autores, null);
                ListView autores = v.findViewById(R.id.listaAutores);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                        android.R.layout.simple_list_item_1,livrinho.getAuthors());

                alert.setView(v);
                autores.setAdapter(adapter);
                final AlertDialog dialog = alert.create();
                dialog.show();
            }
        });







        TextView isbn = v.findViewById(R.id.isbn);
        TextView qtdPaginas = v.findViewById(R.id.qtdPaginas);





        title.setText(livrinho.getTitle());
        alert.setView(v);
        final AlertDialog dialog = alert.create();
        dialog.show();

    }


    private class getLivros extends AsyncTask<Void, Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog loading
            pdialog = new ProgressDialog(MainActivity.this);
            pdialog.setMessage("Carregando");
            pdialog.setCancelable(false);
            pdialog.show();

        }


        @Override
        protected Void doInBackground(Void... voids) {

            baixar_json bj = new baixar_json();

            String jsonStr = bj.makeServiceCall(url);
            Log.e(TAG, "Responde from url: " + jsonStr);

            if (jsonStr != null) {


                try {

                    JSONArray jsonObject = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonObject.length(); i++) {

                        Livro livro = new Livro();
                        JSONObject livros = jsonObject.getJSONObject(i);

                        Iterator<String> a = livros.keys();

                        carregarAtributos(livro, a, livros);


                        arrayListlivros.add(livro);


                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Response parsing error: " + e.getMessage());
                }
            } else {

                Log.e(TAG, "nao pegou o json");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MainActivity.this, "nao pegou o json"
                        //      , Toast.LENGTH_SHORT).show();

                    }
                });
            }

            return null;


        }

        private void carregarAtributos(Livro livro, Iterator<String> a, JSONObject livros) throws JSONException {
            for (Iterator<String> inter = a; inter.hasNext(); ) {

                String s = inter.next();

                if (s.equals("title")) {
                    livro.setTitle(livros.getString(s));
                }

                if (s.equals("isbn")) {
                    livro.setIsbn(livros.getString(s));
                }

                if (s.equals("pageCount")) {
                    livro.setPageCount(livros.getInt(s));
                }


                //if(s.equals("publishedDate")){
                //  livro.setPublishedDate(s);
                //}

                if (s.equals("thumbnailUrl")) {
                    livro.setThumbnailUrl(livros.getString(s));
                }

                if (s.equals("shortDescription")) {
                    livro.setShortDescription(livros.getString(s));
                }

                if (s.equals("longDescription")) {
                    livro.setLongDescription(livros.getString(s));
                }

                if (s.equals("status")) {
                    livro.setStatus(livros.getString(s));
                }

                if(s.equals("authors")){
                    JSONArray autores = livros.getJSONArray(s);
                    ArrayList<String> autoresV = new ArrayList<>();

                    for(int i = 0; i<autores.length();i++){

                        String aut = (String) autores.get(i);
                        autoresV.add(aut);


                    }

                    livro.setAuthors(autoresV);


                }

                if(s.equals("categories")){

                    JSONArray categorias = livros.getJSONArray(s);
                    ArrayList<String> categoriasV = new ArrayList<>();

                    for(int i=0; i< categorias.length();i++){

                        String cate = (String) categorias.get(i);
                        categoriasV.add(cate);
                    }

                    livro.setCategories(categoriasV);


                }


            }
        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);

            //desativer dialog

            if (pdialog.isShowing()) {

                pdialog.dismiss();


            }

            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    adapterRecycle adapter = (adapterRecycle) recyclerView.getAdapter();

                    if (arrayListlivros.size() == linearLayoutManager.findLastVisibleItemPosition() + 1) {
                        ArrayList<Livro> arrayAux = arrayListlivros;

                        for (int i = 0; i < arrayAux.size(); i++) {

                            adapter.addListItem(arrayAux.get(i), arrayListlivros.size());
                        }


                    }

                }

            });

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);


            adapterRecycle adapter = new adapterRecycle(MainActivity.this, arrayListlivros);
            adapter.setRecyclerViewOnClickListenerHack(MainActivity.this);
            recyclerView.setAdapter(adapter);


        }
    }

}