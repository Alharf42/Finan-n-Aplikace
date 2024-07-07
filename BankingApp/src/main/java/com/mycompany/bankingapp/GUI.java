//třída implementující grafické rozhranní aplikace
package com.mycompany.bankingapp;

import com.mycompany.bankingapp.AlterForm.*;
import static com.mycompany.bankingapp.Balance.*;
import static com.mycompany.bankingapp.RevenuesVSExpenses.*;
import java.sql.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;

import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Box;
import static javax.swing.Box.createHorizontalBox;
import static javax.swing.Box.createVerticalBox;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


public class GUI {
    //hlavní okno aplikace
    protected static final JFrame mainFrame= new JFrame("MainFrame");
    //proměná pro potřeby přepínání mezi stránkami
    private static JPanel currentPage;
    private static final Color background = new Color(213,237,252);
    private static final Color lightBlue = new Color(84,154,232);
   
    
    
    public GUI(){
        
       
        
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setSize(1400,900);
        mainFrame.setLocationRelativeTo(null);
        
     //funkce pro vytvoření horní navigace mezi stránkami
        TopNavigation();
        
        //funkce pro přepínaní stránek v navigace jejíž výchozí stránka je MainPage
       switchPage(new MainPagePanel());
     
       mainFrame.setVisible(true);
       
    }
    
    private void TopNavigation(){
        //create menubar
        //create menus
        //add them to menubar
        //set menu bar for this frame
        //add listeners
        JMenuBar nav = new JMenuBar();
        nav.setBackground(lightBlue);
        
        
//        JMenuItem mainPage = new JMenu("mainPage");
//        JMenuItem page2 = new JMenu("page2");
//        JMenuItem page3 = new JMenu("page3");
//        JMenuItem page4 = new JMenu("page4");

//tlačítka do hlavní navigace pro přepínání stránek
          CustomButton mainPage = new CustomButton("");
          ImageIcon mainPageIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/MainPage.png")).
                  getImage().getScaledInstance(15,15,Image.SCALE_DEFAULT));//změní velikost obrázku
          //dle výchozího škálovacího algoritmu 
          mainPage.setIcon(mainPageIcon);
          CustomButton page2 = new CustomButton("Finanční bilance");
          CustomButton page3 = new CustomButton("Příjmy");
          CustomButton page4 = new CustomButton("Výdaje");
          
          //vedlejší menu
          JMenu other = new JMenu();
        ImageIcon otherIcon = new ImageIcon( new ImageIcon(getClass().getResource("/images/OtherMainMenu.png")).
                getImage().getScaledInstance(12, 12, Image.SCALE_DEFAULT));
        //pozadí JMenuBar je neprůhledné
        other.setOpaque(true);
        other.setBackground(Color.white);
        other.setIcon(otherIcon);
        JMenuItem settings = new JMenuItem("Nastavení");
        JMenuItem etc = new JMenuItem("Platební účty");
        other.add(settings);
        other.add(etc);
        
        nav.add(other);
        nav.add(other);
        nav.add(mainPage);
        nav.add(page2);
        nav.add(page3);
        nav.add(page4);
        
