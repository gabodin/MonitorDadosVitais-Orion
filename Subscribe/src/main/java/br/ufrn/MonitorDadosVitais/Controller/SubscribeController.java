package br.ufrn.MonitorDadosVitais.Controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/subscribe")
public class SubscribeController {
	
	
	@PostMapping
	public void notificarFamilia(@RequestBody JsonNode body) {
		JsonNode elemento = body.get("contextResponses").get(0).get("contextElement");
		
		String tipoElemento = elemento.get("type").asText();
		String idPaciente = elemento.get("id").asText();
		
		if(tipoElemento.equals("Paciente")) {
			
			Integer saturacao = null;
			Integer pressao = null;
			Double temperatura = null;
			boolean situacaoCritica = false;
			List<String> situacaoPaciente = new ArrayList<String>();
			
			for(JsonNode node : elemento.get("attributes")) {
				if(node.get("name").asText().equals("saturacao")) {
					saturacao = node.get("value").asInt();
				} else if(node.get("name").asText().equals("pressao")) {
					pressao = node.get("value").asInt();
				} else if(node.get("name").asText().equals("temperatura")) {
					temperatura = node.get("value").asDouble();
				}
			}
			
			if(saturacao < 95) {
				situacaoCritica = true;
				situacaoPaciente.add("Saturação de oxigênio menor que o limite (95%). Saturação: " + saturacao + "%.");
			}
			
			if(temperatura < 36.0) {
				situacaoCritica = true;
				situacaoPaciente.add("Temperatura menor que o limite (36ºC). Temperatura: " + temperatura + " ºC.");
			} else if(temperatura >= 38.0) {
				situacaoCritica = true;
				situacaoPaciente.add("Temperatura maior que o limite (38ºC). Temperatura: " + temperatura + " ºC.");
			}
			
			if(pressao < 90) {
				situacaoCritica = true;
				situacaoPaciente.add("Pressão arterial menor que o limite (90 HHmg). Pressão: " + pressao + " HHmg.");
			} else if(pressao > 120) {
				situacaoCritica = true;
				situacaoPaciente.add("Pressão arterial maior que o limite (120 HHmg). Pressão: " + pressao + " HHmg.");
			}
			
			if(situacaoCritica) {
				
				HttpRequest requestEmail = HttpRequest.newBuilder()
						.uri(URI.create("http://127.0.0.1:1026/v1/contextEntities/"+idPaciente+"/attributes/email"))
						.method("GET", HttpRequest.BodyPublishers.noBody())
						.build();
				HttpRequest requestNome = HttpRequest.newBuilder()
						.uri(URI.create("http://127.0.0.1:1026/v1/contextEntities/"+idPaciente+"/attributes/nome"))
						.method("GET", HttpRequest.BodyPublishers.noBody())
						.build();
				
				try {
					HttpResponse<String> responseEmail = HttpClient.newHttpClient().send(requestEmail, HttpResponse.BodyHandlers.ofString());
					HttpResponse<String> responseNome = HttpClient.newHttpClient().send(requestNome, HttpResponse.BodyHandlers.ofString());
					
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode emailNode = objectMapper.readTree(responseEmail.body());
					JsonNode nomeNode = objectMapper.readTree(responseNome.body());
					
					String email = emailNode.get("attributes").get(0).get("value").asText();
					String nome = nomeNode.get("attributes").get(0).get("value").asText();
			        
			        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			        mailSender.setHost("smtp.gmail.com");
			        mailSender.setPort(587);
			        mailSender.setUsername("monitordadosvitais@gmail.com");
			        mailSender.setPassword("tmivvzigswmwvwwq");
			        
			        Properties props = mailSender.getJavaMailProperties();
			        props.put("mail.smtp.starttls.enable", "true");
			        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
			        
			        SimpleMailMessage mensagem = new SimpleMailMessage();
					mensagem.setTo(email);
					mensagem.setSubject("Dados Vitais de " + nome);
					
					String conteudo = "";
			        conteudo += "Dados vitais alterados:\n\n";
			        for (String msg : situacaoPaciente) {
			            conteudo += msg+"\n";
			        }
			        
			        mensagem.setText(conteudo);
			        
			        mailSender.send(mensagem);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("\n\n\n"+body.toPrettyString());
		
	}

}
