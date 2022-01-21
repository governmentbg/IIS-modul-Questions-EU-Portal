package com.ib.euroact.system;

import com.ib.indexui.system.Constants;

public class EuroConstants extends Constants {
	
	
	// ********************* Кодове на обекти **************************************
		/** Потребител */
		public static final int CODE_ZNACHENIE_JOURNAL_USER = 1;
		
		/**Код на информационния обект "Проектоакт".     */
	    public static final int CODE_ZNACHENIE_JOURNAL_EURO_DOC = 83; 
	    
	    /**Код на информационния обект "Отчет".     */
	    public static final int CODE_ZNACHENIE_JOURNAL_EURO_OTCHET = 84;  
	    
	    /**Код на информационния обект "Бюлетин".     */
	    public static final int CODE_ZNACHENIE_JOURNAL_EURO_BULETIN = 100;  
		
		/** Европейски акт */
		public static final int CODE_ZNACHENIE_JOURNAL_EURO_ACT = 33840;
		
		/** Европейски акт - връзки*/
		public static final int CODE_ZNACHENIE_JOURNAL_EURO_ACT_CONN = 33842;
		
		/** Европейски акт - секция Е*/
		public static final int CODE_ZNACHENIE_JOURNAL_EURO_ACT_SECTION_E = 33843;
	
		/** Европейски акт - секция Z*/
		public static final int CODE_ZNACHENIE_JOURNAL_EURO_ACT_SECTION_Z = 33845;
		
		/** Досие */
		public static final int CODE_ZNACHENIE_JOURNAL_EURO_DOSSIER = 33841;
		
		/** Годишна програма */
		public static final int CODE_ZNACHENIE_JOURNAL_EURO_PROGRAMA = 33846;
		
		/** Точка от годишна програма */
		public static final int CODE_ZNACHENIE_JOURNAL_EURO_PROGRAMA_POINT = 33847;
		
		/** Файл */
		public static final int	CODE_ZNACHENIE_JOURNAL_EURO_FILE	= 13;
		
		
	    
		/** Код на дейност "Неуспешен вход". */
		public static final int	CODE_DEIN_LOGIN_FAILED	= 6;
		
		
		/** Код на дейност "Вход в системата". */
		public static final int	CODE_DEIN_LOGIN			= 5;
		
		/** Код на дейност "Изход от системата". */
		public static final int	CODE_DEIN_LOGOUT		= 9;
	   
		

	// ********************* Системни класификации *********************************
	    
	        
		
		
	    /** Меню */
		public static final int CODE_SYSCLASS_MENU=2;	    
	    
	    
	    /** Код на класификацията за ДА/НЕ  --> Override защото в Парламента 26 е с друго значение */				
		public static final int CODE_CLASSIF_DANE = 3;
		
		 /** Код на класификация "Административни структури"*/
	    public static final int CODE_SYSCLASS_ADMSTRUCTURE = 6;   
		
		
		/** Код на класификация "Министерски съвет" */
	    public static final int CODE_SYSCLASS_MS = 19;
	    
	    /**Код на класификация "Състояние в етапа".      */
	    public static final int CODE_CLASS_ETAP_STATUS = 53;	   
	    
	    /** Код на класификация "Статус на отчет на МС" */
	    public static final int CODE_SYSCLASS_STATUS_OTCHET_MS = 76;
	    
	    /**Код на класификация "Състояние на законопроект".      */
	    public static final int CODE_CLASS_SASTOIANIE = 105;
	    
	    /**Код на класификация "Тематика".      */
	    public static final int CODE_CLASS_HARM_GLAVA = 132;
	       
	    /** Код на класификация "Вид на отчет на МС" */
	    public static final int CODE_SYSCLASS_VID_OTCHET_MS = 133;
	    
	    /**Класификация "Процедури по които се разпределя акта".      */
	    public static final int CODE_SYSCLASS_PROCEDURE_BG = 135;        
	   
	    /**Класификация "Получен от".      */
	    public static final int CODE_SYSCLASS_POLUCHEN_OT = 136;
	    
	    /**Класификация "Автор".      */
	    public static final int CODE_SYSCLASS_AVTOR = 137;
	    
	    /** Сигнатура - серия */
		public static final int CODE_SYSCLASS_SIGNATURA_SERIA=138;
	    
	    /**Класификация "Вид досие".      */
	    public static final int CODE_SYSCLASS_DOSIE = 139;
	    
	    /**Класификация "Тематика ЕК".      */
	    public static final int CODE_SYSCLASS_TEMATIKA_EK = 140;
	    
	    /**Класификация "Тематика НС".      */
	    public static final int CODE_SYSCLASS_TEMATIKA_NS = 141; 
	    
	    /** Код на класификация "Код на процедура" */
	    public static final int CODE_SYSCLASS_PROC_CODE = 142;  
	    
	    /**Класификация "Статус на акт".      */
	    public static final int CODE_SYSCLASS_STATUS = 143;
	    
