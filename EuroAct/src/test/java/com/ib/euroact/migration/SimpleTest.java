package com.ib.euroact.migration;

import java.security.InvalidParameterException;

public class SimpleTest {

	public static void main(String[] args) {		
		
//		String s = "1. Законът за изменение и допълнение на Закона за енергетиката (ДВ, бр. 54 от 2012 г.) предвижда мерки по прилагането на Регламент (ЕС, Евратом) № 617/2010 на Съвета от 24 юни 2010 г. относно съобщаването на Комисията на инвестиционни проекти за енергийна инфраструктура в Европейския съюз, както и за отмяна на Регламент (ЕО) № 736/96 (ОВ, L 180/7 от 15 юли 2010 г.).";
//		System.out.println(s.indexOf("Регламент (ЕС, ЕВРАТОМ) № 617/2010"));
		
		String s = "1. Законът за изменение и допълнение на Закона за енергетиката (ДВ, бр. 54 от 2012 г.) предвижда мерки по прилагането на Регламент(ЕС,Евратом)№617/2010 на Съвета от 24 юни 2010 г. относно съобщаването на Комисията на инвестиционни проекти за енергийна инфраструктура в Европейския съюз, както и за отмяна на Регламент (ЕО) № 736/96 (ОВ, L 180/7 от 15 юли 2010 г.).";
		System.out.println(s.indexOf("ЕВРАТОМ"));

	}
	
	
	
	
	public static void extractDVfromZ(String s) {
		
		int beginIndex = s.lastIndexOf("(");
		int endIndex = s.lastIndexOf(")");
		
		if (beginIndex == -1 || endIndex == -1 || endIndex < beginIndex) {
			throw new InvalidParameterException("Неправилно форматиран акт в секция 'З' -> " + beginIndex + "|" + endIndex );
		}
		
		String dvInfo = s.substring(beginIndex+1, endIndex);
		System.out.println(dvInfo);
		
		int brIndex = dvInfo.indexOf("бр.");
		if (brIndex == -1) {
			throw new InvalidParameterException("Неправилно форматиранo описание за ДВ за акт в секеция 'З' (липсва бр.) -> " + dvInfo );
		}
		brIndex+=3;
		int otIndex = dvInfo.indexOf("от");
		if (otIndex == -1) {
			throw new InvalidParameterException("Неправилно форматиранo описание за ДВ за акт в секеция 'З' (липсва от) -> " + dvInfo );
		}
		
		int godIndex = dvInfo.indexOf("г");
		if (godIndex == -1) {
			throw new InvalidParameterException("Неправилно форматиранo описание за ДВ за акт в секеция 'З' (липсва г.) -> " + dvInfo );
		}
		
		String brStr = dvInfo.substring(brIndex,otIndex).trim();
		String godStr = dvInfo.substring(otIndex+2,godIndex).trim();
		System.out.println("|"+brStr+"|"+godStr+"|");
		
		
		
	}
	

}
