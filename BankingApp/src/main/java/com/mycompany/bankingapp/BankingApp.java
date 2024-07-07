

package com.mycompany.bankingapp;

import com.mycompany.bankingapp.CreateDbSchema.*;

import java.sql.*;


public class BankingApp {

    public static void main(String[] args) {
//       spustí funkci pro vyvrovření schématu databáze
//       try
//       {
//           //CreateDbSchema.schemaCreate();
//           
//       }
//       catch(Exception e)
//       {
//           e.printStackTrace();
//       }
      
//vytvoří a spustí instanci třídy GUI, kde je implenetováno GUI aplikace
       new GUI();
       
   
       
    }
}
