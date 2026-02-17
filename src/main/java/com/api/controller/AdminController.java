package com.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.api.dto.UserDTO;
import com.api.model.User;
import com.api.service.UserService;

@RestController
@RequestMapping("api/v1/admin")
public class AdminController {

	@Autowired
	private UserService userService;

	private Logger logger = LoggerFactory.getLogger(AdminController.class);

	@GetMapping("/getAllUsers")
	public ResponseEntity<List<?>> getUsers() {
		logger.info("Inside get all users method");
		List<UserDTO> users = userService.getAllUsers();
		if (!users.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(users);
		}
		throw new UsernameNotFoundException("No users found in database.");
	}

	@GetMapping("/email-test-dashboard")
	public ModelAndView emailTestDashboard() {
		ModelAndView mav = new ModelAndView("email-test-dashboard");

		List<Map<String, Object>> testCases = new ArrayList<>();
		int totalTests = 0;
		int totalFailures = 0;
		int totalErrors = 0;
		int totalSkipped = 0;

		// Scan for Surefire reports
		File reportDir = new File(System.getProperty("user.dir") + "/target/surefire-reports");
		if (reportDir.exists() && reportDir.isDirectory()) {
			File[] files = reportDir.listFiles((dir, name) -> name.endsWith(".xml") && name.startsWith("TEST-"));

			if (files != null) {
				for (File file : files) {
					try {
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						Document doc = dBuilder.parse(file);
						doc.getDocumentElement().normalize();

						Element testSuite = doc.getDocumentElement();
						totalTests += Integer.parseInt(testSuite.getAttribute("tests"));
						totalFailures += Integer.parseInt(testSuite.getAttribute("failures"));
						totalErrors += Integer.parseInt(testSuite.getAttribute("errors"));
						if (testSuite.hasAttribute("skipped")) {
							totalSkipped += Integer.parseInt(testSuite.getAttribute("skipped"));
						}

						// Parse Test Cases
						NodeList nList = doc.getElementsByTagName("testcase");
						for (int i = 0; i < nList.getLength(); i++) {
							Node nNode = nList.item(i);
							if (nNode.getNodeType() == Node.ELEMENT_NODE) {
								Element eElement = (Element) nNode;
								String testName = eElement.getAttribute("name");
								String className = eElement.getAttribute("classname");

								// Determine status
								String status = "success";
								String badgeType = "Unit"; // Default
								if (className.contains("Integration"))
									badgeType = "Integration";

								List<String> details = new ArrayList<>();
								details.add("Class: " + className);

								NodeList failureList = eElement.getElementsByTagName("failure");
								NodeList errorList = eElement.getElementsByTagName("error");

								if (failureList.getLength() > 0) {
									status = "warning"; // Failures are warnings in our UI logic? Or error? Let's use
														// warning for assertion fails
									details.add(
											"❌ Failure: " + failureList.item(0).getTextContent().trim().split("\n")[0]); // First
																															// line
																															// only
								} else if (errorList.getLength() > 0) {
									status = "error";
									details.add("❌ Error: " + errorList.item(0).getTextContent().trim().split("\n")[0]);
								} else {
									details.add("✅ Test Passed");
								}

								testCases.add(createTestCase(formatTestName(testName), badgeType, status,
										"Test method in " + className.substring(className.lastIndexOf('.') + 1),
										details));
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
						// Continue to next file
					}
				}
			}
		} else {
			// Fallback if no reports found (to avoid empty dashboard)
			mav.addObject("error", "No test reports found in target/surefire-reports. Please run 'mvn test' first.");
		}

		int totalBugs = totalFailures + totalErrors;

		// Dynamic Stats
		mav.addObject("totalEmailTypes", 8); // Still hardcoded as this is config based
		mav.addObject("totalUnitTests", totalTests);
		mav.addObject("totalIntegrationTests", 0); // We merged them into totalTests for simplicity here or could deduce
													// from naming
		mav.addObject("totalBugs", totalBugs);

		mav.addObject("testCases", testCases);
		return mav;
	}

	private String formatTestName(String camelCase) {
		// Simple humanizing: "testWelcomeEmail" -> "Welcome Email"
		String result = camelCase.replace("test", "");
		return result.replaceAll(
				String.format("%s|%s|%s",
						"(?<=[A-Z])(?=[A-Z][a-z])",
						"(?<=[^A-Z])(?=[A-Z])",
						"(?<=[A-Za-z])(?=[^A-Za-z])"),
				" ");
	}

	private Map<String, Object> createTestCase(String title, String badgeType, String status, String description,
			List<String> details) {
		Map<String, Object> testCase = new HashMap<>();
		testCase.put("title", title);
		testCase.put("badgeType", badgeType);
		testCase.put("status", status);
		testCase.put("description", description);
		testCase.put("details", details);
		return testCase;
	}

}
