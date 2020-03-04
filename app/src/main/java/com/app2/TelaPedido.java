package com.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.app2.Classes.PREFERENCIA;
import com.app2.Classes.myJSONArray;
import com.app2.Classes.myJSONObject;
import com.app2.Fragmentos.Concluido;
import com.app2.Fragmentos.ConsultaCondicao;
import com.app2.Fragmentos.ConsultaItem;
import com.app2.Fragmentos.ConsultaProdutoPedido;
import com.app2.Fragmentos.ConsultaSituacao;
import com.app2.Fragmentos.ConsultaTipoEntrega;
import com.app2.Fragmentos.DetalheItemPedido;
import com.app2.Fragmentos.DetalhePedido;
import com.app2.Fragmentos.Observacao;
import com.app2.Fragmentos.Pedido;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;

import java.util.Objects;


public class TelaPedido extends AppCompatActivity {


    Fragment fragment_anterior;

    public PREFERENCIA conf;
    public myJSONObject usuarioJS = new myJSONObject();
    public static String ID_PEDIDO = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        conf = new PREFERENCIA(getBaseContext());
        if(getIntent()!=null) {
            ID_PEDIDO = getIntent().getStringExtra("ID");
            try {
                usuarioJS = new myJSONObject(Objects.requireNonNull(getIntent().getStringExtra("USUARIO")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        abrirPedido(ID_PEDIDO);
    }


    private boolean verificaSituacao() {
        try {
            String situacao = Pedido.getString("SITUACAO");
            if(situacao.equals("ABERTO")){
                return true;
            }else{
                showMessage("Pedido "+situacao);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    myJSONObject Pedido;
    public void abrirPedido(String ID_PEDIDO) {
        String JS = conf.getPedido(ID_PEDIDO);
        if(ID_PEDIDO!=null) {
            try {
                Pedido = new myJSONObject(JS);
                if(Pedido.getString("SITUACAO").equals("BLOQUEADO")){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(TelaPedido.this);
                    dlg.setMessage("Pedido Bloqueado");
                    dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    });
                    dlg.create().show();
                }else {
                    abrirFragmento(new Pedido(Pedido));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showMessage("FALHA AO CARREGAR DADOS");
            }
        }
    }


    public void consultaDetalhesPedido() {
        String JS;
        try {
            JS = conf.getPedido(ID_PEDIDO);
            Pedido = new myJSONObject(JS);
            abrirFragmento(new DetalhePedido(Pedido));
        } catch (JSONException e) {
            e.printStackTrace();
            showMessage("FALHA AO CARREGAR DADOS");
        }
    }


    myJSONArray Itens;
    public void consultaItem(String id_pedido) {
        String JS;
        if(id_pedido!=null) {
            try {
                JS = conf.getPedido(id_pedido);
                Pedido = new myJSONObject(JS);
                JS = conf.getItensPedido(id_pedido);
                Itens = new myJSONArray(JS);
                abrirFragmento(new ConsultaItem(Pedido,Itens));
            } catch (JSONException e) {
                e.printStackTrace();
                showMessage("FALHA AO CARREGAR DADOS");
            }
        }else{
            showMessage("FALHA AO CARREGAR DADOS");
        }
    }

    public void consultaItem(String id_pedido, String tipo) {
        String JS;
        if(id_pedido!=null) {
            try {
                JS = conf.getPedido(id_pedido);
                Pedido = new myJSONObject(JS);
                JS = conf.getItensPedido(id_pedido,tipo);
                Itens = new myJSONArray(JS);
                abrirFragmento(new ConsultaItem(Pedido,Itens));
            } catch (JSONException e) {
                e.printStackTrace();
                showMessage("FALHA AO CARREGAR DADOS");
            }
        }else{
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
            abrirFragmento(new DetalheItemPedido(Item));
        } catch (JSONException e) {
            e.printStackTrace();
            showMessage("FALHA AO CARREGAR DADOS DO ITEM");
        }
    }

    public void consultaProduto() {
        if(verificaSituacao()) {
            abrirFragmento(new ConsultaProdutoPedido());
        }
    }

    public void LeitorCodeBar(){
        if(verificaSituacao()) {
            fragment_anterior = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            IntentIntegrator intent = new IntentIntegrator(TelaPedido.this);
            intent.setOrientationLocked(false);
            intent.initiateScan();
        }
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
        if(verificaSituacao()) {
            if (barcode != null) {
                abrirProduto(barcode);
            } else {
                showMessage( "Falha na leitura");
            }
        }
    }



    private void abrirProduto(String barcode) {
        if(verificaSituacao()) {
            String id_produto;
            String JS;
            JS = conf.getIdProduto(barcode);
            try {
                id_produto = new myJSONObject(JS).getString("product_id");
                if (ID_PEDIDO != null) {
                    if (!id_produto.equals("0")) {
                        abrirItem(null, id_produto);
                    } else {
                        showMessage( "PRODUTO NÃO ENCONTRADO");
                        //abrirFragmentoAnterior();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showMessage( "FALHA AO CARREGAR DADOS");
                //showMessage( JS,Toast.LENGTH_LONG).show();
            }
        }
    }


    public void consultaCondicao(String id_pedido) {
        if(verificaSituacao()) {
            abrirFragmento(new ConsultaCondicao(id_pedido));
        }
    }


    public void consultaTipoEntraga() {
        if(verificaSituacao()) {
            abrirFragmento(new ConsultaTipoEntrega());
        }
    }

    public void alteraSituacao(String id_pedido) {
        if(verificaSituacao()) {
            if (id_pedido != null) {
                abrirFragmento(new ConsultaSituacao(id_pedido));
            } else {
                showMessage( "NULO");
            }
        }
    }

    public void alteraObservacao(String id_pedido) {
        if(verificaSituacao()) {
            String JS;
            if (id_pedido != null) {
                try {
                    JS = conf.getPedido(id_pedido);
                    Pedido = new myJSONObject(JS);
                    abrirFragmento(new Observacao(Pedido));
                } catch (JSONException e) {
                    e.printStackTrace();
                    showMessage( "FALHA AO CARREGAR DADOS");
                    //showMessage( JS,Toast.LENGTH_LONG).show();
                }
            } else {
                showMessage( "NULO");
            }
        }
    }

    public void salvaObservacao(String id_pedido, String observacao) {
        if(verificaSituacao()) {
            String ID_PEDIDO = conf.alterarObs(id_pedido, observacao);
            if (ID_PEDIDO.contains("ERRO") || ID_PEDIDO.contains("FALHA")) {
                showMessage( ID_PEDIDO);
            } else {
                consultaDetalhesPedido();
            }
        }
    }

    public void confirmaCondicao(String id_condicao, String id_pedido) {
        if(verificaSituacao()) {
            String ID_PEDIDO = conf.alterarCondicao(id_pedido, id_condicao);
            if (ID_PEDIDO.contains("ERRO") || ID_PEDIDO.contains("FALHA")) {
                showMessage( ID_PEDIDO);
            } else {
                abrirPedido(ID_PEDIDO);
            }
        }
    }


    public void abrirFragmentoAnterior() {
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment_anterior).commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if(fragment!=null){
            if(fragment instanceof ConsultaProdutoPedido){
                abrirPedido(ID_PEDIDO);
            }else if(fragment instanceof DetalhePedido){
                abrirPedido(ID_PEDIDO);
            }else if(fragment instanceof ConsultaItem){
                abrirPedido(ID_PEDIDO);
            }else if(fragment instanceof DetalheItemPedido){
                abrirFragmentoAnterior();
            }else if(fragment instanceof ConsultaCondicao){
                abrirFragmentoAnterior();
            }else if(fragment instanceof ConsultaTipoEntrega){
                abrirFragmentoAnterior();
            }else if(fragment instanceof ConsultaSituacao){
                abrirFragmentoAnterior();
            }else if(fragment instanceof Pedido){
                voltar();
            }else if(fragment instanceof Concluido){
                telaAnterior();
            }else {
                telaAnterior();
            }
        }else{
            telaAnterior();
        }
    }

    public void voltar() {
        try {
            if(Pedido.getString("SITUACAO").equals("ABERTO")) {
                alteraSituacao(ID_PEDIDO);
            }else {
                telaAnterior();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void telaAnterior() {

        Intent intent = new Intent(TelaPedido.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void abrirFragmento(Fragment fragment){
        fragment_anterior = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
    }

    public void confirmaTipoEntraga(String tipo) {
        if(verificaSituacao()) {
            String retorno = conf.alterarTipoEntraga(ID_PEDIDO, tipo);
            if (retorno.contains("ERRO") || retorno.contains("FALHA")) {
                showMessage( retorno);
            } else {
                consultaDetalhesPedido();
            }
        }
    }

    public void fecharPedido() {
        try {
            String situacao = Pedido.getString("SITUACAO");
            if(situacao.equals("ABERTO")){
                AlertDialog.Builder dlg = new AlertDialog.Builder(TelaPedido.this);
                dlg.setMessage("Confirmar o fechamento?");
                dlg.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    String retorno = conf.fecharPedido(ID_PEDIDO);
                    if (retorno.contains("ERRO") || retorno.contains("FALHA")) {
                        showMessage( retorno);
                    } else {
                        abrirFragmento(new Concluido());
                    }
                    }
                });
                dlg.setNegativeButton("CANCELAR", null);
                dlg.create().show();

            }else if(situacao.equals("RESERVADO")){
                AlertDialog.Builder dlg = new AlertDialog.Builder(TelaPedido.this);
                dlg.setMessage("Confirmar a reabertura do pedido?");
                dlg.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmaSituacaoAberto(ID_PEDIDO);
                    }
                });
                dlg.setNegativeButton("CANCELAR", null);
                dlg.create().show();

            }else {
                showMessage("Pedido "+situacao);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void gravaItem(String json) {
        if(verificaSituacao()) {
            String retorno = conf.CadastraItem(json);
            if (retorno.equals("TRANSAÇÃO FINALIZADA") ) {
                consultaItem(ID_PEDIDO);
            } else {
                showMessage( retorno);
            }
        }
    }

    public void apagaItem(final String id_item) {
        if(verificaSituacao()) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(TelaPedido.this);
        dlg.setMessage("Deseja apagar o item?");
        dlg.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String retorno = conf.ApagarItem(id_item);
                if (retorno.contains("ERRO") || retorno.contains("FALHA")) {
                    showMessage( retorno);
                } else {
                    consultaItem(ID_PEDIDO);
                }
            }
        });
        dlg.setNegativeButton("NÃO", null);
        dlg.create().show();
        }
    }

    public void confirmaSituacao(String id_situacao, String id_pedido) {
        if(verificaSituacao()) {
            String ID_PEDIDO = conf.alterarSituacao(id_pedido, id_situacao);
            if (ID_PEDIDO.contains("ERRO") || ID_PEDIDO.contains("FALHA")) {
                showMessage( ID_PEDIDO);
            } else {
                consultaDetalhesPedido();
            }
        }
    }
    public void confirmaSituacaoAberto(String id_pedido) {
        String ID_PEDIDO = conf.alterarSituacao(id_pedido, "1");
        if (ID_PEDIDO.contains("ERRO") || ID_PEDIDO.contains("FALHA")) {
            showMessage( ID_PEDIDO);
        } else {
            abrirPedido(ID_PEDIDO);
        }
    }


    public void showMessage(String message) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(TelaPedido.this);
        dlg.setMessage(message);
        dlg.setPositiveButton("OK", null);
        dlg.create().show();
    }

}
