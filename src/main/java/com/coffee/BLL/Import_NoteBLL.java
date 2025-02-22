package com.coffee.BLL;

import com.coffee.DAL.Import_NoteDAL;
import com.coffee.DTO.Import_Note;
import com.coffee.DTO.Staff;
import com.coffee.utils.VNString;
import javafx.util.Pair;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Import_NoteBLL extends Manager<Import_Note> {
    private Import_NoteDAL importNoteDAL;

    public Import_NoteBLL() {
        importNoteDAL = new Import_NoteDAL();
    }

    public Import_NoteDAL getImport_NoteDAL() {
        return importNoteDAL;
    }

    public void setImport_NoteDAL(Import_NoteDAL importNoteDAL) {
        this.importNoteDAL = importNoteDAL;
    }

    public Object[][] getData() {
        return getData(importNoteDAL.searchImport_Note());
    }

    public Pair<Boolean, String> addImport(Import_Note import_Note) {

        if (importNoteDAL.addImport_Note(import_Note) == 0)
            return new Pair<>(false, "Thêm phiếu nhập không thành công.");


        return new Pair<>(true, "Thêm phiếu nhập thành công.");
    }

    public List<Import_Note> searchImport(String... conditions) {
        return importNoteDAL.searchImport_Note(conditions);
    }

    public List<Import_Note> findImport(String key, String value) {
        List<Import_Note> list = new ArrayList<>();
        List<Import_Note> importList = importNoteDAL.searchImport_Note();
        for (Import_Note import_Note : importList) {
            if (getValueByKey(import_Note, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(import_Note);
            }
        }
        return list;
    }

    public List<Import_Note> findImportBy(Map<String, Object> conditions) {
        List<Import_Note> imports = importNoteDAL.searchImport_Note();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            imports = findObjectsBy(entry.getKey(), entry.getValue(), imports);
        return imports;
    }

    @Override
    public Object getValueByKey(Import_Note imports, String key) {
        return switch (key) {
            case "id" -> imports.getId();
            case "staff_id" -> imports.getStaff_id();
            case "total" -> imports.getTotal();
            case "received_date" -> imports.getReceived_date();
            default -> null;
        };
    }

    public static void main(String[] args) {
//        Import_NoteBLL importNoteBLL = new Import_NoteBLL();
//        Import_Note importNote = new Import_Note(importNoteBLL.getAutoID(importNoteBLL.searchImport()), 1, 0, Date.valueOf("2024-02-07"));
//        importNoteBLL.addImport(importNote);
    }
}
