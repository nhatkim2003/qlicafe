package com.coffee.DAL;

import com.coffee.DTO.Work_Schedule_Bonus;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Work_Schedule_BonusDAL extends Manager {
    public Work_Schedule_BonusDAL() {
        super("work_schedule_bonus",
                List.of("work_schedule_id",
                        "bonus_name",
                        "bonus_amount",
                        "quantity",
                        "bonus_total"
                ));
    }

    public List<Work_Schedule_Bonus> convertToWork_Schedule_Bonuss(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Work_Schedule_Bonus(
                        Integer.parseInt(row.get(0)), // work_schedule_bonus_id
                        row.get(1),
                        Double.parseDouble(row.get(2)),
                        Integer.parseInt(row.get(3)),
                        Double.parseDouble(row.get(4))// name
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Work_Schedule_BonusDAL.convertToWork_Schedule_Bonuss(): " + e.getMessage());
            }
            return new Work_Schedule_Bonus();
        });
    }

    public int addWork_Schedule_Bonus(Work_Schedule_Bonus work_schedule_bonus) {
        try {
            return create(work_schedule_bonus.getWork_schedule_id(),
                    work_schedule_bonus.getBonus_name(),
                    work_schedule_bonus.getBonus_amount(),
                    work_schedule_bonus.getQuantity(),
                    work_schedule_bonus.getBonus_total()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Work_Schedule_BonusDAL.addWork_Schedule_Bonus(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteWork_Schedule_Bonus(String... conditions) {
        try {
            return delete(conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Work_Schedule_BonusDAL.deleteWork_Schedule_Bonus(): " + e.getMessage());
        }
        return 0;
    }

    public List<Work_Schedule_Bonus> searchWork_Schedule_Bonuss(String... conditions) {
        try {
            return convertToWork_Schedule_Bonuss(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Work_Schedule_BonusDAL.searchWork_Schedule_Bonuss(): " + e.getMessage());
        }
        return new ArrayList<>();
    }


}



