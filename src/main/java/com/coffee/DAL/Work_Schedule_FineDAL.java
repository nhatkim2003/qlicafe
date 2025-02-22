package com.coffee.DAL;

import com.coffee.DTO.Work_Schedule_Fine;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Work_Schedule_FineDAL extends Manager {
    public Work_Schedule_FineDAL() {
        super("work_schedule_fine",
                List.of("work_schedule_id",
                        "fine_name",
                        "fine_amount",
                        "quantity",
                        "fine_total"
                ));
    }

    public List<Work_Schedule_Fine> convertToWork_Schedule_Fines(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Work_Schedule_Fine(
                        Integer.parseInt(row.get(0)), // work_schedule_fine_id
                        row.get(1),
                        Double.parseDouble(row.get(2)),
                        Integer.parseInt(row.get(3)),
                        Double.parseDouble(row.get(4))// name
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Work_Schedule_FineDAL.convertToWork_Schedule_Fines(): " + e.getMessage());
            }
            return new Work_Schedule_Fine();
        });
    }

    public int addWork_Schedule_Fine(Work_Schedule_Fine work_schedule_fine) {
        try {
            return create(work_schedule_fine.getWork_schedule_id(),
                    work_schedule_fine.getFine_name(),
                    work_schedule_fine.getFine_amount(),
                    work_schedule_fine.getQuantity(),
                    work_schedule_fine.getFine_total()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Work_Schedule_FineDAL.addWork_Schedule_Fine(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteWork_Schedule_Fine(String... conditions) {
        try {
            return delete(conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Work_Schedule_FineDAL.deleteWork_Schedule_Fine(): " + e.getMessage());
        }
        return 0;
    }

    public List<Work_Schedule_Fine> searchWork_Schedule_Fines(String... conditions) {
        try {
            return convertToWork_Schedule_Fines(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Work_Schedule_FineDAL.searchWork_Schedule_Fines(): " + e.getMessage());
        }
        return new ArrayList<>();
    }


}



