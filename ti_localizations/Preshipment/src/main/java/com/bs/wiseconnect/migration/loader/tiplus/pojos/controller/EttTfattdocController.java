package com.bs.wiseconnect.migration.loader.tiplus.pojos.controller;
 
import com.bs.wiseconnect.migration.loader.tiplus.pojos.TiattdocExtra;
import com.bs.wiseconnect.migration.loader.tiplus.pojos.controller.exceptions.NonexistentEntityException;
import com.bs.wiseconnect.migration.loader.tiplus.pojos.controller.exceptions.PreexistingEntityException;
import com.misys.tiplus2.services.control.StatusEnum;

import java.io.Serializable;

import java.util.List;

import javax.persistence.EntityManager;

import javax.persistence.EntityManagerFactory;

import javax.persistence.EntityNotFoundException;

import javax.persistence.EntityTransaction;

import javax.persistence.Query;

import javax.persistence.criteria.CriteriaBuilder;

import javax.persistence.criteria.CriteriaQuery;

import javax.persistence.criteria.Root;
 
public class EttTfattdocController

  implements Serializable

{

  public EttTfattdocController(EntityManagerFactory emf)

  {

    this.emf = emf;

  }

  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager()

  {

    return this.emf.createEntityManager();

  }

  public void create(TiattdocExtra fgbdmAccounts)

    throws PreexistingEntityException, Exception

  {

    EntityManager em = null;

    try

    {

      em = getEntityManager();

      em.getTransaction().begin();

      em.persist(fgbdmAccounts);

      em.getTransaction().commit();

    }

    catch (Exception ex)

    {

      if (findFgbdmAccounts(fgbdmAccounts.getLogid()) != null) {

        throw new PreexistingEntityException("Tiattdoc " + fgbdmAccounts + " already exists.", ex);

      }

      throw ex;

    }

    finally

    {

      if (em != null) {

        em.close();

      }

    }

  }

  public void edit(TiattdocExtra fgbdmAccounts)

    throws NonexistentEntityException, Exception

  {

    EntityManager em = null;

    try

    {

      em = getEntityManager();

      em.getTransaction().begin();

      fgbdmAccounts = (TiattdocExtra)em.merge(fgbdmAccounts);

      em.getTransaction().commit();

    }

    catch (Exception ex)

    {

      String msg = ex.getLocalizedMessage();

      if ((msg == null) || (msg.length() == 0))

      {

        String id = fgbdmAccounts.getLogid();

        if (findFgbdmAccounts(id) == null) {

          throw new NonexistentEntityException("The fgbdmAccounts with id " + id + " no longer exists.");

        }

      }

      throw ex;

    }

    finally

    {

      if (em != null) {

        em.close();

      }

    }

  }

  public void destroy(String id)

    throws NonexistentEntityException

  {

    EntityManager em = null;

    try

    {

      em = getEntityManager();

      em.getTransaction().begin();

      try

      {

        TiattdocExtra fgbdmAccounts = (TiattdocExtra)em.getReference(TiattdocExtra.class, id);

        fgbdmAccounts.getLogid();

      }

      catch (EntityNotFoundException enfe)

      {

        throw new NonexistentEntityException("The fgbdmAccounts with id " + id + " no longer exists.", enfe);

      }

      TiattdocExtra fgbdmAccounts;

      em.remove(fgbdmAccounts);

      em.getTransaction().commit();

    }

    finally

    {

      if (em != null) {

        em.close();

      }

    }

  }

  public List<TiattdocExtra> invoiceEntities()

  {

    return invoiceEntities(true, -1, -1);

  }

  public List<TiattdocExtra> findInvoiceCustomer(int maxResults, int firstResult)

  {

    return invoiceEntities(false, maxResults, firstResult);

  }

  private List<TiattdocExtra> invoiceEntities(boolean all, int maxResults, int firstResult)

  {

    EntityManager em = getEntityManager();

    try

    {

      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();

      cq.select(cq.from(TiattdocExtra.class));

      Query q = em.createQuery(cq);

      if (!all)

      {

        q.setMaxResults(maxResults);

        q.setFirstResult(firstResult);

      }

      return q.getResultList();

    }

    finally

    {

      em.close();

    }

  }

  public TiattdocExtra findFgbdmAccounts(String id)

  {

    EntityManager em = getEntityManager();

    try

    {

      return (TiattdocExtra)em.find(TiattdocExtra.class, id);

    }

    finally

    {

      em.close();

    }

  }

  public int getFgbdmAccountsCount()

  {

    EntityManager em = getEntityManager();

    try

    {

      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();

      Root<TiattdocExtra> rt = cq.from(TiattdocExtra.class);

      cq.select(em.getCriteriaBuilder().count(rt));

      Query q = em.createQuery(cq);

      return ((Long)q.getSingleResult()).intValue();

    }

    finally

    {

      em.close();

    }

  }

  public void statusUpdate(StatusEnum statusEnum, String logID, String errorDetails)

    throws NonexistentEntityException, Exception

  {

    TiattdocExtra fis = new TiattdocExtra();

 
    EntityManager em = null;

    try

    {

      em = getEntityManager();

      fis = (TiattdocExtra)em.createQuery("SELECT q FROM TFATTDOCTBL q WHERE q.logid = :logid").setParameter("logid", logID).getSingleResult();

 
 
      em.getTransaction().begin();

      fis = (TiattdocExtra)em.merge(fis);

      em.getTransaction().commit();

    }

    catch (Exception ex)

    {

      throw ex;

    }

    finally

    {

      if (em != null) {

        em.close();

      }

    }

  }

}