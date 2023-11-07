package br.com.otavioms.crudapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AlterarActivity extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    public Button btnTelaAlterar;
    public EditText edValor;
    public Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar);

        btnTelaAlterar = (Button) findViewById(R.id.btnTelaAlterar);
        edValor = (EditText) findViewById(R.id.edValor);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        carregarDados();

        btnTelaAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterar();
            }
        });
    }

    public void carregarDados() {
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id, valor FROM venda WHERE id = " + id.toString(), null);
            cursor.moveToFirst();
            edValor.setText(cursor.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alterar() {
        String valueNome;
        valueNome = edValor.getText().toString();
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "UPDATE venda SET valor =? WHERE id =?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1, valueNome);
            stmt.bindLong(2, id);
            stmt.executeUpdateDelete();
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }
}