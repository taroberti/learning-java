package com.example.coursera.model;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.coursera.model.CassandraConnector;

public class DishesTable {
    CassandraConnector connector;
    String node;
    Integer port;
    String keyspaceName;
    String TABLE_NAME;

    public DishesTable (String node, Integer port, String keyspaceName, String TABLE_NAME) {
        this.node = node;
        this.port = port;
        this.keyspaceName = keyspaceName;
        this.TABLE_NAME = TABLE_NAME;

        this.connector = new CassandraConnector();
    }

    public void createKeyspace(String replicationStrategy, int replicationFactor) {
        this.connector.connect(this.node, this.port);
        Session session = connector.getSession();

        StringBuilder sb =
                new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
                        .append(this.keyspaceName).append(" WITH replication = {")
                        .append("'class':'").append(replicationStrategy)
                        .append("','replication_factor':").append(replicationFactor)
                        .append("};");

        String query = sb.toString();
        session.execute(query);

        connector.close();
    }

    public void checkKeyspace() {
        this.connector.connect(this.node, this.port);
        Session session = connector.getSession();

        ResultSet result = session.execute("SELECT * FROM system_schema.keyspaces;");

        List<String> matchedKeyspaces = result.all()
                .stream()
                .filter(r -> r.getString(0).equals(this.keyspaceName.toLowerCase()))
                .map(r -> r.getString(0))
                .collect(Collectors.toList());

        System.out.println("Size: " + matchedKeyspaces.size());
        System.out.println("Keyspace name: " + matchedKeyspaces.get(0));

        this.connector.close();
    }

    public void createTable() {
        this.connector.connect(this.node, this.port);
        Session session = connector.getSession();

        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(this.keyspaceName).append(".").append(this.TABLE_NAME)
                .append("(")
                .append("id uuid PRIMARY KEY, ")
                .append("name text,")
                .append("description text);");

        String query = sb.toString();
        session.execute(query);

        this.connector.close();
    }

    public void checkTable() {
        this.connector.connect(this.node, this.port);
        Session session = connector.getSession();

        ResultSet result = session.execute("SELECT * FROM " + this.keyspaceName + "." + this.TABLE_NAME + ";");

        List<String> columnNames = result.getColumnDefinitions()
                .asList()
                .stream()
                .map(cl -> cl.getName())
                .collect(Collectors.toList());

        System.out.println("ColumnNames: " + columnNames.toString());

        this.connector.close();
    }

    public void insertTable(Dish dish) {
        this.connector.connect(this.node, this.port);
        Session session = connector.getSession();

        StringBuilder sb = new StringBuilder("INSERT INTO ")
                .append(keyspaceName).append(".").append(TABLE_NAME)
                .append("(id, name, description) ")
                .append("VALUES (")
                .append(dish.getId()).append(", '")
                .append(dish.getName()).append("', '")
                .append(dish.getDescription()).append("');");

        String query = sb.toString();
        session.execute(query);

        this.connector.close();
    }

    public void updateTable(Dish dish) {
        this.connector.connect(this.node, this.port);
        Session session = connector.getSession();

        StringBuilder sb = new StringBuilder("UPDATE ")
                .append(keyspaceName).append(".").append(TABLE_NAME)
                .append(" SET name = '").append(dish.getName())
                .append("', description = '").append(dish.getDescription())
                .append("' WHERE id = ").append(dish.getId()).append(";");

        String query = sb.toString();
        session.execute(query);

        this.connector.close();
    }

    public List<Dish> selectAllDishes() {
        this.connector.connect(this.node, this.port);
        Session session = connector.getSession();

        StringBuilder sb =
                new StringBuilder("SELECT * FROM ")
                        .append(keyspaceName).append(".").append(TABLE_NAME);

        String query = sb.toString();
        ResultSet rs = session.execute(query);

        List<Dish> dishes = new ArrayList<Dish>();

        rs.forEach(r -> {
            Dish dish = new Dish(r.getUUID("id").toString(),
                    r.getString("name"),
                    r.getString("description"));
            dishes.add(dish);
        });

        this.connector.close();

        return dishes;
    }

    public Dish selectDishById(String id) {
        this.connector.connect(this.node, this.port);
        Session session = connector.getSession();

        StringBuilder sb =
                new StringBuilder("SELECT name, description FROM ")
                        .append(keyspaceName).append(".").append(TABLE_NAME)
                        .append(" WHERE id = ").append(id).append(";");

        String query = sb.toString();
        ResultSet rs = session.execute(query);

        this.connector.close();

        Row r = rs.one();

        return new Dish(id, r.getString("name"), r.getString("description"));
    }

