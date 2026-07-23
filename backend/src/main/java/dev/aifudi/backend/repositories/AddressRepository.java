package dev.aifudi.backend.repositories;

import dev.aifudi.backend.entities.Address;

public interface AddressRepository {
    Integer save(Address address);
    Integer update(Address address);
}
