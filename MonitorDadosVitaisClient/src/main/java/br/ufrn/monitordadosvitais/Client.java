package br.ufrn.monitordadosvitais;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufrn.monitordadosvitais.exceptions.RestRequestException;
import br.ufrn.monitordadosvitais.model.Paciente;
import br.ufrn.monitordadosvitais.utils.*;

public class Client {

	public Client() {

	}
	
	private static Map<String, Paciente> pacientes = new HashMap<String, Paciente>();
	private static Map<String, String> idSubscriptions = new HashMap<String, String>();
	
	public static String URI = "http://localhost:8080/MonitorDadosVitais/";
	
	public static FactoryPaciente factory = new FactoryPaciente();
	
	public static String cadastrarPaciente(String nome) throws RestRequestException {
		String uri = URI + "paciente";
		Paciente p = factory.gerarPaciente();
		p.setNome(nome);
		
		Map<String, String> headerParams = new HashMap<String, String>();

		headerParams.put("accept", "application/json");
		headerParams.put("content-type", "application/json");
		
		ObjectMapper objectMapper = new ObjectMapper();

		String body = null;
		
		try {
			body = objectMapper.writeValueAsString(p);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		String response = HttpUtils.httpPostRequest(uri, headerParams, body, 200);
		JsonNode json;
		String id = "";
		
		try {
			json = objectMapper.readTree(response);
			System.out.println(json);
			id = json.get("id").asText();
			pacientes.put(id, p);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
						

		return id;
	}
	
	public static String atualizarPaciente(String id) throws RestRequestException {
		String uri = URI + "paciente/" + id;
		Paciente p = factory.atualizarPaciente(pacientes.get(id));
		
		Map<String, String> headerParams = new HashMap<String, String>();

		headerParams.put("accept", "application/json");
		headerParams.put("content-type", "application/json");
		
		ObjectMapper objectMapper = new ObjectMapper();

		String body = null;
		
		try {
			body = objectMapper.writeValueAsString(p);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		String response = HttpUtils.httpPutRequest(uri, headerParams, body, 200);
		JsonNode json;
		String idResponse = "";
		
		try {
			json = objectMapper.readTree(response);
			System.out.println(json);
			idResponse = json.get("contextResponses").get(0).get("contextElement").get("id").asText();
			pacientes.put(id, p);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return idResponse;
	}
	
	public static String subscreverPaciente(String id) throws RestRequestException {
		String uri = URI + "paciente/" + "subscrever/" + id;
		
		Map<String, String> headerParams = new HashMap<String, String>();

		headerParams.put("accept", "application/json");
		headerParams.put("content-type", "application/json");
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String body = null;
			
		String response = HttpUtils.httpPostRequest(uri, headerParams, body, 200);
		JsonNode json;
		String idSubscription = "";
		
		try {
			json = objectMapper.readTree(response);
			System.out.println(json);
			idSubscription = json.get("subscribeResponse").get("subscriptionId").asText();
			idSubscriptions.put(id, idSubscription);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		
		return idSubscription;
	}
	
	
	public static void main(String[] args) {
		try {
			Paciente p1 = new Paciente(49.65847923195591, 92.00645975790138, "Ian", "ian.barreto.700@ufrn.edu.br", 99, 35.4, 115);
			Paciente p2 = new Paciente(-3.93743448841154, -20.63655202604562, "Tic", "ian.barreto.700@ufrn.edu.br", 93, 35.1, 96);
			pacientes.put("paciente26", p1);
			pacientes.put("paciente27", p2);
			atualizarPaciente("paciente26");
			atualizarPaciente("paciente27");
			
//			String id = cadastrarPaciente("Andrea");
//			String id2 = cadastrarPaciente("Rafaela");
//			String id3 = cadastrarPaciente("Cit");
//			subscreverPaciente(id);
//			subscreverPaciente(id2);
//			subscreverPaciente(id3);

			
			
		} catch (RestRequestException e) {
			e.printStackTrace();
		}
	}


}
