package servicio;

import dao.*;
import modelo.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Clase que gestiona la lógica del club deportivo.
 * Permite dar de alta/baja socios, listar pistas y reservas,
 * y crear reservas con todas las validaciones necesarias.
 *
 * @author Javi
 */
public class ClubDeportivo {

    private SocioDAO socioDAO;
    private PistaDAO pistaDAO;
    private ReservaDAO reservaDAO;

    /**
     * Constructor que inicializa los DAOs necesarios.
     */
    public ClubDeportivo() {
        socioDAO = new SocioDAO();
        pistaDAO = new PistaDAO();
        reservaDAO = new ReservaDAO();
    }

    /**
     * Devuelve todos los socios del club.
     * @return lista de socios
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Javi
     */
    public ArrayList<Socio> getSocios() throws SQLException {
        return new ArrayList<>(socioDAO.findAll());
    }

    /**
     * Devuelve todas las pistas del club.
     * @return lista de pistas
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Javi
     */
    public ArrayList<Pista> getPistas() throws SQLException {
        return new ArrayList<>(pistaDAO.findAll());
    }

    /**
     * Devuelve todas las reservas del club.
     * @return lista de reservas
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Javi
     */
    public ArrayList<Reserva> getReservas() throws SQLException {
        return new ArrayList<>(reservaDAO.findAll());
    }

    /**
     * Da de alta un nuevo socio en el club.
     * @param socio socio a añadir
     * @throws SQLException si ocurre un error en la base de datos
     * @throws IllegalArgumentException si el socio ya existe
     * @author Javi
     */
    public void altaSocio(Socio socio) throws SQLException {
        if (socioDAO.findById(socio.getIdSocio()) != null) {
            throw new IllegalArgumentException("El socio ya existe");
        }
        socioDAO.insert(socio);
    }
    
    /**
     * Da de alta una nueva pista en el club.
     * @param pista pista a añadir
     * @throws SQLException si ocurre un error en la base de datos
     * @throws IllegalArgumentException si la pista ya existe
     */
    public void altaPista(Pista pista) throws SQLException {
        if (pistaDAO.findById(pista.getIdPista()) != null) {
            throw new IllegalArgumentException("La pista ya existe");
        }
        pistaDAO.insert(pista);
    }

    /**
     * Da de baja un socio del club.
     * Solo se puede dar de baja si no tiene reservas futuras.
     * @param idSocio id del socio a eliminar
     * @throws SQLException si ocurre un error en la base de datos
     * @throws IllegalArgumentException si el socio tiene reservas futuras
     * @author Javi
     */
    public void bajaSocio(String idSocio) throws SQLException {
        ArrayList<Reserva> reservas = getReservas();
        boolean tieneFuturas = reservas.stream()
                .anyMatch(r -> r.getIdSocio().equals(idSocio)
                        && r.getFecha().isAfter(LocalDate.now()));
        if (tieneFuturas) {
            throw new IllegalArgumentException("El socio tiene reservas futuras");
        }
        socioDAO.delete(idSocio);
    }

    /**
     * Cambia el estado de disponibilidad de una pista.
     * @param pista la pista a modificar
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void cambiarDisponibilidadPista(Pista pista) throws SQLException {
        pistaDAO.update(pista);
    }

    /**
     * Crea una reserva en el club.
     * Realiza validaciones de socio activo, solapes y fechas pasadas.
     * Calcula el precio usando el procedimiento almacenado.
     * @param reserva reserva a crear
     * @throws SQLException si ocurre un error en la base de datos
     * @throws IllegalArgumentException si falla alguna validación
     * @author Javi
     */
    public void crearReserva(Reserva reserva) throws SQLException {
        // Validar socio
        Socio socio = socioDAO.findById(reserva.getIdSocio());
        if (socio == null) {
            throw new IllegalArgumentException("Socio no activo o no existe");
        }

        // Validar solapes
        ArrayList<Reserva> reservas = getReservas();
        boolean solapa = reservas.stream()
                .anyMatch(r -> r.getIdPista().equals(reserva.getIdPista())
                        && r.getFecha().equals(reserva.getFecha())
                        && reserva.getHoraInicio().isBefore(r.getHoraInicio().plusMinutes(r.getDuracionMin()))
                        && r.getHoraInicio().isBefore(reserva.getHoraInicio().plusMinutes(reserva.getDuracionMin())));
        if (solapa) {
            throw new IllegalArgumentException("La pista ya está reservada en ese horario");
        }

        // Validar que no sea en el pasado
        if (reserva.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede reservar en fechas pasadas");
        }

        // Crear reserva usando DAO y SP
        reservaDAO.crearReservaSP(reserva);
    }

    /**
     * Cancela una reserva.
     * @param idReserva id de la reserva a cancelar
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void cancelarReserva(String idReserva) throws SQLException {
        reservaDAO.delete(idReserva);
    }

    /**
     * Lista todos los socios en consola (opcional para debug o UI).
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Javi
     */
    public void listarSocios() throws SQLException {
        getSocios().forEach(System.out::println);
    }

    /**
     * Lista todas las pistas en consola (opcional para debug o UI).
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Javi
     */
    public void listarPistas() throws SQLException {
        getPistas().forEach(System.out::println);
    }

    /**
     * Lista todas las reservas en consola (opcional para debug o UI).
     * @throws SQLException si ocurre un error de conexión o consulta
     * @author Javi
     */
    public void listarReservas() throws SQLException {
        getReservas().forEach(System.out::println);
    }
}
