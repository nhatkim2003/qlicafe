package com.coffee.GUI.DialogGUI.FormAddGUI;

import com.coffee.BLL.MaterialBLL;
import com.coffee.BLL.Work_ScheduleBLL;
import com.coffee.BLL.StaffBLL;
import com.coffee.DTO.Material;
import com.coffee.DTO.Work_Schedule;
import com.coffee.DTO.Staff;
import com.coffee.GUI.DialogGUI.DialogForm;
import com.coffee.GUI.components.AutocompleteJComboBox;
import com.coffee.GUI.components.DatePicker;
import com.coffee.GUI.components.StringSearchable;
import com.coffee.GUI.components.swing.DataSearch;
import com.coffee.GUI.components.swing.EventClick;
import com.coffee.GUI.components.swing.MyTextField;
import com.coffee.GUI.components.swing.PanelSearch;
import com.coffee.main.Cafe_Application;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class AddWorkScheduleGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeWork_Schedule;
    private List<JTextField> jTextFieldWork_Schedule;
    private JTextField jTextFieldDate;
    private JTextField dateTextField;
    private DatePicker datePicker;
    private JFormattedTextField editor;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private JCheckBox jCheckBox1;
    private JCheckBox jCheckBox2;
    private JCheckBox jCheckBox3;
    private MyTextField txtSearch;
    private PanelSearch search;
    private JPopupMenu menu;
    private StaffBLL staffBLL = new StaffBLL();
    private Work_ScheduleBLL work_ScheduleBLL = new Work_ScheduleBLL();
    private List<String> staffList;
    private int staff_id = -1;

    public AddWorkScheduleGUI() {
        super();
        super.setTitle("Thêm lịch làm việc");
        super.setSize(new Dimension(600, 400));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        init();
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

    private void init() {
        titleName = new JLabel();
        jTextFieldDate = new JTextField();
        datePicker = new DatePicker();
        editor = new JFormattedTextField();
        dateTextField = new JTextField();
        attributeWork_Schedule = new ArrayList<>();
        jTextFieldWork_Schedule = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");
        jCheckBox1 = new JCheckBox();
        jCheckBox2 = new JCheckBox();
        jCheckBox3 = new JCheckBox();
        staffList = new ArrayList<String>();
        content.setLayout(new MigLayout("",
                "50[]20[]50",
                "20[]20[]20"));

        titleName.setText("Thêm lịch làm việc");
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


            if (string.equals("Nhân viên")) {
                txtSearch = new MyTextField();
                txtSearch.setPreferredSize(new Dimension(200, 30));

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
                content.add(txtSearch, "wrap");
                continue;
            }
            if (string.equals("Ngày")) {
                datePicker.setDateSelectionMode(raven.datetime.component.date.DatePicker.DateSelectionMode.SINGLE_DATE_SELECTED);
                datePicker.setEditor(editor);
                datePicker.setCloseAfterSelected(true);
                editor.setPreferredSize(new Dimension(200, 30));
                editor.setFont(new Font("Inter", Font.BOLD, 15));
                content.add(editor, "wrap");
                continue;
            }
            if (string.equals("Ca")) {
                JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                jPanel.setPreferredSize(new Dimension(1000, 30));
                jPanel.setBackground(Color.white);

                jCheckBox1.setText("1: 6h - 12h");
                jCheckBox2.setText("2: 12h - 18h");
                jCheckBox3.setText("3: 18h - 23h");

                jPanel.add(jCheckBox1);
                jPanel.add(jCheckBox2);
                jPanel.add(jCheckBox3);
                content.add(jPanel, "wrap");
            }
        }

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
                cancel();
            }
        });
        containerButton.add(buttonCancel);

        buttonAdd.setPreferredSize(new Dimension(100, 30));
        buttonAdd.setBackground(new Color(1, 120, 220));
        buttonAdd.setForeground(Color.white);
        buttonAdd.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addWork_Schedule();
            }
        });
        containerButton.add(buttonAdd);
    }

    private void addWork_Schedule() {
        Pair<Boolean, String> result;

        int id;
        List<Integer> shifts = new ArrayList<>();
        Date date;
        String checkin, checkout;

        if (staff_id == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên!",
                    "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Work_Schedule> work_schedules = work_ScheduleBLL.searchWork_schedules();
        work_schedules.sort(Comparator.comparing(Work_Schedule::getId));
        id = work_ScheduleBLL.getAutoID(work_schedules);

        date = datePicker.getDateSQL_Single();
        if (date == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày làm!",
                    "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (jCheckBox1.isSelected())
            shifts.add(1);

        if (jCheckBox2.isSelected())
            shifts.add(2);

        if (jCheckBox3.isSelected())
            shifts.add(3);

        if (shifts.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ca làm!",
                    "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (shifts.size() == 3) {
            JOptionPane.showMessageDialog(null, "Một nhân viên chỉ được làm tối đa 12h/ngày.\nVui lòng chọn lại ca làm việc.",
                    "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        checkin = "null";
        checkout = "null";

        List<Work_Schedule> newWork_scheduleList = new ArrayList<>();
        for (Integer shift : shifts) {
            Work_Schedule workSchedule = new Work_Schedule(id, staff_id, date, checkin, checkout, shift, "Không");
            newWork_scheduleList.add(workSchedule);
            id += 1;
        }
        result = work_ScheduleBLL.addWork_schedule(newWork_scheduleList);

        if (result.getKey()) {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
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
        List<Staff> staffs = new StaffBLL().findStaffs("name", text);
        for (Staff m : staffs) {
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
