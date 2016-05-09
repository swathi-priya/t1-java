package com.mediamath.jterminalone;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.Form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.mediamath.jterminalone.Exceptions.ClientException;
import com.mediamath.jterminalone.Exceptions.ParseException;
import com.mediamath.jterminalone.models.Advertiser;
import com.mediamath.jterminalone.models.Agency;
import com.mediamath.jterminalone.models.AtomicCreative;
import com.mediamath.jterminalone.models.Campaign;
import com.mediamath.jterminalone.models.Concept;
import com.mediamath.jterminalone.models.Data;
import com.mediamath.jterminalone.models.FieldError;
import com.mediamath.jterminalone.models.JsonPostResponse;
import com.mediamath.jterminalone.models.JsonResponse;
import com.mediamath.jterminalone.models.Organization;
import com.mediamath.jterminalone.models.Pixel;
import com.mediamath.jterminalone.models.Status;
import com.mediamath.jterminalone.models.Strategy;
import com.mediamath.jterminalone.models.StrategyConcept;
import com.mediamath.jterminalone.models.StrategySupplySource;
import com.mediamath.jterminalone.models.T1Entity;
import com.mediamath.jterminalone.models.T1Error;
import com.mediamath.jterminalone.models.T1Property;
import com.mediamath.jterminalone.models.helper.AdvertiserHelper;
import com.mediamath.jterminalone.models.helper.AgencyHelper;
import com.mediamath.jterminalone.models.helper.AtomicCreativeHelper;
import com.mediamath.jterminalone.models.helper.CampaignHelper;
import com.mediamath.jterminalone.models.helper.ConceptHelper;
import com.mediamath.jterminalone.models.helper.OrganizationHelper;
import com.mediamath.jterminalone.models.helper.PixelHelper;
import com.mediamath.jterminalone.models.helper.StrategyConceptHelper;
import com.mediamath.jterminalone.models.helper.StrategyHelper;
import com.mediamath.jterminalone.models.helper.StrategySupplySourceHelper;
import com.mediamath.jterminalone.service.JT1Service;
import com.mediamath.jterminalone.service.TerminalOnePostService;
import com.mediamath.jterminalone.utils.Constants;
import com.mediamath.jterminalone.utils.Filters;
import com.mediamath.jterminalone.utils.T1JsonToObjParser;

/**
 * handles the authentication, session, entity
 * retrieval, creation etc.
 *
 */
public class JTerminalOne {

	/*
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(JTerminalOne.class);

	/*
	 * connection object
	 */
	public Connection connection = null;

	/*
	 * service object.
	 */
	private JT1Service jt1Service =null;
	
	private TerminalOnePostService postService = null;
	
	
	/*
	 * maintains user session
	 */
	private HashMap<String, HashMap<String, String>> user = new HashMap<String, HashMap<String, String>>();

	/*
	 * is authenticated? 
	 */
	private boolean authenticated = false;

	
	/**
	 * Default Constructor
	 */
	/*public JTerminalOne() {
		logger.info("Loading Environment - setting up connection.");
		connection = new Connection();
		jt1Service = new JT1Service();
	}*/

	/**
	 * the other constructor, tries to connect with the credentials provided.
	 * @throws ClientException 
	 * 
	 */
	public JTerminalOne(String username, String password, String api_key) throws ClientException {
		//this();
		logger.info("Loading Environment - setting up connection.");
		connection = new Connection();
		jt1Service = new JT1Service();
		
		if(api_key == null || api_key.isEmpty()) {
			logger.error("Environment does not exist");
			throw new ClientException("Please Provide Valid Enviornment");
		}
		
		if(username.isEmpty() || password.isEmpty()) {
			logger.error("Please provide valid credentials.");
			throw new ClientException("Please Provide Valid Username and Password.");
		}

		logger.info("Loading Environment - Authenticating.");
		Form form = jt1Service.getLoginFormData(username, password, api_key);

		logger.info("Loading Environment - Authenticating.");
		String url = jt1Service.constructURL(new StringBuffer("login"));
		String response = connection.post(url, form, null);
		getUserSessionInfo(response);
		postService = new TerminalOnePostService(connection, user);
		authenticated = true;
		
	}

