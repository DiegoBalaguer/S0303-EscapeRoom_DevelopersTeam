package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import dao.interfaces.ConnectionDAO;
import dao.interfaces.RewardDAO;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Reward;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class RewardDAOH2Impl implements BaseDAO<Reward, Integer>, RewardDAO {

    private final ConnectionDAO connectionDAO;
    private static final String nameObject = "reward";

    public RewardDAOH2Impl(ConnectionDAO connectionDAO) {
        this.connectionDAO = connectionDAO;
    }

    @Override
    public Reward create(Reward reward) throws DAOException {
        String sql = "INSERT INTO " + nameObject + " (name, description, isActive) VALUES (?, ?, ?);";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, reward.getName());
            stmt.setString(2, reward.getDescription());
            stmt.setBoolean(3, reward.isActive());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                reward.setId(keys.getInt(1));
            }
            return reward;
        } catch (Exception e) {
            String messageError = "Error creating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Optional<Reward> findById(Integer id) throws DAOException {
        String sql = "SELECT idReward, name, isActive, description FROM " + nameObject + " WHERE idReward = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(listResultSetToReward(rs));
            }
            return Optional.empty();
        } catch (Exception e) {
            String messageError = "Error finding " + nameObject + " by ID: ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public List<Reward> findAll() throws DAOException {
        List<Reward> rewards = new ArrayList<>();
        String sql = "SELECT idReward, name, isActive, description FROM " + nameObject + ";";
        try (Connection connection = connectionDAO.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rewards.add(listResultSetToReward(rs));
            }
            return rewards;
        } catch (SQLException e) {
            String messageError = "Error retrieving all " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Reward update(Reward reward) throws DAOException {
        String sql = "UPDATE " + nameObject + " SET name = ?, description = ?, isActive = ? WHERE idReward = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, reward.getName());
            stmt.setString(2, reward.getDescription());
            stmt.setBoolean(3, reward.isActive());
            stmt.setInt(4, reward.getId());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                String messageError = "No " + nameObject + " found to update with ID: " + reward.getId();
                log.error(messageError);
                throw new DAOException(messageError);
            }
            return reward;
        } catch (Exception e) {
            String messageError = "Error updating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public void deleteById(Integer id) throws DAOException {
        String sql = "DELETE FROM " + nameObject + " WHERE idReward = ?;";
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
        String sql = "SELECT 1 FROM " + nameObject + " WHERE idReward = ?;";
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

    private Reward listResultSetToReward(ResultSet rs) throws SQLException {
        return Reward.builder()
                .id(rs.getInt("idReward"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .isActive(rs.getBoolean("isActive"))
                .build();
    }
}

