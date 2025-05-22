package org.daoRelatorios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.model.Produtos;

import java.util.List;

public class RelatorioProdutosDAO {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU");

    public List<Produtos> listarProdutos(){
        EntityManager em = null;
        try{
            em = emf.createEntityManager();
            return em.createQuery("SELECT p FROM Produtos p ORDER BY p.nome", Produtos.class)
                    .getResultList();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        } finally {
            emf.close();
        }
    }

    public List<Produtos> listarAtivos(){
        EntityManager em = emf.createEntityManager();
        try{
            return em.createQuery("SELECT p FROM Produtos p WHERE p.ativo = true", Produtos.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Produtos> listarInativos(){
        EntityManager em = emf.createEntityManager();
        try{
            return em.createQuery("SELECT p FROM Produtos p WHERE p.ativo = false", Produtos.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Produtos> listarPorProdutoId(String produtoId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Produtos p WHERE p.produto_id = :id",
                            Produtos.class)
                    .setParameter("id", produtoId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
