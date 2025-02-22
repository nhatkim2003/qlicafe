package com.coffee.GUI;

import com.coffee.DTO.Staff;
import com.coffee.GUI.components.MyWorkSchedulePanel;
import com.coffee.GUI.components.RoundedPanel;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyWorkScheduleGUI extends RoundedPanel {
    private JLabel jLabelDate;
    private JLabel iconNext;
    private JLabel iconPrev;
    private MyWorkSchedulePanel myWorkSchedulePanel;
    private Staff staff;
    private Date today;
    private List<Date> weekDates;

    public MyWorkScheduleGUI(Staff staff) {
        this.staff = staff;
        today = java.sql.Date.valueOf(LocalDate.now());
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setBackground(new Color(255, 255, 255));
        setLayout(new MigLayout("", "0[]0"));
        setPreferredSize(new Dimension(1165, 733));

        jLabelDate = new JLabel();
        iconNext = new JLabel(new FlatSVGIcon("icon/next.svg"));
        iconPrev = new JLabel(new FlatSVGIcon("icon/previous.svg"));
        myWorkSchedulePanel = new MyWorkSchedulePanel(staff);
        weekDates = new ArrayList<>();
        RoundedPanel TitlePanel = new RoundedPanel();
        JPanel SearchPanel = new JPanel();
        RoundedPanel ContentPanel = new RoundedPanel();
        JPanel jPanelDate = new JPanel();
        JLabel jLabelTitle = new JLabel("    Thông Tin Ca Làm Việc");


        TitlePanel.setLayout(new BorderLayout());
        TitlePanel.setPreferredSize(new Dimension(1160, 50));
        TitlePanel.setBackground(new Color(255, 255, 255));
        add(TitlePanel, "wrap");

        jLabelTitle.setFont(new Font("Lexend", Font.BOLD, 18));
        TitlePanel.add(jLabelTitle, BorderLayout.WEST);

        SearchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        SearchPanel.setPreferredSize(new Dimension(1170, 50));
        SearchPanel.setBackground(new Color(191, 198, 208));
        add(SearchPanel, "wrap");

        iconPrev.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iconPrev.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prev();
            }
        });
        SearchPanel.add(iconPrev);

        jPanelDate.setLayout(new BorderLayout());
        jPanelDate.setBackground(Color.white);
        jPanelDate.setPreferredSize(new Dimension(350, 40));
        SearchPanel.add(jPanelDate);

        jLabelDate.setFont(new Font("Lexend", Font.BOLD, 15));
        jLabelDate.setHorizontalAlignment(JLabel.CENTER);
        jPanelDate.add(jLabelDate, BorderLayout.CENTER);
        loadDate();

        iconNext.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iconNext.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                next();
            }
        });
        SearchPanel.add(iconNext);

        ContentPanel.setLayout(new BorderLayout());
        ContentPanel.setPreferredSize(new Dimension(1160, 300));
        ContentPanel.setBackground(new Color(255, 255, 255));
        add(ContentPanel, "wrap");

        ContentPanel.add(myWorkSchedulePanel);
    }

    public static List<Date> getWeekDatesContaining(Date date) {
        List<Date> weekDates = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

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

    private void loadDate() {
        weekDates = getWeekDatesContaining(today);
        String date = new SimpleDateFormat("dd/MM/yyyy").format(weekDates.get(0)) +
                " - " +
                new SimpleDateFormat("dd/MM/yyyy").format(weekDates.get(weekDates.size() - 1));
        jLabelDate.setText(date);
    }

    private void prev() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        today = calendar.getTime();

        loadDate();
        myWorkSchedulePanel.showWorkSchedule(weekDates.get(0), weekDates.get(weekDates.size() - 1));
    }

    private void next() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        today = calendar.getTime();

        loadDate();
        myWorkSchedulePanel.showWorkSchedule(weekDates.get(0), weekDates.get(weekDates.size() - 1));
    }
}
