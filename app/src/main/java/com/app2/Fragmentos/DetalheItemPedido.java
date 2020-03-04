package com.app2.Fragmentos;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app2.Classes.PREFERENCIA;
import com.app2.Classes.myJSONObject;
import com.app2.MainActivity;
import com.app2.R;
import com.app2.TelaPedido;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalheItemPedido extends Fragment {


    public DetalheItemPedido() {
        // Required empty public constructor
    }

    public DetalheItemPedido(JSONObject item) {
        this.item = item;
    }

    String id_produto;
    String ID_ORDER;
    String TELA_ANT;

    String id_item;

    View view;
    ImageView vBtn_confirmar;
    PREFERENCIA conf;


    JSONObject item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detalhe_item_pedido, container, false);

        conf    = new PREFERENCIA(getContext());
        vBtn_confirmar = view.findViewById(R.id.vBtn_confirmar);
        if(item!=null) {
            carregaDados();
        }

        vBtn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    try {
                        if(Integer.parseInt(vEdit_quantidade.getText().toString())>0) {
                            if(Integer.parseInt(vEdit_quantidade.getText().toString())%item.getInt("multiple")==0) {
                                myJSONObject newItem = new myJSONObject();
                                newItem.put("obs", "")
                                        .put("id_item", item.getString("id"))
                                        .put("id_pedido", ((TelaPedido) getActivity()).ID_PEDIDO)
                                        .put("id_produto", item.getString("product_id"))
                                        .put("qtde", vEdit_quantidade.getText().toString());
                                ((TelaPedido) getActivity()).gravaItem(newItem.toString());
                            }else{
                                Toast.makeText(getContext(),"QUANTIDADE INVALIDA",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getContext(),"QUANTIDADE DEVE SER MAIOR QUE ZERO",Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return view;
    }

    EditText vEdit_quantidade;
    TextView vText_codigo,vText_descricao,vText_valor,vText_disponivel,vText_futuro,vText_multiplo,vText_ipi,vText_minimo,vText_unit,
            vText_dt_atual,vText_dt_futuro;//vText_st
    ImageView vBtn_mais,vBtn_menos, vImg;
    float PRECO = 0;
    int QTDE  = 0;
    int STOCK  = 0;
    int FSTOCK  = 0;
    //{"quantity":"5","code":"SA-M198","ipi":"0","multiple":"1","description":"LL Mountain Seat Assembly","stock_future":"100","id_order":"1","total":"665",
    //       "id_product":"2","detail_description":"LL Mountain Seat Assembly","min_quantity":"1","price":"133","id":"1","stock":"500"}
    private void carregaDados() {
        vEdit_quantidade = view.findViewById(R.id.vEdit_quantidade);
        vText_codigo     = view.findViewById(R.id.vText_codigo);
        vText_descricao  = view.findViewById(R.id.vText_descricao);
        vText_valor      = view.findViewById(R.id.vText_valor);
        vText_disponivel = view.findViewById(R.id.vText_disponivel);
        vText_futuro     = view.findViewById(R.id.vText_futuro);
        vText_multiplo   = view.findViewById(R.id.vText_multiplo);
        vText_ipi        = view.findViewById(R.id.vText_ipi);
        vText_minimo     = view.findViewById(R.id.vText_minimo);
        vBtn_mais        = view.findViewById(R.id.vBtn_mais);
        vBtn_menos       = view.findViewById(R.id.vBtn_menos);
        vText_unit       = view.findViewById(R.id.vText_unit);
        vText_dt_atual   = view.findViewById(R.id.vText_dt_atual);
        vText_dt_futuro  = view.findViewById(R.id.vText_dt_futuro);
        vImg             = view.findViewById(R.id.vImg);
      //  vText_st         = view.findViewById(R.id.vText_st);

        try {
            vEdit_quantidade .setText(item.getString("quantity").replaceFirst("null",""));
            vText_codigo     .setText(item.getString("code").replaceFirst("null",""));
            vText_descricao  .setText(item.getString("description").replaceFirst("null",""));
            vText_disponivel .setText(item.getString("stock").replaceFirst("null",""));
            vText_futuro     .setText(item.getString("stock_future").replaceFirst("null",""));
            vText_multiplo   .setText(item.getString("multiple").replaceFirst("null",""));
            vText_minimo     .setText(item.getString("min_quantity").replaceFirst("null",""));
            vText_unit       .setText(NumberFormat.getCurrencyInstance().format(Float.parseFloat(item.getString("priceNoIpi").replaceFirst("null","").replaceAll(",","."))));
            PRECO           = Float.parseFloat(item.getString("price").replaceFirst("null","0"));
            QTDE            = Integer.parseInt(item.getString("quantity").replaceFirst("null","0"));
            STOCK            = Integer.parseInt(item.getString("stock").replaceFirst("null","0"));
            FSTOCK           = Integer.parseInt(item.getString("stock_future").replaceFirst("null","0"));
            vText_valor      .setText(NumberFormat.getCurrencyInstance().format(Float.parseFloat(item.getString("total").replaceFirst("null","").replaceAll(",","."))));

            vText_dt_atual   .setText(item.getString("date_stock").replaceFirst("null",""));
            vText_dt_futuro  .setText(item.getString("date_stock_future").replaceFirst("null",""));
          //  vText_st         .setText(item.getString("st").replaceFirst("null",""));

            vText_ipi        .setText("(+"+item.getString("ipi")+"% IPI)");
            conf.getImagem(vImg, item.getString("image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        vBtn_mais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    QTDE = QTDE + item.getInt("multiple");
                    if(QTDE>(STOCK+FSTOCK)){
                        QTDE = QTDE - item.getInt("multiple");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }
                vEdit_quantidade.setText(String.valueOf(QTDE));
                vText_valor.setText(NumberFormat.getCurrencyInstance().format(PRECO*QTDE));
            }
        });

        vBtn_menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    QTDE = QTDE-item.getInt("multiple");
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                if(QTDE<0){
                    QTDE = 0;
                }
                vEdit_quantidade.setText(String.valueOf(QTDE));
                vText_valor.setText(NumberFormat.getCurrencyInstance().format(PRECO*QTDE));
            }
        });
        vEdit_quantidade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if(Integer.parseInt("0" + vEdit_quantidade.getText().toString())<=(STOCK+FSTOCK)){
                        QTDE = Integer.parseInt("0" + vEdit_quantidade.getText().toString());
                    }else{
                        QTDE = STOCK+FSTOCK;
                        vEdit_quantidade.setText(String.valueOf(QTDE));
                    }

                }catch (NumberFormatException e) {
                    Log.e("ERRO",e.toString());
                }
                vText_valor.setText(NumberFormat.getCurrencyInstance().format(PRECO*QTDE));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


}
