/*
 * Copyright 1999-2004 Carnegie Mellon University.
 * Portions Copyright 2004 Sun Microsystems, Inc.
 * Portions Copyright 2004 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 *
 */

package demo.sphinx.helloworld;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

/**
 * A simple HelloWorld demo showing a simple speech application built using
 * Sphinx-4. This application uses the Sphinx-4 endpointer, which automatically
 * segments incoming audio into utterances and silences.
 */

public class HelloWorld implements Runnable{

	/**
	 * Main method for running the HelloWorld demo.
	 * @throws Exception 
	 * 
	 */
	
	private Thread t;

	  public void start ()
	   {
	      if (t == null)
	      {
	         t = new Thread (this);
	         t.start ();
	      }
	   }
	  
	public void Stop(){
	      if (t != null)
	      {
	        t.interrupt();
	        t=null;
//	        recognizer.deallocate();
	      }  
//	      recognizer.deallocate();
		  
	  }
	  
	public void printOutput(String question){
		File file= new File("testing.txt");
		PrintWriter out;
		try {
			out = new PrintWriter(file);
			out.write(question);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	 private URL url;
	 private ConfigurationManager cm;
	 private Recognizer recognizer ;
	 private Microphone microphone ;
	
	public HelloWorld(){

	}
	public void AutomatedSpeechRecognition() throws Exception {
		try {
			url = HelloWorld.class.getResource("helloworld.config.xml");
			
			System.out.println("Loading Acoustic Model...");

		    cm = new ConfigurationManager(url);

			recognizer = (Recognizer) cm.lookup("recognizer");
			microphone = (Microphone) cm.lookup("microphone");


			System.out.println("New Call is starting ...");
			/* allocate the resource necessary for the recognizer */
			recognizer.allocate();
			
			/* the microphone will keep recording until the program exits */
			if (microphone.startRecording()) {
				
				System.out.println(" ﬂ·„ : „—Õ»«");
				int count = 1;
				boolean finish=false;
				while (!finish) {
					System.out
					.println("Start speaking. Press Ctrl-C to quit.\n");
					
					/*
					 * This method will return when the end of speech is
					 * reached. Note that the endpointer will determine the end
					 * of speech.
					 */
					Result result = recognizer.recognize();
					if (result != null) {
						String resultText = result.getBestFinalResultNoFiller();
						System.out.println(count++ + " :" + resultText + "\n");
						printOutput(resultText);
					} else {
						System.out.println("I can't hear what you said.\n");
					}
					finish=true;
				}
				
				System.out.println("Recognition finished");
				microphone.stopRecording();
				recognizer.deallocate();
				this.Stop();
			} else {
				System.out.println("Cannot start microphone.");
				recognizer.deallocate();
				System.exit(1);
			}
		} catch (IOException e) {
			System.err.println("Problem when loading HelloWorld: " + e);
			e.printStackTrace();
		} catch (PropertyException e) {
			System.err.println("Problem configuring HelloWorld: " + e);
			e.printStackTrace();
		} catch (InstantiationException e) {
			System.err.println("Problem creating HelloWorld: " + e);
			e.printStackTrace();
		}
	
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			AutomatedSpeechRecognition();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
