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
		
		// L� a capacidade pela classe de configura��o, que obt�m do arquivo de configura��o.
		if (estaMatriculado(aluno) || alunosMatriculados.size()==config.getCapacidadeTurma()) {
			throw new TurmaException();
		}
		
		em.getTransaction().begin();
		em.persist(aluno);
		em.getTransaction().commit();
		
		// Pegamos a string do arquivo de recursos. O nome do aluno, via classe Aluno, � recuperado
		logger.info(bundle.getString("matricula.sucesso", aluno.getNome()));
	}
	
	public boolean estaMatriculado(Aluno aluno)
	{
		List<Aluno> alunosMatriculados = obterAlunosMatriculados();
		return alunosMatriculados.contains(aluno);
	}
	
	// Definimos que o m�todo a seguir � um manipulador de exce��o e ser� chamado pelo framework,
	// quando tratar a exce��o passada como par�metro. No entanto, devemos propagar a exce��o, ou ocorrer�
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
	
	// Inje��o de depend�ncias. Ao inv�s de instanciar diretamente. 
	// A vantagem � que podemos substituir a classe por uma equivalente sem mexer no c�digo
	// No entanto, o c�digo n�o funciona, porque JUnit n�o reconhece CDI do Java. 
	// Classes com m�todos anotados com @Test n�o podem instanciar diretamente esta classe.
	// Corrigimos com o componente demoiselle-junit.
	@Inject
	private Logger logger;
	
	@Inject
	private ResourceBundle bundle;	// Inserimos o manipulador de recursos do Demoiselle
	
	@Inject
	private InscricaoConfig config;	// Classe de configura��o
	
	@Inject
	private EntityManager em;
}
