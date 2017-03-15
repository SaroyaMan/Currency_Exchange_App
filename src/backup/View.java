package backup;
import javax.swing.JFrame;



import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import java.awt.Toolkit;

import javax.swing.JTable;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.SystemColor;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The View class is the view part of the MVC architecture. it represents the user-interface and the
 * interaction between the application and the user.
 * The view has a reference to controller (association) so when the model is updated, the view will
 * show the up to date currencies rates.
 * The view part has 3 frames: the main frame - the frame that opened when the application is started.
 * the calculator frame - the frame that opened when the user wants to converts between 2 currencies.
 * the add-coin frame - the frame that opened when the user wants to add a new coin to the application.
 * @author Yoav Saroya
 */

public class View {		//GUI(View) is the View Part of the MVC

	// Main frame Variables
	private JFrame mainFrame;
	private JTable table;
	private ArrayList<Currency> currencies;
	private String[][] data;
	private Controller controller;
	private ArrayList<String> codeList;
	private JLabel labelDate;

	// Calculator frame Variables:
	private JFrame calculatorFrame;
	private JTextField amount;
	private JTextField result;
	private JTextField searchCurrCoin;
	private JTextField searchNewCoin;
	private JComboBox<String> convertFrom;
	private JComboBox<String> convertTo;

	// AddCoin frame Variables:
	private JFrame newCoinFrame;
	private JTextField txtName;
	private JTextField txtUnit;
	private JTextField txtCountry;
	private JTextField txtCode;
	private JTextField txtRate;
	private JTextField txtChange;

	/**
	 * Create the application.
	 */
	public View(Controller controller) {
		this.controller = controller;
		controller.update();
		initialize();
		this.mainFrame.setVisible(true);
	}


