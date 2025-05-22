package org.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import org.model.Fornecedores;
import org.model.Produtos;

import java.util.List;

public class FornecedoresDAO {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("meuPU");

    public void cadastrar(Fornecedores fornecedor){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(fornecedor);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Fornecedores buscarFornecedorPorId(String fornecedor_id){
        EntityManager em = emf.createEntityManager();
        try{
            return em.createQuery("SELECT f FROM Fornecedores f WHERE f.fornecedor_id =: fornecedor_id", Fornecedores.class)
                    .setParameter("fornecedor_id", fornecedor_id)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
        finally {
            em.close();
        }
    }

    public void editar(Fornecedores fornecedor){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.merge(fornecedor);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void excluir(Long fornecedor_id){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Fornecedores fornecedor = em.find(Fornecedores.class, fornecedor_id);
            if (fornecedor != null){
                em.remove(fornecedor);
            }
            em.getTransaction().commit();
        }
    }

    public List<Fornecedores> listar(){
        EntityManager em = emf.createEntityManager();

        try{
            return em.createQuery("SELECT f FROM Fornecedores f", Fornecedores.class).getResultList();
        } finally {
            em.close();
        }
    }
}
