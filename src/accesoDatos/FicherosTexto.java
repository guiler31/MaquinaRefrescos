package accesoDatos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

/*
 * Todas los accesos a datos implementan la interfaz de Datos
 */

public class FicherosTexto implements I_Acceso_Datos {

	File fDis; // FicheroDispensadores
	File fDep; // FicheroDepositos

	public FicherosTexto() {
		System.out.println("ACCESO A DATOS - FICHEROS DE TEXTO");
	}

	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {

		HashMap<Integer, Deposito> depositosCreados = null;

		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader("./Ficheros/datos/depositos.txt"));
			try {
				depositosCreados = new HashMap<Integer, Deposito>();
				String line;
				while ((line = br.readLine()) != null) {
					String[] datos = line.split(";");
					int clave = Integer.parseInt(datos[1]);
					int cantidad = Integer.parseInt(datos[2]);
					Deposito dep = new Deposito(datos[0], clave, cantidad);
					depositosCreados.put(clave, dep);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return depositosCreados;
	}

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {

		HashMap<String, Dispensador> dispensadoresCreados = null;

		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader("./Ficheros/datos/dispensadores.txt"));
			try {
				dispensadoresCreados = new HashMap<String, Dispensador>();
				String line;
				while ((line = br.readLine()) != null) {
					String[] datos = line.split(";");
					int precio = Integer.parseInt(datos[2]);
					int cantidad = Integer.parseInt(datos[3]);
					Dispensador dep = new Dispensador(datos[0], datos[1], precio, cantidad);
					dispensadoresCreados.put(datos[0], dep);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dispensadoresCreados;

	}

	@Override
	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {

		boolean todoOK = true;

		FileWriter fichero = null;
		PrintWriter pw = null;
		Iterator it = depositos.entrySet().iterator();
		try {
			fichero = new FileWriter("./Ficheros/datos/depositos.txt");
			pw = new PrintWriter(fichero);
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				Deposito depo = (Deposito) pair.getValue();
				pw.println(depo.getNombreMoneda() + ";" + depo.getId() + ";" + depo.getCantidad());
				it.remove(); // avoids a ConcurrentModificationException
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Nuevamente aprovechamos el finally para
				// asegurarnos que se cierra el fichero.
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return todoOK;

	}

	@Override
	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {

		boolean todoOK = true;

		FileWriter fichero2 = null;
		PrintWriter pw2 = null;
		Iterator it2 = dispensadores.entrySet().iterator();
		try {
			fichero2 = new FileWriter("./Ficheros/datos/dispensadores.txt");
			pw2 = new PrintWriter(fichero2);
			while (it2.hasNext()) {
				Map.Entry pair = (Map.Entry) it2.next();
				Dispensador disp = (Dispensador) pair.getValue();
				pw2.println(disp.getClave() + ";" + disp.getNombreProducto() + ";" + disp.getPrecio() + ";"
						+ disp.getCantidad());
				it2.remove(); // avoids a ConcurrentModificationException
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Nuevamente aprovechamos el finally para
				// asegurarnos que se cierra el fichero.
				if (null != fichero2)
					fichero2.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return todoOK;
	}

} // Fin de la clase