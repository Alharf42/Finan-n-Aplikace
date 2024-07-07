//trřída implementující funkce pro získání dat z tabulky
package com.mycompany.bankingapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RevenuesVSExpenses {
    //day-počet sledovaných dní, který se ukládá v soubor jako Propertie
    //implemtace je ve tříde PropertiesManager
    public static int day = PropertiesManager.loadPeriodFromProperties();
    //datum začátku sledovaného období
    private static  Date startOfMonth = setMonthlyDate();
    //proměná do které se ukládá sql příkaz
    private static String query;
    private static final String url = "jdbc:derby://localhost:1527/banking;user=banking;password=banking";
    
    //metoda, která nástaví počáteční datum období určeném uživatelem-int počet dní =day
    //ziská aktuální datum ze třidy kalendář a odečte od něj počet sledovaných dní
    public static Date setMonthlyDate(){
        //instance třídy kalendář s výchozím gregoriánským kalendářem
 Calendar calendar = Calendar.getInstance();
 //výchozí hodnota day je 0
 if(day ==0){
     //add-změní podobu kalendáře
     //od počtu dní v roce odečte 30 dní, aktuálním datum v tomto kalendáři tak bude
     //o 30 dní pozadu
     //výchozí sledované období je tak 30 dní
      calendar.add(Calendar.DAY_OF_YEAR, -30);
 }
 else{
     //pokud day již bylo uživatelem změněné odečte se od počtu dní v roce
     //nový počet dní stanovený uživatelem
        calendar.add(Calendar.DAY_OF_YEAR, -day); 
 }
 //po vytvoření kalendáře se vytvoří nové datum
 //jako parametr konstruktoru se zadá aktuální datum, onoho kalendáře
        startOfMonth = new Date(calendar.getTimeInMillis()); 
        return startOfMonth;
    }
    //metoda aktualizující atribut day
    //používá se ve třídě GUI, při stisku tlačítka pro nastavení sledovaného období
    //parametr je uživatelem vybrané období
    public static void updateDay(int newDay){
        //změní day
        day = newDay;
        //uloží nový day do souboru
        PropertiesManager.storePeriodToProperties(day);
        //aktualizuje počáteční datum
        setMonthlyDate();
    }
    
    //metody získávájící ResultSet z sql SELECT příkazů z konkrétních tabulek
    //v databázi
    public static ResultSet ListOfRevenues(){
        try{
            query = "SELECT Name FROM TypesOfRevenues";
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement prestm = conn.prepareStatement(query);
            ResultSet List = prestm.executeQuery();

            return List;
        }
        catch(SQLException e){
            e.getMessage();
            return null;
        }
        
    }
    
    public static ResultSet ListOfAllExpenses(){
        try{
            query = "SELECT Name, Type FROM TypesOfExpenses";
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement prestm = conn.prepareStatement(query);
            ResultSet List = prestm.executeQuery();

            return List;
        }
        catch(SQLException e){
            e.getMessage();
            return null;
        }
    }
    public static ResultSet ListOfZbytne(){
        try{
            query = "SELECT Name FROM TypesOfExpenses WHERE Type = 'Zbytné'";
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement prestm = conn.prepareStatement(query);
            ResultSet List = prestm.executeQuery();

            return List;
        }
        catch(SQLException e){
            e.getMessage();
            return null;
        }
    }
     public static ResultSet ListOfNezbytne(){
        try{
            query = "SELECT Name FROM TypesOfExpenses WHERE Type = 'Nezbytné'";
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement prestm = conn.prepareStatement(query);
            ResultSet List = prestm.executeQuery();

            return List;
        }
        catch(SQLException e){
            e.getMessage();
            return null;
        }
    }
    
    
    public static ResultSet ListOfAllTransactions(){
        
        try{
            query = "SELECT IdBankAccount, TypeOfTransaction, IdTypesOfExpenses,"
                    + " IdTypesOfRevenues, Amount, Date, Note FROM Transactions"
                    + " ORDER BY Date DESC";
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement prestm = conn.prepareStatement(query);
            ResultSet list = prestm.executeQuery();
            
            
            return list;
            
            
        }
        catch(SQLException e){
            e.getMessage();
            return null;
        }
        
        
    }
    
    public static ResultSet ListOfBankAccounts(){
        try{
            query = "SELECT AccountNumber, Name, Note FROM BankAccount";
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement prestm = conn.prepareStatement(query);
            ResultSet list = prestm.executeQuery();
            return list;
            
        }
        catch(SQLException e){
            e.getMessage();
            return null;
        }
        
    }
    //metody pro získání dat z tabulek pouze za sledované období
    //měsíc je výchozí
public static ResultSet getMonthlyTransactions(){
    try{
       //aktuální datum
        Date currentDate = new Date(System.currentTimeMillis());
        query ="SELECT IdBankAccount, TypeOfTransaction, IdTypesOfExpenses,"
                    + " IdTypesOfRevenues, Amount, Date, Note FROM Transactions WHERE Date BETWEEN ? AND ?";
        Connection conn = DriverManager.getConnection(url);
        PreparedStatement prestm = conn.prepareStatement(query);
        prestm.setDate(1,startOfMonth);
        prestm.setDate(2,currentDate);
        ResultSet rs = prestm.executeQuery();
        
        return rs;
    }
    catch(SQLException e){
        e.getMessage();
        return null;
    }
}
public static ResultSet getMonthlyRevenues(){
    
    try{
        Date currentDate = new Date(System.currentTimeMillis());
        //typy příjmů jsou null pouze u odchozích transakcích, což jsou výdaje
        query ="SELECT IdTypesOfRevenues, Amount FROM Transactions WHERE "
                + "Date BETWEEN ? AND ? AND IdTypesOfRevenues IS NOT NULL";
        Connection conn = DriverManager.getConnection(url);
        PreparedStatement prestm = conn.prepareStatement(query);
         prestm.setDate(1,startOfMonth);
        prestm.setDate(2,currentDate);
        ResultSet monthlyRevenues = prestm.executeQuery();
        
        return monthlyRevenues;
        
    }
    catch(SQLException e){
        e.getMessage();
        return null;
    }
    
}
public static ResultSet getMonthlyExpenses(){
    
    try{
        Date currentDate = new Date(System.currentTimeMillis());
        query ="SELECT IdTypesOfExpenses, Amount FROM Transactions "
                + "WHERE Date BETWEEN ? AND ? AND IdTypesOfExpenses IS NOT NULL";
        Connection conn = DriverManager.getConnection(url);
        PreparedStatement prestm = conn.prepareStatement(query);
         prestm.setDate(1,startOfMonth);
        prestm.setDate(2,currentDate);
        ResultSet monthlyExpenses = prestm.executeQuery();
        
        return monthlyExpenses;
    }
    catch(SQLException e){
        e.getMessage();
        return null;
    }
    
}
//metody, které z ResultSet všech výdajů za dané období vrátí seznam všech zbytných,
// resp. nezbytnéch výdajů
public static List<Map<String,Double>> monthlyZbytne(){
    List<Map<String,Double>> zbytne = new ArrayList();
    try{
        //algoritmus získavající ze všech výdajů za dané období pouze zbytné výdaje
    //odchozí transakce za dané období 
    ResultSet monthlyExpenses = getMonthlyExpenses();
    //všechny druhy zbztnéch nákladů uložených v databázi v tabulce nákladů
    ResultSet listOfZbytne = ListOfZbytne();
    //pokud jsou v databází zbytné náklady
    if(listOfZbytne != null)
    {
    while(listOfZbytne.next()){
        try{
            //pokud jsou v databázi odchozí transakce
        if(monthlyExpenses != null){
        while(monthlyExpenses.next()){
            //pokud se v daném řádku monthlyExpenses nachází zbytný náklad
            //tak se vytvoří Map<> představující řádek tabulky s názvem výdaje a částkou
            //řádek se vloží do seznamu zbytne 
            if(monthlyExpenses.getString("IdTypesOfExpenses").equals(listOfZbytne.getString("Name"))){
                Map<String,Double> row = new HashMap();
                row.put(monthlyExpenses.getString("IdTypesOfExpenses"),monthlyExpenses.getDouble("Amount"));
                zbytne.add(row);
            }
            
        }
        //vrátí seznam zbytných nákladů
        return zbytne;
        }
        else{
            //vrátí prázdný seznam
            return zbytne;
        }
        }
        catch(SQLException e){
            e.getMessage();
            return null;
        }
    }
    
    //vrátí seznam zbytnych nakladu
    return zbytne;
    }
    else
    {
        //vrátí prázdný seznam
        return zbytne;
    }
    }
    catch(SQLException e){
        e.getMessage();
        return null;
    }
}

public static List<Map<String,Double>> monthlyNezbytne(){
    List<Map<String,Double>> nezbytne = new ArrayList();
    try{
     ResultSet monthlyExpenses = getMonthlyExpenses();
     ResultSet listOfNezbytne = ListOfNezbytne();
     if(listOfNezbytne != null){
     while(listOfNezbytne.next()){
         if(monthlyExpenses !=null){
         while(monthlyExpenses.next()){
             if(monthlyExpenses.getString("IdTypesOfExpenses").equals(listOfNezbytne.getString("Name"))){
                 Map<String,Double> row = new HashMap();
                 row.put(monthlyExpenses.getString("IdTypesOfExpenses"), monthlyExpenses.getDouble("Amount"));
                 nezbytne.add(row);
             }
         }
     
     
        
    
        return nezbytne;
         }
        else{
             return nezbytne;
             }
     }
     return nezbytne;
     }
     else{
         return nezbytne;
     }
    }
    catch(SQLException e){
        e.printStackTrace();
        System.out.println("sqlError");
        return null;
    }
    

    
}
    
}
