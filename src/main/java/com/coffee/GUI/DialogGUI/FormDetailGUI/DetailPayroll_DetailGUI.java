package com.coffee.GUI.DialogGUI.FormDetailGUI;

import com.coffee.BLL.*;
import com.coffee.DTO.*;
import com.coffee.GUI.DialogGUI.DialogForm;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.main.Cafe_Application;
import com.coffee.utils.PDF;
import com.coffee.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DetailPayroll_DetailGUI extends DialogForm {
    private final int maxMinutesCheckInLate = 15;
    private final int maxMinutesCheckOutEarly = 15;
    private Payroll_Detail payrollDetail;
    private Payroll payroll;
    private List<List<String>> hoursObjectList = new ArrayList<>();
    private List<List<String>> allowanceObjectList = new ArrayList<>();
    private List<List<String>> lateObjectList = new ArrayList<>();
    private List<List<String>> earlyObjectList = new ArrayList<>();
    private List<List<String>> defaultDeductionObjectList = new ArrayList<>();
    private List<List<String>> absentObjectList1 = new ArrayList<>();
    private List<List<String>> absentObjectList2 = new ArrayList<>();
    private List<List<String>> bonusObjectList = new ArrayList<>();
    private List<List<String>> fineObjectList = new ArrayList<>();

    public DetailPayroll_DetailGUI(Payroll_Detail payrollDetail, Payroll payroll) {
        super();
        super.setTitle("BẢNG LƯƠNG CHI TIẾT THÁNG " + payroll.getMonth() + "/" + payroll.getYear());
        super.setSize(new Dimension(1000, 600));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        this.payrollDetail = payrollDetail;
        this.payroll = payroll;
        init();
        setVisible(true);
    }

    private void init() {
        JLabel titleName = new JLabel();
        content.setLayout(new BorderLayout());

        titleName.setText("BẢNG LƯƠNG CHI TIẾT THÁNG " + payroll.getMonth() + "/" + payroll.getYear());
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);
        title.setBackground(new Color(242, 245, 250));

        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.setBackground(Color.white);
        jTabbedPane.setPreferredSize(new Dimension(1000, 600));
        content.add(jTabbedPane);

        Role_Detail roleDetail = new Role_DetailBLL().searchRole_detailsByStaff(payrollDetail.getStaff_id()).get(0);
        JPanel infoPanel = new JPanel();
        JScrollPane hoursPanel = new JScrollPane();
        JScrollPane allowancePanel = new JScrollPane();
        JScrollPane latePanel = new JScrollPane();
        JScrollPane earlyPanel = new JScrollPane();
        JScrollPane defaultDeductionPanel = new JScrollPane();
        JScrollPane absentPanel1 = new JScrollPane();
        JScrollPane absentPanel2 = new JScrollPane();
        JScrollPane bonusPanel = new JScrollPane();
        JScrollPane finePanel = new JScrollPane();

        jTabbedPane.add("Thông Tin", infoPanel);
        if (roleDetail.getType_salary() == 2)
            jTabbedPane.add("Giờ Công", hoursPanel);
        jTabbedPane.add("Phụ Cấp", allowancePanel);
        jTabbedPane.add("Đi Trễ", latePanel);
        jTabbedPane.add("Về Sớm", earlyPanel);
        jTabbedPane.add("Giảm Trừ Cố định", defaultDeductionPanel);
        jTabbedPane.add("Nghỉ Không Phép", absentPanel2);
        jTabbedPane.add("Nghỉ Có Phép", absentPanel1);
        jTabbedPane.add("Thưởng", bonusPanel);
        jTabbedPane.add("Phạt Vi Phạm", finePanel);

        List<Work_Schedule> work_scheduleList = new Work_ScheduleBLL().searchWork_schedulesByStaff(payrollDetail.getStaff_id(), payroll.getYear(), payroll.getMonth());
        Staff staff = new StaffBLL().searchStaffs("id = " + payrollDetail.getStaff_id()).get(0);
        Salary_Format salaryFormat = new Salary_FormatBLL().searchSalary_Formats("id  = " + staff.getSalary_format_id()).get(0);
        List<Salary_Format_Allowance> salaryFormatAllowances = new Salary_Format_AllowanceBLL().searchSalary_Format_Allowances("salary_format_id = " + salaryFormat.getId());
        List<Salary_Format_Deduction> salaryFormatDeductions = new Salary_Format_DeductionBLL().searchSalary_Format_Deductions("salary_format_id = " + salaryFormat.getId());

        List<Work_Schedule> ca_nghi_co_phep = new ArrayList<>();

        if (roleDetail.getType_salary() == 2) {
            // tinh luong theo gio
            for (Work_Schedule work_schedule : work_scheduleList) {
                if (!work_schedule.getCheck_in().equals("null") && !work_schedule.getCheck_out().equals("null")) {
                    // ca co di lam
                    String[] checkinArr, checkoutArr;
                    double checkin, checkout;

                    checkinArr = work_schedule.getCheck_in().split(":");
                    double v = Double.parseDouble(checkinArr[1]) / 60;
                    checkin = Double.parseDouble(checkinArr[0]) + v;

                    checkoutArr = work_schedule.getCheck_out().split(":");
                    v = Double.parseDouble(checkoutArr[1]) / 60;
                    checkout = Double.parseDouble(checkoutArr[0]) + v;

                    List<String> hoursList = new ArrayList<>();
                    hoursList.add(new SimpleDateFormat("dd/MM/yyyy").format(work_schedule.getDate()));
                    if (work_schedule.getShift() == 1)
                        hoursList.add("Ca 1: 6:00 - 12:00");

                    if (work_schedule.getShift() == 2)
                        hoursList.add("Ca 2: 12:00 - 18:00");

                    if (work_schedule.getShift() == 3)
                        hoursList.add("Ca 3: 18:00 - 23:00");
                    hoursList.add(work_schedule.getCheck_in());
                    hoursList.add(work_schedule.getCheck_out());
                    hoursList.add(String.format("%.2f", (Math.abs(checkout - checkin))));

                    hoursObjectList.add(hoursList);
                }
            }
        }


        for (Salary_Format_Allowance salaryFormatAllowance : salaryFormatAllowances) {
            Allowance allowance = new AllowanceBLL().searchAllowances("id = " + salaryFormatAllowance.getAllowance_id()).get(0);
            List<String> allowanceList = new ArrayList<>();

            // tinh phu cap theo ngay
            if (allowance.getAllowance_type() == 0) {
                List<Date> dates = new ArrayList<>();
                for (Work_Schedule work_schedule : work_scheduleList) {
                    if (!work_schedule.getCheck_in().equals("null") && !work_schedule.getCheck_out().equals("null")) {
                        if (!dates.contains(work_schedule.getDate()))
                            dates.add(work_schedule.getDate());
                    }

                }

                allowanceList.add(allowance.getName());
                allowanceList.add(allowance.getAllowance_type() == 0 ? "Phụ cấp theo ngày" : "Phụ cấp theo tháng");
                allowanceList.add(VNString.currency(allowance.getAllowance_amount()));
                allowanceList.add(dates.size() + " ngày");
                allowanceList.add(VNString.currency(dates.size() * allowance.getAllowance_amount()));
            }
            // tinh phu cap theo thang
            if (allowance.getAllowance_type() == 1) {
                allowanceList.add(allowance.getName());
                allowanceList.add(allowance.getAllowance_type() == 0 ? "Phụ cấp theo ngày" : "Phụ cấp theo tháng");
                allowanceList.add(VNString.currency(allowance.getAllowance_amount()));
                allowanceList.add(1 + " tháng");
                allowanceList.add(VNString.currency(allowance.getAllowance_amount()));
            }
            allowanceObjectList.add(allowanceList);
        }

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
                            List<String> lateList = new ArrayList<>();
                            lateList.add(new SimpleDateFormat("dd/MM/yyyy").format(work_schedule.getDate()));
                            if (work_schedule.getShift() == 1)
                                lateList.add("Ca 1: 6:00 - 12:00");

                            if (work_schedule.getShift() == 2)
                                lateList.add("Ca 2: 12:00 - 18:00");

                            if (work_schedule.getShift() == 3)
                                lateList.add("Ca 3: 18:00 - 23:00");

                            lateList.add(work_schedule.getCheck_in());
                            lateList.add(VNString.currency(deduction.getDeduction_amount()));

                            lateObjectList.add(lateList);
                        }
                    }
                }
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
                            List<String> earlyList = new ArrayList<>();
                            earlyList.add(new SimpleDateFormat("dd/MM/yyyy").format(work_schedule.getDate()));
                            if (work_schedule.getShift() == 1)
                                earlyList.add("Ca 1: 6:00 - 12:00");

                            if (work_schedule.getShift() == 2)
                                earlyList.add("Ca 2: 12:00 - 18:00");

                            if (work_schedule.getShift() == 3)
                                earlyList.add("Ca 3: 18:00 - 23:00");

                            earlyList.add(work_schedule.getCheck_out());
                            earlyList.add(VNString.currency(deduction.getDeduction_amount()));

                            earlyObjectList.add(earlyList);
                        }
                    }
                }
            }
            // tinh giam tru co dinh
            if (deduction.getDeduction_type() == 2) {
                List<String> defaultDeductionList = new ArrayList<>();
                defaultDeductionList.add(deduction.getName());
                defaultDeductionList.add(VNString.currency(deduction.getDeduction_amount()));
                defaultDeductionList.add(1 + " tháng");
                defaultDeductionList.add(VNString.currency(deduction.getDeduction_amount()));

                defaultDeductionObjectList.add(defaultDeductionList);
            }
            // tinh giam tru nghi khong phep
            if (deduction.getDeduction_type() == 3) {
                for (Work_Schedule work_schedule : work_scheduleList) {
                    if (work_schedule.getCheck_in().equals("null") && work_schedule.getCheck_out().equals("null")) {
                        if (work_schedule.getNotice().equals("Không")) {
                            List<String> absentList2 = new ArrayList<>();
                            absentList2.add(new SimpleDateFormat("dd/MM/yyyy").format(work_schedule.getDate()));
                            absentList2.add(String.valueOf(work_schedule.getShift()));
                            absentList2.add(VNString.currency(deduction.getDeduction_amount()));

                            absentObjectList2.add(absentList2);
                        } else {
                            ca_nghi_co_phep.add(work_schedule);
                        }
                    }
                }
            }
        }

        List<Leave_Of_Absence_Form> leaveOfAbsenceForms1 = new Leave_Of_Absence_FormBLL().searchLeave_Of_Absence_Forms("staff_id = " + staff.getId(), "reason = 'Nghỉ phép năm'", "YEAR(date_off) = " + payroll.getYear());
        List<Leave_Of_Absence_Form> leaveOfAbsenceForms2 = new Leave_Of_Absence_FormBLL().searchLeave_Of_Absence_Forms("staff_id = " + staff.getId(), "reason = 'Nghỉ kết hôn'", "YEAR(date_off) = " + payroll.getYear());
        List<Leave_Of_Absence_Form> leaveOfAbsenceForms3 = new Leave_Of_Absence_FormBLL().searchLeave_Of_Absence_Forms("staff_id = " + staff.getId(), "reason = 'Nghỉ con cái kết hôn'", "YEAR(date_off) = " + payroll.getYear());

        for (Leave_Of_Absence_Form leaveOfAbsenceForm : new Leave_Of_Absence_FormBLL().searchLeave_Of_Absence_Forms("staff_id = " + staff.getId(), "MONTH(date_off) = " + payroll.getMonth(), "YEAR(date_off) = " + payroll.getYear())) {
            List<String> absentList1 = new ArrayList<>();

            absentList1.add(new SimpleDateFormat("dd/MM/yyyy").format(leaveOfAbsenceForm.getDate_off()));
            absentList1.add(leaveOfAbsenceForm.getShifts());
            absentList1.add(leaveOfAbsenceForm.getReason());
            String result = "Không tính lương";
            if (roleDetail.getType_salary() == 1) {
                if (leaveOfAbsenceForm.getReason().equals("Nghỉ phép năm")) {
                    if (leaveOfAbsenceForms1.size() <= 12) {
                        leaveOfAbsenceForms1.add(new Leave_Of_Absence_Form());
                        result = "Tính lương";

                    }
                }
                if (leaveOfAbsenceForm.getReason().equals("Nghỉ kết hôn")) {
                    if (leaveOfAbsenceForms2.size() <= 3) {
                        leaveOfAbsenceForms2.add(new Leave_Of_Absence_Form());
                        result = "Tính lương";
                    }
                }
                if (leaveOfAbsenceForm.getReason().equals("Nghỉ con cái kết hôn")) {
                    if (leaveOfAbsenceForms3.isEmpty()) {
                        leaveOfAbsenceForms3.add(new Leave_Of_Absence_Form());
                        result = "Tính lương";
                    }
                }
            }
            absentList1.add(result);
            absentObjectList1.add(absentList1);
        }

        // tinh tien thuong va phat cua ca
        for (Work_Schedule work_schedule : work_scheduleList) {
            if (!work_schedule.getCheck_in().equals("null") && !work_schedule.getCheck_out().equals("null")) {
                List<Work_Schedule_Bonus> work_schedule_bonuses = new Work_Schedule_BonusBLL().searchWork_schedules("work_schedule_id = " + work_schedule.getId());
                List<Work_Schedule_Fine> work_schedule_fines = new Work_Schedule_FineBLL().searchWork_schedules("work_schedule_id = " + work_schedule.getId());

                for (Work_Schedule_Bonus workScheduleBonus : work_schedule_bonuses) {
                    List<String> bonusList = new ArrayList<>();
                    bonusList.add(new SimpleDateFormat("dd/MM/yyyy").format(work_schedule.getDate()));
                    bonusList.add(workScheduleBonus.getBonus_name());
                    bonusList.add(String.valueOf(workScheduleBonus.getQuantity()));
                    bonusList.add(VNString.currency(workScheduleBonus.getBonus_amount()));
                    bonusList.add(VNString.currency(workScheduleBonus.getBonus_total()));

                    bonusObjectList.add(bonusList);
                }
                for (Work_Schedule_Fine workScheduleFine : work_schedule_fines) {
                    List<String> fineList = new ArrayList<>();
                    fineList.add(new SimpleDateFormat("dd/MM/yyyy").format(work_schedule.getDate()));
                    fineList.add(workScheduleFine.getFine_name());
                    fineList.add(String.valueOf(workScheduleFine.getQuantity()));
                    fineList.add(VNString.currency(workScheduleFine.getFine_amount()));
                    fineList.add(VNString.currency(workScheduleFine.getFine_total()));

                    fineObjectList.add(fineList);
                }
            }

        }

