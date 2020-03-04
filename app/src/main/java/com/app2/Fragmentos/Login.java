package com.app2.Fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.app2.Classes.PREFERENCIA;
import com.app2.MainActivity;
import com.app2.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {


    public Login() {
        // Required empty public constructor
    }

    View view;
    public PREFERENCIA conf;
    public String      USUARIO = "";
    public String      SENHA   = "";
    public String      IP      = "";
    public String      PORTA   = "";
    EditText vEdit_usuario, vEdit_senha, vEdit_porta, vEdit_ip;
    Button vBtn_entrar;
    LinearLayout vCaixa_ip,vCaixa_ip2;
    ImageView vBtn_conf;
    boolean oculto = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        if(getActivity() != null){
            ((MainActivity)getActivity()).ocultaTopo();
            conf    = ((MainActivity)getActivity()).conf;
            USUARIO = ((MainActivity)getActivity()).USUARIO;
            SENHA   = ((MainActivity)getActivity()).SENHA;
        }else{
            conf = new PREFERENCIA(getContext());
        }
        PORTA = conf.carregarValor("PORTA");
        IP    = conf.carregarValor("IP"   );

        vEdit_usuario = view.findViewById(R.id.vEdit_usuario );
        vEdit_senha   = view.findViewById(R.id.vEdit_senha   );
        vEdit_porta   = view.findViewById(R.id.vEdit_porta   );
        vEdit_ip      = view.findViewById(R.id.vEdit_ip      );

        vBtn_entrar   = view.findViewById(R.id.vBtn_entrar   );

        vEdit_usuario.setText(USUARIO);
        vEdit_senha  .setText(SENHA  );
        vEdit_ip     .setText(IP);
        vEdit_porta  .setText(PORTA);

        vBtn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                USUARIO = vEdit_usuario.getText().toString();
                SENHA   = vEdit_senha  .getText().toString();
                IP      = vEdit_ip     .getText().toString();
                PORTA   = vEdit_porta  .getText().toString();
                if(USUARIO.length()>0 && SENHA.length()>0) {
                    conf.alterarValor("USUARIO", USUARIO);
                    conf.alterarValor("SENHA",   SENHA);
                    conf.alterarValor("IP",      IP);
                    conf.alterarValor("PORTA",   PORTA);
                    if(getActivity()!=null){
                        ((MainActivity)getActivity()).inicioAPP();
                    }
                }else{
                    if(getActivity()!=null){
                        ((MainActivity)getActivity()).showMessage("PREENCHA TODOS OS CAMPOS");
                    }
                }
            }
        });


        vCaixa_ip = view.findViewById(R.id.vCaixa_ip);
        vCaixa_ip2 = view.findViewById(R.id.vCaixa_ip2);
        vCaixa_ip .setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0));
        vCaixa_ip2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0));
        vBtn_conf = view.findViewById(R.id.vBtn_conf);
        vBtn_conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oculto) {
                    vCaixa_ip .setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    vCaixa_ip2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }else{
                    vCaixa_ip .setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0));
                    vCaixa_ip2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0));
                }
                oculto = !oculto;
            }
        });

        return view;
    }

}
