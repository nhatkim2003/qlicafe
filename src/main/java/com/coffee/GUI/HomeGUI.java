package com.coffee.GUI;

import com.coffee.BLL.*;
import com.coffee.DTO.*;
import com.coffee.DTO.Module;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.GUI.components.RoundedScrollPane;
import com.coffee.main.Cafe_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeGUI extends JFrame {
    private final StaffBLL staffBLL = new StaffBLL();
    private final RoleBLL roleBLL = new RoleBLL();
    public static Account account;
    public static Staff staff;
    public static Role role;
    private JPanel contentPanel;
    private JPanel left;
    private JPanel right;
    private JPanel jPanelLogo;
    private JPanel staffInfo;
    private RoundedPanel infor;
    private JPanel menu;
    private RoundedPanel center;
    private RoundedPanel content;
    private JPanel[] modules;
    private JPanel currentModule;
    private RoundedPanel logout;
    private JScrollPane scrollPane;
    private JLabel name = new JLabel();
    private JLabel roleName = new JLabel();
    private JLabel iconLogo;
    private JLabel iconInfo;
    private JLabel iconLogout;
    private JLabel[] moduleNames;
    public JPanel[] allPanelModules;
    private Color color;
    private Color colorOver;
    private int currentPanel = 0;
    private boolean pressover;
    private boolean over = false;
    public int indexSaleGUI = -1;
    public int indexModuleReceiptGUI = -1;
    public int indexModulePayrollGUI = -1;
    public int indexModuleLeaveOffGUI = -1;
    public int indexModuleCreateWorkScheduleGUI = -1;
    public int indexModuleMaterialGUI = -1;

    public HomeGUI() {
        initComponents();
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
        getUser();
        initMenu();
    }

    public void getUser() {
        staff = staffBLL.findStaffsBy(Map.of("id", account.getStaff_id())).get(0);

        List<Role_Detail> role_detailList = new Role_DetailBLL().searchRole_detailsByStaff(staff.getId());
        Role_Detail roleDetail = role_detailList.get(0);
        role = roleBLL.findRolesBy(Map.of("id", roleDetail.getRole_id())).get(0);

        name.setText("<html>" + staff.getName() + "</html>");
        roleName.setText("<html>Chức vụ: " + role.getName() + "</html>");
    }

    public void initComponents() {
        setIconImage(new FlatSVGIcon("image/coffee_logo.svg").getImage());
        setTitle("Hệ Thống Quản Lý Cửa Hàng Coffee");
        setResizable(false);
        setPreferredSize(new Dimension(1440, 800));
        setMinimumSize(new Dimension());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        contentPanel = new JPanel();
        left = new JPanel();
        right = new JPanel();
        jPanelLogo = new JPanel();
        staffInfo = new JPanel();
        infor = new RoundedPanel();
        menu = new JPanel();
        center = new RoundedPanel();
        content = new RoundedPanel();
        logout = new RoundedPanel();
        scrollPane = new JScrollPane(menu, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        iconInfo = new JLabel();
        iconLogo = new JLabel();
        iconLogout = new JLabel();
//        color = new Color(228, 231, 235);
//        colorOver = new Color(185, 184, 184);
        color = new Color(255, 255, 255);
        colorOver = new Color(228, 231, 235);

        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(191, 198, 208));
        setContentPane(contentPanel);

        left.setLayout(new MigLayout("", "10[]10", "10[]10[]"));
        left.setBackground(new Color(191, 198, 208));
        left.setPreferredSize(new Dimension(250, 800));
        contentPanel.add(left, BorderLayout.WEST);

        right.setLayout(new BorderLayout());
        right.setBackground(new Color(191, 198, 208));
        contentPanel.add(right, BorderLayout.CENTER);

        infor.setLayout(new MigLayout());
        infor.setBackground(new Color(255, 255, 255));
        infor.setPreferredSize(new Dimension(250, 150));
        left.add(infor, "span, wrap");

        JPanel Panel1 = new JPanel();
        Panel1.setBackground(new Color(255, 255, 255));
        Panel1.setPreferredSize(new Dimension(30, 120));
        infor.add(Panel1, "center");

        Panel1.add(new JLabel(new FlatSVGIcon("icon/ACB.svg")));

        JLabel jLabelBranch = new JLabel("Cửa Hàng Coffee");
        jLabelBranch.setFont(new Font("Inter", Font.BOLD, 15));
        infor.add(jLabelBranch, "wrap");

        jPanelLogo.setBackground(new Color(255, 255, 255));
        jPanelLogo.setPreferredSize(new Dimension(30, 120));
        infor.add(jPanelLogo, "span 1 2");

        roleName.setFont(new Font("Inter", Font.PLAIN, 15));
        infor.add(roleName, "wrap");

//        staffInfo.setBackground(new Color(255, 255, 255));
//        staffInfo.setPreferredSize(new Dimension(30, 80));
//        infor.add(staffInfo);

        iconLogo.setIcon(new FlatSVGIcon("icon/avatar.svg"));
        jPanelLogo.add(iconLogo);

//        iconInfo.setIcon(new FlatSVGIcon("icon/profile.svg"));
//        staffInfo.add(iconInfo);

        name.setFont(new Font("Inter", Font.BOLD, 15));
        infor.add(name, "wrap");

        menu.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        menu.setBackground(new Color(255, 255, 255));
        menu.setPreferredSize(new Dimension(250, 500));
        menu.setAlignmentX(Component.CENTER_ALIGNMENT);

        scrollPane.setPreferredSize(new Dimension(250, 550));
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
//        scrollPane.getViewport().setBackground(new Color(191, 198, 208));
//        scrollPane.getViewport().setBackground(new Color(255, 255, 255));
        left.add(scrollPane, "span, wrap");

        logout.setLayout(new FlowLayout(FlowLayout.CENTER));
        logout.setBackground(new Color(255, 255, 255));
        logout.setPreferredSize(new Dimension(160, 40));
        logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logout.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                exit();
            }
        });
        left.add(logout, "span, wrap, center");

        iconLogout = new JLabel("Đăng xuất");
        iconLogout.setIcon(new FlatSVGIcon("icon/logout.svg"));
        iconLogout.setPreferredSize(new Dimension(140, 30));
        iconLogout.setHorizontalAlignment(SwingConstants.LEFT);
        iconLogout.setVerticalAlignment(SwingConstants.CENTER);
        iconLogout.setFont((new Font("FlatLaf.style", Font.PLAIN, 15)));
        iconLogout.setIconTextGap(20);
        iconLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iconLogout.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                exit();
            }
        });
        logout.add(iconLogout);

        center.setLayout(new BorderLayout());
        center.setBackground(new Color(191, 198, 208));
        center.setBorder(BorderFactory.createMatteBorder(10, 0, 15, 10, new Color(191, 198, 208)));
        right.add(center, BorderLayout.CENTER);

        content.setLayout(new BorderLayout());
        content.setBackground(new Color(191, 198, 208));
        center.add(content, BorderLayout.CENTER);
    }

    private void initMenu() {
        menu.removeAll();
        Pair<List<Module>, List<List<Function>>> result = getModulesAndFunctionsFromRole(role.getId());
        List<Module> moduleList = result.getKey();
        List<List<Function>> function2D = result.getValue();

        allPanelModules = new JPanel[moduleList.size()];
        modules = new JPanel[moduleList.size()];
        moduleNames = new JLabel[moduleList.size()];
        for (int i = 0; i < modules.length; i++) {
            modules[i] = new JPanel();
            modules[i].setLayout(new FlowLayout(FlowLayout.CENTER));
            modules[i].setPreferredSize(new Dimension(250, 41));
            modules[i].setBackground(new Color(255, 255, 255));
//            modules[i].setBackground(new Color(228, 231, 235));
            modules[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            Module module = moduleList.get(i);
            List<Function> functions = function2D.get(i);
            allPanelModules[i] = getPanelModule(module.getId(), functions);
            int index = i;
            if (module.getId() == 1)
                indexSaleGUI = index;
            if (module.getId() == 2)
                indexModuleMaterialGUI = index;
            if (module.getId() == 7)
                indexModuleReceiptGUI = index;
            if (module.getId() == 13)
                indexModuleLeaveOffGUI = index;
            if (module.getId() == 18)
                indexModuleCreateWorkScheduleGUI = index;
            if (module.getId() == 19)
                indexModulePayrollGUI = index;
            modules[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!pressover && index != currentPanel) {
                        e.getComponent().setBackground(colorOver);
                        over = true;
                    }
                }

                public void mouseExited(MouseEvent e) {
                    if (!pressover && index != currentPanel) {
                        e.getComponent().setBackground(color);
                        over = false;
                    }
                }

                public void mouseReleased(MouseEvent e) {
                    if (!pressover && index != currentPanel) {
                        if (over) {
                            e.getComponent().setBackground(colorOver);
                        } else {
                            e.getComponent().setBackground(color);
                        }
                    }
                }

                public void mousePressed(MouseEvent e) {
                    openModule(allPanelModules[index]);
                    Active(modules[index]);
                    currentPanel = index;
                }
            });
            menu.add(modules[i]);

            moduleNames[i] = new JLabel(module.getName());
            moduleNames[i].setIcon(new FlatSVGIcon("icon/icon_module.svg"));
            moduleNames[i].setPreferredSize(new Dimension(230, 35));
            moduleNames[i].setHorizontalAlignment(SwingConstants.LEFT);
