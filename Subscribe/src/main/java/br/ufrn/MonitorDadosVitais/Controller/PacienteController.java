package br.ufrn.MonitorDadosVitais.Controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.ufrn.MonitorDadosVitais.Model.Paciente;

@RestController
@RequestMapping("/paciente")
public class PacienteController {
	
	private int counter = 0;
	
	@Value("${ip.adress}")
	private String endereco;
	
	@PostMapping
	public ResponseEntity<String> cadastrarPaciente(@RequestBody JsonNode body) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			
			
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("http://127.0.0.1:1026/v1/contextEntities"))
					.method("GET", HttpRequest.BodyPublishers.noBody())
					.build();
			
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			JsonNode elementosNode = objectMapper.readTree(response.body());
			
			if (elementosNode.get("contextResponses") == null) {
				this.counter = 0;
			}
			else this.counter = elementosNode.get("contextResponses").size() + 25;
			
			
			Paciente p = objectMapper.treeToValue(body, Paciente.class);
			
			ObjectNode novoPaciente = objectMapper.createObjectNode();
			ArrayNode atributos = objectMapper.createArrayNode();
			
			ObjectNode longitude = objectMapper.createObjectNode();
			longitude.put("name", "longitude");
			longitude.put("type", "float");
			longitude.put("value", p.getLongitude());
			atributos.add(longitude);
			
			ObjectNode latitude = objectMapper.createObjectNode();
			latitude.put("name", "latitude");
			latitude.put("type", "float");
			latitude.put("value", p.getLatitude());
			atributos.add(latitude);
			
			ObjectNode email = objectMapper.createObjectNode();
			email.put("name", "email");
			email.put("type", "String");
			email.put("value", p.getEmail());
			atributos.add(email);
			
			ObjectNode nome = objectMapper.createObjectNode();
			nome.put("name", "nome");
			nome.put("type", "String");
			nome.put("value", p.getNome());
			atributos.add(nome);
			
			ObjectNode pressao = objectMapper.createObjectNode();
			pressao.put("name", "pressao");
			pressao.put("type", "Int");
			pressao.put("value", p.getPressao());
			atributos.add(pressao);
			
			ObjectNode saturacao = objectMapper.createObjectNode();
			saturacao.put("name", "saturacao");
			saturacao.put("type", "Int");
			saturacao.put("value", p.getSaturacao());
			atributos.add(saturacao);
			
			ObjectNode temperatura = objectMapper.createObjectNode();
			temperatura.put("name", "temperatura");
			temperatura.put("type", "float");
			temperatura.put("value", p.getTemperatura());
			atributos.add(temperatura);
			
			System.out.println(atributos);
			
			novoPaciente.put("type", "Paciente");
			novoPaciente.put("isPattern", false);
			novoPaciente.put("id", "paciente"+counter);
			novoPaciente.put("attributes", atributos);
			
			System.out.println(objectMapper.writeValueAsString(novoPaciente));
			
			request = HttpRequest.newBuilder()
					.uri(URI.create("http://127.0.0.1:1026/v1/contextEntities"))
					.method("POST", HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(novoPaciente)))
					.header("Content-Type", "application/json")
					.build();
			
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			
			return ResponseEntity.ok(response.body());
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> atualizarPaciente(@RequestBody JsonNode body, @PathVariable("id") String id) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			Paciente p = objectMapper.treeToValue(body, Paciente.class);
			
			ObjectNode paciente = objectMapper.createObjectNode();
			ArrayNode atributos = objectMapper.createArrayNode();
			
			ObjectNode longitude = objectMapper.createObjectNode();
			longitude.put("name", "longitude");
			longitude.put("type", "float");
			longitude.put("value", p.getLongitude());
			atributos.add(longitude);
			
			ObjectNode latitude = objectMapper.createObjectNode();
			latitude.put("name", "latitude");
			latitude.put("type", "float");
			latitude.put("value", p.getLatitude());
			atributos.add(latitude);
			
			ObjectNode email = objectMapper.createObjectNode();
			email.put("name", "email");
			email.put("type", "String");
			email.put("value", p.getEmail());
			atributos.add(email);
			
			ObjectNode nome = objectMapper.createObjectNode();
			nome.put("name", "nome");
			nome.put("type", "String");
			nome.put("value", p.getNome());
			atributos.add(nome);
			
			ObjectNode pressao = objectMapper.createObjectNode();
			pressao.put("name", "pressao");
			pressao.put("type", "Int");
			pressao.put("value", p.getPressao());
			atributos.add(pressao);
			
			ObjectNode saturacao = objectMapper.createObjectNode();
			saturacao.put("name", "saturacao");
			saturacao.put("type", "Int");
			saturacao.put("value", p.getSaturacao());
			atributos.add(saturacao);
			
			ObjectNode temperatura = objectMapper.createObjectNode();
			temperatura.put("name", "temperatura");
			temperatura.put("type", "float");
			temperatura.put("value", p.getTemperatura());
			atributos.add(temperatura);
			
			paciente.put("type", "Paciente");
			paciente.put("isPattern", false);
			paciente.put("id", id);
			paciente.put("attributes", atributos);
			
			ArrayNode elementos = objectMapper.createArrayNode();
			elementos.add(paciente);
			ObjectNode pacienteAtualizado = objectMapper.createObjectNode();
			pacienteAtualizado.put("contextElements", elementos);
			pacienteAtualizado.put("updateAction", "UPDATE");
			
			System.out.println(objectMapper.writeValueAsString(pacienteAtualizado));
			
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("http://127.0.0.1:1026/v1/updateContext"))
					.method("POST", HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(pacienteAtualizado)))
					.header("Content-Type", "application/json")
					.build();
			
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			
			return ResponseEntity.ok(response.body());
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
	}
	
	@PostMapping("/subscrever/{id}")
	public ResponseEntity<String> subscreverPaciente(@PathVariable("id") String id){
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			
			ObjectNode paciente = objectMapper.createObjectNode();
			ArrayNode atributos = objectMapper.createArrayNode();
			
			atributos.add("temperatura");
			atributos.add("saturacao");
			atributos.add("pressao");
			
			paciente.put("type", "Paciente");
			paciente.put("isPattern", false);
			paciente.put("id", id);
			
			ArrayNode elementos = objectMapper.createArrayNode();
			elementos.add(paciente);
			ObjectNode req = objectMapper.createObjectNode();
			req.put("entities", elementos);
			req.put("attributes", atributos);
			System.out.println(endereco);
			req.put("reference", "http://"+endereco+":8080/MonitorDadosVitais/subscribe");
			req.put("duration", "P1M");
			ArrayNode notificacao = objectMapper.createArrayNode();
			ObjectNode tipoNotificacao = objectMapper.createObjectNode();
			tipoNotificacao.put("type", "ONCHANGE");
			notificacao.add(tipoNotificacao);
			req.put("notifyConditions", notificacao);
			req.put("throttling", "PT60S");
			
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("http://127.0.0.1:1026/v1/subscribeContext"))
					.method("POST", HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(req)))
					.header("Content-Type", "application/json")
					.build();
			
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			
			return ResponseEntity.ok(response.body());
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
	}
}
