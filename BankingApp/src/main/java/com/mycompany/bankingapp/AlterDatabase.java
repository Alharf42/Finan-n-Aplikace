//třída metod přidávající řádky do tabulek v databázi
package com.mycompany.bankingapp;

import java.sql.*;

public class AlterDatabase {
    //atribut uchovávající sql příkaz
    private static String query; 
    //konstanta uchovávající url databáze, její název, název a heslo uživatele
    private static final String url = "jdbc:derby://localhost:1527/banking;user=banking;password=banking";
    //metody pro přidání dat do dílčích tabulek
    //jako parametry berou hodnoty jednotlivých sloupců zadaných uživatelem
    public static void AddTypesOfRevenues(String type){
      try{

        query="INSERT INTO TypesOfRevenues (name) VALUES (?)";
        Connection conn = DriverManager.getConnection(url);
        PreparedStatement prestm = conn.prepareStatement(query);
        prestm.setString(1, type);
        prestm.executeUpdate();
      }
      catch(SQLException e)
       {
           e.getMessage();
       }
         
    }
    //metoda přidávající data do Tabulky Výdajů
    //jako parametr bere název výdaje a jeho druh(zbytný, nezbytný)
     public static void AddTypesOfExpenses(String type, String typetype){
      try{
        query="INSERT INTO TypesOfExpenses (name,type) VALUES (?,?)";
        Connection conn = DriverManager.getConnection(url);
        PreparedStatement prestm = conn.prepareStatement(query);
        prestm.setString(1, type);
        prestm.setString(2, typetype);
        prestm.executeUpdate();
        
      }
      catch(SQLException e)
       {
           e.getMessage();
       }
        
    }
     
     public static void AddBankAccounts(String accountNumber, String name, String note){
      try{
        Connection conn = DriverManager.getConnection(url);
        PreparedStatement prestm;
        //hodnoty note a name v tabulce v databázi jsou volitelné
        //ošetřuje všechny možnosti, kde by nějaká hodnota mohla být null
        //v sql příkazech se přidává pouze do těch sloupců, které nemají uživatelem
        //zadanou null hodnotu
        if(note.isEmpty()){
        query="INSERT INTO BankAccount (AccountNumber,Name) VALUES (?,?)";
        prestm = conn.prepareStatement(query);
        prestm.setString(1,accountNumber);
        prestm.setString(2, name);
        }
        else if(name.isEmpty()){
            query="INSERT INTO BankAccount (AccountNumber,Note) VALUES (?,?)";
            prestm = conn.prepareStatement(query);
        prestm.setString(1,accountNumber);
        prestm.setString(2, note);
        }
        else if(name.isEmpty() && note.isEmpty()){
            query="INSERT INTO BankAccount (AccountNumber,Name) VALUES (?)";
            prestm = conn.prepareStatement(query);
        prestm.setString(1,accountNumber);
        
        }
        else{
        query="INSERT INTO BankAccount (AccountNumber,Name,Note) VALUES (?,?,?)";
        prestm = conn.prepareStatement(query);
        prestm.setString(1,accountNumber);
        prestm.setString(2, name);
        prestm.setString(3,note);
        
        }
        
        prestm.executeUpdate();
      }
      catch(SQLException e)
       {
           e.getMessage();
       }
         
    }
     