	/**
	 * Maintains user session information.
	 * 
	 * @param response
	 */
	private void getUserSessionInfo(String response) {
		Gson gson = new Gson();
		Type stringStringMap = new TypeToken<HashMap<String, HashMap<String, String>>>() {}.getType();
		HashMap<String, HashMap<String, String>> map = gson.fromJson(response, stringStringMap);
		this.setUser(map);
	}
	
	
	/**
	 * saves the given Agency.
	 * 
	 * @param entity
	 * @throws ClientException 
	 * @throws ParseException 
	 */
	public Agency save(Agency entity) throws ClientException, ParseException {
		Agency agency = null;
		if(isAuthenticated()) {
			agency = postService.save(entity);
		}
		return agency;
	}
	
	/**
	 * saves the given Advertiser.
	 * 
	 * @param entity
	 * @throws ClientException 
	 * @throws ParseException 
	 */
	public Advertiser save(Advertiser entity) throws ClientException, ParseException {
		Advertiser advertiser = null;
		if(isAuthenticated()) {
			advertiser = postService.save(entity);
		}
		return advertiser;
	}
	
	
	/**
	 * saves the given Strategy.
	 * 
	 * @param entity
	 * @throws ClientException 
	 * @throws ParseException 
	 */
	public Strategy save(Strategy entity) throws ClientException, ParseException {
		Strategy strategy = null;
		if(isAuthenticated()) {
			strategy = postService.save(entity);
		}
		return strategy;
	}
	
	/**
	 * saves the given Strategy Concepts.
	 * 
	 * @param entity
	 * @throws ClientException 
	 * @throws ParseException 
	 */
	public StrategyConcept save(StrategyConcept entity) throws ClientException, ParseException {
		StrategyConcept strategyConcept = null;
		if(isAuthenticated()) {
			strategyConcept = postService.save(entity);
		}
		return strategyConcept;
	}
	
	/**
	 * saves the given Strategy Supply Sources.
	 * 
	 * @param entity
	 * @throws ClientException 
	 * @throws ParseException 
	 */
	public StrategySupplySource save(StrategySupplySource entity) throws ClientException, ParseException {
		StrategySupplySource strategySupplySource = null;
		if(isAuthenticated()) {
			strategySupplySource = postService.save(entity); 
		}
		return strategySupplySource;
	}
	
	/**
	 * saves the given Organization.
	 * 
	 * @param entity
	 * @throws ClientException 
	 * @throws ParseException 
	 */
	public Organization save(Organization entity) throws ClientException, ParseException {
		Organization organization = null;
		if(isAuthenticated()) {
			organization = postService.save(entity);
		}
		return organization;
	}
	
	/**
	 * saves the given Pixel.
	 * 
	 * @param entity
	 * @throws ClientException 
	 * @throws ParseException 
	 */
	public Pixel save(Pixel entity) throws ClientException, ParseException {
		Pixel pixel = null;
		if(isAuthenticated()) {
			pixel = postService.save(entity); 
		}
		return pixel;
	}
	
	/**
	 * saves the given campaign.
	 * 
	 * @param entity
	 * @return
	 * @throws ParseException
	 * @throws ClientException
	 */
	public Campaign save(Campaign entity) throws ParseException, ClientException {
		Campaign campaign = null;
		if(isAuthenticated()) {
			campaign = postService.save(entity); 
		}
		return campaign;
	}
	
	/**
	 * saves concepts
	 * 
	 * @param entity
	 * @return
	 * @throws ParseException
	 * @throws ClientException
	 */
	public Concept save(Concept entity) throws ParseException, ClientException {
		Concept concept = null;
		if(isAuthenticated()) {
			concept = postService.save(entity); 
		}
		return concept;
	}
	
