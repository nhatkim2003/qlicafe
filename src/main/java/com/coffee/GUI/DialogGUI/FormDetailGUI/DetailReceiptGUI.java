package com.coffee.GUI.DialogGUI.FormDetailGUI;

import com.coffee.BLL.*;
import com.coffee.DTO.Export_Note;
import com.coffee.DTO.Receipt;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class DetailReceiptGUI extends DialogFormDetail {
    private JLabel titleName;
    private List<JLabel> attributeReceipt;
    private Receipt_DetailBLL receipt_detailBLL = new Receipt_DetailBLL();

    private DataTable dataTable;
    private String[] columnNames;
    private RoundedScrollPane scrollPane;

    public DetailReceiptGUI(Receipt receipt) {
        super();
        super.setTitle("Thông tin hóa đơn");
        super.setSize(new Dimension(800, 600));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        init(receipt);
        setVisible(true);
    }

    private void init(Receipt receipt) {
        titleName = new JLabel();
        attributeReceipt = new ArrayList<>();
        contenttop.setLayout(new MigLayout("",
                "50[]20[]50",
                "10[]10[]10"));
        contentbot.setLayout(new MigLayout("",
                "50[]20[]50",
                "10[]10[]10"));

        titleName.setText("Thông tin Hóa Đơn");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        for (String string : new String[]{"Mã Hóa Đơn", "Nhân Viên", "Ngày Tạo"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeReceipt.add(label);
            contenttop.add(label);
            JLabel textField = new JLabel();
            textField.setPreferredSize(new Dimension(1000, 30));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            if (string.trim().equals("Ngày Tạo")) {
                textField.setText(receipt.getInvoice_date().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            }
            if (string.trim().equals("Mã Hóa Đơn")) {
                String receiptId = Integer.toString(receipt.getId());
                textField.setText(receiptId);
            }
            if (string.equals("Nhân Viên")) {
                String name = new StaffBLL().findStaffsBy(Map.of("id", receipt.getStaff_id())).get(0).getName();
                textField.setText(name);
            }
            contenttop.add(textField, "wrap");

        }

        columnNames = new String[]{"Sản Phẩm", "Size", "SL", "Giá", "Ghi Chú"};
        dataTable = new DataTable(new Object[0][0], columnNames);
        dataTable.getColumnModel().getColumn(1).setMaxWidth(50);
        dataTable.getColumnModel().getColumn(2).setMaxWidth(50);
        dataTable.getColumnModel().getColumn(3).setMaxWidth(150);
        dataTable.getColumnModel().getColumn(1).setMinWidth(50);
        dataTable.getColumnModel().getColumn(2).setMinWidth(50);
        dataTable.getColumnModel().getColumn(3).setMinWidth(150);
        dataTable.setRowHeight(25);

        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1165, 680));
        contentmid.add(scrollPane, BorderLayout.CENTER);

        loadDataTable(receipt_detailBLL.getData(receipt_detailBLL.findReceipt_DetailsBy(Map.of("receipt_id", receipt.getId()))));

        for (String string : new String[]{"Tổng Cộng", "Khuyến Mãi", "Thành Tiền", "Tiền Nhận", "Tiền Thừa"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 14)));
            attributeReceipt.add(label);
            contentbot.add(label);

            JLabel textField = new JLabel();

            if (string.equals("Tổng Cộng")) {
                textField.setText(VNString.currency(receipt.getTotal_price()));
            }
            if (string.equals("Khuyến Mãi")) {
                if (receipt.getDiscount_id() == 0)
                    textField.setText(VNString.currency(receipt.getTotal_discount()));
                else
                    textField.setText("-" + VNString.currency(receipt.getTotal_discount()) + " (Mã giảm giá: " + receipt.getDiscount_id() + ")");
            }
            if (string.equals("Thành Tiền")) {
                textField.setText(VNString.currency(receipt.getTotal()));
            }
            if (string.equals("Tiền Nhận")) {
                textField.setText(VNString.currency(receipt.getReceived()));
            }
            if (string.equals("Tiền Thừa")) {
                textField.setText(VNString.currency(receipt.getExcess()));
            }

            textField.setPreferredSize(new Dimension(1000, 30));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            contentbot.add(textField, "wrap");
        }

        RoundedPanel roundedPanel = new RoundedPanel();
        roundedPanel.setLayout(new GridBagLayout());
        roundedPanel.setPreferredSize(new Dimension(150, 40));
        roundedPanel.setBackground(new Color(1, 120, 220));
        roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        containerButton.add(roundedPanel);

        JLabel panel = new JLabel("In hoá đơn");
        panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
        panel.setForeground(Color.white);
        panel.setIcon(new FlatSVGIcon("icon/print.svg"));
        roundedPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                PDF.exportReceiptDetialsPDF(receipt, "src/main/resources/ExportPDF");
                JOptionPane.showMessageDialog(null, "In hoá đơn thành công.",
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

            int product_id = Integer.parseInt(data[i][1].toString());
            data[i][1] = "<html>" + new ProductBLL().findProductsBy(Map.of("id", product_id)).get(0).getName() + "</html>";

            if (data[i][2].equals("0"))
                data[i][2] = "";
        }

        for (Object[] object : data) {
            object = Arrays.copyOfRange(object, 1, 6);
            model.addRow(object);
        }
    }
}
