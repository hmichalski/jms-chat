/**
 *
 *  @author Michalski Hubert S28546
 *
 */

import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.awt.event.ActionListener;
import javax.jms.*;

public class UserFrame extends JFrame implements MessageListener {
    private JTextPane chatPane;
    private JTextField chatField;
    private JButton sendButton;
    
    private String nickname;
    private ChatUser chatUser;
    
    public UserFrame(String nickname, UserFrameListener userFrameListener) {
        this.nickname = nickname;        
        chatUser = new ChatUser(nickname, this);
                
        setTitle("Chat - " + nickname);
        setSize(480, 360);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        chatPane = new JTextPane();
        chatPane.setEditable(false);
        add(new JScrollPane(chatPane), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        
        chatField = new JTextField();
        sendButton = new JButton("Send");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = chatField.getText();
                if (chatUser != null && !message.trim().isEmpty()) {
                    new Thread(() -> {
                        try {
                            chatUser.sendMessage(message);
                        } catch (JMSException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                }
                chatField.setText("");
            }
        });
        
        inputPanel.add(chatField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        add(inputPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                chatUser.close();
                userFrameListener.userFrameClosed(nickname);
            }
        });
        
        setVisible(true);
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String text = ((TextMessage) message).getText();
                SwingUtilities.invokeLater(() -> 
                	updateChatPane(text, text.contains(nickname + ":"))
                );
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateChatPane(String message, boolean isSender) {
    	StyledDocument doc = chatPane.getStyledDocument();
        SimpleAttributeSet left = new SimpleAttributeSet();
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(left, new Color(244, 123, 32));

        SimpleAttributeSet right = new SimpleAttributeSet();
        StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setForeground(right, new Color(93, 138, 168));
        
        try {
            int length = doc.getLength();
            doc.insertString(length, message + "\n", null);
            doc.setParagraphAttributes(length, message.length(), isSender ? right : left, false);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
