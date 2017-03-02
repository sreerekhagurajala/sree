package com.metricstream.systemi.nlp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metricstream.systemi.search.constants.SearchBuilderConstants;
import com.metricstream.systemi.services.UniversalSearchService;
import com.metricstream.systemi.services.query.BoolQuery;
import com.metricstream.systemi.services.query.SearchQueryBuilder;
import com.metricstream.systemi.services.query.TextQuery;
import com.metricstream.systemi.services.search.HitField;
import com.metricstream.systemi.services.search.SearchHit;
import com.metricstream.systemi.services.search.SearchRequest;
import com.metricstream.systemi.services.search.SearchResponse;


public class NlpIndexerDetectCategory {
	
	private static final Logger logger = LoggerFactory.getLogger(NlpIndexerDetectCategory.class);
	
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");

	public static void index() throws IOException {
		Connection con = null;
		Statement stmt = null;
		//TransportClient client = getTransportClient();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@172.27.141.5:34422:orcl", "METRICSTREAM",
					"password");
			stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select n.bug_id,n.component,n.bug_status,n.resolution,n.nlp_category,n.nlp_sub_category,n.short_desc,b.cf_rca_details From nlpcategories n, bugs b where n.bug_id = b.bug_id");

			while (rs.next()) {
				Map<String, Object> doc = new HashMap<>();
				String bugId = rs.getString("bug_id");
				doc.put("bug_id", bugId);
				doc.put("component", rs.getString("component"));
				doc.put("nlp_category", rs.getString("nlp_category"));
				doc.put("short_desc", rs.getString("short_desc"));
				doc.put("nlp_sub_category", rs.getString("nlp_sub_category"));
				doc.put("resolution", rs.getString("resolution"));
				doc.put("cf_rca_details", rs.getString("cf_rca_details"));
				/*Timestamp timestamp = rs.getTimestamp("creation_ts");
				if (timestamp != null) {
					doc.put("creation_date", DATE_FORMAT.format(timestamp));
				}*/
				UniversalSearchService.index("application", "trakr", bugId, doc);
				//index(client, "application", "trakr", bugId, doc);
			}

			con.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				con.close();
				stmt.close();
			} catch (SQLException e) {	
				e.printStackTrace();
			}
			//client.close();
		}

	}
	
	public static String[][] search(String shortDesc, String nlpCategory, String nlpSubCategory) throws IOException {
		logger.debug("shortdesc {} nlpcategory {} nlpsubcategory {}", shortDesc, nlpCategory, nlpSubCategory);
		String[][] result = new String[10][6];
		
		TextQuery categoryQuery = SearchQueryBuilder.text().field("nlp_category").contains(nlpCategory);
		TextQuery subCategoryQuery = SearchQueryBuilder.text().field("nlp_sub_category").contains(nlpSubCategory);
		TextQuery shortDescQuery = SearchQueryBuilder.text().field("short_desc").contains(shortDesc);
	BoolQuery finalQuery = SearchQueryBuilder.bool().and(categoryQuery, subCategoryQuery, shortDescQuery);
		
		SearchRequest req = new SearchRequest();
		req.advancedQuery(finalQuery)
		   .indices("application")
		   .types("trakr")
		   .fields("bug_id", "short_desc", "nlp_category", "nlp_sub_category","resolution","cf_rca_details")
		   .fetch(10);
			
		SearchResponse response = UniversalSearchService.search(req, false);
		if (response != null) {
			logger.debug("Total hits {}", response.total());
		}
		
		List<SearchHit> hits = response.getHits();
		if (hits != null) {
			int x = 0;
			for (SearchHit hit : hits) {
				List<HitField> hitFields = hit.hitFields();
				if (hitFields != null) {
					for (HitField hitField : hitFields) {
						String value = hitField.value().toString();
						switch (hitField.name()) {
						case "bug_id":
							value = "<a href=\"http://trakr/show_bug.cgi?id="+value+"\"  target=\"_blank\">"+value+"</a>";
							result[x][0] = value;
							logger.debug("Hit {} Field: bug_id Value: {}", x, value);
							break;
						case "short_desc":
							result[x][1] = value;
							logger.debug("Hit {} Field: short_desc Value: {}", x, value);
							break;
						case "nlp_category":
							result[x][2] = value;
							logger.debug("Hit {} Field: nlp_category Value: {}", x, value);
							break;
						case "nlp_sub_category":
							result[x][3] = value;
							logger.debug("Hit {} Field: nlp_sub_category Value: {}", x, value);
							break;
						case "resolution":
							result[x][4] = value;
							logger.debug("Hit {} Field: resolution Value: {}", x, value);
							break;
						case "cf_rca_details":
							result[x][5] = value;
							logger.debug("Hit {} Field: cf_rca_details Value: {}", x, value);
							break;
						default:
							break;
						}
					}
				}
				++x;
			}
		}
		
		logger.debug("result", result);
		return result;
	}
	
	/*public static String index(Client client, String index, String type, String id, Map<String, ?> source)
		throws Exception {
		IndexRequestBuilder requestBuilder = client.prepareIndex(index, type, id);
		IndexResponse response = requestBuilder.setSource(source)
				.execute()
				.actionGet();
		String docId = response.getId();
		return docId;
	}
	
	private static TransportClient getTransportClient() {
		try {
			Settings.Builder builder = Settings.builder();
			builder.put("cluster.name", "metricstream");
			Settings settings = builder.build();
			TransportClient transportClient = TransportClient.builder().settings(settings).build()
					 .addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("msi-l1763", 9300)));
			TransportClient transportClient = TransportClient.builder().settings(settings).build()
			        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
			return transportClient;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
}