//        System.out.println("gio cong" + Arrays.toString(hoursObjectList.toArray()));
//        System.out.println("tro cap" + Arrays.toString(allowanceObjectList.toArray()));
//        System.out.println("di tre" + Arrays.toString(lateObjectList.toArray()));
//        System.out.println("ve som" + Arrays.toString(earlyObjectList.toArray()));
//        System.out.println("giam tru mac dinh" + Arrays.toString(defaultDeductionObjectList.toArray()));
//        System.out.println("co phep" + Arrays.toString(absentObjectList1.toArray()));
//        System.out.println("khong phep" + Arrays.toString(absentObjectList2.toArray()));
//        System.out.println("thuong" + Arrays.toString(bonusObjectList.toArray()));
//        System.out.println("phat" + Arrays.toString(fineObjectList.toArray()));

        initInfoPanel(infoPanel);
        if (roleDetail.getType_salary() == 2)
            initHoursPanel(hoursPanel);
        initAllowancePanel(allowancePanel);
        initLatePanel(latePanel);
        initEarlyPanel(earlyPanel);
        initDefaultDeductionPanel(defaultDeductionPanel);
        initAbsent1Panel(absentPanel1);
        initAbsent2Panel(absentPanel2);
        initBonusPanel(bonusPanel);
        initFinePanel(finePanel);

        RoundedPanel roundedPanel = new RoundedPanel();
        roundedPanel.setLayout(new GridBagLayout());
        roundedPanel.setPreferredSize(new Dimension(150, 40));
        roundedPanel.setBackground(new Color(1, 120, 220));
        roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        containerButton.add(roundedPanel);

        JLabel panel = new JLabel("In phiếu lương");
        panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
        panel.setForeground(Color.white);
        panel.setIcon(new FlatSVGIcon("icon/print.svg"));
        roundedPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                PDF.exportPayrollDetailPDF(payroll, payrollDetail, "src/main/resources/ExportPDF");
                JOptionPane.showMessageDialog(null, "In phiếu lương thành công.",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
        roundedPanel.add(panel);
    }

    private void initHoursPanel(JScrollPane hoursPanel) {
        hoursPanel.setPreferredSize(new Dimension(1000, 600));
        hoursPanel.setBackground(Color.gray);

        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(new MigLayout("", "10[]10", "10[]10"));

        JPanel jpanel1 = new JPanel(new MigLayout("", "0[]10[]10[]10[]10[]0", "0[]0"));
        jpanel1.setBackground(new Color(227, 242, 250));
        jpanel1.setPreferredSize(new Dimension(860, 30));

        JLabel titleHoursDate = new JLabel();
        JLabel titleHoursShift = new JLabel();
        JLabel titleHoursCheckin = new JLabel();
        JLabel titleHoursCheckout = new JLabel();
        JLabel titleHoursTotal = new JLabel();

        titleHoursDate.setText("Ngày");
        titleHoursShift.setText("Ca làm");
        titleHoursCheckin.setText("Giờ vào ca");
        titleHoursCheckout.setText("Giờ ra ca");
        titleHoursTotal.setText("Tổng giờ công");

        titleHoursDate.setPreferredSize(new Dimension(200, 30));
        titleHoursDate.setFont((new Font("Inter", Font.BOLD, 13)));

        titleHoursShift.setPreferredSize(new Dimension(200, 30));
        titleHoursShift.setFont((new Font("Inter", Font.BOLD, 13)));

        titleHoursCheckin.setPreferredSize(new Dimension(200, 30));
        titleHoursCheckin.setFont((new Font("Inter", Font.BOLD, 13)));

        titleHoursCheckout.setPreferredSize(new Dimension(200, 30));
        titleHoursCheckout.setFont((new Font("Inter", Font.BOLD, 13)));

        titleHoursTotal.setPreferredSize(new Dimension(200, 30));
        titleHoursTotal.setFont((new Font("Inter", Font.BOLD, 13)));

        jpanel1.add(titleHoursDate);
        jpanel1.add(titleHoursShift);
        jpanel1.add(titleHoursCheckin);
        jpanel1.add(titleHoursCheckout);
        jpanel1.add(titleHoursTotal, "wrap");
        panel.add(jpanel1, "wrap");

        for (List<String> list : hoursObjectList) {
            JPanel jPanelHours = new JPanel(new MigLayout("", "0[]10[]10[]10[]10[]0", "0[]0"));
            jPanelHours.setBackground(new Color(255, 255, 255));
            jPanelHours.setPreferredSize(new Dimension(860, 50));

            JTextField jLabelHoursDate = new MyTextFieldUnderLine();
            jLabelHoursDate.setText(list.get(0));
            jLabelHoursDate.setPreferredSize(new Dimension(200, 30));
            jLabelHoursDate.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelHoursDate.setEditable(false);
            jPanelHours.add(jLabelHoursDate);

            JTextField jLabelHoursShift = new MyTextFieldUnderLine();
            jLabelHoursShift.setText(list.get(1));
            jLabelHoursShift.setPreferredSize(new Dimension(200, 30));
            jLabelHoursShift.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelHoursShift.setEditable(false);
            jPanelHours.add(jLabelHoursShift);

            JTextField jLabelHoursCheckin = new MyTextFieldUnderLine();
            jLabelHoursCheckin.setText(list.get(2));
            jLabelHoursCheckin.setPreferredSize(new Dimension(200, 30));
            jLabelHoursCheckin.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelHoursCheckin.setEditable(false);
            jPanelHours.add(jLabelHoursCheckin);

            JTextField jLabelHoursCheckout = new MyTextFieldUnderLine();
            jLabelHoursCheckout.setText(list.get(3));
            jLabelHoursCheckout.setPreferredSize(new Dimension(200, 30));
            jLabelHoursCheckout.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelHoursCheckout.setEditable(false);
            jPanelHours.add(jLabelHoursCheckout);

            JTextField jLabelHoursTotal = new MyTextFieldUnderLine();
            jLabelHoursTotal.setText(list.get(4));
            jLabelHoursTotal.setPreferredSize(new Dimension(200, 30));
            jLabelHoursTotal.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelHoursTotal.setEditable(false);
            jPanelHours.add(jLabelHoursTotal);

            panel.add(jPanelHours, "wrap");
        }

        hoursPanel.setViewportView(panel);
    }

    private void initDefaultDeductionPanel(JScrollPane defaultDeductionPanel) {
        defaultDeductionPanel.setPreferredSize(new Dimension(1000, 600));
        defaultDeductionPanel.setBackground(Color.gray);

        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(new MigLayout("", "10[]10", "10[]10"));

        JPanel jpanel1 = new JPanel(new MigLayout("", "0[]10[]10[]10[]0", "0[]0"));
        jpanel1.setBackground(new Color(227, 242, 250));
        jpanel1.setPreferredSize(new Dimension(860, 30));

        JLabel titleDefaultDeductionName = new JLabel();
        JLabel titleDefaultDeductionAmount = new JLabel();
        JLabel titleDefaultDeductionType = new JLabel();
        JLabel titleDefaultDeductionTotal = new JLabel();

        titleDefaultDeductionName.setText("Tên giảm trừ cố định");
        titleDefaultDeductionAmount.setText("Số tiền giảm trừ");
        titleDefaultDeductionType.setText("Số tháng");
        titleDefaultDeductionTotal.setText("Tổng giảm trừ");

        titleDefaultDeductionName.setPreferredSize(new Dimension(200, 30));
        titleDefaultDeductionName.setFont((new Font("Inter", Font.BOLD, 13)));

        titleDefaultDeductionAmount.setPreferredSize(new Dimension(200, 30));
        titleDefaultDeductionAmount.setFont((new Font("Inter", Font.BOLD, 13)));

        titleDefaultDeductionType.setPreferredSize(new Dimension(200, 30));
        titleDefaultDeductionType.setFont((new Font("Inter", Font.BOLD, 13)));

        titleDefaultDeductionTotal.setPreferredSize(new Dimension(200, 30));
        titleDefaultDeductionTotal.setFont((new Font("Inter", Font.BOLD, 13)));

        jpanel1.add(titleDefaultDeductionName);
        jpanel1.add(titleDefaultDeductionAmount);
        jpanel1.add(titleDefaultDeductionType);
        jpanel1.add(titleDefaultDeductionTotal, "wrap");
        panel.add(jpanel1, "wrap");

        for (List<String> list : defaultDeductionObjectList) {
            JPanel jPanelDefaultDeduction = new JPanel(new MigLayout("", "0[]10[]10[]10[]0", "0[]0"));
            jPanelDefaultDeduction.setBackground(new Color(255, 255, 255));
            jPanelDefaultDeduction.setPreferredSize(new Dimension(860, 50));

            JTextField jLabelDefaultDeductionName = new MyTextFieldUnderLine();
            jLabelDefaultDeductionName.setText(list.get(0));
            jLabelDefaultDeductionName.setPreferredSize(new Dimension(200, 30));
            jLabelDefaultDeductionName.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelDefaultDeductionName.setEditable(false);
            jPanelDefaultDeduction.add(jLabelDefaultDeductionName);

            JTextField jLabelDefaultDeductionAmount = new MyTextFieldUnderLine();
            jLabelDefaultDeductionAmount.setText(list.get(1));
            jLabelDefaultDeductionAmount.setPreferredSize(new Dimension(200, 30));
            jLabelDefaultDeductionAmount.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelDefaultDeductionAmount.setEditable(false);
            jPanelDefaultDeduction.add(jLabelDefaultDeductionAmount);

            JTextField jLabelDefaultDeductionType = new MyTextFieldUnderLine();
            jLabelDefaultDeductionType.setText(list.get(2));
            jLabelDefaultDeductionType.setPreferredSize(new Dimension(200, 30));
            jLabelDefaultDeductionType.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelDefaultDeductionType.setEditable(false);
            jPanelDefaultDeduction.add(jLabelDefaultDeductionType);

            JTextField jLabelDefaultDeductionTotal = new MyTextFieldUnderLine();
            jLabelDefaultDeductionTotal.setText(list.get(3));
            jLabelDefaultDeductionTotal.setPreferredSize(new Dimension(200, 30));
            jLabelDefaultDeductionTotal.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelDefaultDeductionTotal.setEditable(false);
            jPanelDefaultDeduction.add(jLabelDefaultDeductionTotal);

            panel.add(jPanelDefaultDeduction, "wrap");
        }

        defaultDeductionPanel.setViewportView(panel);
    }

    private void initFinePanel(JScrollPane finePanel) {
        finePanel.setPreferredSize(new Dimension(1000, 600));
        finePanel.setBackground(Color.gray);

        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(new MigLayout("", "10[]10", "10[]10"));

        JPanel jpanel1 = new JPanel(new MigLayout("", "0[]10[]10[]10[]10[]0", "0[]0"));
        jpanel1.setBackground(new Color(227, 242, 250));
        jpanel1.setPreferredSize(new Dimension(860, 30));

        JLabel titleFineDate = new JLabel();
        JLabel titleFineName = new JLabel();
        JLabel titleFineQuantity = new JLabel();
        JLabel titleFineAmount = new JLabel();
        JLabel titleFineTotal = new JLabel();

        titleFineDate.setText("Ngày");
        titleFineName.setText("Tên vi phạm");
        titleFineQuantity.setText("Số lần vi phạm");
        titleFineAmount.setText("Số tiền phạt");
        titleFineTotal.setText("Tổng tiền phạt");

        titleFineDate.setPreferredSize(new Dimension(200, 30));
        titleFineDate.setFont((new Font("Inter", Font.BOLD, 13)));

        titleFineName.setPreferredSize(new Dimension(200, 30));
        titleFineName.setFont((new Font("Inter", Font.BOLD, 13)));

        titleFineQuantity.setPreferredSize(new Dimension(200, 30));
        titleFineQuantity.setFont((new Font("Inter", Font.BOLD, 13)));

        titleFineAmount.setPreferredSize(new Dimension(200, 30));
        titleFineAmount.setFont((new Font("Inter", Font.BOLD, 13)));

        titleFineTotal.setPreferredSize(new Dimension(200, 30));
        titleFineTotal.setFont((new Font("Inter", Font.BOLD, 13)));

        jpanel1.add(titleFineDate);
        jpanel1.add(titleFineName);
        jpanel1.add(titleFineQuantity);
        jpanel1.add(titleFineAmount);
        jpanel1.add(titleFineTotal, "wrap");
        panel.add(jpanel1, "wrap");

        for (List<String> list : fineObjectList) {
            JPanel jPanelFine = new JPanel(new MigLayout("", "0[]10[]10[]10[]10[]0", "0[]0"));
            jPanelFine.setBackground(new Color(255, 255, 255));
            jPanelFine.setPreferredSize(new Dimension(860, 50));

            JTextField jLabelFineDate = new MyTextFieldUnderLine();
            jLabelFineDate.setText(list.get(0));
            jLabelFineDate.setPreferredSize(new Dimension(200, 30));
            jLabelFineDate.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelFineDate.setEditable(false);
            jPanelFine.add(jLabelFineDate);

            JTextField jLabelFineName = new MyTextFieldUnderLine();
            jLabelFineName.setText(list.get(1));
            jLabelFineName.setPreferredSize(new Dimension(200, 30));
            jLabelFineName.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelFineName.setEditable(false);
            jPanelFine.add(jLabelFineName);

            JTextField jLabelFineQuantity = new MyTextFieldUnderLine();
            jLabelFineQuantity.setText(list.get(2));
            jLabelFineQuantity.setPreferredSize(new Dimension(200, 30));
            jLabelFineQuantity.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelFineQuantity.setEditable(false);
            jPanelFine.add(jLabelFineQuantity);

            JTextField jLabelFineAmount = new MyTextFieldUnderLine();
            jLabelFineAmount.setText(list.get(3));
            jLabelFineAmount.setPreferredSize(new Dimension(200, 30));
            jLabelFineAmount.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelFineAmount.setEditable(false);
            jPanelFine.add(jLabelFineAmount);

            JTextField jLabelFineTotal = new MyTextFieldUnderLine();
            jLabelFineTotal.setText(list.get(4));
            jLabelFineTotal.setPreferredSize(new Dimension(200, 30));
            jLabelFineTotal.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelFineTotal.setEditable(false);
            jPanelFine.add(jLabelFineTotal);

            panel.add(jPanelFine, "wrap");
        }

        finePanel.setViewportView(panel);
    }

    private void initBonusPanel(JScrollPane bonusPanel) {
        bonusPanel.setPreferredSize(new Dimension(1000, 600));
        bonusPanel.setBackground(Color.gray);

        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(new MigLayout("", "10[]10", "10[]10"));

        JPanel jpanel1 = new JPanel(new MigLayout("", "0[]10[]10[]10[]10[]0", "0[]0"));
        jpanel1.setBackground(new Color(227, 242, 250));
        jpanel1.setPreferredSize(new Dimension(860, 30));

        JLabel titleBonusDate = new JLabel();
        JLabel titleBonusName = new JLabel();
        JLabel titleBonusQuantity = new JLabel();
        JLabel titleBonusAmount = new JLabel();
        JLabel titleBonusTotal = new JLabel();

        titleBonusDate.setText("Ngày");
        titleBonusName.setText("Lý do thưởng");
        titleBonusQuantity.setText("Số lần");
        titleBonusAmount.setText("Số tiền thưởng");
        titleBonusTotal.setText("Tổng tiền thưởng");

        titleBonusDate.setPreferredSize(new Dimension(200, 30));
        titleBonusDate.setFont((new Font("Inter", Font.BOLD, 13)));

        titleBonusName.setPreferredSize(new Dimension(200, 30));
        titleBonusName.setFont((new Font("Inter", Font.BOLD, 13)));

        titleBonusQuantity.setPreferredSize(new Dimension(200, 30));
        titleBonusQuantity.setFont((new Font("Inter", Font.BOLD, 13)));

        titleBonusAmount.setPreferredSize(new Dimension(200, 30));
        titleBonusAmount.setFont((new Font("Inter", Font.BOLD, 13)));

        titleBonusTotal.setPreferredSize(new Dimension(200, 30));
        titleBonusTotal.setFont((new Font("Inter", Font.BOLD, 13)));

        jpanel1.add(titleBonusDate);
        jpanel1.add(titleBonusName);
        jpanel1.add(titleBonusQuantity);
        jpanel1.add(titleBonusAmount);
        jpanel1.add(titleBonusTotal, "wrap");
        panel.add(jpanel1, "wrap");

        for (List<String> list : bonusObjectList) {
            JPanel jPanelBonus = new JPanel(new MigLayout("", "0[]10[]10[]10[]10[]0", "0[]0"));
            jPanelBonus.setBackground(new Color(255, 255, 255));
            jPanelBonus.setPreferredSize(new Dimension(860, 50));

            JTextField jLabelBonusDate = new MyTextFieldUnderLine();
            jLabelBonusDate.setText(list.get(0));
            jLabelBonusDate.setPreferredSize(new Dimension(200, 30));
            jLabelBonusDate.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelBonusDate.setEditable(false);
            jPanelBonus.add(jLabelBonusDate);

            JTextField jLabelBonusName = new MyTextFieldUnderLine();
            jLabelBonusName.setText(list.get(1));
            jLabelBonusName.setPreferredSize(new Dimension(200, 30));
            jLabelBonusName.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelBonusName.setEditable(false);
            jPanelBonus.add(jLabelBonusName);

            JTextField jLabelBonusQuantity = new MyTextFieldUnderLine();
            jLabelBonusQuantity.setText(list.get(2));
            jLabelBonusQuantity.setPreferredSize(new Dimension(200, 30));
            jLabelBonusQuantity.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelBonusQuantity.setEditable(false);
            jPanelBonus.add(jLabelBonusQuantity);

            JTextField jLabelBonusAmount = new MyTextFieldUnderLine();
            jLabelBonusAmount.setText(list.get(3));
            jLabelBonusAmount.setPreferredSize(new Dimension(200, 30));
            jLabelBonusAmount.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelBonusAmount.setEditable(false);
            jPanelBonus.add(jLabelBonusAmount);

            JTextField jLabelBonusTotal = new MyTextFieldUnderLine();
            jLabelBonusTotal.setText(list.get(4));
            jLabelBonusTotal.setPreferredSize(new Dimension(200, 30));
            jLabelBonusTotal.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelBonusTotal.setEditable(false);
            jPanelBonus.add(jLabelBonusTotal);

            panel.add(jPanelBonus, "wrap");
        }

        bonusPanel.setViewportView(panel);
    }

    private void initAbsent2Panel(JScrollPane absentPanel2) {
        absentPanel2.setPreferredSize(new Dimension(1000, 600));
        absentPanel2.setBackground(Color.gray);

        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(new MigLayout("", "10[]10", "10[]10"));

        JPanel jpanel1 = new JPanel(new MigLayout("", "0[]10[]10[]0", "0[]0"));
        jpanel1.setBackground(new Color(227, 242, 250));
        jpanel1.setPreferredSize(new Dimension(860, 30));

        JLabel titleLateDate = new JLabel();
        JLabel titleLateShift = new JLabel();
        JLabel titleLateTotal = new JLabel();

        titleLateDate.setText("Ngày nghỉ có phép");
        titleLateShift.setText("Ca nghỉ");
        titleLateTotal.setText("Tổng tiền giảm trừ");

        titleLateDate.setPreferredSize(new Dimension(200, 30));
        titleLateDate.setFont((new Font("Inter", Font.BOLD, 13)));

        titleLateShift.setPreferredSize(new Dimension(200, 30));
        titleLateShift.setFont((new Font("Inter", Font.BOLD, 13)));

        titleLateTotal.setPreferredSize(new Dimension(200, 30));
        titleLateTotal.setFont((new Font("Inter", Font.BOLD, 13)));

        jpanel1.add(titleLateDate);
        jpanel1.add(titleLateShift);
        jpanel1.add(titleLateTotal, "wrap");
        panel.add(jpanel1, "wrap");

        for (List<String> list : absentObjectList2) {
            JPanel jPanelLate = new JPanel(new MigLayout("", "0[]10[]10[]0", "0[]0"));
            jPanelLate.setBackground(new Color(255, 255, 255));
            jPanelLate.setPreferredSize(new Dimension(860, 50));

            JTextField jLabelLateDate = new MyTextFieldUnderLine();
            jLabelLateDate.setText(list.get(0));
            jLabelLateDate.setPreferredSize(new Dimension(200, 30));
            jLabelLateDate.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelLateDate.setEditable(false);
            jPanelLate.add(jLabelLateDate);

            JTextField jLabelLateShift = new MyTextFieldUnderLine();
            jLabelLateShift.setText(list.get(1));
            jLabelLateShift.setPreferredSize(new Dimension(200, 30));
            jLabelLateShift.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelLateShift.setEditable(false);
            jPanelLate.add(jLabelLateShift);

            JTextField jLabelLateTotal = new MyTextFieldUnderLine();
            jLabelLateTotal.setText(list.get(2));
            jLabelLateTotal.setPreferredSize(new Dimension(200, 30));
            jLabelLateTotal.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelLateTotal.setEditable(false);
            jPanelLate.add(jLabelLateTotal);

            panel.add(jPanelLate, "wrap");
        }

        absentPanel2.setViewportView(panel);
    }

    private void initAbsent1Panel(JScrollPane absentPanel1) {
        absentPanel1.setPreferredSize(new Dimension(1000, 600));
        absentPanel1.setBackground(Color.gray);

        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(new MigLayout("", "10[]10", "10[]10"));

        JPanel jpanel1 = new JPanel(new MigLayout("", "0[]10[]10[]10[]0", "0[]0"));
        jpanel1.setBackground(new Color(227, 242, 250));
        jpanel1.setPreferredSize(new Dimension(860, 30));

        JLabel titleLateDate = new JLabel();
        JLabel titleLateShift = new JLabel();
        JLabel titleLateReason = new JLabel();
        JLabel titleLateStatus = new JLabel();

        titleLateDate.setText("Ngày nghỉ có phép");
        titleLateShift.setText("Ca nghỉ");
        titleLateReason.setText("Lý do");
        titleLateStatus.setText("Trạng thái");

        titleLateDate.setPreferredSize(new Dimension(200, 30));
        titleLateDate.setFont((new Font("Inter", Font.BOLD, 13)));

        titleLateShift.setPreferredSize(new Dimension(200, 30));
        titleLateShift.setFont((new Font("Inter", Font.BOLD, 13)));

        titleLateReason.setPreferredSize(new Dimension(200, 30));
        titleLateReason.setFont((new Font("Inter", Font.BOLD, 13)));

        titleLateStatus.setPreferredSize(new Dimension(200, 30));
        titleLateStatus.setFont((new Font("Inter", Font.BOLD, 13)));

        jpanel1.add(titleLateDate);
        jpanel1.add(titleLateShift);
        jpanel1.add(titleLateReason);
        jpanel1.add(titleLateStatus, "wrap");
        panel.add(jpanel1, "wrap");

        for (List<String> list : absentObjectList1) {
            JPanel jPanelLate = new JPanel(new MigLayout("", "0[]10[]10[]10[]0", "0[]0"));
            jPanelLate.setBackground(new Color(255, 255, 255));
            jPanelLate.setPreferredSize(new Dimension(860, 50));

            JTextField jLabelLateDate = new MyTextFieldUnderLine();
            jLabelLateDate.setText(list.get(0));
            jLabelLateDate.setPreferredSize(new Dimension(200, 30));
            jLabelLateDate.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelLateDate.setEditable(false);
            jPanelLate.add(jLabelLateDate);

            JTextField jLabelLateShift = new MyTextFieldUnderLine();
            jLabelLateShift.setText(list.get(1));
            jLabelLateShift.setPreferredSize(new Dimension(200, 30));
            jLabelLateShift.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelLateShift.setEditable(false);
            jPanelLate.add(jLabelLateShift);

            JTextField jLabelLateReason = new MyTextFieldUnderLine();
            jLabelLateReason.setText(list.get(2));
            jLabelLateReason.setPreferredSize(new Dimension(200, 30));
            jLabelLateReason.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelLateReason.setEditable(false);
            jPanelLate.add(jLabelLateReason);

            JTextField jLabelLateStatus = new MyTextFieldUnderLine();
            jLabelLateStatus.setText(list.get(3));
            jLabelLateStatus.setPreferredSize(new Dimension(200, 30));
            jLabelLateStatus.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelLateStatus.setEditable(false);
            jPanelLate.add(jLabelLateStatus);

            panel.add(jPanelLate, "wrap");
        }

        absentPanel1.setViewportView(panel);
    }

    private void initEarlyPanel(JScrollPane earlyPanel) {
        earlyPanel.setPreferredSize(new Dimension(1000, 600));
        earlyPanel.setBackground(Color.gray);

        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(new MigLayout("", "10[]10", "10[]10"));

        JPanel jpanel1 = new JPanel(new MigLayout("", "0[]10[]10[]10[]0", "0[]0"));
        jpanel1.setBackground(new Color(227, 242, 250));
        jpanel1.setPreferredSize(new Dimension(860, 30));

        JLabel titleLateDate = new JLabel();
        JLabel titleLateShift = new JLabel();
        JLabel titleLateCheckout = new JLabel();
        JLabel titleLateTotal = new JLabel();

        titleLateDate.setText("Ngày về sớm");
        titleLateShift.setText("Ca làm");
        titleLateCheckout.setText("Giờ ra ca");
        titleLateTotal.setText("Tổng tiền giảm trừ");

        titleLateDate.setPreferredSize(new Dimension(200, 30));
        titleLateDate.setFont((new Font("Inter", Font.BOLD, 13)));

        titleLateShift.setPreferredSize(new Dimension(200, 30));
        titleLateShift.setFont((new Font("Inter", Font.BOLD, 13)));

        titleLateCheckout.setPreferredSize(new Dimension(200, 30));
        titleLateCheckout.setFont((new Font("Inter", Font.BOLD, 13)));

        titleLateTotal.setPreferredSize(new Dimension(200, 30));
        titleLateTotal.setFont((new Font("Inter", Font.BOLD, 13)));

        jpanel1.add(titleLateDate);
        jpanel1.add(titleLateShift);
        jpanel1.add(titleLateCheckout);
        jpanel1.add(titleLateTotal, "wrap");
        panel.add(jpanel1, "wrap");

        for (List<String> list : earlyObjectList) {
            JPanel jPanelLate = new JPanel(new MigLayout("", "0[]10[]10[]10[]0", "0[]0"));
            jPanelLate.setBackground(new Color(255, 255, 255));
            jPanelLate.setPreferredSize(new Dimension(860, 50));

            JTextField jLabelLateDate = new MyTextFieldUnderLine();
            jLabelLateDate.setText(list.get(0));
            jLabelLateDate.setPreferredSize(new Dimension(200, 30));
            jLabelLateDate.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelLateDate.setEditable(false);
            jPanelLate.add(jLabelLateDate);

            JTextField jLabelLateShift = new MyTextFieldUnderLine();
            jLabelLateShift.setText(list.get(1));
            jLabelLateShift.setPreferredSize(new Dimension(200, 30));
            jLabelLateShift.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelLateShift.setEditable(false);
            jPanelLate.add(jLabelLateShift);

            JTextField jLabelLateCheckout = new MyTextFieldUnderLine();
            jLabelLateCheckout.setText(list.get(2));
            jLabelLateCheckout.setPreferredSize(new Dimension(200, 30));
            jLabelLateCheckout.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelLateCheckout.setEditable(false);
            jPanelLate.add(jLabelLateCheckout);

            JTextField jLabelLateTotal = new MyTextFieldUnderLine();
            jLabelLateTotal.setText(list.get(3));
            jLabelLateTotal.setPreferredSize(new Dimension(200, 30));
            jLabelLateTotal.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelLateTotal.setEditable(false);
            jPanelLate.add(jLabelLateTotal);

            panel.add(jPanelLate, "wrap");
        }

        earlyPanel.setViewportView(panel);
    }

    private void initLatePanel(JScrollPane latePanel) {
        latePanel.setPreferredSize(new Dimension(1000, 600));
        latePanel.setBackground(Color.gray);

        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(new MigLayout("", "10[]10", "10[]10"));

        JPanel jpanel1 = new JPanel(new MigLayout("", "0[]10[]10[]10[]0", "0[]0"));
        jpanel1.setBackground(new Color(227, 242, 250));
        jpanel1.setPreferredSize(new Dimension(860, 30));

        JLabel titleLateDate = new JLabel();
        JLabel titleLateShift = new JLabel();
        JLabel titleLateCheckin = new JLabel();
        JLabel titleLateTotal = new JLabel();

        titleLateDate.setText("Ngày đi trễ");
        titleLateShift.setText("Ca làm");
        titleLateCheckin.setText("Giờ vào ca");
        titleLateTotal.setText("Tổng tiền giảm trừ");

        titleLateDate.setPreferredSize(new Dimension(200, 30));
        titleLateDate.setFont((new Font("Inter", Font.BOLD, 13)));

        titleLateShift.setPreferredSize(new Dimension(200, 30));
        titleLateShift.setFont((new Font("Inter", Font.BOLD, 13)));

        titleLateCheckin.setPreferredSize(new Dimension(200, 30));
        titleLateCheckin.setFont((new Font("Inter", Font.BOLD, 13)));

        titleLateTotal.setPreferredSize(new Dimension(200, 30));
        titleLateTotal.setFont((new Font("Inter", Font.BOLD, 13)));

        jpanel1.add(titleLateDate);
        jpanel1.add(titleLateShift);
        jpanel1.add(titleLateCheckin);
        jpanel1.add(titleLateTotal, "wrap");
        panel.add(jpanel1, "wrap");

        for (List<String> list : lateObjectList) {
            JPanel jPanelLate = new JPanel(new MigLayout("", "0[]10[]10[]10[]0", "0[]0"));
            jPanelLate.setBackground(new Color(255, 255, 255));
            jPanelLate.setPreferredSize(new Dimension(860, 50));

            JTextField jLabelLateDate = new MyTextFieldUnderLine();
            jLabelLateDate.setText(list.get(0));
            jLabelLateDate.setPreferredSize(new Dimension(200, 30));
            jLabelLateDate.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelLateDate.setEditable(false);
            jPanelLate.add(jLabelLateDate);

            JTextField jLabelLateShift = new MyTextFieldUnderLine();
            jLabelLateShift.setText(list.get(1));
            jLabelLateShift.setPreferredSize(new Dimension(200, 30));
            jLabelLateShift.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelLateShift.setEditable(false);
            jPanelLate.add(jLabelLateShift);

            JTextField jLabelLateCheckin = new MyTextFieldUnderLine();
            jLabelLateCheckin.setText(list.get(2));
            jLabelLateCheckin.setPreferredSize(new Dimension(200, 30));
            jLabelLateCheckin.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelLateCheckin.setEditable(false);
            jPanelLate.add(jLabelLateCheckin);

            JTextField jLabelLateTotal = new MyTextFieldUnderLine();
            jLabelLateTotal.setText(list.get(3));
            jLabelLateTotal.setPreferredSize(new Dimension(200, 30));
            jLabelLateTotal.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelLateTotal.setEditable(false);
            jPanelLate.add(jLabelLateTotal);

            panel.add(jPanelLate, "wrap");
        }

        latePanel.setViewportView(panel);
    }

    private void initAllowancePanel(JScrollPane allowancePanel) {
        allowancePanel.setPreferredSize(new Dimension(1000, 600));
        allowancePanel.setBackground(Color.gray);

        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(new MigLayout("", "10[]10", "10[]10"));

        JPanel jpanel1 = new JPanel(new MigLayout("", "0[]10[]10[]10[]10[]0", "0[]0"));
        jpanel1.setBackground(new Color(227, 242, 250));
        jpanel1.setPreferredSize(new Dimension(860, 30));

        JLabel titleAllowanceName = new JLabel();
        JLabel titleAllowanceType = new JLabel();
        JLabel titleAllowanceAmount = new JLabel();
        JLabel titleAllowanceQuantity = new JLabel();
        JLabel titleAllowanceTotal = new JLabel();

        titleAllowanceName.setText("Tên phụ cấp");
        titleAllowanceType.setText("Loại phụ cấp");
        titleAllowanceAmount.setText("Số tiền phụ cấp");
        titleAllowanceQuantity.setText("Số lượng");
        titleAllowanceTotal.setText("Tổng tiền phụ cấp");

        titleAllowanceName.setPreferredSize(new Dimension(200, 30));
        titleAllowanceName.setFont((new Font("Inter", Font.BOLD, 13)));

        titleAllowanceType.setPreferredSize(new Dimension(200, 30));
        titleAllowanceType.setFont((new Font("Inter", Font.BOLD, 13)));

        titleAllowanceAmount.setPreferredSize(new Dimension(200, 30));
        titleAllowanceAmount.setFont((new Font("Inter", Font.BOLD, 13)));

        titleAllowanceQuantity.setPreferredSize(new Dimension(200, 30));
        titleAllowanceQuantity.setFont((new Font("Inter", Font.BOLD, 13)));

        titleAllowanceTotal.setPreferredSize(new Dimension(200, 30));
        titleAllowanceTotal.setFont((new Font("Inter", Font.BOLD, 13)));

        jpanel1.add(titleAllowanceName);
        jpanel1.add(titleAllowanceType);
        jpanel1.add(titleAllowanceAmount);
        jpanel1.add(titleAllowanceQuantity);
        jpanel1.add(titleAllowanceTotal, "wrap");
        panel.add(jpanel1, "wrap");

        for (List<String> list : allowanceObjectList) {
            JPanel jPanelAllowance = new JPanel(new MigLayout("", "0[]10[]10[]10[]10[]0", "0[]0"));
            jPanelAllowance.setBackground(new Color(255, 255, 255));
            jPanelAllowance.setPreferredSize(new Dimension(860, 50));

            JTextField jLabelAllowanceName = new MyTextFieldUnderLine();
            jLabelAllowanceName.setText(list.get(0));
            jLabelAllowanceName.setPreferredSize(new Dimension(200, 30));
            jLabelAllowanceName.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelAllowanceName.setEditable(false);
            jPanelAllowance.add(jLabelAllowanceName);

            JTextField jLabelAllowanceType = new MyTextFieldUnderLine();
            jLabelAllowanceType.setText(list.get(1));
            jLabelAllowanceType.setPreferredSize(new Dimension(200, 30));
            jLabelAllowanceType.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelAllowanceType.setEditable(false);
            jPanelAllowance.add(jLabelAllowanceType);

            JTextField jLabelAllowanceAmount = new MyTextFieldUnderLine();
            jLabelAllowanceAmount.setText(list.get(2));
            jLabelAllowanceAmount.setPreferredSize(new Dimension(200, 30));
            jLabelAllowanceAmount.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelAllowanceAmount.setEditable(false);
            jPanelAllowance.add(jLabelAllowanceAmount);

            JTextField jLabelAllowanceQuantity = new MyTextFieldUnderLine();
            jLabelAllowanceQuantity.setText(list.get(3));
            jLabelAllowanceQuantity.setPreferredSize(new Dimension(200, 30));
            jLabelAllowanceQuantity.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelAllowanceQuantity.setEditable(false);
            jPanelAllowance.add(jLabelAllowanceQuantity);

            JTextField jLabelAllowanceTotal = new MyTextFieldUnderLine();
            jLabelAllowanceTotal.setText(list.get(4));
            jLabelAllowanceTotal.setPreferredSize(new Dimension(200, 30));
            jLabelAllowanceTotal.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelAllowanceTotal.setEditable(false);
            jPanelAllowance.add(jLabelAllowanceTotal);

            panel.add(jPanelAllowance, "wrap");
        }

        allowancePanel.setViewportView(panel);
    }

    private void initInfoPanel(JPanel infoPanel) {
        infoPanel.setLayout(new MigLayout("", "20[]20", "20[]20"));
        infoPanel.setPreferredSize(new Dimension(1000, 600));
        infoPanel.setBackground(Color.white);

        for (String string : new String[]{"Mã nhân viên", "Họ tên", "Chức vụ", "Lương chính thức", "Tổng phụ cấp", "Tổng giảm trừ", "Tổng thưởng", "Tổng phạt vi phạm", "Giờ công thực tế", "Thực lãnh", "Trạng thái"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 15)));
            infoPanel.add(label);

            JTextField textField = new MyTextFieldUnderLine();
            textField.setEditable(false);
            textField.setPreferredSize(new Dimension(280, 30));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            Role_Detail role_detail = new Role_DetailBLL().searchRole_detailsByStaff(payrollDetail.getStaff_id()).get(0);
            if (string.equals("Mã nhân viên")) {
                textField.setText(String.valueOf(payrollDetail.getStaff_id()));
                infoPanel.add(textField);
                continue;
            }
            if (string.equals("Họ tên")) {
                Staff staff = new StaffBLL().searchStaffs("id = " + payrollDetail.getStaff_id()).get(0);
                textField.setText(staff.getName());
                infoPanel.add(textField, "wrap");
                continue;
            }
            if (string.equals("Chức vụ")) {
                Role role = new RoleBLL().searchRoles("id = " + role_detail.getRole_id()).get(0);
                textField.setText(role.getName());
                infoPanel.add(textField);
                continue;
            }
            if (string.equals("Lương chính thức")) {

                textField.setText(VNString.currency(role_detail.getSalary()) + (role_detail.getType_salary() == 1 ? " /kỳ lương" : " /giờ"));
                infoPanel.add(textField, "wrap");
                continue;
            }
            if (string.equals("Giờ công thực tế")) {
                if (role_detail.getType_salary() == 2)
                    textField.setText(String.format("%.2f", payrollDetail.getHours_amount()) + " giờ");
                else
                    textField.setText(String.valueOf(payrollDetail.getHours_amount()).split("\\.")[0] + " ngày");
                infoPanel.add(textField);
                continue;
            }
            if (string.equals("Tổng phụ cấp")) {
                textField.setText(VNString.currency(payrollDetail.getAllowance_amount()));
                infoPanel.add(textField);
                continue;
            }
            if (string.equals("Tổng giảm trừ")) {
                textField.setText(VNString.currency(payrollDetail.getDeduction_amount()));
                infoPanel.add(textField, "wrap");
                continue;
            }
            if (string.equals("Tổng thưởng")) {
                textField.setText(VNString.currency(payrollDetail.getBonus_amount()));
                infoPanel.add(textField);
                continue;
            }
            if (string.equals("Tổng phạt vi phạm")) {
                textField.setText(VNString.currency(payrollDetail.getFine_amount()));
                infoPanel.add(textField, "wrap");
                continue;
            }
            if (string.equals("Thực lãnh")) {
                textField.setText(VNString.currency(payrollDetail.getSalary_amount()));
                infoPanel.add(textField, "wrap");
                continue;
            }
            if (payrollDetail.isStatus())
                textField.setForeground(new Color(0x009F00));
            textField.setText(payrollDetail.isStatus() ? "Đã trả lương ✔" : "Tạm tính");
            infoPanel.add(textField, "wrap");
        }

    }
}
