package com.coffee.GUI.DialogGUI.FormEditGUI;

import com.coffee.BLL.*;

import com.coffee.DTO.*;
import com.coffee.GUI.CreateWorkScheduleGUI;
import com.coffee.GUI.DialogGUI.DialogForm;
import com.coffee.GUI.components.AutocompleteJComboBox;
import com.coffee.GUI.components.DatePicker;
import com.coffee.GUI.components.MyTextFieldUnderLine;

import com.coffee.GUI.components.swing.DataSearch;
import com.coffee.GUI.components.swing.EventClick;
import com.coffee.GUI.components.swing.MyTextField;
import com.coffee.GUI.components.swing.PanelSearch;
import com.coffee.main.Cafe_Application;
import com.coffee.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DateEvent;
import raven.datetime.component.date.DateSelectionListener;
import raven.datetime.component.time.TimePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class EditWorkScheduleGUI extends DialogForm {
    private StaffBLL staffBLL = new StaffBLL();
    private JLabel titleName;
    private List<JLabel> attributeWork_Schedule;
    //    private List<JTextField> jTextFieldWork_Schedule;
    private JButton buttonCancel;
    private JButton buttonEdit;
    private ButtonGroup bgShift;
    private AutocompleteJComboBox combo;
    private Work_ScheduleBLL workScheduleBLL = new Work_ScheduleBLL();
    private Work_Schedule workSchedule;
    private List<String> staffList = new ArrayList<>();
    private DatePicker datePicker;
    private JFormattedTextField editor;
    private JPanel chamCongPanel;
    private JPanel finePanel;
    private JPanel bonusPanel;
    private MyTextField txtSearch;
    private PanelSearch search;
    private JPopupMenu menu;
    private int staff_id = -1;
    private List<Work_Schedule_Fine> work_schedule_fines = new ArrayList<>();
    private List<Work_Schedule_Bonus> work_schedule_bonuses = new ArrayList<>();
    private TimePicker timePickerCheckin;
    private TimePicker timePickerCheckout;

    public EditWorkScheduleGUI(Work_Schedule workSchedule) {
        super();
        super.setTitle("Chấm công");
        super.setSize(new Dimension(800, 600));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        this.workSchedule = workSchedule;
        init(workSchedule);
        menu = new JPopupMenu();
        search = new PanelSearch();
        menu.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
        menu.add(search);
        menu.setFocusable(false);
        search.addEventClick(new EventClick() {
            @Override
            public void itemClick(DataSearch data) {
                menu.setVisible(false);
                txtSearch.setText(data.getText());
                Staff staff = staffBLL.findStaffsBy(Map.of("name", data.getText().split(" - ")[0])).get(0);
                staff_id = staff.getId();
            }

            @Override
            public void itemRemove(Component com, DataSearch data) {
                search.remove(com);
                menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
                if (search.getItemSize() == 0) {
                    menu.setVisible(false);
                }
            }
        });
        setVisible(true);
    }

    private void init(Work_Schedule workSchedule) {
        work_schedule_fines.addAll(new Work_Schedule_FineBLL().searchWork_schedules("work_schedule_id = " + workSchedule.getId()));
        work_schedule_bonuses.addAll(new Work_Schedule_BonusBLL().searchWork_schedules("work_schedule_id = " + workSchedule.getId()));

        titleName = new JLabel();
        attributeWork_Schedule = new ArrayList<>();
//        jTextFieldWork_Schedule = new ArrayList<>();
        buttonCancel = new JButton("Huỷ lịch");
        buttonEdit = new JButton("Cập nhật");
        bgShift = new ButtonGroup();
        content.setLayout(new MigLayout("",
                "20[]20[]20",
                "20[]20[]20[]20[]0"));
        datePicker = new DatePicker();
        editor = new JFormattedTextField();


        titleName.setText("Chấm công");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        for (String string : new String[]{"Nhân viên", "Ngày", "Ca"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeWork_Schedule.add(label);
            content.add(label);

            JTextField textField = new MyTextFieldUnderLine();

            if (string.equals("Nhân viên")) {
                textField.setText(staffBLL.findStaffsBy(Map.of("id", workSchedule.getStaff_id())).get(0).getName());
                textField.setEditable(false);
            }

            if (string.equals("Ngày")) {
                datePicker.setDateSelectionMode(raven.datetime.component.date.DatePicker.DateSelectionMode.SINGLE_DATE_SELECTED);
                datePicker.setEditor(editor);
                datePicker.setCloseAfterSelected(true);
                datePicker.setSelectedDate(workSchedule.getDate());
                datePicker.addDateSelectionListener(new DateSelectionListener() {
                    @Override
                    public void dateSelected(DateEvent dateEvent) {
                        if (datePicker.getDateSQL_Single() != null && datePicker.getDateSQL_Single().compareTo(workSchedule.getDate()) != 0) {
                            for (Enumeration<AbstractButton> buttons = bgShift.getElements(); buttons.hasMoreElements(); ) {
                                AbstractButton button = buttons.nextElement();

                                if (button.isSelected()) {
                                    if (button.getText().contains("1:"))
                                        checkExistByDate(datePicker.getDateSQL_Single(), 1);

                                    else if (button.getText().contains("2:"))
                                        checkExistByDate(datePicker.getDateSQL_Single(), 2);

                                    else if (button.getText().contains("3:"))
                                        checkExistByDate(datePicker.getDateSQL_Single(), 3);
                                }
                            }
                        }
                    }
                });
                editor.setPreferredSize(new Dimension(280, 30));
                content.add(editor, "wrap");
                continue;
            }

            if (string.equals("Ca")) {
                JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                jPanel.setPreferredSize(new Dimension(1000, 30));
                jPanel.setBackground(Color.white);

                JRadioButton radioShift1 = new JRadioButton("1: 6h - 12h");
                radioShift1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (workSchedule.getShift() != 1)
                            checkExistByShift(datePicker.getDateSQL_Single(), 1);
                    }
                });

                JRadioButton radioShift2 = new JRadioButton("2: 12h - 18h");
                radioShift2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (workSchedule.getShift() != 2)
                            checkExistByShift(datePicker.getDateSQL_Single(), 2);
                    }
                });

                JRadioButton radioShift3 = new JRadioButton("3: 18h - 23h");
                radioShift3.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (workSchedule.getShift() != 3)
                            checkExistByShift(datePicker.getDateSQL_Single(), 3);
                    }
                });

                if (workSchedule.getShift() == 1)
                    radioShift1.setSelected(true);

                if (workSchedule.getShift() == 2)
                    radioShift2.setSelected(true);

                if (workSchedule.getShift() == 3)
                    radioShift3.setSelected(true);

                jPanel.add(radioShift1);
                jPanel.add(radioShift2);
                jPanel.add(radioShift3);

                bgShift.add(radioShift1);
                bgShift.add(radioShift2);
                bgShift.add(radioShift3);
                content.add(jPanel, "wrap");
                continue;
            }

            textField.setPreferredSize(new Dimension(280, 30));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
