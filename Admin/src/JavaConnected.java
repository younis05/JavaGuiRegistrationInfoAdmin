/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author BOUKHTACHE
 */
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
public class JavaConnected {
     public static Connection ConnectionDB(){
        try{
		  Connection conn=null;
		  Class.forName("org.sqlite.JDBC");
                  
                 String Dir=System.getProperty("user.dir");
		 conn=DriverManager.getConnection("jdbc:sqlite:"+Dir+"\\admin.sqlite");
                 //conn=DriverManager.getConnection("jdbc:sqlite:admin.sqlite");
		//conn=DriverManager.getConnection("jdbc:sqlite:C:\\Users\\BOUKHTACHE\\Documents\\NetBeansProjects\\Admin\\admin.sqlite");
		 //JOptionPane.showMessageDialog(null, "success!");
		  return conn;
	  }catch(Exception ex){
		  JOptionPane.showMessageDialog(null, "failed connect!");
		  return null;
	  }
    }
   
}
