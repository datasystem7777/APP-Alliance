package com.app2.Fragmentos;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app2.Classes.PREFERENCIA;
import com.app2.Classes.myJSONArray;
import com.app2.Classes.myJSONObject;
import com.app2.MainActivity;
import com.app2.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */

public class Cliente extends Fragment {


    public Cliente() {
        // Required empty public constructor
    }


    public Cliente(myJSONObject cliente) {
        this.cliente = cliente;
        // Required empty public constructor
        if(cliente == null || cliente.length()==0){
            TEXTO_BTN = "CADASTRAR CLIENTE";
            NOVO = true;
        }else{
            TEXTO_BTN = "ABRIR PEDIDO";
            NOVO = false;
        }
    }


    String TEXTO_BTN = "ABRIR PEDIDO";
    boolean NOVO = false;
    boolean ADD_TRANSP = false;

    Button vBtn_confirmar;
    View view;

    myJSONObject cliente;
    PREFERENCIA conf;

    Button vBtn_cep, vBtn_cnpj, vBtn_transportadora;
    CheckBox vCheck_cliente;
    adapter adp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cliente, container, false);
        if(getActivity()!= null){
            conf = ((MainActivity)getActivity()).conf;
        }else{
            conf = new PREFERENCIA(getContext());
        }

        carregaDados();

        vCheck_cliente = view.findViewById(R.id.vCheck_cliente);
        if(!NOVO){
            vCheck_cliente.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        }else{
            vCheck_cliente.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        vBtn_confirmar = view.findViewById(R.id.vBtn_confirmar);
        vBtn_confirmar.setText(TEXTO_BTN);
        vBtn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    if(NOVO){
                        try {
                            myJSONObject NOVO_CLIENTE = (myJSONObject) new myJSONObject()
                            .put("company_name"      , company_name       .getText().toString())
                            .put("company_fancy_name", company_fancy_name .getText().toString())
                            .put("buyers_name"       , buyers_name        .getText().toString())
                            .put("contact_name"      , contact_name       .getText().toString())
                            .put("cnpj"              , cnpj               .getText().toString())
                            .put("inscricao_estadual", inscricao_estadual .getText().toString())
                            .put("email"             , email              .getText().toString())
                            .put("ddd"               , ddd                .getText().toString())
                            .put("fone1"             , fone1              .getText().toString())
                            .put("fone2"             , fone2              .getText().toString())
                            .put("obs"               , obs                .getText().toString())
                            .put("address"           , address            .getText().toString())
                            .put("neighborhood"      , neighborhood       .getText().toString())
                            .put("city"              , city               .getText().toString())
                            .put("uf"                , uf                 .getText().toString())
                            .put("postal_code"       , postal_code        .getText().toString())
                            .put("new_tranpost"      , ADD_TRANSP                              );

                            if(ADD_TRANSP) {
                                NOVO_CLIENTE.put("tranpost_company"  , vEdit_transportadora.getText().toString());
                            }else{
                                NOVO_CLIENTE.put("tranpost_company"  , adp.getItem(vSpinner_transportadora.getSelectedItemPosition()).getString("id"));
                            }
                            ((MainActivity)getActivity()).cadastrarCliente(NOVO_CLIENTE,vCheck_cliente.isChecked());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            String ID_CLIENTE = cliente.getString("customer_id");
                            ((MainActivity)getActivity()).abrirNovoPedido(ID_CLIENTE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        vBtn_cep = view.findViewById(R.id.vBtn_cep);
        vBtn_cnpj = view.findViewById(R.id.vBtn_cnpj);
        vBtn_transportadora = view.findViewById(R.id.vBtn_transportadora);

        vBtn_cnpj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NOVO) {
                    try {
                        myJSONObject EMPRESA = new myJSONObject(conf.consultaCNPJ(cnpj.getText().toString()));
                        company_name       .setText(EMPRESA.getString("nome"    ));
                        company_fancy_name .setText(EMPRESA.getString("fantasia"));
                        postal_code        .setText(EMPRESA.getString("cep").replace(".",""));
                        address            .setText(EMPRESA.getString("logradouro"));
                        neighborhood       .setText(EMPRESA.getString("bairro"));
                        city               .setText(EMPRESA.getString("municipio"));
                        uf                 .setText(EMPRESA.getString("uf"));
                        cnpj               .setText(EMPRESA.getString("cnpj"));
                        if(getActivity()!=null) {
                            ((MainActivity) getActivity()).fecharTeclado(cnpj);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        vBtn_cep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NOVO) {
                    try {
                        myJSONObject ENDERECO = new myJSONObject(conf.consultaCEP(postal_code.getText().toString()));
                        address.setText(ENDERECO.getString("logradouro"));
                        neighborhood.setText(ENDERECO.getString("bairro"));
                        city.setText(ENDERECO.getString("localidade"));
                        uf.setText(ENDERECO.getString("uf"));
                        if(getActivity()!=null) {
                            ((MainActivity) getActivity()).fecharTeclado(postal_code);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        vBtn_transportadora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NOVO) {
                    if(!ADD_TRANSP) {
                        vSpinner_transportadora.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0));
                        vEdit_transportadora   .setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }else{
                        vEdit_transportadora   .setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0));
                        vSpinner_transportadora.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                    ADD_TRANSP = !ADD_TRANSP;
                }
            }
        });

        return view;
    }

    EditText  company_name ,company_fancy_name ,buyers_name ,contact_name ,cnpj ,inscricao_estadual ,email ,ddd ,fone1 ,fone2 ,obs,
        address,neighborhood, city, uf, postal_code, vEdit_transportadora;
    Spinner vSpinner_transportadora;
    private void carregaDados() {
        company_name            = view.findViewById(R.id.vEdit_razao_social);
        company_fancy_name      = view.findViewById(R.id.vEdit_nome_fantasia);
        buyers_name             = view.findViewById(R.id.vEdit_nome_comprador);
        contact_name            = view.findViewById(R.id.vEdit_nome_contato);
        cnpj                    = view.findViewById(R.id.vEdit_cnpj);
        inscricao_estadual      = view.findViewById(R.id.vEdit_inscricao_estadual);
        email                   = view.findViewById(R.id.vEdit_email);
        ddd                     = view.findViewById(R.id.vEdit_ddd);
        fone1                   = view.findViewById(R.id.vEdit_telefone1);
        fone2                   = view.findViewById(R.id.vEdit_telefone2);
        obs                     = view.findViewById(R.id.vEdit_observacao);
        address                 = view.findViewById(R.id.vEdit_endereco);
        neighborhood            = view.findViewById(R.id.vEdit_bairro);
        city                    = view.findViewById(R.id.vEdit_cidade);
        uf                      = view.findViewById(R.id.vEdit_uf);
        postal_code             = view.findViewById(R.id.vEdit_cep);
        vSpinner_transportadora = view.findViewById(R.id.vSpinner_transportadora);
        vEdit_transportadora    = view.findViewById(R.id.vEdit_transportadora);

        if(NOVO || cliente==null || cliente.length()==0){
            company_name       .setText("");
            company_fancy_name .setText("");
            buyers_name        .setText("");
            contact_name       .setText("");
            cnpj               .setText("");
            inscricao_estadual .setText("");
            email              .setText("");
            ddd                .setText("");
            fone1              .setText("");
            fone2              .setText("");
            obs                .setText("");
            address            .setText("");
            neighborhood       .setText("");
            city               .setText("");
            uf                 .setText("");
            postal_code        .setText("");
            try {
                adp = new adapter(new myJSONArray(conf.getTransportadoras(null)));
                vSpinner_transportadora.setAdapter(adp );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                company_name       .setText(cliente.getString("company_name"      ));
                company_fancy_name .setText(cliente.getString("company_fancy_name"));
                buyers_name        .setText(cliente.getString("buyers_name"       ));
                contact_name       .setText(cliente.getString("contact_name"      ));
                cnpj               .setText(cliente.getString("cnpj"              ));
                inscricao_estadual .setText(cliente.getString("inscricao_estadual"));
                email              .setText(cliente.getString("email"             ));
                ddd                .setText(cliente.getString("ddd"               ));
                fone1              .setText(cliente.getString("fone1"             ));
                fone2              .setText(cliente.getString("fone2"             ));
                obs                .setText(cliente.getString("obs"               ));
                address            .setText(cliente.getString("address"           ));
                neighborhood       .setText(cliente.getString("neighborhood"      ));
                city               .setText(cliente.getString("city"              ));
                uf                 .setText(cliente.getString("uf"                ));
                postal_code        .setText(cliente.getString("postal_code"       ));
                adp = new adapter(new myJSONArray(conf.getTransportadoras(cliente.getString("customer_id"))));
                vSpinner_transportadora.setAdapter(adp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        company_name       .setEnabled(NOVO);
        company_fancy_name .setEnabled(NOVO);
        buyers_name        .setEnabled(NOVO);
        contact_name       .setEnabled(NOVO);
        cnpj               .setEnabled(NOVO);
        inscricao_estadual .setEnabled(NOVO);
        email              .setEnabled(NOVO);
        ddd                .setEnabled(NOVO);
        fone1              .setEnabled(NOVO);
        fone2              .setEnabled(NOVO);
        obs                .setEnabled(NOVO);
        address            .setEnabled(NOVO);
        neighborhood       .setEnabled(NOVO);
        city               .setEnabled(NOVO);
        uf                 .setEnabled(NOVO);
        postal_code        .setEnabled(NOVO);
    }




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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_item_um,parent,false);

            try {
                ((TextView) convertView.findViewById(R.id.text)).setText(array.getJSONObject(position).getString("transport_company"));
            } catch (JSONException e) {
                Log.e("ERRO",e.toString());
            }

            return convertView;
        }
    }
}

