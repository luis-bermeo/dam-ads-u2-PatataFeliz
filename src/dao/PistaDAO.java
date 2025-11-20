package dao;

import modelo.Pista;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla pistas.
 * Gestiona operaciones CRUD sobre pistas en la base de datos MariaDB.
 *
 * @author Luis
 */
public class PistaDAO {

    /**
     * Obtiene todas las pistas de la base de datos.
     * @return lista de pistas
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Luis
     */
    public List<Pista> findAll() throws SQLException {
        List<Pista> pistas = new ArrayList<>();
        String sql = "SELECT * FROM pistas";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pista p = new Pista(
                        rs.getString("id"),
                        rs.getString("tipo"),
                        rs.getString("nombre"),
                        rs.getBoolean("disponible")
                );
                pistas.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pistas;
    }

    /**
     * Busca una pista por su ID.
     * @param id id de la pista
     * @return la pista encontrada o null si no existe
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Luis
     */
    public Pista findById(String id) throws SQLException {
        String sql = "SELECT * FROM pistas WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Pista(
                        rs.getString("id"),
                        rs.getString("tipo"),
                        rs.getString("nombre"),
                        rs.getBoolean("disponible")
                );
            }
        }
        return null;
    }

    /**
     * Inserta una nueva pista en la base de datos.
     * @param pista pista a insertar
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Luis
     */
    public void insert(Pista pista) throws SQLException {
        String sql = "INSERT INTO pistas(id, tipo, nombre, disponible) VALUES(?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pista.getIdPista());
            ps.setString(2, pista.getDeporte());
            ps.setString(3, pista.getDescripcion());
            ps.setBoolean(4, pista.isDisponible());
            ps.executeUpdate();
        }
    }

    /**
     * Actualiza una pista existente.
     * @param pista pista con datos actualizados
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Luis
     */
    public void update(Pista pista) throws SQLException {
        String sql = "UPDATE pistas SET tipo=?, nombre=?, disponible=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pista.getDeporte());
            ps.setString(2, pista.getDescripcion());
            ps.setBoolean(3, pista.isDisponible());
            ps.setString(4, pista.getIdPista());
            ps.executeUpdate();
        }
    }

    /**
     * Elimina una pista por su ID.
     * @param id id de la pista a eliminar
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Luis
     */
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM pistas WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
        }
    }
}
