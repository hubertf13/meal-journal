package meal.journal.mealjournal.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CRUDHelper {

    public static Object read(String tableName, String fieldName, int fieldDataType,
                              String indexFieldName, int indexDataType, Object index) {
        StringBuilder queryBuilder = new StringBuilder("Select ");
        queryBuilder.append(fieldName);
        queryBuilder.append(" from ");
        queryBuilder.append(tableName);
        queryBuilder.append(" where ");
        queryBuilder.append(indexFieldName);
        queryBuilder.append(" = ");
        queryBuilder.append(convertObjectToSQLField(index, indexDataType));

        try (Connection connection = Database.connect()) {
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());

                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();

                    return switch (fieldDataType) {
                        case Types.INTEGER -> rs.getInt(fieldName);
                        case Types.VARCHAR -> rs.getString(fieldName);
                        default ->
                                throw new IllegalArgumentException("Index type " + indexDataType + " from sql.Types is not yet supported.");
                    };
                }
            } else {
                throw new SQLException("Connection is null");
            }
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not fetch from " + tableName + " by index " + index +
                            " and column " + fieldName);
            return null;
        }
    }

    public static long update(String tableName, String[] columns, Object[] values, int[] types,
                              String indexFieldName, int indexDataType, Object index) {
        int number = Math.min(Math.min(columns.length, values.length), types.length);
        StringBuilder queryBuilder = new StringBuilder("UPDATE " + tableName + " SET ");

        for (int i = 0; i < number; i++) {
            queryBuilder.append(columns[i]);
            queryBuilder.append(" = ");
            queryBuilder.append(convertObjectToSQLField(values[i], types[i]));
            if (i < number - 1) queryBuilder.append(", ");
        }

        queryBuilder.append(" WHERE ");
        queryBuilder.append(indexFieldName);
        queryBuilder.append(" = ");
        queryBuilder.append(convertObjectToSQLField(index, indexDataType));

        try (Connection conn = Database.connect()) {
            if (conn != null) {
                PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString());
                return pstmt.executeUpdate(); //number of affected rows
            } else {
                throw new SQLException("Connection is null");
            }

        } catch (SQLException ex) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not add to database");
            return -1;
        }
    }

    public static long create(String tableName, String[] columns, Object[] values, int[] types) {
        int number = Math.min(Math.min(columns.length, values.length), types.length);
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + tableName + " (");

        for (int i = 0; i < number; i++) {
            queryBuilder.append(columns[i]);
            if (i < number - 1) queryBuilder.append(", ");
        }

        queryBuilder.append(") ");
        queryBuilder.append(" VALUES (");

        for (int i = 0; i < number; i++) {
            switch (types[i]) {
                case Types.VARCHAR -> {
                    queryBuilder.append("'");
                    queryBuilder.append((String) values[i]);
                    queryBuilder.append("'");
                }
                case Types.INTEGER -> queryBuilder.append((int) values[i]);
                case Types.NUMERIC -> queryBuilder.append(Double.parseDouble((String) values[i]));
            }
            if (i < number - 1) queryBuilder.append(", ");
        }

        queryBuilder.append(");");

        try (Connection conn = Database.connect()) {
            if (conn != null) {
                PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString());
                int affectedRows = pstmt.executeUpdate();
                // check the affected rows
                if (affectedRows > 0) {
                    // get the ID back
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            return rs.getLong(1);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not add to database");
            return -1;
        }
        return -1;
    }

    public static int delete(String tableName, int id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try (Connection conn = Database.connect()) {
            if (conn != null) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);

                return pstmt.executeUpdate();
            } else {
                throw new SQLException("Connection is null");
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not delete from " + tableName + " by id " + id +
                            " because " + e.getCause());
            return -1;
        }
    }

    private static String convertObjectToSQLField(Object value, int type) {
        StringBuilder queryBuilder = new StringBuilder();

        switch (type) {
            case Types.VARCHAR -> {
                queryBuilder.append("'");
                queryBuilder.append(value);
                queryBuilder.append("'");
            }
            case Types.INTEGER -> queryBuilder.append(value);
            case Types.NUMERIC -> queryBuilder.append(Double.parseDouble((String) value));
            default ->
                    throw new IllegalArgumentException("Index type " + type + " from sql.Types is not yet supported.");
        }
        return queryBuilder.toString();
    }
}