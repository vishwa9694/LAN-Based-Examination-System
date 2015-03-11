import java.net.*;
import java.io.*;
import java.sql.*;

public class ServerOnline   {
   public static void main(String args[])   {
      try   {

         DriverManager.registerDriver(new sun.jdbc.odbc.JdbcOdbcDriver());

      }catch(Exception e)   {
         System.out.println("Error occured : "+e.getMessage());
      }
      new ServerClass();
   }
}

class ServerClass implements Runnable   {
 
   Thread t;
   ServerSocket echoServer = null; 
   Socket clientSocket = null;

   ServerClass()   {
      try   {

         echoServer = new ServerSocket(1111);

      }catch(IOException e)   {
         System.out.println("Could not start server." + e );
         System.exit(1);
      }

      t = new Thread(this);
      System.out.println("Server started !!!");
      t.start();
   }

   public void run()   {
      try  {

         while(true)   {
            clientSocket = echoServer.accept();
            connection con = new connection(clientSocket);   
         }

      }catch(IOException e)   {
         System.err.println("Not listening." + e);
         System.exit(1);
      }
   }
}

class connection implements Runnable  {

   Thread t;
   String tableName = "";	//this gets the name of the tablename from where the question and options are to be retrieved
   String requestQuestion  = "";

   BufferedReader is = null;
   PrintStream os = null;
   Socket clientSocket = null;

   Connection conn;
   Statement stmt;

   String chapterSelected = "",pass;

   public connection(Socket clientSocket)   {
      this.clientSocket = clientSocket;

      try   {
         is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         os = new PrintStream(clientSocket.getOutputStream());

      }catch(IOException e)   {
         try   {
            clientSocket.close();
         }catch(Exception ex)   {}
         System.err.println("Unable to setup stream "+e);
         return;
      }
      t = new Thread(this,"");
      t.start();      
   }   
   
   public void run()   {

      try   {
         conn = DriverManager.getConnection("jdbc:odbc:OnlineExams"); 
         stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

      }catch(Exception e)   {
         System.out.println("Error occured : "+e.getMessage());
      }

      try   {

            pass = is.readLine();

            if(pass.equals("1"))   {

               tableName = is.readLine();		//subjectSelected
               tableName += is.readLine();		//classSelected

               ResultSet rset = stmt.executeQuery("Select * from "+tableName);
               rset.last();
               os.println(rset.getString(1));	//lastSNo

               rset = stmt.executeQuery("Select * from "+tableName);
               while(rset.next())   {
                  os.println(rset.getString(2));	//chapterNos
                  os.println(rset.getString(3));	//chapterNames
               }
             }

             else if(pass.equals("2"))   {
                chapterSelected = is.readLine();	//chapterSelected

                requestQuestion = (String)is.readLine();

                ResultSet rset = stmt.executeQuery("Select * from "+chapterSelected);
                rset.last();
                os.println(rset.getString(1));

                rset = stmt.executeQuery("Select * from "+chapterSelected + " where QNo = "+requestQuestion);
                if(rset.next())   {
                   os.println(rset.getString(2));		//send question
                   os.println(rset.getString(3));		//send Answer
                   os.println(rset.getString(4));		//send hint
                }
             }

      }catch(Exception e)   {
         System.out.println("Error occured while sending/retrieving data: "+e);
      }finally   {
         try   {
            clientSocket.close();
            is.close();
            os.close();
            stmt.close();
            conn.close();

         }catch(Exception e)   {}
      }
   }
}