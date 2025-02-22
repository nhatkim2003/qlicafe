package com.coffee.GUI;

import com.coffee.BLL.AccountBLL;
import com.coffee.BLL.StaffBLL;
import com.coffee.DTO.Account;
import com.coffee.DTO.Function;
import com.coffee.DTO.Staff;
import com.coffee.GUI.DialogGUI.FormAddGUI.AddAccountGUI;
import com.coffee.GUI.components.DataTable;
import com.coffee.GUI.components.Layout1;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.GUI.components.RoundedScrollPane;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AccountGUI extends Layout1 {
    private RoundedPanel containerSearch;
    private JLabel iconSearch;
    private JTextField jTextFieldSearch;
    private JButton jButtonSearch;
    private JComboBox<String> jComboBoxSearch;
    private List<Function> functions;
    private StaffBLL staffBLL = new StaffBLL();
    private AccountBLL accountBLL = new AccountBLL();
    private DataTable dataTable;
    private RoundedScrollPane scrollPane;

    public AccountGUI(List<Function> functions) {
        super();
        this.functions = functions;
        init(functions);
    }

    private void init(List<Function> functions) {
        containerSearch = new RoundedPanel();
        iconSearch = new JLabel();
        jTextFieldSearch = new JTextField();
        jButtonSearch = new JButton("Tìm kiếm");
        jComboBoxSearch = new JComboBox<>(new String[]{"Tài khoản", "Nhân viên"});
        dataTable = new DataTable(new Object[][]{},
                new String[]{"Mã tài khoản", "Tên tài khoản", "Nhân viên"});
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.setPreferredSize(new Dimension(1165, 680));
        bottom.add(scrollPane, BorderLayout.CENTER);

        containerSearch.setLayout(new MigLayout("", "10[]10[]10", ""));
        containerSearch.setBackground(new Color(245, 246, 250));
        containerSearch.setPreferredSize(new Dimension(280, 40));
        SearchPanel.add(containerSearch);

        iconSearch.setIcon(new FlatSVGIcon("icon/search.svg"));
        containerSearch.add(iconSearch);

        jTextFieldSearch.setBackground(new Color(245, 246, 250));
        jTextFieldSearch.setBorder(BorderFactory.createEmptyBorder());
        jTextFieldSearch.putClientProperty("JTextField.placeholderText", "Nhập nội dung tìm kiếm");
        jTextFieldSearch.putClientProperty("JTextField.showClearButton", true);
        jTextFieldSearch.setPreferredSize(new Dimension(250, 30));
        jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchAccounts();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchAccounts();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchAccounts();
            }
        });
        containerSearch.add(jTextFieldSearch);

        jButtonSearch.setBackground(new Color(1, 120, 220));
        jButtonSearch.setForeground(Color.white);
        jButtonSearch.setPreferredSize(new Dimension(100, 30));
        jButtonSearch.addActionListener(e -> searchAccounts());
        SearchPanel.add(jButtonSearch);

        jComboBoxSearch.setBackground(new Color(1, 120, 220));
        jComboBoxSearch.setForeground(Color.white);
        jComboBoxSearch.setPreferredSize(new Dimension(100, 30));
        jComboBoxSearch.addActionListener(e -> selectSearchFilter());
        SearchPanel.add(jComboBoxSearch);

        loadDataTable(accountBLL.getData(accountBLL.searchAccounts()));

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
                    new AddAccountGUI();
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
//        if (functions.stream().anyMatch(f -> f.getName().equals("excel"))) {
//            RoundedPanel roundedPanel = new RoundedPanel();
//            roundedPanel.setLayout(new GridBagLayout());
//            roundedPanel.setPreferredSize(new Dimension(130, 40));
//            roundedPanel.setBackground(new Color(1, 120, 220));
//            roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
//            FunctionPanel.add(roundedPanel);
//
//            JLabel panel = new JLabel("Nhập Excel");
//            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
//            panel.setForeground(Color.white);panel.setIcon(new FlatSVGIcon("icon/import.svg"));
//            roundedPanel.add(panel);
//        }
//        if (functions.stream().anyMatch(f -> f.getName().equals("pdf"))) {
//            RoundedPanel roundedPanel = new RoundedPanel();
//            roundedPanel.setLayout(new GridBagLayout());
//            roundedPanel.setPreferredSize(new Dimension(130, 40));
//            roundedPanel.setBackground(new Color(1, 120, 220));
//            roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
//            FunctionPanel.add(roundedPanel);
//
//            JLabel panel = new JLabel("Xuất PDF");
//            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
//            panel.setForeground(Color.white);panel.setIcon(new FlatSVGIcon("icon/export.svg"));
//            roundedPanel.add(panel);
//        }
    }

    public void refresh() {
        jTextFieldSearch.setText("");
        jComboBoxSearch.setSelectedIndex(0);
        loadDataTable(accountBLL.getData(accountBLL.searchAccounts()));
    }

    private void searchAccounts() {
        if (jTextFieldSearch.getText().isEmpty()) {
            loadDataTable(accountBLL.getData(accountBLL.searchAccounts()));
        } else {
            selectSearchFilter();
        }
    }

    private void selectSearchFilter() {
        if (Objects.requireNonNull(jComboBoxSearch.getSelectedItem()).toString().contains("Nhân viên")) {
            searchAccountsByStaff();
        } else {
            loadDataTable(accountBLL.getData(accountBLL.findAccounts("username", jTextFieldSearch.getText())));
        }
    }

    private void searchAccountsByStaff() {
        List<Account> accountList = new ArrayList<>();
        for (Staff staff : staffBLL.searchStaffs("deleted = 0")) {
            if (staff.getName().toLowerCase().contains(jTextFieldSearch.getText().toLowerCase())) {
                accountList.add(accountBLL.findAccountsBy(Map.of("staff_id", staff.getId())).get(0));
            }
        }
        loadDataTable(accountBLL.getData(accountList));
    }

    public void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        for (Object[] object : objects) {
            int staffId = Integer.parseInt(object[2].toString());
            object[2] = staffBLL.findStaffsBy(Map.of("id", staffId)).get(0).getName();
        }

        model.setRowCount(0);
        for (Object[] object : objects) {
            model.addRow(object);
        }
    }
}
