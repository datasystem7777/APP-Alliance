package com.app2.Classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    private final static String NOMEDOBANCO = "banco.db";
    private final static Integer VERSAO = 1;

    private Context context;
    public DB(Context context) {
        super(context, NOMEDOBANCO, null, VERSAO);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "";

        sql = "  CREATE TABLE PREFERENCIA " +
                "                ( " +
                "                        CHAVE                VARCHAR2(50)                                 , " +
                "                        VALOR                VARCHAR2(50)                                   " +
                "                ); ";

        db.execSQL(sql);



    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql;

        sql = " DROP TABLE IF EXISTS PREFERENCIA ;";
        db.execSQL(sql);

        onCreate(db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql;

        sql = " DROP TABLE IF EXISTS PREFERENCIA ;";
        db.execSQL(sql);

        onCreate(db);

    }

}

