package cl.loteria.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cl.loteria.app.R;

//Clase que maneja la interfaz de input para ingresar un nuevo billete
public class Captura_Billete extends ActionBarActivity {

    //Codigo para reconocer respuestas a un intento de fotografia
    static final int SOLICITUD_CAPTURA = 666;
    //Bitmap que guarda la ultima fotografia realizada
    static Bitmap captureBmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura_billete);

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
        EditText et = (EditText)findViewById(R.id.nombre);
        String msg = et.getText().toString();
        msg = msg.replace("\n","");
        msg = msg.replace(Controlador_Lista.getSeparador(),"?");
        et.setText(msg);
        ((Button)findViewById(R.id.ok_button)).setEnabled((msg.length() > 0 && msg.length() <= 16));
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
        Controlador_Lista.setBillete(nombre,"0101010101",tipo,"Lo siento, perdió");
        this.finish();
    }

    public void go_to_display(View v)
    {
        startActivity(new Intent(this, Display_Imagen.class));
    }

    //Se inicia la camara para tomar foto del billete
    public void tomarFoto(View v)
    {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getFotoTemp(this)) );
        startActivityForResult(intent, SOLICITUD_CAPTURA );
    }

    //Busca archivo temporal en que se guarda ultima imagen
    private File getFotoTemp(Context c)
    {
        final File path = new File( Environment.getExternalStorageDirectory(),getPackageName());
        if(!path.exists())
        {
            path.mkdir();
        }
        return new File(path, "image.tmp");
    }

    //Maneja respuesta de la camara y carga imagen en previsualizacion
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        switch(requestCode)
        {
            case SOLICITUD_CAPTURA:
                if(!setPrevisualizacion())
                {
                    Toast.makeText(this,"Error al cargar imagen",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //Imagen se debe volver a cagar en previsualizacion si la actividad se detiene porque se caraga dinamicamente
    @Override
    public void onResume()
    {
        super.onResume();
        setPrevisualizacion();
    }

    //CArga ultima imagen de archivo temporal a bitmap
    private boolean setPrevisualizacion()
    {

        final File file = getFotoTemp(this);
        try
        {
            if(captureBmp != null)
                captureBmp.recycle();
            captureBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
            ImageView prev = (ImageView)findViewById(R.id.previsualizacion);
            prev.setImageBitmap(captureBmp);
            return true;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
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
