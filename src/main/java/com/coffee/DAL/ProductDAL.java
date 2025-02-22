package com.coffee.DAL;

import com.coffee.DTO.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAL extends Manager {
    public ProductDAL() {
        super("product",
                List.of("id",
                        "name",
                        "size",
                        "category",
                        "capital_price",
                        "price",
                        "image",
                        "deleted"));
    }

    public List<Product> convertToProducts(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Product(
                        Integer.parseInt(row.get(0)), // id
                        row.get(1), // name
                        row.get(2), // size
                        row.get(3), // category
                        Double.parseDouble(row.get(4)), //capital_price
                        Double.parseDouble(row.get(5)), //price
                        row.get(6),//image
                        Boolean.parseBoolean(row.get(7)) //deleted
                );
            } catch (Exception e) {
                System.out.println("Error occurred in ProductDAL.convertToProducts(): " + e.getMessage());
            }
            return new Product();
        });
    }

    public int addProduct(Product product) {
        try {
            return create(product.getId(),
                    product.getName(),
                    product.getSize(),
                    product.getCategory(),
                    product.getCapital_price(),
                    product.getPrice(),
                    product.getImage(),
                    false
            ); // product khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ProductDAL.addProduct(): " + e.getMessage());
        }
        return 0;
    }

    public int updateProduct(Product product) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(product.getId());
            updateValues.add(product.getName());
            updateValues.add(product.getSize());
            updateValues.add(product.getCategory());
            updateValues.add(product.getCapital_price());
            updateValues.add(product.getPrice());
            updateValues.add(product.getImage());
            updateValues.add(product.isDeleted());
            return update(updateValues, "id = " + product.getId(), "size = '" + product.getSize() + "'");
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Product.updateProduct(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteProduct(String... conditions) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(true);
            return update(updateValues, conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ProductDAL.deleteProduct(): " + e.getMessage());
        }
        return 0;
    }

    public List<Product> searchProducts(String... conditions) {
        try {
            return convertToProducts(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ProductDAL.searchProducts(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<String> getAllName() {
        List<String> names = new ArrayList<>();
        try {
            List<List<String>> result = executeQuery("SELECT DISTINCT product.`name` FROM `product` WHERE product.`deleted` = 0");
            for (List<String> name : result) {
                names.add(name.get(0));
            }
            return names;
        } catch (SQLException | IOException e) {
            return names;
        }
    }

    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        try {
            List<List<String>> result = executeQuery("SELECT DISTINCT `category` FROM `product` WHERE `deleted` = 0");
            for (List<String> category : result) {
                categories.add(category.get(0));
            }
            return categories;
        } catch (SQLException | IOException e) {
            return categories;
        }
    }

    public List<List<String>> getBestSellers() {
        try {
            return executeQuery("SELECT rd.product_id\n" +
                    "FROM receipt_detail rd JOIN receipt rp\n" +
                    "WHERE YEAR(rp.invoice_date) = YEAR(NOW())\n" +
                    "GROUP BY rd.product_id\n" +
                    "ORDER BY COUNT(rd.receipt_id) DESC LIMIT 10");
        } catch (SQLException | IOException e) {
            return new ArrayList<>();
        }
    }

    public List<List<String>> getRemain(int id, String size) {
        try {
            return executeQuery("SELECT MIN(ma.remain DIV (re.quantity * 0.001)) \n" +
                    "FROM recipe re JOIN material ma ON re.material_id = ma.id\n" +
                    "WHERE re.product_id = " + id + " AND re.size = '" + size + "'");
        } catch (SQLException | IOException e) {
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        ProductDAL s = new ProductDAL();
        System.out.println(s.searchProducts());
    }
}
