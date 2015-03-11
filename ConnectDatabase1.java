import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import  java.io.*;
import  sun.audio.*;
import javax.sound.sampled.*;

public class ConnectDatabase1 extends Frame implements ActionListener  {

   String classSelected, subjectSelected;
   int QNo,count,noCorrectAnswers;
   String[] questionStored, correctAnsStored;
   int[] randomQnoStored;
   String[] img = new String[15];

   public ConnectDatabase1(String classSelected, String subjectSelected, int QNo, int count, int noCorrectAnswers, String[] questionStored1, String[] correctAnsStored1, int[] randomQnoStored1)   {

      this.classSelected = classSelected;
      this.subjectSelected = subjectSelected;
      this.QNo = QNo;
      this.count = count;
      this.noCorrectAnswers = noCorrectAnswers;
      questionStored = questionStored1.clone();
      correctAnsStored = correctAnsStored1.clone();
      this.randomQnoStored = randomQnoStored1.clone();

      setLayout(null);
      setSize(500,300);
      setVisible(true);
      setBackground(Color.lightGray);

      setFont(new Font("Arial",Font.BOLD,15));
      setForeground(Color.blue);
      Label onlineExam = new Label("Online Exam");
      add(onlineExam);
      onlineExam.setBounds(175,40,100,25);

      Label subject = new Label("("+subjectSelected+")");
      add(subject);
      subject.setBounds(275,40,150,25);

      Button b = new Button("Continue");
      add(b);
      b.setBounds(200,200,80,40);
      b.addActionListener(this);
   }

   public void actionPerformed(ActionEvent ae)   {
      new ConnectDatabase(classSelected,subjectSelected,1,1,0,img,img,randomQnoStored,1,"");
      dispose();
   }

   public void paint(Graphics g)   {
      g.setColor(Color.blue);
      g.drawRect(15,35,450,225);

      g.setFont(new Font("Arial",Font.PLAIN,15));
      g.setColor(Color.black);
      g.drawString("--> Student has to attemp 15 questions",20,120);
      g.drawString("--> All questions are compulsary",20,150);
      g.drawString("--> All the Best",20,180);
   }
}

class ConnectDatabase extends Frame implements ItemListener,ActionListener{
   
   String classSelected = "";
   String subjectSelected = "";

   String Question,correctAnswer, Hint, correctAns;

   Socket smtpSocket = null;
   BufferedReader is = null;
   PrintStream os = null;

   int height1, height2, height3, height4, height5, height6;
   Image question;
   String[] questionStored, correctAnsStored;

   Button submit;
   Checkbox option_1,option_2,option_3,option_4;
   CheckboxGroup cbg;

   int QNo; int count; String answerChosen;
   boolean radioButtonSelected = false;

   int noCorrectAnswers;
   int randomQnoStored[] = new int[15];
   int randomNo = 1;
   boolean found;
   String subjectClassSelected;
   String lastQNo;

   Choice chapters;
   int noOfChapters;
   String chapterSelected,chapterNos[],chapterNames[];
   int pass;	//initial value = 1
   String[] img = new String[15];

