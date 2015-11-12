package com.trang.uima.atomicservice;

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
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

import org.apache.uima.aae.client.UimaAsBaseCallbackListener;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.collection.EntityProcessStatus;

import com.trang.uima.pipeline.SegmenterAndEchoTranslationPipeline;
import com.trang.uima.types.Target;

public class UIMAASTranslationService implements TranslationService {

	private static String deployfile = "C:/Users/Trang/OneDrive/Documents/Phd Research/Topic 3/Source code/"
			+ "jp.go.nict.langrid.webapps.composite/descriptors/deploy/"
			+ "Deploy_SegmenterAndEchoTranslation.xml";
	private static long before = -1;
	private int casID = 0;
	private String result = "";

	@Override
	public String translate(String sourceLang, String targetLang, String source)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguagePairNotUniquelyDecidedException,
			NoAccessPermissionException, ProcessFailedException,
			NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException {
		// TODO Auto-generated method stub

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

				String str = getResult(output);
				String strn = str + "\n";
				result += strn;

			}

			private String getResult(CAS output) {
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

		// constructs a class to create and run a UIMA pipeline
		SegmenterAndEchoTranslationPipeline uimaPipeline;
		try {
			uimaPipeline = new SegmenterAndEchoTranslationPipeline(
					asyncListener, deployfile);

			casID = 0;
			System.out.println("Processing document...");
			before = System.currentTimeMillis();

			uimaPipeline.translate(sourceLang, targetLang, source);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
