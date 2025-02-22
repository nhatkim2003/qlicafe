package com.coffee.GUI;

import com.coffee.BLL.StaffBLL;
import com.coffee.BLL.Leave_Of_Absence_FormBLL;
import com.coffee.DTO.*;

import com.coffee.GUI.DialogGUI.FormDetailGUI.DetailLeave_Of_Absence_FormGUI;
import com.coffee.GUI.DialogGUI.FormEditGUI.EditLeave_Of_Absence_FormGUI;
import com.coffee.GUI.components.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DateEvent;
import raven.datetime.component.date.DateSelectionListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;

public class Leave_Of_Absence_FormGUI extends Layout2 {
    private RoundedPanel containerSearch;
    private JLabel iconSearch;
    private JTextField jTextFieldSearch;
    private DatePicker datePicker;
    private JFormattedTextField editor;
    private JButton jButtonSearch;
    private List<Function> functions;
    private Leave_Of_Absence_FormBLL leave_Of_Absence_FormBLL = new Leave_Of_Absence_FormBLL();
    private DataTable dataTable;
    private RoundedScrollPane scrollPane;
    private int indexColumnDetail = -1;
    private int indexColumnEdit = -1;
    private boolean detail = false;
    private boolean edit = false;
    private String[] columnNames;
    private StaffBLL staffBLL = new StaffBLL();
    private Date date;
    private Object[][] data = new Object[0][0];
    private JComboBox<String> jComboBox;

    public Leave_Of_Absence_FormGUI(List<Function> functions) {
        super();
        this.functions = functions;
        if (functions.stream().anyMatch(f -> f.getName().equals("view")))
            detail = true;
        if (functions.stream().anyMatch(f -> f.getName().equals("edit")))
            edit = true;
        init(functions);
    }

    private void init(List<Function> functions) {
        date = new Date();
        containerSearch = new RoundedPanel();
        iconSearch = new JLabel();
        jTextFieldSearch = new JTextField();
        jButtonSearch = new JButton("Tìm kiếm");
        datePicker = new DatePicker();
        editor = new JFormattedTextField();
        jComboBox = new JComboBox<>();

        columnNames = new String[]{"Mã Đơn", "Họ tên", "Ngày tạo đơn", "Ngày nghỉ", "Ca nghỉ", "Trạng thái"};

        if (detail) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            indexColumnDetail = columnNames.length - 1;
            columnNames[indexColumnDetail] = "Xem";
        }

