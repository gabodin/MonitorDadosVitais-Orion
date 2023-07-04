package br.ufrn.monitordadosvitais.model;

public class Paciente {
	private Double latitude;
	private Double longitude;
	private String nome;
	private String email;
	private Integer saturacao;
	private Double temperatura;
	private Integer pressao;
	
	public Paciente() {
		
	}
	
	public Paciente(Double latitude, Double longitude, String nome, String email, Integer saturacao,
			Double temperatura, Integer pressao) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.nome = nome;
		this.email = email;
		this.saturacao = saturacao;
		this.temperatura = temperatura;
		this.pressao = pressao;
	}


	public Double getLatitude() {
		return latitude;
	}


	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}


	public Double getLongitude() {
		return longitude;
	}


	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Integer getSaturacao() {
		return saturacao;
	}


	public void setSaturacao(Integer saturacao) {
		this.saturacao = saturacao;
	}


	public Double getTemperatura() {
		return temperatura;
	}


	public void setTemperatura(Double temperatura) {
		this.temperatura = temperatura;
	}


	public Integer getPressao() {
		return pressao;
	}


	public void setPressao(Integer pressao) {
		this.pressao = pressao;
	}
	
	
}
