package com.example.projeto_aibox_lab.GUI;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.projeto_aibox_lab.Entidades.Livro;
import com.example.projeto_aibox_lab.Negocio.baixar_json;
import com.example.projeto_aibox_lab.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity  {
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pdialog;
    private ListView listView;
    private  ArrayList<Livro> arrayListlivros = new ArrayList<>();
    private static String url = "https://api.myjson.com/bins/h8xi7/";
    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.lista_livros);
        contactList = new ArrayList<>();

        //listView = (listView) findViewById(R.id.listview);

        new getLivros().execute();


    }

    private class getLivros extends AsyncTask<Void, Void,Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //dialog loading
            pdialog = new ProgressDialog(MainActivity.this);
            pdialog.setMessage("Carregando");
            pdialog.setCancelable(false);
            pdialog.show();

        }


        @Override
        protected Void doInBackground(Void... voids) {

            baixar_json  bj = new baixar_json();

            String jsonStr = bj.makeServiceCall(url);
            Log.e(TAG , "Responde from url: " + jsonStr);

            if (jsonStr != null){


                try{

                    JSONArray jsonObject = new JSONArray(jsonStr);

                    for(int i = 0 ;i<jsonObject.length();i++){

                        Livro livro = new Livro();
                        JSONObject livros = jsonObject.getJSONObject(i);

                        Iterator<String> a = livros.keys();

                        carregarAtributos(livro, a, livros);

//                        String title = livros.getString("title");
//                        String isbn = livros.getString("isbn");
//                        int pageCount = livros.getInt("pageCount");
//
//                        //hashmap
//                        String publishedDate = (livros.getString("publishedDate"));
//
//                        String thumbnailUrl = livros.getString("thumbnailUrl");
//                        String shortDescription = livros.getString("shortDescription");
//                        String longDescription = livros.getString("longDescription");
//                        String status = livros.getString("status");
//
//                        //arraylist
//                        String authors = livros.getString("authors");
//
//                        //arraylist
//                        String caregories =  livros.getString("categories");
//
//
//                        livro.setTitle(title);
//                        livro.setIsbn(isbn);
//                        livro.setPageCount(pageCount);
//                        livro.setThumbnailUrl(thumbnailUrl);
//                        livro.setLongDescription(longDescription);
//                        livro.setShortDescription(shortDescription);
//                        livro.setStatus(status);
//

                        arrayListlivros.add(livro);


                    }

                } catch (final JSONException e){
                    Log.e(TAG, "Response parsing error: " + e.getMessage() );


                }
            }
            else{

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
            for(Iterator<String> inter = a; inter.hasNext();){

                String s = inter.next();

                if(s.equals("title")){
                    livro.setTitle(livros.getString(s));
                }

                if(s.equals("isbn")){
                    livro.setIsbn(livros.getString(s));
                }

                if(s.equals("pageCount")){
                    livro.setPageCount(livros.getInt(s));
                }


                //if(s.equals("publishedDate")){
                  //  livro.setPublishedDate(s);
                //}

                if(s.equals("thumbnailUrl")){
                    livro.setThumbnailUrl(livros.getString(s));
                }

                if(s.equals("shortDescription")){
                    livro.setShortDescription(livros.getString(s));
                }

                if(s.equals("longDescription")){
                    livro.setLongDescription(livros.getString(s));
                }

                if(s.equals("status")){
                    livro.setStatus(livros.getString(s));
                }

                //if(s.equals("authors")){

                //}

                //if(s.equals("categories")){

                //}


            }
        }

        @Override
        protected void onPostExecute(Void avoid){
            super.onPostExecute(avoid);

            //desativer dialog

            if(pdialog.isShowing()){

                pdialog.dismiss();


            }


            //ArrayAdapter<Livro> ada = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1, arrayListlivros);

            adapterLivros ada = new adapterLivros(arrayListlivros, MainActivity.this);

            listView.setAdapter(ada);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                   // Livro livrinho ;
                    //livrinho = (Livro)

                }
            });


                }


        }



    }


