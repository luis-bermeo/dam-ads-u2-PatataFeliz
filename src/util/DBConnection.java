package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de gestionar la conexión con la base de datos MySQL.
 * Utiliza el driver mysql-connector
 * Proporciona un método estático para obtener conexiones JDBC, que
 * posteriormente serán usadas por los distintos DAOs del proyecto.
 *
 * @author Luis
 * @author Javi
 */

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/dama";
    private static final String USER = "root";
    private static final String PASS = "0000";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se pudo cargar el driver JDBC de MySQL", e);
        }
    }

    /**
     * Devuelve una conexión activa a la base de datos MySQL.
     *
     * @return una conexión JDBC lista para usar.
     * @throws SQLException si la conexión falla por credenciales incorrectas,
     *                      URL inválida o servidor MySQL no disponible.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