     public static void AddTransactions(String accountNumber, String typeOfTransaction, 
             String typeOfExpenses, String typeOfRevenues, double amount, Date date, String note) {
    try  {
        Connection conn = DriverManager.getConnection(url);
        PreparedStatement prestm;
        //note je volitená hodnota-může být null
        //pokud je typ transakce odchozí, tak typ přijmu je null
        //pokud je typ transakce příchozí, tak typ výdaje je null
        //ošetřuje všechny možnosti, kde by nějaká hodnota mohla být null
        if(typeOfTransaction.equals("Odchozí") && note.isEmpty())
        {
        query = "INSERT INTO Transactions (IdBankAccount, TypeOfTransaction, IdTypesOfExpenses, "
                + " Amount, Date) VALUES (?,?,?,?,?)";
        prestm = conn.prepareStatement(query);
        prestm.setString(1,accountNumber);
        prestm.setString(2, typeOfTransaction);
        prestm.setString(3, typeOfExpenses);
        prestm.setDouble(4,amount);
        prestm.setDate(5,date);
        }
        else if(typeOfTransaction.equals("Odchozí")){
            query = "INSERT INTO Transactions (IdBankAccount, TypeOfTransaction, IdTypesOfExpenses, "
                + " Amount, Date, Note) VALUES (?,?,?,?,?,?)";
        prestm = conn.prepareStatement(query);
        prestm.setString(1,accountNumber);
        prestm.setString(2, typeOfTransaction);
        prestm.setString(3, typeOfExpenses);
        prestm.setDouble(4,amount);
        prestm.setDate(5,date);
        prestm.setString(6,note);
        }
        else if(typeOfTransaction.equals("Příchozí") && note.isEmpty()){
            query = "INSERT INTO Transactions (IdBankAccount, TypeOfTransaction, "
                + "IdTypesOfRevenues, Amount, Date) VALUES (?,?,?,?,?)";
        prestm = conn.prepareStatement(query);
         prestm.setString(1,accountNumber);
        prestm.setString(2, typeOfTransaction);
        prestm.setString(3, typeOfRevenues);
        prestm.setDouble(4,amount);
        prestm.setDate(5,date);
        }
        else {
            query = "INSERT INTO Transactions (IdBankAccount, TypeOfTransaction,  "
                + "IdTypesOfRevenues, Amount, Date, Note) VALUES (?,?,?,?,?,?)";
        prestm = conn.prepareStatement(query);
         prestm.setString(1,accountNumber);
        prestm.setString(2, typeOfTransaction);
        prestm.setString(3, typeOfRevenues);
        prestm.setDouble(4,amount);
        prestm.setDate(5,date);
        prestm.setString(6,note);
        }
        prestm.executeUpdate();

    } catch (SQLException e) {
        e.getMessage(); 
    }
}
     //metody pro odstranění řádku z tabulky
     //jako parametr mají hodnotu těch sloupců, skrz které se dá identifikovat
     //konkrétní řádek s hodnotami, které chce uživatel odstranit
     public static void DeleteTypeOfExpenses(String delete){
         try{
             
             query = "DELETE FROM TypesOfExpenses WHERE Name = ?";
             
             Connection conn = DriverManager.getConnection(url);
             PreparedStatement prestm = conn.prepareStatement(query);
             
             prestm.setString(1,delete);
             prestm.executeUpdate();
         }
         catch(SQLException e){
             e.getMessage();
         }
     }
     
     public static void DeleteTypeOfRevenues(String delete){
         try{
             
             query = "DELETE FROM TypesOfRevenues WHERE Name = ?";
             
             Connection conn = DriverManager.getConnection(url);
             PreparedStatement prestm = conn.prepareStatement(query);
             prestm.setString(1,delete);
             prestm.executeUpdate();
             
         }
         catch(SQLException e){
             e.getMessage();
         }
     }
     
     public static void DeleteBankAccount(String delete){
         try{
             
             query = "DELETE FROM BankAccount WHERE AccountNumber = ?";
             
             Connection conn = DriverManager.getConnection(url);
             PreparedStatement prestm = conn.prepareStatement(query);

             prestm.setString(1,delete);
             prestm.executeUpdate();
             
             
         }
         catch(SQLException e){
             e.getMessage();
         }
     }
     
     public static void DeleteTransaction(String accountNumber, String typeOfTransaction, 
             String typeOfExpenses, String typeOfRevenues, double amount, Date date){
         try{
             //nelze odstranit null hodnotu
             //typy přijmů a výdajů mohou být null na základě typu transakce
             query = "DELETE FROM Transactions WHERE IdBankAccount =? AND TypeOfTransaction = ?"
                     + "AND (IdTypesOfExpenses = ? OR IdTypesOfExpenses IS NULL)"
                     + " AND (IdTypesOfRevenues = ? OR IdTypesOfRevenues IS NULL) "
                     + "AND Amount = ? AND Date = ?";
             Connection conn = DriverManager.getConnection(url);
             PreparedStatement prestm = conn.prepareStatement(query);
             prestm.setString(1,accountNumber);
             prestm.setString(2,typeOfTransaction);
             prestm.setString(3,typeOfExpenses);
             prestm.setString(4,typeOfRevenues);
             prestm.setDouble(5, amount);
             prestm.setDate(6, date);
             
             prestm.executeUpdate();
             
         }
         catch(SQLException e){
             e.getMessage();
         }
         
     }
     
   
     
     
     
}
