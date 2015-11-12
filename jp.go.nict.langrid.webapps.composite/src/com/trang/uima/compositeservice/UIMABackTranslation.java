package com.trang.uima.compositeservice;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationResult;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationService;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;
import jp.ishidalab.service.uima.types.TranslationText;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.analysis_engine.CasIterator;
import org.apache.uima.analysis_engine.JCasIterator;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.examples.PrintAnnotations;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JFSIndexRepository;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.FileUtils;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLSerializer;
import org.xml.sax.SAXException;

import com.trang.uima.langrid.ComponentServiceFactoryHolder;
import com.trang.uima.types.IntermediateResult;
import com.trang.uima.types.Target;

public class UIMABackTranslation extends AbstractCompositeService implements
		BackTranslationService {
	public void setDescriptor(String description) {
		this.description = description;
	}

	public UIMABackTranslation() {
		org.apache.log4j.BasicConfigurator.configure();
	}

	private String getFile(String serviceName) {
		// TODO Auto-generated method stub
		return "C:/Users/Trang/Documents/GitHub/phdproject/"
				+ "jp.go.nict.langrid.webapps.composite"
				+ "/descriptors/analysisengine/";
	}

	private static final Logger LOGGER = Logger
			.getLogger(UIMABackTranslation.class);
	private String description;

	@Override
	public BackTranslationResult backTranslate(String sourceLang,
			String intermediateLang, String source)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguagePairNotUniquelyDecidedException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException {
		synchronized (BackTranslationService.class) {
			try {
				XMLInputSource in = new XMLInputSource(
						getFile("UIMABackTranslation")
								+ "BackTranslationDescriptor.xml");
				ResourceSpecifier specifier = UIMAFramework.getXMLParser()
						.parseResourceSpecifier(in);
				AnalysisEngine ae = UIMAFramework
						.produceAnalysisEngine(specifier);
				
				try {
					JCas jcas = ae.newJCas();
					jcas.setDocumentText(source);
					jcas.setDocumentLanguage(sourceLang + "-"
							+ intermediateLang);
					ComponentServiceFactoryHolder
							.set(getComponentServiceFactory());					

					// Run CAS
					 try {
					 ae.process(jcas);
					 } finally {
					 ComponentServiceFactoryHolder.remove();
					 }
					 
					BackTranslationResult result = getResult(jcas);
					return result;

				} finally {
					ae.destroy();
				}
			} catch (IOException e) {
				LOGGER.log(Level.ERROR, e);
				throw new ProcessFailedException(e);

			} catch (InvalidXMLException e) {
				LOGGER.log(Level.ERROR, e);
				throw new ProcessFailedException(e);
			} catch (ResourceInitializationException e) {
				LOGGER.log(Level.ERROR, e);
				throw new ProcessFailedException(e);
			} catch (AnalysisEngineProcessException e) {
				LOGGER.log(Level.ERROR, e);
				throw new ProcessFailedException(e);
			}
		}
	}

	private BackTranslationResult getResult(JCas jcas) {
		String interResult = "";
		String targetResult = "";
		FSIterator iter1;
		FSIterator iter2;
		try {
			iter1 = jcas.getAnnotationIndex(IntermediateResult.type)
					.iterator();
			iter2 = jcas.getAnnotationIndex(Target.type)
					.iterator();
			while (iter1.isValid() && iter2.isValid()) {
				FeatureStructure fs1 = iter1.get();
				IntermediateResult interText = (IntermediateResult) fs1;
				interResult = interText.getContent();
				FeatureStructure fs2 = iter2.get();
				Target targetText = (Target) fs2;
				targetResult = targetText.getContent();
				iter1.moveToNext();
				iter2.moveToNext();
			}
		} catch (CASRuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BackTranslationResult ret = new BackTranslationResult(interResult,
				targetResult);
		return ret;
	}

}
