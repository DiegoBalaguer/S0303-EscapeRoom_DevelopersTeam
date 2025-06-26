package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import dao.interfaces.ConnectionDAO;
import dao.interfaces.PlayerDAO;
import lombok.extern.slf4j.Slf4j;
import model.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayerDAOH2Impl implements BaseDAO<Player, Integer>, PlayerDAO {

    private final ConnectionDAO connectionDAO;
    private static final String nameObject = "player";

    public PlayerDAOH2Impl(ConnectionDAO connectionDAO) {
        this.connectionDAO = connectionDAO;
    }

    @Override
    public Player create(Player player) throws DAOException {
        String sql = "INSERT INTO " + nameObject + " (name, email, password, isSubscribed, registrationDate) VALUES ( ?, ?, ?, ?, ?);";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, player.getName());
            stmt.setString(2, player.getEmail());
            stmt.setString(3, player.getPassword());
            stmt.setBoolean(4, player.isSubscribed());
            stmt.setDate(5, Date.valueOf(player.getRegistrationDate()));
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                player.setId(keys.getInt(1));
            }
            return player;
        } catch (Exception e) {
            String messageError = "Error creating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Optional<Player> findById(Integer id) throws DAOException {
        String sql = "SELECT idPlayer, name, email, isSubscribed, isActive FROM " + nameObject + " WHERE idPlayer = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToPlayer(rs));
            }
            return Optional.empty();
        } catch (Exception e) {
            String messageError = "Error finding " + nameObject + " by ID: ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public List<Player> findAll() throws DAOException {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT idPlayer, name, email, isSubscribed, isActive FROM " + nameObject + ";";
        try (Connection connection = connectionDAO.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                players.add(mapResultSetToPlayer(rs));
            }
            return players;
        } catch (SQLException e) {
            String messageError = "Error retrieving all " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

   /* @Override
    public Player save(Player player) throws DAOException {
        return null;
    }*/

    @Override
    public void deleteById(int id) throws DAOException {

    }

    @Override
    public Player update(Player player) throws DAOException {
        String sql = "UPDATE " + nameObject + " SET name = ?, email = ?, password = ?, isSubscribed = ?, registrationDate =?,  isActive = ? WHERE idPlayer = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, player.getName());
            stmt.setString(2, player.getEmail());
            stmt.setString(3, player.getPassword());
            stmt.setBoolean(4, player.isSubscribed());
            stmt.setDate(5, Date.valueOf(player.getRegistrationDate()));
            stmt.setBoolean(6, player.isActive());
            stmt.setInt(7, player.getId());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                String messageError = "No " + nameObject + " found to update with ID: " + player.getId();
                log.error(messageError);
                throw new DAOException(messageError);
            }
            return player;
        } catch (Exception e) {
            String messageError = "Error updating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public void deleteById(Integer id) throws DAOException {
        String sql = "DELETE FROM " + nameObject + " WHERE idPlayer = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                String messageError = "No " + nameObject + " found to delete with ID: " + id;
                log.error(messageError);
                throw new DAOException(messageError);
            }
        } catch (Exception e) {
            String messageError = "Error deleting " + nameObject + " by ID: " + id;
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public boolean isExistsById(Integer id) {
        String sql = "SELECT 1 FROM " + nameObject + " WHERE idPlayer = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            String messageError = "Error check if exist in " + nameObject + " the ID: " + id;
            log.error(messageError, e);
            return false;
        }
    }

   /* @Override
    public List<Player> findTopPlayers(int limit) throws DAOException {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT idPlayer, name, email, isSubscribed, isActive FROM " + nameObject + " ORDER BY name DESC LIMIT ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                players.add(mapResultSetToPlayer(rs));
            }
            return players;
        } catch (Exception e) {
            String messageError = "Error retrieving top " + nameObject + "s";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }*/

    @Override
    public Optional<Player> findById(int id) throws DAOException {
        return Optional.empty();
    }

 /*   @Override
    public Optional<Player> findByName(String name) throws DAOException {
        return Optional.empty();
    }*/

    private Player mapResultSetToPlayer(ResultSet rs) throws SQLException {
        return Player.builder()
                .id(rs.getInt("idPlayer"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .isSubscribed(rs.getBoolean("isSubscribed"))
                .isActive(rs.getBoolean("isActive"))
                .build();
    }
    /*@Override
    public Optional<Player> findByEmail(String email) {
        String query = "SELECT * FROM players WHERE email = ?";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Player player = Player.builder().build();
                player.setId(rs.getInt("id"));
                player.setName(rs.getString("name"));
                player.setEmail(rs.getString("email"));
                player.setPassword(rs.getString("password"));
                player.setSubscribed(rs.getBoolean("isSubscribed"));
                player.setActive(rs.getBoolean("isActive"));
                return Optional.of(player);
            }
        } catch (SQLException e) {
            log.error("Error finding Player by Email: {}", e.getMessage(), e);
        }
        return Optional.empty();
        }*/
}

