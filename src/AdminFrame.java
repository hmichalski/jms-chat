/**
 *
 *  @author Michalski Hubert S28546
 *
 */


import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminFrame extends JFrame implements UserFrameListener {
    private JTextArea logArea;
    private JButton logInButton;

    public AdminFrame() {
        setTitle("Chat Admin Panel");
        setSize(640, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.CENTER);
        
        logInButton = new JButton("Log in");
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openNewUserFrame();
            }
        });
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(logInButton, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private void openNewUserFrame() {
    	String nickname = JOptionPane.showInputDialog(
                AdminFrame.this,
                "Enter your nickname: ",
                "Log in",
                JOptionPane.PLAIN_MESSAGE
         );
    	
        if (nickname != null && !nickname.trim().isEmpty()) {
            new UserFrame(nickname, AdminFrame.this);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM HH:mm:ss"));
            logArea.append("[" + timestamp + "] " + "User '" + nickname + "' logged in.\n");
        } else {
            logArea.append("Invalid nickname.\n");
        }
    }
    
    @Override
    public void userFrameClosed(String nickname) {
    	String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM HH:mm:ss"));
    	logArea.append("[" + timestamp + "] " + "User '" + nickname + "' disconnected.\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminFrame();
            }
        });
    }
}
