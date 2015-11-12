package com.trang.uima.pipeline;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.composite.uima.ComponentServiceFactoryHolder;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;

import org.apache.uima.aae.client.UimaAsBaseCallbackListener;
import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.adapter.jms.client.BaseUIMAAsynchronousEngine_impl;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;

/**
 * A sample UIMA application. It creates a UIMA analysis engine, and provides a
 * method for using it to process text.
 * 
 * This sample creates an asynchronous UIMA analysis engine.
 * 
 * @author
 */
public class SegmenterAndBackTranslationPipeline {

	/** The pipeline created to process text. */
	private UimaAsynchronousEngine uimaAsEngine = null;

	/**
	 * Creates an asynchronous analysis engine.
	 */
	public SegmenterAndBackTranslationPipeline(
			UimaAsBaseCallbackListener callback, String deployFile)
			throws Exception {
		System.out.println("Running UIMA pipeline with UIMA AS");
		System.out.println("==============================================");

		// creating UIMA analysis engine
		uimaAsEngine = new BaseUIMAAsynchronousEngine_impl();

		// preparing map for use in deploying services
		Map<String, Object> deployCtx = new HashMap<String, Object>();
		deployCtx.put(UimaAsynchronousEngine.DD2SpringXsltFilePath,
				System.getenv("UIMA_HOME") + "/bin/dd2spring.xsl");
		deployCtx.put(UimaAsynchronousEngine.SaxonClasspath,
				"file:" + System.getenv("UIMA_HOME") + "/saxon/saxon8.jar");

		// System.out.println("Deploying UIMA services");
		uimaAsEngine.deploy(deployFile, deployCtx);

		// // creating aggregate analysis engine
		// System.out.println("Deploying analysis engine");
		// uimaAsEngine.deploy("./conf/deploy.xml", deployCtx);

		// add callback listener that will be informed when processing completes
		uimaAsEngine.addStatusCallbackListener(callback);

		// preparing map for use in a UIMA client for submitting text to process
		System.out.println("Initialising UIMA client");
		deployCtx
				.put(UimaAsynchronousEngine.ServerUri, "tcp://localhost:61616");
		deployCtx.put(UimaAsynchronousEngine.ENDPOINT,
				"SegmenterAndBackTranslationQueue");
		// deployCtx.put(UimaAsynchronousEngine.CasPoolSize, 15);
		uimaAsEngine.initialize(deployCtx);
	}
	

	/**
	 * Uses the UIMA analysis engine to process the provided document text.
	 */
	public void backTranslate(String sourceLang, String targetLang, String text)
			throws CASException, Exception {
		CAS cas = uimaAsEngine.getCAS();
		cas.setDocumentText(text);
		cas.setDocumentLanguage(sourceLang + "-" + targetLang);
		uimaAsEngine.sendCAS(cas);
		uimaAsEngine.collectionProcessingComplete();
		ComponentServiceFactoryHolder.remove();
	}
}