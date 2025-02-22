package com.coffee.GUI;

import com.coffee.BLL.PayrollBLL;
import com.coffee.DTO.Function;
import com.coffee.DTO.Payroll;
import com.coffee.GUI.DialogGUI.FormAddGUI.AddPayrollGUI;
import com.coffee.GUI.components.DataTable;
import com.coffee.GUI.components.Layout1;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.GUI.components.RoundedScrollPane;
import com.coffee.main.Cafe_Application;
import com.coffee.utils.PDF;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PayrollGUI extends Layout1 {
    private RoundedPanel containerSearch;
    private JMonthChooser jMonthChooserStart;
    private JYearChooser jYearChooserStart;
    private JMonthChooser jMonthChooserEnd;
    private JYearChooser jYearChooserEnd;
    private List<Function> functions;
    private PayrollBLL payrollBLL = new PayrollBLL();
    private DataTable dataTable;
    private RoundedScrollPane scrollPane;
    private int indexColumnDetail = -1;
    private int indexColumnRemove = -1;
    private boolean detail = false;
    private boolean remove = false;
    private String[] columnNames;
    private Object[][] data = new Object[0][0];

    public PayrollGUI(List<Function> functions) {
        super();
        this.functions = functions;
        if (functions.stream().anyMatch(f -> f.getName().equals("view")))
            detail = true;
        if (functions.stream().anyMatch(f -> f.getName().equals("remove")))
            remove = true;
        init(functions);
    }

    private void init(List<Function> functions) {
        containerSearch = new RoundedPanel();

        columnNames = new String[]{"Mã", "Tên Bảng Lương", "Kỳ Làm Việc", "Tổng Lương", "Đã Trả", "Còn Lại"};
        if (detail) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            indexColumnDetail = columnNames.length - 1;
            columnNames[indexColumnDetail] = "Xem";
        }

        if (remove) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            indexColumnRemove = columnNames.length - 1;
            columnNames[indexColumnRemove] = "Xoá";
        }

        dataTable = new DataTable(new Object[0][0], columnNames, e -> selectFunction(), detail, false, remove, 6); // table hiển thị các thuộc tính  nên điền 4
        dataTable.getColumnModel().getColumn(0).setMaxWidth(50);
        dataTable.getColumnModel().getColumn(1).setMaxWidth(450);
        dataTable.getColumnModel().getColumn(2).setMaxWidth(150);
        dataTable.getColumnModel().getColumn(3).setMaxWidth(150);
        dataTable.getColumnModel().getColumn(4).setMaxWidth(150);
        dataTable.getColumnModel().getColumn(5).setMaxWidth(150);

        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1165, 680));
        bottom.add(scrollPane, BorderLayout.CENTER);

        containerSearch.setLayout(new MigLayout("", "10[]10[]10", ""));
        containerSearch.setBackground(new Color(191, 198, 208));
        containerSearch.setPreferredSize(new Dimension(500, 40));
        SearchPanel.add(containerSearch);

        jMonthChooserStart = new JMonthChooser();
        jMonthChooserStart.setPreferredSize(new Dimension(100, 50));
        jMonthChooserStart.setBackground(new Color(245, 246, 250));
        jMonthChooserStart.setMonth(0);
        jMonthChooserStart.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                searchPayrolls();
            }
        });
        containerSearch.add(jMonthChooserStart);

        jYearChooserStart = new JYearChooser();
        jYearChooserStart.setPreferredSize(new Dimension(100, 50));
        jYearChooserStart.setBackground(new Color(245, 246, 250));
        jYearChooserStart.setYear(2000);
        jYearChooserStart.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                searchPayrolls();
            }
        });
        containerSearch.add(jYearChooserStart);

        JLabel jLabel = new JLabel("Đến");
        jLabel.setFont(new Font("Lexend", Font.BOLD, 14));
        containerSearch.add(jLabel);

        jMonthChooserEnd = new JMonthChooser();
        jMonthChooserEnd.setPreferredSize(new Dimension(100, 50));
        jMonthChooserEnd.setBackground(new Color(245, 246, 250));
        jMonthChooserEnd.setMonth(LocalDate.now().getMonth().getValue() - 1);
        jMonthChooserEnd.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                searchPayrolls();
            }
        });
        containerSearch.add(jMonthChooserEnd);

        jYearChooserEnd = new JYearChooser();
        jYearChooserEnd.setPreferredSize(new Dimension(100, 50));
        jYearChooserEnd.setBackground(new Color(245, 246, 250));
        jYearChooserEnd.setYear(LocalDate.now().getYear());
        jYearChooserEnd.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                searchPayrolls();
            }
        });
        containerSearch.add(jYearChooserEnd);

        loadDataTable(payrollBLL.getData(payrollBLL.searchPayrolls()));

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

        if (functions.stream().anyMatch(f -> f.getName().equals("add"))) {
            RoundedPanel roundedPanel = new RoundedPanel();
            roundedPanel.setLayout(new GridBagLayout());
            roundedPanel.setPreferredSize(new Dimension(130, 40));
            roundedPanel.setBackground(new Color(1, 120, 220));
            roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            roundedPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    new AddPayrollGUI();
                    refresh();
                }
            });

            FunctionPanel.add(roundedPanel);

            JLabel panel = new JLabel("Thêm mới");
            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
            panel.setForeground(Color.white);
            panel.setIcon(new FlatSVGIcon("icon/add.svg"));
            roundedPanel.add(panel);
        }
        if (functions.stream().anyMatch(f -> f.getName().equals("excel"))) {
            RoundedPanel roundedPanel = new RoundedPanel();
            roundedPanel.setLayout(new GridBagLayout());
            roundedPanel.setPreferredSize(new Dimension(130, 40));
            roundedPanel.setBackground(new Color(1, 120, 220));
            roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            FunctionPanel.add(roundedPanel);

            JLabel panel = new JLabel("Nhập Excel");
            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
            panel.setForeground(Color.white);
            panel.setIcon(new FlatSVGIcon("icon/import.svg"));
            roundedPanel.add(panel);
        }
        if (functions.stream().anyMatch(f -> f.getName().equals("pdf"))) {
            RoundedPanel roundedPanel = new RoundedPanel();
            roundedPanel.setLayout(new GridBagLayout());
            roundedPanel.setPreferredSize(new Dimension(130, 40));
            roundedPanel.setBackground(new Color(1, 120, 220));
            roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            FunctionPanel.add(roundedPanel);

            JLabel panel = new JLabel("Xuất PDF");
            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
            panel.setForeground(Color.white);
            panel.setIcon(new FlatSVGIcon("icon/export.svg"));
            roundedPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    PDF.exportPayrollPDF(data, "src/main/resources/ExportPDF");
                    JOptionPane.showMessageDialog(null, "Xuất PDF danh sách bảng lương thành công.",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            roundedPanel.add(panel);
        }
    }

    public void refresh() {
        jMonthChooserStart.setMonth(0);
        jYearChooserStart.setYear(2010);

        jMonthChooserEnd.setMonth(LocalDate.now().getMonth().getValue() - 1);
        jYearChooserEnd.setYear(LocalDate.now().getYear());

        loadDataTable(payrollBLL.getData(payrollBLL.searchPayrolls()));
    }

    private void searchPayrolls() {
        int month_start = jMonthChooserStart.getMonth() + 1;
        int year_start = jYearChooserStart.getYear();
        int month_end = jMonthChooserEnd.getMonth() + 1;
        int year_end = jYearChooserEnd.getYear();
        List<Payroll> payrollList = payrollBLL.searchPayrolls();
        List<Payroll> list = new ArrayList<>();
        Date start = java.sql.Date.valueOf(LocalDate.of(year_start, month_start, 1));
        Date end = java.sql.Date.valueOf(LocalDate.of(year_end, month_end, 20));

        for (Payroll payroll : payrollList) {
            Date date = java.sql.Date.valueOf(LocalDate.of(payroll.getYear(), payroll.getMonth(), 1));
            if (start.before(date) && date.before(end))
                list.add(payroll);
        }

        loadDataTable(payrollBLL.getData(list));
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

            if (detail) {
                JLabel iconDetail = new JLabel(new FlatSVGIcon("icon/detail.svg"));
                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = iconDetail;
            }

            if (remove) {
                JLabel iconRemove = new JLabel(new FlatSVGIcon("icon/remove.svg"));
                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = iconRemove;
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
            Cafe_Application.homeGUI.openModule(new PayrollDetailGUI(payrollBLL.searchPayrolls("id = " + data[indexRow][0]).get(0))); // Đối tượng nào có thuộc tính deleted thì thêm  để lấy các đối tượng còn tồn tại, chưa xoá
        if (indexColumn == indexColumnRemove) {
            deletedPayroll(payrollBLL.searchPayrolls("id = " + data[indexRow][0]).get(0));
        }
    }

    private void deletedPayroll(Payroll payroll) {
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn bảng lương cần xoá.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận xoá bảng lương?", "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (choice == 1) {
            Pair<Boolean, String> result = payrollBLL.deletePayroll(payroll);
            if (result.getKey()) {
                JOptionPane.showMessageDialog(null, result.getValue(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                refresh();
            } else {
                JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
