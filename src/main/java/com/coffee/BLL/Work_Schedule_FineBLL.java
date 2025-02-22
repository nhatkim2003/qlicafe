package com.coffee.BLL;

import com.coffee.DAL.Work_Schedule_FineDAL;
import com.coffee.DTO.Work_Schedule;
import com.coffee.DTO.Work_Schedule_Fine;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Work_Schedule_FineBLL extends Manager<Work_Schedule_Fine> {
    private Work_Schedule_FineDAL work_schedule_fineDAL;

    public Work_Schedule_FineBLL() {
        work_schedule_fineDAL = new Work_Schedule_FineDAL();
    }

    public Work_Schedule_FineDAL getWork_scheduleDAL() {
        return work_schedule_fineDAL;
    }

    public void setWork_scheduleDAL(Work_Schedule_FineDAL work_schedule_fineDAL) {
        this.work_schedule_fineDAL = work_schedule_fineDAL;
    }

    public Object[][] getData() {
        return getData(work_schedule_fineDAL.searchWork_Schedule_Fines());
    }

    public Pair<Boolean, String> addWork_schedule(List<Work_Schedule_Fine> work_schedule_fines) {
        Pair<Boolean, String> result;
        for (Work_Schedule_Fine work_schedule_fine : work_schedule_fines) {
            result = exists(work_schedule_fine);
            if (result.getKey()) {
                return new Pair<>(false, result.getValue());
            }
            if (work_schedule_fineDAL.addWork_Schedule_Fine(work_schedule_fine) == 0)
                return new Pair<>(false, "Thêm phạt tiền vào lịch làm việc không thành công.");
        }
        return new Pair<>(true, "Thêm phạt tiền vào lịch làm việc thành công.");
    }

    public Pair<Boolean, String> deleteWork_schedule(Work_Schedule_Fine work_schedule_fine) {
        if (work_schedule_fineDAL.deleteWork_Schedule_Fine("work_schedule_id = " + work_schedule_fine.getWork_schedule_id(), "fine_name = '" + work_schedule_fine.getFine_name() + "'") == 0)
            return new Pair<>(false, "Xoá phạt tiền khỏi lịch không thành công.");

        return new Pair<>(true, "Xoá phạt tiền khỏi lịch thành công.");
    }

    public Pair<Boolean, String> deleteAllWork_schedule_fine(Work_Schedule workSchedule) {
        if (work_schedule_fineDAL.deleteWork_Schedule_Fine("work_schedule_id = " + workSchedule.getId()) == 0)
            return new Pair<>(false, "Xoá phạt tiền khỏi lịch không thành công.");

        return new Pair<>(true, "Xoá phạt tiền khỏi lịch thành công.");
    }

    public List<Work_Schedule_Fine> searchWork_schedules(String... conditions) {
        return work_schedule_fineDAL.searchWork_Schedule_Fines(conditions);
    }

    public List<Work_Schedule_Fine> findWork_schedules(String key, String value) {
        List<Work_Schedule_Fine> list = new ArrayList<>();
        List<Work_Schedule_Fine> work_schedule_fineList = work_schedule_fineDAL.searchWork_Schedule_Fines();
        for (Work_Schedule_Fine work_schedule_fine : work_schedule_fineList) {
            if (getValueByKey(work_schedule_fine, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(work_schedule_fine);
            }
        }
        return list;
    }

    public List<Work_Schedule_Fine> findWork_schedulesBy(Map<String, Object> conditions) {
        List<Work_Schedule_Fine> work_schedule_fines = work_schedule_fineDAL.searchWork_Schedule_Fines();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            work_schedule_fines = findObjectsBy(entry.getKey(), entry.getValue(), work_schedule_fines);
        return work_schedule_fines;
    }

    public Pair<Boolean, String> exists(Work_Schedule_Fine work_schedule_fine) {
        List<Work_Schedule_Fine> work_schedule_fines = findWork_schedulesBy(Map.of(
                "work_schedule_id", work_schedule_fine.getWork_schedule_id(),
                "fine_name", work_schedule_fine.getFine_name()
        ));

        if (!work_schedule_fines.isEmpty()) {
            return new Pair<>(true, "Lịch làm việc đã được thêm mục phạt tiền này.");
        }
        return new Pair<>(false, "");
    }

    @Override
    public Object getValueByKey(Work_Schedule_Fine work_schedule_fine, String key) {
        return switch (key) {
            case "work_schedule_id" -> work_schedule_fine.getWork_schedule_id();
            case "fine_name" -> work_schedule_fine.getFine_name();
            case "fine_amount" -> work_schedule_fine.getFine_amount();
            case "quantity" -> work_schedule_fine.getQuantity();
            case "fine_total" -> work_schedule_fine.getFine_total();
            default -> null;
        };
    }
}
