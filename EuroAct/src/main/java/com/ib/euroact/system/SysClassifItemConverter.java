package com.ib.euroact.system;
import java.util.StringTokenizer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.ib.system.db.dto.SystemClassif;

@FacesConverter("sysClassifItemConverterEU")
public class SysClassifItemConverter implements Converter<Object> {
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if(value!=null){
		//	System.out.println("getAsObject-> "+value);
			StringTokenizer scItem = new StringTokenizer(value, "@@");
								// public SystemClassif(int codeClassif, int code, String tekst, String dopInfo)
		
			try {
				return new SystemClassif(Integer.parseInt(scItem.nextToken()), Integer.parseInt(scItem.nextToken()), scItem.nextToken(), null);
            } catch (Exception cce) {
            	SystemClassif newSC =  new SystemClassif();
            	if(value.equals("0@@0@@")) { //ako e now element
            		newSC.setTekst("");
            	} else {
            		newSC.setTekst(value);
            	}
            	return newSC; // това се случва, ако се добави ново значение!
            }			
			
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
		if(object != null) {			
			if(object instanceof SystemClassif ){
				SystemClassif scItem = (SystemClassif) object;
			//	System.out.println("getAsString-> "+((SystemClassif) object).getCode());
				
	            return scItem.getCodeClassif()+"@@"+scItem.getCode()+"@@"+scItem.getTekst(); //za sega tolkova...
	            
			} else if(object instanceof String ){
				return String.valueOf(object.toString());
			} else {
				return null;
			}
        }
        else {
            return null;
        }

	}


}

