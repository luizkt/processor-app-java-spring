package br.com.fiap.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.entity.Cliente;
import br.com.fiap.model.ClienteJson;
import br.com.fiap.model.EnderecoJson;
import br.com.fiap.repository.ClienteRepository;

@RestController
@RequestMapping(path = "/cliente")
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Transactional
	@RequestMapping(path = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> add(@Valid @RequestBody ClienteJson payload) {

		try {

		//	ObjectMapper mapper = new ObjectMapper();
		//	ClienteJson clienteJson = mapper.convertValue(payload, ClienteJson.class);
			Cliente cliente = new Cliente();

			cliente.setCpf(payload.getCpf());
			cliente.setNome(payload.getNome());
			cliente.setUuid(payload.getUuid());
			cliente.setEmail(payload.getEmail());
			cliente.setDataNascimento(payload.getDataNascimento());
			cliente.setRua(payload.getEndereco().getRua());
			cliente.setBairro(payload.getEndereco().getBairro());
			cliente.setNumero(payload.getEndereco().getNumero());
			cliente.setCidade(payload.getEndereco().getCidade());
			cliente.setEstado(payload.getEndereco().getEstado());
			cliente.setCep(payload.getEndereco().getCep());
			cliente.setPais(payload.getEndereco().getPais());

			clienteRepository.save(cliente);

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"Mensagem\":\"Cliente adicionado com sucesso\"}";

			return new ResponseEntity<>(body, headers, HttpStatus.CREATED);

		} catch (Exception e) {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"Mensagem\":\"Ocorreu um erro\", \"Exceção\":" + e.getMessage() + "}";

			return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
		}

	}

	@Transactional
	@RequestMapping(path = "/update/{cpf}", method = RequestMethod.PATCH)
	@ResponseBody
	public ResponseEntity<String> updateClienteById(@RequestBody ClienteJson payload,
			@PathVariable("cpf") String cpf) {

		try {

			List<Cliente> clienteList = clienteRepository.findByDocument(cpf);

			ObjectMapper mapper = new ObjectMapper();
			//ClienteJson clienteJson = mapper.convertValue(payload, ClienteJson.class);

			clienteList.forEach(cliente -> {
				cliente.setCpf(cpf);
				cliente.setUuid(cliente.getUuid());
				cliente.setNome(payload.getNome() == null || payload.getNome().isEmpty()
						? cliente.getNome()
						: payload.getNome());
				cliente.setEmail(payload.getEmail() == null || payload.getEmail().isEmpty()
						? cliente.getEmail()
						: payload.getEmail());
				cliente.setDataNascimento(payload.getDataNascimento() == null || payload.getDataNascimento().isEmpty()
						? cliente.getDataNascimento()
						: payload.getDataNascimento());
				if(payload.getEndereco() != null) {
					cliente.setRua(payload.getEndereco().getRua() == null || payload.getEndereco().getRua().isEmpty()
							? cliente.getRua()
							: payload.getEndereco().getRua());
					cliente.setBairro(payload.getEndereco().getBairro() == null || payload.getEndereco().getBairro().isEmpty()
							? cliente.getBairro()
							: payload.getEndereco().getBairro());
					cliente.setNumero(payload.getEndereco().getNumero() == null || payload.getEndereco().getNumero().isEmpty()
							? cliente.getNumero()
							: payload.getEndereco().getNumero());
					cliente.setCidade(payload.getEndereco().getCidade() == null || payload.getEndereco().getCidade().isEmpty()
							? cliente.getCidade()
							: payload.getEndereco().getCidade());
					cliente.setEstado(payload.getEndereco().getEstado() == null || payload.getEndereco().getEstado().isEmpty()
							? cliente.getEstado()
							: payload.getEndereco().getEstado());
					cliente.setCep(payload.getEndereco().getCep() == null || payload.getEndereco().getCep().isEmpty()
							? cliente.getCep()
							: payload.getEndereco().getCep());
					cliente.setPais(payload.getEndereco().getPais() == null || payload.getEndereco().getPais().isEmpty()
							? cliente.getPais()
							: payload.getEndereco().getPais());
				}
				
				clienteRepository.save(cliente);
			});

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"Mensagem\":\"Cliente atualizado com sucesso\"}";

			return new ResponseEntity<>(body, headers, HttpStatus.OK);

		} catch (Exception e) {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"Mensagem\":\"Ocorreu um erro\", \"Exceção\":" + e.getMessage() + "}";

			return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
		}

	}

	@Transactional
	@RequestMapping(path = "/delete/{cpf}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deleteByCpf(@PathVariable String cpf) {

		try {

			List<Cliente> clienteList = clienteRepository.findByDocument(cpf);

			clienteList.forEach(cliente -> {
				clienteRepository.deleteById(cliente.getClienteId());
			});

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"Mensagem\":\"Cliente excluido com sucesso\"}";

			return new ResponseEntity<>(body, headers, HttpStatus.OK);

		} catch (Exception e) {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"Mensagem\":\"Ocorreu um erro\", \"Exceção\":" + e.getMessage() + "}";

			return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
		}

	}

	@Transactional(readOnly = true)
	@RequestMapping(path = "/all", method = RequestMethod.GET)
	@ResponseBody
	public Iterable<ClienteJson> getAllUsers() {
		
		List<ClienteJson> clientesJson = new ArrayList<>();
		
		clienteRepository.findAll().forEach(cliente -> {
			ClienteJson clienteJson = new ClienteJson();
			EnderecoJson enderecoJson = new EnderecoJson();
			
			clienteJson.setCpf(cliente.getCpf());
			clienteJson.setDataNascimento(cliente.getDataNascimento());
			clienteJson.setEmail(cliente.getEmail());
			clienteJson.setNome(cliente.getNome());
			clienteJson.setUuid(cliente.getUuid());
			
			enderecoJson.setBairro(cliente.getBairro());
			enderecoJson.setCep(cliente.getCep());
			enderecoJson.setCidade(cliente.getCidade());
			enderecoJson.setEstado(cliente.getEstado());
			enderecoJson.setNumero(cliente.getNumero());
			enderecoJson.setPais(cliente.getPais());
			enderecoJson.setRua(cliente.getRua());
			clienteJson.setEndereco(enderecoJson);
			
			clientesJson.add(clienteJson);	
		});
		
		return clientesJson;
		
	}

	@Transactional(readOnly = true)
	@RequestMapping(value = "/nome/{nome}", method = RequestMethod.GET)
	@ResponseBody
	public List<ClienteJson> findByName(@PathVariable String nome) {
		
		List<ClienteJson> clientesJson = new ArrayList<>();
		
		clienteRepository.findByName(nome).forEach(cliente -> {
			ClienteJson clienteJson = new ClienteJson();
			EnderecoJson enderecoJson = new EnderecoJson();
			
			clienteJson.setCpf(cliente.getCpf());
			clienteJson.setDataNascimento(cliente.getDataNascimento());
			clienteJson.setEmail(cliente.getEmail());
			clienteJson.setNome(cliente.getNome());
			clienteJson.setUuid(cliente.getUuid());
			
			enderecoJson.setBairro(cliente.getBairro());
			enderecoJson.setCep(cliente.getCep());
			enderecoJson.setCidade(cliente.getCidade());
			enderecoJson.setEstado(cliente.getEstado());
			enderecoJson.setNumero(cliente.getNumero());
			enderecoJson.setPais(cliente.getPais());
			enderecoJson.setRua(cliente.getRua());
			clienteJson.setEndereco(enderecoJson);
			
			clientesJson.add(clienteJson);	
		});
		
		return clientesJson;
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/uuid/{uuid}", method = RequestMethod.GET)
	@ResponseBody
	public List<ClienteJson> findByUuid(@PathVariable String uuid) {
		
		List<ClienteJson> clientesJson = new ArrayList<>();
		
		clienteRepository.findByUuid(uuid).forEach(cliente -> {
			ClienteJson clienteJson = new ClienteJson();
			EnderecoJson enderecoJson = new EnderecoJson();
			
			clienteJson.setCpf(cliente.getCpf());
			clienteJson.setDataNascimento(cliente.getDataNascimento());
			clienteJson.setEmail(cliente.getEmail());
			clienteJson.setNome(cliente.getNome());
			clienteJson.setUuid(cliente.getUuid());
			
			enderecoJson.setBairro(cliente.getBairro());
			enderecoJson.setCep(cliente.getCep());
			enderecoJson.setCidade(cliente.getCidade());
			enderecoJson.setEstado(cliente.getEstado());
			enderecoJson.setNumero(cliente.getNumero());
			enderecoJson.setPais(cliente.getPais());
			enderecoJson.setRua(cliente.getRua());
			clienteJson.setEndereco(enderecoJson);
			
			clientesJson.add(clienteJson);	
		});
		
		return clientesJson;
	}

	@Transactional(readOnly = true)
	@RequestMapping(value = "/cpf/{cpf}", method = RequestMethod.GET)
	@ResponseBody
	public List<ClienteJson> findByDocument(@PathVariable String cpf) {
		
		List<ClienteJson> clientesJson = new ArrayList<>();
		
		
		clienteRepository.findByDocument(cpf).forEach(cliente -> {
			ClienteJson clienteJson = new ClienteJson();
			EnderecoJson enderecoJson = new EnderecoJson();
			
			clienteJson.setCpf(cliente.getCpf());
			clienteJson.setDataNascimento(cliente.getDataNascimento());
			clienteJson.setEmail(cliente.getEmail());
			clienteJson.setNome(cliente.getNome());
			clienteJson.setUuid(cliente.getUuid());
			
			enderecoJson.setBairro(cliente.getBairro());
			enderecoJson.setCep(cliente.getCep());
			enderecoJson.setCidade(cliente.getCidade());
			enderecoJson.setEstado(cliente.getEstado());
			enderecoJson.setNumero(cliente.getNumero());
			enderecoJson.setPais(cliente.getPais());
			enderecoJson.setRua(cliente.getRua());
			clienteJson.setEndereco(enderecoJson);
			
			clientesJson.add(clienteJson);	
		});
		
		return clientesJson;
	}

}
