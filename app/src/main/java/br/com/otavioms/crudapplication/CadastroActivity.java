package br.com.otavioms.crudapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CadastroActivity extends AppCompatActivity {
    EditText edValor;
    Button botao;
    SQLiteDatabase bancoDados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edValor = (EditText) findViewById(R.id.edValor);
        botao = (Button) findViewById(R.id.btnTelaCadastar);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });
    }

    public void cadastrar() {
        if (!TextUtils.isEmpty(edValor.getText().toString())) {
            try {
                bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
                String sql = "INSERT INTO venda (valor) VALUES (?)";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);
                stmt.bindString(1, edValor.getText().toString());
                stmt.executeInsert();
                bancoDados.close();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}