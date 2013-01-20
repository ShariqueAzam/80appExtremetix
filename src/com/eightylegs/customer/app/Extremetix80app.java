package com.eightylegs.customer.app;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import com.eightylegs.app.parselinks.ParselinksHelper;
import com.eightylegs.cp.link.CrawlPackageLinkSet;
import com.eightylegs.cp.xml.config.DefaultRegexes;
import com.eightylegs.cp.xml.domain.attribute.DocumentAttribute;
import com.eightylegs.cp.xml.domain.attribute.EventAttribute;
import com.eightylegs.cp.xml.domain.attribute.LocationAttribute;
import com.eightylegs.cp.xml.domain.attribute.TicketAttribute;
import com.eightylegs.cp.xml.domain.tag.common.EventTag;
import com.eightylegs.cp.xml.domain.tag.common.TicketTag;
import com.eightylegs.cp.xml.domain.tag.common.TicketsTag;
import com.eightylegs.cp.xml.domain.tag.secondlevel.LocationTag;
import com.eightylegs.cp.xml.domain.tag.toplevel.DocumentTag;
import com.eightylegs.cp.xml.util.CrawlPackageUtils;
import com.eightylegs.cp.xml.util.CrawlPackageXMLDocument;
import com.eightylegs.customer.Default80AppPropertyKeys;
import com.eightylegs.customer.I80App;

public class Extremetix80app implements I80App 
{
	private static final String BASE_URL = "https://tix.extremetix.com/Online/";
	private static final String CHARSET = "UTF-8";
	
	public static String region = "";
	
	public String getVersion()
	{
		return "80App_1.2";  
	}
	
	public void initialize(Properties properties, byte[] data)
	{ 
		
	}
	
	public Collection<String> parseLinks(byte[] documentContent, String url, Map<String, String> headers, Map<Default80AppPropertyKeys, Object> default80AppProperties, String statusCodeLine)
	{
		try 
		{
			//String documentString = new String(documentContent , CHARSET);
			CrawlPackageLinkSet linkSet = new CrawlPackageLinkSet(new URL(BASE_URL), documentContent, CHARSET);
			HashSet<String> redirectLink = ParselinksHelper.checkForHTTP30XRedirects(url, headers, statusCodeLine,null);
			if (redirectLink != null)
				return redirectLink;
			else
			{
				if (url.contains("siteID") && !url.contains("Event_new.jsp")) {
					linkSet.addLinksForRegex("SRC='([\\s\\S]*?)'");	
				}
				else if(url.contains("Event_new.jsp?siteID=")){
					linkSet.addLinksForRegex("onclick = \"javascript:checkDelivery\\('([\\s\\S]*?)'");	
					}
			}								
			return linkSet.getOutlinkSet();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}		
		return new ArrayList<String>();
	}
	
	public byte[] processDocument(byte[] documentContent, String url, Map<String, String> headers, Map<Default80AppPropertyKeys, Object> default80AppProperties, String statusCodeLine)
	{	
		if (!statusCodeLine.contains("200"))
			return null;
		try
		{
			if(url.contains("venueID=")){											
				DocumentTag documentTag = new DocumentTag();
				documentTag.setAttributeRegex(DocumentAttribute.title, DefaultRegexes.DOCUMENT_TITLE);
				documentTag.setAttributeRegex(DocumentAttribute.meta_description, DefaultRegexes.META_DESCRIPTION);
				documentTag.setAttributeRegex(DocumentAttribute.meta_keywords, DefaultRegexes.META_KEYWORDS);
				documentTag.setAttributeValue(DocumentAttribute.date_crawled, CrawlPackageUtils.getCurrentDate());
				documentTag.setAttributeValue(DocumentAttribute.v, CrawlPackageUtils.getLatestVersion());
				
				Map<String, String> priceReplacementValues = new HashMap<String, String>();
				priceReplacementValues.put("\\$", "USD");
				
				Map<String, String> spaceReplacementValues = new HashMap<String, String>();
				spaceReplacementValues.put("\\&nbsp;", "");
				
				EventTag eventTag = new EventTag();
				eventTag.setAttributeRegex(EventAttribute.name,"<!-- Event NAME -->[\\s\\S]*?\">(.*?)</span>");
				eventTag.setAttributeRegexList(EventAttribute.date, new String[] {"<!-- Event DETAILS/DESCRIPTION -->([\\s\\S]*?)</span>"},new SimpleDateFormat[] {new SimpleDateFormat("MMM dd, yyyy")});
				
				TicketsTag ticketsTag = new TicketsTag("<!-- TICKETS AVAILABLE -Cycle through and pull this info-->[\\s\\S]*?<!-- TICKETS LOOP END -Cycle through and pull ticketing info-->");
				TicketTag ticketTag = new TicketTag();
				ticketTag.setAttributeRegexList(TicketAttribute.ticket_type,new String[]{"ALT=\"View Package Details\"></b>(.*?)<span","<td align=left><span class=\"text-blu12\"><b>[\\s\\S]*?<br>"},spaceReplacementValues);
				ticketTag.setAttributeRegex(TicketAttribute.price, "<td align=left width=40 class=\"text-blu10\" valign=top>(.*?)</b>",priceReplacementValues);
				ticketsTag.addChildTagsMatchingRegex(ticketTag, "<tr align=left>[\\s\\S]*?<TD valign=top align=center>");////<tr align=left>[\\s\\S]*?</tr>
				eventTag.addChildTag(ticketsTag);
	
				LocationTag locationTag = new LocationTag();
				locationTag.setAttributeRegex(LocationAttribute.name, "<b>Venue Name:</b><br>([\\s\\S]*?)<br>",spaceReplacementValues);
				locationTag.setAttributeRegex(LocationAttribute.address, "<b>Address:</b><br>([\\s\\S]*?)<br>",spaceReplacementValues);
				locationTag.setAttributeRegex(LocationAttribute.locality, "<b>Address:</b><br>[\\s\\S]*?<br>([\\s\\S]*?),",spaceReplacementValues);
				locationTag.setAttributeRegex(LocationAttribute.region, "<b>Address:</b><br>[\\s\\S]*?<br>[\\s\\S]*?,([\\s\\S]*?)\\d+");
				
				eventTag.addChildTag(locationTag);
				
				documentTag.addChildTag(eventTag);
				return CrawlPackageXMLDocument.generateXMLAsEncodedByteArray(documentContent, url, documentTag, CHARSET);
			} 
		}			
		catch ( Exception e )
		{
			e.printStackTrace(); 
		}
		return null;
	}
}