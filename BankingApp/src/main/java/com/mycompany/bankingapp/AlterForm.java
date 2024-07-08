//třída implementující formuláře, skrz které uživatel přidá, či odebere řádek z
//tabulky
package com.mycompany.bankingapp;

import static com.mycompany.bankingapp.AlterDatabase.*;
import static com.mycompany.bankingapp.RevenuesVSExpenses.ListOfAllExpenses;
import static com.mycompany.bankingapp.RevenuesVSExpenses.ListOfBankAccounts;
import static com.mycompany.bankingapp.RevenuesVSExpenses.ListOfRevenues;
import static com.mycompany.bankingapp.GUI.mainFrame;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.Box;
import static javax.swing.Box.createVerticalBox;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

//třída tříd implementující formuláře
public class AlterForm extends JPanel{
    //formulář je implementován jako JPanel
       static class AddTransakce extends JPanel{
           //konstruktor má jako parametr JTable, do které se též přidá nový
           //řádek, aby změna byla vidět hned
        public AddTransakce(JTable table){
            //list of bank accounts
            //přidá do JComboBoxu na výběr ze všech účtu přítomných v databázi
        
            ResultSet bankAccounts = ListOfBankAccounts();
            JComboBox ba = new JComboBox();
            try{
               // if(bankAccounts != null){
            while(bankAccounts.next()){
                ba.addItem(bankAccounts.getString("AccountNumber"));
               
            }
                //}
            }
            catch(SQLException e){
                e.getMessage();
            }
            //type of transaction
            String typeOfTransaction[] = {"Odchozí","Příchozí"};
            JComboBox tran = new JComboBox();
            tran.add(new JLabel("Transakce"));
            tran.addItem(typeOfTransaction[0]);
            tran.addItem(typeOfTransaction[1]);
            //type of expenses
            ResultSet typeOfExpenses = ListOfAllExpenses();
            JComboBox ex = new JComboBox();
            try{
            while(typeOfExpenses.next()){
                ex.addItem(typeOfExpenses.getString("Name"));
            }
            }
            catch(SQLException e){
                e.getMessage();
            }
            //tyep ofrevenues
            ResultSet typeOfRevenues = ListOfRevenues();//JComboBox
            JComboBox rev = new JComboBox();
            try{
                while(typeOfRevenues.next()){
                    rev.addItem(typeOfRevenues.getString("Name"));
                }
            }
            catch(SQLException e ){
                e.getMessage();
            }
            
            //amount
            JTextField am = new JTextField();
           //date            
        JTextField d = new JTextField();
            //note
            JTextField n = new JTextField();
            JButton ok = new JButton("Přidat novou transakci");
            ok.addActionListener(e->{
                //warning dialogs amount
                try{
                
                //po stisknutí zavolá funkci na přidání do databáze
                AddTransactions(ba.getSelectedItem().toString(),tran.getSelectedItem().toString(),ex.getSelectedItem().toString(), 
                    rev.getSelectedItem().toString(),Double.parseDouble(am.getText()),Date.valueOf(d.getText()),n.getText());
                //poté se přidají data do JTable, aby byla změna vidět hned po stisknutí
                //vytvoří se vektor s daty v řádku po jednotlivých sloupcích
                Vector<Object> rowData = new Vector<Object>();
                rowData.add(ba.getSelectedItem().toString());
                rowData.add(tran.getSelectedItem().toString());
                //pokud je typ transakce odchozí, přidá se typ výdaje
                //jinak se sloupec výdajů ponechá prázdný
                if(tran.getSelectedItem().toString().equals("Odchozí")){
                rowData.add(ex.getSelectedItem().toString());
                }
                else{
                    rowData.add("");
                }
                //stejně tak u příchozích výdajů a příchozí transakce
                if(tran.getSelectedItem().toString().equals("Příchozí")){
                rowData.add(rev.getSelectedItem().toString());
                }
                else{
                    rowData.add("");
                }
                rowData.add(Double.parseDouble(am.getText()));
                rowData.add(Date.valueOf(d.getText()));
                rowData.add(n.getText());
                //nakonec se vytvoří DefaultTableModel, extrahováním modelu JTable
                DefaultTableModel tm = (DefaultTableModel)table.getModel();
                //přidá se do něj vektor rowData na vždy na začátek-0
                tm.insertRow(0, rowData);
                 }
            catch(java.lang.NumberFormatException amountE){
                    JOptionPane.showMessageDialog(mainFrame, "Částka musí být číslo", "Chyba", JOptionPane.WARNING_MESSAGE);
                    }
            catch(java.lang.IllegalArgumentException dateE){
                 JOptionPane.showMessageDialog(mainFrame, "Datum není ve správném formátu", "Chyba", JOptionPane.WARNING_MESSAGE);
            }
            catch(java.lang.NullPointerException eNull){
                JOptionPane.showMessageDialog(mainFrame, "Nebyl přidán účet, či výdaj, či příjem"
                  , "Cyba", JOptionPane.ERROR_MESSAGE);
            }
            });
            
            //add to AddFrom
            Box box = createVerticalBox();
            box.add(new JLabel("Číslo účtu"));
            box.add(ba);
            box.add(new JLabel("Transakce"));
            box.add(tran);
            box.add(new JLabel("Výdaj"));
            box.add(ex);
            box.add(new JLabel("Příjem"));
            box.add(rev);
            box.add(new JLabel("Částka"));
            box.add(am);
            box.add(new JLabel("Datum ve formátu: YYYY-MM-DD"));
            box.add(d);
            box.add(new JLabel("Poznámka(nepovinný)"));
            box.add(n);
            box.add(ok);
            
            add(box);
        
            
        }
        
    }
    
