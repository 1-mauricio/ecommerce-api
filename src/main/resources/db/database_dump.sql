-- Criação do banco de dados
CREATE DATABASE IF NOT EXISTS ecommerce_db;
USE ecommerce_db;

-- Tabela de usuários
CREATE TABLE users (
    id CHAR(36) PRIMARY KEY NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabela de produtos
CREATE TABLE products (
    id CHAR(36) PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(255) NOT NULL,
    stock_quantity INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category (category),
    INDEX idx_name (name)
);

-- Tabela de pedidos
CREATE TABLE orders (
    id CHAR(36) PRIMARY KEY NOT NULL,
    user_id CHAR(36) NOT NULL,
    status ENUM('PENDING', 'PAID', 'CANCELLED', 'SHIPPED', 'DELIVERED') NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
);

-- Tabela de itens do pedido
CREATE TABLE order_items (
    id CHAR(36) PRIMARY KEY NOT NULL,
    order_id CHAR(36) NOT NULL,
    product_id CHAR(36) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
);

-- Dados de exemplo
INSERT INTO users (id, email, password, name, role) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'admin@ecommerce.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin User', 'ADMIN'),
('550e8400-e29b-41d4-a716-446655440001', 'user@ecommerce.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Regular User', 'USER'),
('550e8400-e29b-41d4-a716-446655440002', 'cliente@ecommerce.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Cliente Teste', 'USER');

INSERT INTO products (id, name, description, price, category, stock_quantity) VALUES
('660e8400-e29b-41d4-a716-446655440000', 'Smartphone Samsung Galaxy S23', 'Smartphone avançado com tela AMOLED', 2999.99, 'Eletrônicos', 50),
('660e8400-e29b-41d4-a716-446655440001', 'Notebook Dell Inspiron 15', 'Notebook para uso profissional', 2499.99, 'Eletrônicos', 30),
('660e8400-e29b-41d4-a716-446655440002', 'Camiseta Nike Dri-FIT', 'Camiseta esportiva respirável', 89.99, 'Roupas', 100),
('660e8400-e29b-41d4-a716-446655440003', 'Tênis Adidas Ultraboost', 'Tênis de corrida confortável', 499.99, 'Calçados', 75),
('660e8400-e29b-41d4-a716-446655440004', 'Livro Spring Boot em Ação', 'Guia completo para Spring Boot', 79.99, 'Livros', 200);

-- Dados de exemplo para pedidos (opcional)
INSERT INTO orders (id, user_id, status, total_amount) VALUES
('770e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440001', 'PAID', 3089.98),
('770e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', 'PAID', 589.98);

INSERT INTO order_items (id, order_id, product_id, quantity, unit_price, total_price) VALUES
('880e8400-e29b-41d4-a716-446655440000', '770e8400-e29b-41d4-a716-446655440000', '660e8400-e29b-41d4-a716-446655440000', 1, 2999.99, 2999.99),
('880e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440000', '660e8400-e29b-41d4-a716-446655440002', 1, 89.99, 89.99),
('880e8400-e29b-41d4-a716-446655440002', '770e8400-e29b-41d4-a716-446655440001', '660e8400-e29b-41d4-a716-446655440003', 1, 499.99, 499.99),
('880e8400-e29b-41d4-a716-446655440003', '770e8400-e29b-41d4-a716-446655440001', '660e8400-e29b-41d4-a716-446655440002', 1, 89.99, 89.99);