   public ConnectDatabase(String classSelected, String subjectSelected, int QNo, int count, int noCorrectAnswers, String[] questionStored1, String[] correctAnsStored1, int randomQnoStored1[],int pass,String chapterSelected)   {
      
      this.classSelected = classSelected;
      this.subjectSelected = subjectSelected;
      this.QNo = QNo;
      this.count = count;
      this.noCorrectAnswers = noCorrectAnswers;
      questionStored = questionStored1.clone();
      correctAnsStored = correctAnsStored1.clone();
      this.randomQnoStored = randomQnoStored1.clone();
      this.pass = pass;
      this.chapterSelected = chapterSelected;

      subjectClassSelected = subjectSelected + classSelected;

      try   {

//         smtpSocket = new Socket("192.168.0.107",1111);			// Remote-Client Version
		 smtpSocket = new Socket("10.100.66.155",1111);				// Standalone Version
		 
         os = new PrintStream(smtpSocket.getOutputStream());

		is = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));

      }catch(UnknownHostException e)   {
         System.out.println("Don't know about host: hostname");
      }catch(IOException e)   {
         System.out.println(" Couldn't get I/O for the connection to: hostname");
      } 

      if(smtpSocket != null && os != null)   {  

         try   {

            setLayout(null);
            setBackground(new Color(100,0,150));
            setSize(1500,700);
            setVisible(true);

            if(pass == 1)   {

               //setBackground(Color.lightGray);
			   os.println("1");		//signal for selection of chapter subject and class name.
               os.println(subjectSelected);
               os.println(classSelected);

               String lastSNo = is.readLine();               
               noOfChapters = java.lang.Integer.parseInt(lastSNo);

               chapterNos = new String[noOfChapters];   
               chapterNames = new String[noOfChapters];

      	       for(int i=0 ; i < noOfChapters ; i++)   {
                  chapterNos[i] = is.readLine();
                  chapterNames[i] = is.readLine();
               }

               chapters = new Choice();
               for(int i=0;i<noOfChapters;i++)
                  chapters.add(chapterNos[i]+"    "+chapterNames[i]);

               add(chapters);
               setFont(new Font("Arial",Font.BOLD,15));
               chapters.setBounds(150,150,200,120);
               chapters.addItemListener(this);

               try   {
                  os.close();
                  is.close();
                  smtpSocket.close();  
               }catch(Exception e)   {}
            }

            else if(pass == 2)   {

               os.println("2");
               os.println(chapterSelected);

               os.println(java.lang.Integer.toString(QNo));	//send request to the server for retrieving the random quesion no. generated

               lastQNo = is.readLine();
  
               Question = is.readLine();	//recieved Question from server
               correctAnswer = is.readLine();   //recieved correct answer of the question from the server
               Hint = is.readLine();	//recieved hint of the question from the server 

               try   {
  
                  if(correctAnswer.equals("a"))
                     correctAns = correctAnswer;
                  else if(correctAnswer.equals("b"))
                     correctAns = correctAnswer;
                  else if(correctAnswer.equals("c"))
                     correctAns = correctAnswer;
                  else if(correctAnswer.equals("d"))
                     correctAns = correctAnswer;

                  questionStored[count-1] = Question;
                  correctAnsStored[count-1] = correctAns;

                  question = Toolkit.getDefaultToolkit().getImage("Questions and Answers/" + subjectClassSelected + "/" + chapterSelected + "/" +Question + ".gif"); 
//					String path="Questions and Answers/" + subjectClassSelected + "/" + chapterSelected + "/" +Question + ".gif";
//					question = ImageIO.read(this.getClass().getResource(path));

                  MediaTracker t = new MediaTracker(this);

                  t.addImage(question,0);
                  t.waitForID(0);
                  t.waitForID(1);

                  cbg = new CheckboxGroup();

                  option_1 = new Checkbox("option_1",false,cbg);
                  add(option_1);
                  option_1.setBounds(30,410,15,15);

                  option_2 = new Checkbox("option_2",false,cbg);            
                  add(option_2);
                  option_2.setBounds(30,450,15,15);

                  option_3 = new Checkbox("option_3",false,cbg);
                  add(option_3);
                  option_3.setBounds(30,490,15,15);

                  option_4 = new Checkbox("option_4",false,cbg);
                  add(option_4);
                  option_4.setBounds(30,530,15,15);

                  submit = new Button("Submit");
                  add(submit);
                  submit.setBounds(400,600,150,50);

                  setFont(new Font("Arial",Font.BOLD,15));
                  setForeground(Color.yellow);
                  Label onlineExam = new Label("Online Exam");
                  add(onlineExam);
                  onlineExam.setBounds(350,40,100,25);

                  Label subject = new Label("("+subjectSelected+")");
                  add(subject);
                  subject.setBounds(450,40,150,25);

                  option_1.addItemListener(this);
                  option_2.addItemListener(this);
                  option_3.addItemListener(this);
                  option_4.addItemListener(this);
                  submit.addActionListener(this);

                  addWindowListener(new WindowAdapter()   {
                     public void windowClosing(WindowEvent we)   {
                        dispose();
                     }
                  });
                  repaint();

               }catch(InterruptedException e)   {}

                  randomQnoStored[count-1] = QNo; 
                  do   {  
                     randomNo = 1+(int)(Math.random()*java.lang.Integer.parseInt(lastQNo));
  
                     found = false;
                     for(int i = 0 ; i < 15 ; i++)   {

                        if(randomQnoStored[i] == randomNo)   {
                           found = true;
                           break;
                        }
                        else continue;
                     }
                  }while(found);
            }
         }catch(UnknownHostException e)   {
            System.err.println("Don't know about host: hostname");
         }catch(IOException e)   {
            System.err.println(" Couldn't get I/O for the connection to: hostname");
         }finally   { 
            try   {
               os.close();
               is.close();
               smtpSocket.close();
  
            }catch(Exception e)   {}
         }
      }
   }

  public void itemStateChanged(ItemEvent ie)   {

    if(pass == 1)   {

      try   {
//         smtpSocket = new Socket("192.168.0.107",1111);			// Remote-Client Version
		 smtpSocket = new Socket("10.100.66.155",1111);				// Standalone Version
		 
         os = new PrintStream(smtpSocket.getOutputStream());

      }catch(UnknownHostException e)   {
         System.out.println("Don't know about host: hostname");
      }catch(IOException e)   {
         System.out.println(" Couldn't get I/O for the connection to: hostname");
      } 

      if(smtpSocket != null && os != null)   {  

         for(int i=0;i<noOfChapters;i++)   {
            if(chapters.getSelectedItem().equals(chapterNos[i]+"    "+chapterNames[i]))   {
               chapterSelected = chapterNames[i];
               os.println("2");		//signal for selection of chapter event.
               os.println(chapterSelected);
               remove(chapters);
               pass = 2;
               new ConnectDatabase(classSelected,subjectSelected,1,1,0,img,img,randomQnoStored,pass,chapterSelected);
               dispose();
               break;
            }
         }

         try   {

            os.close();
            is.close();
            smtpSocket.close();
  
         }catch(Exception e)   {}
      }
    }

    else  {

      if(ie.getItem().equals("option_1") )   {
         answerChosen = "a";
      }

      else if(ie.getItem().equals("option_2") )    {
         answerChosen = "b";
      }

      else if(ie.getItem().equals("option_3") )   {
         answerChosen = "c";
      }

      else if(ie.getItem().equals("option_4"))   {
         answerChosen = "d";
      }
      radioButtonSelected = true;
    }
   }

   public void actionPerformed(ActionEvent ae)   {
      String str = ae.getActionCommand();  

      if(str.equals("Submit"))   {
         if(count <= 15 && radioButtonSelected)   {

            if(correctAnswer.equals(answerChosen))   {
               noCorrectAnswers++;
            }

            QNo = randomNo;
            count++;

            if(count <= 15)   {
               new ConnectDatabase(classSelected,subjectSelected,QNo,count,noCorrectAnswers,questionStored,correctAnsStored,randomQnoStored,2,chapterSelected);
            }

            else new Result(count,noCorrectAnswers,questionStored,correctAnsStored,subjectClassSelected,chapterSelected);

            dispose();
         }
      }
   }

   public void paint(Graphics g)   {

/*      g.setColor(Color.blue);
      g.drawRect(15,35,490,625);*/

      if(pass == 1)   {
         g.setColor(new Color(255,100,100));
         g.setFont(new Font("Arial",Font.BOLD,15));
         g.drawString("Select A Chapter ",150,70);
      }

      if(pass == 2)   {

         g.setColor(new Color(255,100,100));
         g.setFont(new Font("Arial",Font.BOLD,15));
         g.drawString("Question "+count,20,70);

         g.drawImage(question,30,80,this);
         g.drawString(""+"a",50,422);
         g.drawString(""+"b",50,462);
         g.drawString(""+"c",50,502);
         g.drawString(""+"d",50,542);

         g.setColor(new Color(255,100,100));
         g.setFont(new Font("Arial",Font.PLAIN,15));
//         g.drawString("Hint: "+Hint,100,530);
      }     
   }
}

