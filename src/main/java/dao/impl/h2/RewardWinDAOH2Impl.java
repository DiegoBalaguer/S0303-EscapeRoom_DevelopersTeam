package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import dao.interfaces.ConnectionDAO;
import dao.interfaces.RewardWinDAO;
import lombok.extern.slf4j.Slf4j;
import model.RewardWin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class RewardWinDAOH2Impl implements BaseDAO<RewardWin, Integer>, RewardWinDAO {

    private final ConnectionDAO connectionDAO;
    private static final String nameObject = "rewardWin";

    public RewardWinDAOH2Impl(ConnectionDAO connectionDAO) {
        this.connectionDAO = connectionDAO;
    }

    @Override
    public RewardWin create(RewardWin rewardWin) throws DAOException {
        String sql = "INSERT INTO " + nameObject + " (idReward, idPlayer, description, dateDelivery, isActive) VALUES (?, ?, ?, ?, ?);";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, rewardWin.getIdReward());
            stmt.setInt(2, rewardWin.getIdPlayer());
            stmt.setString(3, rewardWin.getDescription());
            stmt.setDate(4, Date.valueOf(rewardWin.getDateDelivery()));
            stmt.setBoolean(5, rewardWin.isActive());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                rewardWin.setId(keys.getInt(1));
            }
            return rewardWin;
        } catch (Exception e) {
            String messageError = "Error creating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Optional<RewardWin> findById(Integer id) throws DAOException {
        String sql = "SELECT idRewardWin, idReward, idPlayer, isActive FROM " + nameObject + " WHERE idRewardWin = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToRewardWin(rs));
            }
            return Optional.empty();
        } catch (Exception e) {
            String messageError = "Error finding " + nameObject + " by ID: ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public List<RewardWin> findAll() throws DAOException {
        List<RewardWin> rewardWins = new ArrayList<>();
        String sql = "SELECT idRewardWin, idReward, idPlayer, isActive FROM " + nameObject + ";";
        try (Connection connection = connectionDAO.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rewardWins.add(mapResultSetToRewardWin(rs));
            }
            return rewardWins;
        } catch (SQLException e) {
            String messageError = "Error retrieving all " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public RewardWin update(RewardWin rewardWin) throws DAOException {
        String sql = "UPDATE " + nameObject + " SET idReward = ?, idPlayer = ?, description = ?, dateDelivery = ?, isActive = ? WHERE idRewardWin = ?";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rewardWin.getIdReward());
            stmt.setInt(2, rewardWin.getIdPlayer());
            stmt.setString(3, rewardWin.getDescription());
            stmt.setDate(4, Date.valueOf(rewardWin.getDateDelivery()));
            stmt.setBoolean(5, rewardWin.isActive());
            stmt.setInt(6, rewardWin.getId());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                String messageError = "No " + nameObject + " found to update with ID: " + rewardWin.getId();
                log.error(messageError);
                throw new DAOException(messageError);
            }
            return rewardWin;
        } catch (Exception e) {
            String messageError = "Error updating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public void deleteById(Integer id) throws DAOException {
        String sql = "DELETE FROM " + nameObject + " WHERE idRewardWin = ?;";
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
        String sql = "SELECT 1 FROM " + nameObject + " WHERE idRewardWin = ?;";
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

    private RewardWin mapResultSetToRewardWin(ResultSet rs) throws SQLException {
        return RewardWin.builder()
                .id(rs.getInt("idRewardWin"))
                .idReward(rs.getInt("idReward"))
                .idPlayer(rs.getInt("idPlayer"))
                 .isActive(rs.getBoolean("isActive"))
                .build();
    }
}
