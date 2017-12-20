package accesoDatos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;
import auxiliares.HibernateUtil;

public class AccesoHibernate implements I_Acceso_Datos {

	Session session;

	public AccesoHibernate() {

		HibernateUtil util = new HibernateUtil();

		session = util.getSessionFactory().openSession();

	}

	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {
		Query q = session.createQuery("select dep from Deposito dep");
		List results = q.list();
		int clave;
		HashMap<Integer, Deposito> depositosCreados = null;
		Iterator equiposIterator = results.iterator();

		while (equiposIterator.hasNext()) {
			Deposito team = (Deposito) equiposIterator.next();
//			team = new Deposito(team.getNombreMoneda(), team.getValor(), team.getCantidad());
//			clave = team.getValor();
			depositosCreados.put(team.getValor(), team);
			System.out.println("		Id: " + team.getId() + " - NombreMoneda: " + team.getNombreMoneda()
					+ " - Valor: " + team.getValor() + " - Cantidad: " + team.getCantidad());
		}

		System.out.println("Fin Consulta Deposito");
		return depositosCreados;
	}

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {
		Query q = session.createQuery("select dis from Dispensador dis");
		List results = q.list();
		String clave;
		HashMap<String, Dispensador> dispensadorCreados = null;
		Iterator equiposIterator = results.iterator();

		while (equiposIterator.hasNext()) {
			Dispensador team = (Dispensador) equiposIterator.next();
//			team = new Dispensador(team.getClave(), team.getNombreProducto(), team.getPrecio(), team.getCantidad());
//			clave = team.getClave();
			dispensadorCreados.put(team.getClave(), team);
			System.out.println("		Id: " + team.getId() + " - Clave: " + team.getClave() + " - NombreProducto: "
					+ team.getNombreProducto() + " - Cantidad: " + team.getCantidad() + " - Precio: "
					+ team.getPrecio());

		}
		System.out.println("Fin Consulta Deposito");
		return dispensadorCreados;
	}

	@Override
	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		for (Integer key : depositos.keySet()) {
			Deposito deposito = (Deposito) depositos.get(key);
			session.update(deposito);
		}
		return true;
	}

	@Override
	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		for (String key : dispensadores.keySet()) {
			Dispensador dispensador = (Dispensador) dispensadores.get(key);
			session.update(dispensador);
		}
		return true;
	}

}