class Result extends Frame implements ActionListener   {

   int count;
   int noCorrectAnswers; 
   String subjectClassSelected;   
   Font f;
   String[] questionStored, correctAnsStored;
   float percentage;
   Image one,two,three,four,five,six,seven;	//(one,two) = below 50, (three,four) = 50-75, (five,six,seven) = 75-100
   String chapterSelected;

   Result(int count, int noCorrectAnswers, String[] questionStored1, String[] correctAnsStored1, String subjectClassSelected,String chapterSelected)   {

      this.count = count-1;
      this.noCorrectAnswers = noCorrectAnswers;
      this.subjectClassSelected = subjectClassSelected;
      this.chapterSelected = chapterSelected;
      questionStored = questionStored1.clone();
      correctAnsStored = correctAnsStored1.clone();

      percentage = ((float)noCorrectAnswers / (float)(count-1)) * 100 ;
/*      if(percentage < 50.0)   {            
         playSoundFile(new File("work_hard.au")); 
      }

      else if(percentage >= 50.0  &&  percentage < 75.0)   {
         playSoundFile(new File("you_can_do_better.au")); 
      }

      else if(percentage >= 75.0)   {
         playSoundFile(new File("good_keep_it_up.au")); 
      }     
*/
      one = Toolkit.getDefaultToolkit().getImage("below50.gif");
      three = Toolkit.getDefaultToolkit().getImage("50,75.gif");
      five = Toolkit.getDefaultToolkit().getImage("75,,100.gif");

      setLayout(null);
      setSize(520,520);
      setVisible(true);

      Button getAnswers = new Button("Get Answers");
      add(getAnswers);
      getAnswers.setBounds(160,430,200,50);
      getAnswers.addActionListener(this);

      addWindowListener(new WindowAdapter()   {
         public void windowClosing(WindowEvent we)   {
            dispose();
         }
      });
   }

