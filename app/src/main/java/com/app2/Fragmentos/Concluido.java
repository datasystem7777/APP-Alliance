package com.app2.Fragmentos;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app2.TelaPedido;
import com.app2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Concluido extends Fragment {


    public Concluido() {
        // Required empty public constructor
    }


    View view;
    ImageView vImg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_concluido, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!= null){
                    ((TelaPedido)getActivity()).telaAnterior();
                }
            }
        });

        vImg = view.findViewById(R.id.vImg);
        vImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!= null){
                    ((TelaPedido)getActivity()).telaAnterior();
                }
            }
        });
        return view;
    }

}
