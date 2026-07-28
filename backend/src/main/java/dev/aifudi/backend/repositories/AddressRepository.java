package dev.aifudi.backend.repositories;

import dev.aifudi.backend.dtos.db.AddressUpdateDataDTO;
import dev.aifudi.backend.entities.Address;

public interface AddressRepository {
    Integer save(Address address);
    void update(AddressUpdateDataDTO address);
}
