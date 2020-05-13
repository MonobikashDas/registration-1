/**
 *
 */
package io.mosip.registration.processor.packet.utility.utils;

import io.mosip.registration.processor.packet.utility.constants.IDschemaConstants;
import io.mosip.registration.processor.packet.utility.exception.ApiNotAccessibleException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;


/**
 * The Class IdSchemaUtils.
 *
 * @author Sowmya
 */

/**
 * Instantiates a new id schema utils.
 */
@Component
public class IdSchemaUtils {

    @Autowired
    private RestUtil restUtil;

    @Autowired
    private Environment env;

    private String idschema = null;

    @Value("${packet.default.source}")
    private String defaultSource;

    @Value("${schema.default.fieldCategory}")
    private String defaultFieldCategory;

    /**
     * Gets the source field category from id schema
     *
     * @param id the id
     * @return the source
     * @throws IOException
     */
    public String getSource(String id) throws IOException, ApiNotAccessibleException {
        try {
            String idSchema = getIdSchema();
            JSONObject properties = getJSONObjFromStr(idSchema, IDschemaConstants.PROPERTIES);
            JSONObject identity = getJSONObj(properties, IDschemaConstants.IDENTITY);
            JSONObject property = getJSONObj(identity, IDschemaConstants.PROPERTIES);
            JSONObject value = getJSONObj(property, id);
            String fieldCategory = getFieldCategory(value);
            return fieldCategory;
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }

    /**
     * Get the id schema from syncdata service
     *
     * @return idschema as string
     * @throws ApiNotAccessibleException
     * @throws IOException
     */
    public String getIdSchema() throws ApiNotAccessibleException, IOException {
        if (idschema != null && !idschema.isEmpty())
            return idschema;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(env.getProperty(IDschemaConstants.IDSCHEMA_URL));
        UriComponents uriComponents = builder.build(false).encode();
        String response = restUtil.getApi(uriComponents.toUri(), String.class);
        String responseString = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject respObj = (JSONObject) jsonObject.get(IDschemaConstants.RESPONSE);
            responseString = respObj != null ? (String) respObj.get(IDschemaConstants.SCHEMA_JSON) : null;
        } catch (JSONException e) {
            throw new IOException(e);
        }

        if (responseString != null)
            idschema = responseString;
        else
            throw new ApiNotAccessibleException("Could not get id schema");

        return idschema;
    }

    /**
     * Gets the json.
     *
     * @param configServerFileStorageURL the config server file storage URL
     * @param uri                        the uri
     * @return the json
     */
    public static String getJson(String configServerFileStorageURL, String uri) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(configServerFileStorageURL + uri, String.class);
    }

    /**
     * Gets the field category.
     *
     * @param jsonObject the json object
     * @return the field category
     */
    private String getFieldCategory(JSONObject jsonObject) throws JSONException {
        String fieldCategory = jsonObject != null ? jsonObject.getString(IDschemaConstants.FIELDCATEGORY) : null;

        if (fieldCategory != null && fieldCategory.equalsIgnoreCase(defaultFieldCategory)) {
            fieldCategory = defaultSource;
        }
        return fieldCategory;
    }

    /**
     * Search a field in json
     *
     * @param jsonObject
     * @param id
     * @return
     * @throws JSONException
     */
    private JSONObject getJSONObj(JSONObject jsonObject, String id) throws JSONException {
        return (jsonObject == null) ? null : (JSONObject) jsonObject.get(id);
    }

    /**
     * Search a field in json string
     *
     * @param jsonString
     * @param id
     * @return
     * @throws JSONException
     */
    private JSONObject getJSONObjFromStr(String jsonString, String id) throws JSONException {
        return (jsonString == null) ? null : (JSONObject) new JSONObject(jsonString).get(id);
    }
}
