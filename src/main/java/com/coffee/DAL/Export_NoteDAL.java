package com.coffee.DAL;

import com.coffee.DTO.Export_Note;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Export_NoteDAL extends Manager {
    public Export_NoteDAL() {
        super("export_note",
                List.of("id",
                        "staff_id",
                        "total",
                        "invoice_date"));
    }

    public List<Export_Note> convertToExport_Note(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Export_Note(
                        Integer.parseInt(row.get(0)),
                        Integer.parseInt(row.get(1)),
                        BigDecimal.valueOf(Double.parseDouble(row.get(2))),
                        Date.valueOf(row.get(3))
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Export_NoteDAL.convertToExport_Note(): " + e.getMessage());
            }
            return new Export_Note();
        });
    }

    public int addExport_Note(Export_Note export) {
        try {
            return create(export.getId(),
                    export.getStaff_id(),
                    export.getTotal(),
                    export.getInvoice_date()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Export_NoteDAL.addExport_Note(): " + e.getMessage());
        }
        return 0;
    }

    public List<Export_Note> searchExport_Note(String... conditions) {
        try {
            return convertToExport_Note(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Export_NoteDAL.searchExport_Note(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

}
