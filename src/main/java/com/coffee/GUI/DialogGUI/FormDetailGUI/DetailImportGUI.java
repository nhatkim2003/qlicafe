package com.coffee.GUI.DialogGUI.FormDetailGUI;

import com.coffee.BLL.*;
import com.coffee.DTO.Import_Note;
import com.coffee.GUI.DialogGUI.DialogFormDetail;
import com.coffee.GUI.components.DataTable;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.GUI.components.RoundedScrollPane;
import com.coffee.main.Cafe_Application;
import com.coffee.utils.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class DetailImportGUI extends DialogFormDetail {
    private JLabel titleName;
    private List<JLabel> attributeImport_Note;
    private Import_NoteBLL import_NoteBLL = new Import_NoteBLL();
    private ShipmentBLL shipmentBLL = new ShipmentBLL();
    private DataTable dataTable;
    private String[] columnNames;
    private RoundedScrollPane scrollPane;

    public DetailImportGUI(Import_Note import_note) {
        super();
        super.setTitle("Thông Tin Phiếu Nhập");
        super.setSize(new Dimension(1100, 700));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        init(import_note);
        setVisible(true);
    }

    private void init(Import_Note import_note) {
        titleName = new JLabel();
        attributeImport_Note = new ArrayList<>();
        contenttop.setLayout(new MigLayout("",
                "50[]20[]50",
                "10[]10[]10"));
        contentbot.setLayout(new MigLayout("",
                "50[]20[]50",
                "10[]10[]10"));

        titleName.setText("Thông Tin Phiếu Nhập");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        for (String string : new String[]{"Mã Phiếu Nhập", "Nhân Viên", "Ngày Nhập"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeImport_Note.add(label);
            contenttop.add(label);
            JLabel textField = new JLabel();
            textField.setPreferredSize(new Dimension(1000, 30));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            if (string.trim().equals("Ngày Nhập")) {
                textField.setText(import_note.getReceived_date().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            }
            if (string.trim().equals("Mã Phiếu Nhập")) {
                String import_noteId = Integer.toString(import_note.getId());
                textField.setText(import_noteId);
            }
            if (string.equals("Nhân Viên")) {
                String name = new StaffBLL().findStaffsBy(Map.of("id", import_note.getStaff_id())).get(0).getName();
                textField.setText(name);
            }
            contenttop.add(textField, "wrap");

        }

        columnNames = new String[]{"Mã Lô", "Nguyên Liệu", "Tên NCC", "SL Nhập", "SL Tồn", "MFG", "EXP",};
        dataTable = new DataTable(new Object[0][0], columnNames);
        dataTable.getColumnModel().getColumn(0).setMaxWidth(100);
        dataTable.getColumnModel().getColumn(1).setMaxWidth(500);
        dataTable.getColumnModel().getColumn(2).setMaxWidth(500);
        dataTable.getColumnModel().getColumn(3).setMaxWidth(100);
        dataTable.getColumnModel().getColumn(4).setMaxWidth(100);
        dataTable.getColumnModel().getColumn(5).setMaxWidth(250);
        dataTable.getColumnModel().getColumn(6).setMaxWidth(250);
        dataTable.setRowHeight(25);
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1165, 680));
        contentmid.add(scrollPane, BorderLayout.CENTER);

        loadDataTable(shipmentBLL.getData(shipmentBLL.findShipmentsBy(Map.of("import_id", import_note.getId()))));

        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(170, 30));
        label.setText("Tổng Tiền");
        label.setFont((new Font("Public Sans", Font.BOLD, 16)));
        contentbot.add(label);

        JLabel textField = new JLabel();
        String total = VNString.currency(Double.parseDouble(import_note.getTotal().toString()));
        textField.setText(total);
        textField.setPreferredSize(new Dimension(1000, 30));
        textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
        textField.setBackground(new Color(245, 246, 250));
        contentbot.add(textField, "wrap");

        RoundedPanel roundedPanel = new RoundedPanel();
        roundedPanel.setLayout(new GridBagLayout());
        roundedPanel.setPreferredSize(new Dimension(150, 40));
        roundedPanel.setBackground(new Color(1, 120, 220));
        roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        containerButton.add(roundedPanel);

        JLabel panel = new JLabel("In phiếu nhập");
        panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
        panel.setForeground(Color.white);
        panel.setIcon(new FlatSVGIcon("icon/print.svg"));
        roundedPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                PDF.exportImportDetailsPDF(import_note, "src/main/resources/ExportPDF");
                JOptionPane.showMessageDialog(null, "In phiếu nhập thành công.",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });


        roundedPanel.add(panel);
    }


    public void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);

        if (objects.length == 0) {
            return;
        }

        Object[][] data = new Object[objects.length][objects[0].length];

        for (int i = 0; i < objects.length; i++) {
            System.arraycopy(objects[i], 0, data[i], 0, objects[i].length);

            int material_id = Integer.parseInt(data[i][1].toString());
            data[i][1] = "<html>" + new MaterialBLL().findMaterialsBy(Map.of("id", material_id)).get(0).getName() + "</html>";

            int supplier_id = Integer.parseInt(data[i][2].toString());
            data[i][2] = "<html>" + new SupplierBLL().findSuppliersBy(Map.of("id", supplier_id)).get(0).getName() + "</html>";

        }


        for (Object[] object : data) {
            Object[] objects1 = object;
            System.arraycopy(objects1, 0, object, 0, 3);
            System.arraycopy(objects1, 4, object, 3, 4);
            object = Arrays.copyOfRange(object, 0, 7);
            model.addRow(object);
        }
    }


}
