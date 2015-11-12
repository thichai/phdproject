package com.trang.uima.compositeservice;

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

import org.apache.log4j.Logger;
import org.apache.uima.aae.client.UimaAsBaseCallbackListener;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.jcas.JCas;

import com.trang.uima.langrid.ComponentServiceFactoryHolder;
import com.trang.uima.pipeline.SegmenterAndBackTranslationPipeline;
import com.trang.uima.types.IntermediateResult;
import com.trang.uima.types.Target;

public class UIMAASBackTranslation extends AbstractCompositeService implements
		BackTranslationService {
	public void setDescriptor(String description) {
		this.description = description;
	}

	public UIMAASBackTranslation() {
		org.apache.log4j.BasicConfigurator.configure();
	}

	private String getFile(String serviceName) {
		// TODO Auto-generated method stub
		return "C:/Users/Trang/Documents/GitHub/phdproject/"
				+ "jp.go.nict.langrid.webapps.composite/"
				+ "descriptors/deploy/";
	}

	private static final Logger LOGGER = Logger
			.getLogger(UIMAASBackTranslation.class);
	private String description;

	private static long before = -1;
	private int casID = 0;
	private String intermediateresult = "";
	private String targetResult = "";
	
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
			
			ComponentServiceFactoryHolder.set(getComponentServiceFactory());
			
			UimaAsBaseCallbackListener asyncListener = new UimaAsBaseCallbackListener() {
				/**
				 * This will be called once the text is processed.
				 */
				@Override
				public void entityProcessComplete(CAS output,
						EntityProcessStatus aStatus) {
					// record the time that this was complete
					long after = System.currentTimeMillis();
					casID += 1;

					// display the time spent processing the text
					System.out.println("   Process " + casID + " CASes in "
							+ (after - before));

					String interstr = getIntermediateResult(output);
					String interstrn = interstr + "\n";
					intermediateresult += interstrn;

					String targetstr = getTargetResult(output);
					String targetstrn = targetstr + "\n";
					targetResult += targetstrn;
				}

				private String getIntermediateResult(CAS output) {
					FSIterator iter;
					String intermediateOut = "";
					try {
						iter = output.getJCas().getAnnotationIndex(IntermediateResult.type)
								.iterator();
						while (iter.isValid()) {
							FeatureStructure fs = iter.get();
							IntermediateResult transText = (IntermediateResult) fs;
							intermediateOut = transText.getContent();
							iter.moveToNext();
						}
					} catch (CASRuntimeException | CASException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return intermediateOut;
				}

				private String getTargetResult(CAS output) {
					FSIterator iter;
					String strOut = "";
					try {
						iter = output.getJCas().getAnnotationIndex(Target.type)
								.iterator();
						while (iter.isValid()) {
							FeatureStructure fs = iter.get();
							Target transText = (Target) fs;
							strOut = transText.getContent();
							iter.moveToNext();
						}
					} catch (CASRuntimeException | CASException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return strOut;
				}

			};
			
			String deployfile = getFile("UIMAASBackTranslation") + "Deploy_SegmenterAndBackTranslationAE.xml";
			
			SegmenterAndBackTranslationPipeline uimaPipeline;
			try {
				uimaPipeline = new SegmenterAndBackTranslationPipeline(
						asyncListener, deployfile);

				casID = 0;
				System.out.println("Processing document...");
				before = System.currentTimeMillis();

				uimaPipeline.backTranslate(sourceLang, intermediateLang, source);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

			BackTranslationResult ret = new BackTranslationResult(
					intermediateresult, targetResult);

			ComponentServiceFactoryHolder.remove();
			
			return ret;
			
		}
	}
}
