package com.ib.euroact.parsers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.xml.datatype.XMLGregorianCalendar;


import com.ib.euroact.db.dto.EuroDocLang;
import com.ib.euroact.db.dao.EuroDocDAO;
import com.ib.euroact.db.dto.EuroDoc;
import com.ib.euroact.db.dto.EuroDocDosie;
import com.ib.euroact.db.dto.EuroDocVraz;
import com.ib.euroact.system.EuroConstants;
import com.ib.euroact.system.SystemData;
import com.ib.system.ActiveUser;
import com.ib.system.db.dto.SystemClassif;
import com.ib.system.exceptions.DbErrorException;
import com.ib.system.exceptions.InvalidParameterException;
import com.ib.system.exceptions.ObjectNotFoundException;
import com.ib.system.exceptions.UnexpectedResultException;

import eu2.*;




public class ActParser {
    
   
    public ActParser() {
    }


    
    public EuroDoc parseAct ( TransmissionRequestType trans, SystemData sd) throws DbErrorException, 
                                                  ObjectNotFoundException, 
                                                  InvalidParameterException, 
                                                  UnexpectedResultException {
        
        
    	EuroDoc parsedAct = new EuroDoc(); // Тук седят парснатите данни от пакета
    	EuroDoc act  = new EuroDoc();  // Новия акт за връщане
        ArrayList<SystemClassif> docSeriaClassif = (ArrayList<SystemClassif>)sd.getSysClassification(EuroConstants.CODE_SYSCLASS_SIGNATURA_SERIA, new Date(), EuroConstants.CODE_DEFAULT_LANG);
        ArrayList<SystemClassif> docProcClassif = (ArrayList<SystemClassif>)sd.getSysClassification(EuroConstants.CODE_SYSCLASS_PROC_CODE, new Date(), EuroConstants.CODE_DEFAULT_LANG);
        ArrayList<SystemClassif> langClassif = (ArrayList<SystemClassif>)sd.getSysClassification(EuroConstants.CODE_SYSCLASS_EZIK, new Date(), EuroConstants.CODE_DEFAULT_LANG);
        parsedAct.setStatus(7258);
        parsedAct.setStatusDate(new Date());
        
        //Извличане на номер на акт
        extractSignature(parsedAct, trans,docSeriaClassif) ;
         
        // Извличане на автор
        extractAvtor(parsedAct, trans) ;
        
        // Извличане на дата на изпращане
        extractDatIzpr(parsedAct, trans) ;
         
        // Извличане на дата на акта
        extractDatAct(parsedAct, trans) ;            
            
        // Извличане на останалите данни за акта
        extractAct(parsedAct, trans,docProcClassif, langClassif);        
            
        // Създаване на URL по процедирите на акта
//        for (EuroDocDosie dosie :parsedAct.getDosieta()){
//            ArrayList<EuroDocLink>  links =  generateURL(dosie,map);  
//            parsedAct.getLinks().addAll(links);
//        }
            
       
        // Ако липсва някоя част от номера на акта - спираме до тук
        if (parsedAct.getSignNomer() == null || parsedAct.getSignGodina() == null || parsedAct.getSignSeria() == null)
            return act;
        
        // Търсим дали има вече записан акт  с този номер
        ArrayList<EuroDoc> rez = new EuroDocDAO(ActiveUser.DEFAULT).filterEuroDoc(parsedAct.getSignSeria(),parsedAct.getSignGodina(),parsedAct.getSignNomer());
        if (rez.size() >= 1){
           // Ако има, то допълваме данните на извлчения вече акт с новите, които идват от пакета
           act = rez.get(0);
           act =  new EuroDocDAO(ActiveUser.DEFAULT).findById(act.getId()); //(EuroDoc)new ObjectUtils().loadObject(EuroDoc.class,act.getId(),sd);
           
           
           transferInfo (act, parsedAct);
        }else{           
           act = parsedAct;           
        } 
                
                
        // Вече имаме всички данни за акта, остава да видим и връзките
                
        // Създаваме връзки между актове
        // Връзките се образуват по два начина. Номерата може да се изпишат след заглавието на акта или
        // ако два или повече акта са по една и съща процедура     
        
        //1. Търсим в заглавието на български                
        generateVrazFromText(act.getZaglBg(), act, docSeriaClassif);
        //2. Търсим в заглавието на английски                
        generateVrazFromText(act.getZaglEn(), act, docSeriaClassif);         
        //3. Търсим връзка по процедурите        
        for (EuroDocDosie dosie : act.getDosieta()){                 
            ArrayList<EuroDoc> acts = new EuroDocDAO(ActiveUser.DEFAULT).filterEuroDocByProc(dosie.getSignProc(),dosie.getSignGodina(),dosie.getSignNomer());
            for (EuroDoc tek : acts){
                boolean found = false;
                for (EuroDocVraz vraz : act.getVraz()){
                    if (tek.getId().equals(vraz.getIdAct1()) || tek.getId().equals(vraz.getIdAct2())){
                        found = true;
                        break;
                    }                                          
                }
                if (found)  
                  continue;
                if (!tek.getId().equals(act.getId())){
                    EuroDocVraz vraz = new EuroDocVraz();
                    vraz.setIdAct1(act.getId());
                    vraz.setIdAct2(tek.getId());
                    vraz.setNameVraz(tek.getZagl());
                    vraz.setSignVraz(tek.getSign());                    
                    act.getVraz().add(vraz);                            
                }
            }
         }
        
        return act;
        
    }
    
    
    
   
    private String extractSignature(EuroDoc parsedAct, TransmissionRequestType trans, ArrayList<SystemClassif> docSeriaClassif){
         
        String err = "";
        WorkflowType workflow = trans.getTransmission().getWorkflow();
        List<Object> works = workflow.getWorkOrWorkReference();
        boolean found = false;
        for (Object tekW : works){
        	
        	Integer year = null;
        	String type = null;
        	Integer pored = null;
        	if (tekW instanceof TWork ){
			   TWork temp = (TWork)tekW;
			   //System.out.println("--->"+temp.getIdentifierWork().getValue());
			   List<TWorkExtensionBase> allExt = temp.getExtension();
			   for (TWorkExtensionBase ext : allExt){
				   if (((WorkExtensionType)ext).getParentWorkReference() == null){
					   WorkNumberType num = ((WorkExtensionType)ext).getInternalNumber();
					   //System.out.println("....-......"+num);
					   //System.out.println("..........."+num.getYear());
					   year =  num.getYear().getValue().intValue();
					   type =  num.getCode().getValue();
					   pored = num.getNumber().getValue().intValue();
					   found = true;
					   break;
				   }
			   }

			 }else{
				   TWorkRef temp = (TWorkRef)tekW;
				   String nom = temp.getRef();
		           year = Integer.parseInt(nom.substring(nom.indexOf("(")+1, nom.indexOf(")")));
		           pored = Integer.parseInt(nom.substring(nom.indexOf(")")+1, nom.indexOf("/")));
		           type = nom.substring(0, nom.indexOf("("));
		           found = true;
				   
			 }
        	if (found){
	        	parsedAct.setDatPoluch(new Date()); // Получен на днешна дата
	            parsedAct.setSignGodina(year);
	            parsedAct.setSignNomer(pored);
	            parsedAct.setSignSeriaText(type);
	            parsedAct.setSign(parsedAct.getSignSeriaText()+"("+ parsedAct.getSignGodina()+") "+ parsedAct.getSignNomer() );
	            for (SystemClassif item : docSeriaClassif){
	                if (item.getTekst().trim().equalsIgnoreCase(parsedAct.getSignSeriaText())){
	                    parsedAct.setSignSeria(item.getCode());
	                    break;
	                }
	            }
	            break;
        	}
        	
        }
        
         return err;
    }
    
