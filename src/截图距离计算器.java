import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class 截图距离计算器 extends JFrame {
    private JTextField distanceTextField;
    private JButton screenshotButton;

    private Point clickPoint1;
    private Point clickPoint2;

    public 截图距离计算器() {
        setTitle("截图距离计算器");
        setSize(400, 100);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建一个面板，用于放置提示文字、输入框和按钮
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // 创建一个标签用于显示提示文字
        JLabel labelHint = new JLabel("距离：");
        labelHint.setFont(new Font("宋体", Font.PLAIN, 20));

        // 创建一个输入框，设置初始值，列数，字体
        distanceTextField = new JTextField();
        distanceTextField.setColumns(10);
        distanceTextField.setFont(new Font("宋体", Font.PLAIN, 20));

        // 创建一个按钮，设置文本，字体，添加动作监听器
        screenshotButton = new JButton("截图");
        screenshotButton.setFont(new Font("宋体", Font.PLAIN, 20));
        screenshotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 按钮被点击时执行的方法
                captureScreenshot();
            }
        });

        // 把标签、输入框和按钮添加到面板中
        panel.add(labelHint);
        panel.add(distanceTextField);
        panel.add(screenshotButton);

        // 把面板添加到窗体的内容面板中
        getContentPane().add(panel, BorderLayout.NORTH);

        // 设置窗体可见
        setVisible(true);
    }

    private void captureScreenshot() {
        try {
            Robot robot = new Robot();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screenRect = new Rectangle(screenSize);

            // 截取整个屏幕
            BufferedImage screenshot = robot.createScreenCapture(screenRect);

            // 显示截图
            ImageIcon icon = new ImageIcon(screenshot);
            JLabel label = new JLabel(icon);

            // 添加鼠标监听器，记录点击的位置
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (clickPoint1 == null) {
                        clickPoint1 = e.getPoint();
                        // 在点击位置绘制标记
                        Graphics g = screenshot.getGraphics();
                        g.setColor(Color.RED);
                        g.fillRect(clickPoint1.x - 5, clickPoint1.y - 5, 10, 10);
                        g.dispose();
                        label.repaint();
                    } else {
                        clickPoint2 = e.getPoint();
                        // 在点击位置绘制标记
                        Graphics g = screenshot.getGraphics();
                        g.setColor(Color.BLUE);
                        g.fillRect(clickPoint2.x - 5, clickPoint2.y - 5, 10, 10);
                        g.dispose();
                        label.repaint();

                        // 计算像素点距离
                        int distance = (int) clickPoint1.distance(clickPoint2);
                        if (distanceTextField.getText() == null || "".equals(distanceTextField.getText())) {
                            distanceTextField.setText(String.valueOf(distance));
                            clickPoint1 = null;
                            clickPoint2 = null;
                            // 移除鼠标监听器
                            label.removeMouseListener(this);

                            // 关闭截图窗口
                            ((Window) SwingUtilities.getRoot(label)).dispose();
                            return;
                        }
                        double inputValue = Double.parseDouble(distanceTextField.getText());
                        double result = (distance / inputValue) * 100;

                        // 显示结果提示
                        JOptionPane.showMessageDialog(null, "距离：" + distance + "像素\n结果：" + result);

                        // 清空点击点
                        clickPoint1 = null;
                        clickPoint2 = null;

                        // 移除鼠标监听器
                        label.removeMouseListener(this);

                        // 关闭截图窗口
                        ((Window) SwingUtilities.getRoot(label)).dispose();
                    }
                }
            });

            // 显示带有截图的窗口
            JOptionPane.showMessageDialog(this, label, "屏幕截图", JOptionPane.PLAIN_MESSAGE);

        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new 截图距离计算器();
            }
        });
    }
}