       static class AddRevenues extends JPanel{
           public AddRevenues(JTable table){
               JTextField revenue = new JTextField();
               
               JButton ok = new JButton("Přidat typ příjmu");
               ok.addActionListener(e->{
                   
                   AddTypesOfRevenues(revenue.getText());
                   Vector<Object> rowData = new Vector<Object>();
                   rowData.add(revenue.getText());
                   DefaultTableModel tm = (DefaultTableModel)table.getModel();
                  tm.addRow(rowData);
               });
               Box box = createVerticalBox();
               box.add(new JLabel("Příjem"));
               box.add(revenue);
               
               
               box.add(ok);
               add(box);
           }
       }
       static class AddExpenses extends JPanel{
           public AddExpenses(JTable table){
                JTextField expense = new JTextField();
                JComboBox type = new JComboBox();
                type.addItem("Zbytné");
                type.addItem("Nezbytné");
               JButton ok = new JButton("Přidat typ výdaje");
               ok.addActionListener(e->{
                   AddTypesOfExpenses(expense.getText(),type.getSelectedItem().toString());
                   Vector<Object> rowData = new Vector<Object>();
                   rowData.add(expense.getText());
                   rowData.add(type.getSelectedItem().toString());
                   DefaultTableModel tm = (DefaultTableModel)table.getModel();
                   tm.addRow(rowData);
                       });
               Box box = createVerticalBox();
               box.add(new JLabel("Výdaj"));
               box.add(expense);
               box.add(new JLabel("Druh výdaje"));
               box.add(type);
               box.add(ok);
               add(box);
               
           }
       }
       
       static class AddBankAccount extends JPanel{
           public AddBankAccount(JTable table){
               JTextField anumber = new JTextField();
               JTextField name = new JTextField();
               JTextField note  = new JTextField();
               JButton ok = new JButton("Přidat účet");
               ok.addActionListener(e->{
                   AddBankAccounts(anumber.getText(),name.getText(),note.getText());
                   
                    DefaultTableModel tm = (DefaultTableModel)table.getModel();
                    Vector<Object> rowData = new Vector<Object>();
                    rowData.add(anumber.getText());
                    rowData.add(name.getText());
                    rowData.add(note.getText());
           tm.addRow(rowData);
                       });
               
               Box box = createVerticalBox();
               
               box.add(new JLabel("Číslo účtu"));
               box.add(anumber);
               box.add(new JLabel("Název(nepovinný)"));
               box.add(name);
               box.add(new JLabel("Poznámka(nepovinný)"));
               box.add(note);
               box.add(ok);
               
               add(box);
               
           }
       }
       
       //metoda, která ze zadané tabulky, identifikované dle klíče whichTable odstraní
       //uživatelem z tabulky vybraný řádek 
       public static void deleteSelectedRow(JTable table, String whichTable){
           //index uživatelem vybraného řádku
           int selectedRow = table.getSelectedRow();
           if(whichTable.equals("Transactions")){
               //získá hodnotu z uživatelem vybraného řádku v nultém sloupci
               //a přetypuje na string
               String ba = table.getValueAt(selectedRow, 0).toString();
               String typeOfT = table.getValueAt(selectedRow, 1).toString();
               String typeOfEx = null;
               if(table.getValueAt(selectedRow, 2) != null){
               typeOfEx = table.getValueAt(selectedRow, 2).toString();
               }
               String typeOfRev = null;
               if(table.getValueAt(selectedRow, 3) != null){
               typeOfRev = table.getValueAt(selectedRow, 3).toString();
               }
               Double amount = Double.parseDouble(table.getValueAt(selectedRow, 4).toString());
               Date date = Date.valueOf(table.getValueAt(selectedRow, 5).toString());
               DeleteTransaction(ba,typeOfT,typeOfEx,typeOfRev,amount,date);
           }
           else if(whichTable.equals("BankAccount")){
               String accountNumber = table.getValueAt(selectedRow, 0).toString();
               DeleteBankAccount(accountNumber);
               
           }
           else if(whichTable.equals("TypesOfExpenses")){
               String name = table.getValueAt(selectedRow, 0).toString();
               DeleteTypeOfExpenses(name);
           }
           else if(whichTable.equals("TypesOfRevenues")){
               String name = table.getValueAt(selectedRow, 0).toString();
               DeleteTypeOfRevenues(name);
           }
           //nakonec se odtraní řádek v JTable
           DefaultTableModel tm = (DefaultTableModel)table.getModel();
           tm.removeRow(selectedRow);
       }
    
}
