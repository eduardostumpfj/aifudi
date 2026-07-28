package dev.aifudi.backend.repositories;

import dev.aifudi.backend.dtos.db.AddressUpdateDataDTO;
import dev.aifudi.backend.entities.Address;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

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
    public void update(AddressUpdateDataDTO addressDTO) {
        StringBuilder sql = new StringBuilder("UPDATE address SET ");
        Map<String, Object> params = new HashMap<>();

        if(addressDTO.cep() != null){
            sql.append("cep = :cep, ");
            params.put("cep", addressDTO.cep());
        }
        if(addressDTO.city() != null){
            sql.append("city = :city, ");
            params.put("city", addressDTO.city());
        }
        if(addressDTO.state() != null){
            sql.append("state = :state, ");
            params.put("state", addressDTO.state());
        }
        if(addressDTO.address() != null){
            sql.append("address = :address, ");
            params.put("address", addressDTO.address());
        }
        if(addressDTO.number() != null){
            sql.append("address_number = :number, ");
            params.put("number", addressDTO.number());
        }
        if(addressDTO.complement() != null){
            sql.append("complement = :complement, ");
            params.put("complement", addressDTO.complement());
        }

        if(params.isEmpty()){ return; }

        sql.setLength(sql.length() -2);
        sql.append(" WHERE user_id = :userId");
        params.put("userId", addressDTO.userId());

        JdbcClient.StatementSpec stmt = jdbcClient.sql(sql.toString());
        params.forEach(stmt::param);
        stmt.update();
    }
}
