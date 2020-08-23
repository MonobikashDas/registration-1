/*
package io.mosip.registration.processor.bio.dedupe.service.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.mosip.kernel.packetmanager.spi.PacketReaderService;
import io.mosip.kernel.packetmanager.util.IdSchemaUtils;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import io.mosip.registration.processor.bio.dedupe.exception.ABISAbortException;
import io.mosip.registration.processor.bio.dedupe.exception.ABISInternalError;
import io.mosip.registration.processor.bio.dedupe.exception.UnableToServeRequestABISException;
import io.mosip.registration.processor.bio.dedupe.exception.UnexceptedError;
import io.mosip.registration.processor.bio.dedupe.service.impl.BioDedupeServiceImpl;
import io.mosip.registration.processor.core.constant.MappingJsonConstants;
import io.mosip.registration.processor.core.exception.ApisResourceAccessException;
import io.mosip.registration.processor.core.logger.LogDescription;
import io.mosip.registration.processor.core.packet.dto.Identity;
import io.mosip.registration.processor.core.packet.dto.abis.AbisIdentifyResponseDto;
import io.mosip.registration.processor.core.packet.dto.abis.AbisInsertResponseDto;
import io.mosip.registration.processor.core.packet.dto.abis.CandidateListDto;
import io.mosip.registration.processor.core.packet.dto.abis.CandidatesDto;
import io.mosip.registration.processor.core.packet.dto.demographicinfo.DemographicInfoDto;
import io.mosip.registration.processor.core.spi.packetmanager.PacketInfoManager;
import io.mosip.registration.processor.core.spi.restclient.RegistrationProcessorRestClientService;
import io.mosip.registration.processor.core.util.JsonUtil;
import io.mosip.registration.processor.packet.storage.dto.ApplicantInfoDto;
import io.mosip.registration.processor.packet.storage.utils.Utilities;

import io.mosip.registration.processor.status.service.RegistrationStatusService;

*/
/**
 * The Class BioDedupeServiceImplTest.
 *//*

@RefreshScope
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
@PrepareForTest({ IOUtils.class, JsonUtil.class })
public class BioDedupeServiceImplTest {

	*/
/** The input stream. *//*

	@Mock
	InputStream inputStream;

	*/
/** The rest client service. *//*

	@Mock
	RegistrationProcessorRestClientService<Object> restClientService;

	*/
/** The packet info manager. *//*

	@Mock
	PacketInfoManager<Identity, ApplicantInfoDto> packetInfoManager;

	*/
/** The abis insert responce dto. *//*

	@Mock
	AbisInsertResponseDto abisInsertResponseDto = new AbisInsertResponseDto();

	*/
/** The bio dedupe service. *//*

	@InjectMocks
	BioDedupeServiceImpl bioDedupeService = new BioDedupeServiceImpl();

	*/
/** The identify response. *//*

	private AbisIdentifyResponseDto identifyResponse = new AbisIdentifyResponseDto();

	*/
/** The registration id. *//*

	String registrationId = "1000";


	*/
/** The identity. *//*

	Identity identity = new Identity();

	private ListAppender<ILoggingEvent> listAppender;

	private Logger fooLogger;

	@Mock
	private Environment env;

	@Mock
	private RegistrationStatusService registrationStatusService;

	@Mock
	private Utilities utility;

	@Mock
	private PacketReaderService packetReaderService;

	@Mock
	private IdSchemaUtils idSchemaUtils;

	@Mock
	LogDescription description;
	private ClassLoader classLoader;
	*/