	    /** Код на класификация "Помощни текстове за URL" */
	    public static final int CODE_SYSCLASS_POM_TEKST = 151;  
	    
	    /** Код на класификация "Езици" */
	    public static final int CODE_SYSCLASS_EZIK = 152;   
	    	    
	    /**Класификация "Статус по IPEX".      */
	    public static final int CODE_SYSCLASS_STATUS_IPEX = 169;
	    
	    /**Класификация "Отговарящи лица от дирекция ЕС".      */
	    public static final int CODE_SYSCLASS_DIR_ES= 1027; 
	    
		
	    /**  Тематика на досие */
		public static final int CODE_SYSCLASS_TEMA_DOSSIER = 183;
	    
		/** Вид на акта */
		public static final int CODE_SYSCLASS_VID_ACT=200;
		/** Динамична класификация - евроактове */
		public static final int CODE_SYSCLASS_EURO_ACT=201;
		/** Видове връзки между актове */
		public static final int CODE_SYSCLASS_VID_VRAZ=202;
		/** Дефиниция на логически списъци */
		public static final int CODE_SYSCLASS_LOG_LISTS=203;		
		/** Отговори по точка АБ.4 */
		public static final int CODE_SYSCLASS_AB4=204;
		/** Динамична класификация на закони */
		public static final int CODE_SYSCLASS_ZAKONI=205;
		
		/** Динамична класификация - съдебни решения */
		public static final int CODE_SYSCLASS_SAD_ACT=206;
		
		/** Код на класификация Период от дати - без бъдещи */
		public static final int CODE_CLASSIF_PERIOD_NOFUTURE = 207;
		
		
		// ********************* Логически списъци *********************************
		/** Дефиниция на логически списък - Прави/Обратни връзки */
		public static final int CODE_LOGIC_LIST_VRAZKI=1;
		
		// Класификация 2 - Меню
		/** Код на значение "Право да изтриваи" класификация "Меню" 2 */
		public static final int	CODE_ZNACHENIE_MENU_DEF_PRAVO_DELETE = 37461;		
		
		//Класификация 200 - Вид на акта
		/** Директиви  - СЪБИРАТЕЛНО*/
		public static final int	TIP_ACT_DIRECTIVI	= 30265;
		/** Директива */
		public static final int	TIP_ACT_DIREKTIVA	= 30225;
		/** Директива за изпълнение */
		public static final int	TIP_ACT_DIREKTIVA_ZA_IZPALNENIE	= 30266;
		/** Делегирана директива */
		public static final int	TIP_ACT_DELEGIRANA_DIREKTIVA	= 30267;
		/** Поправка на Директива */
		public static final int	TIP_ACT_POPRAVKA_NA_DIREKTIVA	= 30268;
		
		
		/** Регламенти - СЪБИРАТЕЛНО */
		public static final int	TIP_ACT_REGLAMENTI	= 30226;	
		/** Регламент */
		public static final int	TIP_ACT_REGLAMENT	= 30229;
		/** Делегиран регламент */
		public static final int	TIP_ACT_DELEGIRAN_REGLAMENT	= 30230;
		/** Регламент за изпълнение */
		public static final int	TIP_ACT_REGLAMENT_ZA_IZPALNENIE	= 30231;
		/** Поправка на Регламент */
		public static final int	TIP_ACT_POPRAVKA_REGLAMENT	= 30269;
		/** Поправка на Делегиран регламент */
		public static final int	TIP_ACT_POPRAVKA_DEL_REGLAMENT	= 30270;
		/** Поправка на Регламент за изпълнение */
		public static final int	TIP_ACT_POPRAVKA_REGLAMENT_IZP	= 30286;
		
		/** Решения - СЪБИРАТЕЛНО*/
		public static final int	TIP_ACT_RESHENIA	= 30271;
		/** Решение */
		public static final int	TIP_ACT_RESHENIE	= 30227;
		/** Рамково решение */
		public static final int	TIP_ACT_RAMKOVO_RESHENIE	= 30228;
		/** Решение за изпълнение */
		public static final int	TIP_ACT_RЕSHENIE_ZA_IZPALNENIE	= 30272; 		
		/** Делегирано решение */
		public static final int	TIP_ACT_DELEGIRANO_RESHENIE	= 30273;
		
