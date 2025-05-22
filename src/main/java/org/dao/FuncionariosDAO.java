package org.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import org.model.Funcionarios;

import java.util.List;

public class FuncionariosDAO {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("meuPU");

    public void salvar(Funcionarios funcionario){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(funcionario);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void editar(Funcionarios funcionario){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.merge(funcionario);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }

    public void excluir(Long funcionario_id){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Funcionarios funcionario = em.find(Funcionarios.class, funcionario_id);
            if (funcionario != null){
                em.remove(funcionario);
            }
            em.getTransaction().commit();
        }
    }

    public Funcionarios buscarPorNome(String nome) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT f FROM Funcionarios f WHERE lower(f.nome) = :nome", Funcionarios.class)
                    .setParameter("nome", nome.toLowerCase())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<Funcionarios> listar(){
        EntityManager em = emf.createEntityManager();

        try{
            return em.createQuery("SELECT f FROM Funcionarios f", Funcionarios.class).getResultList();
        } finally {
            em.close();
        }
    }
}