/*
	 * 
	 * /** Setup.
	 * 
	 * @throws Exception
	 *//*

	@Before
	public void setup() throws Exception {
		classLoader = getClass().getClassLoader();
		Mockito.doNothing().when(packetInfoManager).saveAbisRef(any(), any(), any());

		abisInsertResponseDto.setReturnValue("2");
		Mockito.when(env.getProperty("mosip.registration.processor.datetime.pattern"))
				.thenReturn("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		ReflectionTestUtils.setField(bioDedupeService, "maxResults", "30");
		ReflectionTestUtils.setField(bioDedupeService, "targetFPIR", "30");


		String refId = "01234567-89AB-CDEF-0123-456789ABCDEF";
		List<String> refIdList = new ArrayList<>();
		refIdList.add(refId);
		Mockito.when(packetInfoManager.getReferenceIdByRid(anyString())).thenReturn(refIdList);

		CandidatesDto candidate1 = new CandidatesDto();
		candidate1.setReferenceId("01234567-89AB-CDEF-0123-456789ABCDEG");

		CandidatesDto candidate2 = new CandidatesDto();
		candidate2.setReferenceId("01234567-89AB-CDEF-0123-456789ABCDEH");


		CandidatesDto[] candidateArray = new CandidatesDto[2];
		candidateArray[0] = candidate1;
		candidateArray[1] = candidate2;

		CandidateListDto candidateList = new CandidateListDto();
		candidateList.setCandidates(candidateArray);

		identifyResponse.setCandidateList(candidateList);

		List<DemographicInfoDto> demoList = new ArrayList<>();
		DemographicInfoDto demo1 = new DemographicInfoDto();
		demoList.add(demo1);
		Mockito.when(packetInfoManager.findDemoById(anyString())).thenReturn(demoList);
		Mockito.when(registrationStatusService.checkUinAvailabilityForRid(anyString())).thenReturn(true);

		File file = new File(classLoader.getResource("RegistrationProcessorIdentity.json").getFile());
		InputStream inputStream = new FileInputStream(file);
		String mappingJson = IOUtils.toString(inputStream);
		JSONObject mappingJSONObject = JsonUtil.objectMapperReadValue(mappingJson, JSONObject.class);
		Mockito.when(utility.getRegistrationProcessorMappingJson())
				.thenReturn(JsonUtil.getJSONObject(mappingJSONObject, MappingJsonConstants.IDENTITY));
		fooLogger = (Logger) LoggerFactory.getLogger(BioDedupeServiceImpl.class);
		listAppender = new ListAppender<>();
		Mockito.when(idSchemaUtils.getSource(anyString(), anyDouble())).thenReturn("id");
		File idJson = new File(classLoader.getResource("ID.json").getFile());
		InputStream ip = new FileInputStream(idJson);
		String idJsonString = IOUtils.toString(ip, "UTF-8");
		*/
/*Mockito.when(utility.getDemographicIdentityJSONObject(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(JsonUtil.getJSONObject(JsonUtil.objectMapperReadValue(idJsonString, JSONObject.class),
						MappingJsonConstants.IDENTITY));*//*


	}

	*/
/**
	 * Insert biometrics success test.
	 *
	 * @throws ApisResourceAccessException
	 *             the apis resource access exception
	 * @throws IOException
	 *//*

	@Test
	public void insertBiometricsSuccessTest() throws ApisResourceAccessException, IOException {

		abisInsertResponseDto.setReturnValue("1");
		Mockito.when(restClientService.postApi(any(), any(),any(), any(), any()))
				.thenReturn(abisInsertResponseDto);

		String authResponse = bioDedupeService.insertBiometrics(registrationId);
		assertTrue(authResponse.equals("success"));

	}

	*/
/**
	 * Insert biometrics ABIS internal error failure test.
	 *
	 * @throws ApisResourceAccessException
	 *             the apis resource access exception
	 * @throws IOException
	 *//*

	@Test(expected = ABISInternalError.class)
	public void insertBiometricsABISInternalErrorFailureTest() throws ApisResourceAccessException, IOException {

		abisInsertResponseDto.setFailureReason("1");
		Mockito.when(restClientService.postApi(any(), any(),any(), any(), any()))
				.thenReturn(abisInsertResponseDto);

		String authResponse = bioDedupeService.insertBiometrics(registrationId);
		assertTrue(authResponse.equals("2"));

	}

	*/