    public void checkDish() {
        List<Dish> dishes = this.selectAllDishes();

        dishes.forEach(dish -> {
            System.out.println("Id: " + dish.getId());
            System.out.println("Name: " + dish.getName());
            System.out.println("Description: " + dish.getDescription());
        });
        /*this.connector.connect(this.node, this.port);
        Session session = connector.getSession();

        StringBuilder sb =
                new StringBuilder("SELECT * FROM ")
                        .append(keyspaceName).append(".").append(TABLE_NAME);

        String query = sb.toString();
        ResultSet rs = session.execute(query);

        //List<Dish> dishes = new ArrayList<Dish>();

        rs.forEach(r -> {
            System.out.println("Id: " + r.getUUID("id"));
            System.out.println("Name: " + r.getString("name"));
            System.out.println("Description: " + r.getString("description"));
        });

        this.connector.close();
        */
    }

//    public void alterTable(String keyspaceName, String TABLE_NAME, String columnName, String columnType) {
//        StringBuilder sb = new StringBuilder("ALTER TABLE ")
//                .append(keyspaceName).append(".").append(TABLE_NAME)
//                .append(" ADD ")
//                .append(columnName).append(" ")
//                .append(columnType).append(";");
//
//        String query = sb.toString();
//        session.execute(query);
//    }
//
//    public void createTableBooksByTitle(String keyspaceName, String TABLE_NAME) {
//        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
//                .append(keyspaceName).append(".").append(TABLE_NAME)
//                .append("(")
//                .append("id uuid PRIMARY KEY, ")
//                .append("title text);");
//
//        String query = sb.toString();
//        session.execute(query);
//    }
//
//    public void insertBookByTitle(String keyspaceName, String TABLE_NAME, Book book) {
//        StringBuilder sb = new StringBuilder("INSERT INTO ")
//                .append(keyspaceName).append(".").append(TABLE_NAME)
//                .append("(id, title) ")
//                .append("VALUES (").append(book.getId())
//                .append(", '").append(book.getTitle()).append("');");
//
//        String query = sb.toString();
//        session.execute(query);
//    }
//
//    public void insertBookBatch(String keyspaceName, String TABLE_NAME, String TABLE_NAME_BY_TITLE, Book book) {
//        StringBuilder sb = new StringBuilder("BEGIN BATCH ")
//                .append("INSERT INTO ")
//                .append(keyspaceName).append(".").append(TABLE_NAME)
//                .append("(id, title, subject) ")
//                .append("VALUES (").append(book.getId()).append(", '")
//                .append(book.getTitle()).append("', '")
//                .append(book.getSubject()).append("');")
//                .append("INSERT INTO ")
//                .append(keyspaceName).append(".").append(TABLE_NAME_BY_TITLE)
//                .append("(id, title) ")
//                .append("VALUES (").append(book.getId()).append(", '")
//                .append(book.getTitle()).append("');")
//                .append("APPLY BATCH;");
//
//        String query = sb.toString();
//        session.execute(query);
//    }
//
//    public void checkBooks(String keyspaceName, String TABLE_NAME) {
//        StringBuilder sb =
//                new StringBuilder("SELECT * FROM ")
//                        .append(keyspaceName).append(".").append(TABLE_NAME);
//
//        String query = sb.toString();
//        ResultSet rs = session.execute(query);
//
//        List<Book> books = new ArrayList<Book>();
//
//        rs.forEach(r -> {
//            System.out.println("Id: " + r.getUUID("id"));
//            System.out.println("Title: " + r.getString("title"));
//        });
//    }
//
//    public void deleteTable(String keyspaceName, String TABLE_NAME) {
//        StringBuilder sb =
//                new StringBuilder("DROP TABLE IF EXISTS ")
//                        .append(keyspaceName).append(".").append(TABLE_NAME);
//
//        String query = sb.toString();
//        session.execute(query);
//    }
//
//    public void deleteKeyspace(String keyspaceName) {
//        StringBuilder sb =
//                new StringBuilder("DROP KEYSPACE ").append(keyspaceName);
//
//        String query = sb.toString();
//        session.execute(query);
//    }
}
