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
                Reserva r = new Reserva(
                        rs.getString("id"),
                        rs.getString("id_socio"),
                        rs.getString("id_pista"),
                        rs.getDate("inicio").toLocalDate(),
                        rs.getTime("inicio").toLocalTime(),
                        (int) (rs.getTimestamp("fin").getTime() - rs.getTimestamp("inicio").getTime()) / 60000,
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
        String sql = "{CALL sp_crear_reserva(?,?,?,?,?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, reserva.getIdPista());
            cs.setString(2, reserva.getIdSocio());
            cs.setTimestamp(3, Timestamp.valueOf(reserva.getFecha().atTime(reserva.getHoraInicio())));
            cs.setTimestamp(4, Timestamp.valueOf(reserva.getFecha().atTime(reserva.getHoraInicio().plusMinutes(reserva.getDuracionMin()))));
            cs.registerOutParameter(5, Types.DOUBLE);

            cs.execute();
            reserva.setPrecio(cs.getDouble(5));
        }
    }

    /**
     * Elimina una reserva por su ID.
     * @param id id de la reserva a eliminar
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Luis
     */
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM reservas WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
        }
    }
}
