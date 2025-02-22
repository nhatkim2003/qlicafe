package com.coffee.DAL;

import com.coffee.DTO.Import_Note;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Import_NoteDAL extends Manager {
    public Import_NoteDAL() {
        super("import_note",
                List.of("id",
                        "staff_id",
                        "total",
                        "received_date"));
    }

    public List<Import_Note> convertToImport_Note(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Import_Note(
                        Integer.parseInt(row.get(0)),
                        Integer.parseInt(row.get(1)),
                        BigDecimal.valueOf(Double.parseDouble(row.get(2))),
                        LocalDateTime.parse(row.get(3))
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Import_NoteDAL.convertToImport_Note(): " + e.getMessage());
            }
            return new Import_Note();
        });
    }

    public int addImport_Note(Import_Note importNote) {
        try {
            return create(importNote.getId(),
                    importNote.getStaff_id(),
                    importNote.getTotal(),
                    importNote.getReceived_date()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Import_NoteDAL.addImport_Note(): " + e.getMessage());
        }
        return 0;
    }

    public List<Import_Note> searchImport_Note(String... conditions) {
        try {
            return convertToImport_Note(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Import_NoteDAL.searchImport_Note(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
