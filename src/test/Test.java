package test;

import com.eightylegs.cp.test.CrawlPackageTestUtility;

public class Test
{
	
	public static void main(String[] args) 
	{
		try
		{
			String url = "http://www.extremetix.com/events.html";
			//url="https://tix.extremetix.com/Online/?siteID=1736&SalesRef=WSeTx";
			//url="https://tix.extremetix.com/Online/GA_new.jsp?venueID=5584&eventID=41658&cartID=2697daea-61e3-480d-a04b-797a7c429a5a&venueTimeZone=4&View=";
			url="https://tix.extremetix.com/Online/?siteID=2211&cartID=dd06c14f-0788-4c15-b1e6-0b4e55ded37e&SalesRef=WSeTx";
			//url="https://tix.extremetix.com/Online/?siteID=3245&cartID=2697daea-61e3-480d-a04b-797a7c429a5a&SalesRef=WSeTx";
			//url="https://tix.extremetix.com/Online/Event_new.jsp?siteID=2211&cartID=dd06c14f-0788-4c15-b1e6-0b4e55ded37e";
			url="https://tix.extremetix.com/Online/GA_new.jsp?venueID=5584&eventID=41658&cartID=2697daea-61e3-480d-a04b-797a7c429a5a&venueTimeZone=4&View=";
			url="https://tix.extremetix.com/Online/?siteID=1736&SalesRef=WSeTx";
			url="https://tix.extremetix.com/Online/?siteID=3245&SalesRef=WSeTx";
			url="https://tix.extremetix.com/Online/?siteID=2211&SalesRef=WSeTx";
			url="https://tix.extremetix.com/Online/?siteID=2994&SalesRef=WSeTx";
			
			//url="https://tix.extremetix.com/Online/GA_new.jsp?venueID=5524&eventID=42175&cartID=055c0d33-f62b-4d11-959d-808c973e31d8&venueTimeZone=4&View=";
			// 
			//https://tix.extremetix.com/Online/GA_new.jsp?venueID=5524&eventID=41557&cartID=055c0d33-f62b-4d11-959d-808c973e31d8&venueTimeZone=4&View=
			/* https://tix.extremetix.com/Online/?siteID=1736&SalesRef=WSeTx
                  
                  
                  
                  https://tix.extremetix.com/Online/?siteID=2994&SalesRef=WSeTx
                  https://tix.extremetix.com/Online/?siteID=3463&SalesRef=WSeTx
                  https://tix.extremetix.com/Online/?siteID=2994&SalesRef=WSeTx
                  https://tix.extremetix.com/Online/?siteID=650&SalesRef=WSeTx
                  https://tix.extremetix.com/Online/?siteID=2994&SalesRef=WSeTx
                  https://tix.extremetix.com/Online/?siteID=3009
                  https://tix.extremetix.com/Online/?siteID=2729&SalesRef=WSeTx
                  https://tix.extremetix.com/Online/?siteID=928&SalesRef=WSeTx
                  https://tix.extremetix.com/Online/?siteID=650&SalesRef=WSeTx
                  https://tix.extremetix.com/Online/?siteID=928&SalesRef=WSeTx
			 
			  */
			
			int depthLevel = 0;

			CrawlPackageTestUtility.setDebugMode(false);
			
//			1.  Single-File Test - Use this to run a local test with a local file that you have downloaded and saved as "test.html" on your desktop
			//CrawlPackageTestUtility.testLocal("com.eightylegs.customer.app.Extremetix80app", System.getProperty("user.home")+"\\Desktop\\test.htm", depthLevel, url, null);
			
//			2.  Live Crawl Test - Use this to run a live crawl from your local machine
			CrawlPackageTestUtility.testLiveDepthFirst("com.eightylegs.customer.app.Extremetix80app", url, depthLevel, 10, true, null);
		} 
		catch ( Exception e ) 
		{
			e.printStackTrace();
		}
	}
}
