package com.katalon
import java.nio.file.Path
import java.nio.file.Paths

import org.apache.commons.lang.StringEscapeUtils
import org.apache.commons.lang.StringUtils

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.RestRequestObjectBuilder
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.UrlEncodedBodyParameter
import com.kms.katalon.core.webservice.common.ServiceRequestFactory
import com.kms.katalon.util.CryptoUtil
import com.kms.katalon.core.util.internal.JsonUtil;

import groovy.json.JsonSlurper

trait IgnoreUnknownProperties {
	def propertyMissing(name, value){
		// do nothing
	}
}

class Project implements IgnoreUnknownProperties {
	Long id;
	String name;
	Long teamId;
}

class Team implements IgnoreUnknownProperties {
	Long id;
	String role;
	String name;
	Organization organization;
}

class Organization implements IgnoreUnknownProperties {
	Long id;
	String role;
	String name;
}


public class KatalonHelper {

	private static final String DEFAULT_SERVER_URL = "https://analytics.katalon.com"
	private static final String HEADER_VALUE_AUTHORIZATION_PREFIX = "Bearer ";
	private static final String HEADER_AUTHORIZATION = "Authorization";
	private static final String HEADER_AUTHORIZATION_PREFIX = "Basic ";
	private static final String OAUTH2_CLIENT_ID = "kit_uploader";
	private static final String OAUTH2_CLIENT_SECRET = "kit_uploader";


	private static final String LOGIN_PARAM_PASSWORD = "password";
	private static final String LOGIN_PARAM_USERNAME = "username";
	private static final String LOGIN_PARAM_GRANT_TYPE_NAME = "grant_type";
	private static final String LOGIN_PARAM_GRANT_TYPE_VALUE = "password";


	public static final String KATALON_HOME_ENV_NAME = "KATALON_HOME"
	public static final String KATALON_HOME_DIR = System.getenv(KATALON_HOME_ENV_NAME) != null ? System.getenv(KATALON_HOME_ENV_NAME) : System.getProperty("user.home");
	public static final String APP_USER_DIR_LOCATION = KATALON_HOME_DIR + File.separator + ".katalon";

	public static void updateInfo() {
		try{
			Path testOpsSettingsPath = Paths.get(RunConfiguration.getProjectDir(),
					'settings', 'internal', 'com.kms.katalon.integration.analytics.properties')
			File testOpsSettingsFile = testOpsSettingsPath.toFile()
			if(!isIntegratedEnabled(testOpsSettingsFile)){
				Properties userProperties = getUserProperties()
				String username = userProperties.getProperty('email')
				String encryptedPassword = userProperties.getProperty('password')
				String password = (CryptoUtil.decode(CryptoUtil.getDefault(encryptedPassword)))
				String serverUrl = userProperties.getProperty('testOps.serverUrl')
				if(StringUtils.isBlank(serverUrl)){
					serverUrl = DEFAULT_SERVER_URL
				}
				String token = requestToken(serverUrl, username, password)
				def (project, team) = getFirstProject(serverUrl, token)
				if(project != null && team != null){
					Properties properties = new Properties()
					properties.setProperty('analytics.authentication.token', getRawValue(token))
					properties.setProperty('analytics.integration.enable', 'true')
					properties.setProperty('analytics.team', getRawValue(JsonUtil.toJson(team)))
					properties.setProperty('analytics.project', getRawValue(JsonUtil.toJson(project)))
					properties.setProperty('analytics.testresult.autosubmit', 'true')

					FileOutputStream fos = new FileOutputStream(testOpsSettingsFile)
					properties.store(fos, null)
				}
			}
		} catch (Exception e){
			// do nothing
		}
	}

	public static String getRawValue(String value) {
		if (value == null)
			return null;
		else {
			return "\"" + StringEscapeUtils.escapeJava((String) value) + "\"";
		}
	}

	private static String requestToken(serverUrl, username, password) {
		String clientCredentials = OAUTH2_CLIENT_ID + ":" + OAUTH2_CLIENT_SECRET;
		String url = serverUrl + "/oauth/token"
		def builder = new RestRequestObjectBuilder()
		def request = builder
				.withRestRequestMethod("POST")
				.withRestUrl(url)
				.withHttpHeaders([
					new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/x-www-form-urlencoded"),
					new TestObjectProperty(HEADER_AUTHORIZATION, ConditionType.EQUALS,
					HEADER_AUTHORIZATION_PREFIX +  Base64.getEncoder().encodeToString(clientCredentials.getBytes()))
				])
				.withUrlEncodedBodyContent([
					new UrlEncodedBodyParameter(LOGIN_PARAM_USERNAME, username),
					new UrlEncodedBodyParameter(LOGIN_PARAM_PASSWORD, password),
					new UrlEncodedBodyParameter(LOGIN_PARAM_GRANT_TYPE_NAME, LOGIN_PARAM_GRANT_TYPE_VALUE),
				])
				.build()
		def response = ServiceRequestFactory.getInstance(request).send(request)

		def responseBody = response.getResponseText()
		def jsonSlurper = new JsonSlurper()
		def object = jsonSlurper.parseText(responseBody)

		return object.access_token
	}

	private static def getFirstProject(serverUrl, token) {
		String url = serverUrl + "/api/v1/projects/first"
		def builder = new RestRequestObjectBuilder()
		def request = builder
				.withRestRequestMethod("GET")
				.withRestUrl(url)
				.withHttpHeaders([
					new TestObjectProperty(HEADER_AUTHORIZATION, ConditionType.EQUALS,
					HEADER_VALUE_AUTHORIZATION_PREFIX +  token)
				])
				.build()
		def response = ServiceRequestFactory.getInstance(request).send(request)

		def responseBody = response.getResponseText()
		def jsonSlurper = new JsonSlurper()
		def projects = jsonSlurper.parseText(responseBody)
		if(projects.size() > 0){
			def firstProject = projects.get(0)
			return [
				new Project(firstProject),
				new Team(firstProject.team)
			]
		} else {
			return [null, null]
		}
	}

	private static Properties getUserProperties() {
		Path path = Paths.get(APP_USER_DIR_LOCATION, 'application.properties')
		File file = path.toFile();
		InputStream inputStream = new FileInputStream(file)
		Properties properties = new Properties()
		properties.load(inputStream)
		return properties
	}

	private static boolean isIntegratedEnabled(File settingsFile) {
		if(!settingsFile.exists()){
			return false
		}
		settingsFile.withInputStream{ stream ->
			Properties properties = new Properties()
			properties.load(stream)
			String project = properties.getProperty('analytics.project')
			return project != null
		}
	}
}

