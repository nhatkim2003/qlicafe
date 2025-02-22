package com.coffee.BLL;

import com.coffee.DAL.ProductDAL;
import com.coffee.DTO.Product;
import com.coffee.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductBLL extends Manager<Product> {
    private ProductDAL productDAL;

    public ProductBLL() {
        productDAL = new ProductDAL();
    }

    public ProductDAL getProductDAL() {
        return productDAL;
    }

    public void setProductDAL(ProductDAL productDAL) {
        this.productDAL = productDAL;
    }

    public Object[][] getData() {
        return getData(productDAL.searchProducts("deleted = 0"));
    }

    public Pair<Boolean, String> addProduct(Product product) {
//        Pair<Boolean, String> result;
//
////        result = validateProductAll(product);
////        if(!result.getKey()){
////            return new Pair<>(false,result.getValue());
////        }

        if (productDAL.addProduct(product) == 0)
            return new Pair<>(false, "Thêm sản phẩm không thành công.");

        return new Pair<>(true, "Thêm sản phẩm thành công.");
    }

    public Pair<Boolean, String> updateProduct(Product product) {

        if (productDAL.updateProduct(product) == 0)
            return new Pair<>(false, "Cập nhật sản phẩm không thành công.");

        return new Pair<>(true, "Cập nhật sản phẩm thành công.");
    }

    public Pair<Boolean, String> deleteProduct(Product product) {
        if (productDAL.deleteProduct("id = " + product.getId(), "size = '" + product.getSize() + "'") == 0)
            return new Pair<>(false, "Xoá sản phẩm không thành công.");

        return new Pair<>(true, "Xoá sản phẩm thành công.");
    }

    public List<Product> searchProducts(String... conditions) {
        return productDAL.searchProducts(conditions);
    }

    public List<Product> findProducts(String key, String value) {
        List<Product> list = new ArrayList<>();
        List<Product> productList = productDAL.searchProducts("deleted = 0");
        for (Product product : productList) {
            if (getValueByKey(product, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(product);
            }
        }
        return list;
    }

    public List<Product> findProductsBy(Map<String, Object> conditions) {
        List<Product> products = productDAL.searchProducts("deleted = 0");
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            products = findObjectsBy(entry.getKey(), entry.getValue(), products);
        return products;
    }

    public Pair<Boolean, String> exists(Product newProduct) {
        List<Product> products = productDAL.searchProducts("id = '" + newProduct.getId() + "'", "size = '" + newProduct.getSize() + "'");
        if (!products.isEmpty()) {
            return new Pair<>(true, "Sản phẩm đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    public Pair<Boolean, String> validateProductAll(Product product) {
        Pair<Boolean, String> result;

        result = validateName(product.getName());
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        result = exists(product);
        if (result.getKey()) {
            return new Pair<>(false, result.getValue());
        }
        result = validateCategory(product.getCategory());
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        result = validatePrice(String.valueOf(product.getPrice()), "Giá bán");
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }
        return new Pair<>(true, "");
    }

    public Pair<Boolean, String> validateName(String name) {
        if (name.isBlank())
            return new Pair<>(false, "Tên sản phẩm không được để trống.");
        if (VNString.containsSpecial(name))
            return new Pair<>(false, "Tên sản phẩm không được chứa ký tự đặc biệt.");
        return new Pair<>(true, "");
    }

    public Pair<Boolean, String> validateCategory(String category) {
        if (category.isBlank())
            return new Pair<>(false, "Thể loại không được để trống.");
        if (VNString.containsSpecial(category))
            return new Pair<>(false, "Thể loại không được chứa ký tự đặc biệt.");
        return new Pair<>(true, "");
    }

    public Pair<Boolean, String> validatePrice(String price, String title) {
        if (price.isBlank())
            return new Pair<>(false, title + " của sản phẩm không được để trống.");
        if (!VNString.checkUnsignedNumber(price))
            return new Pair<>(false, title + " của sản phẩm phải lớn hơn 0.");
        return new Pair<>(true, price);
    }

    @Override
    public Object getValueByKey(Product product, String key) {
        return switch (key) {
            case "id" -> product.getId();
            case "name" -> product.getName();
            case "size" -> product.getSize();
            case "category" -> product.getCategory();
            case "price" -> product.getPrice();
            case "capital_price" -> product.getCapital_price();
            case "image" -> product.getImage();
            default -> null;
        };
    }

    public List<String> getAllName() {
        return productDAL.getAllName();
    }

    public List<String> getCategories() {
        return productDAL.getCategories();
    }

    public List<List<String>> getBestSellers() {
        return productDAL.getBestSellers();
    }

    public List<List<String>> getRemain(int id, String size) {
        return productDAL.getRemain(id, size);
    }

    public static void main(String[] args) {
        ProductBLL productBLL = new ProductBLL();
//        Product product = new Product(productBLL.getAutoID(productBLL.searchProducts()), "xyz", "abc", 50, "d", "aaa", false);
//        productBLL.addProduct(product);
//
//        product.setName("long");
//        productBLL.updateProduct(product);
//        System.out.println(productBLL.getCategories());
    }
}