/**
	 * Insert biometrics ABIS abort exception failure test.
	 *
	 * @throws ApisResourceAccessException
	 *             the apis resource access exception
	 * @throws IOException
	 *//*

	@Test(expected = ABISAbortException.class)
	public void insertBiometricsABISAbortExceptionFailureTest() throws ApisResourceAccessException, IOException {

		abisInsertResponseDto = new AbisInsertResponseDto();
		abisInsertResponseDto.setFailureReason("2");
		abisInsertResponseDto.setReturnValue("2");
		Mockito.when(restClientService.postApi(any(), any(),any(), any(), any()))
				.thenReturn(abisInsertResponseDto);

		String authResponse = bioDedupeService.insertBiometrics(registrationId);
		assertTrue(authResponse.equals("2"));

	}

	*/
/**
	 * Insert biometrics unexcepted error failure test.
	 *
	 * @throws ApisResourceAccessException
	 *             the apis resource access exception
	 * @throws IOException
	 *//*

	@Test(expected = UnexceptedError.class)
	public void insertBiometricsUnexceptedErrorFailureTest() throws ApisResourceAccessException, IOException {

		abisInsertResponseDto.setFailureReason("3");
		Mockito.when(restClientService.postApi(any(), any(),any(), any(), any()))
				.thenReturn(abisInsertResponseDto);

		String authResponse = bioDedupeService.insertBiometrics(registrationId);
		assertTrue(authResponse.equals("2"));

	}

	*/
/**
	 * Insert biometrics unable to serve request ABIS exception failure test.
	 *
	 * @throws ApisResourceAccessException
	 *             the apis resource access exception
	 * @throws IOException
	 *//*

	@Test(expected = UnableToServeRequestABISException.class)
	public void insertBiometricsUnableToServeRequestABISExceptionFailureTest()
			throws ApisResourceAccessException, IOException {

		abisInsertResponseDto.setFailureReason("4");
		Mockito.when(restClientService.postApi(any(), any(),any(), any(), any()))
				.thenReturn(abisInsertResponseDto);

		String authResponse = bioDedupeService.insertBiometrics(registrationId);
		assertTrue(authResponse.equals("2"));

	}

	*/
/**
	 * Test perform dedupe success.
	 *
	 * @throws ApisResourceAccessException
	 *             the apis resource access exception
	 * @throws IOException
	 *//*

	@Test
	public void testPerformDedupeSuccess() throws ApisResourceAccessException, IOException {

		identifyResponse.setReturnValue("1");
		Mockito.when(restClientService.postApi(any(), any(),any(), any(), any()))
				.thenReturn(identifyResponse);
		String rid = "27847657360002520181208094056";

		List<String> list = new ArrayList<>();
		list.add(rid);
		Mockito.when(packetInfoManager.getRidByReferenceId(any())).thenReturn(list);

		List<String> ridList = new ArrayList<>();
		ridList.add(rid);
		ridList.add(rid);

		List<DemographicInfoDto> demoList = new ArrayList<>();
		DemographicInfoDto demo1 = new DemographicInfoDto();
		demoList.add(demo1);
		Mockito.when(packetInfoManager.findDemoById(any())).thenReturn(demoList);
		Mockito.when(registrationStatusService.checkUinAvailabilityForRid(any())).thenReturn(true);
		
		List<String> duplicates = bioDedupeService.performDedupe(rid);

		assertEquals(ridList, duplicates);
	}

	*/
/**
	 * Test perform dedupe failure.
	 *
	 * @throws ApisResourceAccessException
	 *             the apis resource access exception
	 * @throws IOException
	 *//*

	@Test(expected = ABISInternalError.class)
	public void testPerformDedupeFailure() throws ApisResourceAccessException, IOException {

		Mockito.when(restClientService.postApi(any(), any(),any(), any(), any()))
				.thenReturn(identifyResponse);
		String rid = "27847657360002520181208094056";
		identifyResponse.setReturnValue("2");
		identifyResponse.setFailureReason("1");

		bioDedupeService.performDedupe(rid);
	}

	*/
