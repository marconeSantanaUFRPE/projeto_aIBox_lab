package com.example.projeto_aibox_lab.GUI.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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

    private  ArrayList<Livro> arrayListlivros = new ArrayList<>();
    private static String url = "https://api.myjson.com/bins/h8xi7/";
    ArrayList<HashMap<String, String>> contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        contactList = new ArrayList<>();
        new getLivros().execute();

    }

    @Override
    public void onClickListener(View view, int position) {
        adapterRecycle adapter = (adapterRecycle) recyclerView.getAdapter();
        final Livro livrinho ;
        livrinho = (Livro) adapter.imprimirItem(position);

        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        View v = getLayoutInflater().inflate(R.layout.detalhes_livro, null);
        TextView title = v.findViewById(R.id.titulo_detalhe);
        ImageView fotoLivro = v.findViewById(R.id.foto_livro);
        ImageButton imageButton = v.findViewById(R.id.imageButtonSair);



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

                chamarDialog(view, livrinho, livrinho.getAuthors(),"Authors");
            }
        });


        TextView categories = v.findViewById(R.id.caregoria);

        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chamarDialog(view,livrinho, livrinho.getCategories(),"Categories");
            }
        });


        TextView isbn = v.findViewById(R.id.isbn);
        TextView qtdPaginas = v.findViewById(R.id.qtdPaginas);
        TextView status = v.findViewById(R.id.status);
        TextView data = v.findViewById(R.id.dataPublicacao);

        TextView descricaoLonga = v.findViewById(R.id.descricaoLonga);

        descricaoLonga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mostrarTextos(view, livrinho, "Long description",livrinho.getLongDescription());


            }
        });

        TextView descricaoCurta = v.findViewById(R.id.descricaoCurta);

        descricaoCurta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mostrarTextos(view,livrinho,"Short Description", livrinho.getShortDescription());
            }
        });

        isbn.setText(livrinho.getIsbn());
        title.setText(livrinho.getTitle());
        String paginas = String.valueOf(livrinho.getPageCount());
        qtdPaginas.setText(paginas);
        status.setText(livrinho.getStatus());
        data.setText(livrinho.getPublishedDate());
        alert.setView(v);
        final AlertDialog dialog = alert.create();
        dialog.show();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



    }

    private void mostrarTextos(View view, Livro livrinho,String string, String longaCurta) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

        View v = getLayoutInflater().inflate(R.layout.dialog_texto, null);
        TextView titulo = v.findViewById(R.id.tituloTexto);
        titulo.setText(string);
        TextView texto = v.findViewById(R.id.corpoTexto);
        texto.setText(longaCurta);
        texto.setMovementMethod(new ScrollingMovementMethod());
        alert.setView(v);
        final AlertDialog dialog = alert.create();
        dialog.show();

        ImageButton imageButton = v.findViewById(R.id.imageButtonTextos);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });


    }

    private void chamarDialog(View view, Livro livrinho,ArrayList<String> stringArrayList,String t) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

        View v = getLayoutInflater().inflate(R.layout.autores, null);
        TextView titulo = v.findViewById(R.id.TituloAutorCategoria);
        titulo.setText(t);
        ListView autores = v.findViewById(R.id.listaAutores);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_list_item_1,stringArrayList);

        alert.setView(v);
        autores.setAdapter(adapter);
        final AlertDialog dialog = alert.create();
        dialog.show();

        ImageButton imageButton = v.findViewById(R.id.imageButtonCaregoriesAuthors);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();


            }
        });



    }


    private class getLivros extends AsyncTask<Void, Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new ProgressDialog(MainActivity.this);
            pdialog.setMessage("Carregando JSON dos Livros");
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
                if(s.equals("publishedDate")){

                    JSONObject jsonArray = livros.getJSONObject(s);
                    String data = jsonArray.getString("$date");
                    livro.setPublishedDate(data);
                }

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

                    livro.setAuthors(jsonToArrayList(livro, livros, s));
                }

                if(s.equals("categories")){

                    livro.setCategories(jsonToArrayList(livro,livros,s));
                }
            }
        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
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

    private ArrayList<String> jsonToArrayList(Livro livro, JSONObject livros, String s) throws JSONException {
        JSONArray autores = livros.getJSONArray(s);
        ArrayList<String> autoresV = new ArrayList<>();

        for(int i = 0; i<autores.length();i++){

            String aut = (String) autores.get(i);
            autoresV.add(aut);
        }
        return autoresV;
    }
}