package com.coffee.GUI.components;

import raven.datetime.component.date.DateSelectionAble;
import raven.datetime.component.date.DateSelectionListener;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.PanelUI;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.*;
import java.awt.im.InputContext;
import java.awt.im.InputMethodRequests;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.EventListener;
import java.util.Locale;
import java.util.Set;

public class DatePicker extends raven.datetime.component.date.DatePicker {
    public DatePicker() {
        super();
        setSeparator("  ―  ");
        setOpaque(false);
    }

    @Override
    public void setToBack() {
        super.setToBack();
    }

    @Override
    public void setToForward() {
        super.setToForward();
    }

    @Override
    public void selectMonth() {
        super.selectMonth();
    }

    @Override
    public void selectYear() {
        super.selectYear();
    }

    @Override
    protected void runEventDateChanged() {
        super.runEventDateChanged();
    }

    @Override
    public DateSelectionMode getDateSelectionMode() {
        return super.getDateSelectionMode();
    }

    @Override
    public void setDateSelectionMode(DateSelectionMode dateSelectionMode) {
        super.setDateSelectionMode(dateSelectionMode);
    }

    @Override
    public void now() {
        super.now();
    }

    @Override
    public void selectCurrentMonth() {
        super.selectCurrentMonth();
    }

    public void setSelectedDate(Date date) {
        super.setSelectedDate(LocalDate.parse(date.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public void setSelectedDateRange(Date from, Date to) {
        super.setSelectedDateRange(LocalDate.parse(from.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalDate.parse(to.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @Override
    public void setEditor(JFormattedTextField editor) {
        super.setEditor(editor);
    }

    @Override
    public DateSelectionAble getDateSelectionAble() {
        return super.getDateSelectionAble();
    }

    @Override
    public void setDateSelectionAble(DateSelectionAble dateSelectionAble) {
        super.setDateSelectionAble(dateSelectionAble);
    }

    @Override
    public void showPopup() {
        super.showPopup();
    }

    @Override
    public void closePopup() {
        super.closePopup();
    }

    @Override
    public void setSeparator(String separator) {
        super.setSeparator(separator);
    }

    @Override
    public boolean isUsePanelOption() {
        return super.isUsePanelOption();
    }

    @Override
    public void setUsePanelOption(boolean usePanelOption) {
        super.setUsePanelOption(usePanelOption);
    }

    @Override
    public boolean isCloseAfterSelected() {
        return super.isCloseAfterSelected();
    }

    @Override
    public void setCloseAfterSelected(boolean closeAfterSelected) {
        super.setCloseAfterSelected(closeAfterSelected);
    }

    @Override
    public String getSeparator() {
        return super.getSeparator();
    }

    @Override
    public void clearSelectedDate() {
        super.clearSelectedDate();
    }

    @Override
    public boolean isDateSelected() {
        return super.isDateSelected();
    }

    @Override
    public LocalDate getSelectedDate() {
        return super.getSelectedDate();
    }

    @Override
    public LocalDate[] getSelectedDateRange() {
        return super.getSelectedDateRange();
    }

    @Override
    public String getSelectedDateAsString() {
        return super.getSelectedDateAsString();
    }

    @Override
    public void slideTo(LocalDate date) {
        super.slideTo(date);
    }

    @Override
    public void addDateSelectionListener(DateSelectionListener event) {
        super.addDateSelectionListener(event);
    }

    @Override
    public void removeDateSelectionListener(DateSelectionListener event) {
        super.removeDateSelectionListener(event);
    }

    @Override
    public void removeAllDateSelectionListener() {
        super.removeAllDateSelectionListener();
    }

    @Override
    public void updateUI() {
        super.updateUI();
    }

    @Override
    public PanelUI getUI() {
        return super.getUI();
    }

    @Override
    public void setUI(PanelUI ui) {
        super.setUI(ui);
    }

    @Override
    public String getUIClassID() {
        return super.getUIClassID();
    }

    @Override
    protected String paramString() {
        return super.paramString();
    }

    @Override
    public AccessibleContext getAccessibleContext() {
        return super.getAccessibleContext();
    }

    @Override
    public void setInheritsPopupMenu(boolean value) {
        super.setInheritsPopupMenu(value);
    }

    @Override
    public boolean getInheritsPopupMenu() {
        return super.getInheritsPopupMenu();
    }

    @Override
    public void setComponentPopupMenu(JPopupMenu popup) {
        super.setComponentPopupMenu(popup);
    }

    @Override
    public JPopupMenu getComponentPopupMenu() {
        return super.getComponentPopupMenu();
    }

    @Override
    protected void setUI(ComponentUI newUI) {
        super.setUI(newUI);
    }

    @Override
    protected Graphics getComponentGraphics(Graphics g) {
        return super.getComponentGraphics(g);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    public void printAll(Graphics g) {
        super.printAll(g);
    }

    @Override
    public void print(Graphics g) {
        super.print(g);
    }

    @Override
    protected void printComponent(Graphics g) {
        super.printComponent(g);
    }

    @Override
    protected void printChildren(Graphics g) {
        super.printChildren(g);
    }

    @Override
    protected void printBorder(Graphics g) {
        super.printBorder(g);
    }

    @Override
    public boolean isPaintingTile() {
        return super.isPaintingTile();
    }

    @Override
    public boolean isManagingFocus() {
        return super.isManagingFocus();
    }

    @Override
    public void setNextFocusableComponent(Component aComponent) {
        super.setNextFocusableComponent(aComponent);
    }

    @Override
    public Component getNextFocusableComponent() {
        return super.getNextFocusableComponent();
    }

    @Override
    public void setRequestFocusEnabled(boolean requestFocusEnabled) {
        super.setRequestFocusEnabled(requestFocusEnabled);
    }

    @Override
    public boolean isRequestFocusEnabled() {
        return super.isRequestFocusEnabled();
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
    }

    @Override
    public boolean requestFocus(boolean temporary) {
        return super.requestFocus(temporary);
    }

    @Override
    public boolean requestFocusInWindow() {
        return super.requestFocusInWindow();
    }

    @Override
    protected boolean requestFocusInWindow(boolean temporary) {
        return super.requestFocusInWindow(temporary);
    }

    @Override
    public void grabFocus() {
        super.grabFocus();
    }

    @Override
    public void setVerifyInputWhenFocusTarget(boolean verifyInputWhenFocusTarget) {
        super.setVerifyInputWhenFocusTarget(verifyInputWhenFocusTarget);
    }

    @Override
    public boolean getVerifyInputWhenFocusTarget() {
        return super.getVerifyInputWhenFocusTarget();
    }

    @Override
    public FontMetrics getFontMetrics(Font font) {
        return super.getFontMetrics(font);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
    }

    @Override
    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }

    @Override
    public void setMaximumSize(Dimension maximumSize) {
        super.setMaximumSize(maximumSize);
    }

    @Override
    public Dimension getMaximumSize() {
        return super.getMaximumSize();
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        super.setMinimumSize(minimumSize);
    }

    @Override
    public Dimension getMinimumSize() {
        return super.getMinimumSize();
    }

    @Override
    public boolean contains(int x, int y) {
        return super.contains(x, y);
    }

    @Override
    public void setBorder(Border border) {
        super.setBorder(border);
    }

    @Override
    public Border getBorder() {
        return super.getBorder();
    }

    @Override
    public Insets getInsets() {
        return super.getInsets();
    }

    @Override
    public Insets getInsets(Insets insets) {
        return super.getInsets(insets);
    }

    @Override
    public float getAlignmentY() {
        return super.getAlignmentY();
    }

    @Override
    public void setAlignmentY(float alignmentY) {
        super.setAlignmentY(alignmentY);
    }

    @Override
    public float getAlignmentX() {
        return super.getAlignmentX();
    }

    @Override
    public void setAlignmentX(float alignmentX) {
        super.setAlignmentX(alignmentX);
    }

    @Override
    public void setInputVerifier(InputVerifier inputVerifier) {
        super.setInputVerifier(inputVerifier);
    }

    @Override
    public InputVerifier getInputVerifier() {
        return super.getInputVerifier();
    }

    @Override
    public Graphics getGraphics() {
        return super.getGraphics();
    }

    @Override
    public void setDebugGraphicsOptions(int debugOptions) {
        super.setDebugGraphicsOptions(debugOptions);
    }

    @Override
    public int getDebugGraphicsOptions() {
        return super.getDebugGraphicsOptions();
    }

    @Override
    public void registerKeyboardAction(ActionListener anAction, String aCommand, KeyStroke aKeyStroke, int aCondition) {
        super.registerKeyboardAction(anAction, aCommand, aKeyStroke, aCondition);
    }

    @Override
    public void registerKeyboardAction(ActionListener anAction, KeyStroke aKeyStroke, int aCondition) {
        super.registerKeyboardAction(anAction, aKeyStroke, aCondition);
    }

    @Override
    public void unregisterKeyboardAction(KeyStroke aKeyStroke) {
        super.unregisterKeyboardAction(aKeyStroke);
    }

    @Override
    public KeyStroke[] getRegisteredKeyStrokes() {
        return super.getRegisteredKeyStrokes();
    }

    @Override
    public int getConditionForKeyStroke(KeyStroke aKeyStroke) {
        return super.getConditionForKeyStroke(aKeyStroke);
    }

    @Override
    public ActionListener getActionForKeyStroke(KeyStroke aKeyStroke) {
        return super.getActionForKeyStroke(aKeyStroke);
    }

    @Override
    public void resetKeyboardActions() {
        super.resetKeyboardActions();
    }

    @Override
    public int getBaseline(int width, int height) {
        return super.getBaseline(width, height);
    }

    @Override
    public BaselineResizeBehavior getBaselineResizeBehavior() {
        return super.getBaselineResizeBehavior();
    }

    @Override
    public boolean requestDefaultFocus() {
        return super.requestDefaultFocus();
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
    }

    @Override
    protected void processComponentKeyEvent(KeyEvent e) {
        super.processComponentKeyEvent(e);
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
    }

    @Override
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        return super.processKeyBinding(ks, e, condition, pressed);
    }

    @Override
    public void setToolTipText(String text) {
        super.setToolTipText(text);
    }

    @Override
    public String getToolTipText() {
        return super.getToolTipText();
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        return super.getToolTipText(event);
    }

    @Override
    public Point getToolTipLocation(MouseEvent event) {
        return super.getToolTipLocation(event);
    }

    @Override
    public Point getPopupLocation(MouseEvent event) {
        return super.getPopupLocation(event);
    }

    @Override
    public JToolTip createToolTip() {
        return super.createToolTip();
    }

    @Override
    public void scrollRectToVisible(Rectangle aRect) {
        super.scrollRectToVisible(aRect);
    }

    @Override
    public void setAutoscrolls(boolean autoscrolls) {
        super.setAutoscrolls(autoscrolls);
    }

    @Override
    public boolean getAutoscrolls() {
        return super.getAutoscrolls();
    }

    @Override
    public void setTransferHandler(TransferHandler newHandler) {
        super.setTransferHandler(newHandler);
    }

    @Override
    public TransferHandler getTransferHandler() {
        return super.getTransferHandler();
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        super.processMouseMotionEvent(e);
    }

    @Override
    public void enable() {
        super.enable();
    }

    @Override
    public void disable() {
        super.disable();
    }

    @Override
    public void setFocusTraversalKeys(int id, Set<? extends AWTKeyStroke> keystrokes) {
        super.setFocusTraversalKeys(id, keystrokes);
    }

    @Override
    public void reshape(int x, int y, int w, int h) {
        super.reshape(x, y, w, h);
    }

    @Override
    public Rectangle getBounds(Rectangle rv) {
        return super.getBounds(rv);
    }

    @Override
    public Dimension getSize(Dimension rv) {
        return super.getSize(rv);
    }

    @Override
    public Point getLocation(Point rv) {
        return super.getLocation(rv);
    }

    @Override
    public int getX() {
        return super.getX();
    }

    @Override
    public int getY() {
        return super.getY();
    }

    @Override
    public int getWidth() {
        return super.getWidth();
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }

    @Override
    public boolean isOpaque() {
        return super.isOpaque();
    }

    @Override
    public void setOpaque(boolean isOpaque) {
        super.setOpaque(isOpaque);
    }

    @Override
    public void computeVisibleRect(Rectangle visibleRect) {
        super.computeVisibleRect(visibleRect);
    }

    @Override
    public Rectangle getVisibleRect() {
        return super.getVisibleRect();
    }

    @Override
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    public void firePropertyChange(String propertyName, char oldValue, char newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    protected void fireVetoableChange(String propertyName, Object oldValue, Object newValue) throws PropertyVetoException {
        super.fireVetoableChange(propertyName, oldValue, newValue);
    }

    @Override
    public synchronized void addVetoableChangeListener(VetoableChangeListener listener) {
        super.addVetoableChangeListener(listener);
    }

    @Override
    public synchronized void removeVetoableChangeListener(VetoableChangeListener listener) {
        super.removeVetoableChangeListener(listener);
    }

    @Override
    public synchronized VetoableChangeListener[] getVetoableChangeListeners() {
        return super.getVetoableChangeListeners();
    }

    @Override
    public Container getTopLevelAncestor() {
        return super.getTopLevelAncestor();
    }

    @Override
    public void addAncestorListener(AncestorListener listener) {
        super.addAncestorListener(listener);
    }

    @Override
    public void removeAncestorListener(AncestorListener listener) {
        super.removeAncestorListener(listener);
    }

    @Override
    public AncestorListener[] getAncestorListeners() {
        return super.getAncestorListeners();
    }

    @Override
    public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
        return super.getListeners(listenerType);
    }

    @Override
    public void addNotify() {
        super.addNotify();
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
    }

    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
        super.repaint(tm, x, y, width, height);
    }

    @Override
    public void repaint(Rectangle r) {
        super.repaint(r);
    }

    @Override
    public void revalidate() {
        super.revalidate();
    }

    @Override
    public boolean isValidateRoot() {
        return super.isValidateRoot();
    }

    @Override
    public boolean isOptimizedDrawingEnabled() {
        return super.isOptimizedDrawingEnabled();
    }

    @Override
    protected boolean isPaintingOrigin() {
        return super.isPaintingOrigin();
    }

    @Override
    public void paintImmediately(int x, int y, int w, int h) {
        super.paintImmediately(x, y, w, h);
    }

    @Override
    public void paintImmediately(Rectangle r) {
        super.paintImmediately(r);
    }

    @Override
    public void setDoubleBuffered(boolean aFlag) {
        super.setDoubleBuffered(aFlag);
    }

    @Override
    public boolean isDoubleBuffered() {
        return super.isDoubleBuffered();
    }

    @Override
    public JRootPane getRootPane() {
        return super.getRootPane();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public int getComponentCount() {
        return super.getComponentCount();
    }

    @Override
    public int countComponents() {
        return super.countComponents();
    }

    @Override
    public Component getComponent(int n) {
        return super.getComponent(n);
    }

    @Override
    public Component[] getComponents() {
        return super.getComponents();
    }

    @Override
    public Insets insets() {
        return super.insets();
    }

    @Override
    public Component add(Component comp) {
        return super.add(comp);
    }

    @Override
    public Component add(String name, Component comp) {
        return super.add(name, comp);
    }

    @Override
    public Component add(Component comp, int index) {
        return super.add(comp, index);
    }

    @Override
    public void setComponentZOrder(Component comp, int index) {
        super.setComponentZOrder(comp, index);
    }

    @Override
    public int getComponentZOrder(Component comp) {
        return super.getComponentZOrder(comp);
    }

    @Override
    public void add(Component comp, Object constraints) {
        super.add(comp, constraints);
    }

    @Override
    public void add(Component comp, Object constraints, int index) {
        super.add(comp, constraints, index);
    }

    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        super.addImpl(comp, constraints, index);
    }

    @Override
    public void remove(int index) {
        super.remove(index);
    }

    @Override
    public void remove(Component comp) {
        super.remove(comp);
    }

    @Override
    public void removeAll() {
        super.removeAll();
    }

    @Override
    public LayoutManager getLayout() {
        return super.getLayout();
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        super.setLayout(mgr);
    }

    @Override
    public void doLayout() {
        super.doLayout();
    }

    @Override
    public void layout() {
        super.layout();
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    public void validate() {
        super.validate();
    }

    @Override
    protected void validateTree() {
        super.validateTree();
    }

    @Override
    public Dimension preferredSize() {
        return super.preferredSize();
    }

    @Override
    public Dimension minimumSize() {
        return super.minimumSize();
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }

    @Override
    public void printComponents(Graphics g) {
        super.printComponents(g);
    }

    @Override
    public synchronized void addContainerListener(ContainerListener l) {
        super.addContainerListener(l);
    }

    @Override
    public synchronized void removeContainerListener(ContainerListener l) {
        super.removeContainerListener(l);
    }

    @Override
    public synchronized ContainerListener[] getContainerListeners() {
        return super.getContainerListeners();
    }

    @Override
    protected void processEvent(AWTEvent e) {
        super.processEvent(e);
    }

    @Override
    protected void processContainerEvent(ContainerEvent e) {
        super.processContainerEvent(e);
    }

    @Override
    public void deliverEvent(Event e) {
        super.deliverEvent(e);
    }

    @Override
    public Component getComponentAt(int x, int y) {
        return super.getComponentAt(x, y);
    }

    @Override
    public Component locate(int x, int y) {
        return super.locate(x, y);
    }

    @Override
    public Component getComponentAt(Point p) {
        return super.getComponentAt(p);
    }

    @Override
    public Point getMousePosition(boolean allowChildren) throws HeadlessException {
        return super.getMousePosition(allowChildren);
    }

    @Override
    public Component findComponentAt(int x, int y) {
        return super.findComponentAt(x, y);
    }

    @Override
    public Component findComponentAt(Point p) {
        return super.findComponentAt(p);
    }

    @Override
    public boolean isAncestorOf(Component c) {
        return super.isAncestorOf(c);
    }

    @Override
    public void list(PrintStream out, int indent) {
        super.list(out, indent);
    }

    @Override
    public void list(PrintWriter out, int indent) {
        super.list(out, indent);
    }

    @Override
    public Set<AWTKeyStroke> getFocusTraversalKeys(int id) {
        return super.getFocusTraversalKeys(id);
    }

    @Override
    public boolean areFocusTraversalKeysSet(int id) {
        return super.areFocusTraversalKeysSet(id);
    }

    @Override
    public boolean isFocusCycleRoot(Container container) {
        return super.isFocusCycleRoot(container);
    }

    @Override
    public void setFocusTraversalPolicy(FocusTraversalPolicy policy) {
        super.setFocusTraversalPolicy(policy);
    }

    @Override
    public FocusTraversalPolicy getFocusTraversalPolicy() {
        return super.getFocusTraversalPolicy();
    }

    @Override
    public boolean isFocusTraversalPolicySet() {
        return super.isFocusTraversalPolicySet();
    }

    @Override
    public void setFocusCycleRoot(boolean focusCycleRoot) {
        super.setFocusCycleRoot(focusCycleRoot);
    }

    @Override
    public boolean isFocusCycleRoot() {
        return super.isFocusCycleRoot();
    }

    @Override
    public void transferFocusDownCycle() {
        super.transferFocusDownCycle();
    }

    @Override
    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        super.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public Container getParent() {
        return super.getParent();
    }

    @Override
    public synchronized void setDropTarget(DropTarget dt) {
        super.setDropTarget(dt);
    }

    @Override
    public synchronized DropTarget getDropTarget() {
        return super.getDropTarget();
    }

    @Override
    public GraphicsConfiguration getGraphicsConfiguration() {
        return super.getGraphicsConfiguration();
    }

    @Override
    public Toolkit getToolkit() {
        return super.getToolkit();
    }

    @Override
    public boolean isValid() {
        return super.isValid();
    }

    @Override
    public boolean isDisplayable() {
        return super.isDisplayable();
    }

    @Override
    public boolean isVisible() {
        return super.isVisible();
    }

    @Override
    public Point getMousePosition() throws HeadlessException {
        return super.getMousePosition();
    }

    @Override
    public boolean isShowing() {
        return super.isShowing();
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public void enable(boolean b) {
        super.enable(b);
    }

    @Override
    public void enableInputMethods(boolean enable) {
        super.enableInputMethods(enable);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void show(boolean b) {
        super.show(b);
    }

    @Override
    public Color getForeground() {
        return super.getForeground();
    }

    @Override
    public boolean isForegroundSet() {
        return super.isForegroundSet();
    }

    @Override
    public Color getBackground() {
        return super.getBackground();
    }

    @Override
    public boolean isBackgroundSet() {
        return super.isBackgroundSet();
    }

    @Override
    public Font getFont() {
        return super.getFont();
    }

    @Override
    public boolean isFontSet() {
        return super.isFontSet();
    }

    @Override
    public Locale getLocale() {
        return super.getLocale();
    }

    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
    }

    @Override
    public ColorModel getColorModel() {
        return super.getColorModel();
    }

    @Override
    public Point getLocation() {
        return super.getLocation();
    }

    @Override
    public Point getLocationOnScreen() {
        return super.getLocationOnScreen();
    }

    @Override
    public Point location() {
        return super.location();
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
    }

    @Override
    public void move(int x, int y) {
        super.move(x, y);
    }

    @Override
    public void setLocation(Point p) {
        super.setLocation(p);
    }

    @Override
    public Dimension getSize() {
        return super.getSize();
    }

    @Override
    public Dimension size() {
        return super.size();
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
    }

    @Override
    public void resize(Dimension d) {
        super.resize(d);
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    @Override
    public Rectangle bounds() {
        return super.bounds();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
    }

    @Override
    public void setBounds(Rectangle r) {
        super.setBounds(r);
    }

    @Override
    public boolean isLightweight() {
        return super.isLightweight();
    }

    @Override
    public boolean isPreferredSizeSet() {
        return super.isPreferredSizeSet();
    }

    @Override
    public boolean isMinimumSizeSet() {
        return super.isMinimumSizeSet();
    }

    @Override
    public boolean isMaximumSizeSet() {
        return super.isMaximumSizeSet();
    }

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public boolean isCursorSet() {
        return super.isCursorSet();
    }

    @Override
    public void paintAll(Graphics g) {
        super.paintAll(g);
    }

    @Override
    public void repaint() {
        super.repaint();
    }

    @Override
    public void repaint(long tm) {
        super.repaint(tm);
    }

    @Override
    public void repaint(int x, int y, int width, int height) {
        super.repaint(x, y, width, height);
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
        return super.imageUpdate(img, infoflags, x, y, w, h);
    }

    @Override
    public Image createImage(ImageProducer producer) {
        return super.createImage(producer);
    }

    @Override
    public Image createImage(int width, int height) {
        return super.createImage(width, height);
    }

    @Override
    public VolatileImage createVolatileImage(int width, int height) {
        return super.createVolatileImage(width, height);
    }

    @Override
    public VolatileImage createVolatileImage(int width, int height, ImageCapabilities caps) throws AWTException {
        return super.createVolatileImage(width, height, caps);
    }

    @Override
    public boolean prepareImage(Image image, ImageObserver observer) {
        return super.prepareImage(image, observer);
    }

    @Override
    public boolean prepareImage(Image image, int width, int height, ImageObserver observer) {
        return super.prepareImage(image, width, height, observer);
    }

    @Override
    public int checkImage(Image image, ImageObserver observer) {
        return super.checkImage(image, observer);
    }

    @Override
    public int checkImage(Image image, int width, int height, ImageObserver observer) {
        return super.checkImage(image, width, height, observer);
    }

    @Override
    public void setIgnoreRepaint(boolean ignoreRepaint) {
        super.setIgnoreRepaint(ignoreRepaint);
    }

    @Override
    public boolean getIgnoreRepaint() {
        return super.getIgnoreRepaint();
    }

    @Override
    public boolean inside(int x, int y) {
        return super.inside(x, y);
    }

    @Override
    public boolean contains(Point p) {
        return super.contains(p);
    }

    @Override
    public boolean postEvent(Event e) {
        return super.postEvent(e);
    }

    @Override
    public synchronized void addComponentListener(ComponentListener l) {
        super.addComponentListener(l);
    }

    @Override
    public synchronized void removeComponentListener(ComponentListener l) {
        super.removeComponentListener(l);
    }

    @Override
    public synchronized ComponentListener[] getComponentListeners() {
        return super.getComponentListeners();
    }

    @Override
    public synchronized void addFocusListener(FocusListener l) {
        super.addFocusListener(l);
    }

    @Override
    public synchronized void removeFocusListener(FocusListener l) {
        super.removeFocusListener(l);
    }

    @Override
    public synchronized FocusListener[] getFocusListeners() {
        return super.getFocusListeners();
    }

    @Override
    public void addHierarchyListener(HierarchyListener l) {
        super.addHierarchyListener(l);
    }

    @Override
    public void removeHierarchyListener(HierarchyListener l) {
        super.removeHierarchyListener(l);
    }

    @Override
    public synchronized HierarchyListener[] getHierarchyListeners() {
        return super.getHierarchyListeners();
    }

    @Override
    public void addHierarchyBoundsListener(HierarchyBoundsListener l) {
        super.addHierarchyBoundsListener(l);
    }

    @Override
    public void removeHierarchyBoundsListener(HierarchyBoundsListener l) {
        super.removeHierarchyBoundsListener(l);
    }

    @Override
    public synchronized HierarchyBoundsListener[] getHierarchyBoundsListeners() {
        return super.getHierarchyBoundsListeners();
    }

    @Override
    public synchronized void addKeyListener(KeyListener l) {
        super.addKeyListener(l);
    }

    @Override
    public synchronized void removeKeyListener(KeyListener l) {
        super.removeKeyListener(l);
    }

    @Override
    public synchronized KeyListener[] getKeyListeners() {
        return super.getKeyListeners();
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
    }

    @Override
    public synchronized void removeMouseListener(MouseListener l) {
        super.removeMouseListener(l);
    }

    @Override
    public synchronized MouseListener[] getMouseListeners() {
        return super.getMouseListeners();
    }

    @Override
    public synchronized void addMouseMotionListener(MouseMotionListener l) {
        super.addMouseMotionListener(l);
    }

    @Override
    public synchronized void removeMouseMotionListener(MouseMotionListener l) {
        super.removeMouseMotionListener(l);
    }

    @Override
    public synchronized MouseMotionListener[] getMouseMotionListeners() {
        return super.getMouseMotionListeners();
    }

    @Override
    public synchronized void addMouseWheelListener(MouseWheelListener l) {
        super.addMouseWheelListener(l);
    }

    @Override
    public synchronized void removeMouseWheelListener(MouseWheelListener l) {
        super.removeMouseWheelListener(l);
    }

    @Override
    public synchronized MouseWheelListener[] getMouseWheelListeners() {
        return super.getMouseWheelListeners();
    }

    @Override
    public synchronized void addInputMethodListener(InputMethodListener l) {
        super.addInputMethodListener(l);
    }

    @Override
    public synchronized void removeInputMethodListener(InputMethodListener l) {
        super.removeInputMethodListener(l);
    }

    @Override
    public synchronized InputMethodListener[] getInputMethodListeners() {
        return super.getInputMethodListeners();
    }

    @Override
    public InputMethodRequests getInputMethodRequests() {
        return super.getInputMethodRequests();
    }

    @Override
    public InputContext getInputContext() {
        return super.getInputContext();
    }

    @Override
    protected AWTEvent coalesceEvents(AWTEvent existingEvent, AWTEvent newEvent) {
        return super.coalesceEvents(existingEvent, newEvent);
    }

    @Override
    protected void processComponentEvent(ComponentEvent e) {
        super.processComponentEvent(e);
    }

    @Override
    protected void processFocusEvent(FocusEvent e) {
        super.processFocusEvent(e);
    }

    @Override
    protected void processMouseWheelEvent(MouseWheelEvent e) {
        super.processMouseWheelEvent(e);
    }

    @Override
    protected void processInputMethodEvent(InputMethodEvent e) {
        super.processInputMethodEvent(e);
    }

    @Override
    protected void processHierarchyEvent(HierarchyEvent e) {
        super.processHierarchyEvent(e);
    }

    @Override
    protected void processHierarchyBoundsEvent(HierarchyEvent e) {
        super.processHierarchyBoundsEvent(e);
    }

    @Override
    public boolean handleEvent(Event evt) {
        return super.handleEvent(evt);
    }

    @Override
    public boolean mouseDown(Event evt, int x, int y) {
        return super.mouseDown(evt, x, y);
    }

    @Override
    public boolean mouseDrag(Event evt, int x, int y) {
        return super.mouseDrag(evt, x, y);
    }

    @Override
    public boolean mouseUp(Event evt, int x, int y) {
        return super.mouseUp(evt, x, y);
    }

    @Override
    public boolean mouseMove(Event evt, int x, int y) {
        return super.mouseMove(evt, x, y);
    }

    @Override
    public boolean mouseEnter(Event evt, int x, int y) {
        return super.mouseEnter(evt, x, y);
    }

    @Override
    public boolean mouseExit(Event evt, int x, int y) {
        return super.mouseExit(evt, x, y);
    }

    @Override
    public boolean keyDown(Event evt, int key) {
        return super.keyDown(evt, key);
    }

    @Override
    public boolean keyUp(Event evt, int key) {
        return super.keyUp(evt, key);
    }

    @Override
    public boolean action(Event evt, Object what) {
        return super.action(evt, what);
    }

    @Override
    public boolean gotFocus(Event evt, Object what) {
        return super.gotFocus(evt, what);
    }

    @Override
    public boolean lostFocus(Event evt, Object what) {
        return super.lostFocus(evt, what);
    }

    @Override
    public boolean isFocusTraversable() {
        return super.isFocusTraversable();
    }

    @Override
    public boolean isFocusable() {
        return super.isFocusable();
    }

    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
    }

    @Override
    public void setFocusTraversalKeysEnabled(boolean focusTraversalKeysEnabled) {
        super.setFocusTraversalKeysEnabled(focusTraversalKeysEnabled);
    }

    @Override
    public boolean getFocusTraversalKeysEnabled() {
        return super.getFocusTraversalKeysEnabled();
    }

    @Override
    public void requestFocus(FocusEvent.Cause cause) {
        super.requestFocus(cause);
    }

    @Override
    protected boolean requestFocus(boolean temporary, FocusEvent.Cause cause) {
        return super.requestFocus(temporary, cause);
    }

    @Override
    public boolean requestFocusInWindow(FocusEvent.Cause cause) {
        return super.requestFocusInWindow(cause);
    }

    @Override
    public Container getFocusCycleRootAncestor() {
        return super.getFocusCycleRootAncestor();
    }

    @Override
    public void transferFocus() {
        super.transferFocus();
    }

    @Override
    public void nextFocus() {
        super.nextFocus();
    }

    @Override
    public void transferFocusBackward() {
        super.transferFocusBackward();
    }

    @Override
    public void transferFocusUpCycle() {
        super.transferFocusUpCycle();
    }

    @Override
    public boolean hasFocus() {
        return super.hasFocus();
    }

    @Override
    public boolean isFocusOwner() {
        return super.isFocusOwner();
    }

    @Override
    public void add(PopupMenu popup) {
        super.add(popup);
    }

    @Override
    public void remove(MenuComponent popup) {
        super.remove(popup);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void list() {
        super.list();
    }

    @Override
    public void list(PrintStream out) {
        super.list(out);
    }

    @Override
    public void list(PrintWriter out) {
        super.list(out);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        super.removePropertyChangeListener(listener);
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return super.getPropertyChangeListeners();
    }

    @Override
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        super.removePropertyChangeListener(propertyName, listener);
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return super.getPropertyChangeListeners(propertyName);
    }

    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    public void firePropertyChange(String propertyName, short oldValue, short newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    public void firePropertyChange(String propertyName, long oldValue, long newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    public void firePropertyChange(String propertyName, float oldValue, float newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    public void firePropertyChange(String propertyName, double oldValue, double newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    public void setComponentOrientation(ComponentOrientation o) {
        super.setComponentOrientation(o);
    }

    @Override
    public ComponentOrientation getComponentOrientation() {
        return super.getComponentOrientation();
    }

    @Override
    public void setMixingCutoutShape(Shape shape) {
        super.setMixingCutoutShape(shape);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Date getDateSQL_Single() {
        LocalDate date = getSelectedDate();
        if (date != null) {
            return java.sql.Date.valueOf(date);
        }
        return null;
    }

    public Date[] getDateSQL_Between() {
        LocalDate[] dates = getSelectedDateRange();
        if (dates != null) {
            return new Date[]{java.sql.Date.valueOf(dates[0]), java.sql.Date.valueOf(dates[1])};
        }
        return null;
    }


}