   public void playSoundFile(File file){

      try{
         //get an AudioInputStream
         AudioInputStream ais = AudioSystem.getAudioInputStream(file);

         //get the AudioFormat for the AudioInputStream
         AudioFormat audioformat = ais.getFormat();

         DataLine.Info datalineinfo = new DataLine.Info (SourceDataLine.class,audioformat);       

         SourceDataLine sourcedataline = (SourceDataLine)AudioSystem.getLine(datalineinfo);
         sourcedataline.open(audioformat);
         sourcedataline.start();
         int framesizeinbytes = audioformat.getFrameSize();
         int bufferlengthinframes   = sourcedataline.getBufferSize() / 8;
         int bufferlengthinbytes = bufferlengthinframes * framesizeinbytes;
         byte[] sounddata = new byte[bufferlengthinbytes];
         int numberofbytesread = 0;

         while ((numberofbytesread = ais.read(sounddata)) != -1){
            int numberofbytesremaining = numberofbytesread;
            sourcedataline.write(sounddata,0,numberofbytesread);
         }

     }catch(LineUnavailableException lue){
        System.err.println("LineUnavailableException: " +   lue.getMessage());

     }catch(UnsupportedAudioFileException uafe){
        System.err.println("UnsupportedAudioFileException: " +   uafe.getMessage());
     }catch(IOException ioe){
        System.err.println("IOException: " +   ioe.getMessage());
     }
   }

   public void actionPerformed(ActionEvent ae)   {

      if(ae.getActionCommand().equals("Get Answers"))   {
         QuestionsCorrectAnswers qca = new QuestionsCorrectAnswers(questionStored,correctAnsStored,subjectClassSelected,chapterSelected);
         qca.setSize(520,700);
         qca.setVisible(true);

         dispose();
      }
   }

