//třídy ukladájící do souboru uživatelem vybrané nastavení aplikace
package com.mycompany.bankingapp;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropertiesManager {
    //název souboru
    private static final String propertiesFile = "config.properties";
    //klíč v hashovací tabulce Properties pro nastavené sledované období
    private static final String periodKey = "period";
    //načte období ze souboru
    public static int loadPeriodFromProperties(){
        try{
            //nový input stream properties souboru
        FileInputStream i = new FileInputStream(propertiesFile);
        //nová instance Properties třídy
        Properties properties = new Properties();
        //načte soubor
        properties.load(i);
        //získá z něj hodnotu pro daný klíč
        String period = properties.getProperty(periodKey);
        //pokud není klíč prázdný vrátí jeho int hodnotu
        if(period != null){
        return Integer.parseInt(period);
        }
        }
        catch(IOException e){
            e.getMessage();
            return 30;
        }
        //jinak vrátí výchozí období
        return 30;
    }
    //uloží nové období do souboru
    public static void storePeriodToProperties(int period){
        try{
            //nový výstup ze soubru Properties
        FileOutputStream o = new FileOutputStream(propertiesFile);
        //instance Properties
        Properties properties = new Properties();
        //uloží do ní uživatelem vybrané období
        properties.setProperty(periodKey, String.valueOf(period));
        //uloží properties do souboru
        properties.store(o, null);
        }
        catch(IOException e){
            e.getMessage();
        }
    }
    
}
