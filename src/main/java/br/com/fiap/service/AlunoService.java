package br.com.fiap.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import br.com.fiap.entity.Aluno;
import br.com.fiap.repository.AlunoRepository;
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

import br.com.fiap.model.AlunoJson;

@RestController
@RequestMapping(path = "/aluno")
public class AlunoService {

	@Autowired
	private AlunoRepository alunoRepository;

	@Transactional
	@RequestMapping(path = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> add(@Valid @RequestBody AlunoJson payload) {

		try {

			Aluno aluno = new Aluno();

			aluno.setMatricula(payload.getMatricula());
			aluno.setNome(payload.getNome());

			alunoRepository.save(aluno);

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"Mensagem\":\"Aluno adicionado com sucesso\"}";

			return new ResponseEntity<>(body, headers, HttpStatus.CREATED);

		} catch (Exception e) {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"Mensagem\":\"Ocorreu um erro\", \"Exceção\":" + e.getMessage() + "}";

			return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
		}

	}

	@Transactional()
	@RequestMapping(value = "/load_from_csv", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> loadFromCsv() {

		List<Aluno> alunos = new ArrayList<Aluno>();

		try {

			BufferedReader csvReader = new BufferedReader(new FileReader("./files/lista_alunos.csv"));
			String row = "";
			while ((row = csvReader.readLine()) != null) {
				String[] data = row.split(";");
				Aluno aluno = new Aluno();

				aluno.setNome(data[0]);
				aluno.setMatricula(data[1]);

				alunos.add(aluno);
//				alunoRepository.save(aluno);
			}
			csvReader.close();

			alunoRepository.saveAll(alunos);

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"Mensagem\":\"Alunos adicionado com sucesso\"}";

			return new ResponseEntity<>(body, headers, HttpStatus.CREATED);

		} catch (Exception e) {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"Mensagem\":\"Ocorreu um erro\", \"Exceção\":" + e.getMessage() + "}";

			return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
		}
	}

	@Transactional
	@RequestMapping(path = "/update/{matricula}", method = RequestMethod.PATCH)
	@ResponseBody
	public ResponseEntity<String> updateAlunoByMatricula(@RequestBody AlunoJson payload,
			@PathVariable("matricula") String matricula) {

		try {

			List<Aluno> alunoList = alunoRepository.findByMatricula(matricula);

			alunoList.forEach(aluno -> {
				aluno.setNome(payload.getNome() == null || payload.getNome().isEmpty()
						? aluno.getNome()
						: payload.getNome());

				
				alunoRepository.save(aluno);
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
	@RequestMapping(path = "/delete/{matricula}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deleteByMatricula(@PathVariable String matricula) {

		try {

			List<Aluno> alunoList = alunoRepository.findByMatricula(matricula);

			alunoList.forEach(aluno -> {
				alunoRepository.deleteById(aluno.getAlunoId());
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
	public Iterable<AlunoJson> getAllAlunos() {
		
		List<AlunoJson> alunosJson = new ArrayList<>();
		
		alunoRepository.findAll().forEach(aluno -> {
			AlunoJson alunoJson = new AlunoJson();

			alunoJson.setMatricula(aluno.getMatricula());
			alunoJson.setNome(aluno.getNome());

			alunosJson.add(alunoJson);
		});
		
		return alunosJson;
		
	}

	@Transactional(readOnly = true)
	@RequestMapping(value = "/nome/{nome}", method = RequestMethod.GET)
	@ResponseBody
	public List<AlunoJson> findByName(@PathVariable String nome) {
		
		List<AlunoJson> alunosJson = new ArrayList<>();
		
		alunoRepository.findByName(nome).forEach(aluno -> {
			AlunoJson alunoJson = new AlunoJson();

			alunoJson.setMatricula(aluno.getMatricula());
			alunoJson.setNome(aluno.getNome());

			alunosJson.add(alunoJson);
		});
		
		return alunosJson;
	}

	@Transactional(readOnly = true)
	@RequestMapping(value = "/matricula/{matricula}", method = RequestMethod.GET)
	@ResponseBody
	public List<AlunoJson> findByMatricula(@PathVariable String matricula) {

		List<AlunoJson> alunosJson = new ArrayList<>();

		alunoRepository.findByMatricula(matricula).forEach(aluno -> {
			AlunoJson alunoJson = new AlunoJson();

			alunoJson.setMatricula(aluno.getMatricula());
			alunoJson.setNome(aluno.getNome());

			alunosJson.add(alunoJson);

			alunosJson.add(alunoJson);
		});

		return alunosJson;
	}

}
