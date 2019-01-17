package org.icann.czds.sdk.model;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClientConfiguration {

    private static ClientConfiguration configuration = null;

    private static String username;
    private static String password;
    private static String authenticationBaseUrl;
    private static String czdsBaseUrl;
    private static String workingDirectory;


    public static ClientConfiguration getInstance() throws IOException{
        if(configuration == null) {
            loadDefaultClientConfiguration();
        }

        return configuration;
    }

    /**
     * Loads the default configuration from application.properties file.
     */
    private static ClientConfiguration loadDefaultClientConfiguration() throws IOException {
        InputStream inputStream = ClientConfiguration.class.getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        properties.load(inputStream);

        String userName = properties.getProperty("icann.account.username");
        String password = properties.getProperty("icann.account.password");

        // Must specify authentication.base.url and czds.base.url
        String authenBaseUrl = properties.getProperty("authentication.base.url");
        String czdsBaseUrl = properties.getProperty("czds.base.url");

        // Default to current dir if zonefile.output.directory is not specified.
        String workingDir = properties.getProperty("working.directory");
        if(StringUtils.isBlank(workingDir)) {
            workingDir = System.getProperty("user.dir");
        }

        configuration =  new ClientConfiguration(userName, password, authenBaseUrl, czdsBaseUrl, workingDir);

        return configuration;
    }

    /**
     * If initiated using this constructor, you can specify location of file download
     * */
    public ClientConfiguration(String userName, String password, String authenBaseUrl, String czdsBaseUrl, String workingDir) {
        setUserName(userName);
        setPassword(password);
        setAuthenticationBaseUrl(authenBaseUrl);
        setCzdsBaseUrl(czdsBaseUrl);
        setWorkingDirectory(workingDir);
    }

    private static String normalizeWithBackSlash(String value) {
        value = StringUtils.trim(value);

        if(!StringUtils.endsWith(value, "/")) {
            value = value + "/";
        }

        return value;
    }

    public static String checkNullValue(String name, String value) throws IOException {
        if(StringUtils.isBlank(value)) {
            throw new IOException(String.format("ERROR: missging %. Please configure it in application.properties file or pass in via command line.", name));
        } else {
            return value.trim();
        }
    }

    public static String validate() {
        StringBuilder sb = new StringBuilder();

        // Check Authentication url
        if(StringUtils.isBlank(getAuthenticationBaseUrl())) {
            sb.append("Missing authentication base URL.\n");
        }

        // Check CZDS url
        if(StringUtils.isBlank(getCzdsBaseUrl())) {
            sb.append("Missing CZDS base URL.\n");
        }

        // check username
        if(StringUtils.isBlank(getUserName())) {
            sb.append("Missing username.\n");
        }

        // check password
        if(StringUtils.isBlank(getPassword())) {
            sb.append("Missing password.\n");
        }

        return sb.toString();
    }

    public static String getUserName() {
        return ClientConfiguration.username;
    }

    public static void setUserName(String userName) {
        if(!StringUtils.isBlank(userName)) {
            ClientConfiguration.username = userName;
        }
    }

    public static String getPassword() {
        return ClientConfiguration.password;
    }

    public static void setPassword(String password) {
        if(!StringUtils.isBlank(password)) {
            ClientConfiguration.password = password;
        }
    }

    public static String getAuthenticationBaseUrl() {
        return authenticationBaseUrl;
    }



    public static void setAuthenticationBaseUrl(String authenBaseUrl) {
        ClientConfiguration.authenticationBaseUrl = authenBaseUrl;
    }


    public static String getCzdsBaseUrl() {
        return czdsBaseUrl;
    }

    public static void setCzdsBaseUrl(String czdsBaseUrl) {
        ClientConfiguration.czdsBaseUrl = czdsBaseUrl;
    }

    public static String getWorkingDirectory() {
        return ClientConfiguration.workingDirectory;
    }

    public static void setWorkingDirectory(String directory) {
        ClientConfiguration.workingDirectory = directory;
    }
}
