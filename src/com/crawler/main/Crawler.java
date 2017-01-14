package com.crawler.main;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Crawler{
	
	static int temp=0;
	final static Logger unreachableLinksLogger = Logger.getLogger(Crawler.class.getName());
	private static FileHandler fh = null;

    /* Main method to make function calls to :
        1. Logger function for tracking of unreachable URLs.
        2. Validation of the URL given by the user.
        3. Crawl the web-page URL given by the user.
        4. Crawl the URLs present in the given web-page, based on the input given by the user.
    */
    
	public static void main(String []args){
		try{
			unreachableLinksConfig();
			
			Scanner reader = new Scanner(System.in);  
			System.out.println("Enter the web-page to be accessed: ");
			String pageUrl=reader.next();
			
			String validatedUrl=validateWebUrl(pageUrl);
            
            List<String> linksOnPage = new ArrayList<String>();
			linksOnPage=crawlGivenPage(new URL(validatedUrl));

			System.out.print("Enter the number of links to be accessed on "+validatedUrl+" To access ALL press enter.");
			reader.nextLine();
			String noOfLinks=reader.nextLine();

			accessLinksOnSpecifiedPage(linksOnPage,noOfLinks);

		}
		catch(MalformedURLException ex){
			System.out.println(ex.getMessage());
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}

	}

	/* This method is for logging unreachable URLs */
	
	private static void unreachableLinksConfig() {
		try {
			LogManager.getLogManager().reset();
			fh = new FileHandler("UnreachablePages.log", false);
			Logger l = Logger.getLogger("");
			fh.setFormatter(new SimpleFormatter());
			l.addHandler(fh);
			l.setLevel(Level.CONFIG);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/*This method validates the input URL given by the user.
	  Valid URLs include : www.google.com / google.com / https://www.google.com / https://google.com /
	                       http://google.com / http://www.google.com
	*/

	public static String validateWebUrl(String url) throws Exception{

		if(url.matches("^(http://|https://)(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$"))
			return url;
		
		else if(url.matches("(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$"))
			return("https://"+url);
		
		else
			throw new Exception("Invalid Format. The website should be of the format : www.google.com / https://www.google.com / google.com");
	
	}

	/* This method is used to read the content on the specified page. 
	   The content is then stored into a string */

	public static List<String> crawlGivenPage(URL url) throws Exception{

		URLConnection connection = url.openConnection();
		Scanner scanner = new Scanner(connection.getInputStream());
		scanner.useDelimiter("\\Z");
		String content = scanner.next();
		return(crawlLinksOnPage(content));
	}

	/* This method is used for extracting URLs from web page content.
	   The duplicate URL's are removed by using Set. 
	   URL's are also added to ArrayList for further crawling.
	 */

	public static List<String> crawlLinksOnPage(String content) throws Exception{

		FileWriter fileWritter=null;
		BufferedWriter bufferWritter=null;
		List<String> listOfLinks= new ArrayList<String>();
		Set<String> setOfLinks=new HashSet<String>();

		File file=new File("Pages/Page"+temp+".txt");
		temp++;

		try{
			fileWritter = new FileWriter(file.getName());   
			bufferWritter = new BufferedWriter(fileWritter);
			String[] strArray=content.split("[\"\']");
            
            //removing duplicate Links from the page.
			for(String i:strArray){
				if(i.matches("http://.*") || i.matches("https://.*"))
					setOfLinks.add(i);
			}
			
			//Adding all the URLs into a list for further crawling.
			for(String x:setOfLinks){
				listOfLinks.add(x);
				bufferWritter.write(x+"\n");
			}

		}catch ( Exception ex ) {
			System.out.println(ex.getMessage());
		}finally{
			bufferWritter.close();
		}
		return listOfLinks;
	}


	/* This method checks the input given by user. If no input is given ALL the URL's are accessed,
	  else those many number of URL's are accessed */

	public static void accessLinksOnSpecifiedPage(List<String> linksOnPage,String noOfLinks){
		try{
			if(noOfLinks.trim().isEmpty()){
				System.out.println("Empty input. All links are being accessed.");
				crawlLinksOnPage(linksOnPage);
			}else{
				int value=Integer.parseInt(noOfLinks);
				crawlLinksOnPage(linksOnPage,value);
			}
			System.out.println("Successful!");
		}
		catch(NumberFormatException e){
			System.out.println("Enter a valid number.");
		}
	}

	/* This method is used to access the links obtained from the specified page, 
	   depending on the number given by the user. */

	public static void crawlLinksOnPage(List<String> linksOnPage,int noOfLinks){

		try {
			if(noOfLinks>linksOnPage.size())
				throw new Exception("The number of links on the web-page are lesser than the number asked.");
			for(int i=0;i<noOfLinks;i++){
				crawlGivenPage(new URL(linksOnPage.get(i)));
			}
		} catch (Exception e) {
			unreachableLinksLogger.log(Level.INFO,e.getMessage());
		}

	}

	/* This method is used to access ALL the links obtained from the specified page */

	public static void crawlLinksOnPage(List<String> linksOnPage){
		for(int i=0;i<linksOnPage.size();i++){
			try {
				crawlGivenPage(new URL(linksOnPage.get(i)));
			} catch (Exception e) {
				unreachableLinksLogger.log(Level.INFO,e.getMessage());
			}
		}
	}
}

