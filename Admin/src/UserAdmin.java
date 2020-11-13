
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Container;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author BOUKHTACHE
 */
public class UserAdmin extends javax.swing.JFrame {
Container c;
    /**
     * Creates new form UserAdmin
     */
   public static com.itextpdf.text.Font normal=FontFactory.getFont("C:/Windows/Fonts/Arial.ttf", BaseFont.IDENTITY_H,18);
    Connection conn=null;
	    PreparedStatement pst=null;
	    ResultSet rs=null;
           private ImageIcon format=null;
           private String filename=null;
	 byte[] person_image=null;
	 int s=0;
	Timer timer;
       private String gender=null;  
       
     private void Update_Table(){
	   try{
		String sql="select id,name,age,gender,date,path,image from admininfo";
		pst=conn.prepareStatement(sql);
		rs=pst.executeQuery();
		table_admin.setModel(DbUtils.resultSetToTableModel(rs));
                table_admin.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                table_admin.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);     
		}catch(Exception ex){
			//JOptionPane.showMessageDialog(null, ex);		
		}finally{
           try{
               rs.close();
               pst.close();
           }catch(Exception ex){
               
           }
       }
   }
    public ArrayList<Admin> ListUsers(String valToSearch){
		ArrayList<Admin> userList = new ArrayList<>();
		Statement st;
		ResultSet rst;
		String txt=txt_search.getText();
    try {
		conn=JavaConnected.ConnectionDB();
		st=conn.createStatement();
		String searchQuery="SELECT * FROM admininfo where id||name||age LIKE '%"+txt+"%'";
		rst=st.executeQuery(searchQuery);
		Admin admin;
		while(rst.next()){
		 admin=new Admin(rst.getInt("id"),rst.getString("name"),rst.getInt("age"),rst.getString("gender"),rst.getString("date"),rst.getBytes("image"));
		 userList.add(admin);
		}	
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return userList;
	}
    	public void FindUser(){
		ArrayList<Admin> users=ListUsers(search .getText());
		DefaultTableModel model=new DefaultTableModel();
		model.setColumnIdentifiers(new Object[]{"id","name","age","gender","date"});
		Object[] row= new Object[5];
		for(int i=0;i<users.size();i++){
			    row[0] =users.get(i).getId();
				row[1] =users.get(i).getName();
				row[2] =users.get(i).getAge();
                                row[3] =users.get(i).getGender();
                                row[4] =users.get(i).getDate();
				model.addRow(row);
		}
		table_admin.setModel(model);   
                table_admin.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                table_admin.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);                  
	}
    public void Clock(){
		Thread tm=new Thread(){
			public void run(){
				try {
					for(;;){
					Calendar cal=new GregorianCalendar();
					int hour=cal.get(Calendar.HOUR);
					int minute=cal.get(Calendar.MINUTE);
					int second=cal.get(Calendar.SECOND);
					txt_time.setText("Time:"+hour+":"+minute+":"+second);
					sleep(1000);
				}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		tm.start();
	
	}
       public void CorrentDate(){
		Calendar cal=new GregorianCalendar();
		int month=cal.get(Calendar.MONTH);
		int year=cal.get(Calendar.YEAR);
		int day=cal.get(Calendar.DAY_OF_MONTH);
      
		txt_date.setText("Date:"+day+"/"+(month+1)+"/"+year);
		
		
	}   
  private void fillcombobox(){
        try{
          String sql="select id,name,age,gender,date,path,image from admininfo";
		pst=conn.prepareStatement(sql);
		rs=pst.executeQuery();
                while(rs.next()){
                    String name=rs.getString("name");
                    combobox1.addItem(name);
                }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }finally{
           try{
               rs.close();
               pst.close();
           }catch(Exception ex){
               
           }
       }
  }
    
    public UserAdmin() {
        initComponents();
        conn=JavaConnected.ConnectionDB();
          Update_Table(); 
          Clock();
          CorrentDate();
          fillcombobox();
          c=this.getContentPane(); 
   c.setLayout(null);
  c.add(txt_id);
    }
public void opened(){
       WindowEvent winClosingEvent=new  WindowEvent(this,WindowEvent.WINDOW_OPENED);
       Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
     }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        txt_search = new javax.swing.JTextField();
        search = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txt_id = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_name = new javax.swing.JTextField();
        txt_age = new javax.swing.JTextField();
        txt_male = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        txt_female = new javax.swing.JRadioButton();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        txt_image = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_admin = new javax.swing.JTable();
        refreche = new javax.swing.JButton();
        save = new javax.swing.JButton();
        update = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        clear = new javax.swing.JButton();
        attachImage = new javax.swing.JButton();
        nameimg = new javax.swing.JTextField();
        print = new javax.swing.JButton();
        txt_date = new javax.swing.JLabel();
        txt_time = new javax.swing.JLabel();
        report = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txt_Date = new com.toedter.calendar.JDateChooser();
        combobox1 = new javax.swing.JComboBox<>();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        printtbl1 = new javax.swing.JMenuItem();
        printtbl2 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        exit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusTraversalPolicyProvider(true);

        search.setText("بحث");
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });

        jLabel1.setText("ID:");

        txt_id.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txt_id.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        txt_id.setDragEnabled(true);

        jLabel2.setText("Name:");

        jLabel3.setText("Age:");

        buttonGroup1.add(txt_male);
        txt_male.setText("Male");
        txt_male.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_maleActionPerformed(evt);
            }
        });

        jLabel4.setText("Gender:");

        buttonGroup1.add(txt_female);
        txt_female.setText("Female");
        txt_female.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_femaleActionPerformed(evt);
            }
        });

        jDesktopPane1.setLayer(txt_image, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_image, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_image, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                .addContainerGap())
        );

        table_admin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table_admin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_adminMouseClicked(evt);
            }
        });
        table_admin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                table_adminKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                table_adminKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(table_admin);

        refreche.setText("تحديث الجدول");
        refreche.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refrecheActionPerformed(evt);
            }
        });

        save.setText("حفظ");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        update.setText("تحديث");
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });

        delete.setText("حذف");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        clear.setText("تفريغ");
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });

        attachImage.setText("الصورة");
        attachImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attachImageActionPerformed(evt);
            }
        });

        print.setText("طباعة");
        print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printActionPerformed(evt);
            }
        });

        report.setText("IReport");
        report.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportActionPerformed(evt);
            }
        });

        jLabel5.setText("Date:");

        txt_Date.setDateFormatString("yyyy MM dd");
        txt_Date.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        combobox1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                combobox1PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jMenuBar2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenuBar2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jMenu4.setText("PRINT");
        jMenu4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);

        jMenu1.setText("CHOOSE");

        printtbl1.setText("PRINT TABLE1");
        printtbl1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printtbl1ActionPerformed(evt);
            }
        });
        jMenu1.add(printtbl1);

        printtbl2.setText("PRINT TABLE2");
        printtbl2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printtbl2ActionPerformed(evt);
            }
        });
        jMenu1.add(printtbl2);

        jMenu4.add(jMenu1);
        jMenu4.add(jSeparator1);

        exit.setText("exit");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        jMenu4.add(exit);

        jMenuBar2.add(jMenu4);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(refreche)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(search)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(delete)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabel4)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(txt_male))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel2)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(txt_name))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel1)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel3)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(txt_age))))
                                            .addComponent(save)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txt_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(33, 33, 33)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(report)
                                                .addGap(62, 62, 62)
                                                .addComponent(print))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(clear)
                                                .addGap(31, 31, 31)
                                                .addComponent(nameimg)
                                                .addGap(18, 18, 18)
                                                .addComponent(attachImage))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(update)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGap(28, 28, 28)
                                                        .addComponent(txt_date, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(84, 84, 84)
                                                        .addComponent(txt_time, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(txt_female)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGap(77, 77, 77)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(combobox1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addGap(0, 0, Short.MAX_VALUE)))))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_date, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_time, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(search)
                            .addComponent(combobox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(txt_age, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(14, 14, 14)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(txt_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(txt_male)
                                    .addComponent(txt_female)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refreche)
                    .addComponent(save)
                    .addComponent(update))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(delete)
                    .addComponent(clear)
                    .addComponent(nameimg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(attachImage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(print)
                    .addComponent(report))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        FindUser();
    }//GEN-LAST:event_searchActionPerformed

    private void refrecheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refrecheActionPerformed
        Update_Table();	
    }//GEN-LAST:event_refrecheActionPerformed

    private void table_adminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_adminMouseClicked
        try{
            int row=table_admin.getSelectedRow();
            String Table_Click=(table_admin.getModel().getValueAt(row, 0)).toString();
                      byte[] img_data=(ListUsers(txt_id.getText()).get(row).getPicture())  ;
	format=new ImageIcon(new ImageIcon(img_data).getImage().getScaledInstance(txt_image.getWidth(),txt_image.getHeight() , Image.SCALE_SMOOTH));
		txt_image.setIcon(format); 
            
           /* String sql="select*from admininfo where id='"+Table_Click+"'";
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            if(rs.next()){
                byte[] img_data=rs.getBytes("image");  
	format=new ImageIcon(new ImageIcon(img_data).getImage().getScaledInstance(txt_image.getWidth(),txt_image.getHeight() , Image.SCALE_SMOOTH));
		txt_image.setIcon(format);  
            }*/
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }finally{
           try{
               rs.close();
               pst.close();
           }catch(Exception ex){
               
           }
       }
        
        try{
            int row=table_admin.getSelectedRow();
            String Table_Click=(table_admin.getModel().getValueAt(row, 0)).toString();
            String sql="select*from admininfo where id='"+Table_Click+"'";
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            SimpleDateFormat dtf = new SimpleDateFormat("yyyy MM dd");
            if(rs.next()){
                String id=rs.getString("id");
                String name=rs.getString("name");
                String age=rs.getString("age"); 
                String dt=rs.getString("date"); 
               
                 String pathage=rs.getString("path"); 
                //search date
                
                
                txt_id.setText(id);
           txt_Date.setDate(dtf.parse(dt));
                txt_name.setText(name);
                txt_age.setText(age);
                nameimg.setText(pathage);
            }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        } finally{
           try{
               rs.close();
               pst.close();
           }catch(Exception ex){
               
           }
       }
    }//GEN-LAST:event_table_adminMouseClicked

    private void table_adminKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_table_adminKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_UP||evt.getKeyCode()==KeyEvent.VK_DOWN){
            try{
            int row=table_admin.getSelectedRow();
            String Table_Click=(table_admin.getModel().getValueAt(row, 0)).toString();
                byte[] img_data=(ListUsers(txt_id.getText()).get(row).getPicture())  ;
	format=new ImageIcon(new ImageIcon(img_data).getImage().getScaledInstance(txt_image.getWidth(),txt_image.getHeight() , Image.SCALE_SMOOTH));
		txt_image.setIcon(format); 
            
        /*    String sql="select*from admininfo where id='"+Table_Click+"'";
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            if(rs.next()){
                byte[] img_data=rs.getBytes("image");  
	format=new ImageIcon(new ImageIcon(img_data).getImage().getScaledInstance(txt_image.getWidth(),txt_image.getHeight() , Image.SCALE_SMOOTH));
		txt_image.setIcon(format);  
            }*/
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }finally{
           try{
               rs.close();
               pst.close();
           }catch(Exception ex){
               
           }
       }
             try{
                 
            int row=table_admin.getSelectedRow();
            String Table_Click=(table_admin.getModel().getValueAt(row, 0)).toString();
            String sql="select*from admininfo where id='"+Table_Click+"'";
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            SimpleDateFormat dtf = new SimpleDateFormat("yyyy MM dd");
            if(rs.next()){
                String id=rs.getString("id");
                String name=rs.getString("name");
                String age=rs.getString("age");
                String pathimg= rs.getString("path");
                 String dt= rs.getString("date");
                txt_id.setText(id);
                txt_name.setText(name);
                txt_age.setText(age);
                txt_Date.setDate(dtf.parse(dt));
                nameimg.setText(pathimg);
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }   catch (ParseException ex) {
                Logger.getLogger(UserAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
           try{
               rs.close();
               pst.close();
           }catch(Exception ex){
               
           }
       }  
         }
    }//GEN-LAST:event_table_adminKeyPressed

    private void table_adminKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_table_adminKeyReleased
          if(evt.getKeyCode()==KeyEvent.VK_UP||evt.getKeyCode()==KeyEvent.VK_DOWN){
           try{
            int row=table_admin.getSelectedRow();
            String Table_Click=(table_admin.getModel().getValueAt(row, 0)).toString();
            byte[] img_data=(ListUsers(txt_id.getText()).get(row).getPicture())  ;
	format=new ImageIcon(new ImageIcon(img_data).getImage().getScaledInstance(txt_image.getWidth(),txt_image.getHeight() , Image.SCALE_SMOOTH));
		txt_image.setIcon(format); 
            
          /*  String sql="select*from admininfo where id='"+Table_Click+"'";
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            if(rs.next()){
                byte[] img_data=rs.getBytes("image");  
	format=new ImageIcon(new ImageIcon(img_data).getImage().getScaledInstance(txt_image.getWidth(),txt_image.getHeight() , Image.SCALE_SMOOTH));
		txt_image.setIcon(format);  
            }*/
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }finally{
           try{
               rs.close();
               pst.close();
           }catch(Exception ex){
               
           }
       }
             try{
                 SimpleDateFormat dtf = new SimpleDateFormat("yyyy MM dd");
            int row=table_admin.getSelectedRow();
            String Table_Click=(table_admin.getModel().getValueAt(row, 0)).toString();
            String sql="select*from admininfo where id='"+Table_Click+"'";
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            if(rs.next()){
                String id=rs.getString("id");
                String name=rs.getString("name");
                String age=rs.getString("age");
                 String pathimg= rs.getString("path");
                 String dt= rs.getString("date");
                txt_id.setText(id);
                txt_name.setText(name);
                txt_age.setText(age);
                 txt_Date.setDate(dtf.parse(dt));
                nameimg.setText(pathimg);

            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }     catch (ParseException ex) {
                  Logger.getLogger(UserAdmin.class.getName()).log(Level.SEVERE, null, ex);
              }finally{
           try{
               rs.close();
               pst.close();
           }catch(Exception ex){
               
           }
       }  
         }
    }//GEN-LAST:event_table_adminKeyReleased

    private void txt_maleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_maleActionPerformed
         gender="male";
    }//GEN-LAST:event_txt_maleActionPerformed

    private void txt_femaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_femaleActionPerformed
        gender="female";
    }//GEN-LAST:event_txt_femaleActionPerformed

    private void attachImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attachImageActionPerformed
        JFileChooser chooser=new JFileChooser();
	chooser.showOpenDialog(null);
	File f=chooser.getSelectedFile();    
	 filename=f.getAbsolutePath();    
	//txt_image.setText(filename);
        nameimg.setText(filename);
        ImageIcon imageIcon=new ImageIcon(new ImageIcon(filename).getImage().getScaledInstance(txt_image.getWidth(),txt_image.getHeight() , Image.SCALE_SMOOTH));
	txt_image.setIcon(imageIcon);
        try{
		File image=new File(filename);
                ByteArrayOutputStream bos;
           try (FileInputStream fis = new FileInputStream (image)) {
               bos = new ByteArrayOutputStream();
               byte[] buf =new byte[1024];
               for(int readNum;(readNum=fis.read(buf))!=-1;){
                   bos.write(buf,0,readNum);
               }
           }
		person_image=bos.toByteArray();	
                nameimg.setText(filename);
			}catch(IOException ex){
		JOptionPane.showMessageDialog(null, ex);
	} 
    }//GEN-LAST:event_attachImageActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        txt_id.setText("");
       txt_name.setText("");
       txt_age.setText("");
       txt_image.setText("");
       nameimg.setText("");
    }//GEN-LAST:event_clearActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
       try{
	    String sql="Insert into admininfo (id,name,age,gender,date,path,image) values (?,?,?,?,?,?,?)";
              pst=conn.prepareStatement(sql);
              pst.setString(1, txt_id.getText());
              pst.setString(2, txt_name.getText());
              pst.setString(3, txt_age.getText());
              pst.setString(4, gender);
            /*  SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
              String addDate= dateformat.format(txt_Date.getDate()) ;
              pst.setString(5,addDate ); */
               pst.setString(5,((JTextField)txt_Date.getDateEditor().getUiComponent()).getText());
              
              pst.setString(6, nameimg.getText());
                pst.setBytes(7, person_image);
              pst.execute();
	     JOptionPane.showMessageDialog(null, "save success!");
		 }catch(HeadlessException | SQLException ex){
		 JOptionPane.showMessageDialog(null,ex);		
		 }finally{
           try{
               rs.close();
               pst.close();
           }catch(Exception ex){
               
           }
       }
	 Update_Table(); 
    }//GEN-LAST:event_saveActionPerformed

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
            
        try{
	    String id=txt_id.getText();
	    String name=txt_name.getText();
            String age=txt_age.getText();
            SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
            String addDate= dateformat.format(txt_Date.getDate()) ;
           ((JTextField)txt_Date.getDateEditor().getUiComponent()).getText();
             String pathimg=nameimg.getText();
		File image=new File(filename);
                ByteArrayOutputStream bos;
             try (FileInputStream fis = new FileInputStream (image)) {
                 bos = new ByteArrayOutputStream();
                 byte[] buf =new byte[1024];
                 for(int readNum;(readNum=fis.read(buf))!=-1;){
                     bos.write(buf,0,readNum);
                 }			
             }
	    String sql="update admininfo set id='"+id+"',name='"
                +name+"',age='"+age+"',gender='"
                    +gender+"',date='"
                    +((JTextField)txt_Date.getDateEditor().getUiComponent()).getText()+
                    "',path='"+pathimg+"',image=?  where id='"+id+"'";        
		pst=conn.prepareStatement(sql);	
                 pst.setBytes(1,bos.toByteArray());
               //  pst.executeUpdate();
		pst.execute();
		JOptionPane.showMessageDialog(null, "update success!");
                    }catch(HeadlessException | IOException | SQLException ex){
                        
			JOptionPane.showMessageDialog(null,ex);
			}finally{
           try{
               rs.close();
               pst.close();
           }catch(Exception ex){
                
               JOptionPane.showMessageDialog(null,ex);
           }
       } 
    
			Update_Table();	
    }//GEN-LAST:event_updateActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
         int msg=JOptionPane.showConfirmDialog(null, "are you sur delete!","delete",JOptionPane.YES_NO_OPTION);
      if(msg==0){
        try{
                 String id=txt_id.getText();
		String sql="delete from admininfo where id=?";
		pst=conn.prepareStatement(sql);
		pst.setString(1, id);
		pst.execute();
		 JOptionPane.showMessageDialog(null, "delete success!");
			 }catch(HeadlessException | SQLException ex){
			 JOptionPane.showMessageDialog(null,ex);		
			 }finally{
           try{
               rs.close();
               pst.close();
           }catch(Exception ex){
               
           }
       }
	 Update_Table();
      } 
    }//GEN-LAST:event_deleteActionPerformed

    private void printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printActionPerformed
        String id=txt_id.getText();
        String name=txt_name.getText();
     Paragraph namex= new Paragraph(name,normal);
        
       
        String age=txt_age.getText();  
        String imgtxt=nameimg.getText();
        try{
	Document document=new Document(PageSize.A4.rotate());
        Calendar tmr=Calendar.getInstance();
        SimpleDateFormat ftm=new SimpleDateFormat("yyyy-MM-dd");
        
	PdfWriter writer ;
             writer = PdfWriter.getInstance(document, new FileOutputStream(ftm.format(tmr.getTime())+"_"+ name+"_"+id+".pdf"));
	//PdfWriter.getInstance(document, new FileOutputStream("Report.pdf"));
	document.open();
	
	//com.itextpdf.text.Image  imgn=com.itextpdf.text.Image.getInstance("C:\\Users\\Boukhtache\\Desktop\\IConTest\\cherry.png");
        com.itextpdf.text.Image  imgn=com.itextpdf.text.Image.getInstance(imgtxt);
        
	imgn.setRotationDegrees(45.0f);
	imgn.scaleAbsolute(60,30);
        imgn.setAlignment(0);
        
	document.add(new Paragraph("Image"));
	document.add(imgn);
	document.add(new Paragraph("hello younes",FontFactory.getFont(FontFactory.TIMES_BOLD,18,Font.BOLD,BaseColor.RED)));
	document.add(new Paragraph(new Date().toString(),FontFactory.getFont(FontFactory.TIMES_BOLD,10,Font.BOLD,BaseColor.BLUE)));
				
	document.add(new Paragraph("---------------------------------------------------------------------------------------------------------------"));
	PdfPTable table=new PdfPTable(4);
        table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
	PdfPCell cell=new PdfPCell(new Paragraph("yones",normal));
        
	cell.setColspan(4);
	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	cell.setBackgroundColor(BaseColor.GREEN);
	cell.setPadding(10.0f);
	table.addCell(cell);
	table.addCell("EmploeeId");
	table.addCell(id);
	table.addCell("Name");
	cell.setBackgroundColor(BaseColor.RED);
	table.addCell( new Paragraph(name,normal));
	table.addCell("Last Name");
	table.addCell(new Paragraph(name,normal));
	table.addCell("Age");
	table.addCell(age);
	document.add(table);
	com.itextpdf.text.List list=new com.itextpdf.text.List(true,20);
	list.add("paragraph 1");
	list.add("paragraph 2");
	list.add("paragraph 3");
	list.add("paragraph 4");
        
	document.add(list);
	document.close();
	JOptionPane.showMessageDialog(null, "Report saved");
	    }catch(DocumentException | HeadlessException | IOException ex){
		JOptionPane.showMessageDialog(null, ex);
	   }
    }//GEN-LAST:event_printActionPerformed

    private void reportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportActionPerformed
         try{ 
            // String Dir=System.getProperty("user.dir");
            // JasperDesign jd=JRXmlLoader.load(Dir+"test1.jrxml");
          //  JasperDesign jd=JRXmlLoader.load("C:\\Users\\BOUKHTACHE\\Documents\\NetBeansProjects\\Admin\\test1.jrxml");
            JasperDesign jd=JRXmlLoader.load("test1.jrxml");
              String sql="SELECT * FROM admininfo where id="+txt_id.getText();
              JRDesignQuery newQuery=new JRDesignQuery();
              newQuery.setText(sql);
              jd.setQuery(newQuery);
              JasperReport jr=JasperCompileManager.compileReport(jd);
              JasperPrint jp=JasperFillManager.fillReport(jr, null, conn);
              JasperViewer.viewReport(jp,false);
           }catch(JRException ex){
               JOptionPane.showMessageDialog(null, ex);
           }
    }//GEN-LAST:event_reportActionPerformed

    private void printtbl1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printtbl1ActionPerformed
       try{ 
            // String Dir=System.getProperty("user.dir");
            // JasperDesign jd=JRXmlLoader.load(Dir+"test1.jrxml");
          //  JasperDesign jd=JRXmlLoader.load("C:\\Users\\BOUKHTACHE\\Documents\\NetBeansProjects\\Admin\\test1.jrxml");
            JasperDesign jd=JRXmlLoader.load("test1.jrxml");
              String sql="SELECT * FROM admininfo where id="+txt_id.getText();
              JRDesignQuery newQuery=new JRDesignQuery();
              newQuery.setText(sql);
              jd.setQuery(newQuery);
              JasperReport jr=JasperCompileManager.compileReport(jd);
              JasperPrint jp=JasperFillManager.fillReport(jr, null, conn);
              JasperViewer.viewReport(jp,false);
           }catch(JRException ex){
               JOptionPane.showMessageDialog(null, ex);
           }
    }//GEN-LAST:event_printtbl1ActionPerformed

    private void printtbl2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printtbl2ActionPerformed
       try{ 
            // String Dir=System.getProperty("user.dir");
            // JasperDesign jd=JRXmlLoader.load(Dir+"test1.jrxml");
          //  JasperDesign jd=JRXmlLoader.load("C:\\Users\\BOUKHTACHE\\Documents\\NetBeansProjects\\Admin\\test1.jrxml");
            JasperDesign jd=JRXmlLoader.load("test2.jrxml");
              String sql="SELECT * FROM admininfo where id="+txt_id.getText();
              JRDesignQuery newQuery=new JRDesignQuery();
              newQuery.setText(sql);
              jd.setQuery(newQuery);
              JasperReport jr=JasperCompileManager.compileReport(jd);
              JasperPrint jp=JasperFillManager.fillReport(jr, null, conn);
              JasperViewer.viewReport(jp,false);
           }catch(JRException ex){
               JOptionPane.showMessageDialog(null, ex);
           }
    }//GEN-LAST:event_printtbl2ActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        System.exit(UserAdmin.EXIT_ON_CLOSE);
    }//GEN-LAST:event_exitActionPerformed

    private void combobox1PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_combobox1PopupMenuWillBecomeInvisible
       
        try{
         String cmbs=(String)combobox1.getSelectedItem();
          String sql="select id,name,age,gender,date,path,image from admininfo where name=?";
		pst=conn.prepareStatement(sql);
                pst.setString(1, cmbs);
		rs=pst.executeQuery();
                if(rs.next()){
                   String id=rs.getString("id");
                String name=rs.getString("name");
                String age=rs.getString("age");   
                 String pathage=rs.getString("path"); 
                txt_id.setText(id);
                txt_name.setText(name);
                txt_age.setText(age);
                nameimg.setText(pathage); 
                byte[] img_data=rs.getBytes("image");  
	format=new ImageIcon(new ImageIcon(img_data).getImage().getScaledInstance(txt_image.getWidth(),txt_image.getHeight() , Image.SCALE_SMOOTH));
		txt_image.setIcon(format);
                }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }finally{
           try{
               rs.close();
               pst.close();
           }catch(Exception ex){
               
           }
       }
    }//GEN-LAST:event_combobox1PopupMenuWillBecomeInvisible

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton attachImage;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton clear;
    private javax.swing.JComboBox<String> combobox1;
    private javax.swing.JButton delete;
    private javax.swing.JMenuItem exit;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTextField nameimg;
    private javax.swing.JButton print;
    private javax.swing.JMenuItem printtbl1;
    private javax.swing.JMenuItem printtbl2;
    private javax.swing.JButton refreche;
    private javax.swing.JButton report;
    private javax.swing.JButton save;
    private javax.swing.JButton search;
    private javax.swing.JTable table_admin;
    private com.toedter.calendar.JDateChooser txt_Date;
    private javax.swing.JTextField txt_age;
    private javax.swing.JLabel txt_date;
    private javax.swing.JRadioButton txt_female;
    private javax.swing.JTextField txt_id;
    private javax.swing.JLabel txt_image;
    private javax.swing.JRadioButton txt_male;
    private javax.swing.JTextField txt_name;
    private javax.swing.JTextField txt_search;
    private javax.swing.JLabel txt_time;
    private javax.swing.JButton update;
    // End of variables declaration//GEN-END:variables
}