//            jTextFieldWork_Schedule.add(textField);
            content.add(textField, "wrap");

        }

        chamCongPanel = new JPanel();
        finePanel = new JPanel();
        bonusPanel = new JPanel();

        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.setPreferredSize(new Dimension(800, 350));
        jTabbedPane.setBackground(Color.white);
        content.add(jTabbedPane, "wrap, span");
        jTabbedPane.addTab("Chấm công", chamCongPanel);
        jTabbedPane.addTab("Phạt vi phạm", finePanel);
        jTabbedPane.addTab("Thưởng", bonusPanel);

        chamCongPanel.setBackground(new Color(255, 255, 255));
        chamCongPanel.setLayout(new MigLayout("", "0[]20[]", "20[]10[]10[]0"));
        initChanCongPanel();

        finePanel.setBackground(new Color(255, 255, 255));
        finePanel.setLayout(new BorderLayout());
        initFinePanel();

        bonusPanel.setBackground(new Color(255, 255, 255));
        bonusPanel.setLayout(new BorderLayout());
        initBonusPanel();

        buttonCancel.setPreferredSize(new Dimension(100, 30));
        buttonCancel.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                buttonCancel.setBackground(new Color(0xD54218));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonCancel.setBackground(Color.white);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                deleteWorkSchedule();
            }
        });
        containerButton.add(buttonCancel);

        buttonEdit.setPreferredSize(new Dimension(100, 30));
        buttonEdit.setBackground(new Color(1, 120, 220));
        buttonEdit.setForeground(Color.white);
        buttonEdit.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                editWork_Schedule();
            }
        });
        containerButton.add(buttonEdit);
    }

    private boolean checkExistsFine(Work_Schedule_Fine workScheduleFine) {
        for (Work_Schedule_Fine workScheduleFine1 : work_schedule_fines)
            if (workScheduleFine1.getFine_name().equals(workScheduleFine.getFine_name()))
                return true;
        return false;
    }

    private boolean checkExistsBonus(Work_Schedule_Bonus workScheduleBonus) {
        for (Work_Schedule_Bonus workScheduleBonus1 : work_schedule_bonuses)
            if (workScheduleBonus1.getBonus_name().equals(workScheduleBonus.getBonus_name()))
                return true;
        return false;
    }

    private void initBonusPanel() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBackground(Color.cyan);
        scrollPane.setPreferredSize(new Dimension(630, 350));
        bonusPanel.add(scrollPane, BorderLayout.CENTER);

        loadBonusPanel(scrollPane);

    }

    private void loadBonusPanel(JScrollPane scrollPane) {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("", "10[]10", "10[]10"));
        panel.setBackground(new Color(255, 255, 255));
        panel.setPreferredSize(new Dimension(630, 30));

        JTextField jLabelBonusName = new MyTextFieldUnderLine();
        jLabelBonusName.setPreferredSize(new Dimension(230, 30));
        jLabelBonusName.setFont((new Font("Inter", Font.PLAIN, 13)));

        JTextField jLabelBonusQuantity = new MyTextFieldUnderLine();
        jLabelBonusQuantity.setPreferredSize(new Dimension(70, 30));
        jLabelBonusQuantity.setFont((new Font("Inter", Font.PLAIN, 13)));

        JTextField jLabelBonusAmount = new MyTextFieldUnderLine();
        jLabelBonusAmount.setPreferredSize(new Dimension(125, 30));
        jLabelBonusAmount.setFont((new Font("Inter", Font.PLAIN, 13)));

        JTextField jLabelBonusTotal = new MyTextFieldUnderLine();
        jLabelBonusTotal.setPreferredSize(new Dimension(125, 30));
        jLabelBonusTotal.setFont((new Font("Inter", Font.PLAIN, 13)));
        jLabelBonusTotal.setEditable(false);

        jLabelBonusAmount.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        Integer.parseInt(jLabelBonusQuantity.getText());
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "Số lần không hợp lệ!",
                                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    try {
                        Double.parseDouble(jLabelBonusAmount.getText());
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "Mức áp dụng không hợp lệ!",
                                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    Work_Schedule_Bonus workScheduleBonusLast = new Work_Schedule_Bonus();
                    workScheduleBonusLast.setWork_schedule_id(workSchedule.getId());
                    workScheduleBonusLast.setBonus_name(jLabelBonusName.getText());
                    workScheduleBonusLast.setQuantity(Integer.parseInt(jLabelBonusQuantity.getText()));
                    workScheduleBonusLast.setBonus_amount(Double.parseDouble(jLabelBonusAmount.getText()));

                    if (checkExistsBonus(workScheduleBonusLast)) {
                        JOptionPane.showMessageDialog(null, "Đã tồn tại nội dung thưởng!",
                                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        double total = Integer.parseInt(jLabelBonusQuantity.getText()) * Double.parseDouble(jLabelBonusAmount.getText());
                        jLabelBonusTotal.setText(VNString.currency(total));
                        workScheduleBonusLast.setBonus_total(Double.parseDouble(jLabelBonusTotal.getText().replaceAll("\\.", "").split(" ₫")[0]));

                        work_schedule_bonuses.add(workScheduleBonusLast);
                        loadBonusPanel(scrollPane);
                        panel.repaint();
                        panel.revalidate();

                        System.out.println(Arrays.toString(work_schedule_bonuses.toArray()));
                    }

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        JLabel titleBonusName = new JLabel();
        JLabel titleBonusQuantity = new JLabel();
        JLabel titleBonusAmount = new JLabel();
        JLabel titleBonusTotal = new JLabel();

        titleBonusName.setText("Loại thưởng");
        titleBonusQuantity.setText("Số lần");
        titleBonusAmount.setText("Mức áp dụng");
        titleBonusTotal.setText("Thành tiền");

        titleBonusName.setPreferredSize(new Dimension(230, 30));
        titleBonusName.setFont((new Font("Inter", Font.BOLD, 13)));

        titleBonusQuantity.setPreferredSize(new Dimension(70, 30));
        titleBonusQuantity.setFont((new Font("Inter", Font.BOLD, 13)));

        titleBonusAmount.setPreferredSize(new Dimension(125, 30));
        titleBonusAmount.setFont((new Font("Inter", Font.BOLD, 13)));

        titleBonusTotal.setPreferredSize(new Dimension(125, 30));
        titleBonusTotal.setFont((new Font("Inter", Font.BOLD, 13)));

        JPanel jpanel1 = new JPanel(new MigLayout("", "0[]10[]10[]10[]0[]0", "0[]0"));
        jpanel1.setBackground(new Color(227, 242, 250));
        jpanel1.setPreferredSize(new Dimension(630, 30));

        jpanel1.add(titleBonusName);
        jpanel1.add(titleBonusQuantity);
        jpanel1.add(titleBonusAmount);
        jpanel1.add(titleBonusTotal);
        jpanel1.add(new JLabel(), "width 50,wrap");
        panel.add(jpanel1, "wrap");

        for (Work_Schedule_Bonus workScheduleBonus : work_schedule_bonuses) {
            JPanel jPanelBonus = new JPanel(new MigLayout("", "0[]10[]10[]10[]0[]0", "0[]0"));
            jPanelBonus.setBackground(new Color(255, 255, 255));
            jPanelBonus.setPreferredSize(new Dimension(730, 50));

            JTextField jLabelBonusName1 = new MyTextFieldUnderLine();
            jLabelBonusName1.setText(workScheduleBonus.getBonus_name());
            jLabelBonusName1.setPreferredSize(new Dimension(230, 30));
            jLabelBonusName1.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelBonusName1.setEditable(false);

            JTextField jLabelBonusQuantity1 = new MyTextFieldUnderLine();
            jLabelBonusQuantity1.setText(String.valueOf(workScheduleBonus.getQuantity()));
            jLabelBonusQuantity1.setPreferredSize(new Dimension(70, 30));
            jLabelBonusQuantity1.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelBonusQuantity1.setEditable(false);

            JTextField jLabelBonusAmount1 = new MyTextFieldUnderLine();
            jLabelBonusAmount1.setText(VNString.currency(workScheduleBonus.getBonus_amount()));
            jLabelBonusAmount1.setPreferredSize(new Dimension(125, 30));
            jLabelBonusAmount1.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelBonusAmount1.setEditable(false);

            JTextField jLabelBonusTotal1 = new MyTextFieldUnderLine();
            jLabelBonusTotal1.setText(VNString.currency(workScheduleBonus.getBonus_total()));
            jLabelBonusTotal1.setPreferredSize(new Dimension(125, 30));
            jLabelBonusTotal1.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelBonusTotal1.setEditable(false);


            jPanelBonus.add(jLabelBonusName1);
            jPanelBonus.add(jLabelBonusQuantity1);
            jPanelBonus.add(jLabelBonusAmount1);
            jPanelBonus.add(jLabelBonusTotal1);

            JLabel jLabelIconRemove = new JLabel(new FlatSVGIcon("icon/remove.svg"));
            jLabelIconRemove.setCursor(new Cursor(Cursor.HAND_CURSOR));
            jLabelIconRemove.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    work_schedule_bonuses.remove(workScheduleBonus);
                    loadBonusPanel(scrollPane);
                    panel.repaint();
                    panel.revalidate();
                }
            });

            jPanelBonus.add(jLabelIconRemove, "width 50,wrap");
            panel.add(jPanelBonus, "wrap");

        }

        JPanel jPanelBonus = new JPanel(new MigLayout("", "0[]10[]10[]10[]0[]0", "0[]0"));
        jPanelBonus.setBackground(new Color(255, 255, 255));
        jPanelBonus.setPreferredSize(new Dimension(730, 50));
        jPanelBonus.add(jLabelBonusName);
        jPanelBonus.add(jLabelBonusQuantity);
        jPanelBonus.add(jLabelBonusAmount);
        jPanelBonus.add(jLabelBonusTotal);
        jPanelBonus.add(new JLabel(), "width 50,wrap");
        panel.add(jPanelBonus, "wrap");

        JButton buttonAddBonus = new JButton("+ Thêm thưởng");
        buttonAddBonus.setBackground(new Color(0, 182, 62));
        buttonAddBonus.setForeground(Color.WHITE);
        buttonAddBonus.setPreferredSize(new Dimension(150, 30));
