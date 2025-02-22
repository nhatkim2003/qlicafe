package com.coffee.ImportExcel;

import com.coffee.BLL.MaterialBLL;
import com.coffee.DTO.Material;
import javafx.util.Pair;
import org.apache.logging.log4j.core.appender.ScriptAppenderSelector;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class AddMaterialFromExcel {
    MaterialBLL materialBLL = new MaterialBLL();

    public Pair<Boolean, String> addMaterialFromExcel(File file) throws IOException {
        List<Material> materials;
        StringBuilder errorAll = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet1 = workbook.getSheetAt(0);

            Pair<List<Material>, String> materialsResult = readMaterialFromExcel(sheet1);
            materials = materialsResult.getKey();
            errorAll.append(materialsResult.getValue());


            // Thêm dữ liệu vào cơ sở dữ liệu nếu không có lỗi
            if (errorAll.isEmpty()) {
                addToDatabase(materials);
            } else {
                return new Pair<>(false, errorAll.toString());
            }

        } catch (IOException e) {
            return new Pair<>(false, "Lỗi khi đọc file Excel.");
        }
        return new Pair<>(true, "Thêm nguyên liệu thành công");

    }


    private Pair<List<Material>, String> readMaterialFromExcel(Sheet sheet) {
        List<Material> materials = new ArrayList<>();
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
                Material material = readMaterialRowData(currentRow);

                validateAttributeSupplier(material, errorRow);
                if (errorRow.isEmpty()) {
                    materials.add(material);
                } else {
                    errorAll.append(" - Dòng").append(rowNum).append(":\n").append(errorRow).append("\n");
                }
            }
        }
        if (errorAll.isEmpty()) {
            return new Pair<>(materials, "");
        }
        return new Pair<>(materials, errorAll.toString());
    }

    private Material readMaterialRowData(Row currentRow) {
        Material material = new Material();
        for (Cell cell : currentRow) {
            int columnIndex = cell.getColumnIndex() + 1;

            switch (columnIndex) {
                case 1: // Name
                    if (cell.getCellType() == CellType.STRING) {
                        material.setName(cell.getStringCellValue());
                    }
                    break;
                case 2: // Tồn tối thiểu
                    if (cell.getCellType() == CellType.NUMERIC) {
                        double min_remain = cell.getNumericCellValue();
                        material.setMinRemain(min_remain);
                    }
                    break;
                case 3: //Tồn tối đa
                    if (cell.getCellType() == CellType.NUMERIC) {
                        double max_remain = cell.getNumericCellValue();
                        material.setMaxRemain(max_remain);
                    }
                    break;

                case 4: // Đơn vị
                    if (cell.getCellType() == CellType.STRING) {
                        String unit = cell.getStringCellValue();
                        material.setUnit(unit);
                    }
                    break;
                case 5: //Giá vốn
                    if (cell.getCellType() == CellType.NUMERIC) {
                        double unit_price = cell.getNumericCellValue();
                       material.setUnit_price(unit_price);
                    }
                    break;
                case 6:// hình thức bán
                    if (cell.getCellType() == CellType.NUMERIC) {
                        System.out.println("Kiểu số ++===================");
                        int sell = (int) cell.getNumericCellValue();
                        System.out.println(sell+" ===========================");
                        if(sell == 0)
                            material.setSell(false);
                        else if(sell == 1)
                            material.setSell(true);
                        else
                            material.setSell(null);
                    }
                    break;
                default:
                    break;
            }
        }

        return material;
    }

    private void validateAttributeSupplier(Material material, StringBuilder errorRow) {
        // nếu ô trong excel để trống hoặc không phải là kiểu chuỗi thì sẽ thông báo này
        if (material.getName() == null) {
            errorRow.append("Tên nguyên liệu bắt buộc phải là kiểu chuỗi và không được để trống .\n");
        }
        if (material.getMinRemain() == null) {
            errorRow.append("Tồn tối thiểu bắt buộc phải là kiểu số và không được để trống\n");
        }

        if (material.getMaxRemain() == null) {
            errorRow.append("Tồn tối đa bắt buộc phải là kiểu số và không được để trống.\n");
        }

        if (material.getUnit() == null) {
            errorRow.append("Đơn vị bắt buộc phải là kiểu chuỗi và không được để trống .\n");
        }else{
            String unit = material.getUnit();
            if(!(unit.equals("kg")|| unit.equals("lít")||unit.equals("cái"))){
                errorRow.append("Đơn vị phải là kg, lít hoặc cái\n");
            }
        }
        if (material.getUnit_price() == null) {
            errorRow.append("Giá vốn bắt buộc phải là kiểu số và không được để trống\n");
        }
        if (material.isSell() == null) {
            errorRow.append("Hình thức bán phải là giá trị 0 hoặc 1\n");
        }
        if (errorRow.isEmpty()) {
            Pair<Boolean, String> result = materialBLL.validateAll(material);
            if (!result.getKey())
                errorRow.append(result.getValue()).append("\n");
        }
    }

    private void addToDatabase(List<Material> materials) {
        int material_id = materialBLL.getAutoID(materialBLL.searchMaterials());
        for (Material material : materials) {
            material.setId(material_id);
            material.setRemain(0);
            materialBLL.addMaterial(material);
            material_id += 1;
        }

    }
}
