package com.aip.inventory.repository;

import com.aip.inventory.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findBySkuCodeIn(List<String> skuCodes);
    List<Inventory> findAllByIdIn(List<Long> ids);

    @Modifying
    @Query("UPDATE Inventory i SET i.quantity = :quantity WHERE i.skuCode = :skuCode")
    int bulkUpdateInventory(@Param("skuCode") String skuCode, @Param("quantity") Integer quantity);
}
