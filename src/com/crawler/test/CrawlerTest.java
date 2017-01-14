package com.crawler.test;

import junit.framework.TestCase;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.crawler.main.Crawler;


public class CrawlerTest extends TestCase {

	/**
	 * Test of main method, of class Crawler.
	 */
	public void testMain() {
		String[] args = null;
		Crawler.main(args);
	}
	
	/**
	 * Test of validateUrl method, of class Crawler.
	 * @throws Exception 
	 */
	
	public void testValidateCorrectUrl() throws Exception{
		try{
			assertTrue(Crawler.validateUrl("mail.google.com"),true);
		}
		catch (Exception ex) {
			Logger.getLogger(CrawlerTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * Test of validateUrl method, of class Crawler.
	 * @throws Exception 
	 */
	
	public void testValidateIncorrectUrl() throws Exception{
		try{
			assertFalse(Crawler.validateUrl("httgoogle"), false);
			
		}catch (Exception ex) {
			Logger.getLogger(CrawlerTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	
	public void testCrawlLinksOnPage() throws Exception{
		try{
			List<String> listUrl=new ArrayList<String>();
			listUrl.add("http://mail.google.com");
			assertNotSame(Crawler.crawlLinksOnPage("<div>http://google.com</div>"), listUrl);
			
		}catch (Exception ex) {
			Logger.getLogger(CrawlerTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	
	
}


