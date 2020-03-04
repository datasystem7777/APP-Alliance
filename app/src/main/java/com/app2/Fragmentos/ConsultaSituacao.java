package com.app2.Fragmentos;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app2.Classes.PREFERENCIA;
import com.app2.TelaPedido;
import com.app2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultaSituacao extends Fragment {


    public ConsultaSituacao() {
        // Required empty public constructor
    }

    public ConsultaSituacao(String id_order) {
        this.id_order = id_order;
    }

    String id_order;

    View view;
    PREFERENCIA conf;
    JSONObject usuarioJS;


    ListView vListView;
    adapterCondicao adpListaCondicaoPag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consulta_situacao, container, false);
        JSONArray array;

        if(getActivity() != null){
            conf      = ((TelaPedido)getActivity()).conf;
            usuarioJS = ((TelaPedido)getActivity()).usuarioJS;
        }else{
            conf    = new PREFERENCIA(getContext());
            usuarioJS = new JSONObject();
        }
        try {
            array = new JSONArray("[{'id_order_status':'3','name':'CANCELAR'},{'id_order_status':'5','name':'RESERVAR'}]");
        } catch (JSONException e) {
            e.printStackTrace();
            array = new JSONArray();
        }


        vListView = view.findViewById(R.id.vListView);
        adpListaCondicaoPag = new adapterCondicao(array);
        vListView.setAdapter(adpListaCondicaoPag);



        vListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(getActivity()!=null){
                    try {
                        String id_situacao = adpListaCondicaoPag.getItem(position).getString("id_order_status");
                        String id_pedido   = id_order;
                        ((TelaPedido)getActivity()).confirmaSituacao(id_situacao,id_pedido);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        return view;
    }



    public class adapterCondicao extends BaseAdapter {

        JSONArray array;

        public adapterCondicao(JSONArray array){
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

        //        vText_nome
//        vText_documento
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_vlista_condicao_pag,parent,false);

            try {
                ((TextView) convertView.findViewById(R.id.vText_descricao)).setText(array.getJSONObject(position).getString("name"));
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
