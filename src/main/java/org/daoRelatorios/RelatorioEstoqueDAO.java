package org.daoRelatorios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.model.Estoque;
import org.model.Produtos;

import java.util.List;

public class RelatorioEstoqueDAO {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU");

    public List<Estoque> listarEstoque(){
        EntityManager em = emf.createEntityManager();

        try{
            return em.createQuery("SELECT e FROM Estoque e JOIN e.produto p ORDER BY p.nome", Estoque.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Estoque> listarAtivos(){
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT e FROM Estoque e JOIN e.produto p WHERE p.ativo = true";
            return em.createQuery(jpql, Estoque.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Estoque> listarInativos(){
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT e FROM Estoque e JOIN e.produto p WHERE p.ativo = false";
            return em.createQuery(jpql, Estoque.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Estoque> listarPorProdutoId(String produtoId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Produtos p WHERE p.produto_id = :id",
                            Estoque.class)
                    .setParameter("id", produtoId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