	/**
	 * saves Atomic Creative
	 * 
	 * @param entity
	 * @return
	 * @throws ParseException
	 * @throws ClientException
	 */
	public AtomicCreative save(AtomicCreative entity) throws ParseException, ClientException {
		AtomicCreative atomicCreative = null;
		if(isAuthenticated()) {
			atomicCreative = postService.save(entity); 
		}
		return atomicCreative;
	}
	
	/**
	 * Get.
	 * 
	 * @param query
	 * @return
	 * @throws ClientException
	 * @throws ParseException
	 */
	public JsonResponse<? extends T1Entity> get(QueryCriteria query) throws ClientException, ParseException {
		
		StringBuffer path=new StringBuffer("");
		
		String childPath = "";
		
		StringBuffer includePath = new StringBuffer("");
		
		//param collection String example "advertisers"
		if(!query.collection.equals(null)){
			path.append(query.collection);
		} else {
			throw new IllegalArgumentException("please specify: collection");
		}
		
		//param entity Int example ID 12121
		if(query.entity > 0){
			path.append("/"+String.valueOf(query.entity));
		}
		
		//param child String example: acl, permissions
		if(query.child!=null){
			childPath = jt1Service.constructChildPath(query.child);
			if(!path.toString().equalsIgnoreCase("") && !childPath.equalsIgnoreCase("")){
				path.append(childPath);
			}
		} //end of child
		
		//param limit, should be key=value pair. example organization : 123456
		if(query.limit.size()>0){
			path.append("/limit/");
			for(String s : query.limit.keySet()){
				if(!path.toString().equalsIgnoreCase("") && path.indexOf("?")!=-1){
					//TODO raise error
				}
				if(!path.toString().equalsIgnoreCase("")){
					path.append(s+"="+String.valueOf(query.limit.get(s)));
				}
			}
		}
		
		//param include
		if(query.includeConditionList != null && !query.includeConditionList.isEmpty()) {
			includePath = jt1Service.constructIncludePath(query.includeConditionList);
			
			if(!path.toString().equalsIgnoreCase("") && !includePath.toString().equalsIgnoreCase("")) {
				path.append(includePath.toString());
			}
		}//end of include
		
		//param sortby example: sortby=id
		if(query.sortBy!=null){
			if(!path.toString().equalsIgnoreCase("") && !includePath.toString().equalsIgnoreCase("") && path.indexOf("?")!=-1){
				path.append("&sort_by="+query.sortBy);
			}
			else{
				path.append("?sort_by="+query.sortBy);
			}
		}//end sortby
		
		//param pageLimit should not be > 100 example: page_limit=30 
		//and param pageOffset, should be > 0 example: page_offset=10 
		if(query.pageLimit > 100){
			throw new ClientException("Page_Limit parameter should not exceed 100");
		}
		else{
			String pagePath = "";
			pagePath = jt1Service.constructPaginationPath(query.pageLimit, query.pageOffset);
			if(!path.toString().equalsIgnoreCase("") && path.indexOf("?")!=-1){
				path.append("&"+pagePath);
			}
			else{
				path.append("?"+pagePath);
			}
		}//end pageLimit
		
		//param QUERY example 
		if(query.query!=null){
			if(!path.toString().equalsIgnoreCase("") && path.indexOf("?")!=-1){
				path.append("&q="+query.query);
			}
			else{
				path.append("?q="+query.query);
			}
		}
		
		
		// get the data from t1 servers.
		String finalPath = jt1Service.constructURL(path);
		String response = this.connection.get(finalPath, this.getUser());
		JsonResponse<? extends T1Entity> jsonResponse;
		// parse the data to entities.
		try{
			jsonResponse = parseResponse(query, response);
			jsonResponse = checkResponseEntities(jsonResponse);
		
		} catch (ParseException e) {
			
			throw new ClientException("Unable to parse the response");
		
		}
		
		// filter and validate data
		
		
		
		return jsonResponse;
	}
	
