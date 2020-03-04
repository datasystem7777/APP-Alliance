package com.app2.Classes;

import android.content.Context;
import android.database.Cursor;

public class PREFERENCIA extends WebService {

    public String CHAVE;
    public String VALOR;

    public PREFERENCIA(Context context) {
        super(context);
    }

    public String getCHAVE() {
        return CHAVE;
    }

    public void setCHAVE(String CHAVE) {
        this.CHAVE = CHAVE;
    }

    public String getVALOR() {
        return VALOR;
    }

    public void setVALOR(String VALOR) {
        this.VALOR = VALOR;
    }

}



























