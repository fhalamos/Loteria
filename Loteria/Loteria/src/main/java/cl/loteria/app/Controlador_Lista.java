package cl.loteria.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 5/3/14.
 */
public class Controlador_Lista
{
    private static File BD;
    private static String separador = "%#%";
    public static void start()
    {
        File root = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/BD");
        if(!root.exists())
        {
            root.mkdir();
        }
        BD = new File(root, "BD.txt");
        try {
            FileOutputStream f = new FileOutputStream(BD);
            PrintWriter pw = new PrintWriter(f);
            setBillete("Primer Billete","98345982798","Resultado: Perdiste wn!! Rindete, nunca vay a ganar nada en la vida");
            setBillete("Segundo Billete","98345982795","Resultado: Ganaste!!");
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) { }
    }

    private static String[] getInfoBillete(int posicion)
    {
        String [] arreglo = new String[3];
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(BD)));
            try{
                int i = 0;
                while(i < posicion)
                {
                    br.readLine();
                    i++;
                }
                arreglo = br.readLine().split(separador);
                br.close();
            } catch(IOException ioe) {}
        } catch (FileNotFoundException e) {}
        if(arreglo != null)
        {
            return arreglo;
        } else { return null; }
    }

    public static String getResultado(int posicion)
    {
        return getInfoBillete(posicion)[2];
    }

    public static String getNombre(int posicion)
    {
        return getInfoBillete(posicion)[0];
    }

    public static String getCodigo(int posicion)
    {
        return getInfoBillete(posicion)[1];
    }

    public static String[] getNombres()
    {
        String line = "";
        List<String> lines = new ArrayList<String>();
        String [] arreglo;
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(BD)));
            try{
                while((line = br.readLine()) != null)
                {
                    lines.add(line.split(separador)[0]);
                }
                br.close();
            } catch(IOException ioe) {}
        } catch (FileNotFoundException e) {}
        if(!lines.isEmpty())
        {
            arreglo = lines.toArray(new String[lines.size()]);
            return arreglo;
        } else { return null; }
    }

    public static boolean setBillete(String nombre, String codigo, String resultado)
    {
        try {
            FileOutputStream f = new FileOutputStream(BD,true);
            PrintWriter pw = new PrintWriter(f);
            pw.println(nombre+separador+codigo+separador+resultado);
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {return false;
        } catch (IOException e) { return false;}
        return true;
    }

}
