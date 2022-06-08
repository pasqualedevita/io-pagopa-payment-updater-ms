package it.gov.pagopa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import it.gov.pagopa.paymentupdater.dto.PaymentMessage;
import it.gov.pagopa.paymentupdater.dto.avro.MessageContentType;
import it.gov.pagopa.paymentupdater.model.Reminder;
import it.gov.pagopa.paymentupdater.repository.PaymentRepository;
import it.gov.pagopa.paymentupdater.service.PaymentServiceImpl;


public class AbstractTest {

	private static final String EMPTY = "empty";
	private static final String FULL = "full";
	private static final String NULL = "null";

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();

	@MockBean
	protected RestTemplate restTemplate;

	@MockBean
	protected PaymentRepository mockRepository;

	@InjectMocks
	protected PaymentServiceImpl service;

	protected void mockSaveWithResponse(Reminder returnReminder) {
		Mockito.when(mockRepository.save(Mockito.any(Reminder.class))).thenReturn(returnReminder);
	}

	protected void mockFindIdWithResponse(Reminder returnReminder1) {
		Mockito.when(mockRepository.findById(Mockito.anyString())).thenReturn(Optional.of(returnReminder1));
	}

	public void mockGetPaymentByNoticeNumberAndFiscalCodeWithResponse(Reminder reminder) {
		Mockito.when(mockRepository.getPaymentByNoticeNumberAndFiscalCode(Mockito.anyString(), Mockito.anyString())).thenReturn(reminder);
	}

	protected List<Reminder>  selectListReminderMockObject(String type) {
		List<Reminder> retList = null;
		Reminder returnReminder1 = null;

		switch (type){
		case EMPTY:
			retList = new ArrayList<Reminder>();
			break;
		case FULL:
			retList = new ArrayList<Reminder>();
			returnReminder1 = selectReminderMockObject(type, "1","GENERIC","AAABBB77Y66A444A",3);
			retList.add(returnReminder1);
			returnReminder1 = selectReminderMockObject(type, "2","PAYMENT","CCCDDD77Y66A444A",3);
			retList.add(returnReminder1);
			break;
		case NULL:
			retList = null;
			break;
		default:
			retList = new ArrayList<Reminder>();
			break;
		};

		return retList;

	}

	protected Reminder selectReminderMockObject(String type, String id, String contentType, String fiscalCode, int numReminder) {
		Reminder returnReminder1 = null;

		switch (type){
		case EMPTY:
			returnReminder1 = new Reminder();
		default:
			returnReminder1 = new Reminder();
			returnReminder1.setId(id);
			returnReminder1.setContent_type(MessageContentType.valueOf(contentType));
			returnReminder1.setFiscal_code(fiscalCode);
			returnReminder1.setContent_paymentData_dueDate(LocalDate.now());
		};

		return returnReminder1;

	}

	protected PaymentMessage selectPaymentMessageObject(String type, String noticeNumber, String payeeFiscalCode, boolean paid, LocalDate dueDate, double amount, String source) {
		PaymentMessage paymentMessage = null;

		switch (type){
			case EMPTY:
				paymentMessage = new PaymentMessage();
			default:
				paymentMessage = new PaymentMessage(noticeNumber, payeeFiscalCode, paid, dueDate, amount, source);
		};

		return paymentMessage;
	}

	protected PaymentMessage getPaymentMessage(String noticeNumber, String fiscalCode, boolean paid, LocalDate d, Double amount, String source) {

		PaymentMessage pm = new PaymentMessage(noticeNumber, fiscalCode, paid, d, amount, source);
		return pm;
	}

	protected void before() {
		service = new PaymentServiceImpl();
	}

}