//        buttonAddBonus.setIcon(new FlatSVGIcon("icon/add.svg"));
        buttonAddBonus.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAddBonus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer.parseInt(jLabelBonusQuantity.getText());
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Số lần không hợp lệ!",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                try {
                    Double.parseDouble(jLabelBonusAmount.getText());
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Mức áp dụng không hợp lệ!",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                Work_Schedule_Bonus workScheduleBonusLast = new Work_Schedule_Bonus();
                workScheduleBonusLast.setWork_schedule_id(workSchedule.getId());
                workScheduleBonusLast.setBonus_name(jLabelBonusName.getText());
                workScheduleBonusLast.setQuantity(Integer.parseInt(jLabelBonusQuantity.getText()));
                workScheduleBonusLast.setBonus_amount(Double.parseDouble(jLabelBonusAmount.getText()));

                if (checkExistsBonus(workScheduleBonusLast)) {
                    JOptionPane.showMessageDialog(null, "Đã tồn tại nội dung thưởng!",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    double total = Integer.parseInt(jLabelBonusQuantity.getText()) * Double.parseDouble(jLabelBonusAmount.getText());
                    jLabelBonusTotal.setText(VNString.currency(total));
                    workScheduleBonusLast.setBonus_total(Double.parseDouble(jLabelBonusTotal.getText().replaceAll("\\.", "").split(" ₫")[0]));

                    work_schedule_bonuses.add(workScheduleBonusLast);
                    loadBonusPanel(scrollPane);
                    panel.repaint();
                    panel.revalidate();

                    System.out.println(Arrays.toString(work_schedule_bonuses.toArray()));
                }

                work_schedule_bonuses.add(workScheduleBonusLast);
                loadBonusPanel(scrollPane);
                panel.repaint();
                panel.revalidate();

                System.out.println(Arrays.toString(work_schedule_bonuses.toArray()));
            }
        });
        panel.add(buttonAddBonus, "span, wrap");

        scrollPane.setViewportView(panel);
    }

    private void initFinePanel() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBackground(Color.cyan);
        scrollPane.setPreferredSize(new Dimension(630, 350));
        finePanel.add(scrollPane, BorderLayout.CENTER);

        loadFinePanel(scrollPane);
    }

    private void loadFinePanel(JScrollPane scrollPane) {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("", "10[]10", "10[]10"));
        panel.setBackground(new Color(255, 255, 255));
        panel.setPreferredSize(new Dimension(630, 30));

        JTextField jLabelFineName = new MyTextFieldUnderLine();
        jLabelFineName.setPreferredSize(new Dimension(230, 30));
        jLabelFineName.setFont((new Font("Inter", Font.PLAIN, 13)));

        JTextField jLabelFineQuantity = new MyTextFieldUnderLine();
        jLabelFineQuantity.setPreferredSize(new Dimension(70, 30));
        jLabelFineQuantity.setFont((new Font("Inter", Font.PLAIN, 13)));

        JTextField jLabelFineAmount = new MyTextFieldUnderLine();
        jLabelFineAmount.setPreferredSize(new Dimension(125, 30));
        jLabelFineAmount.setFont((new Font("Inter", Font.PLAIN, 13)));

        JTextField jLabelFineTotal = new MyTextFieldUnderLine();
        jLabelFineTotal.setPreferredSize(new Dimension(125, 30));
        jLabelFineTotal.setFont((new Font("Inter", Font.PLAIN, 13)));
        jLabelFineTotal.setEditable(false);

        jLabelFineAmount.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        Integer.parseInt(jLabelFineQuantity.getText());
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "Số lần không hợp lệ!",
                                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    try {
                        Double.parseDouble(jLabelFineAmount.getText());
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "Mức áp dụng không hợp lệ!",
                                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    Work_Schedule_Fine workScheduleFineLast = new Work_Schedule_Fine();
                    workScheduleFineLast.setWork_schedule_id(workSchedule.getId());
                    workScheduleFineLast.setFine_name(jLabelFineName.getText());
                    workScheduleFineLast.setQuantity(Integer.parseInt(jLabelFineQuantity.getText()));
                    workScheduleFineLast.setFine_amount(Double.parseDouble(jLabelFineAmount.getText()));

                    if (checkExistsFine(workScheduleFineLast)) {
                        JOptionPane.showMessageDialog(null, "Đã tồn tại nội dung vi phạm!",
                                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        double total = Integer.parseInt(jLabelFineQuantity.getText()) * Double.parseDouble(jLabelFineAmount.getText());
                        jLabelFineTotal.setText(VNString.currency(total));
                        workScheduleFineLast.setFine_total(Double.parseDouble(jLabelFineTotal.getText().replaceAll("\\.", "").split(" ₫")[0]));

                        work_schedule_fines.add(workScheduleFineLast);
                        loadFinePanel(scrollPane);
                        panel.repaint();
                        panel.revalidate();
                    }

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        JLabel titleFineName = new JLabel();
        JLabel titleFineQuantity = new JLabel();
        JLabel titleFineAmount = new JLabel();
        JLabel titleFineTotal = new JLabel();

        titleFineName.setText("Loại vi phạm");
        titleFineQuantity.setText("Số lần");
        titleFineAmount.setText("Mức áp dụng");
        titleFineTotal.setText("Thành tiền");

        titleFineName.setPreferredSize(new Dimension(230, 30));
        titleFineName.setFont((new Font("Inter", Font.BOLD, 13)));

        titleFineQuantity.setPreferredSize(new Dimension(70, 30));
        titleFineQuantity.setFont((new Font("Inter", Font.BOLD, 13)));

        titleFineAmount.setPreferredSize(new Dimension(125, 30));
        titleFineAmount.setFont((new Font("Inter", Font.BOLD, 13)));

        titleFineTotal.setPreferredSize(new Dimension(125, 30));
        titleFineTotal.setFont((new Font("Inter", Font.BOLD, 13)));

        JPanel jpanel1 = new JPanel(new MigLayout("", "0[]10[]10[]10[]0[]0", "0[]0"));
        jpanel1.setBackground(new Color(227, 242, 250));
        jpanel1.setPreferredSize(new Dimension(630, 30));

        jpanel1.add(titleFineName);
        jpanel1.add(titleFineQuantity);
        jpanel1.add(titleFineAmount);
        jpanel1.add(titleFineTotal);
        jpanel1.add(new JLabel(), "width 50,wrap");
        panel.add(jpanel1, "wrap");

        for (Work_Schedule_Fine workScheduleFine : work_schedule_fines) {
            JPanel jPanelFine = new JPanel(new MigLayout("", "0[]10[]10[]10[]0[]0", "0[]0"));
            jPanelFine.setBackground(new Color(255, 255, 255));
            jPanelFine.setPreferredSize(new Dimension(730, 50));

            JTextField jLabelFineName1 = new MyTextFieldUnderLine();
            jLabelFineName1.setText(workScheduleFine.getFine_name());
            jLabelFineName1.setPreferredSize(new Dimension(230, 30));
            jLabelFineName1.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelFineName1.setEditable(false);

            JTextField jLabelFineQuantity1 = new MyTextFieldUnderLine();
            jLabelFineQuantity1.setText(String.valueOf(workScheduleFine.getQuantity()));
            jLabelFineQuantity1.setPreferredSize(new Dimension(70, 30));
            jLabelFineQuantity1.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelFineQuantity1.setEditable(false);

            JTextField jLabelFineAmount1 = new MyTextFieldUnderLine();
            jLabelFineAmount1.setText(VNString.currency(workScheduleFine.getFine_amount()));
            jLabelFineAmount1.setPreferredSize(new Dimension(125, 30));
            jLabelFineAmount1.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelFineAmount1.setEditable(false);

            JTextField jLabelFineTotal1 = new MyTextFieldUnderLine();
            jLabelFineTotal1.setText(VNString.currency(workScheduleFine.getFine_total()));
            jLabelFineTotal1.setPreferredSize(new Dimension(125, 30));
            jLabelFineTotal1.setFont((new Font("Inter", Font.PLAIN, 13)));
            jLabelFineTotal1.setEditable(false);


            jPanelFine.add(jLabelFineName1);
            jPanelFine.add(jLabelFineQuantity1);
            jPanelFine.add(jLabelFineAmount1);
            jPanelFine.add(jLabelFineTotal1);

            JLabel jLabelIconRemove = new JLabel(new FlatSVGIcon("icon/remove.svg"));
            jLabelIconRemove.setCursor(new Cursor(Cursor.HAND_CURSOR));
            jLabelIconRemove.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    work_schedule_fines.remove(workScheduleFine);
                    loadFinePanel(scrollPane);
                    panel.repaint();
                    panel.revalidate();
                }
            });

            jPanelFine.add(jLabelIconRemove, "width 50,wrap");
            panel.add(jPanelFine, "wrap");

        }

        JPanel jPanelFine = new JPanel(new MigLayout("", "0[]10[]10[]10[]0[]0", "0[]0"));
        jPanelFine.setBackground(new Color(255, 255, 255));
        jPanelFine.setPreferredSize(new Dimension(730, 50));
        jPanelFine.add(jLabelFineName);
        jPanelFine.add(jLabelFineQuantity);
        jPanelFine.add(jLabelFineAmount);
        jPanelFine.add(jLabelFineTotal);
        jPanelFine.add(new JLabel(), "width 50,wrap");
        panel.add(jPanelFine, "wrap");

        JButton buttonAddFine = new JButton("+ Thêm vi phạm");
        buttonAddFine.setBackground(new Color(0, 182, 62));
        buttonAddFine.setForeground(Color.WHITE);
        buttonAddFine.setPreferredSize(new Dimension(150, 30));
