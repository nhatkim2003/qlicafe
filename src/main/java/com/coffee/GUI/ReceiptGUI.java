package com.coffee.GUI;

import com.coffee.BLL.*;
import com.coffee.DTO.*;
import com.coffee.GUI.DialogGUI.FormDetailGUI.DetailReceiptGUI;
import com.coffee.GUI.components.*;
import com.coffee.utils.PDF;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DateEvent;
import raven.datetime.component.date.DateSelectionListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class ReceiptGUI extends Layout2 {
    private RoundedPanel containerSearch;
    private JLabel iconSearch;
    private JTextField jTextFieldSearch;
    private DatePicker datePicker;
    private JFormattedTextField editor;
    private JButton jButtonSearch;
    private List<Function> functions;
    private ReceiptBLL receiptBLL = new ReceiptBLL();
    private DataTable dataTable;
    private RoundedScrollPane scrollPane;
    private int indexColumnDetail = -1;
    private boolean detail = false;
    private String[] columnNames;
    private StaffBLL staffBLL = new StaffBLL();
    private Date date;
    private Object[][] data = new Object[0][0];

    public ReceiptGUI(List<Function> functions) {
        super();
        this.functions = functions;
        if (functions.stream().anyMatch(f -> f.getName().equals("view")))
            detail = true;
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

        columnNames = new String[]{"Mã Hóa Đơn", "Nhân Viên", "Tổng Tiền", "Ngày Lập"};
        if (detail) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            indexColumnDetail = columnNames.length - 1;
            columnNames[indexColumnDetail] = "Xem";
        }

        dataTable = new DataTable(new Object[0][0], columnNames,
                e -> selectFunction(),
                detail, false, false, 4);
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
                searchReceipts();
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
                searchReceipts();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchReceipts();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchReceipts();
            }
        });
        containerSearch.add(jTextFieldSearch);

        jButtonSearch.setBackground(new Color(1, 120, 220));
        jButtonSearch.setForeground(Color.white);
        jButtonSearch.setPreferredSize(new Dimension(100, 30));
        jButtonSearch.addActionListener(e -> searchReceipts());
        SearchPanel.add(jButtonSearch);

        loadDataTable(receiptBLL.getData(receiptBLL.searchReceipts()));

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
                    PDF.exportReceiptsPDF(data, "src/main/resources/ExportPDF");
                    JOptionPane.showMessageDialog(null, "Xuất PDF danh sách hoá đơn thành công.",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                }
            });
            roundedPanel.add(panel);
        }
    }

    public void refresh() {
        jTextFieldSearch.setText("");
        datePicker.clearSelectedDate();
        loadDataTable(receiptBLL.getData(receiptBLL.searchReceipts()));

    }

    private void searchReceipts() {
        List<Receipt> receiptList = receiptBLL.searchReceipts();
        if (jTextFieldSearch.getText().isEmpty() && datePicker.getDateSQL_Between() == null) {
            loadDataTable(receiptBLL.getData(receiptList));
        } else {
            if (!jTextFieldSearch.getText().isEmpty()) {
                List<Integer> staffIDList = new ArrayList<>();
                for (Staff staff : staffBLL.findStaffs("name", jTextFieldSearch.getText()))
                    staffIDList.add(staff.getId());
                receiptList.removeIf(receipt -> !staffIDList.contains(receipt.getStaff_id()));
            }
            if (datePicker.getDateSQL_Between() != null) {
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                Date startDate = datePicker.getDateSQL_Between()[0];
                Date endDate = datePicker.getDateSQL_Between()[1];
                if (startDate.after(endDate)) {
                    JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước ngày kết thúc.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                receiptList.removeIf(receipt -> (receipt.getInvoice_date().toLocalDate().isBefore(LocalDate.parse(startDate.toString(), myFormatObj)) || receipt.getInvoice_date().toLocalDate().isAfter(LocalDate.parse(endDate.toString(), myFormatObj))));
            }
            loadDataTable(receiptBLL.getData(receiptList));
        }
    }

    public void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);

        if (objects.length == 0) {
            return;
        }

        data = new Object[objects.length][objects[0].length];
        List<Staff> staffList = staffBLL.searchStaffs("deleted = 0");

        for (int i = 0; i < objects.length; i++) {
            System.arraycopy(objects[i], 0, data[i], 0, objects[i].length);

            int staff_id = Integer.parseInt(data[i][1].toString());
            for (Staff staff : staffList) {
                if (staff.getId() == staff_id) {
                    data[i][1] = staff.getName();
                    break;
                }
            }

            if (detail) {
                JLabel iconDetail = new JLabel(new FlatSVGIcon("icon/detail.svg"));
                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = iconDetail;
            }
        }

        for (Object[] object : data) {
            model.addRow(object);
        }
    }

    private void selectFunction() {
        int indexRow = dataTable.getSelectedRow();
        int indexColumn = dataTable.getSelectedColumn();

        if (detail && indexColumn == indexColumnDetail) {
            new DetailReceiptGUI(receiptBLL.searchReceipts("id = " + data[indexRow][0]).get(0));
        }
    }

}
