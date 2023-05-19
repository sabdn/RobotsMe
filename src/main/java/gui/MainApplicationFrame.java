package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import log.Localization;
import log.Logger;


/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();

    private Localization lok = new Localization();
    private final ControlOfRobot controller;

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width  - inset*2,
                screenSize.height - inset*2);

        setContentPane(desktopPane);


        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        this.controller = new ControlOfRobot();

        gui.GameWindow gameWindow = new gui.GameWindow(controller);
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);


        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }


    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem(
                    "Системная схема",
                    KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem(
                    "Универсальная схема",
                    KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");
        {
            JMenuItem addLogMessageItem = new JMenuItem(
                    "Сообщение в лог",
                    KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }


        JMenu exitMenu = new JMenu("Выход");
        testMenu.setMnemonic(KeyEvent.VK_E);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem addButtonExit = new JMenuItem("Выйти", KeyEvent.VK_S);
            addButtonExit.addActionListener((event) -> {
                exitApp();
            });
            exitMenu.add(addButtonExit);
        }


        JMenu settingsMenu = new JMenu(lok.getStringResource("settings"));
        settingsMenu.setMnemonic(KeyEvent.VK_V);
        settingsMenu.getAccessibleContext().setAccessibleDescription(
                "Настройки");
        lok.addElement(settingsMenu, "settings");

        JMenu changeLanguageMenu = new JMenu(lok.getStringResource("changeLanguage"));
        settingsMenu.add(changeLanguageMenu);
        lok.addElement(settingsMenu, "changeLanguage");

        {
            JMenuItem enLanguageButton = new JMenuItem(
                    lok.getStringResource("english"),
                    KeyEvent.VK_X);
            enLanguageButton.addActionListener((event) -> {
                lok.changeLanguage("en");
            });
            changeLanguageMenu.add(enLanguageButton);
            lok.addElement(enLanguageButton, "english");

            JMenuItem ruLanguageButton = new JMenuItem(
                    lok.getStringResource("russian"),
                    KeyEvent.VK_L);
            ruLanguageButton.addActionListener((event) -> {
                lok.changeLanguage("ru");
            });
            changeLanguageMenu.add(ruLanguageButton);
            lok.addElement(ruLanguageButton, "russian");

        }
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(settingsMenu);
        menuBar.add(exitMenu);
        return menuBar;
    }

    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }

    private void exitApp() {
        String[] options = {"Да", "Нет"};
        int response = JOptionPane.showOptionDialog(this,
                "Закрыть приложение?",
                "Закрытие",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

}