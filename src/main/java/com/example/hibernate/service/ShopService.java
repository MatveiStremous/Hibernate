package com.example.hibernate.service;
import com.example.hibernate.entity.Shop;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class ShopService {
    private SessionFactory sessionFactory;

    public ShopService() {
          sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public List<Shop> getAllStrings() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Shop> shops = session.createQuery("FROM Shop").list();
        transaction.commit();
        session.close();
        return shops;
    }

    public void addNewShop(Shop shop){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(shop);
        transaction.commit();
        session.close();
    }

    public void deleteByID(int id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Shop shop = session.get(Shop.class, id);
        session.delete(shop);
        transaction.commit();
        session.close();
    }

    public void updateShop(Shop shop){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Shop shopDB = session.get(Shop.class, shop.getId());
        shopDB.setAmountOfWorkers(shop.getAmountOfWorkers());
        shopDB.setAverageSalary(shop.getAverageSalary());
        shopDB.setName(shop.getName());
        shopDB.setProfit(shop.getProfit());
        shopDB.setSquare(shop.getSquare());
        session.update(shopDB);
        transaction.commit();
        session.close();
    }

    public void disConnect(){
        sessionFactory.close();
    }
}
