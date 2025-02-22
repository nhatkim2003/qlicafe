package com.coffee.BLL;

import com.coffee.DAL.Work_Schedule_BonusDAL;
import com.coffee.DTO.Work_Schedule;
import com.coffee.DTO.Work_Schedule_Bonus;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Work_Schedule_BonusBLL extends Manager<Work_Schedule_Bonus> {
    private Work_Schedule_BonusDAL work_schedule_bonusDAL;

    public Work_Schedule_BonusBLL() {
        work_schedule_bonusDAL = new Work_Schedule_BonusDAL();
    }

    public Work_Schedule_BonusDAL getWork_scheduleDAL() {
        return work_schedule_bonusDAL;
    }

    public void setWork_scheduleDAL(Work_Schedule_BonusDAL work_schedule_bonusDAL) {
        this.work_schedule_bonusDAL = work_schedule_bonusDAL;
    }

    public Object[][] getData() {
        return getData(work_schedule_bonusDAL.searchWork_Schedule_Bonuss());
    }

    public Pair<Boolean, String> addWork_schedule(List<Work_Schedule_Bonus> work_schedule_bonuss) {
        Pair<Boolean, String> result;
        for (Work_Schedule_Bonus work_schedule_bonus : work_schedule_bonuss) {
            result = exists(work_schedule_bonus);
            if (result.getKey()) {
                return new Pair<>(false, result.getValue());
            }
            if (work_schedule_bonusDAL.addWork_Schedule_Bonus(work_schedule_bonus) == 0)
                return new Pair<>(false, "Thêm tiền thưởng vào lịch làm việc không thành công.");
        }
        return new Pair<>(true, "Thêm tiền thưởng vào lịch làm việc thành công.");
    }

    public Pair<Boolean, String> deleteWork_schedule(Work_Schedule_Bonus work_schedule_bonus) {
        if (work_schedule_bonusDAL.deleteWork_Schedule_Bonus("work_schedule_id = " + work_schedule_bonus.getWork_schedule_id(), "bonus_name = '" + work_schedule_bonus.getBonus_name() + "'") == 0)
            return new Pair<>(false, "Xoá tiền thưởng khỏi lịch không thành công.");

        return new Pair<>(true, "Xoá tiền thưởng khỏi lịch thành công.");
    }

    public Pair<Boolean, String> deleteAllWork_schedule_bonus(Work_Schedule workSchedule) {
        if (work_schedule_bonusDAL.deleteWork_Schedule_Bonus("work_schedule_id = " + workSchedule.getId()) == 0)
            return new Pair<>(false, "Xoá tiền thưởng khỏi lịch không thành công.");

        return new Pair<>(true, "Xoá tiền thưởng khỏi lịch thành công.");
    }

    public List<Work_Schedule_Bonus> searchWork_schedules(String... conditions) {
        return work_schedule_bonusDAL.searchWork_Schedule_Bonuss(conditions);
    }

    public List<Work_Schedule_Bonus> findWork_schedules(String key, String value) {
        List<Work_Schedule_Bonus> list = new ArrayList<>();
        List<Work_Schedule_Bonus> work_schedule_bonusList = work_schedule_bonusDAL.searchWork_Schedule_Bonuss();
        for (Work_Schedule_Bonus work_schedule_bonus : work_schedule_bonusList) {
            if (getValueByKey(work_schedule_bonus, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(work_schedule_bonus);
            }
        }
        return list;
    }

    public List<Work_Schedule_Bonus> findWork_schedulesBy(Map<String, Object> conditions) {
        List<Work_Schedule_Bonus> work_schedule_bonuss = work_schedule_bonusDAL.searchWork_Schedule_Bonuss();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            work_schedule_bonuss = findObjectsBy(entry.getKey(), entry.getValue(), work_schedule_bonuss);
        return work_schedule_bonuss;
    }

    public Pair<Boolean, String> exists(Work_Schedule_Bonus work_schedule_bonus) {
        List<Work_Schedule_Bonus> work_schedule_bonuss = findWork_schedulesBy(Map.of(
                "work_schedule_id", work_schedule_bonus.getWork_schedule_id(),
                "bonus_name", work_schedule_bonus.getBonus_name()
        ));

        if (!work_schedule_bonuss.isEmpty()) {
            return new Pair<>(true, "Lịch làm việc đã được thêm mục tiền thưởng này.");
        }
        return new Pair<>(false, "");
    }

    @Override
    public Object getValueByKey(Work_Schedule_Bonus work_schedule_bonus, String key) {
        return switch (key) {
            case "work_schedule_id" -> work_schedule_bonus.getWork_schedule_id();
            case "bonus_name" -> work_schedule_bonus.getBonus_name();
            case "bonus_amount" -> work_schedule_bonus.getBonus_amount();
            case "quantity" -> work_schedule_bonus.getQuantity();
            case "bonus_total" -> work_schedule_bonus.getBonus_total();
            default -> null;
        };
    }
}
