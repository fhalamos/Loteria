package cl.loteria.app;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

/**
 * Created by Eduardo on 28-05-14.
 */

public class APILoteria extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        return getData(params[0]);
    }

    protected void onPostExecute(String result) {
        System.out.println(result);   //Prints the string content read from input stream
        String[] splitten = result.substring(1, result.length()-2).split("&");
        Hashtable<String, String> hasheado = new Hashtable<String, String>();
        for (String s: splitten){
            String[] a = s.split("=");
            hasheado.put(a[0], a[1]);
        }
        if(hasheado.get("respuesta").equals("1")){
            System.out.println("hay resultados");
            System.out.println("Los numeros son:");
            for (String s: hasheado.get("BolKino1").split(",")){
                System.out.println(s);
            }
        }else{
            System.out.println("No hay resultados aun");
        }
/*
 #respuesta = 1-> ya hay resultados, 2 -> no se ha efectuado sorteo, hay mensaje
 15 #Fecha = Fecha de compra
 16 #BolKino1 = numeros boleto separados por coma
 17 #SorteoIni,SorteoFin,SorteoUlt = numero de sorteo
 18 #mensaje = solo si sorteo no se ha realizado
 19 #hitsK4Juego1P1 = hits chanchito regalon (numero de coicidencias)
 20 #hitsK41 = hits juego normal
 21 #hitsK4Rev1 = hits rekino
 22 #AKino1 = Aciertos kino normal  de la forma 00/01
 23 #AReKino1 = Aciertos rekino
 24 #AKinoJuego1P1 = Aciertos chanchito regalon
 25 #AKinoCJ2Op11:
 26 #AKinoCJ2Op21:
 27 #AKinoCJ2Op31:
 28 #AKinoCJ2Op41:
 29 #AKinoCJ2Op51:
 30 #AKinoCJOp11:
 31 #AKinoCJOp21:
 32 #AKinoCJOp31:
 33 #AKinoCJOp41:
 34 #AKinoMG1 = Aciertos otros juegos
 35 #Monto1:0
 36 #MontoCJ1:0
 37 #MontoCJ2P1:0
 38 #MontoCon1:0
 39 #MontoGM1:0
 40 #MontoJuego1P1:0 = montos de premios
 */

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

