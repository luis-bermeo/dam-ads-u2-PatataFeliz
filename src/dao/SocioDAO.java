package dao;

import modelo.Socio;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla socios.
 * Gestiona operaciones CRUD sobre socios en la base de datos MariaDB.
 *
 * @author Luis
 */
public class SocioDAO {

    /**
     * Obtiene todos los socios de la base de datos.
     * @return lista de socios
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Luis
     */
    public List<Socio> findAll() throws SQLException {
        List<Socio> socios = new ArrayList<>();
        String sql = "SELECT * FROM socios";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Socio s = new Socio(
                        rs.getString("id_socio"),
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                socios.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return socios;
    }

    /**
     * Busca un socio por su ID.
     * @param id id del socio
     * @return el socio encontrado o null si no existe
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Luis
     */
    public Socio findById(String id) throws SQLException {
        String sql = "SELECT * FROM socios WHERE id_socio=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Socio(
                        rs.getString("id_socio"),
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserta un nuevo socio en la base de datos.
     * @param socio socio a insertar
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Luis
     */
    public void insert(Socio socio) throws SQLException {
        String sql = "INSERT INTO socios(id_socio, dni, nombre, apellidos, telefono, email) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, socio.getIdSocio());
            ps.setString(2, socio.getDni());
            ps.setString(3, socio.getNombre());
            ps.setString(4, socio.getApellidos());
            ps.setString(5, socio.getTelefono());
            ps.setString(6, socio.getEmail());
            ps.executeUpdate();
        }
    }

    /**
     * Actualiza un socio existente.
     * @param socio socio con los datos actualizados
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Luis
     */
    public void update(Socio socio) throws SQLException {
        String sql = "UPDATE socios SET dni=?, nombre=?, apellidos=?, telefono=?, email=? WHERE id_socio=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, socio.getDni());
            ps.setString(2, socio.getNombre());
            ps.setString(3, socio.getApellidos());
            ps.setString(4, socio.getTelefono());
            ps.setString(5, socio.getEmail());
            ps.setString(6, socio.getIdSocio());
            ps.executeUpdate();
        }
    }

    /**
     * Elimina un socio por su ID.
     * @param id id del socio a eliminar
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Luis
     */
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM socios WHERE id_socio=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
        }
    }
}
