package com.app2.Classes;

import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.app2.R;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public abstract class WebService extends Controle {

    String URL = "";

    Response response;
    String i ="";
    String retorno;
    Request requestHttp;
    OkHttpClient clientHttp;

    String URLMetodo = "";
    Context context;

    Handler handler;

    public WebService(Context context) {
        super(context);
        this.context = context;
        handler = new Handler();
    }


    boolean EX = true;


    public void getImagem(ImageView view, String arquivo) {
        Glide.with(context).load(getMyUrl()+"getImagem/"+arquivo).into(view);
    }

    public String getMyUrl() {
        URL = "http://"+carregarValor("IP")+":"+carregarValor("PORTA")+"/APP/webresources/generic/";
        return URL;
    }


    public String ChamadaWsDecode(final String Metodo, final String Parametros){
        retorno = "";
        response = null;

        clientHttp = new OkHttpClient.Builder().build();

        URL = "http://"+carregarValor("IP")+":"+carregarValor("PORTA")+"/APP/webresources/generic/";
        // Sending side
        String base64 = "";
        byte[] data;
        try {
            data = Parametros.getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpUrl URL_final = HttpUrl.parse(URL + Metodo).newBuilder()
                .addPathSegment(base64).build();
        Log.e("URL",URL_final.toString());
        try {

            requestHttp = new Request.Builder()
                    .url(URL_final)
                    .build();
        }catch (IllegalArgumentException e){
            Log.e("ERRO",e.toString());
            retorno = e.toString();
        }
        try {
            if(requestHttp!= null) {
                response = clientHttp.newCall(requestHttp).execute();
                retorno = response.body().string();
            }
        } catch ( SocketTimeoutException ex){
            Log.e("ERRO",ex.toString());
            Log.e("ERRO",Metodo);
            retorno = ex.toString();
        } catch (IOException e){
            Log.e("ERRO",e.toString());
            Log.e("ERRO",Metodo);
            retorno = e.toString();
        }
        Log.e("URL RETORNO",retorno);
        return retorno;

    }

    public String ChamadaWs(final String Metodo, final String Parametros){
        retorno = "";
        response = null;

        clientHttp = new OkHttpClient.Builder().build();
        String IP = carregarValor("IP");
        String PORTA = carregarValor("PORTA");
        URL = "http://" + IP + ":" + PORTA + "/APP/webresources/generic/";
        try{
            HttpUrl URL_final = HttpUrl.parse(URL + Metodo).newBuilder()
                    .addPathSegment(Parametros).build();
            Log.e("URL", URL_final.toString());

            requestHttp = new Request.Builder()
                    .url(URL_final)
                    .build();
            if (requestHttp != null) {
                response = clientHttp.newCall(requestHttp).execute();
                retorno = response.body().string();
            }
        } catch (SocketTimeoutException ex) {
            Log.e("ERRO", ex.toString());
            Log.e("ERRO", Metodo);
            retorno = ex.toString();
        } catch (Exception e) {
            Log.e("ERRO", e.toString());
            Log.e("ERRO", Metodo);
            retorno = e.toString();
        }

        Log.e("URL RETORNO", retorno);
        return retorno;

    }

    public String ChamadaWsDif(final String Metodo){
        retorno = "";
        response = null;

        clientHttp = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Log.e("URL", Metodo);
        try {

            requestHttp = new Request.Builder()
                    .url(Metodo)
                    .build();
        }catch (IllegalArgumentException e){
            Log.e("ERRO",e.toString());
            retorno = e.toString();
        }
        try {
            if(requestHttp!= null) {
                response = clientHttp.newCall(requestHttp).execute();
                retorno = response.body().string();
            }
        } catch ( SocketTimeoutException ex){
            Log.e("ERRO",ex.toString());
            Log.e("ERRO",Metodo);
            retorno = ex.toString();
        } catch (IOException e){
            Log.e("ERRO",e.toString());
            Log.e("ERRO",Metodo);
            retorno = e.toString();
        }
        Log.e("URL RETORNO",retorno);
        return retorno;

    }


    public String consultaCEP(String CEP){
        URLMetodo = "https://viacep.com.br/ws/"+CEP.replaceAll("-","")+"/json/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWsDif(URLMetodo);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }
    public String consultaCNPJ(String CNPJ){
        CNPJ = CNPJ.replaceAll("[/\\-\\\\.,: A-z]","");
        URLMetodo = "https://www.receitaws.com.br/v1/cnpj/"+CNPJ;

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWsDif(URLMetodo);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }


    public String validaLogin(String USUARIO, String SENHA) {
        final JSONObject object = new JSONObject();
        try {
            object.put("USUARIO",USUARIO);
            object.put("SENHA",SENHA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "validaLogin/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo,object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }


    public String getIndicadores(String ID_USUARIO) {
        final JSONObject object = new JSONObject();
        try {
            object.put("users_id",ID_USUARIO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "getIndicadores/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo,object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String getClientes(String LIKE) {
        final JSONObject object = new JSONObject();
        try {
            object.put("like",LIKE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "getClientes/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo,object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String getPedidos(String ID_USUARIO) {
        final JSONObject object = new JSONObject();
        try {
            object.put("users_id",ID_USUARIO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "getPedidos/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo,object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String getPedido(String id_order) {
        final JSONObject object = new JSONObject();
        try {
            object.put("id_order",id_order);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "getPedido/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo,object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String getCondicaoPagamento(){
        URLMetodo = "getCondicaoPagamento/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo,"");
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String getProdutos(String like){
        final JSONObject object = new JSONObject();
        try {
            object.put("like",like);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "getProdutos/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo,object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String getItensPedido(String id_order){
        final JSONObject object = new JSONObject();
        try {
            object.put("id_order",id_order);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "getItensPedido/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo,object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String getItensPedido(String id_order, String tipo){
        final JSONObject object = new JSONObject();
        try {
            object.put("id_order",id_order);
            object.put("tipo",tipo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "getItensPedido/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo,object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String getCliente(String id_cliente){
        final JSONObject object = new JSONObject();
        try {
            object.put("customer_id",id_cliente);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "getCliente/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo,object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String getItemPedido(String id_item){
        final JSONObject object = new JSONObject();
        try {
            object.put("id",id_item);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "getItemPedido/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo, object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String getProduto(String id_item){
        final JSONObject object = new JSONObject();
        try {
            object.put("id",id_item);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "getProduto/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo, object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String getIdProduto(String barcode){
        final JSONObject object = new JSONObject();
        try {
            object.put("code",barcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "getIdProduto/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo, object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String cadastrarPedido(String id_cliente, String users_id){
        final JSONObject object = new JSONObject();
        try {
            object.put("id_cliente",id_cliente);
            object.put("users_id"  ,users_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "cadastrarPedido/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo, object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String cadastrarCliente(JSONObject cliente, String users_id) {
         final JSONObject object = new JSONObject();
        try {
            object.put("cliente",cliente);
            object.put("users_id"  ,users_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "cadastrarCliente/";

        Log.e("LINK_URL", URLMetodo);
        Log.e("JSON ENVIADO", object.toString());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWsDecode(URLMetodo,object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String getTransportadoras(String customer_id) {
        final JSONObject object = new JSONObject();
        try {
            object.put("customer_id",customer_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "getTranspotadoras/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo,object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String getSituacao() {
        URLMetodo = "getSituacao/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo,"");
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String alterarObs(String id_pedido, String obs){
        final JSONObject object = new JSONObject();
        try {
            object.put("id_pedido",id_pedido);
            object.put("obs"  ,obs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "alterarObs/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWsDecode(URLMetodo, object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;

    }

    public String alterarCondicao(String id_pedido, String id_condicao){
        final JSONObject object = new JSONObject();
        try {
            object.put("id_pedido",id_pedido);
            object.put("id_condicao"  ,id_condicao);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "alterarCondicao/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo, object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String alterarTipoEntraga(String id_pedido, String tipo) {
        final JSONObject object = new JSONObject();
        try {
            object.put("id_pedido",id_pedido);
            object.put("tipo"  ,tipo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "alterarTipoEntraga/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo, object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String fecharPedido(String id_pedido) {
        final JSONObject object = new JSONObject();
        try {
            object.put("id_pedido",id_pedido);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "fecharPedido/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo, object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public  String CadastraItem(final String json) {
        URLMetodo = "CadastraItem/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo, json);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String ApagarItem(String id_item){
        final JSONObject object = new JSONObject();
        try {
            object.put("id_item",id_item);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "ApagarItem/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo, object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }

    public String alterarSituacao(String id_pedido, String id_situacao) {
        final JSONObject object = new JSONObject();
        try {
            object.put("id_pedido",id_pedido);
            object.put("situacao"  ,id_situacao);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URLMetodo = "alterarSituacao/";

        Log.e("LINK_URL", URLMetodo);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = ChamadaWs(URLMetodo, object.toString());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return i;
    }
}















