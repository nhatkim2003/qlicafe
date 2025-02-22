package com.coffee.BLL;

import com.coffee.DAL.PayrollDAL;
import com.coffee.DTO.*;
import com.coffee.utils.VNString;
import javafx.util.Pair;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class PayrollBLL extends Manager<Payroll> {
    private final int maxMinutesCheckInLate = 15;
    private final int maxMinutesCheckOutEarly = 15;
    private PayrollDAL payrollDAL;

    public PayrollBLL() {
        payrollDAL = new PayrollDAL();
    }

    public PayrollDAL getPayrollDAL() {
        return payrollDAL;
    }

    public void setPayrollDAL(PayrollDAL payrollDAL) {
        this.payrollDAL = payrollDAL;
    }

    public Object[][] getData() {
        return getData(payrollDAL.searchPayrolls());
    }

    public Pair<Boolean, String> addPayroll(Payroll payroll) {
        Pair<Boolean, String> result;

        result = exists(payroll);
        if (result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        List<Payroll_Detail> payrollDetails = new ArrayList<>();
        double total_salary = 0;

        for (Staff staff : new StaffBLL().searchStaffs("deleted = 0", "id != 1")) {
            List<Work_Schedule> work_scheduleList = new Work_ScheduleBLL().searchWork_schedulesByStaff(staff.getId(), payroll.getYear(), payroll.getMonth());
            if (!work_scheduleList.isEmpty()) {
                // co lich lam viec trong thang
                List<Role_Detail> role_detailList = new Role_DetailBLL().searchRole_detailsByStaff(staff.getId());
                if (role_detailList.isEmpty()) {
                    // nhan vien chua dc thiet lap chuc vu
                    return new Pair<>(false, "Vui lòng thiết lập chức vụ nhân viên " + staff.getName());
                } else {
                    if (staff.getSalary_format_id() == 0) {
                        // nhan vien chua co mau luong
                        return new Pair<>(false, "Vui lòng thiết lập chức mẫu nhân viên " + staff.getName());
                    } else {
                        Role_Detail roleDetail = role_detailList.get(0);
                        Salary_Format salaryFormat = new Salary_FormatBLL().searchSalary_Formats("id  = " + staff.getSalary_format_id()).get(0);
                        List<Salary_Format_Allowance> salaryFormatAllowances = new Salary_Format_AllowanceBLL().searchSalary_Format_Allowances("salary_format_id = " + salaryFormat.getId());
                        List<Salary_Format_Deduction> salaryFormatDeductions = new Salary_Format_DeductionBLL().searchSalary_Format_Deductions("salary_format_id = " + salaryFormat.getId());

                        double hours_amount = 0;
                        double allowance_amount = 0;
                        double deduction_amount = 0;
                        double bonus_amount = 0;
                        double fine_amount = 0;
                        double salary_amount = 0;

                        List<Work_Schedule> ca_nghi_co_phep = new ArrayList<>();
                        List<Work_Schedule> ca_nghi_khong_phep = new ArrayList<>();

                        int lateShifts = 0;
                        int earlyShifts = 0;

                        //System.out.println(staff.getName() + "\n");
                        // tinh ca nghi co phep va khong phep
                        for (Work_Schedule work_schedule : work_scheduleList) {
                            if (work_schedule.getCheck_in().equals("null") && work_schedule.getCheck_out().equals("null")) {
                                if (work_schedule.getNotice().equals("Không")) {
                                    ca_nghi_khong_phep.add(work_schedule);
                                } else {
                                    ca_nghi_co_phep.add(work_schedule);
                                }
                            }
                        }

                        // tinh luong ngay cong
                        if (roleDetail.getType_salary() == 1) {
                            // tinh luong co dinh
                            salary_amount = roleDetail.getSalary();

                            //System.out.println("Luong ngay cong " + VNString.currency(salary_amount));
                        } else {
                            List<Date> dateList = new ArrayList<>();
                            List<Double> hoursList = new ArrayList<>();
                            // tinh luong theo gio
                            for (Work_Schedule work_schedule : work_scheduleList) {
                                if (!work_schedule.getCheck_in().equals("null") && !work_schedule.getCheck_out().equals("null")) {
                                    // ca co di lam
                                    //System.out.println("Ngay :" + work_schedule.getDate() + "\n");

                                    String[] checkinArr, checkoutArr;
                                    double checkin, checkout;

                                    checkinArr = work_schedule.getCheck_in().split(":");
                                    double v = Double.parseDouble(checkinArr[1]) / 60;
                                    checkin = Double.parseDouble(checkinArr[0]) + v;

                                    checkoutArr = work_schedule.getCheck_out().split(":");
                                    v = Double.parseDouble(checkoutArr[1]) / 60;
                                    checkout = Double.parseDouble(checkoutArr[0]) + v;

                                    if (dateList.contains(work_schedule.getDate())) {
                                        int index = dateList.indexOf(work_schedule.getDate());
                                        hoursList.set(index, hoursList.get(index) + Math.abs(checkout - checkin));
                                    } else {
                                        dateList.add(work_schedule.getDate());
                                        hoursList.add(Math.abs(checkout - checkin));
                                    }
                                    //System.out.println("Gio lam :" + Math.abs(checkout - checkin) + "\n");
                                }
                            }
                            for (Double aDouble : hoursList) {
                                if (aDouble > 12)
                                    hours_amount += 12;
                                else
                                    hours_amount += aDouble;
                            }

                            salary_amount = hours_amount * roleDetail.getSalary();

                            //System.out.println("Luong ngay cong " + VNString.currency(salary_amount));
                        }

                        // tinh phu cap
                        for (Salary_Format_Allowance salaryFormatAllowance : salaryFormatAllowances) {
                            Allowance allowance = new AllowanceBLL().searchAllowances("id = " + salaryFormatAllowance.getAllowance_id()).get(0);
                            // tinh phu cap theo ngay
                            if (allowance.getAllowance_type() == 0) {
                                List<Date> dates = new ArrayList<>();
                                for (Work_Schedule work_schedule : work_scheduleList) {
                                    if (!work_schedule.getCheck_in().equals("null") && !work_schedule.getCheck_out().equals("null")) {
                                        if (!dates.contains(work_schedule.getDate()))
                                            dates.add(work_schedule.getDate());
                                    }

                                }
                                allowance_amount = allowance_amount + (dates.size() * allowance.getAllowance_amount());

                                //System.out.println(allowance.getName() + "\n");
                                //System.out.println("So ngay :" + dates.size() + "\n");
                                //System.out.println("Tien phu cap :" + VNString.currency(dates.size() * allowance.getAllowance_amount()) + "\n");
                            }
                            // tinh phu cap theo thang
                            if (allowance.getAllowance_type() == 1) {
                                allowance_amount = allowance_amount + allowance.getAllowance_amount();

                                //System.out.println(allowance.getName() + "\n");
                                //System.out.println("Tien phu cap thang " + VNString.currency(allowance.getAllowance_amount()) + "\n");
                            }
                        }

                        //System.out.println("Tong tien phu cap :" + allowance_amount + "\n");

                        // tinh tien giam tru
                        for (Salary_Format_Deduction salaryFormatDeduction : salaryFormatDeductions) {
                            Deduction deduction = new DeductionBLL().searchDeductions("id = " + salaryFormatDeduction.getDeduction_id()).get(0);
                            // tinh giam tru di muon
                            if (deduction.getDeduction_type() == 0) {
                                for (Work_Schedule work_schedule : work_scheduleList) {
                                    if (!work_schedule.getCheck_in().equals("null") && !work_schedule.getCheck_out().equals("null")) {
                                        int hour = Integer.parseInt(work_schedule.getCheck_in().split(":")[0]);
                                        int minute = Integer.parseInt(work_schedule.getCheck_in().split(":")[1]);
                                        LocalTime checkin = LocalTime.of(hour, minute);
                                        LocalTime timeShiftStart = null;

                                        if (work_schedule.getShift() == 1)
                                            timeShiftStart = LocalTime.of(6, 0);

                                        if (work_schedule.getShift() == 2)
                                            timeShiftStart = LocalTime.of(12, 0);

                                        if (work_schedule.getShift() == 3)
                                            timeShiftStart = LocalTime.of(18, 0);

                                        assert timeShiftStart != null;
                                        long minutesLate = ChronoUnit.MINUTES.between(timeShiftStart, checkin);

                                        if (minutesLate >= maxMinutesCheckInLate) {
                                            //System.out.println("Ngay di muon " + work_schedule.getDate() + "\n");
                                            //System.out.println("Thoi gian di muon " + minutesLate + "\n");
                                            lateShifts += 1;
                                        }
                                    }
                                }

                                deduction_amount = deduction_amount + (lateShifts * deduction.getDeduction_amount());

                                //System.out.println(deduction.getName() + "\n");
                                //System.out.println("So ca di muon " + lateShifts + "\n");
                                //System.out.println("Tien giam tru di muon " + VNString.currency(lateShifts * deduction.getDeduction_amount()) + "\n");
                            }
                            // tinh giam tru ve som
                            if (deduction.getDeduction_type() == 1) {
                                for (Work_Schedule work_schedule : work_scheduleList) {
                                    if (!work_schedule.getCheck_in().equals("null") && !work_schedule.getCheck_out().equals("null")) {
                                        int hour = Integer.parseInt(work_schedule.getCheck_out().split(":")[0]);
                                        int minute = Integer.parseInt(work_schedule.getCheck_out().split(":")[1]);
                                        LocalTime checkout = LocalTime.of(hour, minute);
                                        LocalTime timeShiftEnd = null;

                                        if (work_schedule.getShift() == 1)
                                            timeShiftEnd = LocalTime.of(12, 0);

                                        if (work_schedule.getShift() == 2)
                                            timeShiftEnd = LocalTime.of(18, 0);

                                        if (work_schedule.getShift() == 3)
                                            timeShiftEnd = LocalTime.of(23, 0);

                                        assert timeShiftEnd != null;
                                        long minutesEarly = ChronoUnit.MINUTES.between(checkout, timeShiftEnd);

                                        if (minutesEarly >= maxMinutesCheckOutEarly) {
                                            //System.out.println("Ngay ve som " + work_schedule.getDate() + "\n");
                                            //System.out.println("Thoi gian ve som " + minutesEarly + "\n");
                                            earlyShifts += 1;
                                        }
                                    }
                                }
                                deduction_amount = deduction_amount + (earlyShifts * deduction.getDeduction_amount());

                                //System.out.println(deduction.getName() + "\n");
                                //System.out.println("So ca ve som " + lateShifts + "\n");
                                //System.out.println("Tien giam tru ve som " + VNString.currency(earlyShifts * deduction.getDeduction_amount()) + "\n");
                            }
                            // tinh giam tru co dinh
                            if (deduction.getDeduction_type() == 2) {
                                deduction_amount = deduction_amount + (deduction.getDeduction_amount());

                                //System.out.println(deduction.getName() + "\n");
                                //System.out.println("Tien giam tru co dinh " + VNString.currency(deduction.getDeduction_amount()) + "\n");
                            }
                            // tinh giam tru nghi khong phep
                            if (deduction.getDeduction_type() == 3) {
                                deduction_amount = deduction_amount + (deduction.getDeduction_amount() * ca_nghi_khong_phep.size());

                                //System.out.println(deduction.getName() + "\n");
                                //System.out.println("So ca nghi khong phep " + ca_nghi_khong_phep.size() + "\n");
                                //System.out.println("Tien giam nghi khong phep " + VNString.currency(deduction.getDeduction_amount() * ca_nghi_khong_phep.size()) + "\n");
                            }
                        }

                        // tinh tien thuong va phat cua ca
                        for (Work_Schedule work_schedule : work_scheduleList) {
                            if (!work_schedule.getCheck_in().equals("null") && !work_schedule.getCheck_out().equals("null")) {
                                List<Work_Schedule_Bonus> work_schedule_bonuses = new Work_Schedule_BonusBLL().searchWork_schedules("work_schedule_id = " + work_schedule.getId());
                                List<Work_Schedule_Fine> work_schedule_fines = new Work_Schedule_FineBLL().searchWork_schedules("work_schedule_id = " + work_schedule.getId());

                                //System.out.println(work_schedule.getDate() + "\n");
                                for (Work_Schedule_Bonus workScheduleBonus : work_schedule_bonuses) {

                                    //System.out.println(workScheduleBonus.getBonus_name() + "\n");
                                    //System.out.println("So tien thuong: " + VNString.currency(workScheduleBonus.getBonus_total()) + "\n");
                                    bonus_amount += workScheduleBonus.getBonus_total();
                                }
                                for (Work_Schedule_Fine workScheduleFine : work_schedule_fines) {

                                    //System.out.println(workScheduleFine.getFine_name() + "\n");
                                    //System.out.println("So tien phat: " + VNString.currency(workScheduleFine.getFine_total()) + "\n");
                                    fine_amount += workScheduleFine.getFine_total();
                                }
                            }
                        }

                        //System.out.println("Tong tien thuong: " + VNString.currency(bonus_amount) + "\n");
                        //System.out.println("Tong tien phat: " + VNString.currency(fine_amount) + "\n");

                        //System.out.println("So tien sau thuong va phat " + VNString.currency(salary_amount) + "\n");

                        // tinh luong co dinh
                        if (roleDetail.getType_salary() == 1) {
                            List<Date> dates = new ArrayList<>();
                            for (Work_Schedule work_schedule : work_scheduleList) {
                                if (!work_schedule.getCheck_in().equals("null") && !work_schedule.getCheck_out().equals("null")) {
                                    if (!dates.contains(work_schedule.getDate()))
                                        dates.add(work_schedule.getDate());
                                }
                            }
                            int count = 0;
                            List<Leave_Of_Absence_Form> leaveOfAbsenceForms1 = new Leave_Of_Absence_FormBLL().searchLeave_Of_Absence_Forms("staff_id = " + staff.getId(), "reason = 'Nghỉ phép năm'", "YEAR(date_off) = " + payroll.getYear());
                            List<Leave_Of_Absence_Form> leaveOfAbsenceForms2 = new Leave_Of_Absence_FormBLL().searchLeave_Of_Absence_Forms("staff_id = " + staff.getId(), "reason = 'Nghỉ kết hôn'", "YEAR(date_off) = " + payroll.getYear());
                            List<Leave_Of_Absence_Form> leaveOfAbsenceForms3 = new Leave_Of_Absence_FormBLL().searchLeave_Of_Absence_Forms("staff_id = " + staff.getId(), "reason = 'Nghỉ con cái kết hôn'", "YEAR(date_off) = " + payroll.getYear());

                            for (Leave_Of_Absence_Form leaveOfAbsenceForm : new Leave_Of_Absence_FormBLL().searchLeave_Of_Absence_Forms("staff_id = " + staff.getId(), "MONTH(date_off) = " + payroll.getMonth(), "YEAR(date_off) = " + payroll.getYear())) {
                                if (leaveOfAbsenceForm.getReason().equals("Nghỉ phép năm")) {
                                    if (leaveOfAbsenceForms1.size() <= 12) {
                                        leaveOfAbsenceForms1.add(new Leave_Of_Absence_Form());
                                        count += 1;
                                    }
                                }
                                if (leaveOfAbsenceForm.getReason().equals("Nghỉ kết hôn")) {
                                    if (leaveOfAbsenceForms2.size() <= 3) {
                                        leaveOfAbsenceForms2.add(new Leave_Of_Absence_Form());
                                        count += 1;
                                    }
                                }
                                if (leaveOfAbsenceForm.getReason().equals("Nghỉ con cái kết hôn")) {
                                    if (leaveOfAbsenceForms3.isEmpty()) {
                                        leaveOfAbsenceForms3.add(new Leave_Of_Absence_Form());
                                        count += 1;
                                    }
                                }
                            }
                            salary_amount = (salary_amount + allowance_amount) / 26.0 * (dates.size() + count) + bonus_amount - fine_amount - deduction_amount; // lương tháng cố định = (lương thoả thuận + phụ cấp)/26 * (số ngày làm + số ngày nghỉ được hưởng lương) - phạt  + thưởng - giảm trừ

                            //System.out.println("So tien thuc cua luong co dinh: " + VNString.currency(salary_amount));
                            hours_amount = dates.size() + count;
                        } else {
                            salary_amount = salary_amount + allowance_amount + bonus_amount - fine_amount - deduction_amount;
                            //System.out.println("So tien thuc lanh: " + VNString.currency(salary_amount));
                        }
                        Payroll_Detail payrollDetail = new Payroll_Detail(payroll.getId(), staff.getId(), hours_amount, allowance_amount, deduction_amount, bonus_amount, fine_amount, salary_amount, false);
                        payrollDetails.add(payrollDetail);
                        total_salary += salary_amount;
                    }
                }
            } else {
                Payroll_Detail payrollDetail = new Payroll_Detail(payroll.getId(), staff.getId(), 0, 0, 0, 0, 0, 0, false);
                payrollDetails.add(payrollDetail);
            }
        }
        payroll.setTotal_salary(total_salary);
        payroll.setDebt(total_salary);
        Payroll_DetailBLL payrollDetailBLL = new Payroll_DetailBLL();

        if (payrollDAL.addPayroll(payroll) == 0) {
            return new Pair<>(false, "Thêm bảng lương không thành công.");
        }

        for (Payroll_Detail payrollDetail : payrollDetails) {
            payrollDetailBLL.addPayroll_Detail(payrollDetail);
        }
        return new Pair<>(true, "Thêm bảng lương thành công.");
    }

    public Pair<Boolean, String> updatePayroll(Payroll payroll) {
        if (payrollDAL.updatePayroll(payroll) == 0)
            return new Pair<>(false, "Cập nhật bảng lương không thành công.");

        return new Pair<>(true, "Cập nhật bảng lương thành công.");
    }

    public Pair<Boolean, String> deletePayroll(Payroll payroll) {

        if (payrollDAL.deletePayroll("id = " + payroll.getId()) == 0)
            return new Pair<>(false, "Xoá bảng lương không thành công.");

        return new Pair<>(true, "Xoá bảng lương thành công.");
    }

    public List<Payroll> searchPayrolls(String... conditions) {
        return payrollDAL.searchPayrolls(conditions);
    }

    public List<Payroll> findPayrolls(String key, String value) {
        List<Payroll> list = new ArrayList<>();
        List<Payroll> payrollList = payrollDAL.searchPayrolls();
        for (Payroll payroll : payrollList) {
            if (getValueByKey(payroll, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(payroll);
            }
        }
        return list;
    }

    public List<Payroll> findPayrollsBy(Map<String, Object> conditions) {
        List<Payroll> payrolls = payrollDAL.searchPayrolls();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            payrolls = findObjectsBy(entry.getKey(), entry.getValue(), payrolls);
        return payrolls;
    }

    public Pair<Boolean, String> exists(Payroll payroll) {
        List<Payroll> payrolls = findPayrollsBy(Map.of(
                "month", payroll.getMonth(),
                "year", payroll.getYear()
        ));

        if (!payrolls.isEmpty()) {
            return new Pair<>(true, "Bảng lương tháng này đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    public Pair<Boolean, String> validateMonth(String month) {
        if (month.isBlank())
            return new Pair<>(false, "Tháng không được để trống");
        if (!checkMonth(month))
            return new Pair<>(false, "Tháng phải là số nguyên và nằm trong khoảng từ 1 đến 12");
        return new Pair<>(true, "Tháng hợp lệ");
    }

    public Pair<Boolean, String> validateYear(String year) {
        if (year.isBlank())
            return new Pair<>(false, "Năm không được để trống");
        if (!checkYear(year))
            return new Pair<>(false, "Năm phải là số nguyên và phải lớn hơn bằng năm hiện tại");
        return new Pair<>(true, "Tháng hợp lệ");
    }

    public boolean checkMonth(String str) {
        try {
            int number = Integer.parseInt(str);
            return number >= 0 && number <= 12;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean checkYear(String str) {
        try {
            int year = Integer.parseInt(str);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            return year >= currentYear && year < 9999;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Object getValueByKey(Payroll payroll, String key) {
        return switch (key) {
            case "id" -> payroll.getId();
            case "name" -> payroll.getName();
            case "entry_date" -> payroll.getEntry_date();
            case "month" -> payroll.getMonth();
            case "year" -> payroll.getYear();
            case "total_salary" -> payroll.getTotal_salary();
            case "paid" -> payroll.getPaid();
            case "debt" -> payroll.getDebt();
            default -> null;
        };
    }
}
