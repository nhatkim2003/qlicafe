package com.coffee.ImportExcel;

import com.coffee.BLL.MaterialBLL;
import com.coffee.BLL.ProductBLL;
import com.coffee.BLL.RecipeBLL;
import com.coffee.DTO.*;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class AddProductFromExcel {
    private final ProductBLL productBLL = new ProductBLL();
    private final RecipeBLL recipeBLL = new RecipeBLL();
    private final MaterialBLL materialBLL = new MaterialBLL();
    public Pair<Boolean, String> AddProductFromExcell(File file) throws IOException {
        List<Product> products;
        List<Recipe> recipes;
        StringBuilder errorAll = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet1 = workbook.getSheetAt(0);
            Sheet sheet2 = workbook.getSheetAt(1);

            Pair<List<Product>, String> productsResult = reeadProductFromExcel(sheet1);
            products = productsResult.getKey();
            errorAll.append(productsResult.getValue());

            Pair<List<Recipe>, String> recipesResult = readRecipeFromExcel(sheet2);
            recipes = recipesResult.getKey();
            errorAll.append(recipesResult.getValue());

            List<Pair<Product, List<Recipe>>> productDatas = new ArrayList<>();
            if (errorAll.isEmpty()) {
                productDatas = processAndValidateData(products, recipes, errorAll);
            }

            // Thêm dữ liệu vào cơ sở dữ liệu nếu không có lỗi
            if (errorAll.isEmpty()) {
                addToDatabase(productDatas);
            } else {
                return new Pair<>(false, errorAll.toString());
            }

        } catch (IOException e) {
            return new Pair<>(false, "Lỗi khi đọc file Excel.");
        }
        return new Pair<>(true, "");

    }

    private List<Pair<Product, List<Recipe>>> processAndValidateData(List<Product> products, List<Recipe> recipes, StringBuilder errorAll) {
        List<Pair<Product, List<Recipe>>> productDatas = new ArrayList<>();
        int id = productBLL.getAutoID(productBLL.searchProducts());

        // Lọc và cập nhật id cho các công thức theo product
        for (Product product : products) {
            List<Recipe> recipes_of_product = new ArrayList<>();

            if (!product.getSize().equals("Không")) {
                for (int i = 0; i < recipes.size(); i++) {
                    if (recipes.get(i).getProduct_id() == product.getId()) {
                        recipes.get(i).setProduct_id(id);
                        recipes_of_product.add(recipes.get(i));
                        recipes.remove(i);
                        i--;
                    }
                }

                // Kiểm tra xem product có nguyên liệu thành phần hay không
                if (recipes_of_product.isEmpty()) {
                    errorAll.append("Mã sản phẩm ").append(product.getId()).append(" chưa có nguyên liệu thành phần.\n");
                }
                else{
                    // nếu có nguyên liệu thành phần thì update giá vốn của sản phẩm lại vì lúc đầu trong file excel ta nhập tạm là 0
                    Double capital_price =  UpdateCapital_Price(recipes_of_product);
                    product.setCapital_price(capital_price);
                }

            }
            product.setId(id);
            id += 1;

            productDatas.add(new Pair<>(product, recipes_of_product));
        }

        // Kiểm tra xem có nguyên liệu thành phần nào mà  không có product_id tương ứng với product_id bên sheet1 không
        if (!recipes.isEmpty()) {
            recipes.forEach(recipe -> errorAll.append("Mã sản phẩm").append(recipe.getProduct_id()).append(" không tồn tại trong sheet sản phẩm .\n"));

        }
        return productDatas;
    }
