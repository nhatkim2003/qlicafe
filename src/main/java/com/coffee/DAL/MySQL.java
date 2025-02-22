package com.coffee.DAL;

import com.coffee.utils.Database;
import javafx.util.Pair;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MySQL {
    public MySQL() {
    }

    public List<List<String>> executeQuery(String query, Object... values) throws SQLException, IOException {
        Connection connection = Database.getConnection();
        if (connection == null)
            return new ArrayList<>();
        List<List<String>> result;
        try (Statement statement = connection.createStatement()) {
            String formattedQuery = formatQuery(query, values);
            result = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(formattedQuery);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                List<String> row = new ArrayList<>(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getObject(i).toString());
                }
                result.add(row);
            }
//            System.out.println(formattedQuery);
//            System.out.println();
        }
        Database.closeConnection(connection);
        return result;
    }

    public int executeUpdate(String query, Object... values) throws SQLException, IOException {
        Connection connection = Database.getConnection();
        if (connection == null)
            return 0;
        int numOfRows;
        try (Statement statement = connection.createStatement()) {
            String formattedQuery = formatQuery(query, values);
            numOfRows = statement.executeUpdate(formattedQuery);
            System.out.println(formattedQuery);
            System.out.println();
        }
        Database.closeConnection(connection);
        return numOfRows;
    }

    public void executeProcedureAddMaterial(int id, String size, int quantity) throws SQLException, IOException {
        Connection connection = Database.getConnection();
        if (connection == null)
            return;
        int numOfRows;
        try (Statement statement = connection.createStatement()) {
            CallableStatement cstmt = connection.prepareCall("{CALL addMaterial(?, ?, ?)}");

            // Thiết lập tham số
            cstmt.setLong(1, id);
            cstmt.setString(2, size);
            cstmt.setInt(3, quantity);

            // Thực thi thủ tục lưu trữ và kiểm tra kết quả
            boolean hasResultSet = cstmt.execute();
        }
        Database.closeConnection(connection);
    }

    public void executeProcedureSubMaterial(int id, String size, int quantity) throws SQLException, IOException {
        Connection connection = Database.getConnection();
        if (connection == null)
            return;
        int numOfRows;
        try (Statement statement = connection.createStatement()) {
            CallableStatement cstmt = connection.prepareCall("{CALL subMaterial(?, ?, ?)}");

            // Thiết lập tham số
            cstmt.setLong(1, id);
            cstmt.setString(2, size);
            cstmt.setInt(3, quantity);

            // Thực thi thủ tục lưu trữ và kiểm tra kết quả
            boolean hasResultSet = cstmt.execute();
        }
        Database.closeConnection(connection);
    }

    public String formatQuery(String query, Object... values) {
        String stringValue;
        for (Object value : values) {
            if (value instanceof Date day) {
                stringValue = "'" + day + "'";
            } else if (value instanceof String || value instanceof Character) {
                stringValue = "'" + value + "'";
            } else if (value instanceof Boolean) {
                stringValue = (boolean) value ? "1" : "0";
            } else if (value instanceof Number) {
                stringValue = value.toString();
            } else if (value instanceof LocalDateTime) {
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                stringValue = "'" + ((LocalDateTime) value).format(myFormatObj) + "'";
            } else {
                stringValue = "'" + value + "'";
            }
            query = query.replaceFirst("\\?", stringValue);
        }
        return query;
    }

    public static List<List<String>> executeQueryStatistic(String query) throws SQLException, IOException {
        Connection connection = Database.getConnection();
        if (connection == null)
            return new ArrayList<>();
        List<List<String>> result;
        try (Statement statement = connection.createStatement()) {
            result = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                List<String> row = new ArrayList<>(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    if (resultSet.getObject(i) == null)
                        row.add("0");
                    else
                        row.add(resultSet.getObject(i).toString());
                }
                result.add(row);
            }
//            System.out.println(query);
//            System.out.println();
        }
        Database.closeConnection(connection);
        return result;
    }


    public static List<List<String>> getSaleProduct(String productName, String size, String productCategory, String... dates) {
        String query = "SELECT pro.id, pro.`name`, pro.size, SUM(rd.price_discount), SUM(rd.quantity)\n" +
                "FROM receipt rp JOIN receipt_detail rd ON rp.id = rd.receipt_id\n" +
                "\t\t\t\t\t\t\t\tJOIN product pro ON pro.id = rd.product_id AND pro.size = rd.size\n";
        if ((productName != null && size != null) || productCategory != null || dates.length != 0) {
            String[] strings = new String[0];
            if (productName != null && size != null) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "pro.name = '" + productName + "' AND pro.size = '" + size + "'";
            }

            if (!Objects.equals(productCategory, "Tất cả")) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "pro.category = '" + productCategory + "'";
            }

            if (dates.length != 0) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "DATE(rp.invoice_date) >= '" + dates[0] + "' AND DATE(rp.invoice_date) <= '" + dates[1] + "'";
            }
            query += "WHERE " + String.join(" AND ", strings);
        }
        query += "\nGROUP BY pro.id, pro.`name`, pro.size\n";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getTop5SaleProduct(String productName, String size, String productCategory, String... dates) {
        String query = "SELECT pro.id, pro.`name`, pro.size, SUM(rd.price_discount), SUM(rd.quantity)\n" +
                "FROM receipt rp JOIN receipt_detail rd ON rp.id = rd.receipt_id\n" +
                "\t\t\t\t\t\t\t\tJOIN product pro ON pro.id = rd.product_id AND pro.size = rd.size\n";
        if ((productName != null && size != null) || productCategory != null || dates.length != 0) {
            String[] strings = new String[0];
            if (productName != null && size != null) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "pro.name = '" + productName + "' AND pro.size = '" + size + "'";
            }

            if (!Objects.equals(productCategory, "Tất cả")) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "pro.category = '" + productCategory + "'";
            }

            if (dates.length != 0) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "DATE(rp.invoice_date) >= '" + dates[0] + "' AND DATE(rp.invoice_date) <= '" + dates[1] + "'";
            }
            query += "WHERE " + String.join(" AND ", strings);
        }
        query += "\nGROUP BY pro.id, pro.`name`, pro.size\n" +
                "ORDER BY SUM(rd.price_discount) DESC LIMIT 5";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getTop5BestSellers(String productName, String size, String productCategory, String... dates) {
        String query = "SELECT pro.id, pro.`name`, pro.size, SUM(rd.quantity)\n" +
                "FROM receipt rp JOIN receipt_detail rd ON rp.id = rd.receipt_id\n" +
                "\t\t\t\t\t\t\t\tJOIN product pro ON pro.id = rd.product_id AND pro.size = rd.size\n";
        if ((productName != null && size != null) || productCategory != null || dates.length != 0) {
            String[] strings = new String[0];
            if (productName != null && size != null) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "pro.name = '" + productName + "' AND pro.size = '" + size + "'";
            }

            if (!Objects.equals(productCategory, "Tất cả")) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "pro.category = '" + productCategory + "'";
            }

            if (dates.length != 0) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "DATE(rp.invoice_date) >= '" + dates[0] + "' AND DATE(rp.invoice_date) <= '" + dates[1] + "'";
            }
            query += "WHERE " + String.join(" AND ", strings);
        }
        query += "\nGROUP BY pro.id, pro.`name`, pro.size\n" +
                "ORDER BY SUM(rd.quantity) DESC LIMIT 5";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getProfitProduct(String productName, String size, String productCategory, String... dates) {
        String query = "SELECT pro.id, pro.`name`, pro.size, SUM(rd.price_discount - pro.capital_price * rd.quantity), SUM(rd.quantity), SUM(rd.price_discount), SUM(pro.capital_price * rd.quantity), SUM(rd.price_discount - pro.capital_price * rd.quantity)/SUM(rd.price_discount)*100\n" +
                "FROM receipt rp JOIN receipt_detail rd ON rp.id = rd.receipt_id\n" +
                "\t\t\t\t\t\t\t\tJOIN product pro ON pro.id = rd.product_id AND pro.size = rd.size\n";
        if ((productName != null && size != null) || productCategory != null || dates.length != 0) {
            String[] strings = new String[0];
            if (productName != null && size != null) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "pro.name = '" + productName + "' AND pro.size = '" + size + "'";
            }

            if (!Objects.equals(productCategory, "Tất cả")) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "pro.category = '" + productCategory + "'";
            }

            if (dates.length != 0) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "DATE(rp.invoice_date) >= '" + dates[0] + "' AND DATE(rp.invoice_date) <= '" + dates[1] + "'";
            }
            query += "WHERE " + String.join(" AND ", strings);
        }
        query += "\nGROUP BY pro.id, pro.`name`, pro.size\n";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getTop5ProfitProduct(String productName, String size, String productCategory, String... dates) {
        String query = "SELECT pro.id, pro.`name`, pro.size, SUM(rd.price_discount - pro.capital_price * rd.quantity), SUM(rd.quantity), SUM(rd.price_discount), SUM(pro.capital_price * rd.quantity), SUM(rd.price_discount - pro.capital_price * rd.quantity)/SUM(rd.price_discount)*100\n" +
                "FROM receipt rp JOIN receipt_detail rd ON rp.id = rd.receipt_id\n" +
                "\t\t\t\t\t\t\t\tJOIN product pro ON pro.id = rd.product_id AND pro.size = rd.size\n";
        if ((productName != null && size != null) || productCategory != null || dates.length != 0) {
            String[] strings = new String[0];
            if (productName != null && size != null) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "pro.name = '" + productName + "' AND pro.size = '" + size + "'";
            }

            if (!Objects.equals(productCategory, "Tất cả")) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "pro.category = '" + productCategory + "'";
            }

            if (dates.length != 0) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "DATE(rp.invoice_date) >= '" + dates[0] + "' AND DATE(rp.invoice_date) <= '" + dates[1] + "'";
            }
            query += "WHERE " + String.join(" AND ", strings);
        }
        query += "\nGROUP BY pro.id, pro.`name`, pro.size\n" +
                "ORDER BY SUM(rd.price_discount - pro.capital_price * rd.quantity) DESC LIMIT 5";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getTop5CapitalizationRate(String productName, String size, String productCategory, String... dates) {
        String query = "SELECT pro.id, pro.`name`, pro.size, SUM(rd.price_discount - pro.capital_price * rd.quantity)/SUM(rd.price_discount)*100\n" +
                "FROM receipt rp JOIN receipt_detail rd ON rp.id = rd.receipt_id\n" +
                "\t\t\t\t\t\t\t\tJOIN product pro ON pro.id = rd.product_id AND pro.size = rd.size\n";
        if ((productName != null && size != null) || productCategory != null || dates.length != 0) {
            String[] strings = new String[0];
            if (productName != null && size != null) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "pro.name = '" + productName + "' AND pro.size = '" + size + "'";
            }

            if (!Objects.equals(productCategory, "Tất cả")) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "pro.category = '" + productCategory + "'";
            }

            if (dates.length != 0) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                strings[strings.length - 1] = "DATE(rp.invoice_date) >= '" + dates[0] + "' AND DATE(rp.invoice_date) <= '" + dates[1] + "'";
            }
            query += "WHERE " + String.join(" AND ", strings);
        }
        query += "\nGROUP BY pro.id, pro.`name`, pro.size\n" +
                "ORDER BY SUM(rd.price_discount - pro.capital_price * rd.quantity)/SUM(rd.price_discount)*100 DESC LIMIT 5";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getTotalImport(String materialName, String start, String end) {
        String query = "SELECT ma.id, ma.`name`, SUM(sm.quantity)\n" +
                "FROM shipment sm JOIN material ma ON sm.material_id = ma.id\n" +
                "\t\t\t\t\t\t\t\t\tJOIN import_note im on im.id = sm.import_id\n";
        String[] strings = new String[0];
        if (materialName != null) {
            strings = Arrays.copyOf(strings, strings.length + 1);
            strings[strings.length - 1] = "ma.name = '" + materialName + "'";
        }
        strings = Arrays.copyOf(strings, strings.length + 1);
        strings[strings.length - 1] = "DATE(im.received_date) >= '" + start + "' AND DATE(im.received_date) <= '" + end + "'";
        query += "WHERE " + String.join(" AND ", strings);
        query += "\nGROUP BY ma.id, ma.`name`";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getTotalExport(String materialName, String start, String end) {
        String query = "SELECT ma.id, ma.`name`, SUM(CASE WHEN ed.reason = 'Huỷ' THEN ed.quantity ELSE 0 END), SUM(CASE WHEN ed.reason = 'Bán' THEN ed.quantity ELSE 0 END), SUM(ed.quantity)\n" +
                "FROM export_detail ed JOIN shipment sm ON ed.shipment_id = sm.id\n" +
                "\t\t\t\t\t\t\t\t\tJOIN material ma ON sm.material_id = ma.id\n" +
                "\t\t\t\t\t\t\t\t\tJOIN export_note ep on ep.id = ed.export_id\n";
        String[] strings = new String[0];
        if (materialName != null) {
            strings = Arrays.copyOf(strings, strings.length + 1);
            strings[strings.length - 1] = "ma.name = '" + materialName + "'";
        }
        strings = Arrays.copyOf(strings, strings.length + 1);
        strings[strings.length - 1] = "ep.invoice_date >= '" + start + "' AND ep.invoice_date <= '" + end + "'";
        query += "WHERE " + String.join(" AND ", strings);
        query += "\nGROUP BY ma.id, ma.`name`";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getTop5MostImport(String materialName, String start, String end) {
        String query = "SELECT ma.id, ma.`name`, SUM(sm.quantity)\n" +
                "FROM shipment sm JOIN material ma ON sm.material_id = ma.id\n" +
                "\t\t\t\t\t\t\t\t\tJOIN import_note im on im.id = sm.import_id\n";
        String[] strings = new String[0];
        if (materialName != null) {
            strings = Arrays.copyOf(strings, strings.length + 1);
            strings[strings.length - 1] = "ma.name = '" + materialName + "'";
        }
        strings = Arrays.copyOf(strings, strings.length + 1);
        strings[strings.length - 1] = "DATE(im.received_date) >= '" + start + "' AND DATE(im.received_date) <= '" + end + "'";
        query += "WHERE " + String.join(" AND ", strings);
        query += "\nGROUP BY ma.id, ma.`name`\n";
        query += "ORDER BY SUM(sm.quantity) DESC LIMIT 5\n";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getTop5MostExports(String materialName, String start, String end) {
        String query = "SELECT ma.id, ma.`name`, SUM(CASE WHEN ed.reason = 'Huỷ' THEN ed.quantity ELSE 0 END), SUM(CASE WHEN ed.reason = 'Bán' THEN ed.quantity ELSE 0 END), SUM(ed.quantity)\n" +
                "FROM export_detail ed JOIN shipment sm ON ed.shipment_id = sm.id\n" +
                "\t\t\t\t\t\t\t\t\tJOIN material ma ON sm.material_id = ma.id\n" +
                "\t\t\t\t\t\t\t\t\tJOIN export_note ep on ep.id = ed.export_id\n";
        String[] strings = new String[0];
        if (materialName != null) {
            strings = Arrays.copyOf(strings, strings.length + 1);
            strings[strings.length - 1] = "ma.name = '" + materialName + "'";
        }
        strings = Arrays.copyOf(strings, strings.length + 1);
        strings[strings.length - 1] = "ep.invoice_date >= '" + start + "' AND ep.invoice_date <= '" + end + "'";
        query += "WHERE " + String.join(" AND ", strings);
        query += "\nGROUP BY ma.id, ma.`name`\n";
        query += "ORDER BY SUM(ed.quantity) DESC LIMIT 5\n";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getDestroyExports(String materialName, String start, String end) {
        String query = "SELECT ma.id, ma.`name`,  SUM(ed.quantity), ma.unit_price * SUM(ed.quantity)\n" +
                "FROM export_detail ed JOIN shipment sm ON ed.shipment_id = sm.id\n" +
                "\t\t\t\t\t\t\t\t\tJOIN material ma ON sm.material_id = ma.id\n" +
                "\t\t\t\t\t\t\t\t\tJOIN export_note ep on ep.id = ed.export_id\n";
        String[] strings = new String[0];
        if (materialName != null) {
            strings = Arrays.copyOf(strings, strings.length + 1);
            strings[strings.length - 1] = "ma.name = '" + materialName + "'";
        }
        strings = Arrays.copyOf(strings, strings.length + 1);
        strings[strings.length - 1] = "ep.invoice_date >= '" + start + "' AND ep.invoice_date <= '" + end + "'";
        query += "WHERE " + String.join(" AND ", strings);
        query += "\nGROUP BY ma.id, ma.`name`, ma.unit_price\n";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getSale_DiscountByMonth() {
        String query = "SELECT MONTH(rp.invoice_date), SUM(rp.total_price), SUM(rp.total_discount)\n" +
                "FROM receipt rp\n" +
                "WHERE YEAR(rp.invoice_date) = YEAR(NOW()) AND MONTH(rp.invoice_date) <= MONTH(NOW())\n" +
                "GROUP BY MONTH(rp.invoice_date)\n" +
                "ORDER BY MONTH(rp.invoice_date) ASC ";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getCapitalPriceByMonth() {
        String query = "SELECT MONTH(rp.invoice_date), SUM(pro.capital_price * rd.quantity)\n" +
                "FROM receipt rp JOIN receipt_detail rd ON rp.id = rd.receipt_id\n" +
                "\t\t\t\t\t\t\t\tJOIN product pro ON pro.id = rd.product_id AND pro.size = rd.size\n" +
                "WHERE YEAR(rp.invoice_date) = YEAR(NOW()) AND MONTH(rp.invoice_date) <= MONTH(NOW())\n" +
                "GROUP BY MONTH(rp.invoice_date)\n" +
                "ORDER BY MONTH(rp.invoice_date) ASC";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getSalary_Allowance_Bonus_Deduction_FineByMonth() {
        String query = "SELECT pr.`id`, pr.`month`, pr.paid, SUM(pd.allowance_amount), SUM(pd.bonus_amount), SUM(pd.deduction_amount), SUM(pd.fine_amount)\n" +
                "FROM payroll pr JOIN payroll_detail pd ON pr.id = pd.payroll_id\n" +
                "WHERE pr.`year` = YEAR(NOW()) AND pr.`month` <= MONTH(NOW())\n" +
                "GROUP BY pr.`id`, pr.`month`, pr.paid\n" +
                "ORDER BY pr.`month` ASC ";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<List<String>> getExpenseIncomeData(Date date) {
        List<List<String>> result = new ArrayList<>();
        String query = "SELECT CONCAT('PN', LPAD(i.id, 6, '0')) AS `Mã chứng từ`, " +
                " nv.name AS `Người tạo`, " +
                " i.received_date AS `Thời gian`, " +
                " CONCAT('-', FORMAT(i.total, 0)) AS `Thu chi`, " +
                " 'Chi tiền trả NCC' AS `Loại` " +
                "FROM import_note i " +
                "JOIN staff nv ON i.staff_id = nv.id " +
                "WHERE DATE(i.received_date) = ? " +
                "UNION ALL " +
                "SELECT CONCAT('HD', LPAD(r.id, 6, '0')) AS `Mã chứng từ`, " +
                " nv.name AS `Người tạo`, " +
                " r.invoice_date AS `Thời gian`, " +
                " FORMAT(r.total, 0) AS `Thu chi`, " +
                " 'Thu tiền khách trả' AS `Loại` " +
                "FROM receipt r " +
                "JOIN staff nv ON r.staff_id = nv.id " +
                "WHERE DATE(r.invoice_date) = ? " +
                "ORDER BY `Thời gian` DESC";

        Connection connection = null;
        try {
            connection = Database.getConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, new java.sql.Date(date.getTime()));
            stmt.setDate(2, new java.sql.Date(date.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    List<String> row = new ArrayList<>();
                    row.add(rs.getString("Mã chứng từ"));
                    row.add(rs.getString("Loại"));
                    row.add(rs.getString("Người tạo"));
                    row.add(rs.getString("Thời gian"));
                    row.add(rs.getString("Thu chi"));
                    result.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    public static List<List<String>> getSale_DiscountByQuarter() {
        String query = "SELECT QUARTER(rp.invoice_date), SUM(rp.total_price), SUM(rp.total_discount)\n" +
                "FROM receipt rp\n" +
                "WHERE YEAR(rp.invoice_date) = YEAR(NOW()) AND MONTH(rp.invoice_date) <= MONTH(NOW())\n" +
                "GROUP BY QUARTER(rp.invoice_date)\n" +
                "ORDER BY QUARTER(rp.invoice_date) ASC ";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getCapitalPriceByQuarter() {
        String query = "SELECT QUARTER(rp.invoice_date), SUM(pro.capital_price * rd.quantity)\n" +
                "FROM receipt rp JOIN receipt_detail rd ON rp.id = rd.receipt_id\n" +
                "\t\t\t\t\t\t\t\tJOIN product pro ON pro.id = rd.product_id AND pro.size = rd.size\n" +
                "WHERE YEAR(rp.invoice_date) = YEAR(NOW()) AND MONTH(rp.invoice_date) <= MONTH(NOW())\n" +
                "GROUP BY QUARTER(rp.invoice_date)\n" +
                "ORDER BY QUARTER(rp.invoice_date) ASC ";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getSalary_Allowance_Bonus_Deduction_FineByQuarter() {
        String query = "SELECT tb2.QUAR, tb2.paid, tb1.allowance, tb1.bonus, tb1.deduction, tb1.fine\n" +
                "FROM (\n" +
                "\t\t\t\tSELECT QUARTER(DATE_FORMAT(CONCAT(pr.`year`, \"-\", pr.`month`, \"-1\"), '%Y-%m-%d')) AS QUAR, SUM(pd.allowance_amount) AS allowance, SUM(pd.bonus_amount) AS bonus, SUM(pd.deduction_amount) AS deduction, SUM(pd.fine_amount) AS fine\n" +
                "\t\t\t\tFROM payroll pr JOIN payroll_detail pd ON pr.id = pd.payroll_id\n" +
                "\t\t\t\tWHERE pr.`year` = YEAR(NOW()) AND pr.`month` <= MONTH(NOW())\n" +
                "\t\t\t\tGROUP BY QUARTER(DATE_FORMAT(CONCAT(pr.`year`, \"-\", pr.`month`, \"-1\"), '%Y-%m-%d'))\n" +
                "\t\t) tb1 JOIN \n" +
                "\t\t(\n" +
                "\t\t\t\tSELECT QUARTER(DATE_FORMAT(CONCAT(pr.`year`, \"-\", pr.`month`, \"-1\"), '%Y-%m-%d')) AS QUAR, SUM(pr.paid) AS paid\n" +
                "\t\t\t\tFROM payroll pr\n" +
                "\t\t\t\tWHERE pr.`year` = YEAR(NOW()) AND pr.`month` <= MONTH(NOW())\n" +
                "\t\t\t\tGROUP BY QUARTER(DATE_FORMAT(CONCAT(pr.`year`, \"-\", pr.`month`, \"-1\"), '%Y-%m-%d'))\n" +
                "\t\t) tb2 ON tb1.QUAR = tb2.QUAR\n" +
                "ORDER BY tb1.QUAR ASC ";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getSale_DiscountByYear() {
        String query = "SELECT YEAR(rp.invoice_date), SUM(rp.total_price), SUM(rp.total_discount)\n" +
                "FROM receipt rp\n" +
                "WHERE rp.invoice_date <= NOW()\n" +
                "GROUP BY YEAR(rp.invoice_date)\n" +
                "ORDER BY YEAR(rp.invoice_date) ASC ";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getCapitalPriceByYear() {
        String query = "SELECT YEAR(rp.invoice_date), SUM(pro.capital_price * rd.quantity)\n" +
                "FROM receipt rp JOIN receipt_detail rd ON rp.id = rd.receipt_id\n" +
                "\t\t\t\t\t\t\t\tJOIN product pro ON pro.id = rd.product_id AND pro.size = rd.size\n" +
                "WHERE rp.invoice_date <= NOW()\n" +
                "GROUP BY YEAR(rp.invoice_date)\n" +
                "ORDER BY YEAR(rp.invoice_date) ASC";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getSalary_Allowance_Bonus_Deduction_FineByYear() {
        String query = "SELECT tb2.yea, tb2.paid, tb1.allowance, tb1.bonus, tb1.deduction, tb1.fine\n" +
                "FROM (\n" +
                "\t\t\t\tSELECT pr.`year` AS yea, SUM(pd.allowance_amount) AS allowance, SUM(pd.bonus_amount) AS bonus, SUM(pd.deduction_amount) AS deduction, SUM(pd.fine_amount) AS fine\n" +
                "\t\t\t\tFROM payroll pr JOIN payroll_detail pd ON pr.id = pd.payroll_id\n" +
                "\t\t\t\tWHERE DATE_FORMAT(CONCAT(pr.`year`, \"-\", pr.`month`, \"-1\"), '%Y-%m-%d') <= NOW() \n" +
                "\t\t\t\tGROUP BY pr.`year`\n" +
                "\t\t) tb1 JOIN \n" +
                "\t\t(\n" +
                "\t\t\t\tSELECT pr.`year` AS yea, SUM(pr.paid) AS paid\n" +
                "\t\t\t\tFROM payroll pr\n" +
                "\t\t\t\tWHERE DATE_FORMAT(CONCAT(pr.`year`, \"-\", pr.`month`, \"-1\"), '%Y-%m-%d') <= NOW() \n" +
                "\t\t\t\tGROUP BY pr.`year`\n" +
                "\t\t) tb2 ON tb1.yea = tb2.yea\n" +
                "ORDER BY tb1.yea ASC";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getTop3BestStaff() {
        String query = "SELECT pd.staff_id, pd.hours_amount\n" +
                "FROM payroll_detail pd JOIN payroll pr ON pd.payroll_id = pr.id\n" +
                "WHERE pr.`year` = YEAR(NOW()) AND pr.`month` = MONTH(NOW())\n" +
                "ORDER BY pd.hours_amount DESC LIMIT 3";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getSaleCategory(String start, String end) {
        String query = "SELECT pro.category, SUM(rd.quantity), SUM(rd.price)\n" +
                "FROM receipt rp JOIN receipt_detail rd ON rp.id = rd.receipt_id\n" +
                "\t\t\t\t\t\t\t\tJOIN product pro ON pro.id = rd.product_id AND pro.size = rd.size\n";
        query += "WHERE '" + start + "' <= DATE(rp.invoice_date) AND DATE(rp.invoice_date) <= '" + end + "'\n";
        query += "GROUP BY pro.category\n";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<List<String>> getSaleProductEndOfDay(String start, String end) {
        String query = "SELECT pro.id, pro.name, SUM(rd.quantity),  ";
        query += "Sum(rd.price) AS GiaTriNiemYet, ";
        query += "SUM(rd.price_discount) AS DoanhThu, ";
        query += " Sum(rd.price) - Sum(rd.price_discount)  AS ChenhLech ";
        query += "FROM receipt_detail rd ";
        query += "JOIN product pro ON pro.id = rd.product_id AND pro.size = rd.size ";
        query += "JOIN receipt rp ON rp.id = rd.receipt_id ";
        query += "WHERE DATE(rp.invoice_date) BETWEEN '" + start + "' AND '" + end + "' ";
        query += "GROUP BY pro.id, pro.name";


        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getReceiptByProductEndOfDay(String productName, String start, String end) {
        String query = "SELECT rp.id, DATE_FORMAT(rp.invoice_date, '%Y-%m-%d %H:%i'), SUM(rd.quantity), SUM(rd.price),Sum(rd.price_discount),Sum(rd.price-rd.price_discount)\n" +
                "FROM receipt rp JOIN receipt_detail rd ON rp.id = rd.receipt_id\n" +
                "\t\t\t\t\t\t\t\tJOIN product pro ON pro.id = rd.product_id AND pro.size = rd.size\n";
        query += "WHERE '" + start + "' <= DATE(rp.invoice_date) AND DATE(rp.invoice_date) <= '" + end + "' AND pro.`name` = '" + productName + "'\n";
        query += "GROUP BY rp.id";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Pair<List<String>, List<List<String>>>> getProductEndOfDay(String start, String end) {
        List<Pair<List<String>, List<List<String>>>> pairList = new ArrayList<>();

        for (List<String> stringListKey : getSaleProductEndOfDay(start, end)) {
            List<List<String>> stringListValue = getReceiptByProductEndOfDay(stringListKey.get(1), start, end);
            pairList.add(new Pair<>(stringListKey, stringListValue));
        }

        return pairList;
    }


    public static List<List<String>> getSaleTime(String start, String end) {
        String query = "SELECT  DATE(rp.invoice_date),Count(*) AS SoLuongDonBan,  ";
        query += "Sum(rp.total_price) AS TongTienHang, ";
        query += "SUM(rp.total_discount) AS GiamGiaHD, ";
        query += "SUM(rp.total) AS DoanhThu ";
        query += "FROM receipt rp ";
        query += "WHERE DATE(rp.invoice_date) BETWEEN '" + start + "' AND '" + end + "' ";
        query += "GROUP BY  DATE(rp.invoice_date)";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getReceiptByDate(String date) {
        String query = "SELECT rp.id, DATE_FORMAT(rp.invoice_date, '%H:%i'), rp.total_price, rp.total_discount, rp.total\n";
        query += "FROM receipt rp \n";
        query += "WHERE DATE('" + date + "') = DATE(rp.invoice_date) \n";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    //
    public static List<Pair<List<String>, List<List<String>>>> getSalesTime(String start, String end) {
        List<Pair<List<String>, List<List<String>>>> pairList = new ArrayList<>();

        for (List<String> stringListKey : getSaleTime(start, end)) {
            List<List<String>> stringListValue = getReceiptByDate(stringListKey.get(0));
            pairList.add(new Pair<>(stringListKey, stringListValue));
        }

        return pairList;
    }


    public static List<Pair<List<String>, List<List<String>>>> getSalesProfit(String start, String end) {
        List<Pair<List<String>, List<List<String>>>> pairList = new ArrayList<>();

        for (List<String> stringListKey : getProfitInvoice(start, end)) {
            List<List<String>> stringListValue = getProfitPerInvoice(stringListKey.get(0));
            pairList.add(new Pair<>(stringListKey, stringListValue));
        }

        return pairList;
    }

    public static List<List<String>> getProfitPerInvoice(String date) {
        String query = "SELECT rp.id,DATE_FORMAT(rp.invoice_date, '%H:%i'), ";
        query += "SUM(rd.price) AS TongTienHang, ";
        query += "SUM(rd.price) - SUM(rd.price_discount) AS GiamGiaHD, ";
        query += "SUM(rd.price_discount) AS DoanhThu, ";
        query += "SUM(rd.quantity * pro.capital_price) As TongGiaVon, ";
        query += "SUM(rd.price_discount) - SUM(rd.quantity * pro.capital_price) AS LoiNhuan ";
        query += "FROM receipt rp JOIN receipt_detail rd on rp.id = rd.receipt_id JOIN product pro on pro.id = rd.product_id and pro.size = rd.size \n";
        query += "WHERE DATE('" + date + "') = DATE(rp.invoice_date) \n";
        query += "GROUP BY rp.id,DATE(rp.invoice_date)";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getProfitInvoice(String start, String end) {
        String query = "SELECT DATE(rp.invoice_date), ";
        query += "SUM(rd.price) AS TongTienHang, ";
        query += "SUM(rd.price) - SUM(rd.price_discount) AS GiamGiaHD, ";
        query += "SUM(rd.price_discount) AS DoanhThu, ";
        query += "SUM(rd.quantity * pro.capital_price) As TongGiaVon, ";
        query += "SUM(rd.price_discount) - SUM(rd.quantity * pro.capital_price) AS LoiNhuan ";
        query += "FROM receipt rp JOIN receipt_detail rd on rp.id = rd.receipt_id JOIN product pro on pro.id = rd.product_id and pro.size = rd.size ";
        query += "WHERE DATE(rp.invoice_date) BETWEEN '" + start + "' AND '" + end + "' ";
        query += "GROUP BY DATE(rp.invoice_date)";

        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getInvoiceDiscount(String start, String end) {
        String query = "SELECT rp.id, DATE(rp.invoice_date), ";
        query += "rp.total_price AS TongTienHang, ";
        query += "rp.total_discount AS GiamGiaHD, ";
        query += "CASE WHEN d.type = 0 THEN 'Giảm theo sản phẩm' ELSE 'Giảm theo hóa đơn' END AS LoaiGiamGia ";
        query += "FROM receipt rp ";
        query += "LEFT JOIN discount d ON rp.discount_id = d.id ";
        query += "WHERE DATE(rp.invoice_date) BETWEEN '" + start + "' AND '" + end + "' ";
        query += "AND rp.discount_id <> 0 ";
        query += "AND rp.total_discount <> 0";

        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        for (Pair<List<String>, List<List<String>>> pair : getSalesProfit("2024-05-01", "2024-05-02")) {
            System.out.println(Arrays.toString(pair.getKey().toArray()));
            System.out.println(Arrays.toString(pair.getValue().toArray()));
        }
//        String start = "2024-01-01";
//        String end = "2024-12-31";
//        List<List<String>> saleCategories = getSaleCategory(start, end);
//
//        // In kết quả ra terminal
//        printSaleCategory(saleCategories);
    }

    public static List<Pair<List<String>, List<List<String>>>> getSalesEndOFDay(String start, String end) {
        List<Pair<List<String>, List<List<String>>>> pairList = new ArrayList<>();

        List<List<String>> quantityLists = getSalesStatisticsQuantity(start, end);
        for (List<String> stringListKey : getSalesStatistics(start, end)) {
            List<List<String>> stringListValue = getSalesDay(stringListKey.get(0));
            List<String> stringListKeyQuantity = new ArrayList<>();
            stringListKeyQuantity.add(stringListKey.get(0));
            for (List<String> stringListQuantity : quantityLists) {
                if (stringListKey.get(0).equals(stringListQuantity.get(0))) {
                    stringListKeyQuantity.add(stringListQuantity.get(1));
                    break;
                }
            }
            stringListKeyQuantity.add(stringListKey.get(1));
            stringListKeyQuantity.add(stringListKey.get(2));
            stringListKeyQuantity.add(stringListKey.get(3));
            pairList.add(new Pair<>(stringListKeyQuantity, stringListValue));
        }

        return pairList;
    }

    //    public static List<List<String>> getSalesStatistics(String start, String end) {
//        String query = "SELECT DATE(rp.invoice_date), SUM(rd.quantity), SUM(rd.price), SUM(rd.price_discount), SUM(rd.price) - SUM(rd.price_discount) ";
//        query += "FROM receipt rp ";
//        query += "INNER JOIN receipt_detail rd ON rp.id = rd.receipt_id ";
//        query += "WHERE DATE(rp.invoice_date) BETWEEN '" + start + "' AND '" + end + "' ";
//        query += "GROUP BY DATE(rp.invoice_date) ";
//
//
//        try {
//            return executeQueryStatistic(query);
//        } catch (SQLException | IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
    public static List<List<String>> getSalesStatistics(String start, String end) {
        String query = "SELECT DATE(rp.invoice_date), SUM(rp.total_price), SUM(rp.total),SUM(rp.total_discount) ";
        query += "FROM receipt rp ";
        query += "WHERE DATE(rp.invoice_date) BETWEEN '" + start + "' AND '" + end + "' ";
        query += "GROUP BY DATE(rp.invoice_date)";
        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getSalesStatisticsQuantity(String start, String end) {
        String query = "SELECT DATE(rp.invoice_date), SUM(rd.quantity) ";
        query += "FROM receipt rp ";
        query += "INNER JOIN receipt_detail rd ON rp.id = rd.receipt_id ";
        query += "WHERE DATE(rp.invoice_date) BETWEEN '" + start + "' AND '" + end + "' ";
        query += "GROUP BY DATE(rp.invoice_date)";


        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getSalesDay(String date) {
        String query = "SELECT rp.id, DATE_FORMAT(rp.invoice_date, '%H:%i'), s.name, SUM(rd.quantity),rp.total_price, rp.total,rp.total_discount ";
        query += "FROM receipt rp ";
        query += "INNER JOIN receipt_detail rd ON rp.id = rd.receipt_id ";
        query += "INNER JOIN staff s ON rp.staff_id = s.id ";
        query += "WHERE DATE(rp.invoice_date) = DATE('" + date + "') ";
        query += "GROUP BY rp.id, s.name, DATE(rp.invoice_date),rp.total_price, rp.total,rp.total_discount";

        try {
            return executeQueryStatistic(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printSaleCategory(List<List<String>> saleCategories) {
        System.out.println("Category\tQuantity\tTotal Price");
        for (List<String> category : saleCategories) {
            System.out.printf("%s\t%s\t%s\n", category.get(0), category.get(1), category.get(2));
        }
    }


}