/**
	 * Test dedupe abis abort exception.
	 *
	 * @throws ApisResourceAccessException
	 *             the apis resource access exception
	 * @throws IOException
	 *//*

	@Test(expected = ABISAbortException.class)
	public void testDedupeAbisAbortException() throws ApisResourceAccessException, IOException {

		Mockito.when(restClientService.postApi(any(), any(),any(), any(), any()))
				.thenReturn(identifyResponse);
		String rid = "27847657360002520181208094056";
		identifyResponse.setReturnValue("2");
		identifyResponse.setFailureReason("2");

		bioDedupeService.performDedupe(rid);
	}

	*/
/**
	 * Test dedupe unexpected error.
	 *
	 * @throws ApisResourceAccessException
	 *             the apis resource access exception
	 * @throws IOException
	 *//*

	@Test(expected = UnexceptedError.class)
	public void testDedupeUnexpectedError() throws ApisResourceAccessException, IOException {

		Mockito.when(restClientService.postApi(any(), any(),any(), any(), any()))
				.thenReturn(identifyResponse);
		String rid = "27847657360002520181208094056";
		identifyResponse.setReturnValue("2");
		identifyResponse.setFailureReason("3");

		bioDedupeService.performDedupe(rid);
	}

	*/
/**
	 * Test dedupe unable to serve request ABIS exception.
	 *
	 * @throws ApisResourceAccessException
	 *             the apis resource access exception
	 * @throws IOException
	 *//*

	@Test(expected = UnableToServeRequestABISException.class)
	public void testDedupeUnableToServeRequestABISException() throws ApisResourceAccessException, IOException {

		Mockito.when(restClientService.postApi(any(), any(),any(), any(), any()))
				.thenReturn(identifyResponse);
		String rid = "27847657360002520181208094056";
		identifyResponse.setReturnValue("2");
		identifyResponse.setFailureReason("4");

		bioDedupeService.performDedupe(rid);
	}

	*/
/**
	 * Test get file.
	 *
	 * @throws Exception
	 *             the exception
	 *//*

	@Test
	public void testGetFile() throws Exception {
		byte[] data = "1234567890".getBytes();
		Mockito.when(packetReaderService.getFile(anyString(), anyString(), anyString())).thenReturn(inputStream);
		PowerMockito.mockStatic(IOUtils.class);
		PowerMockito.when(IOUtils.class, "toByteArray", inputStream).thenReturn(data);

		byte[] fileData = bioDedupeService.getFileByRegId(registrationId);
		assertArrayEquals(fileData, data);
	}

	@Test
	public void getFileByAbisRefId() throws Exception {
		// case1 : if regId is invalid(null or empty),
		byte[] expected = "1234567890".getBytes();
		Mockito.when(packetReaderService.getFile(anyString(), anyString(), anyString())).thenReturn(inputStream);
		PowerMockito.mockStatic(IOUtils.class);
		PowerMockito.when(IOUtils.class, "toByteArray", inputStream).thenReturn(expected);

		byte[] fileData = bioDedupeService.getFileByAbisRefId(registrationId);
		assertArrayEquals("verfing if byte array returned is null for the given invalid regId ", fileData, null);

		// case2 : if regId is valid
		List<String> regIds = new ArrayList<>();
		regIds.add("10006100360000320190702102135");
		Mockito.when(packetInfoManager.getRidByReferenceId(anyString())).thenReturn(regIds);
		byte[] result = bioDedupeService.getFileByAbisRefId(registrationId);
		assertArrayEquals("verfing if byte array returned is same as expected ", result, expected);
	}

	@Test
	public void IOExceptionTest() throws Exception {

		listAppender.start();
		fooLogger.addAppender(listAppender);
		byte[] data = "1234567890".getBytes();
		Mockito.when(packetReaderService.getFile(anyString(), anyString(), anyString())).thenReturn(inputStream);
		PowerMockito.mockStatic(IOUtils.class);
		PowerMockito.when(IOUtils.class, "toByteArray", inputStream).thenThrow(new IOException());

		byte[] fileData = bioDedupeService.getFileByRegId(registrationId);
		Assertions.assertThatExceptionOfType(IOException.class);
	}
}
*/