    private String extractAvtor(EuroDoc parsedAct, TransmissionRequestType trans){
        
    	String err = "";
    	String podatel = trans.getTransmission().getInstitution().value() ;   //.getTransmission().getInstitution();
    	//parsedAct.setAvtorText(podatel);
    	if (podatel.equals("COM")){
	          parsedAct.setAvtor(EuroConstants.CODE_ZNACHENIE_AVTOR_COMM);                                        
	    }    

        return err;
    }
    
    
    private String extractDatIzpr(EuroDoc parsedAct, TransmissionRequestType trans){
        
	    String err = "";
	    XMLGregorianCalendar sentDate = trans.getTransmission().getDateTime();  
	    if (sentDate != null){  
			GregorianCalendar gc = new GregorianCalendar();                                                     
			gc.set(Calendar.YEAR, sentDate.getYear());
			gc.set(Calendar.MONTH, sentDate.getMonth()-1);
			gc.set(Calendar.DAY_OF_MONTH, sentDate.getDay());                                                    
			gc.set(Calendar.HOUR_OF_DAY, sentDate.getHour());
			gc.set(Calendar.MINUTE, sentDate.getMinute());
			gc.set(Calendar.SECOND, sentDate.getSecond());
			parsedAct.setDatIzpr(gc.getTime());  
	    }
		return err;
    }
    
    
    
