package com.yoav.currencyExchange;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

/**
 * The AddCoinFrame class is a frame which takes part of the View. it represents the add-coin frame.
 * The AddCoinFrame opened when the user wants to add a new coin to the application.
 * @author Yoav Saroya
 */

public class AddCoinFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JTextField txtName;
	private JTextField txtUnit;
	private JTextField txtCountry;
	private JTextField txtCode;
	private JTextField txtRate;
	private JTextField txtChange;
	
	private ArrayList<Currency> currencies;
	private CalculatorFrame calcFrame;
	private ArrayList<String> codeList;
	private MainView mainView;
	
	public AddCoinFrame(CalculatorFrame cf, ArrayList<String> cl,ArrayList<Currency> currs ,MainView mv) {
		super();
		currencies = currs;
		calcFrame = cf;
		codeList = cl;
		mainView = mv;
		initializeAddCoin();
	}
	
	/**
	 * Initialize the contents of the addCoin frame.
	 */
	private void initializeAddCoin() {
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("data/add-icon.png"));
		this.setTitle("Create a new Coin");
		this.setResizable(false);
		this.setBounds(100, 100, 499, 194);
		this.getContentPane().setLayout(null);
		this.setLocation((int)(mainView.getLocationOnScreen().getX())+650,(int)mainView.getLocationOnScreen().getY()+224);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				resetAddCoinFrame();
				setVisible(false);
				LogWriter.getInstance().logger.info("Closing the add-coin frame.");
			}
		});
		txtName = new JTextField();
		txtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (txtName.getText().length() > 14) e.consume();
			}
		});
		txtName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (txtName.getText().equals("")) txtName.setText("Name");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if (txtName.getText().equals("Name")) txtName.setText("");
			}
		});
		txtName.setText("Name");
		txtName.setBounds(53, 13, 116, 22);
		this.getContentPane().add(txtName);
		txtName.setColumns(10);

		txtUnit = new JTextField();
		txtUnit.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtUnit.getText().equals("")) txtUnit.setText("Unit");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if (txtUnit.getText().equals("Unit")) txtUnit.setText("");
			}
		});
		txtUnit.setText("Unit");
		txtUnit.setBounds(203, 13, 116, 22);
		this.getContentPane().add(txtUnit);
		txtUnit.setColumns(10);

		txtCountry = new JTextField();
		txtCountry.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtCountry.getText().equals("")) txtCountry.setText("Country");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if (txtCountry.getText().equals("Country")) txtCountry.setText("");
			}
		});
		txtCountry.setText("Country");
		txtCountry.setBounds(356, 13, 116, 22);
		this.getContentPane().add(txtCountry);
		txtCountry.setColumns(10);

		txtCode = new JTextField();
		txtCode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (txtCode.getText().length() > 2)
					e.consume();
			}
		});
		txtCode.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e)
			{
				if (txtCode.getText().equals("")) txtCode.setText("Code");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if (txtCode.getText().equals("Code")) txtCode.setText("");
			}
		});
		txtCode.setText("Code");
		txtCode.setBounds(53, 48, 116, 22);
		this.getContentPane().add(txtCode);
		txtCode.setColumns(10);

		txtRate = new JTextField();
		txtRate.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtRate.getText().equals("")) txtRate.setText("Rate");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if (txtRate.getText().equals("Rate")) txtRate.setText("");
			}
		});
		txtRate.setText("Rate");
		txtRate.setBounds(203, 48, 116, 22);
		this.getContentPane().add(txtRate);
		txtRate.setColumns(10);

		txtChange = new JTextField();
		txtChange.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtChange.getText().equals("")) txtChange.setText("Change");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if (txtChange.getText().equals("Change")) txtChange.setText("");
			}
		});
		txtChange.setText("Change");
		txtChange.setBounds(356, 48, 116, 22);
		this.getContentPane().add(txtChange);
		txtChange.setColumns(10);

		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetAddCoinFrame();
				LogWriter.getInstance().logger.trace("Restarting the add-coin frame.");
			}
		});
		btnReset.setIcon(new ImageIcon("data/reset.png"));
		btnReset.setForeground(Color.DARK_GRAY);
		btnReset.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnReset.setBackground(SystemColor.activeCaption);
		btnReset.setBounds(0, 83, 255, 76);
		this.getContentPane().add(btnReset);

		JButton btnCreate = new JButton("Create!");
		/* an events that occurs according to the create button.
		 * this method has a lot of validates, which is needed to create a new currency object.
		 * in the end, if all went well, a new currency object is created and added to the currency list.
		 */
		btnCreate.addActionListener((ActionEvent arg0)-> {
			//initializing the variables.
			Currency curr = null;
			double change = 0;
			double rate = 0;
			int unit = 0;
			String name = txtName.getText();
			String country = txtCountry.getText();
			String code = txtCode.getText();
			Pattern pattern = Pattern.compile("[A-Z]{3}"); //a regex for code (must be a 3 latters word)
			Matcher matcher = pattern.matcher(code);
			if (!matcher.find()) {
				JOptionPane.showMessageDialog(null, "code must be a word with 3 Uppercase!", "Input Error!", 0);
				LogWriter.getInstance().logger.trace("An input error in creating a coin - code: "+code);
				return;
			}
			//validates that the currency code is not exists already.
			for (Currency c: currencies) {
				if (c.getCode().equals(code)) {
					JOptionPane.showMessageDialog(null, "code already exists!", "Input Error!", 0);
					LogWriter.getInstance().logger.trace("An input error in creating a coin - code: "+code);
					return;
				}
			}
			//validates that change the change field is a number.
			try {change = Double.parseDouble(txtChange.getText());}
			catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "change must be a number!", "Input Error!", 0);
				LogWriter.getInstance().logger.trace("An input error in creating a coin - change: "+txtChange.getText());
				return;
			}
			//validates that the rate field is a positive number.
			try {
				rate = Double.parseDouble(txtRate.getText());
				if (rate <= 0) throw new NumberFormatException();
			}
			catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "rate must be a positive number!", "Input Error!", 0);
				LogWriter.getInstance().logger.trace("An input error in creating a coin - rate: "+txtRate.getText());
				return;
			}
			//validates that the unit field is a positive number.
			try {
				unit = Integer.parseUnsignedInt(txtUnit.getText());
				if (unit == 0) throw new NumberFormatException();
			}
			catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "unit must be a positive number!", "Input Error!", 0);
				LogWriter.getInstance().logger.trace("An input error in creating a coin - unit: "+txtUnit.getText());
				return;
			}
			//creating the new currency with the field the user inputed.
			curr = new Currency(name, unit, country, code, rate, change);
			//adds the currency where nessascry.
			currencies.add(curr);
			codeList.add(curr.getCode());
			if (calcFrame.getConvertFrom() != null) calcFrame.getConvertFrom().addItem(curr.getCode());
			if (calcFrame.getConvertTo() != null) calcFrame.getConvertTo().addItem(curr.getCode());
			LogWriter.getInstance().logger.trace("A new coin has been created: "+curr.getCode());
			mainView.updateDataTable(true);
			//close the add-coin frame.
			resetAddCoinFrame();
			this.setVisible(false);
		});
		btnCreate.setIcon(new ImageIcon("data/add-icon.png"));
		btnCreate.setForeground(Color.DARK_GRAY);
		btnCreate.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnCreate.setBackground(SystemColor.activeCaption);
		btnCreate.setBounds(253, 83, 240, 76);
		this.getContentPane().add(btnCreate);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 83, 493, 2);
		this.getContentPane().add(separator);
	}
	
	//reset the add-coin frame back to default properties.
	private void resetAddCoinFrame() {
		txtName.setText("Name");
		txtUnit.setText("Unit");
		txtCountry.setText("Country");
		txtCode.setText("Code");
		txtRate.setText("Rate");
		txtChange.setText("Change");
	}

}
