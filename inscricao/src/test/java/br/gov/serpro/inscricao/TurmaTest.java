package br.gov.serpro.inscricao;

import javax.inject.Inject;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import br.gov.frameworkdemoiselle.junit.DemoiselleRunner;
import br.gov.serpro.inscricao.excecoes.TurmaException;

/**
 * 
 * @author 86132113568
 * 
 * Classe de teste a ser chamada pelo JUnit. Usando na programa��o dirigida a testes
 * A classe usa DemoiselleRunner, j� que JUnit n�o suporta CDI.
 */
@RunWith(DemoiselleRunner.class)
public class TurmaTest {
	@Test
	public void matricularAlunoComSucesso() {
		Aluno aluno = new Aluno("Santos Dumont");
		turma.matricularAluno(aluno);
		
		// Usamos Assert do JUnit para checar as condi��es do teste. No caso, se o aluno est� matriculado
		Assert.assertTrue(turma.estaMatriculado(aluno));
	}
	
	// Estes dois m�todos falhar�o de prop�sito, pois esperam RuntimeException
	// Turma.matricularAluno deve disparar esta exception para retornar sucesso no teste
	@Test(expected=TurmaException.class)
	public void falhaAoTentarMatricularAlunoDuplicado() {
		Aluno aluno = new Aluno("Oliver Stone");
		turma.matricularAluno(aluno);
		turma.matricularAluno(aluno);
	}
	
	@Test(expected=TurmaException.class)
	public void falhaAoTentarMatricularAlunoNaTurmaCheia(){
		Aluno aluno = null;
		for (int i = 0; i < 5; i++) {
			aluno = new Aluno("Aluno " + (i+1));
			turma.matricularAluno(aluno);
		}
		
		aluno = new Aluno("Aluno 6");
		turma.matricularAluno(aluno);
	}
	
	@Inject
	private Turma turma;
}
