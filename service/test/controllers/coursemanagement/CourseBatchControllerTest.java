package controllers.coursemanagement;

import static controllers.TestUtil.mapToJson;
import static org.junit.Assert.assertEquals;
import static play.test.Helpers.route;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.BaseControllerTest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.sunbird.common.models.util.JsonKey;
import play.libs.Json;
import play.mvc.Http.RequestBuilder;
import play.mvc.Result;
import util.RequestInterceptor;

/** Created by arvind on 1/12/17. */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(PowerMockRunner.class)
@PrepareForTest(RequestInterceptor.class)
@PowerMockIgnore("javax.management.*")
public class CourseBatchControllerTest extends BaseControllerTest {
  public static String COURSE_ID = "courseId";
  public static String COURSE_NAME = "courseName";
  public static int DAY_OF_MONTH = 2;
  public static String INVALID_ENROLLMENT_TYPE = "invalid";
  public static String BATCH_ID = "batchID";
  public static String USER_IDS = "userIds";

  @Test
  public void testCreateBatchSuccess() {
    String data =
        mapToJson(
            createAndUpdateCourseBatchRequest(
                COURSE_ID, COURSE_NAME, JsonKey.INVITE_ONLY, new Date(), getEndDate(true)));
    JsonNode json = Json.parse(data);
    RequestBuilder req =
        new RequestBuilder().bodyJson(json).uri("/v1/course/batch/create").method("POST");
    req.headers(headerMap);
    Result result = route(req);
    assertEquals(200, result.status());
  }

  @Test
  public void testCreateBatchSuccessWithoutEndDate() {
    String data =
        mapToJson(
            createAndUpdateCourseBatchRequest(
                COURSE_ID, COURSE_NAME, JsonKey.INVITE_ONLY, new Date(), null));

    JsonNode json = Json.parse(data);
    RequestBuilder req =
        new RequestBuilder().bodyJson(json).uri("/v1/course/batch/create").method("POST");
    req.headers(headerMap);
    Result result = route(req);
    assertEquals(200, result.status());
  }

  @Test
  public void testCreateBatchFailureWithInvalidEnrollmentType() {
    String data =
        mapToJson(
            createAndUpdateCourseBatchRequest(
                COURSE_ID, COURSE_NAME, INVALID_ENROLLMENT_TYPE, new Date(), getEndDate(true)));
    JsonNode json = Json.parse(data);
    RequestBuilder req =
        new RequestBuilder().bodyJson(json).uri("/v1/course/batch/create").method("POST");
    req.headers(headerMap);
    Result result = route(req);
    assertEquals(400, result.status());
  }

  @Test
  public void testCreateBatchFailureWithEndDateBeforeStartDate() {
    String data =
        mapToJson(
            createAndUpdateCourseBatchRequest(
                COURSE_ID, COURSE_NAME, INVALID_ENROLLMENT_TYPE, new Date(), getEndDate(false)));
    JsonNode json = Json.parse(data);
    RequestBuilder req =
        new RequestBuilder().bodyJson(json).uri("/v1/course/batch/create").method("POST");
    req.headers(headerMap);
    Result result = route(req);
    assertEquals(400, result.status());
  }

  @Test
  public void testCreateBatchFailureWithSameStartAndEndDate() {
    Date currentdate = new Date();
    String data =
        mapToJson(
            createAndUpdateCourseBatchRequest(
                COURSE_ID, COURSE_NAME, INVALID_ENROLLMENT_TYPE, currentdate, currentdate));

    JsonNode json = Json.parse(data);
    RequestBuilder req =
        new RequestBuilder().bodyJson(json).uri("/v1/course/batch/create").method("POST");
    req.headers(headerMap);
    Result result = route(req);
    assertEquals(400, result.status());
  }

  @Test
  public void testUpdateBatchSuccessWithoutEndDate() {
    String data =
        mapToJson(
            createAndUpdateCourseBatchRequest(
                COURSE_ID, COURSE_NAME, JsonKey.INVITE_ONLY, new Date(), null));

    JsonNode json = Json.parse(data);
    RequestBuilder req =
        new RequestBuilder().bodyJson(json).uri("/v1/course/batch/update").method("PATCH");
    req.headers(headerMap);
    Result result = route(req);
    assertEquals(200, result.status());
  }

  @Test
  public void testUpdateBatchFailureWithEndDateBeforeStartDate() {
    String data =
        mapToJson(
            createAndUpdateCourseBatchRequest(
                COURSE_ID, COURSE_NAME, INVALID_ENROLLMENT_TYPE, new Date(), getEndDate(false)));
    JsonNode json = Json.parse(data);
    RequestBuilder req =
        new RequestBuilder().bodyJson(json).uri("/v1/course/batch/update").method("PATCH");
    req.headers(headerMap);
    Result result = route(req);
    assertEquals(400, result.status());
  }

