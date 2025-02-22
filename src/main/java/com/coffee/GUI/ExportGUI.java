package com.coffee.GUI;

import com.coffee.BLL.Export_NoteBLL;
import com.coffee.BLL.StaffBLL;
import com.coffee.DTO.Function;
import com.coffee.DTO.Export_Note;
import com.coffee.DTO.Staff;
import com.coffee.GUI.DialogGUI.FormAddGUI.AddExportGUI;
import com.coffee.GUI.DialogGUI.FormDetailGUI.DetailExportGUI;

import com.coffee.GUI.components.DataTable;
import com.coffee.GUI.components.Layout2;
import com.coffee.GUI.components.*;
import com.coffee.GUI.components.RoundedScrollPane;
import com.coffee.ImportExcel.AddExportFromExcel;
import com.coffee.ImportExcel.AddImportFromExcel;
import com.coffee.main.Cafe_Application;
import com.coffee.utils.PDF;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
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
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.coffee.utils.Resource.chooseExcelFile;

public class ExportGUI extends Layout2 {

    private RoundedPanel containerSearch;
    private JLabel iconSearch;
    private JTextField jTextFieldSearch;
    private JButton jButtonSearch;
    private List<Function> functions;
    private Export_NoteBLL exportNoteBLL = new Export_NoteBLL();
    private DataTable dataTable;
    private RoundedScrollPane scrollPane;
    private int indexColumnDetail = -1;
    private boolean detail = false;
    private String[] columnNames;
    private DatePicker datePicker;
    private JFormattedTextField editor;
    private Date date;
    private StaffBLL staffBLL = new StaffBLL();
    private Object[][] data = new Object[0][0];

    public ExportGUI(List<Function> functions) {
        super();
        this.functions = functions;
        if (functions.stream().anyMatch(f -> f.getName().equals("view"))) detail = true;
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


        columnNames = new String[]{"Mã Phiếu Xuất", "Nhân Viên", "Tổng Tiền", "Ngày Xuất"};
        if (detail) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            indexColumnDetail = columnNames.length - 1;
            columnNames[indexColumnDetail] = "Xem";
        }

        dataTable = new DataTable(new Object[0][0], columnNames, e -> selectFunction(), detail, false, false, 4);

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
                searchExports();
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
                searchExports();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchExports();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchExports();
            }
        });
        containerSearch.add(jTextFieldSearch);

        jButtonSearch.setBackground(new Color(1, 120, 220));
        jButtonSearch.setForeground(Color.white);
        jButtonSearch.setPreferredSize(new Dimension(100, 30));
        jButtonSearch.addActionListener(e -> searchExports());
        SearchPanel.add(jButtonSearch);

        loadDataTable(exportNoteBLL.getData(exportNoteBLL.searchExport_Note()));

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
                    new AddExportGUI();
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
            roundedPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    File file = chooseExcelFile(null);
                    if (file != null) {
                        Pair<Boolean, String> result;
                        try {
                            result = new AddExportFromExcel().addExportFromExcel(file);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (!result.getKey()) {
                            JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Thêm phiếu xuất  thành công",
                                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            refresh();
                            if (Cafe_Application.homeGUI.indexModuleMaterialGUI != -1) {
                                MaterialGUI materialGUI = (MaterialGUI) Cafe_Application.homeGUI.allPanelModules[Cafe_Application.homeGUI.indexModuleMaterialGUI];
                                materialGUI.refresh();
                            }
                        }
                    }
                }
            });
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
                    PDF.exportExportNotePDF(data, "src/main/resources/ExportPDF");
                    JOptionPane.showMessageDialog(null, "Xuất PDF danh sách phiếu xuất hàng thành công.",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            roundedPanel.add(panel);
        }
    }

    public void refresh() {
        jTextFieldSearch.setText("");
        datePicker.clearSelectedDate();
        loadDataTable(exportNoteBLL.getData(exportNoteBLL.searchExport_Note()));
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

        if (detail && indexColumn == indexColumnDetail)
            new DetailExportGUI(exportNoteBLL.searchExport_Note("id = " + data[indexRow][0]).get(0)); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá
    }

    private void searchExports() {
        List<Export_Note> import_noteList = exportNoteBLL.searchExport_Note();
        if (jTextFieldSearch.getText().isEmpty() && datePicker.getDateSQL_Between() == null) {
            loadDataTable(exportNoteBLL.getData(import_noteList));
        } else {
            if (!jTextFieldSearch.getText().isEmpty()) {
                List<Integer> staffIDList = new ArrayList<>();
                for (Staff staff : staffBLL.findStaffs("name", jTextFieldSearch.getText()))
                    staffIDList.add(staff.getId());
                import_noteList.removeIf(import_note -> !staffIDList.contains(import_note.getStaff_id()));
            }
            if (datePicker.getDateSQL_Between() != null) {
                Date startDate = datePicker.getDateSQL_Between()[0];
                Date endDate = datePicker.getDateSQL_Between()[1];
                if (startDate.after(endDate)) {
                    JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước ngày kết thúc.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                import_noteList.removeIf(import_note -> (import_note.getInvoice_date().before(startDate) || import_note.getInvoice_date().after(endDate)));
            }
            loadDataTable(exportNoteBLL.getData(import_noteList));
        }
    }
}
