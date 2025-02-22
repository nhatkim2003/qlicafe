package com.coffee.BLL;

import com.coffee.DAL.Leave_Of_Absence_FormDAL;
import com.coffee.DTO.Leave_Of_Absence_Form;
import com.coffee.DTO.Payroll;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Leave_Of_Absence_FormBLL extends Manager<Leave_Of_Absence_Form> {
    private Leave_Of_Absence_FormDAL leave_Of_Absence_FormDAL;

    public Leave_Of_Absence_FormBLL() {
        leave_Of_Absence_FormDAL = new Leave_Of_Absence_FormDAL();
    }

    public Leave_Of_Absence_FormDAL getLeave_Of_Absence_FormDAL() {
        return leave_Of_Absence_FormDAL;
    }

    public void setLeave_Of_Absence_FormDAL(Leave_Of_Absence_FormDAL leave_Of_Absence_FormDAL) {
        this.leave_Of_Absence_FormDAL = leave_Of_Absence_FormDAL;
    }

    public Object[][] getData() {
        return getData(leave_Of_Absence_FormDAL.searchLeave_Of_Absence_Forms());
    }

    public Pair<Boolean, String> addLeave_Of_Absence_Form(Leave_Of_Absence_Form leave_Of_Absence_Form) {
        Pair<Boolean, String> result;

        result = exists(leave_Of_Absence_Form);
        if (result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        if (leave_Of_Absence_FormDAL.addLeave_Of_Absence_Form(leave_Of_Absence_Form) == 0)
            return new Pair<>(false, "Thêm đơn nghỉ phép không thành công.");

        return new Pair<>(true, "Thêm đơn nghỉ phép thành công.");
    }

    public Pair<Boolean, String> updateLeave_Of_Absence_Form(Leave_Of_Absence_Form leave_Of_Absence_Form) {
        Pair<Boolean, String> result;

//        result = validateDate(leave_Of_Absence_Form.getStart_date(), leave_Of_Absence_Form.getEnd_date());
//        if (!result.getKey()) {
//            return new Pair<>(false, result.getValue());
//        }

        if (leave_Of_Absence_FormDAL.updateLeave_Of_Absence_Form(leave_Of_Absence_Form) == 0)
            return new Pair<>(false, "Cập nhật đơn nghỉ phép không thành công.");

        return new Pair<>(true, "Cập nhật đơn nghỉ phép thành công.");

    }

    public List<Leave_Of_Absence_Form> searchLeave_Of_Absence_Forms(String... conditions) {
        return leave_Of_Absence_FormDAL.searchLeave_Of_Absence_Forms(conditions);
    }

    public List<Leave_Of_Absence_Form> findLeave_Of_Absence_Forms(String key, String value) {
        List<Leave_Of_Absence_Form> list = new ArrayList<>();
        List<Leave_Of_Absence_Form> leave_Of_Absence_FormList = leave_Of_Absence_FormDAL.searchLeave_Of_Absence_Forms("id != 0");
        for (Leave_Of_Absence_Form leave_Of_Absence_Form : leave_Of_Absence_FormList) {
            if (getValueByKey(leave_Of_Absence_Form, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(leave_Of_Absence_Form);
            }
        }
        return list;
    }

    public List<Leave_Of_Absence_Form> findLeave_Of_Absence_FormsBy(Map<String, Object> conditions) {
        List<Leave_Of_Absence_Form> leave_Of_Absence_Forms = leave_Of_Absence_FormDAL.searchLeave_Of_Absence_Forms("id != 0");
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            leave_Of_Absence_Forms = findObjectsBy(entry.getKey(), entry.getValue(), leave_Of_Absence_Forms);
        return leave_Of_Absence_Forms;
    }

    private static Pair<Boolean, String> validateDate(Date start_date, Date end_date) {
        if (start_date == null)
            return new Pair<>(false, "Ngày bắt đầu không được để trống.");
        if (end_date == null)
            return new Pair<>(false, "Ngày kết thúc không được để trống.");
        if (start_date.before(java.sql.Date.valueOf(LocalDate.now())))
            return new Pair<>(false, "Ngày bắt đầu phải sau ngày hiện tại.");
        if (start_date.after(end_date))
            return new Pair<>(false, "Ngày bắt đầu phải trước ngày kết thúc.");
        return new Pair<>(true, "Thời gian nghỉ phép hợp lệ.");
    }

    public Pair<Boolean, String> exists(Leave_Of_Absence_Form leaveOfAbsenceForm) {
        List<Leave_Of_Absence_Form> payrolls = searchLeave_Of_Absence_Forms("staff_id = " + leaveOfAbsenceForm.getStaff_id(), "date_off = '" + leaveOfAbsenceForm.getDate_off() + "'");
        if (!payrolls.isEmpty()) {
            return new Pair<>(true, "Đã tồn tại đơn nghỉ phép cho ngày này.");
        }
        return new Pair<>(false, "");
    }

    @Override
    public Object getValueByKey(Leave_Of_Absence_Form leave_Of_Absence_Form, String key) {
        return switch (key) {
            case "id" -> leave_Of_Absence_Form.getId();
            case "staff_id" -> leave_Of_Absence_Form.getStaff_id();
            case "date" -> leave_Of_Absence_Form.getDate();
            case "date_off" -> leave_Of_Absence_Form.getDate_off();
            case "shifts" -> leave_Of_Absence_Form.getShifts();
            case "reason" -> leave_Of_Absence_Form.getReason();
            case "status" -> leave_Of_Absence_Form.getStatus();
            default -> null;
        };
    }

    public static void main(String[] args) {
        Leave_Of_Absence_FormBLL leave_Of_Absence_FormBLL = new Leave_Of_Absence_FormBLL();
//        Leave_Of_Absence_Form leave_Of_Absence_Form = new Leave_Of_Absence_Form(leave_Of_Absence_FormBLL.getAutoID(leave_Of_Absence_FormBLL.searchLeave_Of_Absence_Forms()), java.sql.Date.valueOf("2024-03-10"), java.sql.Date.valueOf("2024-05-10"), false);
//        leave_Of_Absence_FormBLL.addLeave_Of_Absence_Form(leave_Of_Absence_Form);
//        leave_Of_Absence_Form.setStatus(true);
//        leave_Of_Absence_FormBLL.updateLeave_Of_Absence_Form(leave_Of_Absence_Form);
        System.out.println(leave_Of_Absence_FormBLL.searchLeave_Of_Absence_Forms("id != 0"));
    }
}