	/**
	 * Initialize the contents of the main frame.
	 */
	private void initialize() {
		LogWriter.getInstance().logger.info("Initializing the main frame.");
		mainFrame = new JFrame();
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				LogWriter.getInstance().logger.info("Closing the application.\n\n");
				System.exit(0);
			}
		});
		mainFrame.setResizable(false);
		mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("data/coin-icon.png"));
		mainFrame.setTitle("Currency Converter");
		mainFrame.setBounds(100, 100, 638, 476);
		mainFrame.getContentPane().setLayout(null);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setLocation(dim.width/2-mainFrame.getSize().width/2-300, dim.height/2-mainFrame.getSize().height/2);

		labelDate = new JLabel("");
		labelDate.setFont(new Font("Tahoma", Font.BOLD, 16));
		labelDate.setBounds(430, 62, 190, 16);
		mainFrame.getContentPane().add(labelDate);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 83, 608, 247);
		mainFrame.getContentPane().add(scrollPane);
		String[] columns =  {"Name", "Unit", "Country", "Code", "Rate", "Change"};
		updateDataTable(false);
		table = new JTable((new DefaultTableModel(data, columns))) {
			private static final long serialVersionUID = -94313380417681847L;
			@Override
			public boolean isCellEditable(int data, int columns) {return false;}
		};
		table.setFont(new Font("Tahoma", Font.PLAIN, 16));
		table.getTableHeader().setReorderingAllowed(false);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for(int x=0;x<table.getColumnCount();x++)
			table.getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
		scrollPane.setViewportView(table);

		JButton refreshButton = new JButton("Refresh");
		refreshButton.setIcon(new ImageIcon("data/refresh.png"));
		
		/* activates the update method of the controller - which updates the view and the model.
		 * happens in a seperate thread to avoid stocking the UI.
		 */
		refreshButton.addActionListener((ActionEvent e)-> {
			new Thread(()->controller.update(), "Refresh-Thread").start();
		});
		refreshButton.setForeground(Color.DARK_GRAY);
		refreshButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		refreshButton.setBackground(SystemColor.activeCaption);
		refreshButton.setBounds(54, 343, 154, 43);
		mainFrame.getContentPane().add(refreshButton);

		JLabel lblExchangeRates = new JLabel("Exchange rates:");
		lblExchangeRates.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblExchangeRates.setBounds(127, 58, 181, 22);
		mainFrame.getContentPane().add(lblExchangeRates);

		JLabel label = new JLabel("Made By Yoav Saroya\u00AE");
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		label.setBounds(468, 403, 170, 52);
		mainFrame.getContentPane().add(label);

		JButton calcButton = new JButton("Calculator");
		//an event that occurs by the calculator button. it opens the calculator frame.
		calcButton.addActionListener((ActionEvent e)-> {
				if (calculatorFrame==null) initializeCalculator();
				if(!calculatorFrame.isVisible()) {
					calculatorFrame.setLocation((int)(mainFrame.getLocationOnScreen().getX())+650,(int)mainFrame.getLocationOnScreen().getY());
					calculatorFrame.setVisible(true);
					LogWriter.getInstance().logger.trace("Displaying the calculator frame.");
				}
			});
		calcButton.setIcon(new ImageIcon("data/calculator-icon.png"));
		calcButton.setForeground(Color.DARK_GRAY);
		calcButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		calcButton.setBackground(SystemColor.activeCaption);
		calcButton.setBounds(419, 343, 161, 43);
		mainFrame.getContentPane().add(calcButton);

		JLabel labelUpdate = new JLabel("Last Update:");
		labelUpdate.setFont(new Font("Tahoma", Font.BOLD, 16));
		labelUpdate.setBounds(320, 62, 110, 16);
		mainFrame.getContentPane().add(labelUpdate);

		JButton btnCreateANew = new JButton("Create Coin");
		//an event that occurs by the create coin button. it opens the add-coin frame.
		btnCreateANew.addActionListener((ActionEvent arg0)-> {
				if (newCoinFrame==null) initializeAddCoin();
				if (!newCoinFrame.isVisible()) {
					newCoinFrame.setLocation((int)(mainFrame.getLocationOnScreen().getX())+650,(int)mainFrame.getLocationOnScreen().getY()+224);
					newCoinFrame.setVisible(true);
					LogWriter.getInstance().logger.trace("Displaying the add-coin frame.");
				}
			});
		btnCreateANew.setIcon(new ImageIcon("data/add-icon.png"));
		btnCreateANew.setForeground(Color.DARK_GRAY);
		btnCreateANew.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCreateANew.setBackground(SystemColor.activeCaption);
		btnCreateANew.setBounds(237, 343, 154, 43);
		mainFrame.getContentPane().add(btnCreateANew);
		LogWriter.getInstance().logger.trace("Displaying the main window frame.");
	}

	/**
	 * Initialize the contents of the calculator frame.
	 */
	private void initializeCalculator() {
		LogWriter.getInstance().logger.info("Initializing the calculator frame.");
		calculatorFrame = new JFrame();
		calculatorFrame.setResizable(false);
		calculatorFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("data/calculator-icon.png"));
		calculatorFrame.setTitle("Calculator");
		calculatorFrame.setBounds(100, 100, 686, 212);
		calculatorFrame.getContentPane().setLayout(null);
		calculatorFrame.setLocation((int)(mainFrame.getLocationOnScreen().getX())+650,(int)mainFrame.getLocationOnScreen().getY());

		calculatorFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				resetCalculatorFrame();
				calculatorFrame.setVisible(false);
				LogWriter.getInstance().logger.info("Closing the add-coin frame.");
			}
		});

		JLabel currLabel = new JLabel("Current Coin:");
		currLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		currLabel.setBounds(25, 19, 107, 16);
		calculatorFrame.getContentPane().add(currLabel);
		String[] list = new String[codeList.size()];
		list = codeList.toArray(list);
		convertFrom = new JComboBox<String>();
		convertFrom.setModel(new DefaultComboBoxModel<String>(list));
		convertFrom.setBounds(115, 17, 82, 22);
		convertFrom.setSelectedIndex(0);
		calculatorFrame.getContentPane().add(convertFrom);

		JLabel amountLabel = new JLabel("Amount:");
		amountLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		amountLabel.setBounds(248, 19, 93, 16);
		calculatorFrame.getContentPane().add(amountLabel);

		amount = new JTextField();
		amount.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (amount.getText().length() > 14) e.consume();
			}
		});
		amount.setText("0");
		amount.setColumns(10);
		amount.setBounds(311, 17, 116, 22);
		calculatorFrame.getContentPane().add(amount);

		JLabel conLabel = new JLabel("Convert to:");
		conLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		conLabel.setBounds(452, 19, 82, 16);
		calculatorFrame.getContentPane().add(conLabel);

		convertTo = new JComboBox<String>();
		convertTo.setModel(new DefaultComboBoxModel<String>(list));
		convertTo.setBounds(531, 17, 82, 22);
		convertTo.setSelectedIndex(0);
		calculatorFrame.getContentPane().add(convertTo);

		JButton conButton = new JButton("Convert!");
		conButton.setIcon(new ImageIcon("data/coin-icon.png"));
		conButton.setForeground(Color.DARK_GRAY);

		//an event that occurs by clicking the convert button.
		conButton.addActionListener((ActionEvent arg0)-> {									  
			//initializing the needed variables.
			double amounted = 0;
			double sum = 0;
			Currency currCurrency = currencies.get(0);
			Currency newCurrency = currencies.get(0);
			String  currCoin = (String) convertFrom.getSelectedItem();
			String newCoin = (String) convertTo.getSelectedItem();
			//searching the currencies according to their code.
			for (int i=0; i<currencies.size(); i++) {
				if (currencies.get(i).getCode().equals(currCoin)) currCurrency = currencies.get(i);			
				if (currencies.get(i).getCode().equals(newCoin)) newCurrency = currencies.get(i);
			}
			try {
				//validates that amount money is a positive number.
				amounted = Double.parseDouble(amount.getText());
				if (amounted < 0) throw new NumberFormatException();

				//calling the convert method of the controller, and store it in the result field.
				sum = controller.convert(amounted, currCurrency,newCurrency);
				DecimalFormat myFormatter = new DecimalFormat("###,###.###");
				result.setText(myFormatter.format(sum)+" " +newCurrency.getCode());
				LogWriter.getInstance().logger.trace("A successfull converting from "+currCurrency.getCode()+" to "+newCurrency.getCode());
			}
			catch(NumberFormatException e) {
				LogWriter.getInstance().logger.trace("An input error in converting - amount of money: "+amount.getText());
				JOptionPane.showMessageDialog(null, "Only positive numbers are allowed!", "Input Error!", 0);
			}
		}
				);
		conButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		conButton.setBackground(SystemColor.activeCaption);
		conButton.setBounds(160, 83, 161, 43);
		calculatorFrame.getContentPane().add(conButton);

		JLabel resultLabel = new JLabel("Result:");
		resultLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		resultLabel.setBounds(250, 137, 82, 16);
		calculatorFrame.getContentPane().add(resultLabel);

		result = new JTextField();
		result.setEditable(false);
		result.setColumns(10);
		result.setBounds(299, 135, 130, 22);
		calculatorFrame.getContentPane().add(result);

		JLabel creditLabel = new JLabel("Made By Yoav Saroya\u00AE");
		creditLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		creditLabel.setBounds(515, 140, 170, 52);
		calculatorFrame.getContentPane().add(creditLabel);

		JLabel bucketImg = new JLabel("");
		bucketImg.setIcon(new ImageIcon("data/Coins.png"));
		bucketImg.setBounds(569, 74, 101, 95);
		calculatorFrame.getContentPane().add(bucketImg);

		JButton resetButton = new JButton("Reset");
		//an event that occurs by clicking the reset button.
		resetButton.addActionListener((ActionEvent arg0)-> {
			//just call the reset method and records it in the Log file.
			resetCalculatorFrame();
			LogWriter.getInstance().logger.trace("Restarting the calculator frame.");
		});
		resetButton.setIcon(new ImageIcon("data/reset.png"));
		resetButton.setForeground(Color.DARK_GRAY);
		resetButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		resetButton.setBackground(SystemColor.activeCaption);
		resetButton.setBounds(385, 83, 161, 43);
		calculatorFrame.getContentPane().add(resetButton);

		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSearch.setBounds(55, 50, 60, 16);
		calculatorFrame.getContentPane().add(lblSearch);

		searchCurrCoin = new JTextField();
		searchCurrCoin.setBounds(115, 48, 60, 22);
		calculatorFrame.getContentPane().add(searchCurrCoin);
		searchCurrCoin.setColumns(10);
		searchCurrCoin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (searchCurrCoin.getText().length() > 2) e.consume();
			}
		});

		searchCurrCoin.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchCurrCoin.getText().equals("NotFound")) searchCurrCoin.setText("");
			}
		});


		JLabel lblbyCode = new JLabel("(by code)");
		lblbyCode.setBounds(55, 65, 56, 16);
		calculatorFrame.getContentPane().add(lblbyCode);

		JButton searchButtonCurrCoin = new JButton("");

		//an events that occurs according to the search button.
		searchButtonCurrCoin.addActionListener((ActionEvent arg0)-> {
			searchCoin(searchCurrCoin, convertFrom);
		});
		searchButtonCurrCoin.setIcon(new ImageIcon("data/search.png"));
		searchButtonCurrCoin.setBounds(174, 48, 20, 21);
		calculatorFrame.getContentPane().add(searchButtonCurrCoin);

		JButton searchButtonNewCoin = new JButton("");
		//an events that occurs according to the search button.
		searchButtonNewCoin.addActionListener((ActionEvent arg0)-> {
				searchCoin(searchNewCoin, convertTo);
			});

		searchButtonNewCoin.setIcon(new ImageIcon("data/search.png"));
		searchButtonNewCoin.setBounds(590, 47, 20, 21);
		calculatorFrame.getContentPane().add(searchButtonNewCoin);

		searchNewCoin = new JTextField();
		searchNewCoin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (searchNewCoin.getText().length() > 2) e.consume();
			}
		});

		searchNewCoin.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchNewCoin.getText().equals("NotFound")) searchNewCoin.setText("");
			}
		});

		searchNewCoin.setColumns(10);
		searchNewCoin.setBounds(531, 47, 60, 22);
		calculatorFrame.getContentPane().add(searchNewCoin);

		JLabel labelSearch2 = new JLabel("Search:");
		labelSearch2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		labelSearch2.setBounds(471, 51, 64, 12);
		calculatorFrame.getContentPane().add(labelSearch2);

		JLabel lblbyCode2 = new JLabel("(by code)");
		lblbyCode2.setBounds(471, 66, 60, 13);
		calculatorFrame.getContentPane().add(lblbyCode2);
	}

	/**
	 * Initialize the contents of the addCoin frame.
	 */
	private void initializeAddCoin() {
		newCoinFrame = new JFrame();
		newCoinFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("data/add-icon.png"));
		newCoinFrame.setTitle("Create a new Coin");
		newCoinFrame.setResizable(false);
		newCoinFrame.setBounds(100, 100, 499, 194);
		newCoinFrame.getContentPane().setLayout(null);
		newCoinFrame.setLocation((int)(mainFrame.getLocationOnScreen().getX())+650,(int)mainFrame.getLocationOnScreen().getY()+224);

		newCoinFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				resetAddCoinFrame();
				newCoinFrame.setVisible(false);
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
		newCoinFrame.getContentPane().add(txtName);
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
		newCoinFrame.getContentPane().add(txtUnit);
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
		newCoinFrame.getContentPane().add(txtCountry);
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
		newCoinFrame.getContentPane().add(txtCode);
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
		newCoinFrame.getContentPane().add(txtRate);
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
		newCoinFrame.getContentPane().add(txtChange);
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
		newCoinFrame.getContentPane().add(btnReset);

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
			//validates that the unit field is a positive natural number.
			try {
				unit = Integer.parseInt(txtUnit.getText());
				if (unit <= 0) throw new NumberFormatException();
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
			if (convertFrom != null) convertFrom.addItem(curr.getCode());
			if (convertTo != null) convertTo.addItem(curr.getCode());
			LogWriter.getInstance().logger.trace("A new coin has been created: "+curr.getCode());
			updateDataTable(true);
			//close the add-coin frame.
			resetAddCoinFrame();
			newCoinFrame.setVisible(false);
		});
		btnCreate.setIcon(new ImageIcon("data/add-icon.png"));
		btnCreate.setForeground(Color.DARK_GRAY);
		btnCreate.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnCreate.setBackground(SystemColor.activeCaption);
		btnCreate.setBounds(253, 83, 240, 76);
		newCoinFrame.getContentPane().add(btnCreate);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 83, 493, 2);
		newCoinFrame.getContentPane().add(separator);
	}
	
	private void searchCoin(JTextField code, JComboBox<String> codeList) {
		for (int i = 0; i <currencies.size(); i++) {
			if (code.getText().equals(currencies.get(i).getCode())) {
				codeList.setSelectedIndex(i);
				if (i != 0) table.addRowSelectionInterval(i-1, i-1);
				return;
			}
		}
		code.setText("NotFound");
	}

	//reset the calculator frame back to default properties.
	private void resetCalculatorFrame() {
		convertTo.setSelectedIndex(0);
		convertFrom.setSelectedIndex(0);
		amount.setText("0");
		result.setText("");
		searchCurrCoin.setText("");
		searchNewCoin.setText("");
		table.clearSelection();
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

	//updating the table of the view.
	public void updateDataTable(boolean update) {
		//getting the currencies according to the local XML file.
		ArrayList<Currency> tempCurrList = controller.parseToCurrencies();
		if (!update) currencies = tempCurrList;

		//if a currency has changed, so the vector of currencies updates.
		for (int i = 0; i < tempCurrList.size(); i++) {
			if (!tempCurrList.get(i).equals(currencies.get(i))) {
				currencies.remove(i);
				currencies.add(tempCurrList.get(i));
			}
		}

		//the codeList gets the code of each currency object.
		codeList = new ArrayList<>();
		for (int i=0; i< currencies.size(); i++)
			codeList.add(currencies.get(i).getCode());

		data = controller.parseToTable(currencies);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		labelDate.setText(dateFormat.format(new Date()));

		if (update) {
			//updating the JTable object.
			for (int i=0; i < table.getRowCount(); i++) {
				for (int j=0; j < table.getColumnCount(); j++) {
					table.setValueAt(data[i][j], i, j);
				}
			}
			DefaultTableModel dtm = ((DefaultTableModel) table.getModel());
			for (int i = table.getRowCount(); i < data.length; i++)
				dtm.addRow(new Object[] {data[i][0],data[i][1],data[i][2], data[i][3],
						data[i][4],data[i][5]});
		}
	}
}