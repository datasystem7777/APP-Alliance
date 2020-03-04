package com.app2.Fragmentos;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app2.Classes.PREFERENCIA;
import com.app2.Classes.myJSONObject;
import com.app2.MainActivity;
import com.app2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class TelaInicial extends Fragment {


    public TelaInicial() {
        // Required empty public constructor
    }

    public TelaInicial(myJSONObject indicadores) {
        this.indicadores = indicadores;
    }


    View view;
    Button vBtn_novo, vBtn_produto;
    TextView vText_pedido, vText_meta, vText_vendas,vText_progresso;
    ProgressBar progressBar;
    PREFERENCIA conf;
    myJSONObject usuarioJS;
    myJSONObject indicadores;
    ImageView olc_pedido, olc_meta, olc_vendas, olc_progresso;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tela_inicial, container, false);


        vBtn_novo       = view.findViewById(R.id.vBtn_novo);
        vBtn_produto    = view.findViewById(R.id.vBtn_produto);
        vText_pedido    = view.findViewById(R.id.vText_pedido);
        vText_meta      = view.findViewById(R.id.vText_meta);
        vText_vendas    = view.findViewById(R.id.vText_vendas);
        vText_progresso = view.findViewById(R.id.vText_progresso);
        olc_pedido      = view.findViewById(R.id.vBtn_ocl_pedido);
        olc_meta        = view.findViewById(R.id.vBtn_ocl_meta);
        olc_vendas      = view.findViewById(R.id.vBtn_ocl_vendas);
        olc_progresso   = view.findViewById(R.id.vBtn_ocl_progresso);

        progressBar     = view.findViewById(R.id.progressBar);

        if(getActivity() != null){
            conf      = ((MainActivity)getActivity()).conf;
            usuarioJS = ((MainActivity)getActivity()).usuarioJS;
        }else{
            conf    = new PREFERENCIA(getContext());
            usuarioJS = new myJSONObject();
        }


        if(indicadores!=null){
            try {
                if(conf.carregarValor("pedidoVISIVEL").equals("1")) {
                    pedidoSetText(indicadores.getString("PEDIDO"));
                }
                if(conf.carregarValor("metaVISIVEL").equals("1")) {
                    metaSetText(indicadores.getString("META"));
                }
                if(conf.carregarValor("vendasVISIVEL").equals("1")) {
                    vendasSetText(indicadores.getString("VENDA"));
                }
                if(conf.carregarValor("progressoVISIVEL").equals("1")) {
                    progressoSetText(indicadores.getString("PROGRESSO"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            vText_pedido   .setText("");
            vText_meta     .setText("");
            vText_vendas   .setText("");
            vText_progresso.setText("");
            progressBar    .setProgress(0);
        }


        olc_pedido   .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conf.carregarValor("pedidoVISIVEL").equals("1")){
                    conf.alterarValor("pedidoVISIVEL","0");
                    vText_pedido   .setText("");
                }else{
                    conf.alterarValor("pedidoVISIVEL","1");
                    try {
                        pedidoSetText(indicadores.getString("PEDIDO"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        olc_meta     .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conf.carregarValor("metaVISIVEL").equals("1")){
                    conf.alterarValor("metaVISIVEL","0");
                    vText_meta     .setText("");
                }else{
                    conf.alterarValor("metaVISIVEL","1");
                    try {
                        metaSetText(indicadores.getString("META"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        olc_vendas   .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conf.carregarValor("vendasVISIVEL").equals("1")){
                    conf.alterarValor("vendasVISIVEL","0");
                    vText_vendas   .setText("");
                }else{
                    conf.alterarValor("vendasVISIVEL","1");
                    try {
                        vendasSetText(indicadores.getString("VENDA"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        olc_progresso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conf.carregarValor("progressoVISIVEL").equals("1")){
                    conf.alterarValor("progressoVISIVEL","0");
                    vText_progresso.setText("");
                    progressBar    .setProgress(0);
                }else{
                    conf.alterarValor("progressoVISIVEL","1");
                    try {
                        progressoSetText(indicadores.getString("PROGRESSO"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        vBtn_novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    ((MainActivity)getActivity()).novoPedido();
                }
            }
        });
        vBtn_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    ((MainActivity)getActivity()).consultaProdutos();
                }
            }
        });
        return view;
    }

    private void progressoSetText(String progresso) {
        if(progresso!=null) {
            vText_progresso.setText(progresso + "%");
            progressBar    .setProgress(Integer.parseInt(progresso));
        }


    }

    private void vendasSetText(String venda) {
        if(venda!=null) {
            vText_vendas.setText(NumberFormat.getCurrencyInstance().format(Float.parseFloat(venda.replaceFirst("null","").replaceAll(",","."))));
        }
    }

    private void pedidoSetText(String pedido) {
        if(pedido!=null) {
            vText_pedido.setText(pedido + " Pedidos");
        }
    }

    private void metaSetText(String meta) {
        if(meta!=null) {
            vText_meta.setText(NumberFormat.getCurrencyInstance().format(Float.parseFloat(meta.replaceFirst("null","").replaceAll(",","."))));
        }
    }
}