		/** Съдебни актове - СЪБИРАТЕЛНО*/
		public static final int	TIP_ACT_SADEVNI_AKTOVE = 30274;
		/** Решение по дело */
		public static final int	TIP_ACT_RESHENIE_PO_DELO = 30281;
		/** Решение по съединени дела */
		public static final int	TIP_ACT_RESHENIE_PO_SAEDINENI_DELA = 30282;
		/** Определение по дело */
		public static final int	TIP_ACT_OPREDELENIE_PO_DELO	= 30283;
		/** Определение по съединени дела */
		public static final int	TIP_ACT_OPREDELENIE_PO_SAEDINENI_DELA = 30311; 
		
		
		/** Други - СЪБИРАТЕЛНО*/
		public static final int	TIP_ACT_DRUGI	= 30275;
		/** Протокол */
		public static final int	TIP_ACT_PROTOKOL	= 30276;
		/** Конвенция */
		public static final int	TIP_ACT_KONVENCIA	= 30277;
		/** Зелена книга */
		public static final int	TIP_ACT_ZELENA_KNIGA	= 30278;
		/** Споразумение */
		public static final int	TIP_ACT_SPORAZUMENIE	= 30279;
		/** Друг акт */
		public static final int	TIP_ACT_DRUG_AKT	= 30280;		
		/** Регулация */
		public static final int	TIP_ACT_REGULACIA	= 30284;		
		/** Предложение */
		public static final int	TIP_ACT_PREDLOJENIE	= 30285;
		
		
		//Класификация 202 - Връзки		
		/** 	Отменен/а/о*/
		public static final int	VID_VRAZ_OTMENEN = 30251;
		/** 	Частично изменя*/
		public static final int	VID_VRAZ_CH_IZMENIA = 30248;
		/** 	Допълва*/
		public static final int	VID_VRAZ_DOPALVA = 30250;
		/** 	Допълнен/а/о*/
		public static final int	VID_VRAZ_DOPALNEN = 30287;
		/** 	Отменя*/
		public static final int	VID_VRAZ_OTMENIA = 30245;
		/** 	Заменя*/
		public static final int	VID_VRAZ_ZAMENIA = 30246;
		/** 	Изменя*/
		public static final int	VID_VRAZ_IZMENIA = 30247;
		/** 	Частично отменя*/
		public static final int	VID_VRAZ_CH_OTMENIA = 30249;
		/** 	Заменен/а/о*/
		public static final int	VID_VRAZ_ZAMENEN = 30252;
		/** 	Изменен/а/о*/
		public static final int	VID_VRAZ_IZMENEN = 30253;
		/** 	Частично изменен/а/о*/
		public static final int	VID_VRAZ_CH_IZMENEN = 30288;
		/** 	Частично отменен/а/о*/
		public static final int	VID_VRAZ_CH_OTMENEN = 30289;
		/** 	Поправя*/
		public static final int	VID_VRAZ_POPRAVIA = 30293;
		/** 	Поправен/а/о*/
		public static final int	VID_VRAZ_POPRAVEN = 30292;
		
		//Малко фиктивни връзки
		/**30290	Основен акт (AB4 за изпълнение)*/
		public static final int	VID_VRAZ_OSN_IZP = 30290;
		/**30291	Приет акт (AB4 за изпълнение)*/
		public static final int	VID_VRAZ_PREIT_IZP = 30291;
		/**30307	Основен акт (AB4 делегирани)*/
		public static final int	VID_VRAZ_OSN_DEL = 30307;
		/**30308	Приет акт (AB4 делегирани)*/
		public static final int	VID_VRAZ_PRIET_DEL = 30308;
		/**30309	Основен акт (Секция Г)*/
		public static final int	VID_VRAZ_OSN_G = 30309;
		/**30310	Съдебен акт (Секция Г)*/
		public static final int	VID_VRAZ_PRIET_G = 30310;
		
		//Класификация 203 - Дефиниция на логически списъци
		/** 	Списък на всички прави връзки*/
		public static final int	DEF_PRAVI_VRAZKI = 30305;
		/** 	Списък на всички обратни връзки*/
		public static final int	DEF_OBRATNI_VRAZKI = 30306;
		
		/** 	Списък АБ2*/
		public static final int	DEF_AB2_VRAZKI = 30312;
		/** 	Списък АБ3*/
		public static final int	DEF_AB3_VRAZKI = 30313;
		
		
		//Отговор по т.АБ.4
		/** Не се приемат */
		public static final int	CODE_OTG_NE_SE_PRIEMAT	= 30325;
		/** Няма приети */
		public static final int	CODE_OTG_NIAMA_PRIETI	= 30326;
		/** Има приети */
		public static final int	CODE_OTG_IMA_PRIETI	= 30327;
		
		
		//------------- от другата система -------------------------------
		
		 // Класификация "Aвтор на проектоакт от ЕС" код 137
	     /** Код на значение "действащ"  */
	     public static final int CODE_ZNACHENIE_AVTOR_COMM = 7097;
	     
	     
	  // Класификация  "Официални езици на ЕС"  код 152
	     /**  Код на  значение "БЪЛГАРСКИ"*/
	     public static final int CODE_ZNACHENIE_LANG_EU_BG = 225; 
	     /**  Код на  значение "АНГЛИЙСКИ"*/
	     public static final int CODE_ZNACHENIE_LANG_EU_EN = 89;  
	     /**  Код на  значение "ФРЕНСКИ"*/
	     public static final int CODE_ZNACHENIE_LANG_EU_FR = 91;  
	     /**  Код на  значение "НЕМСКИ"*/
	     public static final int CODE_ZNACHENIE_LANG_EU_DE = 995;  
		
		
}
