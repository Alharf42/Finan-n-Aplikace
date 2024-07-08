//třída pro vytvoříní a změnu schématu databáze Banking
package com.mycompany.bankingapp;

import java.sql.*;

public class CreateDbSchema {
    public static void schemaCreate() throws Exception{
        
        //register driver-děje se automaticky
       
       //System.out.println("classpath "+System.getProperty("java.class.path"));
       Class.forName("org.apache.derby.client.ClientAutoloadedDriver");

       //create=true
        //gettin connection to the database
        String url = "jdbc:derby://localhost:1527/banking;user=banking;password=banking";
        Connection conn = DriverManager.getConnection(url);
        //creating sql statement object
        Statement stmt = conn.createStatement();
        //execute statement query
        String query = "CREATE TABLE BankAccount( "
                + "Id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                + "AccountNumber VARCHAR(50) NOT NULL UNIQUE, "//unique
                + "Name VARCHAR(50), "
                + "Note VARCHAR(255))";
        String query2 = "CREATE TABLE TypesOfExpenses( "
                + "Id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                + "Name VARCHAR(255) NOT NULL UNIQUE)";//unique
        String query3 = "CREATE TABLE TypesOfRevenues( "
                + "Id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                + "Name VARCHAR(255) NOT NULL UNIQUE)";//unique
        String query4 = "CREATE TABLE Transactions( "
                + "Id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                + "IdBankAccount VARCHAR(50), "//varchar not int
                + "TypeOfTransaction VARCHAR(10) CHECK (TypeOfTransaction IN ('Odchozí', 'Příchozí')), "
                + "IdTypesOfExpenses VARCHAR(255), "//varchar not int
                + "IdTypesOfRevenues VARCHAR(255), "//varchar not int
                + "Amount DECIMAL(15,2) NOT NULL, "
                + "Date DATE NOT NULL, "
                + "Note VARCHAR(255), "
                + "FOREIGN KEY (IdBankAccount) REFERENCES BankAccount(AccountNumber),"//AcountNumber not Id
                + "FOREIGN KEY (IdTypesOfExpenses) REFERENCES TypesOfExpenses(Name), "//Name  not Id
                + "FOREIGN KEY (IdTypesOfRevenues) REFERENCES TypesOfRevenues(Name), "//Name not Id
                //Tabulky typ příjmu a výnosu mají hodnotu závislou na typu transakce- odchozí/příchozí
                + "CHECK ((TypeOfTransaction = 'Odchozí' AND IdTypesOfExpenses IS NOT NULL AND IdTypesOfRevenues IS NULL) "
                + "OR (TypeOfTransaction = 'Příchozí' AND IdTypesOfRevenues IS NOT NULL AND IdTypesOfExpenses IS NULL)))";
        String query5 = "ALTER TABLE TypesOfExpenses "
                + "ADD Type VARCHAR(10) CHECK (Type IN ('Zbytné', 'Nezbytné'))";
        
//        stmt.execute(query);
//       stmt.execute(query2);
//        stmt.execute(query3);
//        stmt.execute(query4);
//        stmt.execute(query5);
        
        System.out.println("Table created");
    }
}
