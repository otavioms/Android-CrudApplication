package br.com.otavioms.crudapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    public ListView listViewDados;
    public Button botao;
    public ArrayList<Integer> arrayIds;
    public Integer idSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewDados = (ListView) findViewById(R.id.listViewDados);
        botao = (Button) findViewById(R.id.btnCadastrar);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaCadastro();
            }
        });

        listViewDados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                idSelecionado = arrayIds.get(position);
                confirmarExlcuir();
                return true;
            }
        });

        criarBrancoDados();
        listarDados();
    }

    public void criarBrancoDados() {
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS venda(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , valor VARCHAR)");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarDados() {
        try {
            arrayIds = new ArrayList<>();
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor meuCursor = bancoDados.rawQuery("SELECT id, valor FROM venda", null);
            ArrayList<String> linhas = new ArrayList<String>();
            ArrayAdapter meuAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhas
            );
            listViewDados.setAdapter(meuAdapter);
            meuCursor.moveToFirst();
            while (meuCursor != null) {
                linhas.add(meuCursor.getString(1));
                arrayIds.add(meuCursor.getInt(0));
                meuCursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirTelaCadastro() {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarDados();
    }

    public void confirmarExlcuir() {
        AlertDialog.Builder msgBox = new AlertDialog.Builder(MainActivity.this);
        msgBox.setTitle("Excluir");
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setMessage("Você realmente deseja excluir esse registro ?");
        msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exlcuir();
                listarDados();
            }
        });
        msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        msgBox.show();
    }

    public void exlcuir() {
        //Toast.makeText(this, position.toString(), Toast.LENGTH_SHORT).show();
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "DELETE FROM venda WHERE id =?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindLong(1, arrayIds.get(idSelecionado));
            stmt.executeUpdateDelete();
            listarDados();
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}