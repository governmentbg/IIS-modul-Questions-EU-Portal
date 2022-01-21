package com.ib.euroact.db.dto;

import java.util.ArrayList;
import java.util.Date;

public class EuroHarm {
	
	private String nomerDoc;
	private Date datDoc;
	private Date datPriemane;
	private String sastoianie;
	private String ime;
	private String imeZakon;
	private String vnositel;
	private String vodesta;
	private Date datParvo;
	private Integer prioritet;
	private Integer pages;
	private String belejkiEs;
	private String belejkiPraven;
	
	private ArrayList<String> glavi = new ArrayList<String>();
	
	public String getNomerDoc() {
		return nomerDoc;
	}
	public void setNomerDoc(String nomerDoc) {
		this.nomerDoc = nomerDoc;
	}
	public Date getDatDoc() {
		return datDoc;
	}
	public void setDatDoc(Date datDoc) {
		this.datDoc = datDoc;
	}
	public Date getDatPriemane() {
		return datPriemane;
	}
	public void setDatPriemane(Date datPriemane) {
		this.datPriemane = datPriemane;
	}
	public String getSastoianie() {
		return sastoianie;
	}
	public void setSastoianie(String sastoianie) {
		this.sastoianie = sastoianie;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getImeZakon() {
		return imeZakon;
	}
	public void setImeZakon(String imeZakon) {
		this.imeZakon = imeZakon;
	}
	public String getVnositel() {
		return vnositel;
	}
	public void setVnositel(String vnositel) {
		this.vnositel = vnositel;
	}
	public String getVodesta() {
		return vodesta;
	}
	public void setVodesta(String vodesta) {
		this.vodesta = vodesta;
	}
	public Date getDatParvo() {
		return datParvo;
	}
	public void setDatParvo(Date datParvo) {
		this.datParvo = datParvo;
	}
	public Integer getPrioritet() {
		return prioritet;
	}
	public void setPrioritet(Integer prioritet) {
		this.prioritet = prioritet;
	}
	public Integer getPages() {
		return pages;
	}
	public void setPages(Integer pages) {
		this.pages = pages;
	}
	public String getBelejkiEs() {
		return belejkiEs;
	}
	public void setBelejkiEs(String belejkiEs) {
		this.belejkiEs = belejkiEs;
	}
	public String getBelejkiPraven() {
		return belejkiPraven;
	}
	public void setBelejkiPraven(String belejkiPraven) {
		this.belejkiPraven = belejkiPraven;
	}
	public ArrayList<String> getGlavi() {
		return glavi;
	}
	public void setGlavi(ArrayList<String> glavi) {
		this.glavi = glavi;
	}
	
	
	

}
