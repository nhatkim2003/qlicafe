package com.coffee.GUI;

import com.coffee.BLL.RoleBLL;

import com.coffee.DTO.*;
import com.coffee.GUI.DialogGUI.FormAddGUI.AddWorkScheduleGUI;
import com.coffee.GUI.components.*;
import com.coffee.ImportExcel.AddWorkScheduleFromExcel;
import com.coffee.utils.PDF;
import com.coffee.ImportExcel.Timekeeping;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;


import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import static com.coffee.utils.Resource.chooseExcelFile;

public class CreateWorkScheduleGUI extends Layout1 {
    private JLabel jLabelDateWork;
    private JLabel iconNext;
    private JLabel iconPrev;
    private List<Function> functions;
    private WorkSchedulePanel workSchedulePanel;
    private boolean edit = false;
    private boolean remove = false;
    private Date today;
    private List<Date> weekDates;

    public CreateWorkScheduleGUI() {
        super();
        today = java.sql.Date.valueOf(LocalDate.now());
        init();
    }

    private void init() {
        SearchPanel.setPreferredSize(new Dimension(400, 50));
        FunctionPanel.setPreferredSize(new Dimension(750, 50));
        jLabelDateWork = new JLabel();
        iconNext = new JLabel(new FlatSVGIcon("icon/next.svg"));
        iconPrev = new JLabel(new FlatSVGIcon("icon/previous.svg"));
        weekDates = new ArrayList<>();
        workSchedulePanel = new WorkSchedulePanel();
        JPanel ContentPanel = new JPanel();
        JPanel jPanelDate = new JPanel();

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
        jPanelDate.setPreferredSize(new Dimension(200, 40));
        SearchPanel.add(jPanelDate);

        jLabelDateWork.setFont(new Font("Lexend", Font.BOLD, 15));
        jLabelDateWork.setHorizontalAlignment(JLabel.CENTER);
        jPanelDate.add(jLabelDateWork, BorderLayout.CENTER);
        loadDate();

        iconNext.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iconNext.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                next();
            }
        });
        SearchPanel.add(iconNext);

        RoundedPanel refreshPanel = new RoundedPanel();
        refreshPanel.setLayout(new GridBagLayout());
        refreshPanel.setPreferredSize(new Dimension(130, 40));
        refreshPanel.setBackground(new Color(1, 120, 220));
        refreshPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                refresh();
            }
        });
        FunctionPanel.add(refreshPanel);

        JLabel refreshLabel = new JLabel("Làm mới");
        refreshLabel.setFont(new Font("Public Sans", Font.PLAIN, 13));
        refreshLabel.setForeground(Color.white);
        refreshLabel.setIcon(new FlatSVGIcon("icon/refresh.svg"));
        refreshPanel.add(refreshLabel);

        RoundedPanel roundedPanelAdd = new RoundedPanel();
        roundedPanelAdd.setLayout(new GridBagLayout());
        roundedPanelAdd.setPreferredSize(new Dimension(130, 40));
        roundedPanelAdd.setBackground(new Color(1, 120, 220));
        roundedPanelAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        roundedPanelAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new AddWorkScheduleGUI();
                refresh();
            }
        });
        FunctionPanel.add(roundedPanelAdd);
        JLabel panelAdd = new JLabel("Thêm mới");
        panelAdd.setForeground(Color.white);
        panelAdd.setFont(new Font("Public Sans", Font.PLAIN, 13));
        panelAdd.setIcon(new FlatSVGIcon("icon/add.svg"));
        roundedPanelAdd.add(panelAdd);

        RoundedPanel roundedPanelExcel = new RoundedPanel();
        roundedPanelExcel.setLayout(new GridBagLayout());
        roundedPanelExcel.setPreferredSize(new Dimension(130, 40));
        roundedPanelExcel.setBackground(new Color(1, 120, 220));
        roundedPanelExcel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        FunctionPanel.add(roundedPanelExcel);

        JLabel panelExcel = new JLabel("Nhập Excel");
        panelExcel.setForeground(Color.white);
        panelExcel.setFont(new Font("Public Sans", Font.PLAIN, 13));
        panelExcel.setIcon(new FlatSVGIcon("icon/import.svg"));
        roundedPanelExcel.add(panelExcel);
        roundedPanelExcel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                File file = chooseExcelFile(null);
                if (file != null) {
                    Pair<Boolean, String> result;
                    try {
                        result = new AddWorkScheduleFromExcel().addWorkScheduleFromExcel(file);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (!result.getKey()) {
                        JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Thêm lịch làm việc thành công",
                                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        refresh();
                    }
                }
            }
        });

        RoundedPanel roundedPanelPDF = new RoundedPanel();
        roundedPanelPDF.setLayout(new GridBagLayout());
        roundedPanelPDF.setPreferredSize(new Dimension(130, 40));
        roundedPanelPDF.setBackground(new Color(1, 120, 220));
        roundedPanelPDF.setCursor(new Cursor(Cursor.HAND_CURSOR));

        FunctionPanel.add(roundedPanelPDF);

        JLabel panelPDF = new JLabel("Xuất PDF");
        panelPDF.setForeground(Color.white);
        panelPDF.setFont(new Font("Public Sans", Font.PLAIN, 13));
        panelPDF.setIcon(new FlatSVGIcon("icon/export.svg"));
        roundedPanelPDF.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                PDF.exportWorkSchedulePDF(weekDates.get(0), weekDates.get(weekDates.size() - 1), "src/main/resources/ExportPDF");
                JOptionPane.showMessageDialog(null, "Xuất PDF lịch làm việc thành công.",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        roundedPanelPDF.add(panelPDF);

        RoundedPanel roundedPaneChamCong = new RoundedPanel();
        roundedPaneChamCong.setLayout(new GridBagLayout());
        roundedPaneChamCong.setPreferredSize(new Dimension(150, 40));
        roundedPaneChamCong.setBackground(new Color(1, 120, 220));
        roundedPaneChamCong.setCursor(new Cursor(Cursor.HAND_CURSOR));
        FunctionPanel.add(roundedPaneChamCong);

        JLabel panelChamCong = new JLabel("Chấm Công Excel");
        panelChamCong.setForeground(Color.white);
        panelChamCong.setFont(new Font("Public Sans", Font.PLAIN, 13));
        panelChamCong.setIcon(new FlatSVGIcon("icon/write-svgrepo-com.svg"));
        roundedPaneChamCong.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                File file = chooseExcelFile(null);
                if (file != null) {
                    Pair<Boolean, String> result;
                    try {
                        result = new Timekeeping().addWorkChamCongFromExcel(file);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (!result.getKey()) {
                        JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Chấm công thành công",
                                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        refresh();
                    }
                }
            }
        });
        roundedPaneChamCong.add(panelChamCong);

        ContentPanel.setPreferredSize(new Dimension(1165, 680));
        ContentPanel.setBackground(new Color(255, 255, 255));
        ContentPanel.setLayout(new BorderLayout());
        bottom.add(ContentPanel, BorderLayout.CENTER);

        JPanel jPanelNotice = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jPanelNotice.setBackground(new Color(255, 255, 255));

        JLabel jLabelAvailable = new JLabel("Chưa chấm công");
        JPanel jPanelAvailable = new JPanel();
        jPanelAvailable.setPreferredSize(new Dimension(10, 10));
        jPanelAvailable.setBackground(new Color(0x88FD8F8F, true));

        jPanelNotice.add(jPanelAvailable);
        jPanelNotice.add(jLabelAvailable);

        JLabel jLabelDone = new JLabel("Đã chấm công");
        JPanel jPanelDone = new JPanel();
        jPanelDone.setPreferredSize(new Dimension(10, 10));
        jPanelDone.setBackground(new Color(0x9162A968, true));

        jPanelNotice.add(jPanelDone);
        jPanelNotice.add(jLabelDone);

        JLabel jLabelAbsent = new JLabel("Nghỉ có phép");
        JPanel jPanelAbsent = new JPanel();
        jPanelAbsent.setPreferredSize(new Dimension(10, 10));
        jPanelAbsent.setBackground(new Color(0x915D5C5C, true));

        jPanelNotice.add(jPanelAbsent);
        jPanelNotice.add(jLabelAbsent);

        ContentPanel.add(jPanelNotice, BorderLayout.NORTH);

        workSchedulePanel = new WorkSchedulePanel();
        ContentPanel.add(workSchedulePanel, BorderLayout.CENTER);

