package dinder;

import java.awt.*;
import javax.swing.*;
class User_Authentication {

    public User_Authentication() {
        super();
    }

    public static void main(String[] args) {
        User_Authentication b = new User_Authentication();
        b.createCenterPanel();
    }

    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("testing");
        label.setPreferredSize(new Dimension(100, 100));
        dialogPanel.add(label, BorderLayout.CENTER);

        return dialogPanel;
    }

}