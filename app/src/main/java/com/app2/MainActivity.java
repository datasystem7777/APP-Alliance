package com.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app2.Classes.PREFERENCIA;
import com.app2.Classes.myJSONArray;
import com.app2.Classes.myJSONObject;
import com.app2.Fragmentos.Cliente;
import com.app2.Fragmentos.ConsultaCliente;
import com.app2.Fragmentos.ConsultaPedido;
import com.app2.Fragmentos.ConsultaProduto;
import com.app2.Fragmentos.DetalheItem;
import com.app2.Fragmentos.Login;
import com.app2.Fragmentos.TelaInicial;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    public PREFERENCIA conf;
    public String      USUARIO = "";
    public String      SENHA   = "";
    public myJSONObject usuarioJS = new myJSONObject();

    LinearLayout cabecalho;
    LinearLayout.LayoutParams params;
    ImageView vBtn_menu, vBtn_perfil;
    Dialog dialogPerfil;

    Button vBtn_sair;
    TextView vText_nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        conf = new PREFERENCIA(getBaseContext());
        cabecalho = findViewById(R.id.cabecalho);
        vBtn_menu   = findViewById(R.id.vBtn_menu);
        vBtn_perfil = findViewById(R.id.vBtn_perfil);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);

        inicioAPP();

    }





    public void inicioAPP() {
        if(validaUsuario()){
                telaInicial();
                iniciaTela();
        }else{
            abrirFragmento(new Login());
        }
    }

    public boolean validaUsuario() {
        USUARIO = conf.carregarValor("USUARIO");
        SENHA   = conf.carregarValor("SENHA");
        if(USUARIO.length()>0 && SENHA.length()>0) {
            try {
                usuarioJS = new myJSONObject(conf.validaLogin(USUARIO,SENHA));
                if(usuarioJS.getBoolean("LOGADO")){
                    return true;
                }else{
                    showMessage("Usuário ou Senha invalidos");
                    return false;
                }
            } catch (JSONException e){
                e.printStackTrace();
                showMessage("Falha ao realizar login");
                return false;
            }
        }else{
            return false;
        }
    }

    public void logout() {
        conf.alterarValor("SENHA","");
        inicioAPP();
    }

    public void iniciaTela() {
        vBtn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultaPedidos();
            }
        });

        dialogPerfil  = new Dialog(MainActivity.this);
        Objects.requireNonNull(dialogPerfil.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialogPerfil.setContentView(R.layout.layout_perfil);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogPerfil.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogPerfil.getWindow().setAttributes(lp);

        vBtn_sair  = dialogPerfil.findViewById(R.id.vBtn_sair);
        vText_nome = dialogPerfil.findViewById(R.id.vText_nome);
        vBtn_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                dialogPerfil.dismiss();
            }
        });
        vBtn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPerfil.show();
            }
        });

        try {
            vText_nome.setText(usuarioJS.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
            vText_nome.setText("");
        }
    }


    myJSONArray Pedidos;
    public void consultaPedidos() {
        Pedidos = new myJSONArray();
        try {
            String JS = conf.getPedidos(usuarioJS.getString("users_id"));
            Pedidos = new myJSONArray(JS);
            abrirFragmento(new ConsultaPedido(Pedidos));
        } catch (JSONException e) {
            e.printStackTrace();
            showMessage("FALHA AO CARREGAR PEDIDOS");
        }

    }

    public void novoPedido() {
        abrirFragmento(new ConsultaCliente());
    }


    myJSONObject Cliente;
    public void abrirCadastro(String ID_CLIENTE) {
        String JS = conf.getCliente(ID_CLIENTE);
        if(ID_CLIENTE!=null) {
            try {
                Cliente = new myJSONObject(JS);
            } catch (JSONException e) {
                e.printStackTrace();
                Cliente = new myJSONObject();
            }
        }
        abrirFragmento(new Cliente(Cliente));
    }

    public void novoCliente() {
        abrirFragmento(new Cliente(null));
    }

    //myJSONObject Pedido;
    public void abrirPedido(String ID_PEDIDO) {
        Intent intent = new Intent(MainActivity.this,TelaPedido.class);
        intent.putExtra("ID",ID_PEDIDO);
        intent.putExtra("USUARIO",usuarioJS.toString());
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if(fragment!=null){
            if(fragment instanceof TelaInicial){
                superOnback();
            }else if(fragment instanceof Cliente){
                novoPedido();
            }else if(fragment instanceof ConsultaCliente){
                inicioAPP();
            }else if(fragment instanceof ConsultaPedido){
                inicioAPP();
            }else if(fragment instanceof DetalheItem){
                consultaProdutos();
            }else if(fragment instanceof ConsultaProduto){
                inicioAPP();
            }else if(fragment instanceof Login){
                superOnback();
            }else{
                superOnback();
            }
        }else{
            superOnback();
        }
    }

    private void superOnback() {
        finishAffinity();
    }

    myJSONObject indicadores;
    private void telaInicial() {
        params.weight=1;
        cabecalho.setLayoutParams(params);

        try {
            indicadores = new myJSONObject(conf.getIndicadores(usuarioJS.getString("users_id")));
        } catch (JSONException e) {
            e.printStackTrace();
            indicadores = null;
        }
        abrirFragmento(new TelaInicial(indicadores));
    }


    public void abrirNovoPedido(String ID_CLIENTE) {
        try {
            String ID_PEDIDO = conf.cadastrarPedido(ID_CLIENTE,usuarioJS.getString("users_id"));
            if(ID_PEDIDO.contains("ERRO") || ID_PEDIDO.contains("FALHA")){
                showMessage(ID_PEDIDO);
            }else {
                abrirPedido(ID_PEDIDO);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void cadastrarCliente(JSONObject CLIENTE, boolean GERAR_PEDIDO) {
        try {
            String ID_CLIENTE = conf.cadastrarCliente(CLIENTE,usuarioJS.getString("users_id"));
            if(ID_CLIENTE.contains("ERRO") || ID_CLIENTE.contains("FALHA")){
                showMessage(ID_CLIENTE);
            }else {
                if (GERAR_PEDIDO) {
                    abrirNovoPedido(ID_CLIENTE);
                } else {
                    abrirCadastro(ID_CLIENTE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fecharTeclado(View view) {
        ((InputMethodManager) Objects.requireNonNull(getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(
                view.getWindowToken(), 0);
    }

    public void ocultaTopo() {
        params.weight=0;
        cabecalho.setLayoutParams(params);
    }

    public void mostraTopo() {
        params.weight=1;
        cabecalho.setLayoutParams(params);
    }



    public void abrirFragmento(Fragment fragment){
        if(fragment instanceof TelaInicial){
            mostraTopo();
        }else{
            ocultaTopo();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
    }



    public void LeitorCodeBar(){
        IntentIntegrator intent = new IntentIntegrator(MainActivity.this);
        intent.setOrientationLocked(false);
        intent.initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            final String barcode = result.getContents();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tratarRetornoLeitor(barcode);
                }
            },500);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void tratarRetornoLeitor(String barcode) {

        if (barcode != null){
            abrirProduto(barcode);
        }else{
            showMessage("Falha na leitura");
            consultaProdutos();
        }
    }



    private void abrirProduto(String barcode) {
        String id_produto;
        String JS;
        JS = conf.getIdProduto(barcode);
        try {
            id_produto = new myJSONObject(JS).getString("product_id");
            if(!id_produto.equals("0")) {
                abrirItem(null,id_produto);
            }else{
                showMessage("PRODUTO NÃO ENCONTRADO");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showMessage("FALHA AO CARREGAR DADOS");
        }
    }

    myJSONObject Item;
    public void abrirItem(String id_item,String id_produto) {
        String JS;
        try {
            if(id_item!=null) {
                JS = conf.getItemPedido(id_item);
            }else {
                JS = conf.getProduto(id_produto);
            }
            Item = new myJSONObject(JS);
            abrirFragmento(new DetalheItem(Item));
        } catch (JSONException e) {
            e.printStackTrace();
            showMessage("FALHA AO CARREGAR DADOS DO ITEM");
        }
    }

    public void consultaProdutos(){
        abrirFragmento(new ConsultaProduto());
    }


    public void showMessage(String message) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
        dlg.setMessage(message);
        dlg.setPositiveButton("OK", null);
        dlg.create().show();
    }
}
