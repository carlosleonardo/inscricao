package br.gov.serpro.inscricao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import br.gov.frameworkdemoiselle.exception.ExceptionHandler;
import br.gov.frameworkdemoiselle.stereotype.Controller;
import br.gov.frameworkdemoiselle.util.ResourceBundle;
import br.gov.serpro.inscricao.config.InscricaoConfig;
import br.gov.serpro.inscricao.excecoes.TurmaException;

@Controller
public class Turma {
	public void matricularAluno(Aluno aluno)
	{
		List<Aluno> alunosMatriculados = obterAlunosMatriculados();
		
		// Lê a capacidade pela classe de configuração, que obtém do arquivo de configuração.
		if (estaMatriculado(aluno) || alunosMatriculados.size()==config.getCapacidadeTurma()) {
			throw new TurmaException();
		}
		
		em.getTransaction().begin();
		em.persist(aluno);
		em.getTransaction().commit();
		
		// Pegamos a string do arquivo de recursos. O nome do aluno, via classe Aluno, é recuperado
		logger.info(bundle.getString("matricula.sucesso", aluno.getNome()));
	}
	
	public boolean estaMatriculado(Aluno aluno)
	{
		List<Aluno> alunosMatriculados = obterAlunosMatriculados();
		return alunosMatriculados.contains(aluno);
	}
	
	// Definimos que o método a seguir é um manipulador de exceção e será chamado pelo framework,
	// quando tratar a exceção passada como parâmetro. No entanto, devemos propagar a exceção, ou ocorrerá
	// uma falha.
	@ExceptionHandler
	public void tratarErro(TurmaException rte)
	{
		logger.warn(bundle.getString("matricula.erro", config.getCapacidadeTurma()));
		throw rte;
	}
	
	private List<Aluno> obterAlunosMatriculados(){
		return em.createQuery("select a from Aluno a").getResultList();
	}
	
	// Injeção de dependências. Ao invés de instanciar diretamente. 
	// A vantagem é que podemos substituir a classe por uma equivalente sem mexer no código
	// No entanto, o código não funciona, porque JUnit não reconhece CDI do Java. 
	// Classes com métodos anotados com @Test não podem instanciar diretamente esta classe.
	// Corrigimos com o componente demoiselle-junit.
	@Inject
	private Logger logger;
	
	@Inject
	private ResourceBundle bundle;	// Inserimos o manipulador de recursos do Demoiselle
	
	@Inject
	private InscricaoConfig config;	// Classe de configuração
	
	@Inject
	private EntityManager em;
}
