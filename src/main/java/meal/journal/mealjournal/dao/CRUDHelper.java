package meal.journal.mealjournal.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CRUDHelper {
    private static final Log log = LogFactory.getLog(CRUDHelper.class);

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
                case Types.NUMERIC -> {
                    if (values[i] instanceof LocalDate date) {
                        queryBuilder.append("'").append(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("'");
                    } else {
                        queryBuilder.append(Double.parseDouble((String) values[i]));
                    }
                }
            }
            if (i < number - 1) queryBuilder.append(", ");
        }

        queryBuilder.append(");");

        try (Connection conn = Database.connect()) {
            if (conn != null) {
                PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString());
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            return rs.getLong(1);
                        }
                    }
                }
            } else {
                throw new SQLException("Connection is null");
            }
        } catch (SQLException e) {
            log.error("Could not add to database: " + e.getMessage());
            return -1;
        }
        return -1;
    }

    public static void delete(String tableName, int id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try (Connection conn = Database.connect()) {
            if (conn != null) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);

                pstmt.executeUpdate();
            } else {
                throw new SQLException("Connection is null");
            }
        } catch (SQLException e) {
            log.error("Could not delete from " + tableName + " by id " + id + " because " + e.getCause());
        }
    }
}