package com.app2.Fragmentos;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app2.Classes.myJSONObject;
import com.app2.R;
import com.app2.TelaPedido;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;


/**
 * A simple {@link Fragment} subclass.
 */
public class Pedido extends Fragment {


    public Pedido() {
        // Required empty public constructor
    }


    public Pedido(myJSONObject pedido) {
        this.pedido = pedido;
    }



    View view;
    FloatingActionButton vFb_carrinho;

    LinearLayout buscaProduto;

    Button vBtn_fechar,vBtn_cod_bar;
    FloatingActionButton vFb_editar;

    myJSONObject pedido;

    LinearLayout vCaixa_condicao, vCaixa_atual, vCaixa_futuro;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pedido, container, false);

        if(pedido!=null) {
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
                            ((TelaPedido) getActivity()).consultaDetalhesPedido();
                    }
                }
            });


            vFb_carrinho = view.findViewById(R.id.vFb_carrinho);
            vFb_carrinho.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        try {
                            ((TelaPedido) getActivity()).consultaItem(pedido.getString("id_order"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


            buscaProduto = view.findViewById(R.id.busca);
            buscaProduto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        ((TelaPedido) getActivity()).consultaProduto();
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
                    if (getActivity() != null) {
                        ((TelaPedido) getActivity()).LeitorCodeBar();
                    }
                }
            });

            vCaixa_condicao = view.findViewById(R.id.vCaixa_condicao);
            vCaixa_condicao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        try {
                            ((TelaPedido) getActivity()).consultaCondicao(pedido.getString("id_order"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            vCaixa_atual = view.findViewById(R.id.vCaixa_atual);
            vCaixa_atual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        try {
                            ((TelaPedido) getActivity()).consultaItem(pedido.getString("id_order"),"A");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            vCaixa_futuro = view.findViewById(R.id.vCaixa_futuro);
            vCaixa_futuro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        try {
                            ((TelaPedido) getActivity()).consultaItem(pedido.getString("id_order"),"F");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }
        return view;
    }

    TextView vText_representante, vText_pedido, vText_cliente_nome, vText_cliente_cnpj, vText_condicao, vText_itens_futuro, vText_valor_futuro, vText_itens_atual, vText_valor_atual,vText_total;
    private void carregaDados() throws JSONException {
        vText_representante = view.findViewById(R.id.vText_representante);
        vText_pedido        = view.findViewById(R.id.vText_pedido       );
        vText_cliente_nome  = view.findViewById(R.id.vText_cliente_nome );
        vText_cliente_cnpj  = view.findViewById(R.id.vText_cliente_cnpj );
        vText_condicao      = view.findViewById(R.id.vText_condicao     );
        vText_itens_futuro  = view.findViewById(R.id.vText_itens_futuro );
        vText_valor_futuro  = view.findViewById(R.id.vText_valor_futuro );
        vText_itens_atual   = view.findViewById(R.id.vText_itens_atual  );
        vText_valor_atual   = view.findViewById(R.id.vText_valor_atual  );
        vText_total         = view.findViewById(R.id.vText_total        );


        vText_pedido        .setText("PEDIDO #"+pedido.getString("id_order"));
        vText_representante .setText("Rep. :"+pedido.getString("user_name"));
        vText_cliente_nome  .setText(pedido.getString("company_name"));
        vText_cliente_cnpj  .setText(pedido.getString("cnpj"));
        vText_condicao      .setText(pedido.getString("payment"));
        vText_itens_futuro  .setText(pedido.getString("futuro"));
        vText_itens_atual   .setText(pedido.getString("atual"));

        vText_valor_futuro  .setText(pedido.getMoney("val_futuro"));
        vText_valor_atual   .setText(pedido.getMoney("val_atual"));
        vText_total         .setText(pedido.getMoney("total"));


    }

}
