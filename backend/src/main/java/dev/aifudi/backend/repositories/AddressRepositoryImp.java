package dev.aifudi.backend.repositories;

import dev.aifudi.backend.entities.Address;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class AddressRepositoryImp implements AddressRepository{
    private final JdbcClient jdbcClient;

    public AddressRepositoryImp(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Integer save(Address address) {
        return this.jdbcClient.sql("INSERT INTO address(user_id, cep, state, city, address, address_number, complement) VALUES (:userId, :cep, :state, :city, :address, :number, :complement)")
                .param("userId", address.getUserId())
                .param("cep", address.getCep())
                .param("state", address.getState())
                .param("city", address.getCity())
                .param("address", address.getAddress())
                .param("number", address.getNumber())
                .param("complement", address.getComplement())
                .update();
    }

    @Override
    public Integer update(Address address) {
        return 0;
    }
}
