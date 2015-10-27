

/* First created by JCasGen Fri Apr 03 14:54:33 JST 2015 */
package com.trang.uima.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Apr 03 15:54:21 JST 2015
 * XML source: C:/Trangmx/Projects/LG_UIMA/jp.go.nict.langrid.webapps.composite/descriptors/typeSystemDescriptor.xml
 * @generated */
public class Target extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Target.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Target() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Target(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Target(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Target(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: FromLang

  /** getter for FromLang - gets 
   * @generated
   * @return value of the feature 
   */
  public String getFromLang() {
    if (Target_Type.featOkTst && ((Target_Type)jcasType).casFeat_FromLang == null)
      jcasType.jcas.throwFeatMissing("FromLang", "com.trang.uima.types.Target");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Target_Type)jcasType).casFeatCode_FromLang);}
    
  /** setter for FromLang - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setFromLang(String v) {
    if (Target_Type.featOkTst && ((Target_Type)jcasType).casFeat_FromLang == null)
      jcasType.jcas.throwFeatMissing("FromLang", "com.trang.uima.types.Target");
    jcasType.ll_cas.ll_setStringValue(addr, ((Target_Type)jcasType).casFeatCode_FromLang, v);}    
   
    
  //*--------------*
  //* Feature: ToLang

  /** getter for ToLang - gets 
   * @generated
   * @return value of the feature 
   */
  public String getToLang() {
    if (Target_Type.featOkTst && ((Target_Type)jcasType).casFeat_ToLang == null)
      jcasType.jcas.throwFeatMissing("ToLang", "com.trang.uima.types.Target");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Target_Type)jcasType).casFeatCode_ToLang);}
    
  /** setter for ToLang - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setToLang(String v) {
    if (Target_Type.featOkTst && ((Target_Type)jcasType).casFeat_ToLang == null)
      jcasType.jcas.throwFeatMissing("ToLang", "com.trang.uima.types.Target");
    jcasType.ll_cas.ll_setStringValue(addr, ((Target_Type)jcasType).casFeatCode_ToLang, v);}    
   
    
  //*--------------*
  //* Feature: Content

  /** getter for Content - gets 
   * @generated
   * @return value of the feature 
   */
  public String getContent() {
    if (Target_Type.featOkTst && ((Target_Type)jcasType).casFeat_Content == null)
      jcasType.jcas.throwFeatMissing("Content", "com.trang.uima.types.Target");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Target_Type)jcasType).casFeatCode_Content);}
    
  /** setter for Content - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setContent(String v) {
    if (Target_Type.featOkTst && ((Target_Type)jcasType).casFeat_Content == null)
      jcasType.jcas.throwFeatMissing("Content", "com.trang.uima.types.Target");
    jcasType.ll_cas.ll_setStringValue(addr, ((Target_Type)jcasType).casFeatCode_Content, v);}    
  }

    