//            moduleNames[i].setVerticalAlignment(SwingConstants.CENTER);
            moduleNames[i].setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            moduleNames[i].setIconTextGap(15);
            moduleNames[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            modules[i].add(moduleNames[i]);
        }
        menu.setPreferredSize(new Dimension(250, Math.max(500, modules.length * 41)));
        openModule(allPanelModules[0]); // custom
        Active(modules[0]); // custom
    }

    public Pair<List<Module>, List<List<Function>>> getModulesAndFunctionsFromRole(int roleID) {
        DecentralizationBLL decentralizationBLL = new DecentralizationBLL();
        List<Decentralization> decentralizations = decentralizationBLL.searchDecentralizations("role_id = " + roleID);
        List<Module> modules = new ArrayList<>();
        List<List<Function>> function2D = new ArrayList<>();
        ModuleBLL moduleBLL = new ModuleBLL();
        FunctionBLL functionBLL = new FunctionBLL();
        for (int i = 0; i < decentralizations.size(); i++) {
            int moduleID = decentralizations.get(i).getModule_id();
            List<Function> functions = new ArrayList<>();
            boolean canView = false;
            do {
                int functionID = decentralizations.get(i).getFunction_id();
                Function function = functionBLL.findFunctionsBy(Map.of("id", functionID)).get(0);
                if (function.getId() == 1) // view
                    canView = true;
                functions.add(function);
            } while (++i < decentralizations.size() && decentralizations.get(i).getModule_id() == moduleID);
            if (canView) {
                modules.add(moduleBLL.findModulesBy(Map.of("id", moduleID)).get(0));
                function2D.add(functions);
            }
            i--;
        }
        return new Pair<>(modules, function2D);
    }

    public JPanel[] getAllPanelModules() {
        return allPanelModules;
    }

    public JPanel getPanelModule(int id, List<Function> functions) {
        return switch (id) {
            case 1 -> new SaleGUI(account);
            case 2 -> new MaterialGUI(functions);
            case 3 -> new StatisticGUI();
//            case 4 -> new StatisticSalaryGUI();
//            case 5 -> new StatisticStaffGUI();
            case 6 -> new DiscountGUI(functions);
            case 7 -> new ReceiptGUI(functions);
            case 8 -> new ExportGUI(functions);
            case 9 -> new ImportGUI(functions);
            case 10 -> new ProductGUI(functions);
            case 11 -> new SupplierGUI(functions);
            case 12 -> new StaffGUI(functions);
            case 13 -> new Leave_Of_Absence_FormGUI(functions);
            case 14 -> new AccountGUI(functions);
            case 15 -> new DecentralizationGUI(functions);
            case 16 -> new InfoGUI(account, staff);
            case 17 -> new MyWorkScheduleGUI(staff);
            case 18 -> new CreateWorkScheduleGUI();
            case 19 -> new PayrollGUI(functions);
            default -> new RoundedPanel();
        };
    }

    public void openModule(JPanel module) {
        content.removeAll();
        content.add(module, BorderLayout.CENTER);
        content.repaint();
        content.revalidate();
        System.gc();
    }

    private void Disable() {
        if (currentModule != null) {
            currentModule.setBackground(color);
        }
    }

    private void Active(JPanel module) {
        Disable();
        currentModule = module;
        module.setBackground(colorOver);
        menu.repaint();
        menu.revalidate();
    }

    public void exit() {
        int message = JOptionPane.showOptionDialog(null,
                "Bạn có chắc chắn muốn đăng xuất?",
                "Đăng xuất",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Đăng xuất", "Huỷ"},
                "Đăng xuất");
        if (message == JOptionPane.YES_OPTION) {
            dispose();
            System.gc();
            Cafe_Application.loginGUI.setVisible(true);
        }
    }

    public static void main(String[] args) {
        AccountBLL accountBLL = new AccountBLL();
        HomeGUI homeGUI = new HomeGUI();
        homeGUI.setVisible(true);
        homeGUI.setAccount(accountBLL.searchAccounts().get(0));
    }
}
