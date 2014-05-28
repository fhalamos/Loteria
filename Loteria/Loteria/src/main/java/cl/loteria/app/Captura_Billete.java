package cl.loteria.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//Clase que maneja la interfaz de input para ingresar un nuevo billete
public class Captura_Billete extends ActionBarActivity {

    //Codigo para reconocer respuestas a un intento de fotografia
    static final int SOLICITUD_CAPTURA = 666;

    static boolean codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura_billete);

        codigo = false;

        /*Preparar spinner con opciones de distintos juegos*/
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        List<String> list = new ArrayList<String>();
        list.add("KINO");
        list.add("Boleto");
        list.add("IMÁN");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);

        //Preparar edittext
        setupUI(findViewById(R.id.parent));
    }

    //Metodo para esconder teclado y verifica validez del nombre
    public void hideSoftKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    public void checkOk()
    {
        EditText et = (EditText)findViewById(R.id.nombre);
        String msg = et.getText().toString();
        msg = msg.replace("\n","");
        msg = msg.replace(Controlador_Lista.getSeparador(),"?");
        et.setText(msg);
        boolean repetido = false;
        boolean porte = (msg.length() > 0 && msg.length() <= 16);
        String[] nombres = getIntent().getStringArrayExtra("NOMBRES_LISTA");
        for(int i = 1 ; i<nombres.length ; i++)
        {
            if(msg == nombres[i])
                repetido = true;
        }
        ((Button)findViewById(R.id.ok_button)).setEnabled(codigo && porte && !repetido);
    }

    //Controladores para esconder teclado cuando campo de texto pierde foco
    public void setupUI(View view)
    {
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText))
        {
            view.setOnTouchListener(new View.OnTouchListener()
            {
                public boolean onTouch(View v, MotionEvent event)
                {
                    hideSoftKeyboard();
                    checkOk();
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup)
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    //Usuario se arrepiente de ingresar nuevo billete
    public void cancelar(View v)
    {
        this.finish();
    }

    //Usuario acepta los datos del billete a ingresar
    public void submit(View v)
    {
        EditText et = (EditText)findViewById(R.id.nombre);
        String nombre = et.getText().toString();
        Spinner sp = (Spinner)findViewById(R.id.spinner1);
        String tipo = sp.getSelectedItem().toString();
        TextView tv = (TextView)findViewById(R.id.codigo_billete);
        String codigo = tv.getText().toString();
        Controlador_Lista.setBillete(nombre, codigo, tipo, "resultado pendiente");
        this.finish();
    }

    //Se inicia la camara para tomar foto del billete
    public void tomarFoto(View v)
    {
        startActivityForResult(new Intent(this, CameraTestActivity.class),SOLICITUD_CAPTURA);
    }

    //Maneja respuesta de la camara y carga imagen en previsualizacion
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        switch(requestCode)
        {
            case SOLICITUD_CAPTURA:
                String resultado = data.getStringExtra("RESULTADO_CODIGO");
                ((TextView)findViewById(R.id.codigo_billete)).setText(resultado);
                codigo = true;
                checkOk();
                break;
        }
    }

    //Imagen se debe volver a cagar en previsualizacion si la actividad se detiene porque se caraga dinamicamente
    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.captura_billete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
