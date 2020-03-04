package com.app2.Classes;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.app2.Fragmentos.Concluido;
import com.app2.TelaPedido;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public abstract class Controle {

    private SQLiteDatabase db;
    private DB banco;
    private Context context;

    public Controle(Context context) {
        banco = new DB(context);
        this.context = context;
    }


    public String Registra(Object o, ContentValues valores) {


        String retorno = null;
        long resultado;

        db = banco.getWritableDatabase();

        resultado = db.insert(o.getClass().getSimpleName(), null, valores);

        if (resultado == -1) {
            retorno = "Erro ao inserir registro";
        } else {
            retorno = "Registro Inserido com sucesso";
        }
        return retorno;
    }

    public boolean Alterar(Object o, String parametro,ContentValues valores) {
        boolean retorno = false;
        long resultado;
        db = banco.getWritableDatabase();

        resultado = db.update(o.getClass().getSimpleName(), valores, parametro, null);

        if (resultado == -1) {
            retorno = false;
        } else {
            retorno = true;
        }
        return retorno;
    }

    public Cursor Carrega( String parametro, Object o) {
        Cursor cursor = ExecutaSelect("PRAGMA table_info("+o.getClass().getSimpleName()+")");
        Field[] fields = new Field[cursor.getCount()];
        String sql = " SELECT ";
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            try {
                fields[i] = o.getClass().getField(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                if (i == fields.length - 1) {
                    if (fields[i].isSynthetic()) {
                        continue;
                    }
                    sql = sql + fields[i].getName();
                } else {
                    sql = sql + fields[i].getName() + " , ";
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        sql = sql + " FROM " + o.getClass().getSimpleName();
        if (parametro != null) {
            sql = sql + " WHERE " + parametro;
        }

        cursor = null;
        try {
            db = banco.getReadableDatabase();

            cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }

            if (cursor != null && cursor.getCount() > 0) {
                for (int i = 0; i < fields.length; i++) {
                    try {
                        switch (cursor.getType(cursor.getColumnIndexOrThrow(fields[i].getName()))) {
                            case Cursor.FIELD_TYPE_INTEGER:
                                fields[i].set(o, cursor.getInt(cursor.getColumnIndexOrThrow(fields[i].getName())));
                                break;
                            case Cursor.FIELD_TYPE_FLOAT:
                                fields[i].set(o, cursor.getFloat(cursor.getColumnIndexOrThrow(fields[i].getName())));
                                break;
                            case Cursor.FIELD_TYPE_BLOB:
                                fields[i].set(o, cursor.getBlob(cursor.getColumnIndexOrThrow(fields[i].getName())));
                                break;
                            case Cursor.FIELD_TYPE_STRING:
                                fields[i].set(o, cursor.getString(cursor.getColumnIndexOrThrow(fields[i].getName())));
                                break;
                            case Cursor.FIELD_TYPE_NULL:
                                fields[i].set(o, null);
                                break;
                        }
                    } catch (IllegalAccessException | RuntimeException e) {
                        e.printStackTrace();
                    }
                }
            }
            return cursor;
        }catch (Exception e) {
            return null;
        }finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void Apagar(Object o, String parametro) {
        db = banco.getReadableDatabase();
        db.delete(o.getClass().getSimpleName(), parametro, null);
    }

    public Cursor ExecutaSelect(String sql) {
        Log.e("SQL", sql);
        Cursor cursor = null;
        db = banco.getReadableDatabase();
        cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    static String ERRO = "";


    public boolean ExecutarSQL(String sql) {
        Log.e("SQL", sql);
        db = banco.getReadableDatabase();
        try {
            db.execSQL(sql);
            Log.e("REGISTRO_SQL", sql);
            return true;
        } catch (SQLException ex){
            ERRO = ex.toString();
            Log.e("ERRO_SQL", ex.toString()+"\n"+sql);
            return false;
        }
    }

    public String ERRO(){
        return ERRO;
    }

    public ContentValues toValores(Object o) {
        ContentValues valores = new ContentValues();
        String valor;
        Field[] fields = o.getClass().getFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                if (fields[i].getName().equals("_id")) {

                } else {
                    valor = o.getClass().getDeclaredMethod("get" + fields[i].getName()).invoke(o).toString();
                    if (!(fields[i].getName().equals("VERSAO"))) {
                        if (valor.equals("0")) {
                            valor = null;
                        }
                    }
                    valores.put(fields[i].getName(), valor);
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NullPointerException e) {
                e.printStackTrace();
            }
        }


        return valores;
    }

    public Cursor filtro(String tabela, String[] colunas, String like,String grupo) throws SQLiteException {
        db = banco.getReadableDatabase();
        if (like == null || like.length() == 0) {
            return db.query(tabela, colunas,
                    null, null, grupo, null, null);
        } else {
            return db.query(true, tabela, colunas, like,
                    null, grupo, null, null, null);
        }

    }


    public String getLink(Object o) {
        String valor = "";
        JSONObject json = new JSONObject();
        Field[] fields = o.getClass().getFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                valor = String.valueOf((o.getClass().getDeclaredMethod("get" + fields[i].getName()).invoke(o))==null?"":o.getClass().getDeclaredMethod("get" + fields[i].getName()).invoke(o));
                if(valor.equals("0")) {
                    valor = " ";
                }
                json.put(fields[i].getName(),valor.replaceAll("/","|").replace("\\","|").replaceAll("'"," "));

            }catch (IllegalAccessException e) {
                e.printStackTrace();
                break;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                break;
            } catch (JSONException e) {
                e.printStackTrace();
                break;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                break;
            }
        }

        return json.toString();
    }

    public String getCursorLink(Object o) {
        Cursor cursor = (Cursor)o;
        String link = "";
        JSONObject json = new JSONObject();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            try {
                if(cursor.getString(i)!=null) {
                    json.put(cursor.getColumnName(i), cursor.getString(i).replaceAll("/","|").replace("\\","|").replaceAll("'"," "));
                }else{
                    json.put(cursor.getColumnName(i),"");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("CAMPO",e.toString());
                break;
            }
            Log.e("CAMPO",cursor.getColumnName(i)+" : "+json.toString());
        }

        return json.toString();
    }


    public void alterarValor(String Chave, String Valor){
        ExecutarSQL("UPDATE PREFERENCIA SET VALOR = '"+Valor+"' WHERE CHAVE = '"+Chave+"' ");
    }

    public String carregarValor(String Chave){
        Cursor cursor = ExecutaSelect("SELECT IFNULL(VALOR,'') FROM PREFERENCIA WHERE CHAVE = '"+Chave+"'");
        if(cursor.getCount()>0) {
            return cursor.getString(0);
        }else{
            ExecutarSQL("INSERT INTO PREFERENCIA VALUES ('"+Chave+"','')");
            return carregarValor(Chave);
        }
    }


}



