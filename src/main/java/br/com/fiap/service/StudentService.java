package br.com.fiap.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import br.com.fiap.entity.Student;
import br.com.fiap.repository.StudentRepository;
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

import br.com.fiap.model.StudentJson;

@RestController
@RequestMapping(path = "/student")
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Transactional
	@RequestMapping(path = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> add(@Valid @RequestBody StudentJson payload) {

		try {

			Student student = new Student();

			student.setStudentRegistrationNumber(payload.getStudentRegistrationNumber());
			student.setName(payload.getName());

			studentRepository.save(student);

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"message\":\"Added the student successfully\"}";

			return new ResponseEntity<>(body, headers, HttpStatus.CREATED);

		} catch (Exception e) {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"message\":\"An error has occurred\", \"Exception\":" + e.getMessage() + "}";

			return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
		}

	}

	@Transactional()
	@RequestMapping(value = "/load_from_csv", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> loadFromCsv() {

		List<Student> students = new ArrayList<>();

		try {

			BufferedReader csvReader = new BufferedReader(new FileReader("./files/lista_alunos.csv"));
			String row;
			while ((row = csvReader.readLine()) != null) {
				String[] data = row.split(";");
				Student student = new Student();

				student.setName(data[0]);
				student.setStudentRegistrationNumber(Integer.parseInt(data[1]));

				students.add(student);
			}
			csvReader.close();

			studentRepository.saveAll(students);

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"message\":\"Added all the students successfully\"}";

			return new ResponseEntity<>(body, headers, HttpStatus.CREATED);

		} catch (Exception e) {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"message\":\"An error has occurred\", \"Exception\":" + e.getMessage() + "}";

			return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
		}
	}

	@Transactional
	@RequestMapping(path = "/update/{studentRegistrationNumber}", method = RequestMethod.PATCH)
	@ResponseBody
	public ResponseEntity<String> updateStudentByStudentRegistrationNumber(@RequestBody StudentJson payload,
			@PathVariable("studentRegistrationNumber") Integer studentRegistrationNumber) {

		try {

			List<Student> studentList = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

			studentList.forEach(student -> {
				student.setName(payload.getName() == null || payload.getName().isEmpty()
						? student.getName()
						: payload.getName());

				
				studentRepository.save(student);
			});

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"Message\":\"Updated the student successfully\"}";

			return new ResponseEntity<>(body, headers, HttpStatus.OK);

		} catch (Exception e) {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"message\":\"An error has occurred\", \"Exception\":" + e.getMessage() + "}";

			return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
		}

	}

	@Transactional
	@RequestMapping(path = "/delete/{studentRegistrationNumber}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deleteStudentByStudentRegistrationNumber(@PathVariable Integer studentRegistrationNumber) {

		try {

			List<Student> studentList = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

			studentList.forEach(student -> studentRepository.deleteById(student.getStudentRegistrationNumber()));

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"Mensagem\":\"Cliente excluido com sucesso\"}";

			return new ResponseEntity<>(body, headers, HttpStatus.OK);

		} catch (Exception e) {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String body = "{\"message\":\"An error has occurred\", \"Exception\":" + e.getMessage() + "}";

			return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
		}

	}

	@Transactional(readOnly = true)
	@RequestMapping(path = "/all", method = RequestMethod.GET)
	@ResponseBody
	public Iterable<StudentJson> getAllAlunos() {
		
		List<StudentJson> alunosJson = new ArrayList<>();
		
		studentRepository.findAll().forEach(student -> {
			StudentJson studentJson = new StudentJson();

			studentJson.setStudentRegistrationNumber(student.getStudentRegistrationNumber());
			studentJson.setName(student.getName());

			alunosJson.add(studentJson);
		});
		
		return alunosJson;
		
	}

	@Transactional(readOnly = true)
	@RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
	@ResponseBody
	public List<StudentJson> findByName(@PathVariable String name) {
		
		List<StudentJson> alunosJson = new ArrayList<>();
		
		studentRepository.findByName(name).forEach(student -> {
			StudentJson studentJson = new StudentJson();

			studentJson.setStudentRegistrationNumber(student.getStudentRegistrationNumber());
			studentJson.setName(student.getName());

			alunosJson.add(studentJson);
		});
		
		return alunosJson;
	}

	@Transactional(readOnly = true)
	@RequestMapping(value = "/studentRegistrationNumber/{studentRegistrationNumber}", method = RequestMethod.GET)
	@ResponseBody
	public List<StudentJson> findByMatricula(@PathVariable Integer studentRegistrationNumber) {

		List<StudentJson> alunosJson = new ArrayList<>();

		studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber).forEach(student -> {
			StudentJson studentJson = new StudentJson();

			studentJson.setStudentRegistrationNumber(student.getStudentRegistrationNumber());
			studentJson.setName(student.getName());

			alunosJson.add(studentJson);

			alunosJson.add(studentJson);
		});

		return alunosJson;
	}

}