   public void paint(Graphics g)   {


      if(percentage < 50.0)   { 
         g.setColor(Color.red);
         g.drawImage(one,0,0,this);
      }

      else if(percentage >= 50.0  &&  percentage < 75.0)   {
         g.setColor(Color.blue);
         g.drawImage(three,0,0,this);
      }

      else if(percentage >= 75.0)   {
         g.setColor(Color.red);
         g.drawImage(five,0,0,this);      
      }

      g.setColor(Color.blue);
      g.setFont(new Font("Dialog",Font.BOLD,25));
      g.drawString("Test Report",180,80);

      g.drawLine(10,100,10,420);		//left vertical 
      g.drawLine(470,100,470,420);		//right vertical

      g.drawLine(10,100,470,100);		//upper horizontal
      g.drawLine(10,420,470,420);		//lower horinontal

      g.drawLine(270,100,270,420);		// middle vertical

      int y1 = 165; int y2 = 165;
      for(int i = 0 ; i<5;i++)   {
         g.drawLine(10,y1,470,y2);
         y1 += 50; y2 += 50;
      }

      g.setFont(new Font("Dialog",Font.PLAIN,20));
      g.drawString("Number of Question(s)",50,140);
      g.drawString(java.lang.Integer.toString(count),350,140);

      g.drawString("Number of Question(s)",50,190);
      g.drawString("attempted",50,210);
      g.drawString(java.lang.Integer.toString(count),350,190);

      g.drawString("Number of Question(s)",50,240);
      g.drawString("not attempted",50,260);
      g.drawString("0",350,240);
      
      g.drawString("Total Marks allocated",50,290);
      g.drawString("15",350,290);

      g.drawString("Total Marks obtained",50,340);
      g.drawString(java.lang.Integer.toString(noCorrectAnswers),350,340);

      g.drawString("Total Percentage",50,390);
      g.drawString(""+percentage+"%",350,390);
   }
}


class QuestionsCorrectAnswers extends JFrame   {

   String[] questionStored, correctAnsStored;
   String subjectClassSelected;
   Image img;
   String chapterSelected;

   public QuestionsCorrectAnswers(String[] questionStored1 , String[] correctAnsStored1, String subjectClassSelected,String chapterSelected)   {

      questionStored = questionStored1.clone() ; 
      correctAnsStored = correctAnsStored1.clone();
      this.subjectClassSelected = subjectClassSelected;
      this.chapterSelected = chapterSelected;

      Container contentPane = getContentPane();

      ImageDisplay id =  new ImageDisplay (questionStored,correctAnsStored,subjectClassSelected,chapterSelected);
      contentPane.add(new JScrollPane(id));
   }
}

class ImageDisplay extends JComponent  {

   BufferedImage[] question = new BufferedImage[15];   

   String[] questionStored,correctAnsStored;
   int myWidth = 0, myHeight = 0, myHeight1 = 0;
   float ratio = 0;
   String chapterSelected;

   ImageDisplay(String[] questionStored1, String[] correctAnsStored1, String subjectClassSelected,String chapterSelected)   {

      questionStored = questionStored1.clone() ; 
      correctAnsStored = correctAnsStored1.clone();
      this.chapterSelected = chapterSelected;
      
     try   {
        for(int i=0;i<15;i++)   {
            question[i] = ImageIO.read(new FileInputStream(new File("Questions and Answers/" + subjectClassSelected + "/"+chapterSelected +"/"+ questionStored[i] + ".gif")));

             myWidth += question[i].getWidth();
             myHeight += question[i].getHeight();
        }
     }catch(Exception e)   {
        e.printStackTrace();
     }

     ratio = ((float) myWidth) / myHeight;
     this.setPreferredSize(new Dimension(myWidth,myHeight));
  }

  public void paintComponent( Graphics g ) {
     Graphics2D g2d = (Graphics2D) g;
     Rectangle r = this.getParent().getBounds();
     float rectangleRatio = ((float) r.width)/r.height;

     myWidth = 0; myHeight = 0;

     for(int i = 0; i<15 ; i++)   {

        if ( ratio <= rectangleRatio ) {
           myWidth += r.width;
           myHeight += Math.round( ((float) (r.width*question[i].getWidth()) ) / question[i].getHeight());
        }
        else {
          myHeight += r.height;
          myWidth += Math.round( ((float) (r.height*question[i].getWidth()) ) / question[i].getHeight() );
        }
     }

    myHeight1 = 0; 
    for(int i=0;i<15;i++)   {

       g.drawString("Question"+(i+1),0,myHeight1+10);
       g2d.drawImage(question[i],0,myHeight1+20,this);

       myHeight1 += question[i].getHeight()+20;
       g.drawString("Answer : (" + correctAnsStored[i] + ")" , 0,myHeight1-20);
    }

    this.setPreferredSize(new Dimension(0,myHeight));
    this.revalidate(); 
  }
}