    private String extractDatAct(EuroDoc parsedAct, TransmissionRequestType trans){
        
        String err = "";
        WorkflowType workflow = trans.getTransmission().getWorkflow();
        if (workflow.getExtension() != null && workflow.getExtension().size() > 0){
        	WorkflowExtensionType ext = (WorkflowExtensionType) workflow.getExtension().get(0);
        	XMLGregorianCalendar aDate = ext.getAdoptionDateTime();
        	if (aDate != null){
        		GregorianCalendar gc = new GregorianCalendar();                                                     
              gc.set(Calendar.YEAR, aDate.getYear());
              gc.set(Calendar.MONTH, aDate.getMonth()-1);
              gc.set(Calendar.DAY_OF_MONTH, aDate.getDay());                                                    
              gc.set(Calendar.HOUR_OF_DAY, aDate.getHour());
              gc.set(Calendar.MINUTE, 0);
              gc.set(Calendar.SECOND, 0);
              parsedAct.setDatDoc(gc.getTime());      
        	}
        }
        
        
         return err;
    }
    
    private String extractAct(EuroDoc parsedAct, TransmissionRequestType trans, ArrayList<SystemClassif> docProcClassif, ArrayList<SystemClassif> langClassif){
        
        String err = "";
        TreeSet<String> langs = new TreeSet<String> ();
        
        
        parsedAct.setZaglEn(null);
        
        WorkflowType workflow = trans.getTransmission().getWorkflow();
        List<Object> works = workflow.getWorkOrWorkReference();
        String zaglBg = "";
        String zaglEn = "";
        String workingDocNom = "";
        for (Object tekW : works){
        	if (tekW instanceof TWork){
        		TWork temp = (TWork)tekW;
        		
        		
        		
        		if (temp.getIdentifierWork() != null && temp.getIdentifierWork().getValue().startsWith("SWD")){
        			workingDocNom += "{" + temp.getIdentifierWork().getValue().substring(0, temp.getIdentifierWork().getValue().indexOf("/"))+ "} ";
        			continue;
        		}
        		
			   List<TExpression> allExp = temp.getExpression();
			   for (TExpression exp : allExp){
				   String lang = exp.getLanguage().getValue();
				   if (exp.getTitle() != null && exp.getTitle().get(0) != null && exp.getTitle().get(0).getValue() != null && !exp.getTitle().get(0).getValue().isEmpty()){
					   langs.add(lang);
					   if ("BUL".equalsIgnoreCase(lang)){
						   
						   zaglBg += exp.getTitle().get(0).getValue() ;
					   }
					   
					   if ("ENG".equalsIgnoreCase(lang)){
						   zaglEn += exp.getTitle().get(0).getValue() ;
						   
					   }
				   }
			   }
        	}
			   
        }
        
        if (zaglBg.isEmpty() && zaglEn.isEmpty() ){
        	
        	 TProcedureInterinstitutional proc = trans.getTransmission().getProcedureInterinstitutional();
			 if (proc != null){
				 for (TEventLegal legal : proc.getEventLegal()){
					 List<Object> allW = legal.getWorkOrWorkRef();
					 for (Object tekW : allW){
						 if (tekW instanceof TWork){
				        	   TWork temp = (TWork)tekW;
				        		
							   List<TExpression> allExp = temp.getExpression();
							   for (TExpression exp : allExp){
								   String lang = exp.getLanguage().getValue();
								   langs.add(lang);
								   if ("BUL".equalsIgnoreCase(lang)){
									   
									   zaglBg += exp.getTitle().get(0).getValue() + "\r\n";
								   }
								   
								   if ("ENG".equalsIgnoreCase(lang)){
									   zaglEn += exp.getTitle().get(0).getValue() + "\r\n";
									   
								   }
							   }
				        	}
					 }
					 
				 }
			 }
			 
			 
        	
        	
        }
        
        if (workingDocNom != null){
        	if (zaglBg != null && !zaglBg.isEmpty()){
        		zaglBg += " " + workingDocNom ;
        	}
        	if (zaglEn != null && !zaglEn.isEmpty()){
        		zaglEn += " " + workingDocNom ;
        	}
        }
        parsedAct.setZaglBg(zaglBg);
        parsedAct.setZaglEn(zaglEn);
        
        

        
        
        Iterator<String> it = langs.iterator();
        while (it.hasNext()){
            String ll = (String)it.next();
            EuroDocLang tek = new EuroDocLang();
            
            if (ll.equals("BUL"))  {                     
                tek.setLang(EuroConstants.CODE_ZNACHENIE_LANG_EU_BG);
            }else {
            	if (ll.equals("FRA")) {                            
            		tek.setLang(EuroConstants.CODE_ZNACHENIE_LANG_EU_FR);
            	}else {
            		if (ll.equals("ENG")) {                          
            			tek.setLang(EuroConstants.CODE_ZNACHENIE_LANG_EU_EN);
            		}else {
            			if (ll.equals("DEU")) {
            				tek.setLang(EuroConstants.CODE_ZNACHENIE_LANG_EU_DE);
            			}
            		}
            	}
            }
                     
            
//            for (SystemClassif item : langClassif)
//                if (item.getCode() == tek.getLang()){                    
//                    break;
//                }     
            if (tek.getLang() != null) {
            	parsedAct.getLangs().add(tek);
            }
            
        }
        
        
        //Досие
        String procYear = null;
        String procCode = null;
        String procNom = null;
        
        
        TProcedureInterinstitutional proc = trans.getTransmission().getProcedureInterinstitutional();
        if (proc != null){
        	TIdentifierProcedure ident = proc.getIdentifierProcedure();
        	if (ident != null){
        		procYear = ident.getYearProcedure().getValue();
        		procNom  = ident.getNumberProcedure().getValue();
        	}
        	List<TEventLegal> eventL = proc.getEventLegal();
        	for (TEventLegal tekEL : eventL){
        		if (tekEL.getTypeInterinstitutionalprocedure() != null){
        			procCode = tekEL.getTypeInterinstitutionalprocedure().getValue().value();        			
        		}
        		if (procCode != null && procYear != null && procNom != null ){
                	
                	String sign = procYear + "/"+procNom+"("+procCode+")";
                	EuroDocDosie dosie = new EuroDocDosie();
                	dosie.setSign(sign);
                	dosie.setSignProcText(procCode);
                	dosie.setSignGodina(Integer.parseInt(procYear));
                	dosie.setSignNomer(Integer.parseInt(procNom));
                	for (SystemClassif item : docProcClassif){
                		if (item.getTekst().equalsIgnoreCase(procCode)){
                			dosie.setSignProc(item.getCode());
                			parsedAct.getDosieta().add(dosie);
                			break;
                		}
                	}
                }
        	}
        }
        
        
        
        
        
        
        return err;
    }
    
    
    
//    private ArrayList<EuroDocLink> generateURL(EuroDocDosie dosie, Map patternMap){
//                                              
//            ArrayList<EuroDocLink> links = new ArrayList<EuroDocLink>();
//            // Prelex
//            String url = null;
//            url = (String)patternMap.get("URLprocPrelex");
//            if (url != null){
//                url = url.replace("CCC", dosie.getSign_procText().trim().toUpperCase() );
//                url = url.replace("YYYY", ""+dosie.getSign_godina() );
//                String nom = ""+dosie.getSign_nomer();
//                while (nom.length() < 4)
//                    nom = "0"+nom;
//                url = url.replace("NNNN", nom );
//                EuroDocLink link = new EuroDocLink();
//                link.setUrl(url);
//                link.setOpis("Prelex"); // + dosie.getSign());
//                links.add(link);
//            }
//            // OEIL
//            url = null;
//            url = (String)patternMap.get("URLprocOEIL");
//            if (url != null){
//                url = url.replace("CCC", dosie.getSign_procText().trim().toUpperCase() );
//                url = url.replace("YYYY", ""+dosie.getSign_godina() );
//                String nom = ""+dosie.getSign_nomer();
//                while (nom.length() < 4)
//                    nom = "0"+nom;
//                url = url.replace("NNNN", nom );
//                EuroDocLink link = new EuroDocLink();
//                link.setUrl(url);
//                link.setOpis("OEIL");// + dosie.getSign());
//                links.add(link);
//            }
//            
//            // IPEX
//            url = null;
//            url = (String)patternMap.get("URLprocIPEX");
//            if (url != null){
//                url = url.replace("CCC", dosie.getSign_procText().trim().toUpperCase() );
//                url = url.replace("YYYY", ""+dosie.getSign_godina() );
//                String nom = ""+dosie.getSign_nomer();
//                while (nom.length() < 4)
//                    nom = "0"+nom;
//                url = url.replace("NNNN", nom );
//                EuroDocLink link = new EuroDocLink();
//                link.setUrl(url);
//                link.setOpis("IPEX");// + dosie.getSign());
//                links.add(link);
//            }
//       
//        return links;
//    }
    
    
    private void transferInfo (EuroDoc act, EuroDoc parsedAct){
                            
        
        act.setDatPoluch(new Date()); // Получен на днешна дата
        
        // Взимаме автор
        if (parsedAct.getAvtor() != null)
            act.setAvtor(parsedAct.getAvtor());
        // Взимаме заглавието на БГ
        if (parsedAct.getZaglBg() != null && parsedAct.getZaglBg().trim().length() > 0)
            act.setZaglBg(parsedAct.getZaglBg());
        // Взимаме заглавието на EN
        if (parsedAct.getZaglEn() != null && parsedAct.getZaglEn().trim().length() > 0)
            act.setZaglEn(parsedAct.getZaglEn() );
        // Взимаме дата на документа
        if (parsedAct.getDatDoc() != null)
            act.setDatDoc(parsedAct.getDatDoc());
        // Взимаме дата на изпращане
        if (parsedAct.getDatIzpr() != null)
            act.setDatIzpr(parsedAct.getDatIzpr());
        // Взимаме вид досие
        if (parsedAct.getVidDosie() != null)
            act.setVidDosie(parsedAct.getVidDosie());
        // Обхождаме процедурите
//        for (EuroDocDosie dosie :parsedAct.getDosieta()){
//            boolean found = false;
//            for (EuroDocDosie oldDosie :act.getDosieta()){
//                if (oldDosie.getSign_proc().equals(dosie.getSign_proc()) && oldDosie.getSign_nomer().equals(dosie.getSign_nomer()) && oldDosie.getSign_godina().equals(dosie.getSign_godina())){
//                    found = true;
//                    break;
//                }                            
//            }
//            if (! found){
//                act.getDosieta().add(dosie);   
//                ArrayList<EuroDocLink> links = generateURL(dosie,patternMap);
//                act.getLinks().addAll(links);
//            }
//        }
        
        for (EuroDocLang lang :parsedAct.getLangs()){
            boolean found = false;
            for (EuroDocLang oldlang :act.getLangs()){
                if (oldlang.getLang().equals(lang.getLang())){
                    found = true;
                    break;
                }                            
            }
            if (! found){
                act.getLangs().add(lang);                  
            }
        }
        
        
        
    }
    
    
    private void generateVrazFromText(String tekst, EuroDoc act, ArrayList<SystemClassif> docSeriaClassif) throws DbErrorException, 
                                                                                       UnexpectedResultException {
    	EuroDocDAO edao = new    EuroDocDAO(ActiveUser.DEFAULT);
        if (tekst != null){
             // Разбиваме на думи - документите са оградени в {}
             String[] parts = tekst.trim().split("[\\{\\}]");
             for (int i = 0; i < parts.length; i++)  {
                 if (parts[i].length() > 3 && parts[i].length() < 21){
                     // Ограничаваме по дължина - махаме големите текстове и празните
                     String tek = parts[i].trim();
                     // Махаме текста final
                     tek = tek.replace("final", "");
                     // Махаме интервалите - ако е номер, то вече трябва да е във формат "SSS(YYYY)NNNN"
                     while (tek.indexOf(" ") != -1)
                         tek = tek.replace(" ", "");
                     // Започваме разбиването
                     int indSkoba1 = tek.indexOf("(");
                     int indSkoba2 = tek.indexOf(")");
                     if (indSkoba1 == -1 || indSkoba2 == -1){
                         System.out.println("Не може да парсне текста : " + tek );
                         continue;
                     }
                     String seriaText = tek.substring(0,indSkoba1);
                     String godinaText = tek.substring(indSkoba1+1,indSkoba2);
                     String nomerText = tek.substring(indSkoba2+1, tek.length());
                     Integer seria = null;
                     Integer godina = null;
                     Integer nomer = null;
                     
                     nomer = Integer.parseInt(nomerText);
                     godina = Integer.parseInt(godinaText);
                     
//                     nomer = 400;
//                     godina = 2012;
//                     seriaText = "COM";
                     
                     for (SystemClassif item : docSeriaClassif)
                         if (item.getTekst().trim().equalsIgnoreCase(seriaText)){
                            seria = item.getCode();
                             break;
                         } 
                     if (seria != null && godina != null && nomer != null && seria.intValue() > 0 && godina.intValue() > 0 && nomer.intValue() > 0){
                         ArrayList<EuroDoc> acts = new EuroDocDAO(ActiveUser.DEFAULT).filterEuroDoc(seria,godina,nomer);
                         if (acts.size() == 0){
                              // Връзка към непристигнал документ.                                                                         
                              
                              // Допълнение - тука трябва да проверим дали вече не сме го създали като връзка
                              boolean hasVraz = false;
                              for (EuroDocVraz tekVraz : act.getVraz() ){
                                  if (tekVraz.getSeria().equals(seria) && tekVraz.getGodina().equals(godina) && tekVraz.getNomer().equals(nomer)){
                                      hasVraz = true;
                                      break;
                                  }
                              }
                              
                              if (! hasVraz){                              
                                  EuroDocVraz vraz = new EuroDocVraz();
                                  vraz.setIdAct1(act.getId());
                                  vraz.setIdAct2(null);
                                  vraz.setNameVraz(null);
                                  vraz.setSignVraz(seriaText + "(" + godinaText + ") " + nomerText);                                  
                                  vraz.setSeria(seria);
                                  vraz.setGodina(godina);
                                  vraz.setNomer(nomer);
                                  act.getVraz().add(vraz);   
                              }
                             
                         }else{
                             for (EuroDoc tekAct : acts){
                            	 tekAct.setVraz(edao.loadVraz(tekAct.getId()));
                                 boolean found = false;
                                 for (EuroDocVraz vraz : act.getVraz()){
                                     if (tekAct.getId().equals(vraz.getIdAct1()) || tekAct.getId().equals(vraz.getIdAct2())){
                                         found = true;
                                         break;
                                     }                                          
                                 }
                                 if (found)  
                                   continue;
                                 if (!tekAct.getId().equals(act.getId())){
                                     EuroDocVraz vraz = new EuroDocVraz();
                                     vraz.setIdAct1(act.getId());
                                     vraz.setIdAct2(tekAct.getId());
                                     vraz.setNameVraz(tekAct.getZagl());
                                     vraz.setSignVraz(tekAct.getSign());                                     
                                     act.getVraz().add(vraz);                            
                                 }
                             }
                         }
                     }
                     
                     
                     
                 }
                 
             }
        }
    }
    
    
}
