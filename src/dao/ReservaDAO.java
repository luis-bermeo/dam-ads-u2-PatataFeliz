package dao;

import modelo.Reserva;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla reservas.
 * Gestiona operaciones CRUD y llamadas al procedimiento almacenado sp_crear_reserva.
 *
 * @author Luis
 */
public class ReservaDAO {

    /**
     * Obtiene todas las reservas de la base de datos.
     * @return lista de reservas
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Luis
     */
    public List<Reserva> findAll() throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reservas";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Corregido para usar las columnas correctas de la BD
                Reserva r = new Reserva(
                        rs.getString("id_reserva"),
                        rs.getString("id_socio"),
                        rs.getString("id_pista"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora_inicio").toLocalTime(),
                        rs.getInt("duracion_min"),
                        rs.getDouble("precio")
                );
                reservas.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservas;
    }

    /**
     * Crea una reserva usando el procedimiento almacenado sp_crear_reserva.
     * @param reserva reserva a crear
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Luis
     */
    public void crearReservaSP(Reserva reserva) throws SQLException {
        // La llamada ahora tiene 7 parámetros para coincidir con tu SP
        String sql = "{CALL sp_crear_reserva(?,?,?,?,?,?,?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            // Asignamos los parámetros EN EL ORDEN CORRECTO
            cs.setString(1, reserva.getIdReserva());
            cs.setString(2, reserva.getIdPista());
            cs.setString(3, reserva.getIdSocio());
            cs.setDate(4, java.sql.Date.valueOf(reserva.getFecha()));
            cs.setTime(5, java.sql.Time.valueOf(reserva.getHoraInicio()));
            cs.setInt(6, reserva.getDuracionMin());

            // Registramos el parámetro de SALIDA (OUT) en la posición 7
            cs.registerOutParameter(7, Types.DECIMAL);

            cs.execute();

            // Obtenemos el precio desde el parámetro de salida 7
            reserva.setPrecio(cs.getDouble(7));
        }
    }

    /**
     * Elimina una reserva por su ID.
     * @param id id de la reserva a eliminar
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Luis
     */
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM reservas WHERE id_reserva=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
        }
    }
}
