package com.inventory.project.controller;

import com.inventory.project.model.*;
import com.inventory.project.repository.CategoryRepository;
import com.inventory.project.repository.ItemRepository;
import com.inventory.project.repository.LocationRepository;
import com.inventory.project.repository.UnitRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/item")
@RestController
@CrossOrigin("*")
public class ItemController {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UnitRepository unitRepository;

    @GetMapping("/add")
    public ResponseEntity<Map<String, Object>> add() {
        Map<String, Object> response = new HashMap<>();
        response.put("item", new Item());
        response.put("categoryList", categoryRepository.findAll());
        response.put("unitList", unitRepository.findAll());
        return ResponseEntity.ok(response);
    }
//    @PostMapping("/add")
//    public ResponseEntity<Map<String, Object>> addItem(@RequestBody Item item) {
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            String name = item.getName();
//            String unitName = item.getUnitName();
//
//            Unit unit = unitRepository.findByUnitName(unitName);
//            if (unit == null) {
//                response.put("error", "Unit Name not found");
//                return ResponseEntity.badRequest().body(response);
//            }
//
//            item.setUnitName(unit.getUnitName());
//
//            Item savedItem = itemRepository.save(item);
//
//            response.put("success", "Item added successfully");
//            response.put("item", savedItem);
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            response.put("error", "Error adding Item: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
@PostMapping("/add")
public ResponseEntity<Map<String, Object>> addItem(@RequestBody Item itemRequest) {
    Map<String, Object> response = new HashMap<>();

    try {
        Item item = new Item();
        item.setItemName(itemRequest.getItemName());
        item.setMinimumStock(itemRequest.getMinimumStock());
        item.setDescription(itemRequest.getDescription());

        Category category = categoryRepository.findByName(itemRequest.getName());
        if (category != null) {
            item.setCategory(category);
            item.setName(category.getName()); // Set name from the fetched category
        } else {
            // Handle category not found error
            response.put("error", "Category not found for name: " + itemRequest.getName());
            return ResponseEntity.badRequest().body(response);
        }

        // Similarly, fetch and set the Unit
        Unit unit = unitRepository.findByUnitName(itemRequest.getUnitName());
        if (unit != null) {
            item.setUnit(unit);
            item.setUnitName(unit.getUnitName()); // Set unitName from the fetched unit
        } else {
            // Handle unit not found error
            response.put("error", "Unit not found for name: " + itemRequest.getUnitName());
            return ResponseEntity.badRequest().body(response);
        }

        Item savedItem = itemRepository.save(item);

        response.put("success", "Item added successfully");
        response.put("item", savedItem);

        return ResponseEntity.ok(response);
    } catch (Exception e) {
        response.put("error", "Error adding Item: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

//    @PostMapping("/add")
//    public ResponseEntity<Map<String, Object>> addItem(@RequestBody Item itemRequest) {
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            Item item = new Item();
//            item.setItemName(itemRequest.getItemName());
//            item.setMinimumStock(itemRequest.getMinimumStock());
//            item.setDescription(itemRequest.getDescription());
//
//            Category category = categoryRepository.findByName(itemRequest.getName());
//            if (category != null) {
//                item.setCategory(category);
//                item.setName(category.getName()); // Set name from the fetched category
//            } else {
//            }
//
//            // Similarly, fetch and set the Unit
//            Unit unit = unitRepository.findByUnitName(itemRequest.getUnitName());
//            if (unit != null) {
//                item.setUnit(unit);
//                item.setUnitName(unit.getUnitName()); // Set unitName from the fetched unit
//            } else {
//                // Handle unit not found error
//            }
//
//            Item savedItem = itemRepository.save(item);
//
//            response.put("success", "Item added successfully");
//            response.put("item", savedItem);
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            response.put("error", "Error adding Item: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }


    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getItemById(@PathVariable Long id) {
        try {

            Item item = itemRepository.findById(id).orElse(null);

            if (item != null) {
                return ResponseEntity.ok(item);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching item");
        }
    }

    private void createInventories(Item item) {


    }
//    @PostMapping("/save")
//    public ResponseEntity<String> save(@RequestBody Item item) {
//        try {
//            Item savedItem = itemRepository.save(item);
//            if (savedItem != null) {
//                return ResponseEntity.status(HttpStatus.CREATED).body("Item saved successfully");
//            }
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save item");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving item");
//        }
//    }



    @GetMapping("/view")
    public ResponseEntity<List<Item>> viewAllItems(HttpSession session) {
        try {
            List<Item> items = itemRepository.findAll();

            if (!items.isEmpty()) {
                return ResponseEntity.ok(items);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateItem(@PathVariable("id") Long id, @RequestBody Item itemDetails) {
        try {
            Item item = itemRepository.findById(id).orElse(null);
            if (item != null) {
                if (itemDetails.getItemName() != null) {
                    item.setItemName(itemDetails.getItemName());
                }
                if (itemDetails.getMinimumStock() != null) {
                    item.setMinimumStock(itemDetails.getMinimumStock());
                }
                if (itemDetails.getDescription() != null) {
                    item.setDescription(itemDetails.getDescription());
                }
                if (itemDetails.getUnit() != null) {
                    // Retain the existing unit mapping without updating its name
                    item.setUnit(item.getUnit());
                }
                if (itemDetails.getCategory() != null) {
                    Category category = categoryRepository.findByName(itemDetails.getCategory().getName());
                    if (category != null) {
                        // If the category exists, update the item's category
                        item.setCategory(category);
                        // Update the name of the category if necessary
                        if (!category.getName().equals(item.getName())) {
                            item.setName(category.getName());
                        }
                    } else {
                        // Handle category not found error
                    }
                }

                Item updatedItem = itemRepository.save(item);
                if (updatedItem != null) {
                    return ResponseEntity.status(HttpStatus.OK).body("Item updated successfully");
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating item");
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable(value = "id") Long id) {
        try {
            itemRepository.deleteById(id);
            return ResponseEntity.ok("Item deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting item");
        }
    }



}