	private JsonResponse<? extends T1Entity> checkResponseEntities(JsonResponse<? extends T1Entity> jsonResponse) throws ClientException {
		
		if(jsonResponse != null) {
			StringBuffer strbuff = null;
			
			if(jsonResponse.getErrors() != null) {
				
				for(T1Error error: jsonResponse.getErrors()) {
					if(error.getMessage() != null) {
						if(strbuff == null){ 
							strbuff = new StringBuffer(error.getMessage());
						} else {
							strbuff.append(", " + error.getMessage());
						}
					}
				}
				// throw the error to client
				throw new ClientException(strbuff.toString());
			}
		}
		// else return the object
		return jsonResponse;
	}


/**
 * parses the response to objects.
 * 
 * @param query
 * @param response
 * @return
 * @throws ParseException
 */
private JsonResponse<? extends T1Entity> parseResponse(QueryCriteria query, String response) throws ParseException {
	T1JsonToObjParser parser = new T1JsonToObjParser();
	int result = parser.getJsonElementType(response);
	Type JsonResponseType = null;
	JsonResponse<? extends T1Entity> jsonresponse = null;
	
	if(query.collection != null) {
		
		if (result != 0) {
			if (result == 1) {
				JsonResponseType = Constants.getEntityType.get(query.collection);
			} else if (result == 2) {
				JsonResponseType = Constants.getListoFEntityType.get(query.collection);
			}

			jsonresponse = parser.parseJsonToObj(response, JsonResponseType);
			
		}
	}
	return jsonresponse;
}

	
	
	/** Find method alternative to query of get
	 * 
	 * @param query
	 * @return
	 * @throws ParseException 
	 * @throws ClientException 
	 */
	public JsonResponse<? extends T1Entity> find(QueryCriteria query) throws ClientException, ParseException  {
		
		StringBuffer qParamVal = new StringBuffer();
		
		if(query.queryOperator.equalsIgnoreCase(Filters.IN)){
			if(query.queryParams.getListValue()==null || (query.queryParams.getListValue()!=null && query.queryParams.getListValue().size() <1)){
				//TODO raise TypeError
			}else{
				qParamVal.append("(");
				if(query.queryParams.getListValue().get(0) instanceof String || query.queryParams.getListValue().get(0) instanceof Number){
					String prefix = "";
					for(Object obj : query.queryParams.getListValue()){
						qParamVal.append(prefix);
						qParamVal.append(String.valueOf(obj));
						prefix = ",";
					}
				}else{
					//TODO raise typeError
				}
				
				qParamVal.append(")");
			}
		}else{
			qParamVal.append(query.queryParamName);
			qParamVal.append(query.queryOperator);
			
			if(query.queryParams.getStrValue()!=null){
				qParamVal.append(query.queryParams.getStrValue());
			}
			else if(query.queryParams.getNumberValue() != null){
				qParamVal.append(query.queryParams.getNumberValue());
			}
			else if(query.queryParams.getBoolValue()==true){
				qParamVal.append(1);
			}
			else if(query.queryParams.getBoolValue()==false){
				qParamVal.append(0);
			}
	
		}
		
		query.query =  qParamVal.toString();
		
	
		
		return this.get(query);
		
	}


	/**
	 * basic authentication method.
	 * 
	 * @return boolean isauthenticated.
	 *//*
	public boolean authenticate(String username, String password, String api_key) throws ClientException {

		// TODO validate
		logger.info("Authenticating.");
		
		Form form = jt1Service.getLoginFormData(username, password, api_key);
		String url = jt1Service.constructURL(new StringBuffer("login"));
		String response = null;
		
		response = connection.post(url, form, null);
		
		getUserSessionInfo(response);
		
		postService = new TerminalOnePostService(connection, user);
		
		// TODO handle Exception
		if (response != null && !response.isEmpty())
			return true;
		else
			return false;

	}*/

	/*
	 * getters and setters
	 */
	private HashMap<String, HashMap<String, String>> getUser() {
		return user;
	}

	private void setUser(HashMap<String, HashMap<String, String>> user) {
		this.user = user;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}
	
}
