package io.github.Matheus251170.repositories;

import io.github.Matheus251170.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    List<Client> findByNome(String cli);

//    @Query("select c from Client c left join fetch c.pedidos where c.id = :id")
//    Client findClientFetchPedidos(Integer id);


//    -----------------------------------------------------------------------------------


//    private static String INSERT = "insert into client (nome) values (?)";
//    private static String SELECT_ALL = "select * from client";
//    private static String UPDATE = "update client set nome = ? where id = ?";
//
//    private static String DELETE = "delete from client where id = ?";

//    @Autowired
//    private JdbcTemplate jdbcTemplate;

//    @Autowired
//    private EntityManager entityManager;

//    @Transactional
//    public Client salvar(Client client) {
////      jdbcTemplate.update(INSERT, new Object[]{client.getNome()});
//        entityManager.persist(client);
//        return client;
//    }
//
//    @Transactional
//    public Client updateClient(Client client) {
////        jdbcTemplate.update(UPDATE, new Object[]{client.getNome(), client.getId()});
//        entityManager.merge(client);
//        return client;
//    }
//
//    @Transactional
//    public Client deleteClient(Client client) {
//        if(!entityManager.contains(client)){
//            client = entityManager.merge(client);
//        }
////        jdbcTemplate.update(DELETE, new Object[]{client.getId()});
//        entityManager.remove(client);
//        return client;
//    }
//
//    @Transactional(readOnly = true)
//    public List<Client> getByName(String nome) {
////        return jdbcTemplate.query(SELECT_ALL.concat(" where nome like (?)"), new Object[]{"%" + nome + "%"}, getClientMapper());
//        String jpql = "select c from Client c where c.nome like :nome";
//        TypedQuery<Client> query = entityManager.createQuery(jpql, Client.class);
//        query.setParameter("nome", "%" + nome + "%");
//        return query.getResultList();
//    }
//
//    @Transactional(readOnly = true)
//    public List<Client> getAll(){
//       return entityManager.createQuery("from Client", Client.class).getResultList();
////        return jdbcTemplate.query(SELECT_ALL, getClientMapper());
//    }

//    private RowMapper<Client> getClientMapper() {
//        return new RowMapper<Client>() {
//            @Override
//            public Client mapRow(ResultSet resultSet, int i) throws SQLException {
//                Integer id = resultSet.getInt("id");
//                String nome = resultSet.getString("nome");
//                return new Client(nome, id);
//            }
//        };
//    }
}
