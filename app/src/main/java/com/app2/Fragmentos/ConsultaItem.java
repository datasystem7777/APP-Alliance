package com.app2.Fragmentos;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app2.Classes.PREFERENCIA;
import com.app2.Classes.myJSONArray;
import com.app2.Classes.myJSONObject;
import com.app2.TelaPedido;
import com.app2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultaItem extends Fragment {


    public ConsultaItem() {
        // Required empty public constructor
    }

    public ConsultaItem(myJSONObject pedido, myJSONArray itens) {
        this.pedido = pedido;
        this.itens = itens;
        try {
            id_order = pedido.getString("id_order");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String id_order;
    JSONArray itens;
    FloatingActionButton vFb_carrinho;



    ListView vListView;
    adapter adpListaItem;

    LinearLayout buscaProduto;

    Button vBtn_fechar,vBtn_cod_bar;
    FloatingActionButton vFb_editar;

    myJSONObject pedido;
    PREFERENCIA conf;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consulta_item, container, false);

        conf    = new PREFERENCIA(getContext());

        try {
            carregaDados();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        vFb_editar = view.findViewById(R.id.vFb_editar);
        vFb_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                        //((TelaPedido) getActivity()).consultaCondicao(pedido.getString("id_order"));
                        ((TelaPedido) getActivity()).consultaDetalhesPedido();
                }
            }
        });



        vListView = view.findViewById(R.id.vListView);
        adpListaItem = new adapter(itens);
        vListView.setAdapter(adpListaItem);

        vListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(getActivity()!=null){
                    try {
                        String ID_ITEM  = adpListaItem.getItem(position).getString("id");
                        ((TelaPedido)getActivity()).abrirItem(ID_ITEM,null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        vListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(getActivity()!=null){
                    try {
                        String ID_ITEM  = adpListaItem.getItem(position).getString("id");
                        ((TelaPedido)getActivity()).apagaItem(ID_ITEM);
                        return true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        vFb_carrinho = view.findViewById(R.id.vFb_carrinho);
        vFb_carrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    ((TelaPedido)getActivity()).abrirPedido(id_order);
                }
            }
        });



        buscaProduto = view.findViewById(R.id.busca);
        buscaProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    ((TelaPedido)getActivity()).consultaProduto();
                }
            }
        });


        vBtn_fechar = view.findViewById(R.id.vBtn_fechar);
        try {
            if(pedido.getString("SITUACAO").equals("ATIVO")){
                vBtn_fechar.setText("FECHAR PEDIDO");
            }else if(pedido.getString("SITUACAO").equals("RESERVADO")){
                vBtn_fechar.setText("REABRIR PEDIDO");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        vBtn_fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    ((TelaPedido)getActivity()).fecharPedido();
                }
            }
        });

        vBtn_cod_bar = view.findViewById(R.id.vBtn_cod_bar);
        vBtn_cod_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    ((TelaPedido)getActivity()).LeitorCodeBar();
                }
            }
        });

        return view;
    }

    TextView vText_total;
    private void carregaDados() throws JSONException {
        vText_total        = view.findViewById(R.id.vText_total        );

        vText_total        .setText(pedido.getMoney("total"));

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

        //A.id" +
        //    "  ,A.id_order" +
        //    "  ,A.price" +
        //    "  ,A.quantity" +
        //    "  ,A.price * A.quantity as 'total'" +
        //    "  ,B.description" +
        //    "  ,B.code
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_vlista_pedido_item,parent,false);
            try {
                ((TextView) convertView.findViewById(R.id.vText_descricao)) .setText(array.getJSONObject(position).getString("description"));
                ((TextView) convertView.findViewById(R.id.vText_codigo))    .setText(array.getJSONObject(position).getString("code"));
                ((TextView) convertView.findViewById(R.id.vText_quantidade)).setText(array.getJSONObject(position).getString("quantity"));
                ((TextView) convertView.findViewById(R.id.vText_valor))     .setText(NumberFormat.getCurrencyInstance().format(Float.parseFloat(
                        array.getJSONObject(position).getString("price").replaceAll(",","."))));
                ((TextView) convertView.findViewById(R.id.vText_total))     .setText(NumberFormat.getCurrencyInstance().format(Float.parseFloat(
                        array.getJSONObject(position).getString("total").replaceAll(",","."))));


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
