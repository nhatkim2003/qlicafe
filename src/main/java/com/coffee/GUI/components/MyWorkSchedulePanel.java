package com.coffee.GUI.components;

import com.coffee.BLL.Work_ScheduleBLL;
import com.coffee.DTO.Staff;
import com.coffee.DTO.Work_Schedule;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class MyWorkSchedulePanel extends JScrollPane {
    private final Staff staff;
    private final List<String> shifts;
    private RoundedPanel panel;
    private List<JLabel> jLabelsDay;
    private final JPanel[][] roundedPanels;

    public MyWorkSchedulePanel(Staff staff) {
        this.staff = staff;

        shifts = new ArrayList<>();
        shifts.add("Shift 1: 6h - 12h");
        shifts.add("Shift 2: 12h - 18h");
        shifts.add("Shift 3: 18h - 23h");
        roundedPanels = new JPanel[shifts.size()][7];
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setBackground(Color.white);


        panel = new RoundedPanel();
        jLabelsDay = new ArrayList<>();

        panel.setLayout(new GridLayout(shifts.size() + 1, 7 + 1));
        panel.setBackground(Color.white);

        int i, j;
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
        for (i = 0; i < shifts.size(); i++) {
            String moduleName = shifts.get(i);
            JLabel jLabel = new JLabel(moduleName);
            jLabel.setBackground(Color.white);
            jLabel.setVerticalAlignment(JLabel.CENTER);
            jLabel.setHorizontalAlignment(JLabel.CENTER);
            jLabel.setFont(new Font("Inter", Font.BOLD, 14));
            jLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
            panel.add(jLabel);
            for (j = 0; j < 7; j++) {
                roundedPanels[i][j] = new JPanel();
                roundedPanels[i][j].setBackground(Color.white);
                roundedPanels[i][j].setBorder(BorderFactory.createLineBorder(Color.black, 1));
                panel.add(roundedPanels[i][j]);
            }
        }

        List<Date> weekDates = getCurrentWeek();
        showWorkSchedule(weekDates.get(0), weekDates.get(6));
        this.setViewportView(panel);
    }

    public void showWorkSchedule(Date date1, Date date2) {
        List<Work_Schedule> work_schedules = new Work_ScheduleBLL().searchWork_schedules("staff_id = " + staff.getId(),
                "date >= '" + new SimpleDateFormat("yyyy-MM-dd").format(date1) + "'",
                "date <= '" + new SimpleDateFormat("yyyy-MM-dd").format(date2) + "'");
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

        for (JPanel[] jPanel : roundedPanels)
            for (JPanel panel1 : jPanel)
                panel1.setBackground(Color.white);

        int i, j;
        for (Work_Schedule work_schedule : work_schedules) {
            i = work_schedule.getShift() - 1;
            j = dates.indexOf(work_schedule.getDate());
            roundedPanels[i][j].setBackground(new Color(242, 164, 164));
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
