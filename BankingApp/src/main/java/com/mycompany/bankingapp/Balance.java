//třída metod pro součet dílčích hodnot z tabulky transakce z databáze
package com.mycompany.bankingapp;
import java.sql.*;
import static com.mycompany.bankingapp.RevenuesVSExpenses.*;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class Balance {
//metoda, která vrátí součet všech příchozích transakcí
    public static double sumAllRevenues(){
        //proměná do které se uloží výsledek
        double result =0;
        try{
            //všechny příchozí transakce za dané období
        ResultSet rs = getMonthlyRevenues();
        //pokud nějaké takové jsou
        if(rs != null){
            //iteruje všechny řádky rs
        while(rs.next()){
            //a sečte jejich sloupec Amount
            result += rs.getDouble("Amount");
        }
        //vrátí výsledek
        return result;
        }
        else{
    //vrátí 0
            return result;
        }
        }
        catch(SQLException e){
            e.getMessage();
            return 0;
        }
    }
    
    //secist vsechny naklady
    public static double sumAllExpenses(){
        double result =0;
        try{
        ResultSet rs = getMonthlyExpenses();
        while(rs.next()){
            result += rs.getDouble("Amount");
        }
        
        return result;
        }
        catch(SQLException e){
            e.getMessage();
            return 0;
        }
    }
    //metoda pro součet provedených příchozích transakcí jednotlivých typů příjmů
    //vrátí Map
    //string nazev prijmu, Double celkova hodnota za období 
    public static Map<String,Double> sumTypesOfRevenues(){
        Map<String,Double> result = new HashMap();
        ResultSet rs = getMonthlyRevenues();
        try{
            if(rs != null){
            while(rs.next()){
                //pokud je typ příjmu v rs stejný jako ten co je již jako klíč v result
                //tak sečte jejich částky a změní konečnou value v Map result

                //klíč v result bude název typu příjmu z rs 
                String key = rs.getString("IdTypesOfRevenues");
                //value v result bude částka z rs
                Double amount = rs.getDouble("Amount");

                // pokud je klíč již v result
                if (result.containsKey(key)) {
                    // sečte částky
                    Double value = result.get(key) + amount;
                    //dosadí součet jako novou value pro daný klíč v result
                    result.replace(key, value);
                } else {
                    //pokud v result daný příjem není
                    //přidá se do něj nový klíč a patřičná částka
                    result.put(key, amount);
                }     
            }
            
            return result;
            }
            else
            {
                //vrátí prázdný Map
                return result;
            }
        }
        catch(SQLException e){
            e.getMessage();
            return null;
        }
    }
    //secicte částky jdenotlivých typů výdajů
    public static Map<String,Double> sumTypesOfExpenses(){
        Map<String,Double> result = new HashMap();
        ResultSet rs = getMonthlyExpenses();
        try{
            if(rs != null){
            while(rs.next()){
                
                String key = rs.getString("IdTypesOfExpenses");
                Double value = rs.getDouble("Amount");
                if(result.containsKey(key)){
                    value += result.get(key);
                    result.replace(key,value);
                }
                else{
                    result.put(key, value);
                }
                        
            }
            return result;
            }
            else{
                return result;
            }
        }
        catch(SQLException e){
            e.getMessage();
            return null;
        }
    }
    //metoda pro součet všech zbytných nákladů
    //sečte zbytné náklay ze seznamu řádků z monthlyZbytne
    public static double sumZbytne(){
        double result =0;
        //seznam řádků tabulky
        //Map -řádek-název výdaje, částka
        List<Map<String,Double>> rs = monthlyZbytne();
        //iteruje seznam po řádcích
          for(Map<String,Double> row : rs){
              //iteruje hashovou tabulku po hodnotách
            for(Double values : row.values()){
                //sečte hodnoty z hashobé tabulky
            result += values;
            }
        }
        //vrátí konečný součet
        return result;
        
        
    }
    //sečte nezbytne výdaje
    public static double sumNezbytne(){
        double result = 0;
        List<Map<String,Double>> rs = monthlyNezbytne();
        for(Map<String,Double> row : rs){
            for(Double value : row.values()){
                result += value;
            }
        }
        
        return result;
    }
    //odecte vsechny prijmy od vsech výdajů
    public static double sumBalance(){
        double rev = sumAllRevenues();
        double ex = sumAllExpenses();
        double result = rev-ex;
        return result;
    }
    
    
}
