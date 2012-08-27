package com.ermes.controlGasto;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ControlGastoActivity extends Activity implements OnClickListener, OnKeyListener {
    /** Called when the activity is first created. */
    EditText gasto;
    Button ingresar;
    TextView total;
    Spinner concepto;
    ArrayAdapter<CharSequence> adaptador;

    public void accion() {
        // oculto el teclado cuando le doy al enter
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        total.setText("el concepto es " + concepto.getSelectedItem() + " y la fecha es " + );
        Apunte entrada = new Apunte(this);
        entrada.abrir();
        entrada.crearApunte(gasto.getText().toString(), concepto.getSelectedItem().toString());
        entrada.cerrar();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (gasto.getText().toString().equals("")) {
            Toast.makeText(this, "Debes rellenar el salario bruto anual", Toast.LENGTH_SHORT)
                    .show();
        } else {
            accion();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        concepto = (Spinner) findViewById(R.id.spinner);
        gasto = (EditText) findViewById(R.id.txtGasto);
        ingresar = (Button) findViewById(R.id.btnIngresar);
        total = (TextView) findViewById(R.id.txtTotal);

        adaptador = ArrayAdapter.createFromResource(this, R.array.arrayConcepto,
                android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        concepto.setAdapter(adaptador);

        ingresar.setGravity(Gravity.LEFT);

        ingresar.setOnClickListener(this);
        gasto.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            accion();
        }
        return false;
    }
}