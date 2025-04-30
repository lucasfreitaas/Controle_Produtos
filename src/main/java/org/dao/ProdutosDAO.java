package org.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.model.Produtos;

import java.util.List;

public class ProdutosDAO {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("meuPU");

    public void cadastrar(Produtos produtos){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(produtos);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Produtos> listar(){
        EntityManager em = emf.createEntityManager();

        try{
            return em.createQuery("SELECT p FROM Produtos p", Produtos.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void editar(Produtos produtos) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(produtos);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void excluir(Long produto_id){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Produtos produtos = em.find(Produtos.class, produto_id);
            if (produtos != null){
                em.remove(produtos);
            }
            em.getTransaction().commit();
        }
    }
}
