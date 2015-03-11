import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ClientOnline extends JFrame   {
      
    public ClientOnline()   {

      JTabbedPane jtp = new JTabbedPane();
      jtp.addTab("Class 10th",new Class10th());
      jtp.addTab("Class 12th",new Class12th());
      getContentPane().add(jtp);

      addWindowListener(new WindowAdapter()   {
         public void windowClosing(WindowEvent we)   {
            dispose();
         }
      });
   }

   public static void main(String args[])   {
      ClientOnline clonlie = new ClientOnline();
      clonlie.setSize(400,150);
      clonlie.setVisible(true);   
   }
}

class Class10th extends JPanel implements ActionListener {

   String subjectSelected = "";
   String classSelected = "10";
   String[] img = new String[15];
   int[] randomQnoStored = new int[15];

   public Class10th()   {

     JRadioButton physics = new JRadioButton("Physics");
     physics.addActionListener(this);
     add(physics);

     JRadioButton chemistry = new JRadioButton("Chemistry");
     chemistry.addActionListener(this);
     add(chemistry);

     JRadioButton biology = new JRadioButton("Biology");
     biology.addActionListener(this);
     add(biology);

     JRadioButton mathematics = new JRadioButton("Mathematics");
     mathematics.addActionListener(this);
     add(mathematics);

     ButtonGroup bg = new ButtonGroup();
     bg.add(physics);
     bg.add(chemistry);
     bg.add(biology);
     bg.add(mathematics);

     JButton connect = new JButton("Connect to DataBase");
     connect.addActionListener(this);
     add(connect);
   }

   public void actionPerformed(ActionEvent ae)   {

      if(ae.getActionCommand().equals("Connect to DataBase") && subjectSelected != "")   {
         try   {
            new ConnectDatabase1(classSelected,subjectSelected,1,1,0,img,img,randomQnoStored);
         }catch(Exception e)   {
            System.out.println("Could not connect to the database Server !!!"+e);
         }
      }

      else if(ae.getActionCommand().equals("Physics"))   {
         subjectSelected = "Physics";
      }

      else if(ae.getActionCommand().equals("Chemistry"))   {
         subjectSelected = "Chemistry";         
      }

      else if(ae.getActionCommand().equals("Biology"))   {
         subjectSelected = "Biology";         
      }

      else if(ae.getActionCommand().equals("Mathematics"))   {
         subjectSelected = "Mathematics";         
      }     
   }
}

class Class12th extends JPanel implements ActionListener {

   String subjectSelected = "";
   String classSelected = "12";
   String[] img = new String[15];
   int[] randomQnoStored = new int[15];

   public Class12th()   {

     JRadioButton physics = new JRadioButton("Physics");
     physics.addActionListener(this);
     add(physics);

     JRadioButton chemistry = new JRadioButton("Chemistry");
     chemistry.addActionListener(this);
     add(chemistry);

     JRadioButton biology = new JRadioButton("Biology");
     biology.addActionListener(this);
     add(biology);

     JRadioButton mathematics = new JRadioButton("Mathematics");
     mathematics.addActionListener(this);
     add(mathematics);

     ButtonGroup bg = new ButtonGroup();
     bg.add(physics);
     bg.add(chemistry);
     bg.add(biology);
     bg.add(mathematics);

     JButton connect = new JButton("Connect to DataBase");
     connect.addActionListener(this);
     add(connect);
   }

   public void actionPerformed(ActionEvent ae)   {
      if(ae.getActionCommand().equals("Connect to DataBase") && subjectSelected != "")   {
         
         try   {
            new ConnectDatabase1(classSelected,subjectSelected,1,1,0,img,img,randomQnoStored);
         }catch(Exception e)   {
            System.out.println("Could not connect to database !!!");
         }
      }

      else if(ae.getActionCommand().equals("Physics"))   {
         subjectSelected = "Physics";
      }

      else if(ae.getActionCommand().equals("Chemistry"))   {
         subjectSelected = "Chemistry";         
      }

      else if(ae.getActionCommand().equals("Biology"))   {
         subjectSelected = "Biology";         
      }

      else if(ae.getActionCommand().equals("Mathematics"))   {
         subjectSelected = "Mathematics";         
      }   
   }
}

