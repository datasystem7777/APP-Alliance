package com.app2.Fragmentos;


import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;

import com.app2.Classes.PREFERENCIA;
import com.app2.Classes.myJSONArray;
import com.app2.Classes.myJSONObject;
import com.app2.MainActivity;
import com.app2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultaCliente extends Fragment {


    public ConsultaCliente() {
        // Required empty public constructor
    }


    View view;
    EditText vEdit_busca;
    ListView vList_cliente;
    adapter adp;
    myJSONArray array;
    FloatingActionButton vFbtn_novo;

    PREFERENCIA conf;
    myJSONObject usuarioJS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consulta_cliente, container, false);
        if(getActivity() != null){
            conf      = ((MainActivity)getActivity()).conf;
            usuarioJS = ((MainActivity)getActivity()).usuarioJS;
        }else{
            conf    = new PREFERENCIA(getContext());
            usuarioJS = new myJSONObject();
        }

        array = new myJSONArray();


        vEdit_busca    = view.findViewById(R.id.vEdit_busca);
        vList_cliente  = view.findViewById(R.id.vList_cliente);

        vList_cliente.setFastScrollEnabled(true);

        ViewGroup footer = (ViewGroup)inflater.inflate(R.layout.listview_footer,vList_cliente,false);
        vList_cliente.addFooterView(footer,null,false);
        adp = new adapter(array);
        vList_cliente.setAdapter(adp);
        vEdit_busca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(vEdit_busca.getText().toString().length()>0){
                    try {
                        array = new myJSONArray(conf.getClientes(vEdit_busca.getText().toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        array = new myJSONArray();
                    }
                    adp = new adapter(array);
                    vList_cliente.setAdapter(adp);
                }else{
                    array = new myJSONArray();
                    adp = new adapter(array);
                    vList_cliente.setAdapter(adp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

/*
        vEdit_busca.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    carregaLista();
                }else if(vEdit_busca.getText().toString().length()==0){
                    escondeLista();
                }
            }
        });
*/
        vList_cliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(getActivity()!=null){
                    try {
                        String ID_CLIENTE = adp.getItem(position).getString("customer_id");
                        ((MainActivity)getActivity()).abrirCadastro(ID_CLIENTE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        vFbtn_novo = view.findViewById(R.id.vFbtn_novo);
        vFbtn_novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    ((MainActivity)getActivity()).novoCliente();
                }
            }
        });
        return view;
    }
/*
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
    public void carregaLista() {
        params.weight = 5;
        vList_cliente.setLayoutParams(params);
        Toast.makeText(getContext(),"CARREGA",Toast.LENGTH_LONG).show();
    }

    public void escondeLista() {
        params.weight = 0;
        vList_cliente.setLayoutParams(params);
        Toast.makeText(getContext(),"ESCONDE",Toast.LENGTH_LONG).show();
    }
*/




    public class adapter extends BaseAdapter {

        myJSONArray array;

        public adapter(myJSONArray array){
            this.array = array;
        }

        public myJSONArray getArray() {
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
        public myJSONObject getItem(int position) {
            if(array!=null) {
                try {
                    return new myJSONObject(array.getJSONObject(position).toString());
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_vlista_clientes,parent,false);

            try {
                ((TextView) convertView.findViewById(R.id.vText_documento)).setText(array.getJSONObject(position).getString("cnpj"));
                ((TextView) convertView.findViewById(R.id.vText_nome))     .setText(array.getJSONObject(position).getString("company_name"));
            } catch (JSONException e) {
                Log.e("ERRO",e.toString());
            }

            if(position%2>0){
                convertView.setBackgroundColor(Color.rgb(245,245,245));
            }else{
                convertView.setBackgroundColor(Color.WHITE);
            }

            return convertView;
        }
    }
}