  @Test
  public void testUpdateBatchSuccess() {
    String data =
        mapToJson(
            createAndUpdateCourseBatchRequest(
                COURSE_ID, COURSE_NAME, JsonKey.INVITE_ONLY, new Date(), getEndDate(true)));
    JsonNode json = Json.parse(data);
    RequestBuilder req =
        new RequestBuilder().bodyJson(json).uri("/v1/course/batch/update").method("PATCH");
    req.headers(headerMap);
    Result result = route(req);
    assertEquals(200, result.status());
  }

  @Test
  public void testUpdateBatchFailureWithSameStartAndEndDate() {
    Date currentDate = new Date();
    String data =
        mapToJson(
            createAndUpdateCourseBatchRequest(
                COURSE_ID, COURSE_NAME, INVALID_ENROLLMENT_TYPE, currentDate, currentDate));
    JsonNode json = Json.parse(data);
    RequestBuilder req =
        new RequestBuilder().bodyJson(json).uri("/v1/course/batch/update").method("PATCH");
    req.headers(headerMap);
    Result result = route(req);
    assertEquals(400, result.status());
  }

  @Test
  public void testGetBatchSuccess() {
    RequestBuilder req =
        new RequestBuilder().uri("/v1/course/batch/read/" + BATCH_ID).method("GET");
    req.headers(headerMap);
    Result result = route(req);
    assertEquals(200, result.status());
  }

  @Test
  public void testSearchBatchSuccess() {
    Map<String, Object> requestMap = new HashMap<>();
    Map<String, Object> innerMap = new HashMap<>();
    innerMap.put(JsonKey.FILTERS, BATCH_ID);
    requestMap.put(JsonKey.REQUEST, innerMap);
    String data = mapToJson(requestMap);
    JsonNode json = Json.parse(data);
    RequestBuilder req =
        new RequestBuilder().bodyJson(json).uri("/v1/course/batch/search").method("POST");
    req.headers(headerMap);
    Result result = route(req);
    assertEquals(200, result.status());
  }

  @Test
  public void testAddUserToBatchSuccess() {
    String data = mapToJson(getAddUserToBatchRequest(BATCH_ID, USER_IDS));
    JsonNode json = Json.parse(data);
    RequestBuilder req =
        new RequestBuilder()
            .bodyJson(json)
            .uri("/v1/course/batch/users/add/batchid")
            .method("POST");
    req.headers(headerMap);
    Result result = route(req);
    assertEquals(200, result.status());
  }

  @Test
  public void testAddUserToBatchFailureWithUserIdsNull() {
    String data = mapToJson(getAddUserToBatchRequest(BATCH_ID, null));
    JsonNode json = Json.parse(data);
    RequestBuilder req =
        new RequestBuilder()
            .bodyJson(json)
            .uri("/v1/course/batch/users/add/" + BATCH_ID)
            .method("POST");
    req.headers(headerMap);
    Result result = route(req);
    assertEquals(400, result.status());
  }

  private Map<String, Object> createAndUpdateCourseBatchRequest(
      String courseId, String name, String enrollmentType, Date startDate, Date endDate) {
    Map<String, Object> innerMap = new HashMap<>();
    if (courseId != null) innerMap.put(JsonKey.COURSE_ID, courseId);
    if (name != null) innerMap.put(JsonKey.NAME, name);
    if (enrollmentType != null) innerMap.put(JsonKey.ENROLLMENT_TYPE, enrollmentType);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    if (startDate != null) innerMap.put(JsonKey.START_DATE, format.format(startDate));
    if (endDate != null) {
      innerMap.put(JsonKey.END_DATE, format.format(endDate));
    }
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(JsonKey.REQUEST, innerMap);
    return requestMap;
  }

  private Date getEndDate(boolean isFuture) {
    Calendar calendar = Calendar.getInstance();
    if (isFuture) {

      calendar.add(Calendar.DAY_OF_MONTH, DAY_OF_MONTH);

    } else {
      calendar.add(Calendar.DAY_OF_MONTH, -DAY_OF_MONTH);
    }
    return calendar.getTime();
  }

  private Map<String, Object> getAddUserToBatchRequest(String batchID, String userIds) {
    Map<String, Object> requestMap = new HashMap<>();
    Map<String, Object> innerMap = new HashMap<>();
    innerMap.put(JsonKey.BATCH_ID, batchID);
    innerMap.put(JsonKey.USER_IDs, userIds);
    requestMap.put(JsonKey.REQUEST, innerMap);
    return requestMap;
  }
}
