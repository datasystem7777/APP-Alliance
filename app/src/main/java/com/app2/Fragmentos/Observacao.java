package com.app2.Fragmentos;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app2.Classes.myJSONObject;
import com.app2.TelaPedido;
import com.app2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;


/**
 * A simple {@link Fragment} subclass.
 */
public class Observacao extends Fragment {


    public Observacao() {
        // Required empty public constructor
    }


    public Observacao(myJSONObject pedido) {
        this.pedido = pedido;
    }



    View view;

    myJSONObject pedido;

    EditText vEdit_obs;
    Button vBtn_Salvar, vBtn_Cancelar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_observacao, container, false);

        vEdit_obs     = view.findViewById(R.id.vEdit_obs);
        vBtn_Salvar   = view.findViewById(R.id.vBtn_Salvar);
        vBtn_Cancelar = view.findViewById(R.id.vBtn_Cancelar);

        if(pedido!=null) {
            try {
                vEdit_obs     .setText(pedido.getString("obs"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            vBtn_Salvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity()!=null){
                        try {
                            ((TelaPedido)getActivity()).salvaObservacao(pedido.getString("id_order"),vEdit_obs.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


            vBtn_Cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                            ((TelaPedido) getActivity()).consultaDetalhesPedido();
                    }
                }
            });

        }
        return view;
    }


}
