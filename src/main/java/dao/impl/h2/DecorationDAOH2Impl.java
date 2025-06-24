package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import dao.interfaces.DecorationDAO;
import lombok.extern.slf4j.Slf4j;
import model.Decoration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DecorationDAOH2Impl implements BaseDAO<Decoration, Integer>, DecorationDAO {

    private final Connection connection;
    private static final String nameObject = "decoration";

    public DecorationDAOH2Impl() {
        this.connection = ConnectionDAOH2Impl.getConnection();
    }

    @Override
    public Decoration create(Decoration decoration) throws DAOException {
        String sql = "INSERT INTO ? (idRoom, name, description, price, isActive) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, decoration.getIdRoom());
            stmt.setString(3, decoration.getName());
            stmt.setString(4, decoration.getDescription());
            stmt.setBigDecimal(5, decoration.getPrice());
            stmt.setBoolean(6, decoration.isActive());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                decoration.setId(keys.getInt(1));
            }
            return decoration;
        } catch (Exception e) {
            String messageError = "Error creating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Optional<Decoration> findById(Integer id) throws DAOException {
        String sql = "SELECT idDecoration, idRoom, name, price, isActive FROM ? WHERE idDecoration = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToDecoration(rs));
            }
            return Optional.empty();
        } catch (Exception e) {
            String messageError = "Error finding " + nameObject + " by ID: ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public List<Decoration> findAll() throws DAOException {
        List<Decoration> decorations = new ArrayList<>();
        String sql = "SELECT idDecoration, idRoom, name, price, isActive FROM " + nameObject;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                decorations.add(mapResultSetToDecoration(rs));
            }
            return decorations;
        } catch (SQLException e) {
            String messageError = "Error retrieving all " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Decoration update(Decoration decoration) throws DAOException {
        String sql = "UPDATE ? SET idRoom = ?, name = ?, description = ?, price = ?, isActive = ? WHERE idDecoration = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, decoration.getIdRoom());
            stmt.setString(3, decoration.getName());
            stmt.setString(4, decoration.getDescription());
            stmt.setBigDecimal(5, decoration.getPrice());
            stmt.setBoolean(6, decoration.isActive());
            stmt.setInt(7, decoration.getId());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                String messageError = "No " + nameObject + " found to update with ID: " + decoration.getId();
                log.error(messageError);
                throw new DAOException(messageError);
            }
            return decoration;
        } catch (Exception e) {
            String messageError = "Error updating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public void deleteById(Integer id) throws DAOException {
        String sql = "DELETE FROM ? WHERE idDecoration = ?";
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
        String sql = "SELECT 1 FROM ? WHERE idDecoration = ?";
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

    private Decoration mapResultSetToDecoration(ResultSet rs) throws SQLException {
        return Decoration.builder()
                .id(rs.getInt("idDecoration"))
                .idRoom(rs.getInt("idRoom"))
                .name(rs.getString("name"))
                .price(rs.getBigDecimal("price"))
                .isActive(rs.getBoolean("isActive"))
                .build();
    }
}

