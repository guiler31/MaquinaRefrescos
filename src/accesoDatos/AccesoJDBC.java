package accesoDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import auxiliares.LeeProperties;
import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

public class AccesoJDBC implements I_Acceso_Datos {

	private String driver, urlbd, user, password; // Datos de la conexion
	private Connection conn1;

	public AccesoJDBC() {
		System.out.println("ACCESO A DATOS - Acceso JDBC");

		try {
			HashMap<String, String> datosConexion;

			LeeProperties properties = new LeeProperties("Ficheros/config/accesoJDBC.properties");
			datosConexion = properties.getHash();

			driver = datosConexion.get("driver");
			urlbd = datosConexion.get("urlbd");
			user = datosConexion.get("user");
			password = datosConexion.get("password");
			conn1 = null;

			Class.forName(driver);
			conn1 = DriverManager.getConnection(urlbd, user, password);
			if (conn1 != null) {
				System.out.println("Conectado a la base de datos");
			}

		} catch (ClassNotFoundException e1) {
			System.out.println("ERROR: No Conectado a la base de datos. No se ha encontrado el driver de conexion");
			// e1.printStackTrace();
			System.out.println("No se ha podido inicializar la maquina\n Finaliza la ejecucion");
			System.exit(1);
		} catch (SQLException e) {
			System.out.println("ERROR: No se ha podido conectar con la base de datos");
			System.out.println(e.getMessage());
			// e.printStackTrace();
			System.out.println("No se ha podido inicializar la maquina\n Finaliza la ejecucion");
			System.exit(1);
		}
	}

	public int cerrarConexion() {
		try {
			conn1.close();
			System.out.println("Cerrada conexion");
			return 0;
		} catch (Exception e) {
			System.out.println("ERROR: No se ha cerrado corretamente");
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {

		String query = "SELECT * FROM depositos";

		HashMap<Integer, Deposito> depositosCreados = new HashMap<Integer, Deposito>();
		Deposito auxDeposito;
		int clave;

		try {
			PreparedStatement pstmt = conn1.prepareStatement(query);

			ResultSet rset = pstmt.executeQuery(query);
			ResultSetMetaData rsmd = rset.getMetaData();

			rset = pstmt.executeQuery(query);

			while (rset.next()) {

				clave = Integer.parseInt(rset.getString("valor"));
				auxDeposito = new Deposito(rset.getString("nombre"), Integer.parseInt(rset.getString("valor")),
						Integer.parseInt(rset.getString("cantidad")));
				depositosCreados.put(clave, auxDeposito);

			}

			return depositosCreados;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

	}

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {

		String query = "SELECT * FROM dispensadores";
		HashMap<String, Dispensador> dispensadoresCreado = new HashMap<String, Dispensador>();

		try {
			PreparedStatement pstmt = conn1.prepareStatement(query);

			ResultSet rset = pstmt.executeQuery(query);
			ResultSetMetaData rsmd = rset.getMetaData();

			rset = pstmt.executeQuery(query);

			String clave, nombre;
			int precio, cantidad;
			Dispensador dispensador;

			while (rset.next()) {

				dispensador = new Dispensador(rset.getString("clave"), rset.getString("nombre"),
						Integer.parseInt(rset.getString("precio")), Integer.parseInt(rset.getString("cantidad")));
				clave = rset.getString("clave");
				dispensadoresCreado.put(clave, dispensador);

			}
			return dispensadoresCreado;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

	}

	@Override
	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		boolean todoOK = true;

		try {
			ArrayList<Integer> cantidad = new ArrayList<Integer>();
			int valor[] = new int[6];
			valor[0] = 200;
			valor[1] = 100;
			valor[2] = 50;
			valor[3] = 20;
			valor[4] = 10;
			valor[5] = 5;

			for (int i : depositos.keySet()) {
				Deposito dep = new Deposito();
				dep = depositos.get(i);
				cantidad.add(dep.getCantidad());
			}
			for (int i = 0; i < valor.length; i++) {
				String sql = "UPDATE `dat_maquinarefrescos_jdbc`.`depositos` SET cantidad = " + cantidad.get(i)
						+ " WHERE valor = " + valor[i] + ";";
				PreparedStatement stmt = conn1.prepareStatement(sql);
				stmt.executeUpdate();
				stmt.close();
			}

			return todoOK;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		boolean todoOK = true;
		try {
			ArrayList<Integer> cantidad = new ArrayList<Integer>();
			String clave[] = new String[dispensadores.size()];
			int j = 0;
			for (String i : dispensadores.keySet()) {
				Dispensador disp = new Dispensador();
				disp = dispensadores.get(i);
				clave[j] = disp.getClave();
				cantidad.add(disp.getCantidad());
				j++;
			}

			for (int i = 0; i < clave.length; i++) {
				String sql = "UPDATE `dat_maquinarefrescos_jdbc`.`dispensadores` SET cantidad = " + cantidad.get(i)
						+ " WHERE clave = '" + clave[i] + "';";
				PreparedStatement stmt = conn1.prepareStatement(sql);
				stmt.executeUpdate();
				stmt.close();
			}

			return todoOK;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}
} // Fin de la clase