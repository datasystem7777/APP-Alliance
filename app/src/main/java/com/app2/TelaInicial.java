package com.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class TelaInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);
      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
        */        startActivity(new Intent(TelaInicial.this, MainActivity.class));
          /*  }
        },200);
*/
    }
}
