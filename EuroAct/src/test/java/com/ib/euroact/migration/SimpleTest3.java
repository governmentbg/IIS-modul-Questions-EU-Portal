package com.ib.euroact.migration;

import java.util.Arrays;

public class SimpleTest3 {

	public static void main(String[] args) {
//		 String s = "2003/54/ЕO на Европейския парламент и на Съвета от 26 юни 2003 г. относно общите правила за вътрешния пазар на електроенергия и отменяща Директива 96/92/ЕО (ОВ, L 176/37 от 15 юли 2004 г.).";
//		 String maska = "0123456789() EOECЕОЕИОЕС/";
//		 String nomer = "";
//		 for (int i = 0; i < s.length(); i++) {
//			 String tek = s.substring(i, i+1);
//			 if (!maska.contains(tek)) {
//				 break;
//			 }else {
//				 nomer += tek;
//			 }
//		 }
//		 
//		 System.out.println(nomer.trim());
		
		
		
		
		String t1 = "4. Частична или цялостна предварителна оценка на въздействието, проведена в съответствие с изискванията"; 
		String t2 =	"4. Частична или цялостна предварителна оценка на въздействието, проведена в съответствие с изискванията";
		
		System.out.println(t1.equals(t2));
		
		//t1 = t1.replace(" ", " ");
		
		System.out.println(t1.equals(t2));
		
		byte[] tb1 = t1.getBytes();
		byte[] tb2 = t2.getBytes();
		
		System.out.println(Arrays.toString(tb1));
		System.out.println(Arrays.toString(tb2));

		for (int i = 0; i < t1.length(); i++) {
			System.out.println(t1.charAt(i) + "\t" + t2.charAt(i) + "\t" + (int)t1.charAt(i)+ "\t" + (int)t2.charAt(i));
		}
		
	}

}
