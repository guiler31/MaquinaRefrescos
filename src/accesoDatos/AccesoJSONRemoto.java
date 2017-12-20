package accesoDatos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import auxiliares.ApiRequests;
import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;


public class AccesoJSONRemoto implements I_Acceso_Datos {

	ApiRequests encargadoPeticiones;
	private String SERVER_PATH, GET_DEPOSITO, SET_DEPOSITO,GET_DISPENSADOR,SET_DISPENSADOR; // Datos de la conexion
	
	public AccesoJSONRemoto(){
		
		encargadoPeticiones = new ApiRequests();

		SERVER_PATH = "http://localhost/1/maquinaRefrescos/";
		GET_DEPOSITO = "leeDepositos.php";
		GET_DISPENSADOR = "leeDispensadores.php";

	}
	
public void anadirJugadorJSON(Deposito auxDeposito) {

	try {
		JSONObject objDeposito = new JSONObject();
		JSONObject objPeticion = new JSONObject();

		objDeposito.put("nombre", auxDeposito.getNombreMoneda());
		objDeposito.put("cantidad", auxDeposito.getCantidad());
		objDeposito.put("valor", auxDeposito.getValor());
		objDeposito.put("id", auxDeposito.getId());

		// Tenemos el jugador como objeto JSON. Lo a�adimos a una peticion
		// Lo transformamos a string y llamamos al
		// encargado de peticiones para que lo envie al PHP

		objPeticion.put("peticion", "add");
		objPeticion.put("jugadorAnnadir", objDeposito);
		
		String json = objPeticion.toJSONString();

		System.out.println("Lanzamos peticion JSON para almacenar un jugador");

		String url = SERVER_PATH + SET_DEPOSITO;

		System.out.println("La url a la que lanzamos la petici�n es " + url);
		System.out.println("El json que enviamos es: ");
		System.out.println(json);
		//System.exit(-1);

		String response = encargadoPeticiones.postRequest(url, json);
		
		System.out.println("El json que recibimos es: ");
		
		System.out.println(response); // Traza para pruebas
		System.exit(-1);
		
		// Parseamos la respuesta y la convertimos en un JSONObject
		
		//Por aqui
		JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

		if (respuesta == null) { // Si hay alg�n error de parseo (json
									// incorrecto porque hay alg�n caracter
									// raro, etc.) la respuesta ser� null
			System.out.println("El json recibido no es correcto. Finaliza la ejecuci�n");
			System.exit(-1);
		} else { // El JSON recibido es correcto
			
			// Sera "ok" si todo ha ido bien o "error" si hay alg�n problema
			String estado = (String) respuesta.get("estado"); 
			if (estado.equals("ok")) {

				System.out.println("Almacenado jugador enviado por JSON Remoto");

			} else { // Hemos recibido el json pero en el estado se nos
						// indica que ha habido alg�n error

				System.out.println("Acceso JSON REMOTO - Error al almacenar los datos");
				System.out.println("Error: " + (String) respuesta.get("error"));
				System.out.println("Consulta: " + (String) respuesta.get("query"));

				System.exit(-1);

			}
		}
	} catch (Exception e) {
		System.out.println(
				"Excepcion desconocida. Traza de error comentada en el m�todo 'annadirJugador' de la clase JSON REMOTO");
		// e.printStackTrace();
		System.out.println("Fin ejecuci�n");
		System.exit(-1);
	}

}


@Override
public HashMap<Integer, Deposito> obtenerDepositos() {
	HashMap<Integer, Deposito> auxhm = new HashMap<Integer, Deposito>();
	
	try{
		
	    System.out.println("---------- Leemos datos de JSON --------------------");	
	    
		System.out.println("Lanzamos peticion JSON para jugadores");
		
		String url = SERVER_PATH + GET_DEPOSITO; // Sacadas de configuracion
		
		System.out.println("La url a la que lanzamos la peticion es " + url);
		
		String response = encargadoPeticiones.getRequest(url);

		System.out.println(response); // Traza para pruebas
		
		JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());
		
		JSONArray arr =(JSONArray) respuesta.get("depositos");
		
		for (int i = 0; i < arr.size(); i++) {
			Deposito deposito = new Deposito();
			JSONObject dep = (JSONObject) arr.get(i);
			String nombreMoneda = (String) dep.get("nombre").toString();
			String id= dep.get("id").toString();
			String valor=dep.get("valor").toString();
			String cantidad=dep.get("cantidad").toString();
			deposito.setId(Integer.parseInt(id));
			deposito.setNombreMoneda(nombreMoneda);
			deposito.setCantidad(Integer.parseInt(cantidad));
			deposito.setValor(Integer.parseInt(valor));
			auxhm.put(deposito.getId(), deposito);
		}
		

		
	}catch(Exception e){
		System.out.println("Ha ocurrido un error en la busqueda de datos");
		
		e.printStackTrace();
		
		System.exit(-1);
	}		
	
    return auxhm;
	
}


@Override
public HashMap<String, Dispensador> obtenerDispensadores() {
	HashMap<String, Dispensador> auxhm = new HashMap<String, Dispensador>();
	
	try{
		
	    System.out.println("---------- Leemos datos de JSON --------------------");	
	    
		System.out.println("Lanzamos peticion JSON para jugadores");
		
		String url = SERVER_PATH + GET_DISPENSADOR; // Sacadas de configuracion
		
		System.out.println("La url a la que lanzamos la peticion es " + url);
		
		String response = encargadoPeticiones.getRequest(url);

		System.out.println(response); // Traza para pruebas
		
		JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());
		
		JSONArray arr =(JSONArray) respuesta.get("dispensadores");
		
		for (int i = 0; i < arr.size(); i++) {
			Dispensador dispensador = new Dispensador();
			JSONObject disp = (JSONObject) arr.get(i);
			String nombreProducto = (String) disp.get("nombre").toString();
			String id= disp.get("id").toString();
			String clave=disp.get("clave").toString();
			String cantidad=disp.get("cantidad").toString();
			String precio=disp.get("precio").toString();
			
			dispensador.setId(Integer.parseInt(id));
			dispensador.setNombreProducto(nombreProducto);
			dispensador.setCantidad(Integer.parseInt(cantidad));
			dispensador.setPrecio(Integer.parseInt(precio));
			dispensador.setClave(clave);
			auxhm.put(String.valueOf(dispensador.getId()), dispensador);
		}
		

		
	}catch(Exception e){
		System.out.println("Ha ocurrido un error en la busqueda de datos");
		
		e.printStackTrace();
		
		System.exit(-1);
	}		
	
    return auxhm;
}


@Override
public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
	// TODO Auto-generated method stub
	return false;
}


@Override
public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
	// TODO Auto-generated method stub
	return false;
}
}
