package com.ecommerce.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.util.List;
import java.util.UUID;

public class OrderRequestDTO {
    @NotEmpty(message = "Lista de itens não pode estar vazia")
    private List<OrderItemRequestDTO> items;
    
    public List<OrderItemRequestDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequestDTO> items) {
        this.items = items;
    }
   
    public static class OrderItemRequestDTO {
        @NotNull(message = "ID do produto é obrigatório")
        private UUID productId;
        
        @NotNull(message = "Quantidade é obrigatória")
        @Min(value = 1, message = "Quantidade deve ser pelo menos 1")
        private Integer quantity;

        public UUID getProductId() {
            return productId;
        }

        public void setProductId(UUID productId) {
            this.productId = productId;
        }
        
        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
