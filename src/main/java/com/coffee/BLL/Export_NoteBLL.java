package com.coffee.BLL;

import com.coffee.DAL.Export_NoteDAL;
import com.coffee.DTO.Export_Note;
import javafx.util.Pair;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Export_NoteBLL extends Manager<Export_Note> {
    private Export_NoteDAL exportDAL;

    public Export_NoteBLL() {
        exportDAL = new Export_NoteDAL();
    }

    public Export_NoteDAL getExport_NoteNoteDAL() {
        return exportDAL;
    }

    public void setExport_NoteNoteDAL(Export_NoteDAL exportDAL) {
        this.exportDAL = exportDAL;
    }

    public Object[][] getData() {
        return getData(exportDAL.searchExport_Note());
    }

    public Pair<Boolean, String> addExport_Note(Export_Note export) {

        if (exportDAL.addExport_Note(export) == 0)
            return new Pair<>(false, "Thêm phiếu xuất không thành công.");

        return new Pair<>(true, "Thêm phiếu xuất thành công.");
    }

    public List<Export_Note> searchExport_Note(String... conditions) {
        return exportDAL.searchExport_Note(conditions);
    }

    public List<Export_Note> findExport_Note(String key, String value) {
        List<Export_Note> list = new ArrayList<>();
        List<Export_Note> exportList = exportDAL.searchExport_Note();
        for (Export_Note export : exportList) {
            if (getValueByKey(export, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(export);
            }
        }
        return list;
    }

    public List<Export_Note> findExport_NoteBy(Map<String, Object> conditions) {
        List<Export_Note> exports = exportDAL.searchExport_Note();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            exports = findObjectsBy(entry.getKey(), entry.getValue(), exports);
        return exports;
    }

    @Override
    public Object getValueByKey(Export_Note exports, String key) {
        return switch (key) {
            case "id" -> exports.getId();
            case "staff_id" -> exports.getStaff_id();
            case "total" -> exports.getTotal();
            case "invoice_date" -> exports.getInvoice_date();
            default -> null;
        };
    }

    public static void main(String[] args) {
//        Export_NoteBLL exportNoteBLL = new Export_NoteBLL();
//        Export_Note exportNote = new Export_Note(exportNoteBLL.getAutoID(exportNoteBLL.searchExport_Note()), 1, 0, Date.valueOf("2024-02-07"));
//        exportNoteBLL.addExport_Note(exportNote);
    }
}
