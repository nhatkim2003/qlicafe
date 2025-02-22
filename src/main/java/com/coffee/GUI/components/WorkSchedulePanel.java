package com.coffee.GUI.components;


import com.coffee.BLL.Role_DetailBLL;
import com.coffee.BLL.StaffBLL;
import com.coffee.BLL.Work_ScheduleBLL;
import com.coffee.DTO.Role_Detail;
import com.coffee.DTO.Staff;
import com.coffee.DTO.Work_Schedule;
import com.coffee.GUI.DialogGUI.FormEditGUI.EditWorkScheduleGUI;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

public class WorkSchedulePanel extends JScrollPane {
    private final List<String> shifts;
    private JPanel panel;
    private List<JLabel> jLabelsDay;
    private final JPanel[][] roundedPanels;
    private int role_id;

    public WorkSchedulePanel() {
//        this.role_id = role_id;

        shifts = new ArrayList<>();
        shifts.add("Quản Lý");
        shifts.add("Ca 1: 6h - 12h");
        shifts.add("Ca 2: 12h - 18h");
        shifts.add("Ca 3: 18h - 23h");
        shifts.add("Kho");
        shifts.add("Ca 1: 6h - 12h");
        shifts.add("Ca 2: 12h - 18h");
        shifts.add("Ca 3: 18h - 23h");
        shifts.add("Bán Hàng");
        shifts.add("Ca 1: 6h - 12h");
        shifts.add("Ca 2: 12h - 18h");
        shifts.add("Ca 3: 18h - 23h");

        roundedPanels = new JPanel[12][7];
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setBackground(new Color(255, 255, 255));


        panel = new JPanel();
        jLabelsDay = new ArrayList<>();

//        if (role_id == 2)
        panel.setLayout(new GridLayout(shifts.size() + 1, 7 + 1));
//        else
//            panel.setLayout(new GridLayout(shifts.size(), 7 + 1));
        panel.setBackground(Color.white);
//
        int i, j;
//        if (role_id == 2) {
        panel.add(new JLabel());
        for (i = 0; i < 7; i++) {
            JLabel jLabel = new JLabel();
            jLabel.setBackground(Color.white);
            jLabel.setVerticalAlignment(JLabel.CENTER);
            jLabel.setHorizontalAlignment(JLabel.CENTER);
            jLabel.setFont(new Font("Inter", Font.BOLD, 14));
            jLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
            panel.add(jLabel);
            jLabelsDay.add(jLabel);
        }
//        }
//
        for (i = 0; i < shifts.size(); i++) {
            String shift = shifts.get(i);
            JLabel jLabel = new JLabel(shift);
            jLabel.setBackground(Color.white);
            jLabel.setVerticalAlignment(JLabel.CENTER);
            jLabel.setHorizontalAlignment(JLabel.CENTER);
            jLabel.setFont(new Font("Inter", Font.BOLD, 14));
            if (i == 0 || i == 4 || i == 8) {
                JPanel panel1 = new JPanel(new GridBagLayout());
                panel1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
                panel1.setBackground(new Color(228, 231, 235));
                panel1.add(jLabel);
                panel.add(panel1);
            } else {
                jLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
                panel.add(jLabel);
            }
            for (j = 0; j < 7; j++) {
                roundedPanels[i][j] = new JPanel(new MigLayout("", "5[]5", ""));
                roundedPanels[i][j].setBackground(Color.white);
                roundedPanels[i][j].setBorder(BorderFactory.createLineBorder(Color.black, 1));
                panel.add(roundedPanels[i][j]);
            }
        }

        for (int k = 0; k < 7; k++) {
            roundedPanels[0][k].setBackground(new Color(228, 231, 235));
            roundedPanels[4][k].setBackground(new Color(228, 231, 235));
            roundedPanels[8][k].setBackground(new Color(228, 231, 235));
        }

        List<Date> weekDates = getCurrentWeek();
        showWorkSchedule(weekDates.get(0), weekDates.get(6));

        this.setViewportView(panel);
        this.setColumnHeaderView(null);
    }

