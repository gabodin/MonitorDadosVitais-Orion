package br.ufrn.monitordadosvitais.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Random;

import com.github.javafaker.Faker;

import br.ufrn.monitordadosvitais.model.Paciente;

public class FactoryPaciente {
	
	private Faker faker;
	
	private String email = "ian.barreto.700@ufrn.edu.br";  
	
	public FactoryPaciente() {
		faker = new Faker();
	}
	
	
	
	private static int gerarPressaoArterial() {
        Random random = new Random();
        int pressaoMinima = 70;
        int pressaoMaxima = 150;

        return pressaoMinima + random.nextInt(pressaoMaxima - pressaoMinima + 1);
    }

    private static double gerarTemperatura() {
        Random random = new Random();
        double temperaturaMinima = 35.5;
        double temperaturaMaxima = 39.5;

        double temperatura = temperaturaMinima + (temperaturaMaxima - temperaturaMinima) * random.nextDouble();
        
        
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        String formattedNumber = decimalFormat.format(temperatura);
        double parsedNumber = 0.0d;
        
        try {
            parsedNumber = decimalFormat.parse(formattedNumber).doubleValue();
            System.out.println("Parsed number: " + parsedNumber);
        } catch (ParseException e) {
            System.out.println("Unable to parse the number: " + e.getMessage());
        }
        
        
        return parsedNumber;
    }

    private static int gerarSaturacaoOxigenio() {
        Random random = new Random();
        int saturacaoMinima = 90;
        int saturacaoMaxima = 100;

        return saturacaoMinima + random.nextInt(saturacaoMaxima - saturacaoMinima + 1);
    }
      
    private static Double gerarLatitude() {
		double minLatitude = -90.0;
        double maxLatitude = 90.0;
        Random random = new Random();
        
        double latitude = minLatitude + (maxLatitude - minLatitude) * random.nextDouble();
        
        return latitude;
	}
	
	private static Double gerarLongitude() {
		double minLongitude = -180.0;
        double maxLongitude = 180.0;
        Random random = new Random();
        
        Double longitude = minLongitude + (maxLongitude - minLongitude) * random.nextDouble();
        
        return longitude;
	}
	
	public Paciente gerarPaciente() {
		Integer pressaoArterial = FactoryPaciente.gerarPressaoArterial();
		Double temperatura = FactoryPaciente.gerarTemperatura();
		Integer saturacao = FactoryPaciente.gerarSaturacaoOxigenio();
		Double latitude = FactoryPaciente.gerarLatitude();
		Double longitude = FactoryPaciente.gerarLongitude();
		
		Paciente p = new Paciente(latitude, longitude, new String(""), this.email, saturacao, temperatura, pressaoArterial);
		
		return p;
	}
	
	 
	public Paciente atualizarPaciente(Paciente p) {
		Integer pressaoArterial = FactoryPaciente.gerarPressaoArterial();
		Double temperatura = FactoryPaciente.gerarTemperatura();
		Integer saturacao = FactoryPaciente.gerarSaturacaoOxigenio();
		
		p.setPressao(pressaoArterial);
		p.setTemperatura(temperatura);
		p.setSaturacao(saturacao);
		
		return p;
	}
}
