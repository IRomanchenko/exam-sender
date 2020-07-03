package com.iromanchenko.exam.sender;

import com.iromanchenko.exam.sender.beans.Mail;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class MessagingServiceApplication {

	public static void main(String[] args) throws Exception {
		String sendGridApiKey = "";
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "Bearer " + sendGridApiKey);
		httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		Base64.Encoder encoder = Base64.getEncoder();

		Workbook workbook = new XSSFWorkbook("inputs-test.xlsx");
		Sheet datatypeSheet = workbook.getSheetAt(0);
		for (Row row : datatypeSheet) {
			if (row.getCell(1) != null && !"".equals(row.getCell(1).getStringCellValue())) {

				String email = row.getCell(1).getStringCellValue();
				String filePath = row.getCell(2).getStringCellValue();

				Path path = Path.of(filePath);
				byte[] fileBase64 = encoder.encode(Files.readAllBytes(path));

				Mail mail = new Mail(
						new Mail.Email("Олександр Мінарченко", "minar@ua.fm"),
						"Іспит",
						List.of(new Mail.Personalization(List.of(new Mail.Email(null, email)))),
						List.of(new Mail.Content("text/plain", "Ви маєте надіслати заповнену форму впродовж 30 хвилин")),
						List.of(new Mail.Attachment(new String(fileBase64), path.getFileName().toString())));

				RestTemplate restTemplate = new RestTemplate();
				HttpEntity<Mail> entity = new HttpEntity<>(mail, httpHeaders);
				restTemplate.postForObject("https://api.sendgrid.com/v3/mail/send", entity, Void.class);
			}
		}
		workbook.close();
	}
}
