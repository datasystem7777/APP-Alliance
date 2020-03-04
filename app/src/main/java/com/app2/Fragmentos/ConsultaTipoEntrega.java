package com.app2.Fragmentos;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app2.Classes.PREFERENCIA;
import com.app2.TelaPedido;
import com.app2.R;
/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultaTipoEntrega extends Fragment {


    public ConsultaTipoEntrega() {
        // Required empty public constructor
    }

    View view;
    PREFERENCIA conf;
    String [] lista =  new String[]{"PARCIAL","INTEGRAL"};


    ListView vListView;
    adapterCondicao adpListaCondicaoPag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consulta_tipo_entrega, container, false);

        vListView = view.findViewById(R.id.vListView);
        adpListaCondicaoPag = new adapterCondicao(lista);
        vListView.setAdapter(adpListaCondicaoPag);



        vListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(getActivity()!=null){
                    String tipo = lista[position];
                    if(tipo.equals("PARCIAL")){
                        tipo = "P";
                    }else{
                        tipo = "I";
                    }
                    ((TelaPedido)getActivity()).confirmaTipoEntraga(tipo);
                }
            }
        });



        return view;
    }



    public class adapterCondicao extends BaseAdapter {

        String[] lista;

        public adapterCondicao(String[] lista){
            this.lista = lista;
        }

        public String[] getArray() {
            return lista;
        }

        @Override
        public int getCount() {
            if(lista!=null) {
                return lista.length;
            }else{
                return 0;
            }
        }

        @Override
        public String getItem(int position) {
            return lista[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_vlista_condicao_pag,parent,false);
            ((TextView) convertView.findViewById(R.id.vText_descricao)).setText(lista[position]);

//            if(position%2>0){
//                convertView.setBackgroundColor(Color.rgb(245,245,245));
//            }else{
//                convertView.setBackgroundColor(Color.WHITE);
//            }

            return convertView;
        }
    }


}