private Double UpdateCapital_Price( List<Recipe> recipes_of_product){
        double product_price = 0.0;
        for(Recipe recipe : recipes_of_product){
            Material material = materialBLL.searchMaterials("id  ='" +  recipe.getMaterial_id() + "'").get(0);
            double material_price = material.getUnit_price();
            double quantity = recipe.getQuantity();
            double price_temp = material_price*quantity;
            product_price+=price_temp;
        }
        return product_price;
}
    private void addToDatabase(List<Pair<Product, List<Recipe>>> productsData) {
        // Duyệt qua danh sách sản phẩm và công thức của từng sản phẩm để thêm vào database
        for (Pair<Product, List<Recipe>> productData : productsData) {
            Product product = productData.getKey();
            List<Recipe> recipe_of_product = productData.getValue();

            productBLL.addProduct(product);

            if (!recipe_of_product.isEmpty())
                for (Recipe recipe : recipe_of_product)
                    recipeBLL.addRecipe(recipe);

        }
    }

    private Pair<List<Product>, String> reeadProductFromExcel(Sheet sheet) {
        List<Product> products = new ArrayList<>();
        StringBuilder errorAll = new StringBuilder();

        Iterator<Row> iterator = sheet.iterator();

        if (iterator.hasNext()) {
            iterator.next(); // Bỏ qua dòng tiêu đề
        }

        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            if (currentRow.getPhysicalNumberOfCells() > 0) {
                int rowNum = currentRow.getRowNum() + 1; // Số dòng bắt đầu từ 1
                StringBuilder errorRow = new StringBuilder();
                Map<String, Object> rowData = processProductRowData(currentRow);

                validateAttributeProduct(rowData, errorRow);
                if (errorRow.isEmpty()) {
                    Product product = createProduct(rowData);
                    products.add(product);
                } else {
                    if(errorAll.isEmpty())
                        errorAll.append("Lỗi ở sheet Sản phẩm ").append(":\n").append(" - Dòng").append(rowNum).append(":\n").append(errorRow).append("\n");
                    else
                        errorAll.append(" - Dòng").append(rowNum).append(":\n").append(errorRow).append("\n");
                }
            }
        }
        if (errorAll.isEmpty()) {
            return new Pair<>(products, "");
        }
        return new Pair<>(products, errorAll.toString());
    }

    private Map<String, Object> processProductRowData(Row row) {
        Map<String, Object> rowData = new HashMap<>();
        for (Cell cell : row) {
            int columnIndex = cell.getColumnIndex() + 1; // Số cột bắt đầu từ 1

            switch (columnIndex) {
                case 1: // ID
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("id", (int) cell.getNumericCellValue());
                    }
                    break;
                case 2: // Name
                    if (cell.getCellType() == CellType.STRING) {
                        rowData.put("name", cell.getStringCellValue());
                    }
                    break;
                case 3: // Category
                    if (cell.getCellType() == CellType.STRING) {
                        rowData.put("category", cell.getStringCellValue());
                    }
                    break;
                case 4: // Size
                    if (cell.getCellType() == CellType.STRING) {
                        rowData.put("size", cell.getStringCellValue());
                    }
                    break;
                case 5: //Giá vốn
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("capital_price", cell.getNumericCellValue());
                    } else if (cell.getCellType() == CellType.BLANK) {
                        rowData.put("capital_price", 0);
                    }
                    break;
                case 6: //Giá bán
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("price", cell.getNumericCellValue());
                    }
                    break;
                case 7: //Image
                    if (cell.getCellType() == CellType.STRING) {
                        rowData.put("image", cell.getStringCellValue());
                    }
                    break;
                default:
                    break;
            }
        }
        return rowData;
    }

    private void validateAttributeProduct(Map<String, Object> rowData, StringBuilder errorRow) {
        if (!rowData.containsKey("id") || rowData.get("id") == null) {
            errorRow.append("Mã sản phẩm không hợp lệ.\n");
        }

        if (!rowData.containsKey("name") || rowData.get("name") == null || ((String) rowData.get("name")).isEmpty()) {
            errorRow.append("Tên sản phẩm không hợp lệ.\n");
        }
        if (!rowData.containsKey("category") || rowData.get("category") == null || ((String) rowData.get("category")).isEmpty()) {
            errorRow.append("Thể loại không hợp lệ.\n");
        }

        if (!rowData.containsKey("size") || rowData.get("size") == null)
            errorRow.append("Định dạng của cột size phải là kiểu chuỗi\n");
        else {
            String size = (String) rowData.get("size");
            List<String> validSizes = Arrays.asList("S", "M", "L","Không");
            if (!validSizes.contains(size) ) {
                errorRow.append("Size phải là S, M, L hoặc Không .\n");
            }
        }

        if (!rowData.containsKey("price") || rowData.get("price") == null) {
            errorRow.append("Giá bán không hợp lệ.\n");
        }
        if (!rowData.containsKey("image") || rowData.get("image") == null){
            rowData.put("image","productDefault");
        }
        //check xem sản phẩm có có tồn tại không
        if(errorRow.isEmpty()){
            String name = (String) rowData.get("name");
            String size = (String) rowData.get("size");
            List<Product> products = productBLL.searchProducts("name = '" + name + "'", "size = '" + size + "'", "deleted = 0");
            if (!products.isEmpty()) {
                errorRow.append("Sản phẩm đã tồn tại.");
            }
        }

    }

    private Product createProduct(Map<String, Object> rowData) {
        Integer id = (Integer) rowData.get("id");
        String name = (String) rowData.get("name");
        String size = (String) rowData.get("size");
        String category = (String) rowData.get("category");
        Double capital_price = (Double) rowData.get("capital_price");
        Double price = (Double) rowData.get("price");
        String image = (String) rowData.get("image");

        return new Product(id, name, size, category, capital_price, price, image, false);
    }


    private Pair<List<Recipe>, String> readRecipeFromExcel(Sheet sheet) {
        List<Recipe> recipes = new ArrayList<>();
        StringBuilder errorAll = new StringBuilder();

        // Bỏ qua dòng tiêu đề
        Iterator<Row> iterator = sheet.iterator();
        if (iterator.hasNext()) {
            iterator.next();
        }
        int rowNum = 1;
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            rowNum++;

            Map<String, Object> rowData = processRecipeRowData(currentRow);
            StringBuilder errorRow = new StringBuilder();

            validateAttributesRecipe(rowData, errorRow);

            if (errorRow.isEmpty()) {
                Recipe recipe = createRecipe(rowData);
                recipes.add(recipe);
            } else {
                errorAll.append("Lỗi ở Sheet chi tiết giảm giá ").append(":\n Dòng").append(rowNum).append(":\n").append(errorRow).append("\n");
            }
        }

        // kiểm tra xem có nguyện liệu nào được thêm 2 lần với cùng 1 sản phẩm không
        checkForDuplicates(recipes,errorAll);

        return new Pair<>(recipes, errorAll.toString());
    }

    private Map<String, Object> processRecipeRowData(Row row) {
        Map<String, Object> rowData = new HashMap<>();
        for (Cell cell : row) {
            int columnIndex = cell.getColumnIndex() + 1; // Số cột bắt đầu từ 1

            switch (columnIndex) {
                case 1: // product_id
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("product_id", (int) cell.getNumericCellValue());
                    }
                    break;
                case 2: // material_id
                    if (cell.getCellType() == CellType.STRING) {
                        rowData.put("material_name", cell.getStringCellValue());
                    }
                    break;
                case 3: //size
                    if (cell.getCellType() == CellType.STRING) {
                        rowData.put("size", cell.getStringCellValue());
                    }
                    break;
                case 4: // quantity
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("quantity", cell.getNumericCellValue());
                    }
                    break;

                default:
                    break;
            }
        }
        return rowData;
    }

    private void validateAttributesRecipe(Map<String, Object> rowData, StringBuilder errorRow) {
        if (!rowData.containsKey("product_id") || rowData.get("product_id") == null)
            errorRow.append("Mã sản phẩm không hợp lệ.\n");

        if (!rowData.containsKey("material_name") || rowData.get("material_name") == null || ((String) rowData.get("material_name")).isEmpty()) {
            errorRow.append("Tên nguyên liệu không hợp lệ.\n");
        } else {
            String material_name = (String) rowData.get("material_name");

            List<Material> materials = new MaterialBLL().searchMaterials("name  ='" + material_name + "'");
            if (materials.isEmpty()) errorRow.append("Tên nguyên liệu không tồn tại trong hệ thống.\n");
            else { // nếu tìm thấy tên nguyên liệu thì tạo  id và đơn vị của nguyên liệu đó
                rowData.put("material_id", materials.get(0).getId());
                rowData.put("unit", materials.get(0).getUnit());
            }

        }

        if (!rowData.containsKey("size") || rowData.get("size") == null)
            errorRow.append("Định dạng của cột size phải là kiểu chuỗi\n");
        else {
            String size = (String) rowData.get("size");
            List<String> validSizes = Arrays.asList("S", "M", "L");

            if (!validSizes.contains(size)) {
                errorRow.append("Size phải là S, M, L.\n");
            }
        }

        if (!rowData.containsKey("quantity") || rowData.get("quantity") == null || (Double) rowData.get("quantity") < 0)
            errorRow.append("Số lượng không hợp lệ.\n");
    }

    private Recipe createRecipe(Map<String, Object> rowData) {
        Integer product_id = (Integer) rowData.get("product_id");
        Integer material_id = (Integer) rowData.get("material_id");
        Double quantity = (Double) rowData.get("quantity");
        String size = (String) rowData.get("size");
        String unit = (String) rowData.get("unit");

        return new Recipe(product_id, material_id, quantity, size, unit);
    }


    private boolean isDuplicate(Recipe recipe1, Recipe recipe2) {
        // nếu mã sản phẩm và mã size bằng nhau thì check xem nguyên liệu có trùng nhau không
        if (recipe1.getProduct_id() == recipe2.getProduct_id() && recipe1.getSize().equals(recipe2.getSize()))
            return recipe1.getMaterial_id() == recipe2.getMaterial_id();
        return false;
    }

    private void checkForDuplicates(List<Recipe> recipes, StringBuilder errorAll) {
        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe1 = recipes.get(i);
            for (int j = i + 1; j < recipes.size(); j++) {
                Recipe recipe2 = recipes.get(j);
                if (isDuplicate(recipe1, recipe2))
                    if (errorAll.isEmpty())
                        errorAll.append("- Lỗi sheet Công thức \n").append("- Dòng ").append(i + 2).append(" bị trùng nguyên liệu với dòng ").append(j + 2).append("\n");
                    else
                        errorAll.append("- Dòng ").append(i + 2).append(" bị trùng nguyên liệu với dòng ").append(j + 2).append("\n");
            }
        }
    }


}
