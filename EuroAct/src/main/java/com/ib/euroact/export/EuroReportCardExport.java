
package com.ib.euroact.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.euroact.db.dto.EuroDoc;
import com.ib.euroact.db.dto.EuroDocLang;
import com.ib.euroact.db.dto.EuroDocProcedure;
import com.ib.euroact.db.dto.EuroDocProcedureStan;
import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.SystemData;
import com.ib.indexui.system.Constants;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.exceptions.InvalidParameterException;
import com.ib.system.exceptions.ObjectNotFoundException;



public class EuroReportCardExport extends ExportBase{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 8277807835079336261L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EuroReportCardExport.class);
    
    public EuroReportCardExport(){
    	
    }
    
   
    
    
    /**Метода изгражда html таблица, която се използва за експорт на данните ( печат на филтъра)
     * @param act       -   обект за експорт
     * @param expType   -   вид на експорта

     * @throws IOException
     * @throws ObjectNotFoundException
     */
    public void ReportEUDocExp(EuroDoc  act,  int expType)  //List<EuroDocProcedure> proceduriDateList,
        throws 
            IOException, 
            ObjectNotFoundException, 
            InvalidParameterException, 
            DbErrorException {
         
    	SystemData sd = (SystemData) getSystemData();
    	
    	StringBuffer rowData = new StringBuffer( "<table style=\""+ExportBase.styleTable9pt+ "\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\" width=\"100%\">"+
        "<tr><td align=\"right\" colspan=\"4\" style=\""+ ExportBase.styleRow10+"\"> </td></tr>");
        
            rowData.append("<tr>");
            addNewColumn(rowData,"Д О К У М Е Н Т &nbsp;&nbsp;&nbsp; Н А &nbsp;&nbsp;&nbsp;Е С",null,ExportBase.styleHeader18+"font-size: 14pt","center",null,null,"4",null);
            rowData.append("</tr><tr>");
            addNewColumn(rowData,"",null,ExportBase.styleHeader18+"font-size: 14pt","center",null,null,"4",null);
            rowData.append("</tr><tr>");
            if(act!=null){
                
                if (act.getSign()!=null && !act.getSign().trim().equals("")){
                    addNewColumn(rowData,"Сигнатура на документа:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    addNewColumn(rowData,checkString(act.getSign()),"25%",ExportBase.styleRow9,null,null,null,null,null);  
                    if (act.getStatus()!=null){
                        addNewColumn(rowData,"Статус:",null,ExportBase.styleRow9Bold,null,"top",null,null,null);  
                        addNewColumn(rowData,checkString(sd.decodeItem(EuroConstants.CODE_SYSCLASS_STATUS,act.getStatus(), getCurrentLang(), act.getStatusDate()) ),null,ExportBase.styleRow9,null,null,null,null,null);
                    }
                rowData.append("</tr><tr>");
                }else{
                        if (act.getStatus()!=null){
                        addNewColumn(rowData,"","50%",ExportBase.styleRow9Bold,null,"top",null,"2",null);
                        addNewColumn(rowData,"Статус:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                        addNewColumn(rowData,checkString(sd.decodeItem(EuroConstants.CODE_SYSCLASS_STATUS,act.getStatus(), getCurrentLang(), act.getStatusDate())),"25%",ExportBase.styleRow9,null,null,null,null,null);
                        rowData.append("</tr><tr>");
                    }
                }
                
                
                
                if (act.getDatDoc()!=null){
                    addNewColumn(rowData,"Дата на документа:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    addNewColumn(rowData,changeDateLongtoString(act.getDatDoc()),"75%",ExportBase.styleRow9,null,null,null,"3",null);  
                    rowData.append("</tr><tr>");
                }
                if (act.getDatIzpr()!=null){
                    addNewColumn(rowData,"Дата на изпращане:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    addNewColumn(rowData,changeDateChasLongtoString(act.getDatIzpr()),"25%",ExportBase.styleRow9,null,null,null,null,null);  
                    if (act.getDatPoluch()!=null){
                        addNewColumn(rowData,"Дата на получаване:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                        addNewColumn(rowData,changeDateChasLongtoString(act.getDatPoluch()),"25%",ExportBase.styleRow9,null,null,null,null,null);  
                        rowData.append("</tr><tr>");
                    }
                    
                    rowData.append("</tr><tr>");
                }else{
                    if (act.getDatPoluch()!=null){
                        addNewColumn(rowData,"","50%",ExportBase.styleRow9Bold,null,"top",null,"2",null);
                        addNewColumn(rowData,"Дата на получаване:",null,ExportBase.styleRow9Bold,null,"top",null,null,null);  
                        addNewColumn(rowData,changeDateChasLongtoString(act.getDatPoluch()),null,ExportBase.styleRow9,null,null,null,null,null);  
                        rowData.append("</tr><tr>");
                    }
                }
                
                if (act.getDosieta()!=null && act.getDosieta().size()>0){
                    addNewColumn(rowData,"Междуинституционално досие:",null,ExportBase.styleRow9Bold+"font-style:italic;",null,"top",null,"4",null);  
                    rowData.append("</tr><tr>");
                    for(int i =0 ; i < act.getDosieta().size();i++){
                    
                        addNewColumn(rowData,"","25%",ExportBase.styleRow9Bold,null,"center",null,null,null);
                        if(i==0)
                            addNewColumn(rowData,checkString(act.getDosieta().get(i).getSign()),"75%",ExportBase.styleRow9+ExportBase.styleRow+ExportBase.styleRightBorder+"border-top: #808080 1px solid;","left","center",null,"3",null);
                        else
                            addNewColumn(rowData,checkString(act.getDosieta().get(i).getSign()),"75%",ExportBase.styleRow9+ExportBase.styleRow+ExportBase.styleRightBorder,"left","center",null,"3",null);
                        rowData.append("</tr><tr>");
                    }
                }
                               
                if((act.getUid()!=null && !act.getUid().trim().equals(""))||(act.getLangs()!=null && act.getLangs().size()>0)|| (act.getNsProg()!=null && act.getNsProg().longValue() == Constants.CODE_ZNACHENIE_DA)
                   ||  (act.getMsProg()!=null && act.getMsProg().longValue() == Constants.CODE_ZNACHENIE_DA)){
                    addNewColumn(rowData,"Номер,Езици ,Годишна програма","100%",ExportBase.styleRow9Bold+"font-style:italic;",null,"top",null,"4",null);  
                    rowData.append("</tr><tr>");
                }
                    
                
                if (act.getUid()!=null && !act.getUid().trim().equals("")){
                    addNewColumn(rowData,"","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    addNewColumn(rowData,"<b>Номер на документ:</b>&nbsp;"+checkString(act.getUid()),"75%",ExportBase.styleRow9,null,"top",null,"3",null); 
                    rowData.append("</tr><tr>");
                }
                
                if (act.getLangs()!=null && act.getLangs().size()>0){
                    addNewColumn(rowData,"","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    
                    String ezici = "<b>Езици на документа:</b>&nbsp;";
                    
                    for(EuroDocLang langa:act.getLangs()){
                        ezici += " "+sd.decodeItem(EuroConstants.CODE_SYSCLASS_EZIK,langa.getLang(), getCurrentLang(), act.getDateLastMod())  +", ";
                    }
                    
                    addNewColumn(rowData,ezici,"75%",ExportBase.styleRow9,null,"top",null,"3",null);  
                    
                    rowData.append("</tr><tr>");
                }
                               
                if (act.getNsProg()!=null && act.getNsProg().longValue() == Constants.CODE_ZNACHENIE_DA){
                    addNewColumn(rowData,"<b>Включен в Годишната работна програма НС:</b> ДА","50%",ExportBase.styleRow9,null,"top",null,"2",null);
                    if (act.getNsProgNomer()!=null ){
                        if (act.getNsProgGodina()!=null)
                            addNewColumn(rowData,"<b>Номер:</b>&nbsp;"+act.getNsProgNomer()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Година:</b>&nbsp;"+act.getNsProgGodina(),"50%",ExportBase.styleRow9,null,"top",null,"2",null);  
                        else
                            addNewColumn(rowData,"<b>Номер:</b>&nbsp;"+act.getNsProgNomer(),"50%",ExportBase.styleRow9,null,"top",null,"2",null);  
                        rowData.append("</tr><tr>");
                    }else{
                            if (act.getNsProgGodina()!=null){
                            addNewColumn(rowData,"<b>Година:</b>&nbsp;"+act.getNsProgGodina(),"50%",ExportBase.styleRow9Bold,null,"top",null,"2",null);
                            rowData.append("</tr><tr>");
                            }
                    }
                }
                
                if (act.getMsProg()!=null && act.getMsProg().longValue() == Constants.CODE_ZNACHENIE_DA){
                    addNewColumn(rowData,"<b>Включен в Годишната работна програма MС:</b> ДА","50%",ExportBase.styleRow9,null,"top",null,"2",null);
                    if (act.getMsProgNomer()!=null ){
                        if (act.getMsProgGodina()!=null)
                            addNewColumn(rowData,"<b>Номер:</b>&nbsp;"+act.getMsProgNomer()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Година:</b>&nbsp;"+act.getMsProgGodina(),"50%",ExportBase.styleRow9,null,"top",null,"2",null);  
                        else
                            addNewColumn(rowData,"<b>Номер:</b>&nbsp;"+act.getMsProgNomer(),"50%",ExportBase.styleRow9,null,"top",null,"2",null);  
                        rowData.append("</tr><tr>");
                    }else{
                            if (act.getNsProgGodina()!=null){
                            addNewColumn(rowData,"<b>Година:</b>&nbsp;"+act.getMsProgGodina(),"50%",ExportBase.styleRow9Bold,null,"top",null,"2",null);
                            rowData.append("</tr><tr>");
                            }
                    } 
                }
               
                //------------- proceduri
                
                if(act.getProcedures()!= null && act.getProcedures().size()>0){
                    addNewColumn(rowData,"Работа по процедури:",null,ExportBase.styleRow9Bold+"border-top: black 1px solid;font-style:italic;",null,"top",null,"4",null);  
                    rowData.append("</tr><tr>");
                    for(int i=0 ;i<act.getProcedures().size();i++){
                    	EuroDocProcedure proc= (EuroDocProcedure)act.getProcedures().get(i);
                      //  if(proc.isSelected_()){
                           
                        	if (proc.getSrok()!=null)
                                addNewColumn(rowData,"<b>"+checkString( sd.decodeItem(EuroConstants.CODE_SYSCLASS_PROCEDURE_BG,proc.getProcedure(), getCurrentLang(), act.getDateLastMod()))+"</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Срок:</b>&nbsp;"+changeDateLongtoString(proc.getSrok()),"100%",ExportBase.styleRow9+"border-bottom: gray 1px solid;",null,"top",null,"4",null);  
                            else
                                addNewColumn(rowData,checkString(sd.decodeItem(EuroConstants.CODE_SYSCLASS_PROCEDURE_BG,proc.getProcedure(), getCurrentLang(), act.getDateLastMod())),"100%",ExportBase.styleRow9Bold+"border-bottom: gray 1px solid;",null,"top",null,"4",null);  

                               
                            rowData.append("</tr><tr>");
                            Boolean check_stan=false;
                            for(int k = 0; k<proc.getStanovista().size();k++){
                                
                            	EuroDocProcedureStan stan= proc.getStanovista().get(k);
                                if(check_stan){
                                    if((stan.getRnDoc()!=null &&!stan.getRnDoc().equals(""))||stan.getStanoviste()!=null){
                                        addNewColumn(rowData,"","25%",ExportBase.styleRow9Bold,null,"center",null,null,null);
                                        addNewColumn(rowData,checkString(stan.getRnDoc())+changeDateLongtoString("от",stan.getStanoviste()),"25%",ExportBase.styleRow9+ExportBase.styleRow+ExportBase.styleRightBorder,"left","center",null,null,null);
                                        rowData.append("</tr><tr>");
                                    }
                                }else{
                                    if((stan.getRnDoc()!=null &&!stan.getRnDoc().equals(""))||stan.getStanoviste()!=null){
                                        addNewColumn(rowData,"Становище номер:","25%",ExportBase.styleRow9Bold,null,"ceneter",null,null,null);
                                        addNewColumn(rowData,checkString(stan.getRnDoc())+changeDateLongtoString("от",stan.getStanoviste()),"25%",ExportBase.styleRow9+ExportBase.styleRow+ExportBase.styleRightBorder,"left","center",null,null,null);
                                        check_stan = true;
                                        rowData.append("</tr><tr>");
                                    }
                                }
                            }                         
                       // }
                    } 
                }
                
                
                //------------------------------ kraj na procedurite -------------------------------------------------------------
                
                if (act.getPoluchenOt()!=null ){
                    addNewColumn(rowData,"Получен от:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    addNewColumn(rowData,checkString(sd.decodeItem(EuroConstants.CODE_SYSCLASS_POLUCHEN_OT,act.getPoluchenOt(), getCurrentLang(), act.getDateLastMod())),"25%",ExportBase.styleRow9,null,null,null,null,null);  
                    if (act.getAvtor()!=null ){
                        addNewColumn(rowData,"Автор:",null,ExportBase.styleRow9Bold,null,"top",null,null,null);  
                        addNewColumn(rowData,checkString(sd.decodeItem(EuroConstants.CODE_SYSCLASS_AVTOR,act.getAvtor(), getCurrentLang(), act.getDateLastMod())),null,ExportBase.styleRow9,null,null,null,null,null);
                    }
                    rowData.append("</tr><tr>");
                }else{
                        if (act.getAvtor()!=null ){
                            addNewColumn(rowData,"","50%",ExportBase.styleRow9Bold,null,"top",null,"2",null);
                            addNewColumn(rowData,"Автор:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                            addNewColumn(rowData,checkString(sd.decodeItem(EuroConstants.CODE_SYSCLASS_AVTOR,act.getAvtor(), getCurrentLang(), act.getDateLastMod())),"25%",ExportBase.styleRow9,null,null,null,null,null);
                            rowData.append("</tr><tr>");
                        }
                }
                
                if (act.getSluj()!=null){
                    addNewColumn(rowData,"Служител:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    addNewColumn(rowData,checkString(sd.decodeItem(EuroConstants.CODE_SYSCLASS_AVTOR,act.getSluj(), getCurrentLang(), act.getDateLastMod())),"75%",ExportBase.styleRow9,null,null,null,"3",null);
                    rowData.append("</tr><tr>");
                }
                if (act.getVidDosie()!=null){
                    addNewColumn(rowData,"Вид досие:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    addNewColumn(rowData,checkString(sd.decodeItem(EuroConstants.CODE_SYSCLASS_DOSIE,act.getVidDosie(), getCurrentLang(), act.getDateLastMod())),"75%",ExportBase.styleRow9,null,null,null,"3",null);
                    rowData.append("</tr><tr>");
                }
                if (act.getZaglBg()!=null && !act.getZaglBg().trim().equals("")){
                    addNewColumn(rowData,"Наименование на акта - български:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    addNewColumn(rowData,checkString(act.getZaglBg()),"75%",ExportBase.styleRow9,"justify",null,null,"3",null);
                    rowData.append("</tr><tr>");
                }
                if (act.getZaglEn()!=null && !act.getZaglEn().trim().equals("")){
                    addNewColumn(rowData,"Наименование на акта - английски:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    addNewColumn(rowData,checkString(act.getZaglEn()),"75%",ExportBase.styleRow9,"justify",null,null,"3",null);
                    rowData.append("</tr><tr>");
                }
                
                if (act.getTemaNS()!=null && act.getTemaNS().size()>0){
                    addNewColumn(rowData,"Тематична област на НС:",null,ExportBase.styleRow9Bold,null,"top",null,"4",null);  
                    rowData.append("</tr><tr>");
                    for(int i =0 ; i < act.getTemaNS().size();i++){
                        addNewColumn(rowData,"","25%",ExportBase.styleRow9Bold,null,"center",null,null,null);
                        if(i==0)
                            addNewColumn(rowData,checkString(sd.decodeItem(EuroConstants.CODE_SYSCLASS_TEMATIKA_NS,act.getTemaNS().get(i).getTema(), getCurrentLang(), act.getDateLastMod())),"75%",ExportBase.styleRow9+ExportBase.styleRow+ExportBase.styleRightBorder+"border-top: #808080 1px solid;","left","center",null,"3",null);
                        else
                        addNewColumn(rowData,checkString(sd.decodeItem(EuroConstants.CODE_SYSCLASS_TEMATIKA_NS,act.getTemaNS().get(i).getTema(), getCurrentLang(), act.getDateLastMod())),"75%",ExportBase.styleRow9+ExportBase.styleRow+ExportBase.styleRightBorder,"left","center",null,"3",null);
                        rowData.append("</tr><tr>");
                    }
                }
                
                if (act.getTemaEK()!=null && act.getTemaEK().size()>0){
                    addNewColumn(rowData,"Тематична област на ЕК:",null,ExportBase.styleRow9Bold,null,"top",null,"4",null);  
                    rowData.append("</tr><tr>");
                    for(int i =0 ; i < act.getTemaEK().size();i++){
                        addNewColumn(rowData,"","25%",ExportBase.styleRow9Bold,null,"center",null,null,null);
                        if(i==0)
                            addNewColumn(rowData,checkString(sd.decodeItem(EuroConstants.CODE_SYSCLASS_TEMATIKA_EK,act.getTemaEK().get(i).getTema(), getCurrentLang(), act.getDateLastMod())),"75%",ExportBase.styleRow9+ExportBase.styleRow+ExportBase.styleRightBorder+"border-top: #808080 1px solid;","left","center",null,"3",null);
                        else
                        addNewColumn(rowData,checkString(sd.decodeItem(EuroConstants.CODE_SYSCLASS_TEMATIKA_EK,act.getTemaEK().get(i).getTema(), getCurrentLang(), act.getDateLastMod())),"75%",ExportBase.styleRow9+ExportBase.styleRow+ExportBase.styleRightBorder,"left","center",null,"3",null);
                        rowData.append("</tr><tr>");
                    }
                }
                
                if (act.getEtapES()!=null && !act.getEtapES().trim().equals("")){
                    addNewColumn(rowData,"Етап на приемане на проекта в Съвета на ЕС:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    addNewColumn(rowData,checkString(act.getEtapES()),"75%",ExportBase.styleRow9,"justify",null,null,"3",null);
                    rowData.append("</tr><tr>");
                }
                
                if (act.getEtapEP()!=null && !act.getEtapEP().trim().equals("")){
                    addNewColumn(rowData,"Етап на приемане на проекта в Европейския парламент:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    addNewColumn(rowData,checkString(act.getEtapEP()),"75%",ExportBase.styleRow9,"justify",null,null,"3",null);
                    rowData.append("</tr><tr>");
                }
                
                if (act.getPredstoiasti()!=null && !act.getPredstoiasti().trim().equals("")){
                    addNewColumn(rowData,"Предстоящи събития:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    addNewColumn(rowData,checkString(act.getPredstoiasti()),"75%",ExportBase.styleRow9,"justify",null,null,"3",null);
                    rowData.append("</tr><tr>");
                }
            
                if (act.getComment()!=null && !act.getComment().trim().equals("")){
                    addNewColumn(rowData,"Коментар:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    addNewColumn(rowData,checkString(act.getComment()),"75%",ExportBase.styleRow9,"justify",null,null,"3",null);
                    rowData.append("</tr><tr>");
                }
                
                if( (act.getPriemaneCelex()!=null && !act.getPriemaneCelex().trim().equals(""))||
                    (act.getPriemaneIme()!=null && !act.getPriemaneIme().trim().equals("")) ||
                    (act.getPriemaneBroi()!=null && !act.getPriemaneBroi().trim().equals("")) || (act.getPriemaneDate()!=null) ||
                    (act.getPriemaneUrl()!=null && !act.getPriemaneUrl().trim().equals(""))
                    
                    ){
                    addNewColumn(rowData,"Приемане на акта от европейските институции:","100%",ExportBase.styleRow9Bold+"font-style:italic;",null,"top",null,"4",null);  
                    rowData.append("</tr><tr>");
                }
                
                if (act.getPriemaneCelex()!=null && !act.getPriemaneCelex().trim().equals("")){
                    addNewColumn(rowData,"Номер CELEX:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    addNewColumn(rowData,checkString(act.getPriemaneCelex()),"75%",ExportBase.styleRow9,null,null,null,"3",null);
                    rowData.append("</tr><tr>");
                }
                if (act.getPriemaneIme()!=null && !act.getPriemaneIme().trim().equals("")){
                    addNewColumn(rowData,"Наименование:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    addNewColumn(rowData,checkString(act.getPriemaneIme()),"75%",ExportBase.styleRow9,"justify",null,null,"3",null);
                    rowData.append("</tr><tr>");
                }
                if (act.getPriemaneBroi()!=null && !act.getPriemaneBroi().trim().equals("")){
                    if (act.getPriemaneDate()!=null)
                        addNewColumn(rowData,"<b>Обнародван в Официален вестник брой:</b>&nbsp;"+act.getPriemaneBroi()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>дата:</b>&nbsp;"
                                                +changeDateLongtoString(act.getPriemaneDate()),"100%",ExportBase.styleRow9,null,"top",null,"4",null);  
                    else
                        addNewColumn(rowData,"<b>Обнародван в Официален вестник брой:</b>&nbsp;"+act.getPriemaneBroi(),"100%",ExportBase.styleRow9,null,"top",null,"4",null);  
                        rowData.append("</tr><tr>");
                }else{
                        if (act.getPriemaneDate()!=null){
                            addNewColumn(rowData,"<b>дата:</b>&nbsp;"+changeDateLongtoString(act.getPriemaneDate()),"100%",ExportBase.styleRow9Bold,null,"top",null,"4",null);
                            rowData.append("</tr><tr>");
                        }
                }
                if (act.getPriemaneUrl()!=null && !act.getPriemaneUrl().trim().equals("")){
                    addNewColumn(rowData,"URL ЕurLex:","25%",ExportBase.styleRow9Bold,null,"top",null,null,null);  
                    addNewColumn(rowData,checkString(act.getPriemaneUrl()),"75%",ExportBase.styleRow9,null,null,null,"3",null);
                    rowData.append("</tr><tr>");
                }
                
                if (act.getVraz()!=null && act.getVraz().size()>0){
                    addNewColumn(rowData,"Връзки с други документи на ЕС:",null,ExportBase.styleRow9Bold+"font-style:italic;",null,"top",null,"4",null);  
                    rowData.append("</tr><tr>");
                    for(int i =0 ; i < act.getVraz().size();i++){
                    
                        addNewColumn(rowData,"","25%",ExportBase.styleRow9Bold,null,"center",null,null,null);
                        if(i==0)
                            addNewColumn(rowData,checkString(act.getVraz().get(i).getSignVraz()),"75%",ExportBase.styleRow9+ExportBase.styleRow+ExportBase.styleRightBorder+"border-top: #808080 1px solid;","left","center",null,"3",null);
                        else
                            addNewColumn(rowData,checkString(act.getVraz().get(i).getSignVraz()),"75%",ExportBase.styleRow9+ExportBase.styleRow+ExportBase.styleRightBorder,"left","center",null,"3",null);
                        rowData.append("</tr><tr>");
                    }
                }
                
//                if (act.getFiles()!=null && act.getFiles().size()>0){
//                    addNewColumn(rowData,"Връзки с други документи на ЕС:",null,this.styleRow9Bold+"font-style:italic;",null,"top",null,"4",null);  
//                    rowData.append("</tr><tr>");
//                    for(int i =0 ; i < act.getFiles().size();i++){
//                    
//                        addNewColumn(rowData,"","25%",this.styleRow9Bold,null,"center",null,null,null);
//                        if(i==0)
//                            addNewColumn(rowData,(i+1)+".&nbsp;"+checkString(act.getFiles().get(i).getFileName()),"75%",this.styleRow9+this.styleRow+this.styleRightBorder+"border-top: #808080 1px solid;","left","center",null,"3",null);
//                        else
//                            addNewColumn(rowData,(i+1)+".&nbsp;"+checkString(act.getFiles().get(i).getFileName()),"75%",this.styleRow9+this.styleRow+this.styleRightBorder,"left","center",null,"3",null);
//                        rowData.append("</tr><tr>");
//                    }
//                }
                
            } 
            
            
	    rowData.append("</tr></table>");
	    
	    //----------------------------------------------------------------------------
	    
	    String fileName = ""  ;
        
        String contentType = null;
        
        
        switch (expType){
            case 1:
                contentType = "text/html; charset=UTF-8";
                fileName = "ReportEUDocExp.html"; 
                break;
        
            case 2:      
                contentType = "application/vnd.ms-word;  charset=UTF-8";
                fileName = "ReportEUDocExp.doc"; 
                break;
             
            
        }
        
//        FacesContext ctx = FacesContext.getCurrentInstance();
//        FacesContext fContext = FacesContext.getCurrentInstance();
//        
//        HttpServletResponse response =(HttpServletResponse)fContext.getExternalContext(). getResponse();              
//        ServletOutputStream out = response.getOutputStream();    
//        
//        OutputStreamWriter osw = new OutputStreamWriter(out, Charset.forName("UTF-8"));
//        
//        response.setContentType(contentType);            
//        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
//        
//        String tmpString =  "<html><meta http-equiv=\"Content-Type\" " + 
//                            "content=\""+ contentType + "\"></meta> " + 
//                            "<style> td { mso-number-format:\"\\@\";} </style>";      
//        osw.write(tmpString);
//	    
//	    
//	    osw.write(rowData.toString()); 
//	    
//	    tmpString = "</body></html>";
//    
//	    try{
//		    osw.write(tmpString.toString());
//		    osw.flush();
//		    osw.close();
//		    out.flush();
//		    fContext.responseComplete();
//	    } catch (IOException e){
//	    	System.out.println(e.getMessage());
//	    }
//	    ctx.getApplication().getStateManager().saveSerializedView(ctx);
//	   
//	    ctx.responseComplete();
        
        try {
	        FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			
			
			HttpServletResponse response = (HttpServletResponse)externalContext.getResponse();
			ServletOutputStream out = response.getOutputStream();    
			OutputStreamWriter osw = new OutputStreamWriter(out, Charset.forName("UTF-8"));
			
			response.setContentType(contentType);            
	        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
	        
	        osw.write("<html><meta http-equiv=\"Content-Type\" " + 
	                "content=\""+ contentType + "\"></meta> " + 
	                "<style> td { mso-number-format:\"\\@\";} </style>");
	        osw.write(rowData.toString()); 
	        osw.write("</body></html>");
	        
	        
	        osw.flush();
		    osw.close();
		    out.flush();
		    
			facesContext.responseComplete();
			
        } catch (Exception e) {
			LOGGER.error("Exception: " + e.getMessage(), e);
		}
        
    }
   
   
    
}