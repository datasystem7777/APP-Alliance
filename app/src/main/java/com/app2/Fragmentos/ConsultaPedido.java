package com.app2.Fragmentos;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app2.Classes.PREFERENCIA;
import com.app2.MainActivity;
import com.app2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultaPedido extends Fragment {


    public ConsultaPedido() {
        // Required empty public constructor
    }
    public ConsultaPedido(JSONArray array) {
        this.array = array;
    }

    JSONArray array;
    View view;
    ListView vList_pedidos;
    adapter adp;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consulta_pedidos, container, false);

        vList_pedidos     = view.findViewById(R.id.vList_pedidos);
        vList_pedidos.setFastScrollEnabled(true);

        ViewGroup footer = (ViewGroup)inflater.inflate(R.layout.listview_footer,vList_pedidos,false);
        vList_pedidos.addFooterView(footer,null,false);
        adp = new adapter(array);
        vList_pedidos.setAdapter(adp);

        vList_pedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ID = "";
                try {
                    ID = adp.getItem(position).getString("id_order");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(ID.length()>0){
                    if(getActivity()!=null){
                        ((MainActivity)getActivity()).abrirPedido(ID);
                    }
                }
            }
        });


        return view;
    }



    public class adapter extends BaseAdapter {

        JSONArray array;

        public adapter(JSONArray array){
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_vlista_pedidos,parent,false);

            try {
                if(array.getJSONObject(position).has("id_order")) {
                    ((TextView) convertView.findViewById(R.id.vText_num)).setText(array.getJSONObject(position).getString("id_order"));
                }
                if(array.getJSONObject(position).has("company_name")) {
                    ((TextView) convertView.findViewById(R.id.vText_nome))    .setText(array.getJSONObject(position).getString("company_name"));
                }
                if(array.getJSONObject(position).has("name")) {
                    ((TextView) convertView.findViewById(R.id.vText_situacao)).setText(array.getJSONObject(position).getString("name"));
                    switch (array.getJSONObject(position).getString("name")) {
                        case "ATIVO":
                            convertView.findViewById(R.id.vText_situacao).setBackgroundColor(Color.GREEN);
                            break;
                        case "CANCELADO":
                            convertView.findViewById(R.id.vText_situacao).setBackgroundColor(Color.RED);
                            break;
                        case "BLOQUEADO":
                            convertView.findViewById(R.id.vText_situacao).setBackgroundColor(Color.YELLOW);
                            break;
                        case "FECHADO":
                            convertView.findViewById(R.id.vText_situacao).setBackgroundColor(Color.GRAY);
                            break;
                    }
                }
//                vermelho para cancelado
//                amarelo para bloqueado
//                verde para Aberto
//                cinza para fechado

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
