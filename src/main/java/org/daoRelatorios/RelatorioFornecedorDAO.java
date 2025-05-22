package org.daoRelatorios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.model.Estoque;
import org.model.Fornecedores;

import java.util.List;

public class RelatorioFornecedorDAO {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU");

    public List<Fornecedores> listar(){
        EntityManager em = emf.createEntityManager();

        try{
            return em.createQuery("SELECT f FROM Fornecedores f ORDER BY f.nome", Fornecedores.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Fornecedores> listarFornecedorPorId(String fornecedor_id){
        EntityManager em = emf.createEntityManager();
        try{
            return em.createQuery("SELECT f FROM Fornecedores f WHERE f.fornecedor_id =: fornecedor_id"
            , Fornecedores.class)
            .setParameter("fornecedor_id", fornecedor_id)
            .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Fornecedores> listarAtivos(){
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT f FROM Fornecedores f WHERE f.ativo = true";
            return em.createQuery(jpql, Fornecedores.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Fornecedores> listarInativos(){
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT f FROM Fornecedores f WHERE f.ativo = false";
            return em.createQuery(jpql, Fornecedores.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
