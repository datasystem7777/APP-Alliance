package com.app2.Fragmentos;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app2.Classes.PREFERENCIA;
import com.app2.MainActivity;
import com.app2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultaProduto extends Fragment {


    public ConsultaProduto() {
        // Required empty public constructor
    }



    View view;


    ListView vListView;
    EditText vEdit_busca;
    adapterProduto adpListaBuscaProduto;
    ImageView vBtn_cod_bar;

    PREFERENCIA conf;
    JSONObject usuarioJS;
    JSONArray array;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consulta_produto, container, false);

        if(getActivity() != null){
            conf      = ((MainActivity)getActivity()).conf;
            usuarioJS = ((MainActivity)getActivity()).usuarioJS;
        }else{
            conf    = new PREFERENCIA(getContext());
            usuarioJS = new JSONObject();
        }

        try {
            array = new JSONArray(conf.getProdutos(""));
        } catch (JSONException e) {
            e.printStackTrace();
            array = new JSONArray();
        }



        vListView = view.findViewById(R.id.vListView);
        adpListaBuscaProduto = new adapterProduto(array);
        vListView.setAdapter(adpListaBuscaProduto);


        vListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(getActivity()!=null){
                    try {
                        String ID_PRODUTO = adpListaBuscaProduto.getItem(position).getString("product_id");
                        ((MainActivity)getActivity()).abrirItem(null,ID_PRODUTO);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        vEdit_busca    = view.findViewById(R.id.vEdit_busca);
        vEdit_busca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    array = new JSONArray(conf.getProdutos(vEdit_busca.getText().toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                    array = new JSONArray();
                }
                adpListaBuscaProduto = new adapterProduto(array);
                vListView.setAdapter(adpListaBuscaProduto);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        vBtn_cod_bar = view.findViewById(R.id.vBtn_cod_bar);

        vBtn_cod_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    ((MainActivity)getActivity()).LeitorCodeBar();
                }
            }
        });

        return view;
    }


    public class adapterProduto extends BaseAdapter {

        JSONArray array;

        public adapterProduto(JSONArray array){
            this.array = array;
        }

        public JSONArray getArray() {
            return array;
        }

        @Override
        public int getCount() {
            if(array!=null) {
                return array.length();
            }else{
                return 0;
            }
        }

        @Override
        public JSONObject getItem(int position) {
            if(array!=null) {
                try {
                    return array.getJSONObject(position);
                } catch (JSONException e) {
                    Log.e("ERRO",e.toString());
                    return null;
                }
            }else{
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }




        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_vlista_produto,parent,false);
            try {
                ((TextView) convertView.findViewById(R.id.vText_descricao)).setText(array.getJSONObject(position).getString("description"));
                ((TextView) convertView.findViewById(R.id.vText_codigo))   .setText(array.getJSONObject(position).getString("code"));
                ((TextView) convertView.findViewById(R.id.vText_valor))    .setText(NumberFormat.getCurrencyInstance().format(Float.parseFloat(
                        array.getJSONObject(position).getString("price").replaceAll(",","."))));
                conf.getImagem(((ImageView)convertView.findViewById(R.id.vImg)), array.getJSONObject(position).getString("image"));
            } catch (JSONException e) {
                Log.e("ERRO",e.toString());
            }

//            if(position%2>0){
//                convertView.setBackgroundColor(Color.rgb(245,245,245));
//            }else{
//                convertView.setBackgroundColor(Color.WHITE);
//            }

            return convertView;
        }
    }
}
