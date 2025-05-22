package org.dao;

import jakarta.persistence.*;
import org.model.Estoque;
import org.model.Produtos;

public class EstoqueDAO {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("meuPU");

    public void cadastrar(Estoque estoque, Long produto_id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Produtos produto = em.find(Produtos.class, produto_id);

            if (produto == null){
                throw new IllegalArgumentException("Produto não encontrado com o ID:" + produto_id);
            }

            estoque.setProduto(produto);
            em.persist(estoque);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Estoque buscarEstoquePorProdutoId(String produtoId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT e FROM Estoque e WHERE e.produto.produto_id = :id", Estoque.class)
                    .setParameter("id", produtoId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public boolean verificarEntradas(Long produtoId) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT e FROM Estoque e WHERE e.produto.produto_id = :produtoId";
            Estoque estoque = em.createQuery(jpql, Estoque.class)
                    .setParameter("produtoId", produtoId)
                    .getSingleResult();

            return estoque.getEntradas() > 0;
        } catch (NoResultException e) {
            // Não há estoque associado a esse produto ainda
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }




    public void atualizar(Estoque estoque, Long produto_id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // Busca o produto correspondente ao estoque
            Produtos produto = em.find(Produtos.class, produto_id);
            if (produto == null) {
                throw new IllegalArgumentException("Produto com ID " + produto_id + " não encontrado.");
            }

            // Garante que o estoque está ligado ao produto
            estoque.setProduto(produto);

            em.merge(estoque); // merge precisa do produto setado!
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}