//        buttonAddFine.setIcon(new FlatSVGIcon("icon/add.svg"));
        buttonAddFine.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAddFine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer.parseInt(jLabelFineQuantity.getText());
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Số lần không hợp lệ!",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                try {
                    Double.parseDouble(jLabelFineAmount.getText());
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Mức áp dụng không hợp lệ!",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                Work_Schedule_Fine workScheduleFineLast = new Work_Schedule_Fine();
                workScheduleFineLast.setWork_schedule_id(workSchedule.getId());
                workScheduleFineLast.setFine_name(jLabelFineName.getText());
                workScheduleFineLast.setQuantity(Integer.parseInt(jLabelFineQuantity.getText()));
                workScheduleFineLast.setFine_amount(Double.parseDouble(jLabelFineAmount.getText()));

                if (checkExistsFine(workScheduleFineLast)) {
                    JOptionPane.showMessageDialog(null, "Đã tồn tại nội dung vi phạm!",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    double total = Integer.parseInt(jLabelFineQuantity.getText()) * Double.parseDouble(jLabelFineAmount.getText());
                    jLabelFineTotal.setText(VNString.currency(total));
                    workScheduleFineLast.setFine_total(Double.parseDouble(jLabelFineTotal.getText().replaceAll("\\.", "").split(" ₫")[0]));

                    work_schedule_fines.add(workScheduleFineLast);
                    loadFinePanel(scrollPane);
                    panel.repaint();
                    panel.revalidate();
                }
            }
        });
        panel.add(buttonAddFine, "span, wrap");

        scrollPane.setViewportView(panel);
    }

    private void initChanCongPanel() {
        if (!workSchedule.getNotice().equals("Không")) {
            JLabel jLabel = new JLabel("Nghỉ có phép");
            jLabel.setFont((new Font("Public Sans", Font.BOLD, 15)));
            chamCongPanel.add(jLabel, "wrap");

            JLabel jLabel1 = new JLabel("Chọn nhân viên làm thay ca");
            jLabel1.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            chamCongPanel.add(jLabel1);

            txtSearch = new MyTextField();
            txtSearch.setPreferredSize(new Dimension(250, 30));
            txtSearch.putClientProperty("JTextField.placeholderText", "Nhập tên nhân viên làm thay ca");
            txtSearch.putClientProperty("JTextField.showClearButton", true);
            txtSearch.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    txtSearchMouseClicked(evt);
                }
            });
            txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    txtSearchKeyPressed(evt);
                }

                public void keyReleased(java.awt.event.KeyEvent evt) {
                    txtSearchKeyReleased(evt);
                }
            });
            chamCongPanel.add(txtSearch, "wrap");
        } else {
            JLabel jLabel = new JLabel("Đi làm");
            jLabel.setFont((new Font("Public Sans", Font.BOLD, 15)));
            chamCongPanel.add(jLabel, "wrap");

            JLabel jLabel1 = new JLabel("Giờ vào");
            jLabel1.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            chamCongPanel.add(jLabel1);

            timePickerCheckin = new TimePicker();
            timePickerCheckin.set24HourView(true);
            timePickerCheckin.setOrientation(SwingConstants.HORIZONTAL);
            JFormattedTextField editorCheckin = new JFormattedTextField();
//            editorCheckin.setEditable(false);
            timePickerCheckin.setEditor(editorCheckin);

            editorCheckin.setPreferredSize(new Dimension(150, 30));
            chamCongPanel.add(editorCheckin, "wrap");

            JLabel jLabel2 = new JLabel("Giờ ra");
            jLabel2.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            chamCongPanel.add(jLabel2);

            timePickerCheckout = new TimePicker();
            timePickerCheckout.set24HourView(true);
            timePickerCheckout.setOrientation(SwingConstants.HORIZONTAL);
            JFormattedTextField editorCheckout = new JFormattedTextField();
//            editorCheckout.setEditable(false);
            timePickerCheckout.setEditor(editorCheckout);

            editorCheckout.setPreferredSize(new Dimension(150, 30));
            chamCongPanel.add(editorCheckout, "wrap");


            if (!workSchedule.getCheck_in().equals("null") && !workSchedule.getCheck_out().equals("null")) {
                timePickerCheckin.setSelectedTime(LocalTime.of(Integer.parseInt(workSchedule.getCheck_in().split(":")[0]), Integer.parseInt(workSchedule.getCheck_in().split(":")[1])));
                timePickerCheckout.setSelectedTime(LocalTime.of(Integer.parseInt(workSchedule.getCheck_out().split(":")[0]), Integer.parseInt(workSchedule.getCheck_out().split(":")[1])));
            }
        }
    }

    private void checkExistByDate(Date date, int shift) {
        List<Work_Schedule> work_schedules = workScheduleBLL.findWork_schedulesBy(Map.of(
                "staff_id", workSchedule.getStaff_id(),
                "date", date,
                "shift", shift
        ));

        if (!work_schedules.isEmpty()) {
            datePicker.setSelectedDate(workSchedule.getDate());
            JOptionPane.showMessageDialog(null, "Lịch làm việc đã tồn tại!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        }
    }

    private void checkExistByShift(Date date, int shift) {
        System.out.println(date);
        List<Work_Schedule> work_schedules = workScheduleBLL.findWork_schedulesBy(Map.of(
                "staff_id", workSchedule.getStaff_id(),
                "date", date,
                "shift", shift
        ));

        if (!work_schedules.isEmpty()) {
            for (Enumeration<AbstractButton> buttons = bgShift.getElements(); buttons.hasMoreElements(); ) {
                AbstractButton button = buttons.nextElement();

                if (button.getText().contains("1:") && workSchedule.getShift() == 1) {
                    button.setSelected(true);
                } else if (button.getText().contains("2:") && workSchedule.getShift() == 2) {
                    button.setSelected(true);
                } else if (button.getText().contains("3:") && workSchedule.getShift() == 3) {
                    button.setSelected(true);
                }
            }
            JOptionPane.showMessageDialog(null, "Lịch làm việc đã tồn tại!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        }
    }

    private void editWork_Schedule() {
        Pair<Boolean, String> result;
        int id, staff_id, shift = 0;
        Date date;
        String checkin, checkout;

        id = workSchedule.getId();
        staff_id = workSchedule.getStaff_id();
        date = datePicker.getDateSQL_Single();

        if (date == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày làm việc!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Enumeration<AbstractButton> buttons = bgShift.getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                if (button.getText().contains("1:"))
                    shift = 1;

                if (button.getText().contains("2:"))
                    shift = 2;

                if (button.getText().contains("3:"))
                    shift = 3;
            }
        }
        if (!workSchedule.getNotice().equals("Không")) {
            checkin = workSchedule.getCheck_in();
            checkout = workSchedule.getCheck_out();
        } else {
            if ((timePickerCheckin.getSelectedTime() == null && timePickerCheckout.getSelectedTime() != null) || (timePickerCheckin.getSelectedTime() != null && timePickerCheckout.getSelectedTime() == null)) {
                JOptionPane.showMessageDialog(null, "Vui lòng chấm công đủ giờ vào và giờ ra!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            } else {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
                checkin = timePickerCheckin.getSelectedTime() == null ? "null" : timePickerCheckin.getSelectedTime().format(df);
                checkout = timePickerCheckout.getSelectedTime() == null ? "null" : timePickerCheckout.getSelectedTime().format(df);
            }
        }

        Work_Schedule workSchedule1 = new Work_Schedule(id, staff_id, date, checkin, checkout, shift, workSchedule.getNotice());

        result = workScheduleBLL.updateWork_schedule(workSchedule1);

        if (result.getKey()) {
            if (this.staff_id != -1) {
                Work_Schedule newWork_schedule = new Work_Schedule();
                List<Work_Schedule> work_schedules1 = workScheduleBLL.searchWork_schedules();
                work_schedules1.sort(Comparator.comparing(Work_Schedule::getId));
                int newId = workScheduleBLL.getAutoID(work_schedules1);
                newWork_schedule.setId(newId);
                newWork_schedule.setStaff_id(this.staff_id);
                newWork_schedule.setDate(workSchedule1.getDate());
                newWork_schedule.setCheck_in("null");
                newWork_schedule.setCheck_out("null");
                newWork_schedule.setShift(workSchedule1.getShift());
                newWork_schedule.setNotice("Không");
                workScheduleBLL.addWork_schedule(List.of(newWork_schedule));
            }

            new Work_Schedule_FineBLL().deleteAllWork_schedule_fine(workSchedule);
            new Work_Schedule_BonusBLL().deleteAllWork_schedule_bonus(workSchedule);

            new Work_Schedule_FineBLL().addWork_schedule(work_schedule_fines);
            new Work_Schedule_BonusBLL().addWork_schedule(work_schedule_bonuses);

            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            CreateWorkScheduleGUI createWorkScheduleGUI = (CreateWorkScheduleGUI) Cafe_Application.homeGUI.allPanelModules[Cafe_Application.homeGUI.indexModuleCreateWorkScheduleGUI];
            createWorkScheduleGUI.refresh();
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteWorkSchedule() {
        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận xoá lịch làm việc?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (choice == 1) {
            Pair<Boolean, String> result = workScheduleBLL.deleteWork_schedule(workSchedule);
            if (result.getKey()) {
                JOptionPane.showMessageDialog(null, result.getValue(),
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                CreateWorkScheduleGUI createWorkScheduleGUI = (CreateWorkScheduleGUI) Cafe_Application.homeGUI.allPanelModules[Cafe_Application.homeGUI.indexModuleCreateWorkScheduleGUI];
                createWorkScheduleGUI.refresh();
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, result.getValue(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {
        if (search.getItemSize() > 0 && !txtSearch.getText().isEmpty()) {
            menu.show(txtSearch, 0, txtSearch.getHeight());
            search.clearSelected();
        }
    }

    private List<DataSearch> search(String text) {
        staff_id = -1;
        List<DataSearch> list = new ArrayList<>();
        List<Staff> staffList1 = new ArrayList<>();
        List<Staff> staffList2 = new ArrayList<>();
        List<Staff> staffs = new StaffBLL().findStaffs("name", text);

        List<Role_Detail> role_detailList = new Role_DetailBLL().searchRole_detailsByStaff(workSchedule.getStaff_id());
        Role_Detail roleDetail = role_detailList.get(0);
        for (Staff staff : staffs) {
            List<Role_Detail> role_detailList1 = new Role_DetailBLL().searchRole_detailsByStaff(staff.getId());
            Role_Detail roleDetail1 = role_detailList1.get(0);
            if (roleDetail1.getRole_id() == roleDetail.getRole_id()) {
                staffList1.add(staff);
            }
        }

        for (Staff staff : staffList1) {
            if (workScheduleBLL.searchWork_schedules("date = '" + workSchedule.getDate() + "'", "shift = " + workSchedule.getShift(), "staff_id = " + staff.getId()).isEmpty()) {
                staffList2.add(staff);
            }
        }

        for (Staff m : staffList2) {
            if (list.size() == 7)
                break;
            list.add(new DataSearch(m.getName() + " - " + m.getId()));
        }
        return list;
    }

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtSearch.getText().trim().toLowerCase();
            search.setData(search(text));
            if (search.getItemSize() > 0 && !txtSearch.getText().isEmpty()) {
                menu.show(txtSearch, 0, txtSearch.getHeight());
                menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
            } else {
                menu.setVisible(false);
            }
        }
    }

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            search.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            search.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = search.getSelectedText();
            txtSearch.setText(text);

        }
        menu.setVisible(false);

    }
}
