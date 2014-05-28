package cl.loteria.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ListaBilletes extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean iniciado = Controlador_Lista.start();
        setContentView(R.layout.activity_lista_billetes);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        //Si no hay billetes, va directamente a cargar uno

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }



    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if(position!=0)
        {
            // update the main content by replacing fragments
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position))
                    .commit();
        }
        else
        {
            //Si se elige la opcionMinima, se agrega nuevo billete
            startActivity(new Intent(this, Captura_Billete.class));
        }
    }

    public void onNavigationDrawerItemErased(int position) {
        //((DrawerLayout)findViewById(R.id.drawer_layout)).openDrawer(findViewById(R.id.drawer_layout));
    }

    public void onSectionAttached(int number) {
        mTitle = Controlador_Lista.getNombre(number);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.lista_billetes, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
        if (id == R.id.action_agregar) {
            Intent intent = new Intent(this, Captura_Billete.class);
            intent.putExtra("NOMBRES_LISTA",Controlador_Lista.getNombres());
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_capture) {
            Intent intent = new Intent(this, Captura_Billete.class);
            intent.putExtra("NOMBRES_LISTA",Controlador_Lista.getNombres());
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment()
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState)
        {
            int index = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            if(index !=-1 )
                textView.setText("Código: "+Controlador_Lista.getCodigo(index)+"\nResultado: "+Controlador_Lista.getResultado(index));
            else
                textView.setText("Arrastre pantalla o presione\nvolver o arriba para volver a la lista.");
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            int index = getArguments().getInt(ARG_SECTION_NUMBER);
            if(index != -1)
                ((ListaBilletes) activity).onSectionAttached( index );
        }
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getData(params[0]);
        }

        protected void onPostExecute(String result) {
            System.out.println(result);   //Prints the string content read from input stream



        }

        protected void onProgressUpdate(Integer... progress) {

        }


        public String getData(String ticketNumber) {

            try{
                URL url = new URL("http://www.loteria.cl/KinoASP/procesa_consulta_kinoP3V2i016CJNK.asp?onHTTPStatus=%5Btype%20Function%5D&Nconsulta=1&panel=0&DV=&Rut=&boleto=0" + ticketNumber + "&sorteo=0");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder out = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        out.append(line);
                    }

                    reader.close();
                    return out.toString();

                }
                finally {
                    urlConnection.disconnect();
                }
            }catch(Exception e){

            }
            return "";
        }
    }

}
