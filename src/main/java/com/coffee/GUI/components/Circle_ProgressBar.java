package com.coffee.GUI.components;

import com.coffee.GUI.components.swing.LiquidProgress;
import com.coffee.main.Cafe_Application;

import javax.swing.*;
import java.awt.*;

import static java.lang.Thread.sleep;

public class Circle_ProgressBar extends JDialog {
    private LiquidProgress liquidProgress;

    public Circle_ProgressBar() {
        super((Frame) null, "", true);
        setSize(new Dimension(200, 200));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(Cafe_Application.homeGUI);
        setUndecorated(true);
        initComponents();
    }

    private void initComponents() {
        liquidProgress = new LiquidProgress();
        liquidProgress.setBackground(new Color(153, 219, 255));
        liquidProgress.setForeground(new Color(42, 161, 233));
        liquidProgress.setValue(0);
        liquidProgress.setAnimateColor(new Color(255, 255, 255));
        liquidProgress.setBorderColor(new Color(42, 161, 233));
        liquidProgress.setBorderSize(8);
        liquidProgress.setSpaceSize(10);
        add(liquidProgress);
    }

    public void progress() {
        try {
            Thread threadProgress = new Thread(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    while (i <= 100) {
                        i++;
                        liquidProgress.setValue(i);
                        try {
                            sleep(10);
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    dispose();
                }
            });
            threadProgress.start();
        } catch (Exception ignored) {

        }

    }
}