//        TitlePanelManager.setLayout(new FlowLayout(FlowLayout.LEFT));
//        TitlePanelManager.setBackground(Color.white);
//        bottom.add(TitlePanelManager);
//
//        jLabelTitleManager.setFont(new Font("Lexend", Font.BOLD, 15));
//        TitlePanelManager.add(jLabelTitleManager);
//
//        ContentPanelManager.setLayout(new BorderLayout());
//        ContentPanelManager.setPreferredSize(new Dimension(1160, 185));
//        ContentPanelManager.setBackground(Color.white);
//        bottom.add(ContentPanelManager);
//
//        ContentPanelManager.add(workSchedulePanelManager);
//
//        TitlePanelStaffSale.setLayout(new FlowLayout(FlowLayout.LEFT));
//        TitlePanelStaffSale.setBackground(Color.white);
//        bottom.add(TitlePanelStaffSale);
//
//        jLabelTitleSale.setFont(new Font("Lexend", Font.BOLD, 15));
//        TitlePanelStaffSale.add(jLabelTitleSale);
//
//        ContentPanelStaffSale.setLayout(new BorderLayout());
//        ContentPanelStaffSale.setPreferredSize(new Dimension(1160, 185));
//        ContentPanelStaffSale.setBackground(Color.white);
//        bottom.add(ContentPanelStaffSale);
//
//        ContentPanelStaffSale.add(workSchedulePanelStaffSale);
//
//        TitlePanelStaffWarehouse.setLayout(new FlowLayout(FlowLayout.LEFT));
//        TitlePanelStaffWarehouse.setBackground(Color.white);
//        bottom.add(TitlePanelStaffWarehouse);
//
//        jLabelTitleWarehouse.setFont(new Font("Lexend", Font.BOLD, 15));
//        TitlePanelStaffWarehouse.add(jLabelTitleWarehouse);
//
//        ContentPanelStaffWarehouse.setLayout(new BorderLayout());
//        ContentPanelStaffWarehouse.setPreferredSize(new Dimension(1160, 185));
//        ContentPanelStaffWarehouse.setBackground(Color.white);
//        bottom.add(ContentPanelStaffWarehouse);
//
//        ContentPanelStaffWarehouse.add(workSchedulePanelStaffWarehouse);

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

    public void loadDate() {
        weekDates = getWeekDatesContaining(today);
        String date = new SimpleDateFormat("dd/MM/yyyy").format(weekDates.get(0)) +
                " - " +
                new SimpleDateFormat("dd/MM/yyyy").format(weekDates.get(weekDates.size() - 1));
        jLabelDateWork.setText(date);
    }

    public void refresh() {
        today = java.sql.Date.valueOf(LocalDate.now());
        loadDate();
        workSchedulePanel.showWorkSchedule(weekDates.get(0), weekDates.get(weekDates.size() - 1));

    }

    private void prev() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        today = calendar.getTime();

        loadDate();
        workSchedulePanel.showWorkSchedule(weekDates.get(0), weekDates.get(weekDates.size() - 1));
    }

    private void next() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        today = calendar.getTime();

        loadDate();
        workSchedulePanel.showWorkSchedule(weekDates.get(0), weekDates.get(weekDates.size() - 1));

    }

}
