package com.coffee.GUI.DialogGUI.FormDetailGUI;

import com.coffee.BLL.*;
import com.coffee.DTO.Export_Note;
import com.coffee.DTO.Shipment;
import com.coffee.GUI.DialogGUI.DialogFormDetail;
import com.coffee.GUI.components.DataTable;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.GUI.components.RoundedScrollPane;
import com.coffee.main.Cafe_Application;

import com.coffee.utils.PDF;
import com.coffee.utils.Resource;
import com.coffee.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DetailExportGUI extends DialogFormDetail {
    private JLabel titleName;
    private List<JLabel> attributeExport_Note;
    private Export_DetailBLL export_detailBLL = new Export_DetailBLL();

    private DataTable dataTable;
    private String[] columnNames;
    private RoundedScrollPane scrollPane;

    public DetailExportGUI(Export_Note export) {
        super();
        super.setTitle("Thông Tin Phiếu Xuất");
        super.setSize(new Dimension(700, 700));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        init(export);
        setVisible(true);
    }

    private void init(Export_Note export) {
        titleName = new JLabel();
        attributeExport_Note = new ArrayList<>();
        contenttop.setLayout(new MigLayout("",
                "50[]20[]50",
                "10[]10[]10"));
        contentbot.setLayout(new MigLayout("",
                "50[]20[]50",
                "10[]10[]10"));

        titleName.setText("Thông Tin Phiếu Xuất");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        for (String string : new String[]{"Mã Phiếu Xuất", "Nhân Viên", "Ngày Tạo"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeExport_Note.add(label);
            contenttop.add(label);
            JLabel textField = new JLabel();
            textField.setPreferredSize(new Dimension(1000, 30));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            if (string.trim().equals("Ngày Tạo")) {
                textField.setText(new SimpleDateFormat("dd/MM/yyyy").format(export.getInvoice_date()));
            }
            if (string.trim().equals("Mã Phiếu Xuất")) {
                String exportId = Integer.toString(export.getId());
                textField.setText(exportId);
            }
            if (string.equals("Nhân Viên")) {
                String name = new StaffBLL().findStaffsBy(Map.of("id", export.getStaff_id())).get(0).getName();
                textField.setText(name);
            }
            contenttop.add(textField, "wrap");

        }

        columnNames = new String[]{"Mã lô", "Nguyên Liệu", "SL", "Lý Do",};
        dataTable = new DataTable(new Object[0][0], columnNames);
        dataTable.getColumnModel().getColumn(0).setMaxWidth(100);
        dataTable.getColumnModel().getColumn(1).setMaxWidth(500);
        dataTable.getColumnModel().getColumn(2).setMaxWidth(50);
        dataTable.getColumnModel().getColumn(3).setMaxWidth(200);
        dataTable.setRowHeight(25);
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1165, 680));
        contentmid.add(scrollPane, BorderLayout.CENTER);

        loadDataTable(export_detailBLL.getData(export_detailBLL.findExportBy(Map.of("export_id", export.getId()))));

        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(170, 30));
        label.setText("Tổng Tiền");
        label.setFont((new Font("Public Sans", Font.BOLD, 16)));
        contentbot.add(label);

        JLabel textField = new JLabel();
        String total = VNString.currency(Double.parseDouble(export.getTotal().toString()));
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

        JLabel panel = new JLabel("In phiếu xuất");
        panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
        panel.setForeground(Color.white);
        panel.setIcon(new FlatSVGIcon("icon/print.svg"));
        roundedPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                PDF.exportExportDetailsPDF(export, "src/main/resources/ExportPDF");
                JOptionPane.showMessageDialog(null, "In phiếu xuất thành công.",
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

            int shipment_id = Integer.parseInt(data[i][1].toString());
            Shipment shipment = new ShipmentBLL().findShipmentsBy(Map.of("id", shipment_id)).get(0);
            data[i][2] = "<html>" + new MaterialBLL().findMaterialsBy(Map.of("id", shipment.getMaterial_id())).get(0).getName() + "</html>";

            if (data[i][2].equals("0"))
                data[i][2] = "";
        }

        for (Object[] object : data) {
            object = Arrays.copyOfRange(object, 1, 5);
            model.addRow(object);
        }
    }
}