    public void showWorkSchedule(Date date1, Date date2) {
        List<Integer> staffIDList1 = new ArrayList<>();
        List<Role_Detail> role_detailList1 = new Role_DetailBLL().searchRole_detailsByRole(2, new SimpleDateFormat("yyyy-MM-dd").format(date2));
        for (Role_Detail roleDetail : role_detailList1)
//            if (!staffIDList1.contains(roleDetail.getStaff_id()))
            staffIDList1.add(roleDetail.getStaff_id());

        List<Work_Schedule> work_schedules1 = new ArrayList<>();
        for (Integer id : staffIDList1) {
            work_schedules1.addAll(new Work_ScheduleBLL().searchWork_schedules("staff_id = " + id,
                    "date >= '" + new SimpleDateFormat("yyyy-MM-dd").format(date1) + "'",
                    "date <= '" + new SimpleDateFormat("yyyy-MM-dd").format(date2) + "'"));
        }

        List<Date> dates = getDaysBetween(date1, date2);
        for (int i = 0; i < dates.size(); i++) {
            DateTimeFormatter dtfInput = DateTimeFormatter.ofPattern("u-M-d", Locale.ENGLISH);
            DateTimeFormatter dtfOutput = DateTimeFormatter.ofPattern("EEEE", new Locale("vi", "VN"));
            DateTimeFormatter dtfDayMonth = DateTimeFormatter.ofPattern("d/M", new Locale("vi", "VN"));

            String date = LocalDate.parse(
                            new SimpleDateFormat("yyyy-MM-dd").format(dates.get(i)),
                            dtfInput
                    )
                    .format(dtfOutput) +
                    " (" +
                    LocalDate.parse(
                                    new SimpleDateFormat("yyyy-MM-dd").format(dates.get(i)),
                                    dtfInput
                            )
                            .format(dtfDayMonth) + ")";
            jLabelsDay.get(i).setText(date);
        }


        for (JPanel[] jPanel : roundedPanels) {
            for (JPanel panel1 : jPanel) {
                panel1.removeAll();
                panel1.repaint();
                panel1.revalidate();
            }
        }

        int i, j;
        for (Work_Schedule work_schedule : work_schedules1) {
            i = work_schedule.getShift();
            j = dates.indexOf(work_schedule.getDate());

            Staff staff = new StaffBLL().searchStaffs("id = " + work_schedule.getStaff_id()).get(0);
            JPanel jPanel = new JPanel(new BorderLayout());
            jPanel.setPreferredSize(new Dimension(130, 10));
            jPanel.setBackground(new Color(0x4CA2A2A2, true));
            jPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            jPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    new EditWorkScheduleGUI(work_schedule);
                }
            });

            JLabel jLabelName = new JLabel(staff.getName());
            jLabelName.setFont(new Font("Inter", Font.BOLD, 12));
            jPanel.add(jLabelName, BorderLayout.CENTER);

            if (!work_schedule.getNotice().equals("Không")) {
                JLabel jLabelCheckInOut = new JLabel("Nghỉ có phép");
                jLabelCheckInOut.setFont(new Font("Inter", Font.PLAIN, 10));
                jPanel.add(jLabelCheckInOut, BorderLayout.SOUTH);
            } else {
                if (work_schedule.getCheck_in().equals("null") && work_schedule.getCheck_out().equals("null"))
                    jPanel.setBackground(new Color(0x4CFD8F8F, true));
                else
                    jPanel.setBackground(new Color(0x4C62A968, true));

                String checkin = work_schedule.getCheck_in().equals("null") ? "--" : work_schedule.getCheck_in();
                String checkout = work_schedule.getCheck_out().equals("null") ? "--" : work_schedule.getCheck_out();

                JLabel jLabelCheckInOut = new JLabel(checkin + " " + checkout);
                jLabelCheckInOut.setFont(new Font("Inter", Font.PLAIN, 10));
                jPanel.add(jLabelCheckInOut, BorderLayout.SOUTH);
            }
            roundedPanels[i][j].add(jPanel, "wrap");
            roundedPanels[i][j].repaint();
            roundedPanels[i][j].revalidate();
        }

        // nhan vien kho

        List<Integer> staffIDList2 = new ArrayList<>();
        List<Role_Detail> role_detailList2 = new Role_DetailBLL().searchRole_detailsByRole(3, new SimpleDateFormat("yyyy-MM-dd").format(date2));
        for (Role_Detail roleDetail : role_detailList2)