        if (edit) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            indexColumnEdit = columnNames.length - 1;
            columnNames[indexColumnEdit] = "Sửa";
        }

        dataTable = new DataTable(new Object[0][0], columnNames,
                e -> selectFunction(),
                detail, edit, false, 6);
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1165, 680));
        bottom.add(scrollPane, BorderLayout.CENTER);

        datePicker.setDateSelectionMode(raven.datetime.component.date.DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED);
        datePicker.setEditor(editor);
        datePicker.setUsePanelOption(true);
        datePicker.setCloseAfterSelected(true);
        datePicker.addDateSelectionListener(new DateSelectionListener() {
            @Override
            public void dateSelected(DateEvent dateEvent) {
                searchLeave_Of_Absence_Forms();
            }
        });

        editor.setPreferredSize(new Dimension(280, 40));
        editor.setFont(new Font("Inter", Font.BOLD, 15));
        FilterDatePanel.add(editor, BorderLayout.WEST);

        containerSearch.setLayout(new MigLayout("", "10[]10[]10", ""));
        containerSearch.setBackground(new Color(245, 246, 250));
        containerSearch.setPreferredSize(new Dimension(280, 40));
        SearchPanel.add(containerSearch);

        iconSearch.setIcon(new FlatSVGIcon("icon/search.svg"));
        containerSearch.add(iconSearch);

        jTextFieldSearch.setBackground(new Color(245, 246, 250));
        jTextFieldSearch.setBorder(BorderFactory.createEmptyBorder());
        jTextFieldSearch.putClientProperty("JTextField.placeholderText", "Nhập tên nhân viên cần tìm kiếm");
        jTextFieldSearch.putClientProperty("JTextField.showClearButton", true);
        jTextFieldSearch.setPreferredSize(new Dimension(250, 30));
        jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchLeave_Of_Absence_Forms();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchLeave_Of_Absence_Forms();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchLeave_Of_Absence_Forms();
            }
        });
        containerSearch.add(jTextFieldSearch);

        jButtonSearch.setBackground(new Color(1, 120, 220));
        jButtonSearch.setForeground(Color.white);
        jButtonSearch.setPreferredSize(new Dimension(100, 30));
        jButtonSearch.addActionListener(e -> searchLeave_Of_Absence_Forms());
        SearchPanel.add(jButtonSearch);

        jComboBox.setBackground(new Color(1, 120, 220));
        jComboBox.setForeground(Color.white);
        jComboBox.setPreferredSize(new Dimension(200, 30));
        jComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchLeave_Of_Absence_Forms();
            }
        });
        SearchPanel.add(jComboBox);

        loadCombobox();
        loadDataTable(leave_Of_Absence_FormBLL.getData(leave_Of_Absence_FormBLL.searchLeave_Of_Absence_Forms()));

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
    }

    public void refresh() {
        jComboBox.setSelectedIndex(0);
        jTextFieldSearch.setText("");
        datePicker.clearSelectedDate();
        loadDataTable(leave_Of_Absence_FormBLL.getData(leave_Of_Absence_FormBLL.searchLeave_Of_Absence_Forms()));
    }

    private void searchLeave_Of_Absence_Forms() {
        List<Leave_Of_Absence_Form> work_scheduleList = leave_Of_Absence_FormBLL.searchLeave_Of_Absence_Forms();
        if (jTextFieldSearch.getText().isEmpty() && datePicker.getDateSQL_Between() == null) {
            if (jComboBox.getSelectedIndex() == 1) {
                work_scheduleList.removeIf(leaveOfAbsenceForm -> leaveOfAbsenceForm.getStatus() != 0);
            }
            if (jComboBox.getSelectedIndex() == 2) {
                work_scheduleList.removeIf(leaveOfAbsenceForm -> leaveOfAbsenceForm.getStatus() != 1);
            }
            if (jComboBox.getSelectedIndex() == 3) {
                work_scheduleList.removeIf(leaveOfAbsenceForm -> leaveOfAbsenceForm.getStatus() != 2);
            }
            loadDataTable(leave_Of_Absence_FormBLL.getData(work_scheduleList));
        } else {
            if (!jTextFieldSearch.getText().isEmpty()) {
                List<Integer> staffIDList = new ArrayList<>();
                for (Staff staff : staffBLL.findStaffs("name", jTextFieldSearch.getText()))
                    staffIDList.add(staff.getId());
                work_scheduleList.removeIf(work_schedule -> !staffIDList.contains(work_schedule.getStaff_id()));
            }
            if (datePicker.getDateSQL_Between() != null) {
                Date startDate = datePicker.getDateSQL_Between()[0];
                Date endDate = datePicker.getDateSQL_Between()[1];
                if (startDate.after(endDate)) {
                    JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước ngày kết thúc.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                work_scheduleList.removeIf(work_schedule -> (work_schedule.getDate().before(startDate) || work_schedule.getDate().after(endDate)));
            }
            if (jComboBox.getSelectedIndex() == 1) {
                work_scheduleList.removeIf(leaveOfAbsenceForm -> leaveOfAbsenceForm.getStatus() != 0);
            }
            if (jComboBox.getSelectedIndex() == 2) {
                work_scheduleList.removeIf(leaveOfAbsenceForm -> leaveOfAbsenceForm.getStatus() != 1);
            }
            if (jComboBox.getSelectedIndex() == 3) {
                work_scheduleList.removeIf(leaveOfAbsenceForm -> leaveOfAbsenceForm.getStatus() != 2);
            }
            loadDataTable(leave_Of_Absence_FormBLL.getData(work_scheduleList));
        }
    }

    public void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);

        if (objects.length == 0) {
            return;
        }

        data = new Object[objects.length][objects[0].length];

        for (int i = 0; i < objects.length; i++) {
            System.arraycopy(objects[i], 0, data[i], 0, objects[i].length);

            int staffId = Integer.parseInt(data[i][1].toString());

            data[i][1] = staffBLL.findStaffsBy(Map.of("id", staffId)).get(0).getName();

            if (detail) {
                JLabel iconDetail = new JLabel(new FlatSVGIcon("icon/detail.svg"));
                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = iconDetail;
            }

            if (edit) {
                JLabel iconEdit = new JLabel(new FlatSVGIcon("icon/edit.svg"));
                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = iconEdit;
            }
        }

        for (Object[] object : data) {
            model.addRow(object);
        }
    }

    private void selectFunction() {
        int indexRow = dataTable.getSelectedRow();
        int indexColumn = dataTable.getSelectedColumn();

        if (detail && indexColumn == indexColumnDetail)
            new DetailLeave_Of_Absence_FormGUI(leave_Of_Absence_FormBLL.searchLeave_Of_Absence_Forms("id = " + data[indexRow][0]).get(0)); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá

        if (edit && indexColumn == indexColumnEdit) {
            new EditLeave_Of_Absence_FormGUI(leave_Of_Absence_FormBLL.searchLeave_Of_Absence_Forms("id = " + data[indexRow][0]).get(0)); // Đối tượng nào có thuộc tính deleted thì thêm  để lấy các đối tượng còn tồn tại, chưa xoá
            loadCombobox();
            refresh();

        }

//        if (remove && indexColumn == indexColumnRemove)
//            deleteWorkSchedule(leave_Of_Absence_FormBLL.searchLeave_Of_Absence_Forms().get(indexRow)); // Đối tượng nào có thuộc tính deleted thì thêm  để lấy các đối tượng còn tồn tại, chưa xoá

    }

    private void loadCombobox() {
        jComboBox.removeAllItems();
        int countStatus0 = 0;
        int countStatus1 = 0;
        int countStatus2 = 0;
        int countAll = 0;
        for (Leave_Of_Absence_Form leaveOfAbsenceForm : leave_Of_Absence_FormBLL.searchLeave_Of_Absence_Forms()) {
            if (leaveOfAbsenceForm.getStatus() == 0)
                countStatus0 += 1;
            if (leaveOfAbsenceForm.getStatus() == 1)
                countStatus1 += 1;
            if (leaveOfAbsenceForm.getStatus() == 2)
                countStatus2 += 1;
            countAll += 1;
        }
        jComboBox.addItem("Tất cả (" + countAll + ")");
        jComboBox.addItem("Chưa duyệt (" + countStatus0 + ")");
        jComboBox.addItem("Duyệt (" + countStatus1 + ")");
        jComboBox.addItem("Không duyệt (" + countStatus2 + ")");
        jComboBox.setSelectedIndex(0);
    }
}
