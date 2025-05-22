// package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository;

// import jakarta.persistence.EntityManager;
// import jakarta.persistence.PersistenceContext;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;

// @DataJpaTest
// public class PurchaseHistoryRepositoryTest {

//     @Autowired
//     private PurchaseHistoryRepository repository;

//     @PersistenceContext
//     private EntityManager entityManager;

//     @Test
//     void testFindFullCustomerPurchaseHistoryRawReturnsCorrectData() {
//         // Insert customer and get its id
//         entityManager.createNativeQuery(
//             "INSERT INTO customers (name, phone_number, is_active) VALUES ('Alice', '0811111111', true)"
//         ).executeUpdate();
//         entityManager.flush();
//         Integer customerId = (Integer) entityManager.createNativeQuery(
//             "SELECT id FROM customers WHERE phone_number = '0811111111'"
//         ).getSingleResult();

//         // Insert product and get its id
//         entityManager.createNativeQuery(
//             "INSERT INTO products (name, price, quantity) VALUES ('Keyboard', 10000, 10)"
//         ).executeUpdate();
//         entityManager.flush();
//         Integer productId = (Integer) entityManager.createNativeQuery(
//             "SELECT id FROM products WHERE name = 'Keyboard'"
//         ).getSingleResult();

//         // Insert sales_transaction referencing customer id
//         entityManager.createNativeQuery(
//             "INSERT INTO sales_transaction (customer_phone, status) VALUES (?, 1)"
//         ).setParameter(1, customerId)
//          .executeUpdate();
//         entityManager.flush();
//         Integer transactionId = (Integer) entityManager.createNativeQuery(
//             "SELECT id FROM sales_transaction WHERE customer_phone = ?"
//         ).setParameter(1, customerId)
//          .getSingleResult();

//         // Insert sales_item referencing transaction id and product id
//         entityManager.createNativeQuery(
//             "INSERT INTO sales_item (transaction_id, product_id, quantity, price) VALUES (?, ?, 2, 10000.0)"
//         ).setParameter(1, transactionId)
//          .setParameter(2, productId)
//          .executeUpdate();
//         entityManager.flush();

//         List<Object[]> result = repository.findFullCustomerPurchaseHistoryRaw("0811111111");

//         assertEquals(1, result.size());
//         Object[] row = result.get(0);
//         assertEquals("Alice", row[0]);
//         assertEquals("0811111111", row[1]);
//         assertEquals(transactionId.intValue(), ((Number) row[2]).intValue());
//         // assertEquals(1, ((Number) row[3]).intValue()); // status as integer, removed
//         assertEquals(productId, ((Number) row[3]).intValue()); // productId as integer
//         assertEquals(2, ((Number) row[4]).intValue());
//         assertEquals(10000.0, ((Number) row[5]).doubleValue());
//     }
// }