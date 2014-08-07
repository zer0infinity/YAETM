package ch.hsr.i.yaetm.db;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ch.hsr.i.yaetm.logic.Ersatzteil;
import ch.hsr.i.yaetm.logic.History;
import ch.hsr.i.yaetm.logic.Reservation;

public class ActiveRecordManager {

	static private Connection connection;
	static private String database = "jdbc:sqlite:database/Sqlite";

	public static void setDatabase(String jdbcName) {
		database = jdbcName;
	}

	public static Connection getConnection() throws SQLException {
		try {
			if (connection == null || connection.isClosed()) {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection(database);
				DatabaseMetaData metaData = connection.getMetaData();
				System.out.println("sqlite driver mode: "
						+ metaData.getDriverVersion());
				System.out.println("database driver: "
						+ metaData.getDriverName());
			}
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver nof found");
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * execute an insert statement.
	 */
	public static int executeInsert(String sql) throws SQLException {
		int newId = -1;
		Statement stat = getConnection().createStatement();
		stat.execute(sql);
		newId = getIdOfInsertedElement(stat);
		stat.close();
		return newId;
	}

	public static int executeInsert(String prepStmt, String... arguments)
			throws SQLException {
		int newId = -1;
		Connection conn = getConnection();
		PreparedStatement prep = createStatementWithArguments(prepStmt, conn,
				arguments);
		prep.execute();
		Statement stat = conn.createStatement();
		newId = getIdOfInsertedElement(stat);
		stat.close();
		return newId;
	}

	public static void execute(String sql) throws SQLException {
		Connection conn = getConnection();
		Statement stat = conn.createStatement();
		stat.execute(sql);
		stat.close();
	}

	public static void execute(String prepStmt, String... arguments)
			throws SQLException {
		Connection conn = getConnection();
		PreparedStatement prep = createStatementWithArguments(prepStmt, conn,
				arguments);
		prep.execute();
		prep.close();
	}

	/**
	 * Execute the query sql and return the, resulting {@link ActiveRecord}s as a
	 * {@link List} of type returnedClass.<br>
	 * 
	 * @param returnedClass
	 *            : <b>MUST</b> implement a constructor with a {@link ResultSet}
	 *            as the only parameter.
	 */
	public static <T> List<T> getObjectList(String sql, Class<T> returnedClass) {
		ArrayList<T> list = new ArrayList<T>();
		try {
			Connection conn = getConnection();
			Statement stat = conn.createStatement();
			ResultSet res = stat.executeQuery(sql);
			while (res.next()) {
				Constructor<T> ctor = returnedClass
						.getConstructor(ResultSet.class);
				T object = ctor.newInstance(res);
				list.add(object);
			}
			stat.close();
		} catch (NoSuchMethodException me) {
			System.err.println("The class :" + returnedClass.getName()
					+ " has no Constructor for ResultSet");
			me.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error while creating class out of a RowSet\n"
					+ e);
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<Ersatzteil> getErsatzteilList(String sql) {
		ArrayList<Ersatzteil> mylist = new ArrayList<Ersatzteil>();
		DecimalFormat precisionTwo = new DecimalFormat("0.00");
		try {
			Connection conn = getConnection();
			Statement stat = conn.createStatement();
			ResultSet res = stat.executeQuery(sql);
			while (res.next()) {
				mylist.add(new Ersatzteil(res.getString("name"),precisionTwo.format(res.getDouble("preis")),res.getInt("anzahl"),res.getInt("id")));
			}
			stat.close();
		
		} catch (Exception e) {
			System.out.println("Error while creating ErsatzteilList out of a RowSet\n"
					+ e);
			e.printStackTrace();
		}
		return mylist;
	}
	
	public static ArrayList<Reservation> getReservationList(String sql) {
		ArrayList<Reservation> mylist = new ArrayList<Reservation>();
		try {
			Connection conn = getConnection();
			Statement stat = conn.createStatement();
			ResultSet res = stat.executeQuery(sql);
			while (res.next()) {
				mylist.add(new Reservation(res.getString("name"),res.getInt("anzahl"),res.getString("datum"), res.getString("reservator"), res.getInt("id"), res.getInt("fk_ersatzteil")));
			}
			stat.close();
		
		} catch (Exception e) {
			System.out.println("Error while creating ReservationList out of a RowSet\n"
					+ e);
			e.printStackTrace();
		}
		return mylist;
	}

	public static List<String> getStringList(String sql) {
		List<String> resultlist = new ArrayList<String>();
		Connection conn;
		try {
			conn = getConnection();
			Statement stat = conn.createStatement();
			ResultSet res = stat.executeQuery(sql);
			while (res.next()) {
				resultlist.add(res.getString(1));
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultlist;
	}

	private static int getIdOfInsertedElement(Statement stat)
			throws SQLException {
		int newId = -1;
		ResultSet res = stat.executeQuery("SELECT last_insert_rowid();");
		if (res.next())
			newId = res.getInt("last_insert_rowid()");
		return newId;
	}

	private static PreparedStatement createStatementWithArguments(
			String prepStmt, Connection conn, String... arguments)
			throws SQLException {
		PreparedStatement prep = conn.prepareStatement(prepStmt);

		for (int i = 0; i < arguments.length; ++i) {
			prep.setString(i + 1, arguments[i]);
		}
		return prep;
	}

	public static ArrayList<History> getHistoryList(String sql) {
		ArrayList<History> mylist = new ArrayList<History>();
		try {
			Connection conn = getConnection();
			Statement stat = conn.createStatement();
			ResultSet res = stat.executeQuery(sql);
			while (res.next()) {
				mylist.add(new History(res.getInt("id"),res.getString("datum"),res.getString("user"), res.getString("type"), res.getString("query"), res.getString("undoquery")));
			}
			stat.close();
		
		} catch (Exception e) {
			System.out.println("Error while creating ReservationList out of a RowSet\n"
					+ e);
			e.printStackTrace();
		}
		return mylist;
	}
}