//            if (!staffIDList2.contains(roleDetail.getStaff_id()))
            staffIDList2.add(roleDetail.getStaff_id());

        List<Work_Schedule> work_schedules2 = new ArrayList<>();
        for (Integer id : staffIDList2) {
            work_schedules2.addAll(new Work_ScheduleBLL().searchWork_schedules("staff_id = " + id,
                    "date >= '" + new SimpleDateFormat("yyyy-MM-dd").format(date1) + "'",
                    "date <= '" + new SimpleDateFormat("yyyy-MM-dd").format(date2) + "'"));
        }
        for (Work_Schedule work_schedule : work_schedules2) {
            i = work_schedule.getShift() + 4;
            j = dates.indexOf(work_schedule.getDate());

            Staff staff = new StaffBLL().searchStaffs("id = " + work_schedule.getStaff_id()).get(0);
            JPanel jPanel = new JPanel(new BorderLayout());
            jPanel.setBackground(new Color(0x4CA2A2A2, true));
            jPanel.setPreferredSize(new Dimension(130, 10));
            jPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            jPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    new EditWorkScheduleGUI(work_schedule);
                }
            });

            JLabel jLabelName = new JLabel(staff.getName());
            jLabelName.setFont(new Font("Inter", Font.BOLD, 12));
            jPanel.add(jLabelName, BorderLayout.CENTER);

            if (!work_schedule.getNotice().equals("Không")) {
                JLabel jLabelCheckInOut = new JLabel("Nghỉ có phép");
                jLabelCheckInOut.setFont(new Font("Inter", Font.PLAIN, 10));
                jPanel.add(jLabelCheckInOut, BorderLayout.SOUTH);
            } else {
                if (work_schedule.getCheck_in().equals("null") && work_schedule.getCheck_out().equals("null"))
                    jPanel.setBackground(new Color(0x4CFD8F8F, true));
                else
                    jPanel.setBackground(new Color(0x4C62A968, true));

                String checkin = work_schedule.getCheck_in().equals("null") ? "--" : work_schedule.getCheck_in();
                String checkout = work_schedule.getCheck_out().equals("null") ? "--" : work_schedule.getCheck_out();

                JLabel jLabelCheckInOut = new JLabel(checkin + " " + checkout);
                jLabelCheckInOut.setFont(new Font("Inter", Font.PLAIN, 10));
                jPanel.add(jLabelCheckInOut, BorderLayout.SOUTH);
            }
            roundedPanels[i][j].add(jPanel, "wrap");
            roundedPanels[i][j].repaint();
            roundedPanels[i][j].revalidate();
        }

        // nhan vien ban hang

        List<Integer> staffIDList3 = new ArrayList<>();
        List<Role_Detail> role_detailList3 = new Role_DetailBLL().searchRole_detailsByRole(4, new SimpleDateFormat("yyyy-MM-dd").format(date2));
        for (Role_Detail roleDetail : role_detailList3)
//            if (!staffIDList3.contains(roleDetail.getStaff_id()))
            staffIDList3.add(roleDetail.getStaff_id());

        List<Work_Schedule> work_schedules3 = new ArrayList<>();
        for (Integer id : staffIDList3) {
            work_schedules3.addAll(new Work_ScheduleBLL().searchWork_schedules("staff_id = " + id,
                    "date >= '" + new SimpleDateFormat("yyyy-MM-dd").format(date1) + "'",
                    "date <= '" + new SimpleDateFormat("yyyy-MM-dd").format(date2) + "'"));
        }
        for (Work_Schedule work_schedule : work_schedules3) {
            i = work_schedule.getShift() + 8;
            j = dates.indexOf(work_schedule.getDate());

            Staff staff = new StaffBLL().searchStaffs("id = " + work_schedule.getStaff_id()).get(0);
            JPanel jPanel = new JPanel(new BorderLayout());
            jPanel.setBackground(new Color(0x4CA2A2A2, true));
            jPanel.setPreferredSize(new Dimension(130, 10));
            jPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            jPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    new EditWorkScheduleGUI(work_schedule);
                }
            });

            JLabel jLabelName = new JLabel(staff.getName());
            jLabelName.setFont(new Font("Inter", Font.BOLD, 12));
            jPanel.add(jLabelName, BorderLayout.CENTER);

            if (!work_schedule.getNotice().equals("Không")) {
                JLabel jLabelCheckInOut = new JLabel("Nghỉ có phép");
                jLabelCheckInOut.setFont(new Font("Inter", Font.PLAIN, 10));
                jPanel.add(jLabelCheckInOut, BorderLayout.SOUTH);
            } else {
                if (work_schedule.getCheck_in().equals("null") && work_schedule.getCheck_out().equals("null"))
                    jPanel.setBackground(new Color(0x4CFD8F8F, true));
                else
                    jPanel.setBackground(new Color(0x4C62A968, true));

                String checkin = work_schedule.getCheck_in().equals("null") ? "--" : work_schedule.getCheck_in();
                String checkout = work_schedule.getCheck_out().equals("null") ? "--" : work_schedule.getCheck_out();

                JLabel jLabelCheckInOut = new JLabel(checkin + " " + checkout);
                jLabelCheckInOut.setFont(new Font("Inter", Font.PLAIN, 10));
                jPanel.add(jLabelCheckInOut, BorderLayout.SOUTH);
            }
            roundedPanels[i][j].add(jPanel, "wrap");
            roundedPanels[i][j].repaint();
            roundedPanels[i][j].revalidate();
        }

    }

    public static List<Date> getDaysBetween(Date start, Date end) {
        List<Date> daysBetween = new ArrayList<>();

        long startTime = start.getTime();
        long endTime = end.getTime();

        long dayInMillis = 1000 * 60 * 60 * 24;

        for (long time = startTime; time <= endTime; time += dayInMillis) {
            daysBetween.add(new Date(time));
        }

        return daysBetween;
    }

    private List<Date> getCurrentWeek() {
        List<Date> weekDates = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(java.sql.Date.valueOf(LocalDate.now()));

        // Tìm ngày đầu tiên của tuần (Thứ Hai)
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }

        // Lặp qua các ngày từ Thứ Hai đến Chủ nhật và thêm vào danh sách
        for (int i = 0; i < 7; i++) {
            weekDates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
        return weekDates;
    }
}
