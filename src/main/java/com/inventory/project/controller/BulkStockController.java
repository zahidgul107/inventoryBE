package com.inventory.project.controller;

import com.inventory.project.model.*;
import com.inventory.project.repository.*;
import com.inventory.project.serviceImpl.IncomingStockService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bulkstock")
@CrossOrigin("*")
public class BulkStockController {
    @Autowired
    EntityRepository entityRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    CategoryRepository  categoryRepository;

    @Autowired
    BrandRepository  brandRepository;
    @Autowired
    InventoryRepository inventoryRepo;
    @Autowired
    UnitRepository unitRepository;

@Autowired
    IncomingStockService incomingStockService;
@Autowired
    BulkStockRepo bulkStockRepo;


//    @PostMapping("/add")
//    public ResponseEntity<String> addIncomingStock(@RequestBody BulkStockDto bulkStockDto) {
//
//        BulkStock bulkStock = new BulkStock();
//        bulkStock.setQuantity(bulkStockDto.getQuantity());
//        bulkStock.setUnitPrice(bulkStockDto.getUnitPrice());
//        bulkStock.setExtendedValue(bulkStockDto.getExtendedValue());
//        bulkStock.setDate(bulkStockDto.getDate());
//
//        bulkStock.setPurchaseOrder(bulkStockDto.getPurchaseOrder());
//        bulkStock.setPn(bulkStockDto.getPn());
//        bulkStock.setSn(bulkStockDto.getSn());
////        incomingStock.setBlindCount(incomingStockRequest.getBlindCount());
//        bulkStock.setPrice(bulkStockDto.getPrice());
//        bulkStock.setRemarks(bulkStockDto.getRemarks());
//        bulkStock.setStandardPrice(bulkStockDto.getStandardPrice());
////        bulkStock.setStatus(bulkStockDto.getStatus());
//        bulkStock.setImpaCode(bulkStockDto.getImpaCode());
//        bulkStock.setStoreNo(bulkStockDto.getStoreNo());
//
//        Item item = new Item();
//        item.setDescription(bulkStockDto.getDescription());
//        Location location = locationRepository.findByLocationName((bulkStockDto.getLocationName()));
//        Address address = addressRepository.findByAddress(bulkStockDto.getAddress());
//      Currency currency = currencyRepository.findTopByCurrencyName((bulkStockDto.getCurrencyName()));
//        Category category=categoryRepository.findByName((bulkStockDto.getName()));
//        Brand  brand =brandRepository.findByBrandName(bulkStockDto.getBrandName());
//        Unit unit=unitRepository.findByUnitName(bulkStockDto.getUnitName());
//        Optional<Inventory> optionalInventory = Optional.ofNullable(inventoryRepo.findAllByQuantity(bulkStockDto.getQuantity()));
//
//        if (optionalInventory.isPresent()) {
//            Inventory inventory = optionalInventory.get();
//
//            int newQuantity = inventory.getQuantity() + bulkStockDto.getQuantity();
//            inventory.setQuantity(newQuantity);
//
//            // Save the updated Inventory entity
//            inventoryRepo.save(inventory);
//        } else {
//            // Handle case when Inventory entity with given ID is not found
//            return ResponseEntity.badRequest().body("Inventory not found for the given ID");
//        }        Entity entity=entityRepository.findByEntityName(bulkStockDto.getEntityName());
//
//        StringBuilder errorMessages = new StringBuilder();
//
//        if (item == null) {
//            errorMessages.append("Item not found. ");
//        }
//        if (location == null) {
//            errorMessages.append("Location not found. ");
//        }
//
//        if (address == null || location == null || !address.getLocation().equals(location)) {
//            errorMessages.append("Invalid address or location association. ");
//        }
////        if (currency == null) {
////            errorMessages.append("Currency not found. ");
////        }
//        if (category == null) {
//            errorMessages.append("Category not found. ");
//        }
//        if (brand == null) {
//            errorMessages.append("Brand not found. ");
//        }
//        if (unit == null) {
//            errorMessages.append("Unit not found. ");
//        }
////        if (inventory == null) {
////            errorMessages.append("Inventory not found. ");
////        }
//        if (entity == null) {
//            errorMessages.append("Entity not found. ");
//        }
//
//        if (errorMessages.length() > 0) {
//            return ResponseEntity.badRequest().body(errorMessages.toString().trim());
//        }
//
//        bulkStock.setItemDescription(item.getDescription());
//        bulkStock.setLocation(location);
//        bulkStock.setAddress(address);
//        bulkStock.setCurrency(currency);
//        bulkStock.setCategory(category);
//        bulkStock.setBrand(brand);
//        bulkStock.setUnit(unit);
////        bulkStock.setInventory( inventory);
//        bulkStock.setEntity(entity);
//
//        bulkStockRepo.save(bulkStock);
//
//        return ResponseEntity.ok("Bulk Stock added successfully");
//    }
@PostMapping("/add")
public ResponseEntity<?> addBulkStock(@RequestBody BulkStockDto incomingStockRequest) {
    BulkStock incomingStock = new BulkStock();
    incomingStock.setQuantity(incomingStockRequest.getQuantity());
    incomingStock.setUnitPrice(incomingStockRequest.getUnitPrice());
    incomingStock.setExtendedValue(incomingStockRequest.getExtendedValue());
    incomingStock.setDate(incomingStockRequest.getDate());
    incomingStock.setPurchaseOrder(incomingStockRequest.getPurchaseOrder());
    incomingStock.setPn(incomingStockRequest.getPn());
    incomingStock.setSn(incomingStockRequest.getSn());
    incomingStock.setPrice(incomingStockRequest.getPrice());
    incomingStock.setRemarks(incomingStockRequest.getRemarks());
    incomingStock.setStandardPrice(incomingStockRequest.getStandardPrice());
//    incomingStock.setStatus(incomingStockRequest.getStatus());
    incomingStock.setImpaCode(incomingStockRequest.getImpaCode());
    incomingStock.setStoreNo(incomingStockRequest.getStoreNo());

    Item item = new Item();
    item.setDescription(incomingStockRequest.getDescription());
    Address address = addressRepository.findByAddress(incomingStockRequest.getAddress());
    Category category=categoryRepository.findByName((incomingStockRequest.getName()));
    Brand  brand =brandRepository.findByBrandName(incomingStockRequest.getBrandName());
    Unit unit=unitRepository.findByUnitName(incomingStockRequest.getUnitName());
    Inventory inventory=inventoryRepo.findAllByQuantity(incomingStockRequest.getQuantity());
    Entity entity=entityRepository.findByEntityName(incomingStockRequest.getEntityName());
    Currency currency = currencyRepository.findTopByCurrencyName((incomingStockRequest.getCurrencyName()));

    String requestedAddress = incomingStockRequest.getAddress();

    // Find the Location using locationName
    Location location = locationRepository.findByLocationName(incomingStockRequest.getLocationName());

    // Check if the location was found
    if (location != null) {
        // Access the list of addresses associated with the location
        List<Address> addresses = location.getAddresses();

        // Check if the provided address belongs to the found location
        boolean addressFound = addresses.stream()
                .anyMatch(addr -> addr.getAddress().equals(requestedAddress));

        // If the address was found for the location
        if (addressFound) {
            // Set all fields for incomingStock
            incomingStock.setItemDescription(item.getDescription());
            incomingStock.setLocation(location);
            incomingStock.setAddress(address);
            incomingStock.setCurrency(currency);
            incomingStock.setCategory(category);
            incomingStock.setBrand(brand);
            incomingStock.setUnit(unit);
            incomingStock.setInventory( inventory);
            incomingStock.setEntity(entity);

            bulkStockRepo.save(incomingStock);

            IncomingStockRequest responseDTO = new IncomingStockRequest();
            responseDTO.setLocationName(location.getLocationName());
            responseDTO.setAddress(requestedAddress);
            responseDTO.setQuantity(incomingStockRequest.getQuantity());
            responseDTO.setUnitPrice(incomingStockRequest.getUnitPrice());
            responseDTO.setExtendedValue(incomingStockRequest.getExtendedValue());
            responseDTO.setDate(incomingStockRequest.getDate());
            responseDTO.setPurchaseOrder(incomingStockRequest.getPurchaseOrder());
            responseDTO.setPn(incomingStockRequest.getPn());
            responseDTO.setSn(incomingStockRequest.getSn());
//            responseDTO.setBlindCount(incomingStockRequest.getBlindCount());
            responseDTO.setPrice(incomingStockRequest.getPrice());
            responseDTO.setName(incomingStockRequest.getName());
            responseDTO.setDescription(incomingStockRequest.getDescription());
            responseDTO.setRemarks(incomingStockRequest.getRemarks());
            responseDTO.setCurrencyName(incomingStockRequest.getCurrencyName());
            responseDTO.setBrandName(incomingStockRequest.getBrandName());
            responseDTO.setUnitName(incomingStockRequest.getUnitName());
            responseDTO.setStandardPrice(incomingStockRequest.getStandardPrice());
//            responseDTO.setStatus(incomingStockRequest.getStatus());
            responseDTO.setImpaCode(incomingStockRequest.getImpaCode());
            responseDTO.setStoreNo(incomingStockRequest.getStoreNo());
            responseDTO.setEntityName(incomingStockRequest.getEntityName());


            // Return the DTO object within ResponseEntity.ok
            return ResponseEntity.ok().body(responseDTO);
        } else {
            // Address not found for the given location
            return ResponseEntity.badRequest().body("Address not found for the specified location.");
        }
    } else {
        // Location not found
        return ResponseEntity.badRequest().body("Location not found.");
    }

    // ... (rest of your existing code)
}
    @GetMapping("/view")
    public ResponseEntity<List<BulkStock>> viewAllBulkstock(HttpSession session) {
        try {
            List<BulkStock> incomingStocks = bulkStockRepo.findAll();

            if (!incomingStocks.isEmpty()) {
                return ResponseEntity.ok(incomingStocks);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<BulkStock> getBulkStockById(@PathVariable Long id) {
        Optional<BulkStock> bulkStock = bulkStockRepo.findById(id);

        if (bulkStock.isPresent()) {
            return ResponseEntity.ok(bulkStock.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateIncomingStock(
            @PathVariable Long id,
            @RequestBody BulkStockDto incomingStockRequest) {

        Optional<BulkStock> optionalIncomingStock = bulkStockRepo.findById(id);

        if (optionalIncomingStock.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Bulk stock with ID " + id + " not found");
        }

        BulkStock incomingStock = optionalIncomingStock.get();

        // Update the fields based on the incoming request
        incomingStock.setQuantity(incomingStockRequest.getQuantity());
        incomingStock.setUnitPrice(incomingStockRequest.getUnitPrice());
        incomingStock.setExtendedValue(incomingStockRequest.getExtendedValue());
        incomingStock.setDate(incomingStockRequest.getDate());
        incomingStock.setPurchaseOrder(incomingStockRequest.getPurchaseOrder());
        incomingStock.setPn(incomingStockRequest.getPn());
        incomingStock.setSn(incomingStockRequest.getSn());
        incomingStock.setPrice(incomingStockRequest.getPrice());
        incomingStock.setRemarks(incomingStockRequest.getRemarks());
        incomingStock.setStandardPrice(incomingStockRequest.getStandardPrice());
//        incomingStock.setStatus(incomingStockRequest.getStatus());
        incomingStock.setImpaCode(incomingStockRequest.getImpaCode());
        incomingStock.setStoreNo(incomingStockRequest.getStoreNo());

        // Fetching associated entities by their names
        Location location = locationRepository.findByLocationName(incomingStockRequest.getLocationName());
        Address address = addressRepository.findByAddress(incomingStockRequest.getAddress());
        Currency currency = currencyRepository.findTopByCurrencyName(incomingStockRequest.getCurrencyName());
        Category category = categoryRepository.findByName(incomingStockRequest.getName());
        Brand brand = brandRepository.findByBrandName(incomingStockRequest.getBrandName());
        Unit unit = unitRepository.findByUnitName(incomingStockRequest.getUnitName());
        Inventory inventory = inventoryRepo.findAllByQuantity(incomingStockRequest.getQuantity());

        Entity entity = entityRepository.findByEntityName(incomingStockRequest.getEntityName());

        StringBuilder errorMessages = new StringBuilder();
        if (location == null) {
            errorMessages.append("Location not found. ");
        }

        if (errorMessages.length() > 0) {
            return ResponseEntity.badRequest().body(errorMessages.toString().trim());
        }

        incomingStock.setItemDescription(incomingStockRequest.getDescription());
        incomingStock.setLocation(location);
        incomingStock.setAddress(address);
        incomingStock.setCurrency(currency);
        incomingStock.setCategory(category);
        incomingStock.setBrand(brand);
        incomingStock.setUnit(unit);
        incomingStock.setInventory(inventory);
        incomingStock.setEntity(entity);

        bulkStockRepo.save(incomingStock);

        return ResponseEntity.ok("Bulk Stock updated successfully");
    }


}