        mainFrame.setJMenuBar(nav);
       //přídání ActionListener k tlačítkům, aby při kliknutí supstili funkci
       //switchPage, která přepne stránka na danou zvolenou
        mainPage.addActionListener(e -> switchPage(new MainPagePanel()));
        page2.addActionListener(e -> switchPage(new Page2Panel()));
        page3.addActionListener(e-> switchPage(new Page3Panel()));
        page4.addActionListener(e->switchPage(new Page4Panel()));
        settings.addActionListener(e ->
                switchPage(new SettingsPanel())
            );
        etc.addActionListener(e ->
                switchPage(new EtcPanel()));
   
    }
    //funkce pro přepínání stránek
    private void switchPage(JPanel newPage){
        //odstraní z mainFrame aktuální stránku, pokud zrovna nespouštíme
        //aplikaci
        if(currentPage != null){ 
            mainFrame.remove(currentPage);
        }
        //nastavíme aktuální stránku na stránku na kterou chceme jít-newPage
        currentPage = newPage;
        mainFrame.add(currentPage, BorderLayout.CENTER);
        //po změně JFrame 
        //layout manager přepočítá layout
        mainFrame.revalidate();
        //znovu se vykreslí mainFrame,odstraní předhozí výkresy
        mainFrame.repaint();
        
        
    }
    //třída pro vytvoření tlačítka třidy JButton s jinou grafikou
    class CustomButton extends JButton{
        //konstruktor s parametrem text-nápis na tlačítku
        public CustomButton(String text){
            //volá konstruktor rodiče
            super(text);
            //nastaví tlačítko s prázdným pozadí
            setContentAreaFilled(false);
            //zruší původní grafiku při přejetí myši 
            setFocusPainted(false);
            //nyní prázdné pozadí je i průhledné
            setOpaque(false);
            //zruší původní border
            setBorderPainted(false);
           
        }
        //přepíše funkci rodiče pro grafiku tlačítka
        @Override
            public void paintComponent(Graphics g){
                
            if(getModel().isPressed()){
                Color darkerBlue = new Color(9,98,201);
            g.setColor(darkerBlue);
            //nastaví barvu písma
            setForeground(Color.LIGHT_GRAY);
            }
            else if(getModel().isRollover()){
                Color darkerBlue = new Color(41,123,216);
            g.setColor(darkerBlue);
            setForeground(Color.DARK_GRAY);
            }
            else{
            Color lightBlue = new Color(84,154,232);
            g.setColor(lightBlue);
            setForeground(Color.DARK_GRAY);
            }
            //
            //grafika vyplní obdelník
            g.fillRect(0, 0, getWidth(), getHeight()); 
            
        //nyní se zavolá původní funkce rodiče
            super.paintComponent(g);
            
        }
    }
    //fuknce pro vytvoření vyskakovacího okna používáného při, odstranění a
    //přidání dat do tabulky
    public static void createPopup(JPanel popup){
        //Popup po;
        //PopupFactory pf = new PopupFactory();
        
        //vytvoří nový JFrame do kterého vloží daný JPanel
        JFrame f = new JFrame();
        f.setLayout(new BorderLayout());
        f.setSize(300,400);
        f.setLocationRelativeTo(null);
        //po = pf.getPopup(f, popup, 300, 400);
        f.add(popup);
        f.show();
    }
    //vlatní JPanel pro zobrazení tabulek z databáze 
    class TablePanel extends JPanel{
        //addd je formulář pro vkládaní dat do tabulky
        //implemetace formulářů jsou ve třídě AlterForm
        //table je tabulka kterou chceme vložit
        
        //whichTable se používá u funkce deleteSelectedRow(implementováno ve třídě AlterForm),
        //pro identifikaci tabulky z které chceme odstranit řádek
        public TablePanel(JPanel addd, JTable table, String whichTable){//addForm addd
            JMenuBar menu = new JMenuBar();
            
            CustomButton add = new CustomButton("+");
            //po kliknutní tlačítka add se zobrazí formulář addd
            add.addActionListener(e -> createPopup(addd) );
            
            CustomButton delete = new CustomButton("—");
            delete.addActionListener(e->{
                try{
                    AlterForm.deleteSelectedRow(table,whichTable);
                }
                catch(java.lang.ArrayIndexOutOfBoundsException nothingSelected){
                     JOptionPane.showMessageDialog(mainFrame, "Nebyl vybrán řádek k odstranění", "Chyba", JOptionPane.WARNING_MESSAGE);
                }
                            });
            
            menu.add(add);
            menu.add(delete);
            menu.setBackground(lightBlue);
            
            setLayout(new BorderLayout());
            add(menu, BorderLayout.NORTH);
            //nastaví preferovanou velikost panelu
            setPreferredSize(new Dimension(500,300));
            setBorder(new LineBorder(Color.BLUE));
            
        }
    }
    //vlastní panel pro zobrazení vypočítaných dat
    class DataPanel extends JPanel{
        
        public DataPanel(){
            setLayout(new FlowLayout());
            setBackground(background);
            setBorder(new LineBorder(Color.blue));
            setSize(30,100);
            
        }
        
    }
    //funkce pro vytvoření modelu tabulky z ResultSet(SELECT FROM)tabulky z databáze
    //funkce jsou implementované ve třídě RevenuesVSExpenses
    public static DefaultTableModel createTableModel(ResultSet rs){
        try{
            ResultSetMetaData metaData = rs.getMetaData();
            Vector<String> columnNames = new Vector<String>();
            for(int i =1;i<=metaData.getColumnCount();i++){
                columnNames.add(metaData.getColumnName(i));
            }
            //vektor z vektorů představujících jednotlivé řádky tabulky
            Vector<Vector<Object>> dataVectors = new Vector<Vector<Object>>();
            //iteruje rs po řádku, dokud nějaký existuje
        while(rs.next()){
            //při každé iteraci se vytvoří nový vektor s aktuálním řádkem z rs
            Vector<Object> columndata = new Vector<Object>();//one row of columns
            //iteruje rs po sloupcích a vkládá z ních data do vektoru
           for(int i =1; i<= metaData.getColumnCount();i++){
              
               columndata.add(rs.getObject(i));
               
           }
           //poté je vektor s aktuálním řádkem vložen do dataVectors
           dataVectors.add(columndata);
        }
        
        DefaultTableModel tm = new DefaultTableModel(dataVectors,columnNames);
        return tm;
        }
        catch(SQLException e){
            e.getMessage();
            return null;
        }
        
    }
    //funkce pro nastavení vlastní hlavičky tabulky
    public static void setTableHeader(String header[], JTable table){
        
            for(int i =0; i<table.getColumnCount();i++){
                //získá sloupec hlavičky dané tabulky
                //a změní její hodnotu dle zadaného stringu s názvy sloupců
                TableColumn col = table.getTableHeader().getColumnModel().getColumn(i);
                col.setHeaderValue(header[i]);
            }
    }
    //vlastní hlavní panel
    class MainPagePanel extends JPanel{
        
        public MainPagePanel(){
           
            setLayout(new FlowLayout());
 //box s vypočtenými daty       
            Box ver1 = createVerticalBox();
            //box s tabulkou
            Box ver2 = createVerticalBox();
            
            Box ver1CurrentDate = createHorizontalBox();
            Box ver1SetDate = createHorizontalBox();
            Box ver1Sum = createHorizontalBox();
            Box ver1Income = createHorizontalBox();
            Box ver1Expenses = createHorizontalBox();
            
            DataPanel date = new DataPanel();
            date.add(new JLabel("Dnešní datum: "));
            
            DataPanel date2 = new DataPanel();
             SimpleDateFormat fr = new SimpleDateFormat("dd-MM-yyyy");
            String dateTime = fr.format(new java.util.Date());
            date2.add(new JLabel(dateTime));
            
            ver1CurrentDate.add(date);
            ver1CurrentDate.add(date2);
            
            DataPanel setDate = new DataPanel();
            setDate.add(new JLabel("Začátek sledovaného období: "));
            
            DataPanel setDate2 = new DataPanel();
            Date setPeriod = setMonthlyDate();
            SimpleDateFormat fr2 = new SimpleDateFormat("dd-MM-yyyy");
            String dateTime2 = fr2.format(setPeriod);
            setDate2.add(new JLabel(dateTime2));
            
            ver1SetDate.add(setDate);
            ver1SetDate.add(setDate2);
            
            DataPanel sum = new DataPanel();
            sum.add(new JLabel("Celkem: "));
            
            DataPanel sum2 = new DataPanel();
            String sumBalance = String.valueOf(sumBalance());
            sum2.add(new JLabel(sumBalance));
            
            ver1Sum.add(sum);
            ver1Sum.add(sum2);
            
            DataPanel income = new DataPanel();
            JLabel prijmy = new JLabel("Příjmy: ");
            income.add(prijmy);
            
            DataPanel income2 = new DataPanel();
            String rev = String.valueOf(sumAllRevenues());
            income2.add(new JLabel(rev));
            
            ver1Income.add(income);
            ver1Income.add(income2);
            
            DataPanel expenses = new DataPanel();
            JLabel naklady = new JLabel("Výdaje: ");
            expenses.add(naklady);
            
            DataPanel expenses2 = new DataPanel();
            String ex = String.valueOf(sumAllExpenses());
            expenses2.add(new JLabel(ex));
            
            ver1Expenses.add(expenses);
            ver1Expenses.add(expenses2);
            
            //tabulka se všemi transakcemi
            //implemetace funkce ListOfAllTransactions je ve tride RevenuesVSExpenes
            JTable transakceTable = new JTable(createTableModel(ListOfAllTransactions()));
            String header[] = {"Číslo účtu", "Transakce", "Typ Výdaje", "Typ Výnosu","Částka","Datum","Poznámka" };
            setTableHeader(header,transakceTable);
            
            //JPanel formulář pro přidání řádku do tabulky
            var transakceAdd = new AddTransakce(transakceTable);
            TablePanel transakce = new TablePanel(transakceAdd,transakceTable, "Transactions");
            transakce.add(new JScrollPane(transakceTable), BorderLayout.CENTER);
            
            ver2.add(new JLabel("Transakce"));
            ver2.add(transakce);
            
            ver1.add(ver1CurrentDate);
            ver1.add(ver1SetDate);
            ver1.add(ver1Sum);
            ver1.add(ver1Income);
            ver1.add(ver1Expenses);
           
            
            add(ver1);
            add(ver2);
            
            //barva pozadí
            setBackground(background);
            
        }
        
    }
    //Financni bilance table
    class Page2Panel extends JPanel{
        
        public Page2Panel(){
            Box tables = createVerticalBox();
            Box calculations = createVerticalBox();
            
            //tabulka mesicnich prijmu 
            DefaultTableModel monthlyRevModel = createTableModel(getMonthlyRevenues());
            JTable tableMonthlyRev = new JTable(monthlyRevModel);
            String monthlyRevHeader[] ={"Příjem","Částka"};
            setTableHeader(monthlyRevHeader,tableMonthlyRev);
            //tabulka mesicnich nakladu 
            DefaultTableModel monthlyExModel = createTableModel(getMonthlyExpenses());
            JTable tableMonthlyEx = new JTable(monthlyExModel);
            String monthlyExHeader[] ={"Výdaj","Částka"};
            setTableHeader(monthlyExHeader,tableMonthlyEx);
            //tabilka mesicnich transakci 
            
            DefaultTableModel tm = createTableModel(getMonthlyTransactions());
            JTable tableTransactions = new JTable(tm);
             String header[] = {"Číslo účtu", "Transakce", "Typ Výdaje", "Typ Výnosu","Částka","Datum","Poznámka" };
            setTableHeader(header,tableTransactions);
            
            
            //tabulka zbytnych (nutných) nákladů
    
            List<Map<String,Double>> unnecMap = monthlyZbytne();//funkce implementována
            //ve třidě RevenuesVSExpenses
            //zde se vytvoří nový model pro tabulku z List<Map>, algoritmus je stejný
            //jako u funkce createTableModel
            Vector<String> unnecColumnNames = new Vector<String>();
            unnecColumnNames.add("Zbytné");
            unnecColumnNames.add("Částka");
            Vector<Vector<Object>> unnecDataVectors = new Vector<Vector<Object>>();
            for(Map<String,Double> row : unnecMap){
                 Vector<Object> rowOfColumns = new Vector<Object>();
                for(Map.Entry<String,Double> entry : row.entrySet()){
               
                rowOfColumns.add(entry.getKey());
                rowOfColumns.add(entry.getValue());
                
            }
                unnecDataVectors.add(rowOfColumns);
                
            }
             DefaultTableModel tmUnnecModel = new DefaultTableModel(unnecDataVectors,unnecColumnNames);
             JTable tableMonthlyUnnec = new JTable(tmUnnecModel);
           
                    
            //tabulka nezbytnych - do panelu naklady
            List<Map<String,Double>> necMap = monthlyNezbytne();
            
            Vector<String> necColumnNames = new Vector<String>();
            necColumnNames.add("Nezbytné");
            necColumnNames.add("Částka");
            Vector<Vector<Object>> necDataVectors = new Vector<Vector<Object>>();
            for(Map<String,Double> row : necMap){
                 Vector<Object> rowOfColumns = new Vector<Object>();
                for(Map.Entry<String,Double> entry : row.entrySet()){
               
                rowOfColumns.add(entry.getKey());
                rowOfColumns.add(entry.getValue());
                
            }
                necDataVectors.add(rowOfColumns);
                
            }
             DefaultTableModel tmNecModel = new DefaultTableModel(necDataVectors,necColumnNames);
             JTable tableMonthlyNec = new JTable(tmNecModel);
            
            //zde se vytvoří DataPanely pro vybranné vypočtené hodnoty
            //funkce pro výpočet hodnot jsou implementovány ve třídě Balance
            //balanc
            //celkove prijmy
            //celkove naklady
            DataPanel sum = new DataPanel();
            sum.add(new JLabel("Finanční bilance: "));
            sum.setPreferredSize(new Dimension(110,20));
            
            DataPanel sum2 = new DataPanel();
            String sumBalance = String.valueOf(sumBalance());
            sum2.add(new JLabel(sumBalance));
            sum2.setPreferredSize(new Dimension(100,20));
            
            Box balance = createHorizontalBox();
            balance.add(sum);
            balance.add(sum2);
            
            DataPanel income = new DataPanel();
            JLabel prijmy = new JLabel("Celkové příjmy: ");
            income.add(prijmy);
            income.setPreferredSize(new Dimension(110,20));
            
            DataPanel income2 = new DataPanel();
            String rev = String.valueOf(sumAllRevenues());
            income2.add(new JLabel(rev));
            income2.setPreferredSize(new Dimension(100,20));
            
            Box allRev = createHorizontalBox();
            allRev.add(income);
            allRev.add(income2);
            
            DataPanel expenses = new DataPanel();
            JLabel naklady = new JLabel("Celkové náklady: ");
            expenses.add(naklady);
            expenses.setPreferredSize(new Dimension(110,20));
            
            DataPanel expenses2 = new DataPanel();
            String ex = String.valueOf(sumAllExpenses());
            expenses2.add(new JLabel(ex));
            expenses2.setPreferredSize(new Dimension(100,20));
            
            Box allEx = createHorizontalBox();
            allEx.add(expenses);
            allEx.add(expenses2);
            //celkove zbytne
             DataPanel zbytne = new DataPanel();
            zbytne.add(new JLabel("Zbytné Náklady: "));
            zbytne.setPreferredSize(new Dimension(110,20));
            
            DataPanel zbytne2 = new DataPanel();
            zbytne2.add(new JLabel(String.valueOf(sumZbytne())));
            zbytne2.setPreferredSize(new Dimension(100,20));
            
            Box unnecessary = createHorizontalBox();
            unnecessary.add(zbytne);
            unnecessary.add(zbytne2);
            //celkove nezbytne
            DataPanel nezbytne = new DataPanel();
            nezbytne.add(new JLabel("Nezbytné Náklady: "));
            nezbytne.setPreferredSize(new Dimension(110,20));
            
            DataPanel nezbytne2 = new DataPanel();
            nezbytne2.add(new JLabel(String.valueOf(sumNezbytne())));
            nezbytne2.setPreferredSize(new Dimension(100,20));
            
            Box necessary = createHorizontalBox();
            necessary.add(nezbytne);
            necessary.add(nezbytne2);
            
            //tabulka celkovych typu prijmy za mesic
            //jejich součet je implementován ve třidě Balance
            
            Vector<String> revColumnNames = new Vector<String>();
            revColumnNames.add("Typ příjmu");
            revColumnNames.add("Částka");
            Map<String,Double> typesOfRevenues = sumTypesOfRevenues();
            Vector<Vector<Object>> revDataVector = new Vector<Vector<Object>>();
            //iteruje map typesOfRevenues
            //vytvoří vektor pro data v řádku a pak jej vloží do revDataVector
            for(Map.Entry<String,Double> entry : typesOfRevenues.entrySet()){
                //in for statetment to add only one row each time
                Vector<Object> rowData = new Vector<Object>();
             rowData.add(entry.getKey());
             rowData.add(entry.getValue());
             revDataVector.add(rowData);
            
            }
            DefaultTableModel tmRev = new DefaultTableModel(revDataVector,revColumnNames);
            JTable revenues = new JTable(tmRev);
            
            //tabulka celkovy typu naklady za mesic
            Vector<String> exColumnNames = new Vector<String>();
            exColumnNames.add("Typ výdaje");
            exColumnNames.add("Částka");
            Map<String,Double> typesOfExpenses = sumTypesOfExpenses();
            Vector<Vector<Object>> exDataVector = new Vector<Vector<Object>>();
            
            for(Map.Entry<String,Double> entry : typesOfExpenses.entrySet()){
                //in for statetment to add only one row each time
                Vector<Object> rowData = new Vector<Object>();
             rowData.add(entry.getKey());
             rowData.add(entry.getValue());
             exDataVector.add(rowData);
            
            }
            DefaultTableModel tmEx = new DefaultTableModel(exDataVector,exColumnNames);
            JTable expensesTypes = new JTable(tmEx);
            
            ///
            
            tables.add(new JLabel("Transakce za dané období"));
            JScrollPane paneTransactions = new JScrollPane(tableTransactions);
            //aby byly všechny tabulky stejně velké stačí nastavit
            //velikost JScrollPane pouze u jedné, Box přízbpůsobý velikost u ostatních
            paneTransactions.setPreferredSize(new Dimension(500,300));
            tables.add(paneTransactions);
            tables.add(new JLabel("Typy příjmů za dané období"));
            tables.add(new JScrollPane(revenues));
            tables.add(new JLabel("Typy výdajů za dané období"));
            tables.add(new JScrollPane(expensesTypes));
             tables.add(new JLabel("Příchozí transakce za dané období"));
            tables.add(new JScrollPane(tableMonthlyRev));
             tables.add(new JLabel("Odchozí transakce za dané období"));
              tables.add(new JScrollPane(tableMonthlyEx));
              tables.add(new JLabel("Zbytné výdaje za dané období"));
              tables.add(new JScrollPane(tableMonthlyUnnec));
              tables.add(new JLabel("Nezbytné výdaje za dané období"));
              tables.add(new JScrollPane(tableMonthlyNec));
            
            calculations.add(balance);
            calculations.add(allRev);
            calculations.add(allEx);
            calculations.add(necessary);
            calculations.add(unnecessary);
            
            //panel pro scrollování boxu s tabulkami
            JPanel scrollPanel = new JPanel();
            scrollPanel.add(tables);
            scrollPanel.setBackground(background);
            JScrollPane pane = new JScrollPane(scrollPanel);
            pane.setBackground(background);
            //nastaví rychlost vertikálního srollbaru
            pane.getVerticalScrollBar().setUnitIncrement(16);
            
            setLayout(new BorderLayout());
            
            add(calculations,BorderLayout.WEST);
            add(pane,BorderLayout.CENTER);
            
            
            setBackground(background);
        }
        
    }
    //panel s tabulkou všech typů příjmů, pro jejich odebrání a přidání z
    //databáze
    class Page3Panel extends JPanel{
        
        public Page3Panel(){
            Box box = createVerticalBox();
            box.add(new JLabel("Typy příjmů"));
            
            JTable prijmyTable = new JTable(createTableModel(ListOfRevenues()));
            String header[] = {"Příjmy"};
            setTableHeader(header,prijmyTable);
            AddRevenues addprijmy = new AddRevenues(prijmyTable);
            TablePanel prijmy = new TablePanel(addprijmy,prijmyTable,"TypesOfRevenues");
        
            prijmy.add(new JScrollPane(prijmyTable));
            
            box.add(prijmy);
            add(box);
            setBackground(background);
        }
        
    }
    //panel pro tabulku všech typů výdajů
    class Page4Panel extends JPanel{
        
        public Page4Panel(){
            
            Box all = createVerticalBox();
            
            JTable allTable = new JTable(createTableModel(ListOfAllExpenses()));
            String header[] = {"Výdaj","Druh výdaje"};
            setTableHeader(header,allTable);
            
            AddExpenses exp = new AddExpenses(allTable);
            TablePanel allExp= new TablePanel(exp,allTable,"TypesOfExpenses");
            
            
            allExp.add(new JScrollPane(allTable));
            
            all.add(new JLabel("Všechny výdaje"));
            all.add(allExp);
            
            add(all);
            setBackground(background);
        }
        
    }
    //panel pro nastavení sledovaného období
    class SettingsPanel extends JPanel{
        
        public SettingsPanel(){
            
            Box setDate = createHorizontalBox();
            //kombo box pro výběr počtu sledovaných dní
            JComboBox day = new JComboBox();
            day.addItem(30);
            day.addItem(180);
            day.addItem(360);
            //jako vychozí hodnota se v comboBoxu day zobrazí ta dříve zvolená
            day.setSelectedItem(RevenuesVSExpenses.day);
            
            JButton monthlyDate = new JButton("Nastavit sledované období");
            //před stiknutím tlačítka se nastaví výchozí období, zavoláním funkce
            //setMontlyDate
             RevenuesVSExpenses.setMonthlyDate();
             //po stiknutí tlačítka se zavolá funkce pro změnu sledovaného období
             //která má jako parametr vybraný počet dní z comboBoxu
            monthlyDate.addActionListener(e->{
                
                RevenuesVSExpenses.updateDay((int)day.getSelectedItem());
                
                    });
            setDate.add(new JLabel("Zadejte počet sledovaných dní: "));
            setDate.add(day);
            setDate.add(monthlyDate);
            
            add(setDate);
            setBackground(background);
        }
        
    }
    //panel s tabulkou pro přidání a odebrání účtu z databáze
    class EtcPanel extends JPanel{
        
        public EtcPanel(){
            Box box = createVerticalBox();
            
            
            JTable baTable = new JTable(createTableModel(ListOfBankAccounts()));
             String header[] = {"Číslo účtu","Název","Poznámka"};
            setTableHeader(header,baTable);
            AddBankAccount addBaPanel = new AddBankAccount(baTable);
            TablePanel baPanel = new TablePanel(addBaPanel,baTable,"BankAccount");
            
           
            baPanel.add(new JScrollPane(baTable));
            
            box.add(new JLabel("Účty"));
            box.add(baPanel);
            add(box);
            setBackground(background);
        }
        
    }
    
}
