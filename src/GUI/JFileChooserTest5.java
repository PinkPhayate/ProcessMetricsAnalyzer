package GUI;

import javax.swing.*;
import java.io.File;
import java.awt.BorderLayout;
import java.awt.event.*;

public class JFileChooserTest5 extends JFrame implements ActionListener{

	JLabel label;
	JPanel buttonPanelLower  = null;
	JButton buttonUpper = null;
	JButton buttonLower = null;
	JTextField upperTtext  = null;
	JTextField lowerText  = null;

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
		this.buttonUpper = new JButton("directory1");
		this.buttonUpper.setBounds(200,10,80,20);
		this.buttonUpper.addActionListener(this);
		buttonPanel.add(this.buttonUpper);
		getContentPane().add(buttonPanel, BorderLayout.CENTER);

		JPanel p = new JPanel();

		this.upperTtext = new JTextField("");
		this.upperTtext.setBounds(0, 10, 200, 20);
		buttonPanel.add(this.upperTtext);    
		this.upperTtext.setLayout(null);


		buttonPanelLower = new JPanel();
		buttonPanelLower.setLayout(null);
		buttonLower = new JButton("directory2");
		buttonLower.setBounds(200,50,80,20);
		buttonLower.addActionListener(this);
		buttonPanel.add(buttonLower);
		
		this.lowerText = new JTextField("");
		this.lowerText.setBounds(0, 50, 200, 20);
		buttonPanel.add(this.lowerText);    
		this.lowerText.setLayout(null);

	}

	public void actionPerformed(ActionEvent e){
		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int selected = filechooser.showOpenDialog(this);
		if (selected == JFileChooser.APPROVE_OPTION){
			File file = filechooser.getSelectedFile();
			if(e.getSource() == this.buttonUpper){
				upperTtext.setText(file.getName());
			}
			if(e.getSource() == this.buttonLower){
				lowerText.setText(file.getName());
			}
		}

		//    }else if (selected == JFileChooser.CANCEL_OPTION){
		//      label.setText("キャンセルされました");
		//    }else if (selected == JFileChooser.ERROR_OPTION){
		//      label.setText("エラー又は取消しがありました");
		//    }
	}
}