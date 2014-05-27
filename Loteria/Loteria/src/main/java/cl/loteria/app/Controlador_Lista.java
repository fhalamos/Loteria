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
    private static String EOF = "%@%";

    //Inicializa el sistema de archivos. Si no hay archivo, retorna false
    public static boolean start()
    {
        boolean ret = false;
        File root = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/BD");
        if(!root.exists())
        {
            root.mkdir();
        }
        BD = new File(root, "BD.txt");
        /*try {
            FileOutputStream f = new FileOutputStream(BD);
            PrintWriter pw = new PrintWriter(f);
            setBillete("Segundo Billete","98345982795","KINO","Resultado: Ganaste!!");
            setBillete("Primer Billete","98345982796","Boleto","Resultado: Perdiste!!");
            setBillete("Tercero Billete","98345982797","Boleto","Resultado: Perdiste!!");
            setBillete("Cuarto Billete","98345982798","Boleto","Resultado: Perdiste!!");
            setBillete("Quinto Billete","98345982799","Boleto","Resultado: Perdiste!!");
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) { }*/

        //Verificar que hay billetes en el archivo ademas de la opcionMinima
        if(getNombres().length > 1)
            ret = true;
        return ret;
    }

    //Retorna toda la informacion del billete. Importante que el indice parta de cero
    private static String[] getInfoBillete(int posicion)
    {
        String [] arreglo = new String[1];
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
        return getInfoBillete(posicion-1)[3];
    }

    public static String getNombre(int posicion)
    {
        return getInfoBillete(posicion-1)[0];
    }

    public static String getTipo(int posicion)
    {
        return getInfoBillete(posicion-1)[2];
    }

    public static String getCodigo(int posicion)
    {
        return getInfoBillete(posicion-1)[1];
    }

    //Retorna nombre de elementos a mostrar en la lista del navegador
    //opcionMinima es para asegurar que siempre habrá algo que mostrar en el drawer
    public static String[] getNombres()
    {
        String line = "";
        String opcionMinima = "Añadir nuevo billete";
        List<String> lines = new ArrayList<String>();
        lines.add(opcionMinima);
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
        arreglo = lines.toArray(new String[lines.size()]);
        return arreglo;
    }

    public static boolean setBillete(String nombre, String codigo, String tipo, String resultado)
    {
        try {
            FileOutputStream f = new FileOutputStream(BD,true);
            PrintWriter pw = new PrintWriter(f);
            pw.println(nombre+separador+codigo+separador+tipo+separador+resultado);
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) { return false;
        } catch (IOException e) { return false;}
        return true;
    }

    /*Igual a borrar pero reescribe el billete index con nombre cambiado*/
    public static void renombrar(int index, String nombre)
    {
        int i = 0;
        index--;
        String line = "";
        List<String> lines = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(BD)));
            try{
                while((line = br.readLine()) != null)
                {
                    if(i==index)
                    {
                        String[] temp = line.split(separador);
                        temp[0] = nombre;
                        line = temp[0]+separador+temp[1]+separador+temp[2]+separador+temp[3];
                    }
                    lines.add(line);
                    //System.out.println(lines.get(lines.size()-1));
                    i++;
                }
                br.close();
            }
            catch(IOException ioe) { }
        }
        catch (FileNotFoundException e) { }

        try
        {
            FileOutputStream f = new FileOutputStream(BD,false);
            PrintWriter pw = new PrintWriter(f);
            for(int j = 0; j < lines.size() ; j++)
                pw.println(lines.get(j));
            pw.flush();
            pw.close();
            f.close();
        }
        catch (FileNotFoundException e) { }
        catch (IOException e) { }
    }

    /*El indice esta desfasado por opcionMinima. Lee todo menos el billete a borrar y reescribe*/
    public static void borrar(int index)
    {
        int i = 0;
        index--;
        String line = "";
        List<String> lines = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(BD)));
            try{
                while((line = br.readLine()) != null)
                {
                    if(i!=index)
                        lines.add(line);
                    //System.out.println(lines.get(lines.size()-1));
                    i++;
                }
                br.close();
            }
            catch(IOException ioe) { }
        }
        catch (FileNotFoundException e) { }

        try
        {
            FileOutputStream f = new FileOutputStream(BD,false);
            PrintWriter pw = new PrintWriter(f);
            for(int j = 0; j < lines.size() ; j++)
                pw.println(lines.get(j));
            pw.flush();
            pw.close();
            f.close();
        }
        catch (FileNotFoundException e) { }
        catch (IOException e) { }
    }

}
