package com.aryasindhu.queue.test;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class TestNGRunner {

	public static void main(String[] args) {
		TestNG testNG = new TestNG();

		XmlSuite suite = new XmlSuite();
		XmlTest test = new XmlTest(suite);
		test.setName("Queue Tests : Using Apache Kafka");
		List<XmlClass> testClasses = new ArrayList<XmlClass>();
		testClasses.add(new XmlClass(KafkaProducerTest.class));
		testClasses.add(new XmlClass(KafkaConsumerTest.class));
		test.setXmlClasses(testClasses);

		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		suites.add(suite);

		testNG.setXmlSuites(suites);

		@SuppressWarnings("rawtypes")
		List<Class> classes = new ArrayList<Class>();
		classes.add(org.uncommons.reportng.HTMLReporter.class);
		classes.add(org.uncommons.reportng.JUnitXMLReporter.class);
		testNG.setListenerClasses(classes);

		testNG.run();
	}

}
