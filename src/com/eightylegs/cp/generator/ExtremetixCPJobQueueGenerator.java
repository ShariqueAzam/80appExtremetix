package com.eightylegs.cp.generator;

import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.eightylegs.common.domain.crawlpackagegenerator.CrawlPackageGeneratorJob;
import com.eightylegs.common.domain.crawlpackagegenerator.JobQueueGeneratorData;
import com.eightylegs.cp.xml.util.CrawlPackageRegexUtility;

public class ExtremetixCPJobQueueGenerator extends CrawlPackageJobQueueGenerator 
{
	private static final String LOCATIONS_URL ="http://www.extremetix.com/events.html";
	private static final String PROJECT_NAME = "Extremetix";
	private static final String CYCLE = "CYCLE";
	private static final String DATE = "DATE";
	private static final String JOB_NAME_PATTERN = PROJECT_NAME + " - " + CYCLE+ " - " + DATE;
	
	public static void main(String[] args) 
	{
		JobQueueGeneratorData data = new JobQueueGeneratorData();
		data.setCurrentCrawlCycleNumber(0);
		new ExtremetixCPJobQueueGenerator().printTestData(data);
	}
		
	public List<CrawlPackageGeneratorJob> getCrawlPackageGeneratorJobs(JobQueueGeneratorData data) throws Exception 
	{
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy.MM.dd");
		String JQGDate = formatter.format(date);
		String locationsPageContent = this.crawlURL(new URL(LOCATIONS_URL));
		if (locationsPageContent == null || locationsPageContent.isEmpty()) {
			throw new Exception("ERROR: LOCATIONS_URL may be invalid!");
		}
		List<CrawlPackageGeneratorJob> jobs = new ArrayList<CrawlPackageGeneratorJob>();
		String categories = CrawlPackageRegexUtility.getLastGroupForFirstMatch("<!--// BODY BELOW //-->[\\s\\S]*?<!--// eof BODY BELOW //-->", locationsPageContent, false);
		if(categories != null)
		{
			String[] category = CrawlPackageRegexUtility.getLastGroupForAllMatches("<br /><a href=\"([\\s\\S]*?)\"", categories, false);
			if(category != null && category.length > 0)
			{
				List<String> urls = new ArrayList<String>();
				for(String items : category)
				{
					urls.add(items);
				}
				jobs.add(new CrawlPackageGeneratorJob(JOB_NAME_PATTERN.replace ("CYCLE",  String.valueOf(data.getCurrentCrawlCycleNumber())).replace(DATE, JQGDate), urls));
			}
		}	
		return jobs;	
	}
	@Override
	public byte[] getCrawlPackageGeneratorState() 
	{
//		Extremetix doesn't need to keep track of CPG state
		return null; 
	}
}
