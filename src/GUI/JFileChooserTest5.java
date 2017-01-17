package GUI;

import javax.swing.*;
import java.io.File;
import java.awt.BorderLayout;
import java.awt.event.*;

public class JFileChooserTest5 extends JFrame implements ActionListener{

  JLabel label;

  public static void main(String[] args){
    JFileChooserTest5 frame = new JFileChooserTest5();

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setBounds(10, 10, 300, 200);
    frame.setTitle("DIMA");
    frame.setVisible(true);
  }

  JFileChooserTest5(){


    label = new JLabel();
    JPanel labelPanel = new JPanel();
    labelPanel.add(label);
    getContentPane().add(labelPanel, BorderLayout.CENTER);
    
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(null);
    JButton button = new JButton("file select");
    button.setBounds(200,10,80,20);
    button.addActionListener(this);
    buttonPanel.add(button);
    getContentPane().add(buttonPanel, BorderLayout.CENTER);
    
    JPanel buttonPanelLower = new JPanel();
    buttonPanelLower.setLayout(null);
    JButton buttonLower = new JButton("file select");
    buttonLower.setBounds(200,50,80,20);
    buttonLower.addActionListener(this);
    buttonPanel.add(buttonLower);
//    getContentPane().add(buttonPanelLower, BorderLayout.CENTER);

  }

  public void actionPerformed(ActionEvent e){
    JFileChooser filechooser = new JFileChooser();

    int selected = filechooser.showOpenDialog(this);
    if (selected == JFileChooser.APPROVE_OPTION){
      File file = filechooser.getSelectedFile();
      label.setText(file.getName());
    }else if (selected == JFileChooser.CANCEL_OPTION){
      label.setText("キャンセルされました");
    }else if (selected == JFileChooser.ERROR_OPTION){
      label.setText("エラー又は取消しがありました");
    }
  }
}