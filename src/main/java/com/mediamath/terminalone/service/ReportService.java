/*******************************************************************************
 * Copyright 2016 MediaMath
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.mediamath.terminalone.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mediamath.terminalone.Connection;
import com.mediamath.terminalone.ReportCriteria;
import com.mediamath.terminalone.exceptions.ClientException;
import com.mediamath.terminalone.models.JsonResponse;
import com.mediamath.terminalone.models.T1Entity;
import com.mediamath.terminalone.models.T1User;
import com.mediamath.terminalone.models.reporting.Having;
import com.mediamath.terminalone.models.reporting.ReportError;
import com.mediamath.terminalone.models.reporting.ReportErrorEntityInfo;
import com.mediamath.terminalone.models.reporting.ReportFilter;
import com.mediamath.terminalone.models.reporting.ReportStatus;
import com.mediamath.terminalone.models.reporting.ReportValidationResponse;
import com.mediamath.terminalone.models.reporting.Reports;
import com.mediamath.terminalone.models.reporting.meta.Meta;
import com.mediamath.terminalone.models.reporting.meta.MetaData;
import com.mediamath.terminalone.models.reporting.meta.MetaDimensionData;
import com.mediamath.terminalone.models.reporting.meta.MetaDimensions;
import com.mediamath.terminalone.models.reporting.meta.MetaMetrics;
import com.mediamath.terminalone.models.reporting.meta.MetricsData;
import com.mediamath.terminalone.models.reporting.meta.TimeField;
import com.mediamath.terminalone.models.reporting.meta.TimeInterval;
import com.mediamath.terminalone.utils.Utility;

public class ReportService {

  private static final String UTF_8 = "UTF-8";

  private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

  private static final String META = "meta";

  private static final String YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";

  /**
   * gets uri for report meta data.
   * 
   * @return StringBuilder object.
   * 
   */
  public StringBuilder getMetaUri() {
    StringBuilder path = new StringBuilder();
    path.append(META);
    return path;
  }

  /**
   * gets uri for reports.
   * 
   * @param report
   *          expects a ReportCriteria entity.
   * 
   * @return StringBuilder object.
   */
  public StringBuilder getReportUri(ReportCriteria report) {
    StringBuilder path;

    if (report == null)
      return null;

    path = new StringBuilder(report.getReportName());

    try {
      // dimensions
      filterDimensions(report, path);

      // filters
      addFilters(report, path);

      // having.
      addHaving(report, path);

      // metrics
      filterMetrics(report, path);

      // time rollup.
      filterTimeRollUp(report, path);

      // time_ window or start_date or end_date
      filterTimeWindowStartDateEndDate(report, path);

      // order
      filterOrder(report, path);

      // pagelimit & page offset
      filterPageLimitPageOffset(report, path);

      // precision
      if (report.getPrecision() > 0) {
        path.append("&precision=" + report.getPrecision());
      }

    } catch (UnsupportedEncodingException exception) {
      logger.debug("getReportUri: UnsupportedEncodingException occured: ");
      Utility.logStackTrace(exception);
    }

    return path;
  }

  private void filterPageLimitPageOffset(ReportCriteria report, StringBuilder path) {
    if (report.getPageLimit() != null && !report.getPageLimit().isEmpty()
        && (report.getPageOffset() == null || report.getPageOffset().isEmpty())) {

      uriAppender(path);
      path.append("page_limit=" + report.getPageLimit());

    } else if (report.getPageOffset() != null && !report.getPageOffset().isEmpty()
        && report.getPageLimit() != null && !report.getPageLimit().isEmpty()) {

      uriAppender(path);
      path.append("page_limit=" + report.getPageLimit());
      path.append("&page_offset=" + report.getPageOffset());
    }
  }

  private void filterOrder(ReportCriteria report, StringBuilder path) {
    if (report.getOrder() != null && !report.getOrder().isEmpty()) {

      uriAppender(path);

      StringBuilder buffer = new StringBuilder();
      for (String order : report.getOrder()) {
        if (buffer.length() == 0) {
          buffer.append("order=" + order);
        } else {
          buffer.append("," + order);
        }
      }
      path.append(buffer);
    }
  }

  private void filterTimeWindowStartDateEndDate(ReportCriteria report, StringBuilder path) {
    if (report.getTimeWindow() != null && !report.getTimeWindow().isEmpty()
        && report.getStartDate() == null && report.getEndDate() == null) {

      uriAppender(path);

      path.append("time_window=" + report.getTimeWindow());

    } else if (checkTimeWindow(report) && checkStartDate(report) && checkEndDate(report)) {

      uriAppender(path);

      path.append("start_date=" + report.getStartDate());
      path.append("&end_date=" + report.getEndDate());

    }
  }
  
  private boolean checkEndDate(ReportCriteria report) {
    return report.getEndDate() != null && !report.getEndDate().isEmpty();
  }
  
  private boolean checkStartDate(ReportCriteria report) {
    return report.getStartDate() != null && !report.getStartDate().isEmpty();
  }

  private boolean checkTimeWindow(ReportCriteria report) {
    return report.getTimeWindow() == null;
  }

  private void filterTimeRollUp(ReportCriteria report, StringBuilder path) {
    if (report.getTimeRollup() != null && !report.getTimeRollup().isEmpty()) {
      uriAppender(path);

      path.append("time_rollup=" + report.getTimeRollup());
    }
  }

  private void filterMetrics(ReportCriteria report, StringBuilder path)
      throws UnsupportedEncodingException {
    if (report.getMetrics() != null && !report.getMetrics().isEmpty()) {

      if (path.indexOf("?") == -1) {
        path.append("?metrics=");
      } else {
        path.append("&metrics=");
      }

      StringBuilder buffer = new StringBuilder();
      for (String metric : report.getMetrics()) {
        if (buffer.length() == 0) {
          buffer.append(metric);
        } else {
          buffer.append("," + metric);
        }
      }
      path.append(URLEncoder.encode(buffer.toString(), UTF_8));
    }
  }

  private void addHaving(ReportCriteria report, StringBuilder path)
      throws UnsupportedEncodingException {
       
    if (report.getHaving() != null && !report.getHaving().isEmpty()) {

      if (path.indexOf("?") == -1) {
        path.append("?having=");
      } else {
        path.append("&having=");
      }

      StringBuilder buffer = buildHavingCriteria(report);
      // encode
      path.append(URLEncoder.encode(buffer.toString(), UTF_8));
    }
  }

  private StringBuilder buildHavingCriteria(ReportCriteria report) {
    StringBuilder buffer = new StringBuilder();
    int havingSize = 0;
    for (Having having : report.getHaving()) {
      if (checkHavingKey(having) && checkHavingOperator(having) && checkHavingValue(having)) {

        buffer.append(having.getKey() + having.getOperator() + having.getValue());

        if (havingSize != report.getHaving().size() - 1) {
          buffer.append("&");
        }
        havingSize++;
      }
    }
    return buffer;
  }
  
  private boolean checkHavingValue(Having having) {
    return having.getValue() != null && !having.getValue().isEmpty();
  }
  
  private boolean checkHavingKey(Having having) {
    return having.getKey() != null && !having.getKey().isEmpty();
  }
  
  private boolean checkHavingOperator(Having having) {
    return having.getOperator() != null && !having.getOperator().isEmpty();
  }

  private void addFilters(ReportCriteria report, StringBuilder path)
      throws UnsupportedEncodingException {
    if (report.getFilters() != null && !report.getFilters().isEmpty()) {

      if (path.indexOf("?") == -1) {
        path.append("?filter=");
      } else {
        path.append("&filter=");
      }

      StringBuilder buffer = buildFilterCriteria(report);
      // encode
      path.append(URLEncoder.encode(buffer.toString(), UTF_8));
    }
  }

  private StringBuilder buildFilterCriteria(ReportCriteria report) {
    int filterSize = 0;
    StringBuilder buffer = new StringBuilder();
    for (ReportFilter f : report.getFilters()) {
      if (checkFilterKey(f) && checkFilterOperator(f) && checkFilterValue(f)) {
        buffer.append(f.getKey() + f.getOperator() + f.getValue());
        if (filterSize != report.getFilters().size() - 1) {
          buffer.append("&");
        }
        filterSize++;
      }
    }
    return buffer;
  }
  
  private boolean checkFilterKey(ReportFilter f) {
    return f.getKey() != null && !f.getKey().isEmpty();
  }
  
  private boolean checkFilterOperator(ReportFilter f) {
    return f.getOperator() != null && !f.getOperator().isEmpty();
  }
  
  private boolean checkFilterValue(ReportFilter f) {
    return f.getValue() != null && !f.getValue().isEmpty();
  }

  private void filterDimensions(ReportCriteria report, StringBuilder path)
      throws UnsupportedEncodingException {
    if (report.getDimensions() != null && !report.getDimensions().isEmpty()) {

      if (path.indexOf("?") == -1) {
        path.append("?dimensions=");
      } else {
        path.append("&dimensions=");
      }

      StringBuilder buffer = new StringBuilder();
      for (String dimension : report.getDimensions()) {
        if (buffer.length() == 0) {
          buffer.append(dimension);
        } else {
          buffer.append("," + dimension);
        }
      }
      // Encode
      path.append(URLEncoder.encode(buffer.toString(), UTF_8));
    }
  }

  private void uriAppender(StringBuilder path) {
    if (path.indexOf("?") == -1) {
      path.append("?");
    } else {
      path.append("&");
    }
  }

  /**
   * this method parses meta query response.
   * 
   * @param response
   *          requires the JSON response.
   * @return JsonResponse<? extends T1Entity> returns JsonResponse of type T.
   * 
   */
  public JsonResponse<? extends T1Entity> parseMetaResponse(String response) {
    JsonParser parser = new JsonParser();
    JsonResponse<Meta> finalResponse;
    JsonObject obj = parser.parse(response).getAsJsonObject();

    JsonElement reportsElement = obj.get("reports");
    JsonObject reportsObj = reportsElement.getAsJsonObject();

    if (reportsObj == null)
      return null;

    Meta meta = new Meta();
    Type type = new TypeToken<MetaData>() {
    }.getType();

    HashMap<String, MetaData> metaData = new HashMap<String, MetaData>();

    GsonBuilder builder = new GsonBuilder();
    builder.setDateFormat(YYYY_MM_DD_T_HH_MM_SS);
    Gson gson = builder.create();

    for (Entry<String, JsonElement> a : reportsObj.entrySet()) {
      String key = a.getKey();
      MetaData value = gson.fromJson(a.getValue(), type);
      metaData.put(key, value);
    }
    meta.setMetaData(metaData);
    finalResponse = new JsonResponse<Meta>(meta);

    return finalResponse;
  }

  /**
   * parses the meta data response for a particular report query.
   * 
   * @param response
   *          requires JSON response.
   * @return MetaData object.
   */
  public MetaData parseReportMetaResponse(String response) {

    GsonBuilder builder = new GsonBuilder();
    builder.setDateFormat(YYYY_MM_DD_T_HH_MM_SS);
    Gson gson = builder.create();
    MetaData data = gson.fromJson(response, MetaData.class);

    JsonParser parser = new JsonParser();
    JsonObject obj = parser.parse(response).getAsJsonObject();
    JsonElement reportsElement = obj.get("structure");

    JsonObject dimensionObj = reportsElement.getAsJsonObject().get("dimensions").getAsJsonObject();
    JsonObject metricsObj = reportsElement.getAsJsonObject().get("metrics").getAsJsonObject();
    JsonObject timefieldObj = reportsElement.getAsJsonObject().get("time_field").getAsJsonObject();

    // dimensions
    parseDimensions(gson, data, dimensionObj);

    // metrics
    parseMetrics(gson, data, metricsObj);

    // time_field
    parseTimeField(gson, data, timefieldObj);

    return data;
    // end
  }

  private void parseTimeField(Gson gson, MetaData data, JsonObject timefieldObj) {
    if (timefieldObj != null) {

      TimeField timefield = new TimeField();
      HashMap<String, TimeInterval> timeFieldMap = new HashMap<String, TimeInterval>();

      for (Entry<String, JsonElement> a : timefieldObj.entrySet()) {
        String key = a.getKey();
        TimeInterval value = gson.fromJson(a.getValue(), TimeInterval.class);
        timeFieldMap.put(key, value);
      }

      timefield.setData(timeFieldMap);
      data.getStructure().setTimeField(timefield);
    }
  }

  private void parseMetrics(Gson gson, MetaData data, JsonObject metricsObj) {
    if (metricsObj != null) {
      MetaMetrics metrics = new MetaMetrics();
      HashMap<String, MetricsData> metricsMap = new HashMap<String, MetricsData>();

      for (Entry<String, JsonElement> a : metricsObj.entrySet()) {
        String key = a.getKey();
        MetricsData value = gson.fromJson(a.getValue(), MetricsData.class);
        metricsMap.put(key, value);
      }

      metrics.setMetricsMap(metricsMap);
      data.getStructure().setMetrics(metrics);
    }
  }

  private void parseDimensions(Gson gson, MetaData data, JsonObject dimensionObj) {
    if (dimensionObj != null) {
      MetaDimensions dimensions = new MetaDimensions();
      HashMap<String, MetaDimensionData> dimensionsInfoMap = new HashMap<String, MetaDimensionData>();

      for (Entry<String, JsonElement> a : dimensionObj.entrySet()) {
        String key = a.getKey();
        MetaDimensionData value = gson.fromJson(a.getValue(), MetaDimensionData.class);
        dimensionsInfoMap.put(key, value);
      }

      dimensions.setDimensionsInfoMap(dimensionsInfoMap);
      data.getStructure().setDimensions(dimensions);
    }
  }

  /**
   * Gets Report Data.
   * 
   * @param report
   *          type of report for which you want to query.
   * @param finalPath
   *          path to hit the API endpoint.
   * @param connection
   *          requires a Connection endpoint.
   * @param user
   *          requires a valid user login session.
   * @throws ClientException
   *           a client exception is thrown if any error occurs.
   */
  public BufferedReader getReportData(Reports report, String finalPath, Connection connection,
      T1User user) throws ClientException {

    Response response = connection.getReportData(finalPath, user);
    BufferedReader reader = null;
    if ("text".equalsIgnoreCase(response.getMediaType().getType()) && "xml".equalsIgnoreCase(response.getMediaType().getSubtype()) && response.getStatus() != 200) {
      try {

        ObjectMapper xmlMapper = new XmlMapper();
        String error = response.readEntity(String.class);
        ReportError re = xmlMapper.readValue(error, ReportError.class);
        throwReportError(re);

      } catch (JsonParseException jsonParseException) {
        Utility.logStackTrace(jsonParseException);
        throw new ClientException("Json Parse Exception Occured");
      } catch (JsonMappingException jsonMappingException) {
        Utility.logStackTrace(jsonMappingException);
        throw new ClientException("Json Mapping Exception Occured");
      } catch (IOException ioException) {
        Utility.logStackTrace(ioException);
        throw new ClientException("IO Exception Occured");
      }

    } else if ("text".equalsIgnoreCase(response.getMediaType().getType()) 
          && "csv".equalsIgnoreCase(response.getMediaType().getSubtype())
          && response.getStatus() == 200) {
      
      InputStream responseStream = response.readEntity(InputStream.class);
      reader = new BufferedReader(new InputStreamReader(responseStream));
    }
    return reader;
  }

  /**
   * validates the Report Data.
   * 
   * @param report
   *          the specific report you want to validate.
   * 
   * @param finalPath
   *          path to API endpoint.
   * @param connection
   *          requires a connection object.
   * @param user
   *          requires a valid user login session.
   * @return ReportValidationResponse object.
   * @throws ClientException
   *           a client exception is thrown if any error occurs.
   */
  public ReportValidationResponse validateReportData(Reports report, String finalPath,
      Connection connection, T1User user) throws ClientException {

    Response response = connection.getReportData(finalPath, user);
    ReportValidationResponse re = null;

    if ("text".equalsIgnoreCase(response.getMediaType().getType()) && "xml".equalsIgnoreCase(response.getMediaType().getSubtype())) {

      try {

        ObjectMapper xmlMapper = new XmlMapper();
        String error = response.readEntity(String.class);
        re = xmlMapper.readValue(error, ReportValidationResponse.class);

      } catch (JsonParseException jsonParseException) {
        Utility.logStackTrace(jsonParseException);
        throw new ClientException("Json Parse Exception Occured");
      } catch (JsonMappingException jsonMappingException) {
        Utility.logStackTrace(jsonMappingException);
        throw new ClientException("Json Mapping Exception Occured");
      } catch (IOException ioException) {
        Utility.logStackTrace(ioException);
        throw new ClientException("IO Exception Occured");
      }

    }
    return re;
  }

  private void throwReportError(ReportError re) throws ClientException {
    if (re != null) {

      StringBuilder buffer = new StringBuilder();

      parseErrorEntity(re, buffer);

      parseErrorStatus(re, buffer);

      throw new ClientException(buffer.toString());
    }

  }

  private void parseErrorStatus(ReportError re, StringBuilder buffer) {
    if (re.getStatus() != null && re.getStatus().length > 0) {
      for (ReportStatus stats : re.getStatus()) {
        if (checkStatusCode(stats) && checkStatusReason(stats) && checkStatusValue(stats)) {
          buffer.append("Status[ Code: " + stats.getCode() + ", Reason: " + stats.getReason()
              + ", value: " + stats.getValue() + " ]");
        }
      }
    }
  }
  
  private boolean checkStatusValue(ReportStatus stats) {
    return stats.getValue() != null && !stats.getValue().isEmpty();
  }
  
  private boolean checkStatusReason(ReportStatus stats) {
    return stats.getReason() != null && !stats.getReason().isEmpty();
  }

  private boolean checkStatusCode(ReportStatus stats) {
    return stats.getCode() != null && !stats.getCode().isEmpty();
  }

  private void parseErrorEntity(ReportError re, StringBuilder buffer) {
    if (re.getEntity() != null && re.getEntity().length > 0) {
      for (ReportErrorEntityInfo rentity : re.getEntity()) {

        if (rentity.getId() != null && rentity.getType() != null && !rentity.getId().isEmpty()
            && !rentity.getType().isEmpty()) {
          buffer.append("Entity[ ID: " + rentity.getId() + ", Type: " + rentity.getType() + " ]");
        }
      }

    }
  }
}
