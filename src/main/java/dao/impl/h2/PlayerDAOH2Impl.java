package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import dao.interfaces.PlayerDAO;
import lombok.extern.slf4j.Slf4j;
import model.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayerDAOH2Impl implements BaseDAO<Player, Integer>, PlayerDAO {

     private final Connection connection;
     private static final String nameObject = "player";

    public PlayerDAOH2Impl() {
        this.connection = ConnectionDAOH2Impl.getConnection();
    }

    @Override
    public Player create(Player player) throws DAOException {
        String sql = "INSERT INTO ? (name, email, password, isSubscribed, isActive) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nameObject);
            stmt.setString(2, player.getName());
            stmt.setString(3, player.getEmail());
            stmt.setString(4, player.getPassword());
            stmt.setBoolean(5, player.isSubscribed());
            stmt.setBoolean(6, player.isActive());
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
        String sql = "SELECT idPlayer, name, email, isSubscribed, isActive FROM ? WHERE idPlayer = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, id);
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
        String sql = "SELECT idPlayer, name, email, isSubscribed, isActive FROM " + nameObject;
        try (Statement stmt = connection.createStatement();
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

    @Override
    public Player update(Player player) throws DAOException {
        String sql = "UPDATE ? SET name = ?, email = ?, password = ?, isSubscribed = ?, isActive = ? WHERE idPlayer = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nameObject);
            stmt.setString(2, player.getName());
            stmt.setString(3, player.getEmail());
            stmt.setString(4, player.getPassword());
            stmt.setBoolean(5, player.isSubscribed());
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
        String sql = "DELETE FROM ? WHERE idPlayer = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, id);
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
        String sql = "SELECT 1 FROM ? WHERE idPlayer = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            String messageError = "Error check if exist in " + nameObject + " the ID: " + id;
            log.error(messageError, e);
            return false;
        }
    }

    @Override
    public List<Player> findTopPlayers(int limit) throws DAOException {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT idPlayer, name, email, isSubscribed, isActive FROM ? ORDER BY name DESC LIMIT ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, limit);
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
    }

    private Player mapResultSetToPlayer(ResultSet rs) throws SQLException {
        return Player.builder()
                .id(rs.getInt("idPlayer"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .isSubscribed(rs.getBoolean("isSubscribed"))
                .isActive(rs.getBoolean("isActive"))
                .build();
    }
}
